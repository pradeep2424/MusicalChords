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
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import androidx.coordinatorlayout.widget.CoordinatorLayout;
//import androidx.core.content.ContextCompat;
//import androidx.core.widget.NestedScrollView;
//
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
//import android.widget.LinearLayout;
//import android.widget.PopupWindow;
//import android.widget.TextView;
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
//import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
//import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
//import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener;
//import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
//import com.travijuu.numberpicker.library.Enums.ActionEnum;
//import com.travijuu.numberpicker.library.Listener.DefaultValueChangedListener;
//import com.travijuu.numberpicker.library.NumberPicker;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.TimerTask;
//
//public class SongDetailsActivity extends AppCompatActivity implements Constants, View.OnTouchListener {
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
////    private PowerMenu optionMenu;
////    private OnMenuItemClickListener<PowerMenuItem> onOptionMenuClickListener;
//
//    private PowerManager.WakeLock wakeLock;
//    private Handler autoScrollHandler;
//
//    private float lastXCoordinate;
//    private float lastYCoordinate;
//
//    private int capoFret = 0;
//    private int transposeHalfSteps = 0;
//    private volatile String chordText;
//    private List<ChordInText> chordsInText;
//
//    private static final int PROGRESS_DIALOG_MIN_TIME = 600;
//    private static final int CHORD_POPUP_Y_OFFSET_IN_SP = 24;
//
//    private UtilLogger log = new UtilLogger(SongDetailsActivity.class);
////    SparkButton heartButton;
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
////        initYouTubeViewPlayer();
//        componentEvents();
//        setSongData();
//        initializeChordDictionary();
//        applyColorScheme();
//
//
//        if (songObject != null && songObject.getSongLyrics() != null) {
//            chordText = songObject.getSongLyrics();
//        }
//
////        showConfirmChordchartDialog();
//
//
////        chordText = "G                     Em\n" +
////                "Dil ka dariya beh hi gaya\n" +
////                "  C                      D\n" +
////                "Raahon mein yun jo tu mil gaya\n" +
////                "     G                   Em\n" +
////                "Mushqil se main sambhla tha haan\n" +
////                "       C               D\n" +
////                "Toot gaya hoon phir ek dafaa\n" +
////                "        C            Bm\n" +
////                "Baat bigdi hai iss qadar\n" +
////                "        Am               D\n" +
////                "Dil hai toota, toote hain hum\n" +
////                " \n" +
////                "      Am                    D\n" +
////                "Tere bin ab na lenge ek bhi dum\n" +
////                "       C      D          G\n" +
////                "Tujhe kitna chaahein aur hum\n" +
////                "     Am                    D\n" +
////                "Tere bin ab na lenge ek bhi dum\n" +
////                "     C         D           G\n" +
////                "Tujhe kitna chaahein aur hum\n" +
////                "    C                 Bm\n" +
////                "Baat bigdi hai iss qadar\n" +
////                "        Am                D\n" +
////                "Dil hai toota, toote hain hum\n" +
////                "    Am                    D\n" +
////                "Tere bin ab na lenge ek bhi dum\n" +
////                "C           D           G\n" +
////                "Tujhe kitna chaahein aur hum\n" +
////                "Am                          D\n" +
////                "Tere bin ab na lenge ek bhi dum\n" +
////                "C         D            G\n" +
////                "Tujhe kitna chaahein aur hum";
//
////		showConfirmChordchartDialog(true);
//
//        analyzeChordsAndShowChordView();
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
//        tvLyrics.setTextSize(TypedValue.COMPLEX_UNIT_SP, PreferenceHelper.getTextSizePreference(this));
//        youTubePlayerView = findViewById(R.id.youtube_player_view);
//
////        tvToolbarTitle.setText(songObject.getSongTitle());
//
////        heartButton = (SparkButton) findViewById(R.id.heart_button);
////        btnAudio = (Button) findViewById(R.id.btn_audio);
////        btnPictures = (Button) findViewById(R.id.btn_pictures);
//
////        final Handler handler = new Handler();
////        handler.postDelayed(new Runnable() {
////            @Override
////            public void run() {
////                if (songObject.getIsFavourites()) {
////                    heartButton.setChecked(true);
////                    heartButton.playAnimation();
////                }
////            }
////        }, 500);
//    }
//
//    private void initYouTubeViewPlayer() {
//        fullScreenHelper = new FullScreenHelper(this);
//
//        getLifecycle().addObserver(youTubePlayerView);
//        youTubePlayerView.getPlayerUiController().showMenuButton(false);
//        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
//            @Override
//            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
//                String videoId = "KPLWWIOCOOQ";
//                youTubePlayer.cueVideo(videoId, 0);
////                youTubePlayer.loadVideo(videoId, 0);
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
////        heartButton.setEventListener(new SparkEventListener(){
////            @Override
////            public void onEvent(ImageView button, boolean buttonState) {
////                if (buttonState) {
////                    // Button is active
////                    liked();
////                } else {
////                    // Button is inactive
////                    unLiked();
////                }
////            }
////
////            @Override
////            public void onEventAnimationEnd(ImageView button, boolean buttonState) {
////
////            }
////
////            @Override
////            public void onEventAnimationStart(ImageView button, boolean buttonState) {
////
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
//
//    private void initializeChordDictionary() {
//        // do in the background to avoid jank
//        new AsyncTask<Void, Void, Void>() {
//
//            @Override
//            protected Void doInBackground(Void... params) {
//                ChordDictionary.initialize(SongDetailsActivity.this);
//                return null;
//            }
//        }.execute((Void) null);
//
//    }
//
//    private void showConfirmChordchartDialog() {
//        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final EditText editText = (EditText) inflater.inflate(R.layout.confirm_chords_edit_text, null);
//        editText.setText(chordText);
//
//        new AlertDialog.Builder(SongDetailsActivity.this)
//                .setTitle(R.string.confirm_chords)
//                .setView(editText)
//                .setCancelable(true)
//                .setNegativeButton(android.R.string.cancel, null)
//                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        chordText = editText.getText().toString();
//                        switchToViewingMode();
//
////                        analyzeHtml();
//                    }
//                })
//                .create()
//                .show();
//    }
//
//    private void analyzeHtml() {
//
////        if (chordWebpage != null) {
////            // known webpage
////
////            log.d("known web page: %s", chordWebpage);
////
////            chordText = WebPageExtractionHelper.extractChordChart(
////                    chordWebpage, html, getNoteNaming());
////        } else {
////            // unknown webpage
//
//        log.d("unknown webpage");
//
//        chordText = WebPageExtractionHelper.extractLikelyChordChart(chordText, getNoteNaming());
//
//
//        if (chordText == null) { // didn't find a good extraction, so use the entire html
//
//            log.d("didn't find a good chord chart using the <pre> tag");
//
//            chordText = WebPageExtractionHelper.convertHtmlToText(chordText);
//        }
////        }
//
////        showConfirmChordchartDialog();
//        switchToViewingMode();
//    }
//
//
//    private void switchToViewingMode() {
//        wakeLock.acquire();
////        resetDataExceptChordTextAndFilename();
//
//        capoFret = 0;
//        transposeHalfSteps = 0;
//        analyzeChordsAndShowChordView();
//    }
//
//    private void analyzeChordsAndShowChordView() {
//        chordsInText = ChordParser.findChordsInText(chordText, getNoteNaming());
//
//        log.d("found %d chords", chordsInText.size());
//        showChordView();
//    }
//
//    private NoteNaming getNoteNaming() {
//        return PreferenceHelper.getNoteNaming(this);
//    }
//
//    private void showChordView() {
//        // do in the background to avoid jankiness
//        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setTitle(R.string.loading_title);
//        progressDialog.setMessage(getText(R.string.please_wait));
//        progressDialog.setIndeterminate(true);
//
//        AsyncTask<Void, Void, Spannable> task = new AsyncTask<Void, Void, Spannable>() {
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                progressDialog.show();
//            }
//
//            @Override
//            protected Spannable doInBackground(Void... params) {
//                long start = System.currentTimeMillis();
//
//                if (capoFret != 0 || transposeHalfSteps != 0) {
//                    updateChordsInTextForTransposition(-transposeHalfSteps, -capoFret);
//                }
//
//                Spannable newText = buildUpChordTextToDisplay();
//
//                long elapsed = System.currentTimeMillis() - start;
//
//                if (elapsed < PROGRESS_DIALOG_MIN_TIME) {
//                    // show progressdialog for at least 1 second, or else it goes by too fast
//                    // XXX: this is a weird UI hack, but I don't know what else to do
//                    try {
//                        Thread.sleep(PROGRESS_DIALOG_MIN_TIME - elapsed);
//                    } catch (InterruptedException e) {
//                        log.e(e, "unexpected exception");
//                    }
//                }
//                return newText;
//            }
//
//            @Override
//            protected void onPostExecute(Spannable newText) {
//                super.onPostExecute(newText);
//
//                applyLinkifiedChordsTextToTextView(newText);
//                progressDialog.dismiss();
//            }
//        };
//
//        task.execute((Void) null);
//    }
//
//    private void updateChordsInTextForTransposition(int transposeDiff, int capoDiff) {
//        for (ChordInText chordInText : chordsInText) {
//
//            chordInText.setChord(TransposeHelper.transposeChord(
//                    chordInText.getChord(), capoDiff, transposeDiff));
//        }
//    }
//
//    private Spannable buildUpChordTextToDisplay() {
//        // have to build up a new string, because some of the chords may have different string lengths
//        // than in the original text (e.g. if they are transposed)
//        int lastEndIndex = 0;
//        StringBuilder sb = new StringBuilder();
//
//        List<Pair<Integer, Integer>> newStartAndEndPositions =
//                new ArrayList<Pair<Integer, Integer>>(chordsInText.size());
//
//        for (ChordInText chordInText : chordsInText) {
//
//            //log.d("chordInText is %s", chordInText);
//
//            sb.append(chordText.substring(lastEndIndex, chordInText.getStartIndex()));
//
//            String chordAsString = chordInText.getChord().toPrintableString(getNoteNaming());
//            sb.append(chordAsString);
//
//            newStartAndEndPositions.add(new Pair<Integer, Integer>(
//                    sb.length() - chordAsString.length(), sb.length()));
//
//            lastEndIndex = chordInText.getEndIndex();
//        }
//
//        // append the last bit of text after the last chord
//        sb.append(chordText.substring(lastEndIndex, chordText.length()));
//
//        Spannable spannable = new Spannable.Factory().newSpannable(sb.toString());
//
//        //log.d("new start and end positions are: %s", newStartAndEndPositions);
//
//        // add a hyperlink to each chord
//        for (int i = 0; i < newStartAndEndPositions.size(); i++) {
//
//            Pair<Integer, Integer> newStartAndEndPosition = newStartAndEndPositions.get(i);
//
//            //log.d("pair is %s", newStartAndEndPosition);
//            //log.d("substr is '%s'", sb.substring(
//            //		newStartAndEndPosition.getFirst(), newStartAndEndPosition.getSecond()));
//
//            final Chord chord = chordsInText.get(i).getChord();
//
//            InternalURLSpan urlSpan = new InternalURLSpan(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    showChordPopup(chord);
//                }
//            });
//
//            spannable.setSpan(urlSpan,
//                    newStartAndEndPosition.getFirst(),
//                    newStartAndEndPosition.getSecond(),
//                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }
//
//        return spannable;
//    }
//
//    private void applyLinkifiedChordsTextToTextView(Spannable newText) {
//        tvLyrics.setMovementMethod(LinkMovementMethod.getInstance());
//        tvLyrics.setText(newText);
//    }
//
//    private void showChordPopup(Chord chord) {
//        if (!ChordDictionary.isInitialized()) {
//            // it could take a second or two to initialize, so just wait until then...
//            return;
//        }
//
//        final PopupWindow window = PopupHelper.newBasicPopupWindow(this);
//
//
//        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//        View view = inflater.inflate(R.layout.chord_popup, null);
//        TextView textView = (TextView) view.findViewById(android.R.id.text1);
//        textView.setText(chord.toPrintableString(getNoteNaming()));
//
//        TextView textView2 = (TextView) view.findViewById(android.R.id.text2);
//        textView2.setText(createGuitarChordText(chord));
//
//        window.setContentView(view);
//
//        int[] textViewLocation = new int[2];
//        tvLyrics.getLocationOnScreen(textViewLocation);
//
//        int chordPopupOffset = Math.round(TypedValue.applyDimension(
//                TypedValue.COMPLEX_UNIT_SP, CHORD_POPUP_Y_OFFSET_IN_SP, getResources().getDisplayMetrics()));
//
//        int offsetX = Math.round(lastXCoordinate - textViewLocation[0]);
//        int offsetY = Math.max(0, Math.round(lastYCoordinate - textViewLocation[1]) - chordPopupOffset);
//
//        int heightOverride = getResources().getDimensionPixelSize(R.dimen.popup_height);
//
//        PopupHelper.showLikeQuickAction(window, view, tvLyrics, getWindowManager(), offsetX, offsetY, heightOverride);
//
//    }
//
//    private CharSequence createGuitarChordText(Chord chord) {
//        // TODO: have a better interface for switching between alternative ways of playing the same chord.
//        // For now, just build up a list and show everything at once.
//
//        List<String> guitarChords = ChordDictionary.getGuitarChordsForChord(chord);
//        // Given how the dictionary is read in, these chords should have the simplest ones first
//        // Just separate each with a number, if there is more than one
//
//        switch (guitarChords.size()) {
//            case 0:
//                return getString(R.string.no_guitar_chord_available);
//            case 1:
//                return guitarChords.get(0);
//            default:
//                // create a list
//                StringBuilder stringBuilder = new StringBuilder();
//                for (int i = 0; i < guitarChords.size(); i++) {
//                    stringBuilder
//                            .append(getString(R.string.variation))
//                            .append(' ')
//                            .append(i + 1)
//                            .append(": ")
//                            .append(guitarChords.get(i))
//                            .append('\n');
//                }
//                return stringBuilder.substring(0, stringBuilder.length() - 1); // cut off final newline
//        }
//    }
//
//    private void createTransposeDialog() {
//        final View view = DialogHelper.createTransposeDialogView(this, capoFret, transposeHalfSteps);
//        new AlertDialog.Builder(this)
//                .setTitle(R.string.transpose)
//                .setCancelable(true)
//                .setNegativeButton(android.R.string.cancel, null)
//                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // grab the user's chosen values for the capo and the transposition
//
//                        View transposeView = view.findViewById(R.id.transpose_include);
//                        View capoView = view.findViewById(R.id.capo_include);
//
//                        int newTransposeHalfSteps = DialogHelper.getSeekBarValue(transposeView) + DialogHelper.TRANSPOSE_MIN;
//                        int newCapoFret = DialogHelper.getSeekBarValue(capoView) + DialogHelper.CAPO_MIN;
//
//                        log.d("transposeHalfSteps is now %d", newTransposeHalfSteps);
//                        log.d("capoFret is now %d", newCapoFret);
//
//                        changeTransposeOrCapo(newTransposeHalfSteps, newCapoFret);
//
//                        dialog.dismiss();
//
//                    }
//                })
//                .setView(view)
//                .show();
//    }
//
//    protected void changeTransposeOrCapo(final int newTransposeHalfSteps, final int newCapoFret) {
//
////        // persist
////        if (filename != null) {
////            ChordReaderDBHelper dbHelper = new ChordReaderDBHelper(this);
////            dbHelper.saveTransposition(filename, newTransposeHalfSteps, newCapoFret);
////            dbHelper.close();
////        }
//
//        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setTitle(R.string.transposing);
//        progressDialog.setMessage(getText(R.string.please_wait));
//        progressDialog.setIndeterminate(true);
//
//        // transpose in background to avoid jankiness
//        AsyncTask<Void, Void, Spannable> task = new AsyncTask<Void, Void, Spannable>() {
//
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                progressDialog.show();
//            }
//
//            @Override
//            protected Spannable doInBackground(Void... params) {
//
//                long start = System.currentTimeMillis();
//
//
//                int capoDiff = capoFret - newCapoFret;
//                int transposeDiff = transposeHalfSteps - newTransposeHalfSteps;
//                capoFret = newCapoFret;
//                transposeHalfSteps = newTransposeHalfSteps;
//
//                updateChordsInTextForTransposition(transposeDiff, capoDiff);
//
//                Spannable chordTextSpannable = buildUpChordTextToDisplay();
//
//                long elapsed = System.currentTimeMillis() - start;
//
//                if (elapsed < PROGRESS_DIALOG_MIN_TIME) {
//                    // show progressdialog for at least 1 second, or else it goes by too fast
//                    // XXX: this is a weird UI hack, but I don't know what else to do
//                    try {
//                        Thread.sleep(PROGRESS_DIALOG_MIN_TIME - elapsed);
//                    } catch (InterruptedException e) {
//                        log.e(e, "unexpected exception");
//                    }
//                }
//
//
//                return chordTextSpannable;
//
//            }
//
//            @Override
//            protected void onPostExecute(Spannable chordText) {
//                super.onPostExecute(chordText);
//
//                applyLinkifiedChordsTextToTextView(chordText);
//                progressDialog.dismiss();
//            }
//
//
//        };
//
//        task.execute((Void) null);
//    }
//
//
//    private boolean isInViewingMode() {
//        return nestedScrollView.getVisibility() == View.VISIBLE;
//    }
//
//
////    private void resetDataExceptChordTextAndFilename() {
////
////
////        chordsInText = null;
////        if (filename != null) {
////            ChordReaderDBHelper dbHelper = new ChordReaderDBHelper(this);
////            Transposition transposition = dbHelper.findTranspositionByFilename(filename);
////            dbHelper.close();
////            if (transposition != null) {
////                capoFret = transposition.getCapo();
////                transposeHalfSteps = transposition.getTranspose();
////            } else {
////                capoFret = 0;
////                transposeHalfSteps = 0;
////            }
////        } else {
////            capoFret = 0;
////            transposeHalfSteps = 0;
////        }
////
////    }
//
//    private void showBottomSheetDialogTextSize() {
//        View view = View.inflate(this, R.layout.layout_sheet_text_size, null);
//
//        DialogSheet dialogSheet = new DialogSheet(this, true)
//                .setView(view)
//                .setCancelable(true);
//        dialogSheet.show();
//
//        NumberPicker numberPicker = view.findViewById(R.id.numberPicker_textSize);
//        LinearLayout llDone = view.findViewById(R.id.ll_done);
//        TextView tvSampleText = view.findViewById(R.id.tv_sampleText);
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
//
//
////        val dialogSheet = DialogSheet(this)
////                .setTitle(R.string.app_name)
////                .setMessage(R.string.lorem)
////                .setColoredNavigationBar(true)
////                .setTitleTextSize(20) // In SP
////                .setCancelable(false)
////                .setPositiveButton(android.R.string.ok) {
////            // Your action
////        }
////    .setNegativeButton(android.R.string.cancel) {
////            // Your action
////        }
////    .setNeutralButton("Neutral")
////                .setRoundedCorners(false) // Default value is true
////                .setBackgroundColor(Color.BLACK) // Your custom background color
////                .setButtonsColorRes(R.color.colorAccent) // You can use dialogSheetAccent style attribute instead
////                .setNeutralButtonColor(Color.WHITE)
////                .show()
//    }
//
//    private void showBottomSheetDialogTranspose() {
//        View view = View.inflate(this, R.layout.layout_sheet_transpose, null);
//
//        DialogSheet dialogSheet = new DialogSheet(this, true)
//                .setView(view)
//                .setCancelable(true);
//        dialogSheet.show();
//
//        NumberPicker numberPicker = view.findViewById(R.id.numberPicker_transpose);
//        LinearLayout llDone = view.findViewById(R.id.ll_done);
//
//        numberPicker.setValueChangedListener(new DefaultValueChangedListener() {
//            @Override
//            public void valueChanged(int value, ActionEnum action) {
//                super.valueChanged(value, action);
//
//                changeTransposeOrCapo(value, DialogHelper.CAPO_MIN);
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
//    private void starAutoScrollPage() {
//        autoScrollHandler = new Handler();
//        Runnable timerRunnable = new Runnable() {
//            @Override
//            public void run() {
////                nestedScrollView.smoothScrollBy(0, 5);         // 1 is how many pixels you want it to scroll vertically by
//                nestedScrollView.smoothScrollBy(0, 5);         // 1 is how many pixels you want it to scroll vertically by
//                autoScrollHandler.postDelayed(this, 100);     // 40 is how many milliseconds you want this thread to run
//            }
//        };
//        autoScrollHandler.postDelayed(timerRunnable, 0);
//
////        Timer timer = new Timer();
////        timer.scheduleAtFixedRate(new RemindTask(), 0, 500); // delay*/
//
//
////        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(nestedScrollView, "scrollY",
////                clRootLayout.getChildAt(0).getHeight() - nestedScrollView.getHeight());
////        objectAnimator.setDuration(4000);
////        objectAnimator.setInterpolator(new LinearInterpolator());
////        objectAnimator.start();
//    }
//
//    private void stopAutoScrollPage() {
//        autoScrollHandler.removeCallbacksAndMessages(null);
//    }
//
//    private int textViewOneLineHeight() {
//        int textViewHeight = tvLyrics.getLineHeight();
//
//        return textViewHeight;
//    }
//
//    class RemindTask extends TimerTask {
//        //        int current = nestedScrollView.getCurrentItem();
//        int position = 0;
//
//        @Override
//        public void run() {
//            runOnUiThread(new Runnable() {
//                public void run() {
//                    if (position == chordsInText.size()) {
//                        position = 0;
//                        position++;
//                    } else {
//                        position++;
//                    }
//                    nestedScrollView.smoothScrollBy(0, position);
//                }
//            });
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
//    public boolean onTouch(View v, MotionEvent event) {
//
//        // record where the user touched so we know where to place the window, so it will be out of the way
//
//        lastXCoordinate = event.getRawX();
//        lastYCoordinate = event.getRawY();
//
//        return false;
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
//
////        SubMenu subMenuShare = menuItemShare.getSubMenu();
////        MenuItem submenuItemShareText = subMenuShare.findItem(R.id.submenu_share_text);
////        MenuItem submenuItemSharePDF = subMenuShare.findItem(R.id.submenu_share_pdf);
//
////        int color = ContextCompat.getColor(this, R.color.gray);
////        updateMenuWithIcon(menuItemFullscreen, color);
////        updateMenuWithIcon(menuItemTextSize, color);
////        updateMenuWithIcon(menuItemAutoScroll, color);
////        updateMenuWithIcon(menuItemTranspose, color);
////        updateMenuWithIcon(menuItemShare, color);
//////        updateMenuWithIcon(submenuItemShareText, color);
//////        updateMenuWithIcon(submenuItemSharePDF, color);
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
//                showBottomSheetDialogTextSize();
//                return true;
//
//            case R.id.menu_auto_scroll:
//                starAutoScrollPage();
//                return true;
//
//            case R.id.menu_transpose:
//                showBottomSheetDialogTranspose();
////                createTransposeDialog();
//                return true;
//
//            case R.id.menu_share:
//
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
//
//        if (wakeLock.isHeld()) {
//            log.d("Releasing wakelock");
//            wakeLock.release();
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        // just in case the text size has changed
//        tvLyrics.setTextSize(TypedValue.COMPLEX_UNIT_SP, PreferenceHelper.getTextSizePreference(this));
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