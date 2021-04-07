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
import com.music.chords.utils.Application;
import com.music.chords.utils.InternetConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class SplashActivity extends AppCompatActivity {
    CreateDB dbCreate;
    DBSongDetails dbSongDetails;

    ArrayList<SongObject> listSongData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        loadNextPage();
        init();
        loadSongData();
    }

    private void init() {
        listSongData = new ArrayList<>();
        dbCreate = new CreateDB(this);
        dbSongDetails = new DBSongDetails(this);
    }

    private boolean isInternetAvailable() {
        if (InternetConnection.checkConnection(SplashActivity.this)) {
            return true;
        } else {
            return false;
        }
    }

    private void loadSongData() {
        if (isInternetAvailable()) {
//           internet is available downloading new data from serer
            downloadNewSongDataFromServer();

        } else {
//           no internet available checking if data is available in local DB
            if (checkLocalDBHasData()) {
//                fetching data from local DB
                getSongDataFromDB();
                loadNextPage();
            } else {
//              no data available in local db also
//        Toasty.warning(SplashActivity.this, getString(R.string.connect_to_internet), Toast.LENGTH_LONG, true).show();
                Toasty.normal(SplashActivity.this, getString(R.string.connect_to_internet), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void downloadNewSongDataFromServer() {
        boolean isLocalDBHasData;

        try {
            String responseString = loadJSONFromAsset();

            JSONObject jsonObj = new JSONObject(responseString);
            String respStatus = jsonObj.getString("Status");

            if (respStatus.equalsIgnoreCase("Success")) {
                isLocalDBHasData = checkLocalDBHasData();
                listSongData.clear();

                JSONArray jsonArrayRoot = jsonObj.getJSONArray("SongData");
                if (jsonArrayRoot != null && jsonArrayRoot.length() > 0) {

                    for (int index = 0; index < jsonArrayRoot.length(); index++) {
                        JSONObject json = jsonArrayRoot.getJSONObject(index);

                        int songID = json.getInt("ID");
                        String songTitle = json.getString("Title");
                        String songSubtitle = json.getString("Subtitle");
                        String songArtist = json.getString("Artist");
                        String youTubeURL = json.getString("YouTubeURL");
                        String songLyrics = json.getString("Lyrics");
                        int songIconColor = getRandomMaterialColor();
                        boolean isFavorites = false;

                        SongObject songObject = new SongObject();
                        songObject.setSongId(songID);
                        songObject.setSongTitle(songTitle);
                        songObject.setSongSubtitle(songSubtitle);
                        songObject.setSongArtist(songArtist);
                        songObject.setSongYouTubeURL(youTubeURL);
                        songObject.setSongLyrics(songLyrics);
                        songObject.setSongIconColor(songIconColor);

//                       checking DB if song is added to favorites
                        if (isLocalDBHasData) {
                            Cursor rss = dbSongDetails.getSingleSongData(songID);
                            int countDBRows = rss.getCount();

                            rss.moveToFirst();
                            isFavorites = (rss.getInt(6) == 1);
                            rss.close();

                            songObject.setIsFavorites(isFavorites);
                        } else {
                            songObject.setIsFavorites(false);

//                           No data foudn in DB, hence inserting data in DB in same loop
                            insertDataIntoDB(songID, songTitle, songSubtitle, songArtist,
                                    youTubeURL, songLyrics, isFavorites, songIconColor);
                        }

                        listSongData.add(songObject);
                    }

//                  if DB has data then clearing and updating db
                    if (isLocalDBHasData) {
                        deleteAllTableData();
                        copyDataFromArrayListToDB();
                    }
                }

                loadNextPage();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertDataIntoDB(int songID, String songTitle, String songSubtitle, String songArtist,
                                  String songYouTubeURL, String songLyrics, Boolean isFavorites, int songIconColor) {
        dbSongDetails.insertData(songID, songTitle, songSubtitle, songArtist,
                songYouTubeURL, songLyrics, isFavorites, songIconColor);
    }

    private void deleteAllTableData() {
        checkLocalDBHasData();
        dbSongDetails.deleteAllTableData();
        checkLocalDBHasData();
    }

    private void copyDataFromArrayListToDB() {
        for (int index = 0; index < listSongData.size(); index++) {

            int songID = listSongData.get(index).getSongId();
            String songTitle = listSongData.get(index).getSongTitle();
            String songSubtitle = listSongData.get(index).getSongSubtitle();
            String songArtist = listSongData.get(index).getSongArtist();
            String youTubeURL = listSongData.get(index).getSongYouTubeURL();
            String songLyrics = listSongData.get(index).getSongLyrics();
            boolean isFavorites = listSongData.get(index).getIsFavorites();
            int songIconColor = listSongData.get(index).getSongIconColor();

            insertDataIntoDB(songID, songTitle, songSubtitle, songArtist,
                    youTubeURL, songLyrics, isFavorites, songIconColor);

            checkLocalDBHasData();
        }
    }

    private boolean checkLocalDBHasData() {
        Cursor rss = dbSongDetails.getData();
        int rows = rss.getCount();
        rss.close();

        if (rows > 0) {
            return true;
        } else {
            return false;
        }
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
                listSongData.add(songObject);

                rss.moveToNext();
            }
            rss.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        InputStream is;
        try {

            is = getAssets().open("songs/songs_response.json");

//            is = getActivity().getAssets().open("aarti_marathi.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
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
        Application.allSongsData = listSongData;

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