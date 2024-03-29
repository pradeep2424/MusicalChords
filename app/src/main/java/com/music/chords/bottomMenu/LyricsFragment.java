package com.music.chords.bottomMenu;

import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
import com.music.chords.R;
import com.music.chords.activity.ItemDetailsChordsActivity;
import com.music.chords.adapter.SongItemAdapter;
import com.music.chords.database.DBSongDetails;
import com.music.chords.interfaces.Constants;
import com.music.chords.interfaces.SongAdapterListener;
import com.music.chords.objects.SongObject;
import com.music.chords.service.retrofit.ApiInterface;
import com.music.chords.service.retrofit.RetroClient;
import com.music.chords.utils.Application;
import com.music.chords.utils.InternetConnection;
import com.music.chords.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LyricsFragment extends Fragment implements SongAdapterListener, SwipeRefreshLayout.OnRefreshListener,
        Constants {
    private View rootView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private SongItemAdapter adapter;

    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;

    DBSongDetails dbSongDetails;

    ArrayList<SongObject> listSongsData = new ArrayList<>();
    ArrayList<SongObject> listLyricsData = new ArrayList<>();
    ArrayList<SongObject> listChordsData = new ArrayList<>();
    ArrayList<SongObject> listSelectedSongs = new ArrayList<>();

    final public int REQUEST_CODE_AARTI_DETAILS_ACTIVITY = 100;
//    private List<FavoriteContacts> aartiList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbSongDetails = new DBSongDetails(getActivity());
        listLyricsData = Application.allLyricsData;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_lyrics, container, false);

        init();
        events();
        setupRecyclerView();

        return rootView;
    }

    private void init() {
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).setTitle(R.string.lyrics);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setEnabled(false);
    }

    private void events() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSongListData();
            }
        });
    }

    private void setupRecyclerView() {
//        getAllSongsData();

        adapter = new SongItemAdapter(getActivity(), listLyricsData);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
//        recyclerView.addItemDecoration(Utils.getDividerItemDecoration(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.setSongAdapterListener(this);

        actionModeCallback = new ActionModeCallback();

        // show loader and fetch messages
        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
//                        getInbox();
                    }
                }
        );

//        prepareFavouriteContactsData();
    }

    private void getSongListData() {
        if (InternetConnection.checkConnection(getActivity())) {

            ApiInterface apiService = RetroClient.getApiService(getActivity());
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
                                            songIconColor = ContextCompat.getColor(getActivity(), R.color.chords_icon);
                                        } else {
                                            songIconColor = ContextCompat.getColor(getActivity(), R.color.lyrics_icon);
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

                                Application.allSongsData = listSongsData;
                                Application.allLyricsData = listLyricsData;
                                Application.allChordsData = listChordsData;

                                swipeRefreshLayout.setRefreshing(false);
                                adapter.notifyDataSetChanged();
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

            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet),
                    Toast.LENGTH_LONG).show();

            Snackbar.make(rootView, getResources().getString(R.string.no_internet),
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getSongListData();
                        }
                    })
                    .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
                    .show();
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

//    private int getRandomMaterialColor(String typeColor) {
//        int returnColor = Color.GRAY;
//        int arrayId = getResources().getIdentifier("mdcolor_" + typeColor, "array", getActivity().getPackageName());
//
//        if (arrayId != 0) {
//            TypedArray colors = getResources().obtainTypedArray(arrayId);
//            int index = (int) (Math.random() * colors.length());
//            returnColor = colors.getColor(index, Color.GRAY);
//            colors.recycle();
//        }
//        return returnColor;
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            Toast.makeText(getActivity(), "Search...", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    public ArrayList<AartiObject> getAartiData() {
//
//        try {
//            String responseString = loadJSONFromAsset();
//
//            JSONObject jsonObj = new JSONObject(responseString);
//            String respStatus = jsonObj.getString("Status");
//            String aartiLanguage = jsonObj.getString("AartiLanguage");
//
//            if (respStatus.equalsIgnoreCase("Success")) {
//
//                JSONArray jsonArrayRoot = jsonObj.getJSONArray("AartiData");
//                if (jsonArrayRoot != null && jsonArrayRoot.length() > 0) {
//
//                    for (int index = 0; index < jsonArrayRoot.length(); index++) {
//                        ArrayList<String> listWallpaper = new ArrayList<>();
//                        JSONObject json = jsonArrayRoot.getJSONObject(index);
//
//                        String id = json.getString("ID");
//                        String title = json.getString("Title");
//                        String subtitle = json.getString("Subtitle");
//                        String lyrics = json.getString("Lyrics");
//                        String coverPictureURL = json.getString("CoverPictureURL");
//                        String audio = json.getString("Audio");
//
//                        JSONArray jsonArrayWallpaper = json.getJSONArray("Wallpapers");
//                        if (jsonArrayWallpaper != null && jsonArrayWallpaper.length() > 0) {
//
//                            for (int j = 0; j < jsonArrayWallpaper.length(); j++) {
//                                JSONObject jsonObject = jsonArrayWallpaper.getJSONObject(j);
//                                String imageURL = jsonObject.getString("ImageURL");
//
//                                listWallpaper.add(imageURL);
//                            }
//                        }
//
////                        default Favourites and Recent Activity is false
//                        AartiObject aartiObject = new AartiObject(id, title, subtitle, lyrics,
//                                coverPictureURL, audio, listWallpaper, false, false);
//
//
//
//
//
////                        aartiObject.setAartiId(id);
////                        aartiObject.setAartiTitle(title);
////                        aartiObject.setAartiSubtitle(subtitle);
////                        aartiObject.setAartiLyrics(lyrics);
////                        aartiObject.setAartiCoverPic(coverPictureURL);
////                        aartiObject.setAartiAudio(audio);
////                        aartiObject.setAartiWallpaper(listWallpaper);
//
//                        allAartiData.add(aartiObject);
//                    }
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return allAartiData;
//    }
//
//    public String loadJSONFromAsset() {
//        String json = null;
//        InputStream is;
//        try {
//
//            if (prefManagerConfig.getAartiLanguage().equalsIgnoreCase(AartiApplication.aartiLangHI)) {
//                is = getActivity().getAssets().open("aarti_hindi.json");
//
//            } else {
//                is = getActivity().getAssets().open("aarti_marathi.json");
//            }
//
////            is = getActivity().getAssets().open("aarti_marathi.json");
//            int size = is.available();
//            byte[] buffer = new byte[size];
//            is.read(buffer);
//            is.close();
//            json = new String(buffer, "UTF-8");
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return null;
//        }
//        return json;
//    }

    private void enableActionMode(int position) {
        if (actionMode == null) {
            actionMode = getActivity().startActionMode(actionModeCallback);
//            actionMode = startSupportActionMode(actionModeCallback);
        }

        addSelectedItems(position);
        toggleSelection(position);
    }

    private void toggleSelection(int position) {
        adapter.toggleSelection(position);
        int count = adapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    private void addSelectedItems(int position) {
        SongObject songObject = listLyricsData.get(position);
        if (listSelectedSongs.contains(songObject)) {
            listSelectedSongs.remove(songObject);

        } else {
            listSelectedSongs.add(songObject);
        }
    }

    private boolean checkAllSelectedItemsSaved() {
        int count = 0;
        for (SongObject songObject : listSelectedSongs) {
            if (songObject.getIsFavorites()) {
                count++;
            }
        }

        if (count == listSelectedSongs.size()) {
            return true;
        } else {
            return false;
        }
    }

    private void markAllSavedUnsaved(boolean isSaved) {
        adapter.resetAnimationIndex();

        List<Integer> selectedItemPositions = adapter.getSelectedItems();
        for (int i = 0; i < selectedItemPositions.size(); i++) {
            int position = selectedItemPositions.get(i);
            listLyricsData.get(position).setIsFavorites(isSaved);
//            adapter.removeData(selectedItemPositions.get(i));

            updateFavoritesInDB(listLyricsData.get(position));
        }
        adapter.notifyDataSetChanged();
    }

    private void updateFavoritesInDB(SongObject songObject) {
        int songID = songObject.getSongId();
        boolean isFavorites = songObject.getIsFavorites();

        if (isFavorites) {
            dbSongDetails.setSongToFavorites(songID);
        } else {
            dbSongDetails.removeSongFromFavorites(songID);
        }
    }

    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_action_mode, menu);
            MenuItem menuItem = menu.findItem(R.id.action_saved_unsaved);

//            boolean isAtLeastOneUnsaved = adapter.checkAtLeastOneUnsavedItem();
//            if (isAtLeastOneUnsaved) {
//                menuItem.setTitle(R.string.action_mark_saved);
//            } else {
//                menuItem.setTitle(R.string.action_mark_unsaved);
//            }

            // disable swipe refresh if action mode is enabled
            swipeRefreshLayout.setEnabled(false);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            MenuItem menuItem = menu.findItem(R.id.action_saved_unsaved);

            if (checkAllSelectedItemsSaved()) {
                menuItem.setTitle(R.string.action_mark_unsaved);
                menuItem.setIcon(R.drawable.ic_star_unselected);
            } else {
                menuItem.setTitle(R.string.action_mark_saved);
                menuItem.setIcon(R.drawable.ic_star_selected);
            }
            DrawableCompat.setTint(menuItem.getIcon(), ContextCompat.getColor(getActivity(), R.color.white));
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_saved_unsaved:

                    String menuText = item.getTitle().toString();
                    if (menuText.equalsIgnoreCase(getString(R.string.action_mark_saved))) {
                        markAllSavedUnsaved(true);                         // saved

                    } else {
                        markAllSavedUnsaved(false);                        // unsaved
                    }

//                    // delete all the selected messages
//                    deleteMessages();
                    mode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            adapter.clearSelections();
            swipeRefreshLayout.setEnabled(true);
            actionMode = null;

            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    adapter.resetAnimationIndex();
                    // mAdapter.notifyDataSetChanged();
                }
            });
        }
    }

//    private boolean checkAtLeastOneUnsavedItem() {
//        List<Integer> selectedItemPositions = adapter.getSelectedItems();
//        for (SongObject article : listLyricsData) {
//            if (article.getIsFavorites()) {
//                return true;
//            }
//        }
//        return false;
//    }


    // deleting the messages from recycler view
    private void deleteMessages() {
        adapter.resetAnimationIndex();
        List<Integer> selectedItemPositions = adapter.getSelectedItems();

        for (int i = selectedItemPositions.size() - 1; i >= 0; i--) {
            adapter.removeData(selectedItemPositions.get(i));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onIconClicked(int position) {
        enableActionMode(position);

//        if (actionMode == null) {
//            actionMode = getActivity().startActionMode(actionModeCallback);
////            actionMode = ((AppCompatActivity)getActivity()).startSupportActionMode(actionModeCallback);
//        }
//
//        toggleSelection(position);
    }

    @Override
    public void onIconImportantClicked(int position) {
        // Star icon is clicked,
        // mark the message as important
        SongObject songObject = listLyricsData.get(position);
        boolean isFavorites = !songObject.getIsFavorites();

        songObject.setIsFavorites(isFavorites);
        listLyricsData.set(position, songObject);
        adapter.notifyDataSetChanged();

        updateFavoritesInDB(songObject);
    }

    @Override
    public void onMessageRowClicked(int position) {
        // verify whether action mode is enabled or not
        // if enabled, change the row state to activated
        if (adapter.getSelectedItemCount() > 0) {
            enableActionMode(position);

        } else {
            SongObject songObject = listLyricsData.get(position);
//            Intent intent = new Intent(getActivity(), ItemDetailsLyricsActivity.class);
            Intent intent = new Intent(getActivity(), ItemDetailsChordsActivity.class);
            intent.putExtra(SONG_OBJECT, songObject);
            startActivity(intent);

//            // read the message which removes bold from the row
//            SongObject songObject = listLyricsData.get(position);
////            songObject.setRead(true);
//            listLyricsData.set(position, songObject);
//            adapter.notifyDataSetChanged();
//            Toast.makeText(getActivity(), "Read: " + songObject.getSongTitle(), Toast.LENGTH_SHORT).show();
        }
    }

//    @Override
//    public void onClick(View view, int position) {
//        Application.songObject = listLyricsData.get(position);
//
//        Intent intent = new Intent(getActivity(), SongDetailsActivity.class);
//        startActivity(intent);
//
//    }

    @Override
    public void onRowLongClicked(int position) {
        // long press is performed, enable action mode
        enableActionMode(position);
    }

    @Override
    public void onRefresh() {

    }

    public void showSnackbarErrorMsg(String erroMsg) {
        Snackbar.make(rootView, erroMsg, Snackbar.LENGTH_LONG).show();
    }

    public void showSnackbarErrorMsgWithButton(String erroMsg) {
        Snackbar.make(rootView, erroMsg, Snackbar.LENGTH_INDEFINITE)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                })
                .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
                .show();
    }
}