package com.music.chords.bottomMenu;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.music.chords.R;
import com.music.chords.activity.ItemDetailsChordsActivity;
import com.music.chords.activity.ItemDetailsLyricsActivity;
import com.music.chords.adapter.SongItemAdapter;
import com.music.chords.database.DBSongDetails;
import com.music.chords.interfaces.Constants;
import com.music.chords.interfaces.SongAdapterListener;
import com.music.chords.interfaces.TriggerDBChangeListener;
import com.music.chords.objects.SongObject;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment implements SongAdapterListener, Constants {
    private View rootView;
    private View viewToolbar;
    private SearchView searchView;

    private RecyclerView recyclerView;
    private SongItemAdapter adapterSearch;

    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;

    private DBSongDetails dbSongDetails;

    private TriggerDBChangeListener triggerFavoritesChangeListener;
    private ArrayList<SongObject> listSearchedSongs = new ArrayList<>();
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
        rootView = inflater.inflate(R.layout.fragment_search, container, false);

        init();
        events();
        setupSearchProduct();
        return rootView;
    }

    private void init() {
        viewToolbar = rootView.findViewById(R.id.view_toolbar);
        searchView = viewToolbar.findViewById(R.id.searchView);
        recyclerView = rootView.findViewById(R.id.rv_productItems);
    }

    private void events() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (TextUtils.isEmpty(query)) {
                    listSearchedSongs.clear();
                    adapterSearch.notifyDataSetChanged();

                } else if (!TextUtils.isEmpty(query) && query.length() > 2) {
                    getSearchItems(query);
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    listSearchedSongs.clear();
                    adapterSearch.notifyDataSetChanged();

                } else if (!TextUtils.isEmpty(newText) && newText.length() > 2) {
                    getSearchItems(newText);
                }
                return true;
            }
        });
    }

    private void setupSearchProduct() {
        adapterSearch = new SongItemAdapter(getActivity(), listSearchedSongs);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapterSearch);
        adapterSearch.setSongAdapterListener(this);

        actionModeCallback = new ActionModeCallback();

//        adapterSearch = new SearchAutoCompleteAdapter(getActivity(), listSearchedSongs);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.setAdapter(adapterSearch);
//
//        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
//
//        adapterSearch.setClickListener(this);
//        adapterSearch.notifyDataSetChanged();
    }

    private void getSearchItems(final String searchText) {
        try {
            listSearchedSongs.clear();

            Cursor rss = dbSongDetails.getSearchedSongData(searchText);
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
                    listSearchedSongs.add(songObject);

                    rss.moveToNext();
                }
                rss.close();

            } else {
//                empty recyclerview no search results
            }

            adapterSearch.notifyDataSetChanged();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getRandomMaterialColor( ) {
        String typeColor = "400";
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


//    private void getSearchItems(final String searchText) {
//        if (InternetConnection.checkConnection(getActivity())) {
////            showDialog();
//
//            ApiInterface apiService = RetroClient.getApiService(getActivity());
//            Call<ResponseBody> call = apiService.getSearchItem(searchText);
//            call.enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//                    try {
//                        int statusCode = response.code();
//
//                        if (response.isSuccessful()) {
//                            listSearchedSongs.clear();
//
//                            String responseString = response.body().string();
//
//                            JSONArray jsonArray = new JSONArray(responseString);
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jsonObj = jsonArray.getJSONObject(i);
//
//                                int categoryID = jsonObj.optInt("CategoryID");
//                                String categoryName = jsonObj.optString("CategoryName");
//                                String foodType = jsonObj.optString("FoodType");
//                                int foodTypeID = jsonObj.optInt("FoodTypeId");
//                                String group = jsonObj.optString("Group");
//                                int groupID = jsonObj.optInt("GroupId");
//                                double price = jsonObj.optDouble("Price");
//                                String productDesc = jsonObj.optString("ProductDesc");
//                                int productID = jsonObj.optInt("ProductId");
//                                String productImage = jsonObj.optString("PhotoPath");
//                                String productName = jsonObj.optString("ProductName");
//                                String unit = jsonObj.optString("Unit");
//                                int unitID = jsonObj.optInt("UnitId");
//
////                                double cgst = jsonObj.optDouble("CGST");
////                                int haveRuntimeRate = jsonObj.optInt("HaveRuntimeRate");
////                                String isDiscounted = jsonObj.optString("IsDiscounted");
////                                double sgst = jsonObj.optDouble("SGST");
////                                int taxID = jsonObj.optInt("TaxID");
////                                String taxName = jsonObj.optString("TaxName");
//
//                                ArrayList<String> listProdImages = new ArrayList<>();
//                                listProdImages.add(productImage);
//
//                                SongObject productObject = new SongObject();
////                                productObject.setProductID(productID);
////                                productObject.setProductName(productName);
////                                productObject.setProductDescription(productDesc);
////                                productObject.setListProductImage(listProdImages);
////                                productObject.setPrice(price);
////                                productObject.setCategoryID(categoryID);
////                                productObject.setCategoryName(categoryName);
////                                productObject.setFoodTypeID(foodTypeID);
////                                productObject.setFoodType(foodType);
////                                productObject.setGroupID(groupID);
////                                productObject.setGroup(group);
////                                productObject.setUnitID(unitID);
////                                productObject.setUnit(unit);
//
//                                listSearchedSongs.add(productObject);
//                            }
//
//                            adapterSearch.notifyDataSetChanged();
//
//                        } else {
//                            showSnackbarErrorMsg(getResources().getString(R.string.something_went_wrong));
//                        }
//
////                        setupRecyclerViewRestaurant();
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
////                    dismissDialog();
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    try {
////                        dismissDialog();
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
//            Snackbar.make(rootView, getResources().getString(R.string.no_internet),
//                    Snackbar.LENGTH_INDEFINITE)
//                    .setAction("RETRY", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            getSearchItems(searchText);
//                        }
//                    })
////                    .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
//                    .show();
//        }
//    }
//
//    public void showSnackbarErrorMsg(String erroMsg) {
////        Snackbar.make(fragmentRootView, erroMsg, Snackbar.LENGTH_LONG).show();
//
//        Snackbar snackbar = Snackbar.make(rootView, erroMsg, Snackbar.LENGTH_LONG);
//        View snackbarView = snackbar.getView();
//        TextView snackTextView = (TextView) snackbarView
//                .findViewById(R.id.snackbar_text);
//        snackTextView.setMaxLines(4);
//        snackbar.show();
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
        adapterSearch.toggleSelection(position);
        int count = adapterSearch.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    private void addSelectedItems(int position) {
        SongObject songObject = listSearchedSongs.get(position);
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
        adapterSearch.resetAnimationIndex();

        List<Integer> selectedItemPositions = adapterSearch.getSelectedItems();
        for (int i = 0; i < selectedItemPositions.size(); i++) {
            int position = selectedItemPositions.get(i);
            listSearchedSongs.get(position).setIsFavorites(isSaved);
//            adapter.removeData(selectedItemPositions.get(i));

            updateFavoritesInDB(listSearchedSongs.get(position));
        }

        triggerFavoritesChangedListener();
        adapterSearch.notifyDataSetChanged();
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

    private void triggerFavoritesChangedListener() {
        triggerFavoritesChangeListener.onDBDataChanged();
    }


    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_action_mode, menu);
            MenuItem menuItem = menu.findItem(R.id.action_saved_unsaved);

//            boolean isAtLeastOneUnsaved = adapterSearch.checkAtLeastOneUnsavedItem();
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
            adapterSearch.clearSelections();
            actionMode = null;

            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    adapterSearch.resetAnimationIndex();
                    // mAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    // deleting the messages from recycler view
    private void deleteMessages() {
        adapterSearch.resetAnimationIndex();
        List<Integer> selectedItemPositions = adapterSearch.getSelectedItems();

        for (int i = selectedItemPositions.size() - 1; i >= 0; i--) {
            adapterSearch.removeData(selectedItemPositions.get(i));
        }
        adapterSearch.notifyDataSetChanged();
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
        SongObject songObject = listSearchedSongs.get(position);
        boolean isFavorites = !songObject.getIsFavorites();

        songObject.setIsFavorites(isFavorites);
        listSearchedSongs.set(position, songObject);
        adapterSearch.notifyDataSetChanged();

        updateFavoritesInDB(songObject);
        triggerFavoritesChangedListener();

//        SongObject songObject = listSearchedSongs.get(position);
//        songObject.setIsFavorites(!songObject.getIsFavorites());
//
//        listSearchedSongs.set(position, songObject);
//        adapterSearch.notifyDataSetChanged();
    }

    @Override
    public void onMessageRowClicked(int position) {
        // verify whether action mode is enabled or not
        // if enabled, change the row state to activated
        if (adapterSearch.getSelectedItemCount() > 0) {
            enableActionMode(position);

        } else {
            SongObject songObject = listSearchedSongs.get(position);
            boolean isContainsChords = songObject.isContainsChords();

            Intent intent;
            if (isContainsChords) {
                intent = new Intent(getActivity(), ItemDetailsChordsActivity.class);
            } else {
                intent = new Intent(getActivity(), ItemDetailsLyricsActivity.class);
            }

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
