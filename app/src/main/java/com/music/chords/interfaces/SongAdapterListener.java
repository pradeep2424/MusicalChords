package com.music.chords.interfaces;

public interface SongAdapterListener {
    void onIconClicked(int position);

    void onIconImportantClicked(int position);

    void onMessageRowClicked(int position);

    void onRowLongClicked(int position);
}
