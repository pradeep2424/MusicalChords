package com.music.chords.database;

import android.content.Context;
import android.database.sqlite.*;

public class CreateDB extends SQLiteOpenHelper {
	// static String DATABASE_NAME = "DBAccountStatement";

	private static final String DATABASE_NAME = "dbDominos";
	static int DATABASE_VERSION = 1;

	private static final String TABLE_MENU = "DominosMenu";
		private static final String TABLE_CRUST = "CrustDetails";
	private static final String TABLE_ORDER_CART = "OrderCart";
	private static final String TABLE_ORDER_ITEM = "OrderItem";

	
	private static final String KEY_ORDER_ID = "OrderId";
	private static final String KEY_ORDER_NO = "OrderNumber";
	private static final String KEY_DATE = "Date";
	private static final String KEY_TIME = "Time";
	private static final String KEY_VAT = "Vat";
	private static final String KEY_SERVICE_TAX = "ServiceTax";
	
	private static final String KEY_ITEM_NAME = "ItemName";
	private static final String KEY_CATEGORY = "Category";
	private static final String KEY_CRUST = "Crust";
	private static final String KEY_SIZE = "ItemSize";
	private static final String KEY_PRICE = "Price";
	private static final String KEY_TAX = "ServiceTax";
	private static final String KEY_QUANTITY = "ItemQuantity";
	
//	private static final String KEY_ITEM_POSITION = "ItemPosition";

	// private static final String KEY_TRANS_SUB_TYPE = "Trans_Sub_Type";
	// private static final String KEY_DEBIT_CREDIT = "Debit_Credit_Indicator";
	// private static final String KEY_TRANS_VALUE_DATE = "Trans_Value_Date";
	// private static final String KEY_DEBIT = "Debit";
	// private static final String KEY_CREDIT = "Credit";
	// private static final String KEY_PARTICULAR = "Trans_Part";
	// private static final String KEY_TRANS_POST_DATE = "Trans_Post_Date";
	// private static final String KEY_INTRUMENT_NO = "Instru_No";
	// private static final String KEY_BALANCE = "Balance";
	// private static final String KEY_SUB_CATEGORY = "Sub_Category";
	// private static final String KEY_NOTES = "Notes";

	// private static final String KEY_SUB_STRING = "Sub_String";
	// private static final String KEY_CUSTOMERID = "CustomerId";

	//	private static final String KEY_SIZE = "ItemSize";
	//	private static final String KEY_rPRICE = "rPrice";
	//	private static final String KEY_mPRICE = "mPrice";
	//	private static final String KEY_lPRICE = "lPrice";

	public CreateDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		try {
			
			String CREATE_MENU = "CREATE TABLE IF NOT EXISTS " + TABLE_MENU
					+ "(" + KEY_ITEM_NAME + " TEXT," + KEY_CATEGORY + " TEXT,"
					+ KEY_CRUST + " TEXT," + KEY_SIZE + " TEXT," 
					+ KEY_PRICE + " TEXT," + KEY_TAX + " TEXT" + ")";
			
			String CREATE_CRUST = "CREATE TABLE IF NOT EXISTS " + TABLE_CRUST
					+ "(" + KEY_CRUST + " TEXT," + KEY_SIZE + " TEXT" 
					+ ")";

			
			String CREATE_CART = "CREATE TABLE IF NOT EXISTS "
					+ TABLE_ORDER_CART + "("
					// + KEY_CATEGORY + " TEXT,"
					+ KEY_ITEM_NAME + " TEXT," 
					+ KEY_PRICE + " TEXT,"
					+ KEY_CRUST + " TEXT,"
					+ KEY_SIZE + " TEXT,"
					+ KEY_QUANTITY + " TEXT,"
//					+ KEY_TAX + " TEXT"
					+ KEY_VAT + " TEXT," 
					+ KEY_SERVICE_TAX + " TEXT" 
					+ ")";
			//
			// String CREATE_ITEM_PRICE = "CREATE TABLE IF NOT EXISTS " +
			// TABLE_ITEM_PRICE
			// + "(" + KEY_CATEGORY + " TEXT," + KEY_ITEM_NAME + " TEXT," +
			// KEY_rPRICE + " TEXT,"
			// + KEY_mPRICE + " TEXT," + KEY_lPRICE + " TEXT" + ")";
			//
			String CREATE_ORDER_ITEM = "CREATE TABLE IF NOT EXISTS " +
					TABLE_ORDER_ITEM
					+ "("
					+ KEY_ORDER_ID + " TEXT,"
					+ KEY_ORDER_NO + " TEXT,"
					+ KEY_DATE + " TEXT,"
					+ KEY_TIME + " TEXT,"
					+ KEY_ITEM_NAME + " TEXT,"
					+ KEY_PRICE + " TEXT,"
					+ KEY_CRUST + " TEXT,"
					+ KEY_SIZE + " TEXT,"
					+ KEY_QUANTITY + " TEXT,"
					+ KEY_VAT + " TEXT," 
					+ KEY_SERVICE_TAX + " TEXT" 
					+ ")";

			//
			// String CREATE_CUSTOMER = "CREATE TABLE IF NOT EXISTS " +
			// TABLE_PASSBOOK_CUSTOMER
			// + "(" + KEY_CUSTOMERID + " TEXT " + ")";

			db.execSQL(CREATE_MENU);
			db.execSQL(CREATE_CRUST);
			db.execSQL(CREATE_CART);
			db.execSQL(CREATE_ORDER_ITEM);

		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		try {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENU);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_CRUST);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_CART);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_ITEM);
			

			onCreate(db);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
