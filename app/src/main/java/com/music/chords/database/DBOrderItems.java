package com.music.chords.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBOrderItems extends CreateDB {
	// private static final String DATABASE_NAME = "dbPassbook";
	// private static final int DATABASE_VERSION = 1;

	private static final String TABLE_ORDER_ITEM = "OrderItem";
	// private static final String TABLE_ORDER_CART_CATMAN = "CategoryManager";
	// private static final String TABLE_ORDER_CART_CATMASTER =
	// "CategoryMaster";
	// private static final String TABLE_ORDER_CART_CUSTOMER = "Customer";

	private static final String KEY_ORDER_ID = "OrderId";
	private static final String KEY_ORDER_NO = "OrderNumber";
	private static final String KEY_DATE = "Date";
	private static final String KEY_TIME = "Time";
	private static final String KEY_ITEM_NAME = "ItemName";
	private static final String KEY_PRICE = "Price";
	private static final String KEY_CRUST = "Crust";
	private static final String KEY_SIZE = "ItemSize";
	private static final String KEY_QUANTITY = "ItemQuantity";
	private static final String KEY_VAT = "Vat";
	private static final String KEY_SERVICE_TAX = "ServiceTax";

	public DBOrderItems(Context context) 
	{
		super(context);
	}

	public void deleteAllTableData() 
	{
		try 
		{
			SQLiteDatabase db = getWritableDatabase();
			db.execSQL("DELETE from " + TABLE_ORDER_ITEM);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	public boolean insertData(String cId,String cNo, String cDate, String cTime , String cItems,
			String cPrice,String cCrust, String cPriceType, Integer cQuantity, String cVat, String cTax) 
	{
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues contentValues = new ContentValues();

			contentValues.put(KEY_ORDER_ID, cId);
			contentValues.put(KEY_ORDER_NO, cNo);
			contentValues.put(KEY_DATE, cDate);
			contentValues.put(KEY_TIME, cTime);
			contentValues.put(KEY_ITEM_NAME, cItems);
			contentValues.put(KEY_PRICE, cPrice);
			contentValues.put(KEY_CRUST, cCrust);
			contentValues.put(KEY_SIZE, cPriceType);
			contentValues.put(KEY_QUANTITY, cQuantity);
			contentValues.put(KEY_VAT, cVat);
			contentValues.put(KEY_SERVICE_TAX, cTax);

			db.insert(TABLE_ORDER_ITEM, null, contentValues);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return true;
	}

	public boolean deleteData(String cItems, Integer cPrice, String cPriceType,
			Integer cImage) {
		try {
			SQLiteDatabase db = getWritableDatabase();
			db.execSQL("DELETE from " + TABLE_ORDER_ITEM + " Where " + KEY_ITEM_NAME
					+ " = '" + cItems + "' And " + KEY_PRICE + " = '" + cPrice
					+ "' And " + KEY_SIZE + " = '" + cPriceType
					+ "'");
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return true;
	}

	public Cursor getData() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("SELECT * from " + TABLE_ORDER_ITEM, null);
		return res;
	}
	
	public Cursor getDistinctId()
	{
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("SELECT DISTINCT " + KEY_ORDER_NO + " from " + TABLE_ORDER_ITEM, null);
		return res;
	}

}
