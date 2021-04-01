package com.music.chords.activity;


import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import logs.TraceLog;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.bewo.mach.MACHPrinter;
import com.music.chords.database.CreateDB;
import com.music.chords.database.DBCartItems;
import com.music.chords.database.DBCrustDetails;
import com.music.chords.database.DBDominosMenu;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
import android.telephony.TelephonyManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;

public class SplashActivity extends Activity 
{
	private static int SPLASH_TIME_OUT = 5000;
	String imei;
	String result;

	DBDominosMenu dbMenu;
	CreateDB dbCreate;
	DBCrustDetails dbCrust;
	DBCartItems dbCart;

	//	public static String NAMESPACE ="http://tempuri.org/";
	public static String NAMESPACE;
	public static String URL;
	//	public final static String URL = "http://192.168.1.3:8091/Service1.svc?wsdl";
	//	String METHOD_NAME = "Tab_Authentication";
	//	String SOAP_ACTION = NAMESPACE + "IService1/"+ METHOD_NAME;
	String METHOD_NAME ;
	String SOAP_ACTION ;

	Boolean isInternetPresent = false;

	private ProgressDialog pDialog;

	String file_Path;
	Properties pp;
	String port;
	String ip;

	PeripheralConnect peripheralConnect = null;

	ProgressDialog progressDialog;
	
	TraceLog tc = new TraceLog();


	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		dbCart = new DBCartItems(this);

		if( Build.VERSION.SDK_INT >= 9)
		{
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy); 
		}

		file_Path=Environment.getExternalStorageDirectory()+ "/Android/data/forbes.dominos/";
		//
		File conf=new File(file_Path + "config.properties");
		if(!conf.exists()) 
		{
			copyFiles();
		}

		peripheralConnect = PeripheralConnect.getInstance(getApplicationContext());
		peripheralConnect.USB_API_Initialization();
		
		
		new CountDownTimer(5000, 1000) 
		{ 
	        public void onTick(long millisUntilFinished) 
	        {
	        	
	        }
	        public void onFinish() 
	        {
	        	checkPaper();
	        }
	     }.start(); 

		Cursor rss = dbCart.getData();
		int countDBRows = rss.getCount();
		int countDBColumns = rss.getColumnCount();

		if (countDBRows > 0) 
		{
			dbCart.deleteAllTableData();
			StaticMenu.count = 0;

			tc.WriteToTransactionLog("SplashActivity : countDBRows :- " + "Cart Data Deleted");
		}

		//		Calendar c = Calendar.getInstance();
		//		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		//		String formattedTime = sdf.format(c.getTime());
		//		
		////		String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
		//		

		ReadConfig rc = new ReadConfig();

		readConfigFileDetails();

		// Show the Up button in the action bar.
		//		ActionBar bar = getActionBar();
		//		bar.hide();

		//		dbCart = new DBCartItems(this);
		//		dbMenu = new DBItemPrice(this);
		//		dbOrder = new DBOrderItems(this);

		dbCreate = new CreateDB(this);
		dbMenu = new DBDominosMenu(this);
		dbCrust = new DBCrustDetails(this);

		TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		imei = telephonyManager.getDeviceId();
		//		Toast.makeText(getApplicationContext(), "" + imei , Toast.LENGTH_SHORT).show();

		System.out.println("################## : " + imei);

		checkNet();
		
	}

	public void onStart() 
	{
		super.onStart();
//		checkPaper();
	}

	public void checkNet()
	{
		boolean chknet=isNetworkOnline();

		if(chknet)
		{	
//			progressDialog = ProgressDialog.show(SplashActivity.this, "", "Please Wait...");
			
			validateTab();
			
//			result = "authenticated";

			//		result = "menu##Pizza#ChizBust#Regular#450#Thane#Veg##" +
			//				"Pizza#PepiPanner#Small#200#Thane#NonVeg##" +
			//				"Coke#Pepsi#Regular#30#Thane#Veg$$";

			System.out.println("Result : " + result);
			
			if(result == null ||result.equalsIgnoreCase("Server Connection Lost"))
			{
				Toast.makeText(getApplicationContext(), "Server Connection Lost, Please Try After Sometime" , Toast.LENGTH_SHORT).show();
				tc.WriteToTransactionLog("SplashActivity : result :- " + result);

				new Handler().postDelayed(new Runnable() 
				{
					@Override
					public void run() 
					{
						System.exit(0);
					}
				}, SPLASH_TIME_OUT);
			}
			else
			{
				if(result.equalsIgnoreCase("unsuccessful"))
				{
					Toast.makeText(getApplicationContext(), "Not A Valid Tab, Please Register The Tab First" , Toast.LENGTH_LONG).show();
					tc.WriteToTransactionLog("SplashActivity : result :- " + result);

					new Handler().postDelayed(new Runnable() 
					{
						@Override
						public void run() 
						{
							System.exit(0);
						}
					}, SPLASH_TIME_OUT);
				}
				else if(result.equalsIgnoreCase("authenticated"))
				{	
					Cursor rss = dbMenu.getData();
					int rows = rss.getCount();
					rss.close();

					if(rows > 0)
					{
						tc.WriteToTransactionLog("SplashActivity : result :- " + "authenticated but db rows>0");

						new Handler().postDelayed(new Runnable() 
						{
							@Override
							public void run() 
							{
								Intent i = new Intent(SplashActivity.this, MenuHome.class);
								startActivity(i);
								overridePendingTransition( R.anim.push_up_in, R.anim.push_up_out );
								finish();
							}
						}, SPLASH_TIME_OUT);

					}
					else
					{
						Toast.makeText(getApplicationContext(), "No Menu Data Found, Activate Menu From Server", Toast.LENGTH_LONG).show();
						tc.WriteToTransactionLog("SplashActivity : result :- " + "authenticated but db rows<0");

						new Handler().postDelayed(new Runnable() 
						{
							@Override
							public void run() 
							{
								System.exit(0);
							}
						}, SPLASH_TIME_OUT);
					}
				}
				else
				{
					System.out.println("******** result : " + result);

					String check = result.substring(result.length()-2);
					//				Toast.makeText(getApplicationContext(), "check " + check, Toast.LENGTH_LONG).show();

					if(check.equalsIgnoreCase("$$"))
					{
						tc.WriteToTransactionLog("SplashActivity : result :- " + "Downloaded Data Is Complete As String Contails $$");

						// to remove menu##...
						result = result.substring(6, result.length()-2);
						System.out.println("******** result : " + result);

						String updateDB = insertPriceToDB();


						if(updateDB.equalsIgnoreCase("success"))
						{
							acknowledgeDownload();
						}
						else
						{
							tc.WriteToTransactionLog("SplashActivity : acknowledgeDownload :- " + "Error Downloading Data");
							Toast.makeText(getApplicationContext(), "Error In Downloading Data, Please Download Data Manully From Admin Module" , Toast.LENGTH_LONG).show();
						}

						new Handler().postDelayed(new Runnable() 
						{
							@Override
							public void run() 
							{
								tc.WriteToTransactionLog("SplashActivity : Intent :- " + "MenuHome");

								Intent i = new Intent(SplashActivity.this, MenuHome.class);
								startActivity(i);
								overridePendingTransition( R.anim.push_up_in, R.anim.push_up_out );
								finish();
								//								Toast.makeText(getApplicationContext(), "Success" , Toast.LENGTH_LONG).show();
							}
						}, SPLASH_TIME_OUT);
					}
					else
					{
						Toast.makeText(getApplicationContext(), "Error In Downloading Data, Please Download Data Manully From Admin Module" , Toast.LENGTH_LONG).show();
					}
				}
			}
			
//			new Thread() 
//			{
//				public void run() 
//				{
//					try
//					{
//						sleep(500);
//					} 
//					catch(Exception e) 
//					{
//						tc.WriteToTransactionLog("Exception: MainActivity_p new Thread():- "+e.toString());
//					}
//					// dismiss the progress dialog
////							progressDialog.dismiss();
//					if (progressDialog != null) 
//				    {
//				    	progressDialog.dismiss();
//				    	progressDialog = null;
//				    }
//				}
//			}.start();
		}
		else
		{
			tc.WriteToTransactionLog("SplashActivity : chknet :- " + "No Internate Connection");

			RetryDialog rd = new RetryDialog(SplashActivity.this);
			rd.show();
			rd.setCancelable(false);
		}  


		//				getCategoryForMenu();
	}

	public String validateTab()
	{
		try 
		{
			SoapSerializationEnvelope envelope;
			SoapObject request;

			METHOD_NAME = "Tab_Authentication";
			SOAP_ACTION = NAMESPACE + "IService1/"+ METHOD_NAME;

			request = new SoapObject(NAMESPACE, METHOD_NAME);
			PropertyInfo custidProp =new PropertyInfo();
			custidProp.setName("TabNo1");
			custidProp.setValue(imei);
			custidProp.setType(String.class);
			request.addProperty(custidProp);

			//			PropertyInfo numProp =new PropertyInfo();
			//			numProp.setName("TabNo2");
			//			numProp.setValue("1234");
			//			numProp.setType(String.class);
			//			request.addProperty(numProp);

			envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);					
			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

			androidHttpTransport.call(SOAP_ACTION, envelope);				
			SoapPrimitive response = (SoapPrimitive)envelope.getResponse();

			result=response.toString();
			System.out.println("########## result : " + result);
			//			Toast.makeText(getApplicationContext(), "result : " +result , Toast.LENGTH_LONG).show();

			return result;

		} 
		catch(Exception e)
		{
			e.printStackTrace();
			tc.WriteToTransactionLog("SplashActivity : validateTab() :- " + e.toString());
			return "Server Connection Lost";
		}
	}

	public void acknowledgeDownload()
	{
		try 
		{

			SoapSerializationEnvelope envelope1;
			SoapObject request1;

			String parameter1 = "success";
			METHOD_NAME = "Acknowldgement";
			SOAP_ACTION = NAMESPACE + "IService1/"+ METHOD_NAME;

			request1 = new SoapObject(NAMESPACE, METHOD_NAME);
			PropertyInfo custidProp =new PropertyInfo();
			custidProp.setName("Result");
			custidProp.setValue(parameter1);
			custidProp.setType(String.class);
			request1.addProperty(custidProp);

			PropertyInfo numProp =new PropertyInfo();
			numProp.setName("TabId");
			numProp.setValue(imei);
			numProp.setType(String.class);
			request1.addProperty(numProp);

			envelope1 = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope1.dotNet = true;
			envelope1.setOutputSoapObject(request1);					
			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

			androidHttpTransport.call(SOAP_ACTION, envelope1);				
			SoapPrimitive response = (SoapPrimitive)envelope1.getResponse();

			//				result=response.toString();
			//				System.out.println("########## result : " + result);
			//				Toast.makeText(getApplicationContext(), "result : " +result , Toast.LENGTH_LONG).show();
			//
			//				return result;
			tc.WriteToTransactionLog("SplashActivity : acknowledgeDownload :- " + "Ack send successfull");
		} 
		catch(Exception e)
		{
			//			Toast.makeText(getApplicationContext(), "result : " +result , Toast.LENGTH_LONG).show();
			//			Toast.makeText(getApplicationContext(), "Exception e : " +e , Toast.LENGTH_LONG).show();
			e.printStackTrace();
			tc.WriteToTransactionLog("SplashActivity : acknowledgeDownload :- " + e.toString());
			//			tc.WriteToTransactionLog(this.getClass() + " - loginGranted() : Exception : " + e.toString());
			//			tc.WriteToTransactionLog("Exception: Registration.java loginGranted():- "+e.toString());	
		}
	}

	public String insertPriceToDB()
	{

		try
		{
			dbMenu.deleteAllTableData();

			String[] rowArray = result.split("##");

			//			String[] firstItem = rowArray[0].split("#");
			//			
			//			Toast.makeText(getApplicationContext(), "firstItem : " + firstItem[0] , Toast.LENGTH_SHORT).show();

			for(int i=0; i<rowArray.length; i++)
			{
				System.out.println("########## strArray : " + rowArray[i]);
				//			Toast.makeText(getApplicationContext(), "strArray : " + rowArray[i] , Toast.LENGTH_SHORT).show();

				String[] colArray = rowArray[i].split("#");

				System.out.println("########## colArray[i]rray : " + colArray[0]);

				String itemName = colArray[0];
				String category = colArray[1];
				String crust = colArray[2];
				String size = colArray[3];
				double price = Double.parseDouble(colArray[4]);

				//				Toast.makeText(getApplicationContext(), "price : " + price , Toast.LENGTH_SHORT).show();

				String tax = colArray[5];

				if(dbMenu.insertData(itemName, category, crust, size, price, tax));
				{
					int element;		

					Cursor rss = dbMenu.getData();
					int countDBRows = rss.getCount();
					int countDBColumns = rss.getColumnCount();

					//						String[][] dbData = new String[countDBRows][countDBColumns];

					rss.moveToFirst();

					for(element = 0; element < countDBRows; element++)
					{			
						String item, itemCat, itemCrust, itemSize, itemTax;
						int amt;

						item = rss.getString(0);
						itemCat = rss.getString(1);
						itemCrust = rss.getString(2);
						itemSize = rss.getString(3);
						amt = rss.getInt(4);
						itemTax = rss.getString(5);

						rss.moveToNext();

						//						String meduData = item + " |" + itemCat + " |" + itemCrust 
						//								+ " |" + itemSize + " |" + amt + " |" + itemTax;
						//						Toast.makeText(getApplicationContext(), "meduData : " + meduData, Toast.LENGTH_SHORT).show();

					}
					rss.close();
				}
			}			
			tc.WriteToTransactionLog("SplashActivity : insertPriceToDB :- " + "Data completely inserted to DB");

			insertCrustToDB();

			return "success";
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			tc.WriteToTransactionLog("SplashActivity : insertPriceToDB :- " + e.toString());

			//				tc.WriteToTransactionLog(this.getClass() + " - copyFiles() : Exception : " + e.toString());
			return "s";
		} 
	}

	public void insertCrustToDB()
	{
		
//		#######################################################
		
		String pizzaCategory = "Veg";
		
		dbCrust.deleteAllTableData();

		int element;		

		Cursor rss = dbMenu.getDistinctCrust(pizzaCategory);
		int countDBRows = rss.getCount();

		rss.moveToFirst();

		for(element = 0; element < countDBRows; element++)
		{			
			String itemCrust, itemSize;

			itemCrust = rss.getString(0);
			itemSize = rss.getString(1);


			dbCrust.insertData(itemCrust, itemSize);
			{
				Cursor cr = dbCrust.getData();
				int counRows = cr.getCount();

				cr.moveToFirst();

				for(int i = 0; i < counRows; i++)
				{			
					String crust, size;

					crust = cr.getString(0);
					size = cr.getString(1);

					cr.moveToNext();

					//					String crustData =  crust + " | " + size;
					//					Toast.makeText(getApplicationContext(), "crustData : " + i + " " + crustData, Toast.LENGTH_SHORT).show();
				}
				cr.close();
			}
			rss.moveToNext();

			//						String meduData = item + " |" + itemCat + " |" + itemCrust 
			//								+ " |" + itemSize + " |" + amt + " |" + itemTax;
			//						Toast.makeText(getApplicationContext(), "finalData : " + meduData, Toast.LENGTH_SHORT).show();

		}
		rss.close();
		tc.WriteToTransactionLog("SplashActivity : insertCrustToDB :- " + "Cust completely inserted to DB");
	}

	//Checking for Data Connection 
	public boolean isNetworkOnline() 
	{
		boolean status=false;
		try
		{
			ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cm.getNetworkInfo(0);
			if (netInfo != null && netInfo.getState()==NetworkInfo.State.CONNECTED) 
			{
				status= true;
			}
			else 
			{
				netInfo = cm.getNetworkInfo(1);
				if(netInfo!=null && netInfo.getState()==NetworkInfo.State.CONNECTED)
					status= true;
			}
		}
		catch(Exception e)
		{
			//				tc.WriteToTransactionLog("Exception: MainActivity isNetworkOnline():- "+e.toString());
			return false;
		}
		return status;
	}

	public void checkPaper()
	{
		try 
		{	
			if (PeripheralConnect.machServices.isMachActive())
			{
				byte printer_status = PeripheralConnect.machServices.check_printer_status();
				
				switch (printer_status) 
				{
				case MACHPrinter.BAT_LOW_ERROR:
					Toast.makeText(getApplicationContext(),"Battery Is Low, Please Charge The Device First ",
							Toast.LENGTH_LONG).show();
					break;
				
				case MACHPrinter.THERMALPRINTER_NO_PLATEN:
					Toast.makeText(getApplicationContext(), "No Platen ",
							Toast.LENGTH_LONG).show();
					break;
					
				case MACHPrinter.THERMALPRINTER_NO_PAPER:
					Toast.makeText(getApplicationContext(),"No Paper Found, Please Insert The Paper First ",
							Toast.LENGTH_LONG).show();
					break;
				
				case MACHPrinter.THERMALPRINTER_NO_PRINTER:
					Toast.makeText(getApplicationContext(),"No Printer Module ",
							Toast.LENGTH_LONG).show();
					break;
				
				case MACHPrinter.THERMALPRINTER_STATUS_OK:
//					Toast.makeText(getApplicationContext(),"Printer Status : OK ", Toast.LENGTH_LONG).show();
					break;
				
				case MACHPrinter.THERMALPRINTER_TIMEOUT_APP:
					Toast.makeText(getApplicationContext(),"Timeout From The Application ",
							Toast.LENGTH_LONG).show();
					break;

				default:
					Toast.makeText(getApplicationContext()," " + printer_status,
							Toast.LENGTH_LONG).show();
					break;
				}
			} 
			else 
			{
				Toast.makeText(getApplicationContext(),
						"The Device is not active. Did you connect to Debugging mode with PC.If So please disconnect",
						Toast.LENGTH_LONG).show();
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			// Toast.makeText(getApplicationContext(),"Exception : " + e,
			// Toast.LENGTH_SHORT).show();
		}
	}

	//	//Copy Property Files from Asset Folder to SD card on startup
	//	public void copyFiles()
	//	{
	//		String ALGORITHM = "AES";
	//		String TRANSFORMATION = "AES";
	//		String key = "The Thirsty Crow";
	//
	//		try 
	//		{
	//			File dataDirectory = new File(file_Path);
	//			// have the object build the directory structure, if needed.
	//			if(!dataDirectory.exists())
	//				dataDirectory.mkdirs();
	//
	//			AssetManager am = getAssets();
	//			String[] list = am.list("");
	//			for (String s:list) 
	//			{
	//				if (s.startsWith("config")) 
	//				{
	//					InputStream inStream = am.open(s);
	//					int size = inStream.available();
	//					byte[] buffer = new byte[size];
	//
	//					Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
	//					Cipher cipher = Cipher.getInstance(TRANSFORMATION);
	//					cipher.init(Cipher.ENCRYPT_MODE, secretKey);
	//
	//					inStream.read(buffer);
	//
	//					byte[] outputBytes = cipher.doFinal(buffer);
	//
	//					inStream.close();
	//					FileOutputStream fos = new FileOutputStream(file_Path + s);
	//					fos.write(outputBytes);
	//					fos.close();
	//				}
	//			}
	//		}
	//		catch (Exception e) 
	//		{
	//			e.printStackTrace();
	//			//				tc.WriteToTransactionLog(this.getClass() + " - copyFiles() : Exception : " + e.toString());
	//		} 
	//	}


	public void copyFiles()
	{
		try 
		{
			File dataDirectory = new File(file_Path);
			// have the object build the directory structure, if needed.
			if(!dataDirectory.exists())
				dataDirectory.mkdirs();

			AssetManager am = getAssets();
			String[] list = am.list("");
			for (String s:list) 
			{
				if (s.startsWith("config")) 
				{
					InputStream inStream = am.open(s);
					int size = inStream.available();
					byte[] buffer = new byte[size];
					inStream.read(buffer);
					inStream.close();
					FileOutputStream fos = new FileOutputStream(file_Path + s);
					fos.write(buffer);
					fos.close();
				}
			}
		}
		catch (FileNotFoundException ex) 
		{
			//			tc.WriteToTransactionLog("Exception: MainActivity copyFiles():- "+ex.toString());
			System.exit(0);
		} 
		catch (IOException e) 
		{
			//			tc.WriteToTransactionLog("Exception: MainActivity copyFiles():- "+e.toString());
		}
	}


	public void readConfigFileDetails()
	{
		try
		{
			ip=ReadConfig.ip;
			port=ReadConfig.port;

			NAMESPACE = ReadConfig.NAMESPACE;
			URL=ReadConfig.URL;

			//			Toast.makeText(getApplicationContext(), "ip : " + ip, Toast.LENGTH_SHORT).show();
			//			Toast.makeText(getApplicationContext(), "port : " + port, Toast.LENGTH_SHORT).show();
			//			Toast.makeText(getApplicationContext(), "NAMESPACE : " + NAMESPACE, Toast.LENGTH_SHORT).show();
			//			Toast.makeText(getApplicationContext(), "URL : " + URL, Toast.LENGTH_SHORT).show();


		}
		catch(Exception e)
		{
			//			tc.WriteToTransactionLog("Exception: ChangePin.java readConfigFileDetails():- "+e.toString());
			e.printStackTrace();
			//			tc.WriteToTransactionLog(this.getClass() + " - readConfigFileDetails() : Exception : " + e.toString());
		}		
	}

	//	@Override
	//	public boolean onCreateOptionsMenu(Menu menu) 
	//	{
	//		// Inflate the menu; this adds items to the action bar if it is present.
	//		getMenuInflater().inflate(R.menu.splash_activity, menu);
	//		return true;
	//	}
	//
	//	@Override
	//	public boolean onOptionsItemSelected(MenuItem item) 
	//	{
	//		switch (item.getItemId()) 
	//		{
	//		case R.id.exit:
	//			System.exit(0);
	//			finish();
	//			return true;
	//		}
	//		return super.onOptionsItemSelected(item);
	//	}
	//
	@Override
	public void onBackPressed()
	{	

	}

	@Override
	public void onPause(){

		super.onPause();
		if(pDialog != null)
			pDialog.dismiss();
	}

	public class RetryDialog extends Dialog implements
	android.view.View.OnClickListener 
	{
		public Activity c;
		public Dialog d;
		public Button yes, no;

		public RetryDialog(Activity a) 
		{
			super(a);
			this.c = a;
		}

		@Override
		protected void onCreate(Bundle savedInstanceState) 
		{
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.activity_retry);

			yes = (Button) findViewById(R.id.btn_yesRetry);
			no = (Button) findViewById(R.id.btn_exit);

			yes.setOnClickListener(this);
			no.setOnClickListener(this);	
		}

		@Override
		public void onClick(View v)
		{
			try
			{
				switch (v.getId()) 
				{
				case R.id.btn_yesRetry:
					dismiss();
					checkNet();
					break;	

				case R.id.btn_exit:
					dismiss();
					System.exit(0);
					finish();
					break;

				default:
					break;
				}
				dismiss();
			}		
			catch(Exception e) 
			{
				e.printStackTrace();
				//				tc.WriteToTransactionLog(this.getClass() + " : ConfirmPDF - onClick() : Exception : " + e.toString());
				//				tc.WriteToTransactionLog("Exception: AccountStatement.java/ConfirmPDF-onClick():- "+e.toString());
			}
		}
	}

}