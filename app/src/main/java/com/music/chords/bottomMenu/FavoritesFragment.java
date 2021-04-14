package com.music.chords.bottomMenu;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.music.chords.R;
import com.music.chords.activity.ItemDetailsActivity;
import com.music.chords.adapter.SongItemAdapter;
import com.music.chords.database.DBSongDetails;
import com.music.chords.interfaces.Constants;
import com.music.chords.interfaces.SongAdapterListener;
import com.music.chords.interfaces.TriggerDBChangeListener;
import com.music.chords.objects.SongObject;

import java.util.ArrayList;
import java.util.List;


public class FavoritesFragment extends Fragment implements SongAdapterListener, Constants {
    private View rootView;
    //    private View viewToolbar;
    private RecyclerView recyclerView;
    private SongItemAdapter adapter;

    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;

    private DBSongDetails dbSongDetails;

    private TriggerDBChangeListener triggerFavoritesChangeListener;
    private ArrayList<SongObject> listFavoriteSongs = new ArrayList<>();
    private ArrayList<SongObject> listSelectedSongs = new ArrayList<>();

    private final int REQUEST_CODE_PRODUCT_DETAILS = 100;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        triggerFavoritesChangeListener = (TriggerDBChangeListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbSongDetails = new DBSongDetails(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_favorites, container, false);

        init();
//        events();
        setupRecyclerView();

        return rootView;
    }

    private void init() {
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

//        viewToolbar = rootView.findViewById(R.id.view_toolbar);
        recyclerView = rootView.findViewById(R.id.rv_productItems);
    }

    private void setupRecyclerView() {
        getFavoritesItems();

        adapter = new SongItemAdapter(getActivity(), listFavoriteSongs);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        adapter.setSongAdapterListener(this);

        actionModeCallback = new ActionModeCallback();
    }

    private void getFavoritesItems() {
        try {
//            for (int i = 0; i < Application.allSongsData.size(); i++) {
//                SongObject songObject = Application.allSongsData.get(i);
//                if (songObject.getIsFavorites()) {
//                    listFavoriteSongs.add(songObject);
//                }
//            }

            Cursor rss = dbSongDetails.getFavoritesSongData();
            int countDBRows = rss.getCount();
            if (countDBRows > 0) {
                rss.moveToFirst();

                for (int index = 0; index < countDBRows; index++) {
                    int songId = rss.getInt(0);
                    String songTitle = rss.getString(1);
                    String songSubtitle = rss.getString(2);
                    String songLyrics = rss.getString(3);
                    String songArtist = rss.getString(4);
                    String songYouTubeURL = rss.getString(5);
                    Boolean isFavorites = (rss.getInt(6) == 1);
                    int songIconColor = (rss.getInt(7));

                    SongObject songObject = new SongObject();
                    songObject.setSongId(songId);
                    songObject.setSongTitle(songTitle);
                    songObject.setSongSubtitle(songSubtitle);
                    songObject.setSongLyrics(songLyrics);
                    songObject.setSongArtist(songArtist);
                    songObject.setSongYouTubeURL(songYouTubeURL);
                    songObject.setIsFavorites(isFavorites);
                    songObject.setSongIconColor(songIconColor);
                    listFavoriteSongs.add(songObject);

                    rss.moveToNext();
                }
                rss.close();

            } else {
//                empty recyclerview no search results
            }

            adapter.notifyDataSetChanged();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
        SongObject songObject = listFavoriteSongs.get(position);
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

    private void removeFavoritesFromList() {
        adapter.resetAnimationIndex();

        for (int i = 0; i < listSelectedSongs.size(); i++) {
            SongObject selectedSong = listSelectedSongs.get(i);
            listFavoriteSongs.remove(selectedSong);

            updateFavoritesInDB(selectedSong);
        }

        triggerFavoritesChangedListener();
        adapter.notifyDataSetChanged();
    }

    private void updateFavoritesInDB(SongObject songObject) {
        int songID = songObject.getSongId();
        boolean isFavorites = !songObject.getIsFavorites();

        if (isFavorites) {
            dbSongDetails.setSongToFavorites(songID);
        } else {
            dbSongDetails.removeSongFromFavorites(songID);
        }
    }

    private void triggerFavoritesChangedListener() {
        triggerFavoritesChangeListener.onDBDataChanged();
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
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            MenuItem menuItem = menu.findItem(R.id.action_saved_unsaved);
            menuItem.setTitle(R.string.action_mark_unsaved);
            menuItem.setIcon(R.drawable.ic_star_unselected);
            DrawableCompat.setTint(menuItem.getIcon(), ContextCompat.getColor(getActivity(), R.color.white));

            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_saved_unsaved:
                    removeFavoritesFromList();                        // unsaved
                    mode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            adapter.clearSelections();
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
        SongObject songObject = listFavoriteSongs.get(position);
//        boolean isFavorites = !songObject.getIsFavorites();
//
//        songObject.setIsFavorites(isFavorites);
//        listFavoriteSongs.set(position, songObject);

        listFavoriteSongs.remove(position);
        adapter.notifyDataSetChanged();

        updateFavoritesInDB(songObject);
        triggerFavoritesChangedListener();
    }

    @Override
    public void onMessageRowClicked(int position) {
        // verify whether action mode is enabled or not
        // if enabled, change the row state to activated
        if (adapter.getSelectedItemCount() > 0) {
            enableActionMode(position);

        } else {
            SongObject songObject = listFavoriteSongs.get(position);
            Intent intent = new Intent(getActivity(), ItemDetailsActivity.class);
            intent.putExtra(SONG_OBJECT, songObject);
            startActivityForResult(intent, REQUEST_CODE_PRODUCT_DETAILS);
        }
    }

    @Override
    public void onRowLongClicked(int position) {
        // long press is performed, enable action mode
        enableActionMode(position);
    }


}
