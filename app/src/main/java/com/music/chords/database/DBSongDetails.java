package com.music.chords.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBSongDetails extends CreateDB {
    private static final String TABLE_SONG_DETAILS = "SongDetails";
    private static final String KEY_SONG_ID = "SongID";
    private static final String KEY_SONG_TITLE = "SongTitle";
    private static final String KEY_SONG_SUBTITLE = "SongSubtitle";
    private static final String KEY_SONG_ARTIST = "SongArtist";
    private static final String KEY_SONG_YOUTUBE_URL = "SongYouTubeURL";
    private static final String KEY_SONG_LYRICS = "SongLyrics";
    private static final String KEY_IS_FAVORITES = "IsFavorites";
    private static final String KEY_SONG_ICON_COLOR = "SongIconColor";
    private static final String KEY_SONG_LANGUAGE = "SongLanguage";
    private static final String KEY_SONG_IS_CONTAINS_CHORDS = "SongIsContainsChords";

    public DBSongDetails(Context context) {
        super(context);
    }

    public void deleteAllTableData() {
        try {
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL("DELETE from " + TABLE_SONG_DETAILS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean insertData(int songID, String songTitle, String songSubtitle, String songArtist,
                              String songYouTubeURL, String songLyrics, boolean isFavorites,
                              int songIconColor, String songLanguage, boolean songIsContainsChords) {

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put(KEY_SONG_ID, songID);
            contentValues.put(KEY_SONG_TITLE, songTitle);
            contentValues.put(KEY_SONG_SUBTITLE, songSubtitle);
            contentValues.put(KEY_SONG_ARTIST, songArtist);
            contentValues.put(KEY_SONG_YOUTUBE_URL, songYouTubeURL);
            contentValues.put(KEY_SONG_LYRICS, songLyrics);
            contentValues.put(KEY_IS_FAVORITES, isFavorites);
            contentValues.put(KEY_SONG_ICON_COLOR, songIconColor);
            contentValues.put(KEY_SONG_LANGUAGE, songLanguage);
            contentValues.put(KEY_SONG_IS_CONTAINS_CHORDS, songIsContainsChords);

            db.insert(TABLE_SONG_DETAILS, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


    public Cursor getData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * from " + TABLE_SONG_DETAILS, null);
        return res;
    }

    public Cursor getLyricsData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * from " + TABLE_SONG_DETAILS + " where "
                + KEY_SONG_IS_CONTAINS_CHORDS + " = 0", null);
        return res;
    }

    public Cursor getChordsData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * from " + TABLE_SONG_DETAILS + " where "
                + KEY_SONG_IS_CONTAINS_CHORDS + " = 1", null);
        return res;
    }

    public Cursor getSingleSongData(int songID) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("SELECT * from " + TABLE_SONG_DETAILS + " Where "
                + KEY_SONG_ID + " = '" + songID + "'", null);

        return res;
    }

    public Cursor getFavoritesSongData() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("SELECT * from " + TABLE_SONG_DETAILS + " Where "
                + KEY_IS_FAVORITES + " = 1", null);

        return res;
    }

    public boolean setSongToFavorites(int songID) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put(KEY_IS_FAVORITES, true);

            db.update(TABLE_SONG_DETAILS, contentValues, KEY_SONG_ID + " = '" + songID + "'", null);
        } catch (Exception e) {
            System.out.println("Exception : " + e);
        }

        return true;
    }

    public boolean removeSongFromFavorites(int songID) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put(KEY_IS_FAVORITES, false);

            db.update(TABLE_SONG_DETAILS, contentValues, KEY_SONG_ID + " = '" + songID + "'", null);
        } catch (Exception e) {
            System.out.println("Exception : " + e);
        }

        return true;
    }


    public Cursor getSearchedSongData(String searchText) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("SELECT * from " + TABLE_SONG_DETAILS + " WHERE "
                + KEY_SONG_TITLE + " LIKE '%" + searchText + "%' OR "
                + KEY_SONG_SUBTITLE + " LIKE '%" + searchText + "%' OR "
                + KEY_SONG_ARTIST + " LIKE '%" + searchText + "%' ", null);
        return res;
    }

    public boolean updateSongData(int songID, String songTitle, String songSubtitle, String songArtist,
                                  String songYouTubeURL, String songLyrics, Boolean isFavorites, int songIconColor) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put(KEY_SONG_TITLE, songTitle);
            contentValues.put(KEY_SONG_SUBTITLE, songSubtitle);
            contentValues.put(KEY_SONG_ARTIST, songArtist);
            contentValues.put(KEY_SONG_YOUTUBE_URL, songYouTubeURL);
            contentValues.put(KEY_SONG_LYRICS, songLyrics);
            contentValues.put(KEY_IS_FAVORITES, isFavorites);
            contentValues.put(KEY_SONG_ICON_COLOR, songIconColor);

            db.update(TABLE_SONG_DETAILS, contentValues, KEY_SONG_ID + " = '" + songID + "'", null);
        } catch (Exception e) {
            System.out.println("Exception : " + e);
        }

        return true;
    }

//    public boolean deleteData(String itemName, String category, String crust) {
//        try {
//            SQLiteDatabase db = getWritableDatabase();
//            db.execSQL("DELETE from " + TABLE_SONG_DETAILS + " Where " + KEY_ITEM_NAME
//                    + " = '" + itemName + "' And " + KEY_CATEGORY + " = '" + category
//                    + "' And " + KEY_CRUST + " = '" + crust
//                    + "'");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return true;
//    }

//    public Cursor getDistinctMenu() {
//        Cursor res = null;
//
//        SQLiteDatabase db = this.getReadableDatabase();
//        res = db.rawQuery("SELECT DISTINCT " + KEY_CATEGORY + " from "
//                + TABLE_SONG_DETAILS, null);
//        return res;
//    }


//	public Cursor getMenuItem(String category) 
//	{
//		SQLiteDatabase db = this.getReadableDatabase();
//		Cursor res = db.rawQuery("SELECT * from " + TABLE_SONG_DETAILS +" where "
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
//				+ TABLE_SONG_DETAILS, null);
//		return res;
//	}
}
