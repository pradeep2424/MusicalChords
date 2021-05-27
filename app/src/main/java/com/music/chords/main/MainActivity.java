package com.music.chords.main;

import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;
import com.music.chords.R;
import com.music.chords.bottomMenu.FavoritesFragment;
import com.music.chords.bottomMenu.ChordsFragment;
import com.music.chords.bottomMenu.LyricsFragment;
import com.music.chords.bottomMenu.SettingsFragment;
import com.music.chords.bottomMenu.SearchFragment;
import com.music.chords.database.DBSongDetails;
import com.music.chords.interfaces.TriggerDBChangeListener;
import com.music.chords.interfaces.TriggerTabChangeListener;
import com.music.chords.objects.SongObject;
import com.music.chords.utils.Application;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;


public class MainActivity extends AppCompatActivity implements TriggerDBChangeListener, TriggerTabChangeListener {
    CoordinatorLayout clRootLayout;
    FrameLayout frameLayout;
    BottomBar bottomBar;

    DBSongDetails dbSongDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setTheme(R.style.BaseTheme_Blue);
        setContentView(R.layout.activity_main);

        init();
        componentEvents();
    }

    private void init() {
        dbSongDetails = new DBSongDetails(this);

        clRootLayout = (CoordinatorLayout) findViewById(R.id.cl_rootLayout);
        frameLayout = (FrameLayout) findViewById(R.id.framelayout);
        bottomBar = (BottomBar) findViewById(R.id.bottombar);

        for (int i = 0; i < bottomBar.getTabCount(); i++) {
            bottomBar.getTabAtPosition(i).setGravity(Gravity.CENTER_VERTICAL);
        }
    }

    private void componentEvents() {
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {

            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab_lyrics:
                        replaceFragment(new LyricsFragment());
                        break;

                    case R.id.tab_chords:
                        replaceFragment(new ChordsFragment());
                        break;

                    case R.id.tab_search:
                        replaceFragment(new SearchFragment());
                        break;

                    case R.id.tab_cart:
                        replaceFragment(new FavoritesFragment());
                        break;

                    case R.id.tab_settings:
                        replaceFragment(new SettingsFragment());
                        break;
                }
            }
        });
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout, fragment);
        transaction.commit();
    }

    private void getSongDataFromDB() {
        try {
            Application.allSongsData.clear();
            Application.allLyricsData.clear();
            Application.allChordsData.clear();

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
                int iconColor = rss.getInt(7);
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
                songObject.setSongIconColor(iconColor);
                songObject.setSongLanguage(songLanguage);
                songObject.setContainsChords(isContainsChords);

                Application.allSongsData.add(songObject);
                if (isContainsChords) {
                    Application.allChordsData.add(songObject);
                } else {
                    Application.allLyricsData.add(songObject);
                }

                rss.moveToNext();
            }
            rss.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    private void getCartItems() {
//        if (InternetConnection.checkConnection(this)) {
//
//            int userID = Application.userDetails.getUserID();
//            int restaurantID = 0;
////            int restaurantID = Application.categoryObject.getRestaurantID();
//
//            ApiInterface apiService = RetroClient.getApiService(this);
//            Call<ResponseBody> call = apiService.getCartItem(userID, restaurantID);
//            call.enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//                    try {
//                        int statusCode = response.code();
//
//                        if (response.isSuccessful()) {
//                            String responseString = response.body().string();
//                            JSONArray jsonArray = new JSONArray(responseString);
//
//                            int noOfCartItems = jsonArray.length();
//                            setCartItemsBadgeCount(noOfCartItems);
//
//                        } else {
//                            showSnackbarErrorMsg(getResources().getString(R.string.something_went_wrong));
//                        }
//
////                        getTESTUserLikeDishData();
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    try {
//                        showSnackbarErrorMsg(getResources().getString(R.string.server_conn_lost));
//                        Log.e("Error onFailure : ", t.toString());
//                        t.printStackTrace();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//        } else {
////            signOutFirebaseAccounts();
//
//            Snackbar.make(clRootLayout, getResources().getString(R.string.no_internet),
//                    Snackbar.LENGTH_INDEFINITE)
//                    .setAction("RETRY", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            getCartItems();
//                        }
//                    })
////                    .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
//                    .show();
//        }
//    }

    public void showSnackbarErrorMsg(String erroMsg) {
//        Snackbar.make(fragmentRootView, erroMsg, Snackbar.LENGTH_LONG).show();

        Snackbar snackbar = Snackbar.make(clRootLayout, erroMsg, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        TextView snackTextView = (TextView) snackbarView
                .findViewById(R.id.snackbar_text);
        snackTextView.setMaxLines(4);
        snackbar.show();
    }

    private void setCartItemsBadgeCount(int count) {
        BottomBarTab cartItems = bottomBar.getTabWithId(R.id.tab_cart);
        cartItems.setBadgeCount(count);
    }

    @Override
    public void onDBDataChanged() {
        getSongDataFromDB();
    }

    @Override
    public void setTab(int position) {
        if (bottomBar != null) {
            bottomBar.selectTabAtPosition(position, true);
        }
    }

    @Override
    public void setBadgeCount(int count) {

    }

    @Override
    public void onBackPressed() {
        int selectedTab = bottomBar.getCurrentTabPosition();
        if (selectedTab != 0) {
            bottomBar.selectTabAtPosition(0);

        } else {
            super.onBackPressed();
        }
    }
}
