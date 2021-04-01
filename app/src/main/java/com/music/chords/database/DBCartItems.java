package com.music.chords.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

//  TO CREATE TRANSACTION TABLE AND USE TRANSACTION RELATED QUERIES	

public class DBCartItems extends CreateDB {

	// private static final String DATABASE_NAME = "dbPassbook";
	// private static final int DATABASE_VERSION = 1;

	private static final String TABLE_ORDER_CART = "OrderCart";
	// private static final String TABLE_ORDER_CART_CATMAN = "CategoryManager";
	// private static final String TABLE_ORDER_CART_CATMASTER =
	// "CategoryMaster";
	// private static final String TABLE_ORDER_CART_CUSTOMER = "Customer";

	private static final String KEY_ITEM_NAME = "ItemName";
	private static final String KEY_PRICE = "Price";
	private static final String KEY_CRUST = "Crust";
	private static final String KEY_SIZE = "ItemSize";
	private static final String KEY_QUANTITY = "ItemQuantity";
	private static final String KEY_VAT = "Vat";
	private static final String KEY_SERVICE_TAX = "ServiceTax";
	//	private static final String KEY_TAX = "ServiceTax";

	// private static final String KEY_IMAGE = "Image";

	public DBCartItems(Context context)
	{
		super(context);
	}

	// private static final String KEY_INITIAL = "Initial";

	// public DBAccountStatement(Context context)
	// {
	// super(context, DATABASE_NAME, null, DATABASE_VERSION);
	// }
	//
	// @Override
	// public void onCreate(SQLiteDatabase db)
	// {
	// //
	// database.execSQL("CREATE TABLE IF NOT EXISTS passbookData(id INTEGER PRIMARY KEY, date TEXT,"
	// +
	// //
	// " particular TEXT, withdrawal TEXT, deposit TEXT, balance TEXT, initial TEXT);");
	//
	// String CREATE_PASSBOOK_TABLE = "CREATE TABLE IF NOT EXISTS " +
	// TABLE_ORDER_CART
	// + "(" + KEY_ID + " TEXT PRIMARY KEY," + KEY_ITEM + " TEXT,"
	// + KEY_PRICE + " TEXT," + KEY_TRANS_TYPE + " TEXT,"
	// + KEY_TRANS_SUB_TYPE + " TEXT," + KEY_DEBIT_CREDIT + " TEXT,"
	// + KEY_TRANS_VALUE_DATE + " TEXT," + KEY_DEBIT + " TEXT,"
	// + KEY_CREDIT + " TEXT," + KEY_PARTICULAR + " TEXT,"
	// + KEY_TRANS_POST_DATE + " TEXT," + KEY_INTRUMENT_NO + " TEXT,"
	// + KEY_BALANCE + " TEXT," + KEY_SUB_CATEGORY + " TEXT,"
	// + KEY_NOTES + " TEXT" + ")";
	//
	// db.execSQL(CREATE_PASSBOOK_TABLE);
	// }



	public boolean insertData(String cItems, String cPrice, String cCrust, String cSize,
			Integer cQuantity,String cVat, String cTax)
	{
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues contentValues = new ContentValues();

			// contentValues.put(KEY_ID, cId);
			contentValues.put(KEY_ITEM_NAME, cItems);
			contentValues.put(KEY_PRICE, cPrice);
			contentValues.put(KEY_CRUST, cCrust);
			contentValues.put(KEY_SIZE, cSize);
			contentValues.put(KEY_QUANTITY, cQuantity);
			//			contentValues.put(KEY_TAX, cTax);
			contentValues.put(KEY_VAT, cVat);
			contentValues.put(KEY_SERVICE_TAX, cTax);


			db.insert(TABLE_ORDER_CART, null, contentValues);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public boolean deleteData(String cItems, String cCrust, String cSize) 
	{
		try 
		{
			SQLiteDatabase db = getWritableDatabase();

			db.execSQL("DELETE from " + TABLE_ORDER_CART + " Where " + KEY_ITEM_NAME + " = '" + cItems 
					+ "' And " + KEY_CRUST + " = '" + cCrust + "' And " + KEY_SIZE + " = '" + cSize 
					//					+ "' And " + KEY_ITEM_POSITION + " = '" + cPosition 
					+ "'");

			db.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return true;
	}

	public Cursor selectRow(String cItems, String cCrust, String cSize) 
	{
		Cursor res = null;

		SQLiteDatabase db = getWritableDatabase();

		res = db.rawQuery("SELECT * from " + TABLE_ORDER_CART + " Where " + KEY_ITEM_NAME + " = '" + cItems 
				+ "' And " + KEY_CRUST + " = '" + cCrust + "' And " + KEY_SIZE + " = '" + cSize 
				//					+ "' And " + KEY_ITEM_POSITION + " = '" + cPosition 
				+ "'",null);

		return res;
	}

	public Cursor getData() 
	{
		Cursor res = null;
		SQLiteDatabase db = this.getReadableDatabase();
		res = db.rawQuery("SELECT * from " + TABLE_ORDER_CART, null);
		return res;
	}

	public Cursor getSubTotal()
	{
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor res = db.rawQuery("select sum(" + KEY_VAT + ") from " + TABLE_ORDER_CART 
				//				+ " where "
				//				+ KEY_TAX + " = 'True' "
				, null);
		return res;
	}

	public Cursor getTotalVat()
	{
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor res = db.rawQuery("select sum(" + KEY_VAT + ") from " + TABLE_ORDER_CART 
				//				+ " where "
				//				+ KEY_TAX + " = 'True' "
				, null);
		return res;
	}

	public Cursor getTotalTax()
	{
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor res = db.rawQuery("select sum(" + KEY_SERVICE_TAX + ") from " + TABLE_ORDER_CART 
				//				+ " where "
				//				+ KEY_TAX + " = 'False' "
				, null);
		return res;
	}

	public void deleteAllTableData() 
	{
		try 
		{
			SQLiteDatabase db = getWritableDatabase();
			db.execSQL("DELETE from " + TABLE_ORDER_CART);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	public Cursor checkItemAlreadyExist(String cItems, String cCrust, String cSize)
	{

		Cursor res = null;
		SQLiteDatabase db = this.getReadableDatabase();

		res = db.rawQuery("Select " + KEY_QUANTITY + "," + KEY_PRICE + " from " + TABLE_ORDER_CART + " Where " 
				+ KEY_ITEM_NAME + " = '" + cItems + "' And " + KEY_CRUST + " = '" 
				+ cCrust + "' And " + KEY_SIZE + " = '" + cSize + "'" , null);	    

		return res;
	}

	public boolean updateCartItem(String cItems, String cCrust, String cSize,
			String cPrice,Integer cQuantity, String cVat, String cTax) 
	{
		try
		{
			SQLiteDatabase db = this.getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(KEY_PRICE, cPrice);
			values.put(KEY_QUANTITY, cQuantity);
			values.put(KEY_VAT, cVat);
			values.put(KEY_SERVICE_TAX, cTax);

			db.update(TABLE_ORDER_CART, values, KEY_ITEM_NAME + " = '" + cItems + "' And " + KEY_CRUST + " = '" 
					+ cCrust + "' And " + KEY_SIZE + " = '" + cSize + "'" , null);
		}
		catch(Exception e)
		{
			System.out.println("################################################## " + e);        	
		}

		return true;
	}

	// public Cursor getCategory(String transId)
	// {
	// SQLiteDatabase db = this.getReadableDatabase();
	//
	// Cursor res = db.rawQuery( "SELECT * from " + TABLE_ORDER_CART + " where "
	// + KEY_ID + " = '" + transId + "'" , null);
	//
	// // Cursor c = MyData.rawQuery("SELECT * FROM " + tableName +
	// " where Category = '" +categoryex + "'" , null);
	// return res;
	// }
	//
	// public Cursor getUserNotes()
	// {
	// SQLiteDatabase db = this.getReadableDatabase();
	//
	// Cursor res = db.rawQuery( "SELECT * from " + TABLE_ORDER_CART
	// + " where ifnull(length(" + KEY_NOTES + "), 0) != 0", null);
	// return res;
	// }
	//
	// public Cursor getTotalIncome()
	// {
	// SQLiteDatabase db = this.getReadableDatabase();
	//
	// Cursor res = db.rawQuery("select sum(Credit) from " + TABLE_ORDER_CART +
	// " where "
	// + KEY_DEBIT_CREDIT + " = 'C' ", null);
	// return res;
	// }
	//
	// //************************************ Total Expense
	// ***************************************
	//


	// @Override
	// public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	// {
	// db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_CART);
	// onCreate(db);
	// }

}