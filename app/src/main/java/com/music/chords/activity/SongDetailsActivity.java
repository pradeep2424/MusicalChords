package com.music.chords.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.gson.Gson;
import com.music.chords.R;
import com.music.chords.objects.SongObject;
import com.music.chords.utils.Application;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SongDetailsActivity extends AppCompatActivity {

    SongObject aartiObject;

//    int aartiSelectedPosition;

    ImageView ivCoverPic;
    TextView tvTitle;
    TextView tvSubtitle;
    TextView tvLyrics;

//    SparkButton heartButton;
    Button btnAudio;
    Button btnPictures;

//    private PrefManagerAppData prefManagerAppData;

    final public int REQUEST_CODE_PHOTOS = 100;
    final public int REQUEST_CODE_AUDIO = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        aartiObject = Application.songObject;

//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null) {
//            aartiObject = (AartiObject) bundle.getSerializable("AartiObject");
//            aartiSelectedPosition = bundle.getInt("SelectedPosition");
//        }

        init();
        componentEvents();
        setAartiData();
    }

    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setTitle(getResources().getString(R.string.title_aarti));

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, R.color.transparent));
        collapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.white));
        collapsingToolbarLayout.setTitle(aartiObject.getSongTitle());

//        prefManagerAppData = new PrefManagerAppData(this);

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
//                if (aartiObject.getIsFavourites()) {
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

    private void setAartiData() {
        if (aartiObject != null) {

            Glide.with(this).load(aartiObject.getSongYouTubeURL()).into(ivCoverPic);
            tvTitle.setText(aartiObject.getSongTitle());
            tvSubtitle.setText(aartiObject.getSongSubtitle());
            tvLyrics.setText(aartiObject.getSongLyrics());
        }
    }

    //    @Override
//    public void liked() {
//        try {
//            ArrayList<AartiObject> listFavourites = new ArrayList<>();
//
//            String json = prefManagerAppData.getFavourites();
//            if (!json.equalsIgnoreCase("NotFound")) {
//                listFavourites = getFavouritesListFromSP();
//            }
//
//            AartiObject favouriteAartiObject = (AartiObject) SerializationUtils.clone(aartiObject);
//
//            favouriteAartiObject.setIsFavourites(true);
//            listFavourites.add(favouriteAartiObject);
//
//            AartiApplication.allAartiData.set(AartiApplication.allAartiData.indexOf(aartiObject), favouriteAartiObject);
//
////        AartiApplication.allAartiData.set(aartiSelectedPosition, aartiObject);
//
//            setFavouritesListToSP(listFavourites);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    //    @Override
//    public void unLiked() {
//        ArrayList<AartiObject> listNewFavourites = new ArrayList<>();
//
//        String json = prefManagerAppData.getFavourites();
//        if (!json.equalsIgnoreCase("NotFound")) {
//            listNewFavourites = getFavouritesListFromSP();
//        }
//
//        AartiObject removedFavouriteObject = (AartiObject) SerializationUtils.clone(aartiObject);
//        if (listNewFavourites != null) {
//            listNewFavourites.remove(removedFavouriteObject);
//        }
//
//        removedFavouriteObject.setIsFavourites(false);
//        AartiApplication.allAartiData.set(AartiApplication.allAartiData.indexOf(aartiObject), removedFavouriteObject);
//
////        saving new favourite list to shared preference
//        setFavouritesListToSP(listNewFavourites);
//    }
//
//
//    private ArrayList<AartiObject> getFavouritesListFromSP() {
//        String json = prefManagerAppData.getFavourites();
//        Gson gson = new Gson();
//        Type type = new TypeToken<List<AartiObject>>() {
//        }.getType();
//        ArrayList<AartiObject> listFavourites = gson.fromJson(json, type);
//
//        return listFavourites;
//    }
//
//    private void setFavouritesListToSP(ArrayList<AartiObject> listFavourites) {
//        String jsonFavourites = null;
//
////        removing duplicate records
//        Set<AartiObject> set = new HashSet<AartiObject>();
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