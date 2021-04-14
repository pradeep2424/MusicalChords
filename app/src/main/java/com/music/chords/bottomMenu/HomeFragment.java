package com.music.chords.bottomMenu;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.util.Predicate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.music.chords.R;
import com.music.chords.activity.ItemDetailsActivity;
import com.music.chords.adapter.SongItemAdapter;
import com.music.chords.database.DBSongDetails;
import com.music.chords.interfaces.Constants;
import com.music.chords.interfaces.SongAdapterListener;
import com.music.chords.loader.OnRecyclerViewClickListener;
import com.music.chords.objects.SongObject;
import com.music.chords.service.retrofit.ApiInterface;
import com.music.chords.service.retrofit.RetroClient;
import com.music.chords.utils.Application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment implements SongAdapterListener, SwipeRefreshLayout.OnRefreshListener,
        Constants {
    private View rootView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private SongItemAdapter adapter;

    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;

    DBSongDetails dbSongDetails;

    ArrayList<SongObject> listAllSongsData = new ArrayList<>();
    ArrayList<SongObject> listSelectedSongs = new ArrayList<>();

    final public int REQUEST_CODE_AARTI_DETAILS_ACTIVITY = 100;
//    private List<FavoriteContacts> aartiList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbSongDetails = new DBSongDetails(getActivity());
        listAllSongsData = Application.allSongsData;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        init();
//        getDummyData();
        setupRecyclerView();

        return rootView;
    }

    private void init() {
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void setupRecyclerView() {
//        getAllSongsData();

        adapter = new SongItemAdapter(getActivity(), listAllSongsData);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
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

    /**
     * Fetches mail messages by making HTTP request
     * url: http://api.androidhive.info/json/inbox.json
     */
    private void getAllSongsData() {
        listAllSongsData.addAll(Application.allSongsData);

//        for (int i = 0; i < 10; i++) {
//            String songTitle = "Title " + i;
//            String songSubtitle = "Subtitle" + i;
//            String songArtist = "Artist" + i;
//
//            SongObject songObject = new SongObject();
//            songObject.setSongTitle(songTitle);
//            songObject.setSongSubtitle(songSubtitle);
//            songObject.setSongArtist(songArtist);
//            songObject.setSongIconColor(getRandomMaterialColor("400"));
//            songObject.setIsFavorites(false);
//
//            listAllSongsData.add(songObject);
//        }
    }

    private void getInbox() {
        swipeRefreshLayout.setRefreshing(true);

        ApiInterface apiService = RetroClient.getApiService(getActivity());
        Call<ResponseBody> call = apiService.getInbox();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // clear the inbox
                listAllSongsData.clear();


                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "Unable to fetch json: " + t.getMessage(), Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private int getRandomMaterialColor(String typeColor) {
        int returnColor = Color.GRAY;
        int arrayId = getResources().getIdentifier("mdcolor_" + typeColor, "array", getActivity().getPackageName());

        if (arrayId != 0) {
            TypedArray colors = getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.GRAY);
            colors.recycle();
        }
        return returnColor;
    }

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
        SongObject songObject = listAllSongsData.get(position);
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
            listAllSongsData.get(position).setIsFavorites(isSaved);
//            adapter.removeData(selectedItemPositions.get(i));

            updateFavoritesInDB(listAllSongsData.get(position));
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
//        for (SongObject article : listAllSongsData) {
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
        SongObject songObject = listAllSongsData.get(position);
        boolean isFavorites = !songObject.getIsFavorites();

        songObject.setIsFavorites(isFavorites);
        listAllSongsData.set(position, songObject);
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
            SongObject songObject = listAllSongsData.get(position);
            Intent intent = new Intent(getActivity(), ItemDetailsActivity.class);
            intent.putExtra(SONG_OBJECT, songObject);
            startActivity(intent);

//            // read the message which removes bold from the row
//            SongObject songObject = listAllSongsData.get(position);
////            songObject.setRead(true);
//            listAllSongsData.set(position, songObject);
//            adapter.notifyDataSetChanged();
//            Toast.makeText(getActivity(), "Read: " + songObject.getSongTitle(), Toast.LENGTH_SHORT).show();
        }
    }

//    @Override
//    public void onClick(View view, int position) {
//        Application.songObject = listAllSongsData.get(position);
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
}