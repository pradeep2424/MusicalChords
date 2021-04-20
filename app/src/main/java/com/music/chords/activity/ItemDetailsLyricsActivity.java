//package com.music.chords.activity;
//
//import android.app.AlertDialog;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.pm.ActivityInfo;
//import android.content.res.ColorStateList;
//import android.graphics.drawable.Drawable;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.PowerManager;
//import android.text.Spannable;
//import android.text.SpannableStringBuilder;
//import android.text.Spanned;
//import android.text.method.LinkMovementMethod;
//import android.text.style.ImageSpan;
//import android.util.TypedValue;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.PopupWindow;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import androidx.coordinatorlayout.widget.CoordinatorLayout;
//import androidx.core.content.ContextCompat;
//import androidx.core.widget.NestedScrollView;
//
//import com.google.android.material.appbar.AppBarLayout;
//import com.google.android.material.appbar.CollapsingToolbarLayout;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.marcoscg.dialogsheet.DialogSheet;
//import com.music.chords.R;
//import com.music.chords.chordsReader.chords.Chord;
//import com.music.chords.chordsReader.chords.NoteNaming;
//import com.music.chords.chordsReader.chords.regex.ChordInText;
//import com.music.chords.chordsReader.chords.regex.ChordParser;
//import com.music.chords.chordsReader.data.ColorScheme;
//import com.music.chords.chordsReader.helper.ChordDictionary;
//import com.music.chords.chordsReader.helper.DialogHelper;
//import com.music.chords.chordsReader.helper.PopupHelper;
//import com.music.chords.chordsReader.helper.PreferenceHelper;
//import com.music.chords.chordsReader.helper.TransposeHelper;
//import com.music.chords.chordsReader.helper.WebPageExtractionHelper;
//import com.music.chords.chordsReader.utils.InternalURLSpan;
//import com.music.chords.chordsReader.utils.Pair;
//import com.music.chords.chordsReader.utils.UtilLogger;
//import com.music.chords.helper.FullScreenHelper;
//import com.music.chords.interfaces.Constants;
//import com.music.chords.objects.SongObject;
//import com.music.chords.sharedPreference.AppSharedPreference;
//import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
//import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
//import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener;
//import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
//import com.travijuu.numberpicker.library.Enums.ActionEnum;
//import com.travijuu.numberpicker.library.Listener.DefaultValueChangedListener;
//import com.travijuu.numberpicker.library.NumberPicker;
//import com.warkiz.widget.IndicatorSeekBar;
//import com.warkiz.widget.OnSeekChangeListener;
//import com.warkiz.widget.SeekParams;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.TimerTask;
//
//public class ItemDetailsLyricsActivity extends AppCompatActivity implements Constants {
//    SongObject songObject;
//
//    CoordinatorLayout clRootLayout;
//    AppBarLayout appBarLayout;
//    NestedScrollView nestedScrollView;
//    FloatingActionButton fabExitFullScreen;
//    //    ImageView ivCoverPic;
////    TextView tvToolbarTitle;
//    FullScreenHelper fullScreenHelper;
//    YouTubePlayerView youTubePlayerView;
//    TextView tvTitle;
//    TextView tvSubtitle;
//    TextView tvLyrics;
//
//    View viewAutoScrollBottomSeekBar;
//    IndicatorSeekBar seekBar;
//    ImageView ivPlayPause;
//    ImageView ivCancel;
//
//    private PowerManager.WakeLock wakeLock;
//    private Handler autoScrollHandler;
//
//    private boolean isPlaying = false;
//
//    private UtilLogger log = new UtilLogger(ItemDetailsChordsActivity.class);
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_product_details);
//
//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null) {
//            songObject = (SongObject) bundle.getSerializable(SONG_OBJECT);
//        }
//
//        init();
//        componentEvents();
//        initYouTubeViewPlayer();
//        setSongData();
//        setFontSizeToLyrics();
//        setAutoScrollSpeed();
//        applyColorScheme();
//    }
//
//    private void init() {
//        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
//        wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, getPackageName());
//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//
////        getSupportActionBar().setTitle(getResources().getString(R.string.title_aarti));
//
//        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
//        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, R.color.transparent));
//        collapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.white));
//
//        if (songObject != null && songObject.getSongTitle() != null) {
//            collapsingToolbarLayout.setTitle(songObject.getSongTitle());
//        }
//
////        prefManagerAppData = new PrefManagerAppData(this);
//
//        clRootLayout = findViewById(R.id.cl_rootLayout);
//        appBarLayout = findViewById(R.id.appBar);
////        tvToolbarTitle = findViewById(R.id.tv_toolbarTitle);
//        nestedScrollView = findViewById(R.id.nestedScrollView);
//        fabExitFullScreen = findViewById(R.id.fab_exitFullScreen);
////        ivCoverPic =  findViewById(R.id.iv_coverPic);
//        tvTitle = findViewById(R.id.tv_title);
//        tvSubtitle = findViewById(R.id.tv_subtitle);
//        tvLyrics = findViewById(R.id.tv_lyricsText);
////        tvLyrics.setTextSize(TypedValue.COMPLEX_UNIT_SP, PreferenceHelper.getTextSizePreference(this));
//        tvLyrics.setTextSize(TypedValue.COMPLEX_UNIT_SP, AppSharedPreference.SSP().getFontSize());
//        youTubePlayerView = findViewById(R.id.youtube_player_view);
//
//        viewAutoScrollBottomSeekBar = findViewById(R.id.view_autoScrollSetup);
//        ivPlayPause = viewAutoScrollBottomSeekBar.findViewById(R.id.iv_playPause);
//        ivCancel = viewAutoScrollBottomSeekBar.findViewById(R.id.iv_cancel);
//        seekBar = viewAutoScrollBottomSeekBar.findViewById(R.id.seekBar_speed);
//        seekBar.setIndicatorTextFormat("${TICK_TEXT} --");
//    }
//
//    private void initYouTubeViewPlayer() {
//        AsyncTask.execute(new Runnable() {
//            @Override
//            public void run() {
//                fullScreenHelper = new FullScreenHelper(ItemDetailsLyricsActivity.this);
//
//                getLifecycle().addObserver(youTubePlayerView);
//                youTubePlayerView.getPlayerUiController().showMenuButton(false);
//                youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
//                    @Override
//                    public void onReady(@NonNull YouTubePlayer youTubePlayer) {
//                        String videoId = "KPLWWIOCOOQ";
//                        youTubePlayer.cueVideo(videoId, 0);
////                youTubePlayer.loadVideo(videoId, 0);
//                    }
//                });
//            }
//        });
//
//    }
//
//    private void componentEvents() {
//        youTubePlayerView.addFullScreenListener(new YouTubePlayerFullScreenListener() {
//            @Override
//            public void onYouTubePlayerEnterFullScreen() {
//                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                fullScreenHelper.enterFullScreen();
//
////                youTubePlayerView.enterFullScreen();
//            }
//
//            @Override
//            public void onYouTubePlayerExitFullScreen() {
//                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                fullScreenHelper.exitFullScreen();
//
////                youTubePlayerView.exitFullScreen();
//            }
//        });
//
//        fabExitFullScreen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                exitFullScreenMode();
//            }
//        });
//
//        seekBar.setOnSeekChangeListener(new OnSeekChangeListener() {
//            @Override
//            public void onSeeking(SeekParams seekParams) {
//                stopAutoScrollPage();
//
//                double scrollSpeed = seekParams.progressFloat;
//                startAutoScrollPage(scrollSpeed);
//            }
//
//            @Override
//            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
//
//            }
//        });
//
//        ivPlayPause.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isPlaying) {
//                    ivPlayPause.setImageDrawable(ContextCompat.getDrawable(ItemDetailsLyricsActivity.this, R.drawable.ic_play));
//                    stopAutoScrollPage();
//
//                } else {
//                    ivPlayPause.setImageDrawable(ContextCompat.getDrawable(ItemDetailsLyricsActivity.this, R.drawable.ic_pause));
//
//                    double scrollSpeed = seekBar.getProgressFloat();
//                    startAutoScrollPage(scrollSpeed);
//                }
//            }
//        });
//
//        ivCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                viewAutoScrollBottomSeekBar.setVisibility(View.GONE);
//            }
//        });
//
////        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
////            @Override
////            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
////                if (!v.canScrollVertically(1)) {
////                    // bottom of scroll view
////
////                    if(isPlaying) {
////                        stopAutoScrollPage();
////                        Toast.makeText(ItemDetailsActivity.this, getString(R.string.end_of_song), Toast.LENGTH_SHORT).show();
////                    }
////                }
////                if (!v.canScrollVertically(-1)) {
////                    // top of scroll view
////                }
////            }
////        });
//    }
//
//    private void setSongData() {
//        if (songObject != null) {
//
////            Glide.with(this).load(songObject.getSongYouTubeURL()).into(ivCoverPic);
//            tvTitle.setText(songObject.getSongTitle());
//            tvSubtitle.setText(songObject.getSongSubtitle());
//            tvLyrics.setText(songObject.getSongLyrics());
//        }
//    }
//
//    private void setFontSizeToLyrics() {
//        int fontSize = AppSharedPreference.SSP().getFontSize();
//        tvLyrics.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
//    }
//
//    private void setAutoScrollSpeed() {
//        float scrollSpeed = AppSharedPreference.SSP().getAutoScrollSpeed();
//        seekBar.setProgress(scrollSpeed);
//    }
//
////    private void initPowerMenu() {
////        optionMenu = getSongDetailsMenu(this, this, onOptionMenuClickListener);
////    }
////
////    public PowerMenu getSongDetailsMenu(
////            Context context,
////            LifecycleOwner lifecycleOwner,
////            OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener) {
////
////        String fullScreen = getString(R.string.action_fullscreen);
////        String textSize = getString(R.string.action_font_size);
////        String autoScroll = getString(R.string.action_auto_scroll);
////        String transpose = getString(R.string.action_transpose);
////        String share = getString(R.string.action_share);
////        String shareText = getString(R.string.action_share_text);
////        String sharePDF = getString(R.string.action_share_pdf);
////
////        return new PowerMenu.Builder(context)
////                .addItem(new PowerMenuItem(fullScreen, R.drawable.ic_fullscreen, false))
////                .addItem(new PowerMenuItem(textSize, R.drawable.ic_text_size, false))
////                .addItem(new PowerMenuItem(autoScroll, R.drawable.ic_auto_scroll, false))
////                .addItem(new PowerMenuItem(transpose, R.drawable.ic_transpose, false))
////                .addItem(new PowerMenuItem(share, false))
////                .addItem(new PowerMenuItem(shareText, R.drawable.ic_share_text, false))
////                .addItem(new PowerMenuItem(sharePDF, R.drawable.ic_share_pdf, false))
////                .setAutoDismiss(true)
////                .setLifecycleOwner(lifecycleOwner)
////                .setAnimation(MenuAnimation.SHOWUP_TOP_RIGHT)
////                .setCircularEffect(CircularEffect.BODY)
////                .setMenuRadius(10f)
////                .setMenuShadow(10f)
////                .setTextColor(ContextCompat.getColor(context, R.color.main_text))
////                .setTextSize(14)
////                .setTextGravity(Gravity.CENTER)
////                .setTextTypeface(Typeface.create("sans-serif-medium", Typeface.BOLD))
////                .setSelectedTextColor(Color.WHITE)
////                .setMenuColor(Color.WHITE)
////                .setSelectedMenuColor(ContextCompat.getColor(context, R.color.colorPrimary))
////                .setOnMenuItemClickListener(onMenuItemClickListener)
//////                .setOnDismissListener(onDismissedListener)
////                .setPreferenceName("HamburgerPowerMenu")
////                .setInitializeRule(Lifecycle.Event.ON_CREATE, 0)
////                .build();
////    }
//
//
//    //    @Override
////    public void liked() {
////        try {
////            ArrayList<songObject> listFavourites = new ArrayList<>();
////
////            String json = prefManagerAppData.getFavourites();
////            if (!json.equalsIgnoreCase("NotFound")) {
////                listFavourites = getFavouritesListFromSP();
////            }
////
////            songObject favouriteSongObject = (songObject) SerializationUtils.clone(songObject);
////
////            favouriteSongObject.setIsFavourites(true);
////            listFavourites.add(favouriteSongObject);
////
////            SongApplication.allSongData.set(SongApplication.allSongData.indexOf(songObject), favouriteSongObject);
////
//////        SongApplication.allSongData.set(songSelectedPosition, songObject);
////
////            setFavouritesListToSP(listFavourites);
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////    }
//
//    //    @Override
////    public void unLiked() {
////        ArrayList<songObject> listNewFavourites = new ArrayList<>();
////
////        String json = prefManagerAppData.getFavourites();
////        if (!json.equalsIgnoreCase("NotFound")) {
////            listNewFavourites = getFavouritesListFromSP();
////        }
////
////        songObject removedFavouriteObject = (songObject) SerializationUtils.clone(songObject);
////        if (listNewFavourites != null) {
////            listNewFavourites.remove(removedFavouriteObject);
////        }
////
////        removedFavouriteObject.setIsFavourites(false);
////        SongApplication.allSongData.set(SongApplication.allSongData.indexOf(songObject), removedFavouriteObject);
////
//////        saving new favourite list to shared preference
////        setFavouritesListToSP(listNewFavourites);
////    }
////
////
////    private ArrayList<songObject> getFavouritesListFromSP() {
////        String json = prefManagerAppData.getFavourites();
////        Gson gson = new Gson();
////        Type type = new TypeToken<List<songObject>>() {
////        }.getType();
////        ArrayList<songObject> listFavourites = gson.fromJson(json, type);
////
////        return listFavourites;
////    }
////
////    private void setFavouritesListToSP(ArrayList<songObject> listFavourites) {
////        String jsonFavourites = null;
////
//////        removing duplicate records
////        Set<songObject> set = new HashSet<songObject>();
////        set.addAll(listFavourites);
////        listFavourites.clear();
////        listFavourites.addAll(set);
////
////        Gson gson = new Gson();
////        if (listFavourites != null && listFavourites.size() > 0) {
////            jsonFavourites = gson.toJson(listFavourites);
////        }
////        prefManagerAppData.setFavourites(jsonFavourites);
////    }
//
//
////    @Override
////    public boolean onCreateOptionsMenu(Menu menu) {
////        // Inflate the menu; this adds items to the action bar if it is present.
////        getMenuInflater().inflate(R.menu.menu_aarti_details, menu);
////
////        MenuItem menuMarathi = menu.findItem(R.id.menu_marathi);
////        MenuItem menuHindi = menu.findItem(R.id.menu_hindi);
////
////        menuHindi.setChecked(true);
////
////
////        return true;
////    }
//
//    private void applyColorScheme() {
//        ColorScheme colorScheme = PreferenceHelper.getColorScheme(this);
//
////        messageTextView.setTextColor(colorScheme.getForegroundColor(this));
//        tvLyrics.setTextColor(colorScheme.getForegroundColor(this));
//        clRootLayout.setBackgroundColor(colorScheme.getBackgroundColor(this));
//        tvLyrics.setLinkTextColor(ColorStateList.valueOf(colorScheme.getLinkColor(this)));
//
////        messageSecondaryView.setBackgroundResource(colorScheme.getSelectorResource());
//    }
//
//    private boolean isInViewingMode() {
//        return nestedScrollView.getVisibility() == View.VISIBLE;
//    }
//
//    private void showBottomSheetDialogFontSize() {
//        int fontSize = AppSharedPreference.SSP().getFontSize();
//
//        View view = View.inflate(this, R.layout.layout_sheet_text_size, null);
//        DialogSheet dialogSheet = new DialogSheet(this, true)
//                .setView(view)
//                .setCancelable(true);
//        dialogSheet.show();
//
//        LinearLayout llDone = view.findViewById(R.id.ll_done);
//        TextView tvSampleText = view.findViewById(R.id.tv_sampleText);
//        NumberPicker numberPicker = view.findViewById(R.id.numberPicker_textSize);
//        numberPicker.setValue(fontSize);
//
//        numberPicker.setValueChangedListener(new DefaultValueChangedListener() {
//            @Override
//            public void valueChanged(int value, ActionEnum action) {
//                super.valueChanged(value, action);
//
//                tvSampleText.setTextSize(TypedValue.COMPLEX_UNIT_SP, value);
//                tvLyrics.setTextSize(TypedValue.COMPLEX_UNIT_SP, value);
//            }
//        });
//
//        llDone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialogSheet.dismiss();
//            }
//        });
//    }
//
//    private void showAutoScrollBoomSeekBar() {
//        viewAutoScrollBottomSeekBar.setVisibility(View.VISIBLE);
//    }
//
//    private void hideAutoScrollBoomSeekBar() {
//        viewAutoScrollBottomSeekBar.setVisibility(View.GONE);
//    }
//
//    private void startAutoScrollPage(double scrollSpeed) {
//        isPlaying = true;
//
//        int pixelScroll = (int) Math.round(scrollSpeed * 5);
//
//        autoScrollHandler = new Handler();
//        Runnable timerRunnable = new Runnable() {
//            @Override
//            public void run() {
////                nestedScrollView.smoothScrollBy(0, 5);         // 1 is how many pixels you want it to scroll vertically by
//                nestedScrollView.smoothScrollBy(0, pixelScroll);         // 1 is how many pixels you want it to scroll vertically by
//                autoScrollHandler.postDelayed(this, 40);     // 40 is how many milliseconds you want this thread to run
//            }
//        };
//        autoScrollHandler.postDelayed(timerRunnable, 0);
//    }
//
//    private void stopAutoScrollPage() {
//        isPlaying = false;
//
//        if (autoScrollHandler != null) {
//            autoScrollHandler.removeCallbacksAndMessages(null);
//        }
//    }
//
//    public void expandToolbar() {
//        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
//        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
//        if (behavior != null) {
//            behavior.onNestedFling(clRootLayout, appBarLayout, null, 0, -10000, false);
//        }
//    }
//
//    private void enterFullScreenMode() {
//        fabExitFullScreen.setVisibility(View.VISIBLE);
//
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
//        params.height = 0;
//        appBarLayout.setLayoutParams(params);
//    }
//
//    private void exitFullScreenMode() {
//        fabExitFullScreen.setVisibility(View.GONE);
//
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
//
//        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
//        params.height = CoordinatorLayout.LayoutParams.WRAP_CONTENT;
//        appBarLayout.setLayoutParams(params);
//    }
//
//    private void updateMenuWithIcon(@NonNull final MenuItem item, final int color) {
//        SpannableStringBuilder builder = new SpannableStringBuilder()
//                .append("*") // the * will be replaced with the icon via ImageSpan
//                .append("    ") // This extra space acts as padding. Adjust as you wish
//                .append(item.getTitle());
//
//        // Retrieve the icon that was declared in XML and assigned during inflation
//        if (item.getIcon() != null && item.getIcon().getConstantState() != null) {
//            Drawable drawable = item.getIcon().getConstantState().newDrawable();
//
//            // Mutate this drawable so the tint only applies here
//            drawable.mutate().setTint(color);
//
//            // Needs bounds, or else it won't show up (doesn't know how big to be)
//            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
//            ImageSpan imageSpan = new ImageSpan(drawable);
//            builder.setSpan(imageSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            item.setTitle(builder);
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_song_details, menu);
//
////        if (menu instanceof MenuBuilder) {
////            MenuBuilder m = (MenuBuilder) menu;
////            m.setOptionalIconsVisible(true);
////        }
//
//        MenuItem menuItemFullscreen = menu.findItem(R.id.menu_fullscreen_lyrics);
//        MenuItem menuItemTextSize = menu.findItem(R.id.menu_text_size);
//        MenuItem menuItemAutoScroll = menu.findItem(R.id.menu_auto_scroll);
//        MenuItem menuItemTranspose = menu.findItem(R.id.menu_transpose);
//        MenuItem menuItemShare = menu.findItem(R.id.menu_share);
//        menuItemTranspose.setVisible(false);
//
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.menu_fullscreen_lyrics:
//                enterFullScreenMode();
//                return true;
//
//            case R.id.menu_text_size:
//                showBottomSheetDialogFontSize();
//                return true;
//
//            case R.id.menu_auto_scroll:
//                showAutoScrollBoomSeekBar();
////                startAutoScrollPage();
//                return true;
//
////            case R.id.menu_transpose:
////                showBottomSheetDialogTranspose();
////                return true;
//
//            case R.id.menu_share:
//                return true;
//
//            case android.R.id.home:
//                onBackPressed();
//                return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//    @Override
//    public void onBackPressed() {
////        super.onBackPressed();
//
//        Intent intent = new Intent();
//        setResult(RESULT_OK, intent);
//        finish();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (wakeLock.isHeld()) {
//            log.d("Releasing wakelock");
//            wakeLock.release();
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        // just in case the text size has changed
////        tvLyrics.setTextSize(TypedValue.COMPLEX_UNIT_SP, PreferenceHelper.getTextSizePreference(this));
//
//        if (isInViewingMode() && !wakeLock.isHeld()) {
//            log.d("Acquiring wakelock");
//            wakeLock.acquire();
//        }
//
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        youTubePlayerView.release();
//    }
//}