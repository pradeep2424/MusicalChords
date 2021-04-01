package com.music.chords.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBDominosMenu extends CreateDB {
	// private static final String TABLE_PASSBOOK_CATMASTER = "CategoryMaster";
	// private static final String TABLE_PASSBOOK_CUSTOMER = "Customer";

	private static final String TABLE_MENU = "DominosMenu";

	private static final String KEY_ITEM_NAME = "ItemName";
	private static final String KEY_CATEGORY = "Category";
	private static final String KEY_CRUST = "Crust";
	private static final String KEY_SIZE = "ItemSize";
	private static final String KEY_PRICE = "Price";
	private static final String KEY_TAX = "ServiceTax";
//	private static final String KEY_rPRICE = "rPrice";
//	private static final String KEY_mPRICE = "mPrice";
//	private static final String KEY_lPRICE = "lPrice";

	public DBDominosMenu(Context context) {
		super(context);
	}

	public void deleteAllTableData() {
		try {
			SQLiteDatabase db = getWritableDatabase();
			db.execSQL("DELETE from " + TABLE_MENU);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean insertData(String itemName, String category, String crust,
			String size, double price, String tax) {
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues contentValues = new ContentValues();

			contentValues.put(KEY_ITEM_NAME, itemName);
			contentValues.put(KEY_CATEGORY, category);
			contentValues.put(KEY_CRUST, crust);
			contentValues.put(KEY_SIZE, size);
			contentValues.put(KEY_PRICE, price);
			contentValues.put(KEY_TAX, tax);

			db.insert(TABLE_MENU, null, contentValues);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public boolean deleteData(String itemName, String category, String crust) {
		try {
			SQLiteDatabase db = getWritableDatabase();
			db.execSQL("DELETE from " + TABLE_MENU + " Where " + KEY_ITEM_NAME
					+ " = '" + itemName + "' And " + KEY_CATEGORY + " = '" + category
					+ "' And " + KEY_CRUST + " = '" + crust
					+ "'");
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return true;
	}

	public Cursor getData() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("SELECT * from " + TABLE_MENU, null);
		return res;
	}

	// public Cursor getPrice(String item)
	// {
	// SQLiteDatabase db = this.getReadableDatabase();
	// Cursor res = db.rawQuery( "SELECT rPrice, mPrice, lPrice from " +
	// TABLE_MENU + " Where "
	// // + KEY_CATEGORY + " = '" + category + "' And "
	// + KEY_ITEM_NAME + " = '" + item + "'" , null );
	// return res;
	// }

//	public Cursor getMenuItem(String category) 
//	{
//		SQLiteDatabase db = this.getReadableDatabase();
//		Cursor res = db.rawQuery("SELECT " + KEY_ITEM_NAME + ","+ KEY_PRICE +  " from "
//				+ TABLE_MENU 
//				+" where "
//				 + KEY_CATEGORY + " = '" + category + "'", null);
//		return res;
//	}
	
	public Cursor getDistinctMenu() 
	{
		Cursor res = null;
		
		SQLiteDatabase db = this.getReadableDatabase();
		res = db.rawQuery("SELECT DISTINCT " + KEY_CATEGORY + " from "
				+ TABLE_MENU, null);
		return res;
	}
	
	public Cursor getDistinctItem(String category) 
	{
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("SELECT DISTINCT " + KEY_ITEM_NAME + " from " + TABLE_MENU +" where "
				+ KEY_CATEGORY + " = '" + category + "'", null);
		return res;
	}
	
//	public Cursor getMenuItem(String category) 
//	{
//		SQLiteDatabase db = this.getReadableDatabase();
//		Cursor res = db.rawQuery("SELECT * from " + TABLE_MENU +" where "
//				 + KEY_CATEGORY + " = '" + category + "'", null);
//		
//		return res;
//	}
	
//	public Cursor getDistinctCrust() 
//	{
//		Cursor res = null;
//		
//		SQLiteDatabase db = this.getReadableDatabase();
//		res = db.rawQuery("SELECT DISTINCT " + KEY_CRUST + "," + KEY_SIZE + " from "
//				+ TABLE_MENU, null);
//		return res;
//	}
	
	public Cursor getDistinctCrust(String pizzaCategory) 
	{
		Cursor res = null;
		
		SQLiteDatabase db = this.getReadableDatabase();
		res = db.rawQuery("SELECT DISTINCT " + KEY_CRUST + "," + KEY_SIZE + " from "
				+ TABLE_MENU + " where " + KEY_CATEGORY + " = '" + pizzaCategory
				+ "'", null);
		return res;
	}

	public Cursor getPrice(String itemName, String crust, String size) 
	{
		Cursor res = null;
		
		SQLiteDatabase db = this.getReadableDatabase();
		res = db.rawQuery("SELECT "+ KEY_PRICE +"," + KEY_TAX  + " from " + TABLE_MENU + " Where "
		+ KEY_ITEM_NAME + " = '" + itemName 
		+ "' And " + KEY_CRUST + " = '" + crust
		+ "' And " + KEY_SIZE + " = '" + size 
		+ "'", null);
		return res;
	}
}
