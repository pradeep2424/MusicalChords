package com.music.chords.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

//  TO CREATE TRANSACTION TABLE AND USE TRANSACTION RELATED QUERIES	

public class DBCrustDetails extends CreateDB {

	// private static final String DATABASE_NAME = "dbPassbook";
	// private static final int DATABASE_VERSION = 1;

	private static final String TABLE_CRUST = "CrustDetails";
	// private static final String TABLE_ORDER_CART_CATMAN = "CategoryManager";
	// private static final String TABLE_ORDER_CART_CATMASTER =
	// "CategoryMaster";
	// private static final String TABLE_ORDER_CART_CUSTOMER = "Customer";

	private static final String KEY_CRUST = "Crust";
	private static final String KEY_SIZE = "ItemSize";

	// private static final String KEY_IMAGE = "Image";

	public DBCrustDetails(Context context) 
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

	public void deleteAllTableData() {
		try {
			SQLiteDatabase db = getWritableDatabase();
			db.execSQL("DELETE from " + TABLE_CRUST);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean insertData(String crust, String size) 
	{
		try 
		{
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues contentValues = new ContentValues();

			contentValues.put(KEY_CRUST, crust);
			contentValues.put(KEY_SIZE, size);


			db.insert(TABLE_CRUST, null, contentValues);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public boolean deleteData(String crust)
	{
		try 
		{
			SQLiteDatabase db = getWritableDatabase();
			db.execSQL("DELETE from " + TABLE_CRUST + " Where " + KEY_CRUST + " = '" + crust
					+ "'");
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return true;
	}

	public Cursor getData() 
	{
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("SELECT * from " + TABLE_CRUST, null);
		return res;
	}

	// public Cursor getPrice(String item)
	// {
	// SQLiteDatabase db = this.getReadableDatabase();
	// Cursor res = db.rawQuery( "SELECT rPrice, mPrice, lPrice from " +
	// TABLE_CRUST + " Where "
	// // + KEY_CATEGORY + " = '" + category + "' And "
	// + KEY_ITEM_NAME + " = '" + item + "'" , null );
	// return res;
	// }

//	public Cursor getMenuItem(String category) 
//	{
//		SQLiteDatabase db = this.getReadableDatabase();
//		Cursor res = db.rawQuery("SELECT " + KEY_ITEM_NAME + ","+ KEY_PRICE +  " from "
//				+ TABLE_CRUST 
//				+" where "
//				 + KEY_CATEGORY + " = '" + category + "'", null);
//		return res;
//	}
	
	public Cursor getAllCrust() 
	{
		Cursor res = null;
		
		SQLiteDatabase db = this.getReadableDatabase();
		res = db.rawQuery("SELECT " + KEY_CRUST + " from " + TABLE_CRUST, null);
		return res;
	}
	
	public Cursor getCrust(String size) 
	{
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("SELECT " + KEY_CRUST + " from " + TABLE_CRUST +" where "
				+ KEY_SIZE + " = '" + size + "'", null);
		return res;
	}
	
//	public Cursor getCrustMedium(String size) 
//	{
//		SQLiteDatabase db = this.getReadableDatabase();
//		Cursor res = db.rawQuery("SELECT " + KEY_CRUST + " from " + TABLE_CRUST +" where "
//				+ KEY_SIZE + " = '" + size + "'", null);
//		return res;
//	}
//	
//	public Cursor getCrustLarge(String size) 
//	{
//		SQLiteDatabase db = this.getReadableDatabase();
//		Cursor res = db.rawQuery("SELECT " + KEY_CRUST + " from " + TABLE_CRUST +" where "
//				+ KEY_SIZE + " = '" + size + "'", null);
//		return res;
//	}
	
//	public Cursor getMenuItem(String category) 
//	{
//		SQLiteDatabase db = this.getReadableDatabase();
//		Cursor res = db.rawQuery("SELECT * from " + TABLE_CRUST +" where "
//				 + KEY_CATEGORY + " = '" + category + "'", null);
//		
//		return res;
//	}

//	public Cursor getPrice(String itemName, String size) 
//	{
//		Cursor res = null;
//		
//		SQLiteDatabase db = this.getReadableDatabase();
//		res = db.rawQuery("SELECT "+ KEY_PRICE + " from " + TABLE_CRUST + " Where "
//		+ KEY_ITEM_NAME + " = '" + itemName + "' And " + KEY_SIZE + " = '" + size + "'", null);
//		return res;
//	}

}