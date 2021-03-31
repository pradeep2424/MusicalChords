package com.music.chords.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import android.os.Handler;
import android.os.PowerManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.gson.Gson;
import com.music.chords.R;
import com.music.chords.interfaces.Constants;
import com.music.chords.objects.SongObject;
import com.music.chords.utils.Application;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SongDetailsActivity extends AppCompatActivity implements Constants {
    SongObject songObject;

    NestedScrollView nestedScrollView;
    ImageView ivCoverPic;
    TextView tvTitle;
    TextView tvSubtitle;
    TextView tvLyrics;

    private PowerManager.WakeLock wakeLock;

    private volatile String chordText;

//    SparkButton heartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            songObject = (SongObject) bundle.getSerializable(SONG_OBJECT);
        }

        init();
        componentEvents();
        setSongData();

        chordText = "";
    }

    private void init() {
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, getPackageName());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        getSupportActionBar().setTitle(getResources().getString(R.string.title_aarti));

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, R.color.transparent));
        collapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.white));
        collapsingToolbarLayout.setTitle(songObject.getSongTitle());

//        prefManagerAppData = new PrefManagerAppData(this);

        nestedScrollView = findViewById(R.id.nestedScrollView);
        ivCoverPic = (ImageView) findViewById(R.id.iv_coverPic);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvSubtitle = (TextView) findViewById(R.id.tv_subtitle);
        tvLyrics = (TextView) findViewById(R.id.tv_lyricsText);

//        heartButton = (SparkButton) findViewById(R.id.heart_button);
//        btnAudio = (Button) findViewById(R.id.btn_audio);
//        btnPictures = (Button) findViewById(R.id.btn_pictures);

//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (songObject.getIsFavourites()) {
//                    heartButton.setChecked(true);
//                    heartButton.playAnimation();
//                }
//            }
//        }, 500);
    }

    private void componentEvents() {
//        heartButton.setEventListener(new SparkEventListener(){
//            @Override
//            public void onEvent(ImageView button, boolean buttonState) {
//                if (buttonState) {
//                    // Button is active
//                    liked();
//                } else {
//                    // Button is inactive
//                    unLiked();
//                }
//            }
//
//            @Override
//            public void onEventAnimationEnd(ImageView button, boolean buttonState) {
//
//            }
//
//            @Override
//            public void onEventAnimationStart(ImageView button, boolean buttonState) {
//
//            }
//        });
    }

    private void setSongData() {
        if (songObject != null) {

            Glide.with(this).load(songObject.getSongYouTubeURL()).into(ivCoverPic);
            tvTitle.setText(songObject.getSongTitle());
            tvSubtitle.setText(songObject.getSongSubtitle());
            tvLyrics.setText(songObject.getSongLyrics());
        }
    }



    //    @Override
//    public void liked() {
//        try {
//            ArrayList<songObject> listFavourites = new ArrayList<>();
//
//            String json = prefManagerAppData.getFavourites();
//            if (!json.equalsIgnoreCase("NotFound")) {
//                listFavourites = getFavouritesListFromSP();
//            }
//
//            songObject favouriteSongObject = (songObject) SerializationUtils.clone(songObject);
//
//            favouriteSongObject.setIsFavourites(true);
//            listFavourites.add(favouriteSongObject);
//
//            SongApplication.allSongData.set(SongApplication.allSongData.indexOf(songObject), favouriteSongObject);
//
////        SongApplication.allSongData.set(songSelectedPosition, songObject);
//
//            setFavouritesListToSP(listFavourites);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    //    @Override
//    public void unLiked() {
//        ArrayList<songObject> listNewFavourites = new ArrayList<>();
//
//        String json = prefManagerAppData.getFavourites();
//        if (!json.equalsIgnoreCase("NotFound")) {
//            listNewFavourites = getFavouritesListFromSP();
//        }
//
//        songObject removedFavouriteObject = (songObject) SerializationUtils.clone(songObject);
//        if (listNewFavourites != null) {
//            listNewFavourites.remove(removedFavouriteObject);
//        }
//
//        removedFavouriteObject.setIsFavourites(false);
//        SongApplication.allSongData.set(SongApplication.allSongData.indexOf(songObject), removedFavouriteObject);
//
////        saving new favourite list to shared preference
//        setFavouritesListToSP(listNewFavourites);
//    }
//
//
//    private ArrayList<songObject> getFavouritesListFromSP() {
//        String json = prefManagerAppData.getFavourites();
//        Gson gson = new Gson();
//        Type type = new TypeToken<List<songObject>>() {
//        }.getType();
//        ArrayList<songObject> listFavourites = gson.fromJson(json, type);
//
//        return listFavourites;
//    }
//
//    private void setFavouritesListToSP(ArrayList<songObject> listFavourites) {
//        String jsonFavourites = null;
//
////        removing duplicate records
//        Set<songObject> set = new HashSet<songObject>();
//        set.addAll(listFavourites);
//        listFavourites.clear();
//        listFavourites.addAll(set);
//
//        Gson gson = new Gson();
//        if (listFavourites != null && listFavourites.size() > 0) {
//            jsonFavourites = gson.toJson(listFavourites);
//        }
//        prefManagerAppData.setFavourites(jsonFavourites);
//    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_aarti_details, menu);
//
//        MenuItem menuMarathi = menu.findItem(R.id.menu_marathi);
//        MenuItem menuHindi = menu.findItem(R.id.menu_hindi);
//
//        menuHindi.setChecked(true);
//
//
//        return true;
//    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_song_details, menu);

//        if(menu instanceof MenuBuilder){
//            MenuBuilder m = (MenuBuilder) menu;
//            m.setOptionalIconsVisible(true);
//        }

        // Find the menuItem to add your SubMenu
        MenuItem menuItemShare = menu.findItem(R.id.menu_share);
        SubMenu subMenuShare = menuItemShare.getSubMenu();

        MenuItem submenuItemShareText = subMenuShare.findItem(R.id.submenu_share_text);
        MenuItem submenuItemSharePDF = subMenuShare.findItem(R.id.submenu_share_pdf);

//        // Inflating the sub_menu menu this way, will add its menu items
//        // to the empty SubMenu you created in the xml
//        getMenuInflater().inflate(R.menu.sub_menu, myMenuItem.getSubMenu());

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.menu_marathi:
//                return true;
//
//            case R.id.menu_hindi:
//                return true;

            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}