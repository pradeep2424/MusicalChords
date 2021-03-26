package com.music.chords.main;

import android.os.Bundle;
import android.util.Log;
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
import com.music.chords.bottomMenu.BookmarksFragment;
import com.music.chords.bottomMenu.HomeFragment;
import com.music.chords.bottomMenu.SettingsFragment;
import com.music.chords.bottomMenu.SearchFragment;
import com.music.chords.interfaces.TriggerTabChangeListener;
import com.music.chords.service.retrofit.ApiInterface;
import com.music.chords.service.retrofit.RetroClient;
import com.music.chords.utils.InternetConnection;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;

import org.json.JSONArray;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements TriggerTabChangeListener {
    CoordinatorLayout clRootLayout;
    FrameLayout frameLayout;
    BottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setTheme(R.style.BaseTheme_Blue);
        setContentView(R.layout.activity_main);

        init();
        componentEvents();
    }

    private void init() {
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
                    case R.id.tab_home:
                        replaceFragment(new HomeFragment());
                        break;

                    case R.id.tab_search:
                        replaceFragment(new SearchFragment());
                        break;

                    case R.id.tab_cart:
                        replaceFragment(new BookmarksFragment());
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
    public void setTab(int position) {
        if (bottomBar != null) {
            bottomBar.selectTabAtPosition(position, true);
        }
    }

    @Override
    public void setBadgeCount(int count) {
//        getCartItems();
        setCartItemsBadgeCount(count);
    }
}