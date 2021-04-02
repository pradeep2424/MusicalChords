package com.music.chords.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.music.chords.R;
import com.music.chords.database.CreateDB;
import com.music.chords.database.DBSongDetails;
import com.music.chords.objects.SongObject;
import com.music.chords.utils.InternetConnection;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class SplashActivity extends AppCompatActivity {
    CreateDB dbCreate;
    DBSongDetails dbSongDetails;

    ArrayList<SongObject> listAllSongsData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        loadNextPage();
        init();
        loadSongData();
    }

    private void init() {
        listAllSongsData = new ArrayList<>();
        dbCreate = new CreateDB(this);
        dbSongDetails = new DBSongDetails(this);
    }

    private void loadSongData() {
        if (isInternetAvailable()) {
//           internet is available downloading new data from serer
            downloadNewSongDataFromServer();

        } else {
//           no internet available checking if data is available in local DB
            int rowsInDB = checkLocalDBCount();
            if (rowsInDB > 0) {
//                fetching data from local DB
                getSongDataFromDB();
            } else {
//              no data available in local db also
//        Toasty.warning(SplashActivity.this, getString(R.string.connect_to_internet), Toast.LENGTH_LONG, true).show();
                Toasty.normal(SplashActivity.this, getString(R.string.connect_to_internet), Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean isInternetAvailable() {
        if (InternetConnection.checkConnection(SplashActivity.this)) {
            return true;
        } else {
            return false;
        }
    }

    private void downloadNewSongDataFromServer() {

    }

    private int checkLocalDBCount() {
        Cursor rss = dbSongDetails.getData();
        int rows = rss.getCount();
        rss.close();
        return rows;
    }

    private void getSongDataFromDB() {
        try {

            Cursor rss = dbSongDetails.getData();
            int countDBRows = rss.getCount();

            rss.moveToFirst();

            for (int index = 0; index < countDBRows; index++) {
                int songId = rss.getInt(0);
                String songTitle = rss.getString(1);
                String songSubtitle = rss.getString(2);
                String songLyrics = rss.getString(3);
                String songArtist = rss.getString(4);
                String songYouTubeURL = rss.getString(5);
                Boolean isFavorites = (rss.getInt(6) == 1);

                SongObject songObject = new SongObject();
                songObject.setSongId(songId);
                songObject.setSongTitle(songTitle);
                songObject.setSongSubtitle(songSubtitle);
                songObject.setSongLyrics(songLyrics);
                songObject.setSongArtist(songArtist);
                songObject.setSongYouTubeURL(songYouTubeURL);
                songObject.setIsFavorites(isFavorites);
                songObject.setSongIconColor(getRandomMaterialColor());
                listAllSongsData.add(songObject);

                rss.moveToNext();
            }
            rss.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertDataIntoLocalDB()
    {
        dbSongDetails.insertData();
    }

    private int getRandomMaterialColor() {
        String typeColor = "400";
        int returnColor = Color.GRAY;
        int arrayId = getResources().getIdentifier("mdcolor_" + typeColor, "array", getPackageName());

        if (arrayId != 0) {
            TypedArray colors = getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.GRAY);
            colors.recycle();
        }
        return returnColor;
    }

    private void loadNextPage() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }


}