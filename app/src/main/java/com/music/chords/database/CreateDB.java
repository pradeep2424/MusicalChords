package com.music.chords.database;

import android.content.Context;
import android.database.sqlite.*;

public class CreateDB extends SQLiteOpenHelper {
    // static String DATABASE_NAME = "DBAccountStatement";

    private static final String DATABASE_NAME = "dbMusicalChords";
    static int DATABASE_VERSION = 1;

    private static final String TABLE_SONG_DETAILS = "SongDetails";
    private static final String KEY_SONG_ID = "SongID";
    private static final String KEY_SONG_TITLE = "SongTitle";
    private static final String KEY_SONG_SUBTITLE = "SongSubtitle";
    private static final String KEY_SONG_LYRICS = "SongLyrics";
    private static final String KEY_SONG_ARTIST = "SongArtist";
    private static final String KEY_SONG_YOUTUBE_URL = "SongYouTubeURL";
    private static final String KEY_IS_FAVORITES = "IsFavorites";

    public CreateDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {

            String CREATE_MENU = "CREATE TABLE IF NOT EXISTS " + TABLE_SONG_DETAILS + "("
                    + KEY_SONG_ID + " INTEGER,"
                    + KEY_SONG_TITLE + " TEXT,"
                    + KEY_SONG_SUBTITLE + " TEXT,"
                    + KEY_SONG_LYRICS + " TEXT,"
                    + KEY_SONG_ARTIST + " TEXT,"
                    + KEY_SONG_YOUTUBE_URL + " TEXT,"
                    + KEY_IS_FAVORITES + " INTEGER" + ")";

            db.execSQL(CREATE_MENU);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SONG_DETAILS);
            onCreate(db);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
