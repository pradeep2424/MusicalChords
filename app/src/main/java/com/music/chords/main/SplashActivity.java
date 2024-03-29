package com.music.chords.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.material.snackbar.Snackbar;
import com.music.chords.R;
import com.music.chords.database.CreateDB;
import com.music.chords.database.DBSongDetails;
import com.music.chords.objects.SongObject;
import com.music.chords.service.retrofit.ApiInterface;
import com.music.chords.service.retrofit.RetroClient;
import com.music.chords.utils.Application;
import com.music.chords.utils.InternetConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    RelativeLayout rlRootLayout;

    CreateDB dbCreate;
    DBSongDetails dbSongDetails;

    VideoView videoView;
    RelativeLayout rlAppNameLayout;
    TextView tvAppName;

    boolean isServiceDataDownloadFinished = false;
    CountDownTimer countDownTimer;

    Animation fadeOut;

    ArrayList<SongObject> listSongsData;
    ArrayList<SongObject> listLyricsData;
    ArrayList<SongObject> listChordsData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        loadNextPage();
        init();
        componentEvents();
        initSplashVideo();
        initAppNameHandler();
        loadSongData();
    }

    private void init() {
        listSongsData = new ArrayList<>();
        listLyricsData = new ArrayList<>();
        listChordsData = new ArrayList<>();

        dbCreate = new CreateDB(this);
        dbSongDetails = new DBSongDetails(this);

        rlRootLayout = findViewById(R.id.rl_rootLayout);
        videoView = findViewById(R.id.videoView);
        rlAppNameLayout = findViewById(R.id.rl_appNameLayout);
        tvAppName = findViewById(R.id.tv_appName);

        fadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
    }

    private void componentEvents() {
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                checkIfServiceDataDownloadFinish();

//                loadSongData();

//                final Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        loadSongData();
//                    }
//                }, 2000);

//                loadNextPage();
            }
        });

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                checkIfServiceDataDownloadFinish();
                return false;
            }
        });
    }


    private void initSplashVideo() {
        Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video_splash);

        videoView.setVideoURI(video);
//        videoView.setMediaController(new MediaController(this));
        videoView.start();
    }

    private void initAppNameHandler() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rlAppNameLayout.setVisibility(View.VISIBLE);
                rlAppNameLayout.startAnimation(fadeOut);
//                tvAppName.setVisibility(View.VISIBLE);
//                tvAppName.startAnimation(fadeOut);
            }
        }, 7500);
    }

    private void checkIfServiceDataDownloadFinish() {
        countDownTimer = new CountDownTimer(Long.MAX_VALUE, 1000) {

            // This is called after every 1 sec interval.
            public void onTick(long millisUntilFinished) {
                if (isServiceDataDownloadFinished) {
                    loadNextPage();
                }
            }

            public void onFinish() {
                start();
            }
        }.start();
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
//                loadNextPage();
            } else {
//              no data available in local db also
//        Toasty.warning(SplashActivity.this, getString(R.string.connect_to_internet), Toast.LENGTH_LONG, true).show();
                Toasty.normal(SplashActivity.this, getString(R.string.connect_to_internet), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void downloadNewSongDataFromServer() {
        if (InternetConnection.checkConnection(getApplicationContext())) {

            ApiInterface apiService = RetroClient.getApiService(this);
            Call<ResponseBody> call = apiService.getSongDetails();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        boolean isLocalDBHasData;

                        int statusCode = response.code();
                        if (response.isSuccessful()) {

                            String responseString = response.body().string();
                            JSONObject jsonObj = new JSONObject(responseString);
                            String respStatus = jsonObj.getString("Status");

                            if (respStatus.equalsIgnoreCase("Success")) {
                                isLocalDBHasData = checkLocalDBHasData();
                                listSongsData.clear();
                                listLyricsData.clear();
                                listChordsData.clear();

//                                JSONArray jsonArrayRoot = jsonObj.getJSONArray("SongData");
                                JSONArray jsonArrayRoot = jsonObj.getJSONArray("songMasters");
                                if (jsonArrayRoot != null && jsonArrayRoot.length() > 0) {

                                    for (int index = 0; index < jsonArrayRoot.length(); index++) {
                                        JSONObject json = jsonArrayRoot.getJSONObject(index);

                                        int songID = json.getInt("SongID");
                                        String songTitle = json.getString("SongTitle");
                                        String songSubtitle = json.getString("SongSubtitle");
                                        String songArtist = json.getString("SongArtist");
                                        String youTubeURL = json.getString("SongYouTubeURL");
                                        String songLyrics = json.getString("SongLyrics");
                                        String songLanguage = json.getString("SongLanguage");
                                        boolean isContainsChords = json.getBoolean("IsContainsChords");
//                        int songIconColor = getRandomMaterialColor();
                                        int songIconColor;
                                        if (isContainsChords) {
                                            songIconColor = ContextCompat.getColor(SplashActivity.this, R.color.chords_icon);
                                        } else {
                                            songIconColor = ContextCompat.getColor(SplashActivity.this, R.color.lyrics_icon);
                                        }
                                        boolean isFavorites = false;

                                        SongObject songObject = new SongObject();
                                        songObject.setSongId(songID);
                                        songObject.setSongTitle(songTitle);
                                        songObject.setSongSubtitle(songSubtitle);
                                        songObject.setSongArtist(songArtist);
                                        songObject.setSongYouTubeURL(youTubeURL);
                                        songObject.setSongLyrics(songLyrics);
                                        songObject.setSongLanguage(songLanguage);
                                        songObject.setContainsChords(isContainsChords);
                                        songObject.setSongIconColor(songIconColor);

//                       checking DB if song is added to favorites
                                        if (isLocalDBHasData) {
                                            Cursor rss = dbSongDetails.getSingleSongData(songID);
                                            int countDBRows = rss.getCount();

                                            if (countDBRows > 0) {
                                                rss.moveToFirst();
                                                isFavorites = (rss.getInt(6) == 1);
                                                rss.close();
                                            }
                                            songObject.setIsFavorites(isFavorites);
                                        } else {
                                            songObject.setIsFavorites(false);

//                           No data foudn in DB, hence inserting data in DB in same loop
                                            insertDataIntoDB(songID, songTitle, songSubtitle, songArtist,
                                                    youTubeURL, songLyrics, isFavorites, songIconColor,
                                                    songLanguage, isContainsChords);
                                        }

                                        listSongsData.add(songObject);
                                        if (isContainsChords) {
                                            listChordsData.add(songObject);
                                        } else {
                                            listLyricsData.add(songObject);
                                        }
                                    }

//                  if DB has data then clearing and updating db
                                    if (isLocalDBHasData) {
                                        deleteAllTableData();
                                        copyDataFromArrayListToDB();
                                    }
                                }

                                isServiceDataDownloadFinished = true;

//                final Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        isServiceDataDownloadFinished = true;
//                    }
//                }, 15000);


//                loadNextPage();
                            }
                        } else {

                            showSnackbarErrorMsg(getResources().getString(R.string.something_went_wrong));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    try {
                        showSnackbarErrorMsg(getResources().getString(R.string.server_conn_lost));
                        Log.e("Error onFailure : ", t.toString());
                        t.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {

            Toast.makeText(SplashActivity.this, getResources().getString(R.string.no_internet),
                    Toast.LENGTH_LONG).show();

            Snackbar.make(rlRootLayout, getResources().getString(R.string.no_internet),
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            downloadNewSongDataFromServer();
                        }
                    })
                    .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
                    .show();
        }
    }

    public void showSnackbarErrorMsg(String erroMsg) {
        Snackbar.make(rlRootLayout, erroMsg, Snackbar.LENGTH_LONG).show();
    }

    public void showSnackbarErrorMsgWithButton(String erroMsg) {
        Snackbar.make(rlRootLayout, erroMsg, Snackbar.LENGTH_INDEFINITE)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                })
                .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
                .show();
    }

//    private void downloadNewSongDataFromServer() {
//        boolean isLocalDBHasData;
//
//        try {
//            String responseString = loadJSONFromAsset();
//
//            JSONObject jsonObj = new JSONObject(responseString);
//            String respStatus = jsonObj.getString("Status");
//
//            if (respStatus.equalsIgnoreCase("Success")) {
//                isLocalDBHasData = checkLocalDBHasData();
//                listSongsData.clear();
//                listLyricsData.clear();
//                listChordsData.clear();
//
//                JSONArray jsonArrayRoot = jsonObj.getJSONArray("SongData");
//                if (jsonArrayRoot != null && jsonArrayRoot.length() > 0) {
//
//                    for (int index = 0; index < jsonArrayRoot.length(); index++) {
//                        JSONObject json = jsonArrayRoot.getJSONObject(index);
//
//                        int songID = json.getInt("ID");
//                        String songTitle = json.getString("Title");
//                        String songSubtitle = json.getString("Subtitle");
//                        String songArtist = json.getString("Artist");
//                        String youTubeURL = json.getString("YouTubeURL");
//                        String songLyrics = json.getString("Lyrics");
//                        String songLanguage = json.getString("SongLanguage");
//                        boolean isContainsChords = json.getBoolean("IsContainsChords");
////                        int songIconColor = getRandomMaterialColor();
//                        int songIconColor;
//                        if (isContainsChords) {
//                            songIconColor = getColor(R.color.chords_icon);
//                        } else {
//                            songIconColor = getColor(R.color.lyrics_icon);
//                        }
//                        boolean isFavorites = false;
//
//                        SongObject songObject = new SongObject();
//                        songObject.setSongId(songID);
//                        songObject.setSongTitle(songTitle);
//                        songObject.setSongSubtitle(songSubtitle);
//                        songObject.setSongArtist(songArtist);
//                        songObject.setSongYouTubeURL(youTubeURL);
//                        songObject.setSongLyrics(songLyrics);
//                        songObject.setSongLanguage(songLanguage);
//                        songObject.setContainsChords(isContainsChords);
//                        songObject.setSongIconColor(songIconColor);
//
////                       checking DB if song is added to favorites
//                        if (isLocalDBHasData) {
//                            Cursor rss = dbSongDetails.getSingleSongData(songID);
//                            int countDBRows = rss.getCount();
//
//                            if (countDBRows > 0) {
//                                rss.moveToFirst();
//                                isFavorites = (rss.getInt(6) == 1);
//                                rss.close();
//                            }
//                            songObject.setIsFavorites(isFavorites);
//                        } else {
//                            songObject.setIsFavorites(false);
//
////                           No data foudn in DB, hence inserting data in DB in same loop
//                            insertDataIntoDB(songID, songTitle, songSubtitle, songArtist,
//                                    youTubeURL, songLyrics, isFavorites, songIconColor,
//                                    songLanguage, isContainsChords);
//                        }
//
//                        listSongsData.add(songObject);
//                        if (isContainsChords) {
//                            listChordsData.add(songObject);
//                        } else {
//                            listLyricsData.add(songObject);
//                        }
//                    }
//
////                  if DB has data then clearing and updating db
//                    if (isLocalDBHasData) {
//                        deleteAllTableData();
//                        copyDataFromArrayListToDB();
//                    }
//                }
//
//                isServiceDataDownloadFinished = true;
//
////                final Handler handler = new Handler();
////                handler.postDelayed(new Runnable() {
////                    @Override
////                    public void run() {
////                        isServiceDataDownloadFinished = true;
////                    }
////                }, 15000);
//
//
////                loadNextPage();
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private void insertDataIntoDB(int songID, String songTitle, String songSubtitle, String songArtist,
                                  String songYouTubeURL, String songLyrics, Boolean isFavorites,
                                  int songIconColor, String songLanguage, boolean isContainsChords) {
        dbSongDetails.insertData(songID, songTitle, songSubtitle, songArtist, songYouTubeURL,
                songLyrics, isFavorites, songIconColor, songLanguage, isContainsChords);
    }

    private void deleteAllTableData() {
        dbSongDetails.deleteAllTableData();
    }

    private void copyDataFromArrayListToDB() {
        for (int index = 0; index < listSongsData.size(); index++) {
            SongObject songObject = listSongsData.get(index);

            int songID = songObject.getSongId();
            String songTitle = songObject.getSongTitle();
            String songSubtitle = songObject.getSongSubtitle();
            String songArtist = songObject.getSongArtist();
            String youTubeURL = songObject.getSongYouTubeURL();
            String songLyrics = songObject.getSongLyrics();
            boolean isFavorites = songObject.getIsFavorites();
            int songIconColor = songObject.getSongIconColor();
            String songLanguage = songObject.getSongLanguage();
            boolean isContainsChords = songObject.isContainsChords();

            insertDataIntoDB(songID, songTitle, songSubtitle, songArtist, youTubeURL,
                    songLyrics, isFavorites, songIconColor, songLanguage, isContainsChords);

//            if (isContainsChords) {
//                listChordsData.add(songObject);
//            } else {
//                listLyricsData.add(songObject);
//            }
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
                boolean isFavorites = (rss.getInt(6) == 1);
                int songIconColor = (rss.getInt(7));
                String songLanguage = (rss.getString(8));
                boolean isContainsChords = (rss.getInt(9) == 1);

                SongObject songObject = new SongObject();
                songObject.setSongId(songId);
                songObject.setSongTitle(songTitle);
                songObject.setSongSubtitle(songSubtitle);
                songObject.setSongLyrics(songLyrics);
                songObject.setSongArtist(songArtist);
                songObject.setSongYouTubeURL(songYouTubeURL);
                songObject.setIsFavorites(isFavorites);
                songObject.setSongIconColor(songIconColor);
                songObject.setSongLanguage(songLanguage);
                songObject.setContainsChords(isContainsChords);

                listSongsData.add(songObject);

                if (isContainsChords) {
                    listChordsData.add(songObject);
                } else {
                    listLyricsData.add(songObject);
                }

                rss.moveToNext();
            }
            rss.close();
            isServiceDataDownloadFinished = true;

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

//    private int getRandomMaterialColor() {
//        String typeColor = "400";
//        int returnColor = Color.GRAY;
//        int arrayId = getResources().getIdentifier("mdcolor_" + typeColor, "array", getPackageName());
//
//        if (arrayId != 0) {
//            TypedArray colors = getResources().obtainTypedArray(arrayId);
//            int index = (int) (Math.random() * colors.length());
//            returnColor = colors.getColor(index, Color.GRAY);
//            colors.recycle();
//        }
//        return returnColor;
//    }

    private void stopCountDownTimer() {
        try {
            countDownTimer.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadNextPage() {
        isServiceDataDownloadFinished = false;
        Application.allSongsData = listSongsData;
        Application.allLyricsData = listLyricsData;
        Application.allChordsData = listChordsData;

        stopCountDownTimer();

        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }


}