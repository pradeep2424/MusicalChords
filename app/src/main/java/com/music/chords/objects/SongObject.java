package com.music.chords.objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Wolf Soft on 8/3/2017.
 */

public class SongObject implements Serializable {
//public class SongObject implements Parcelable {
    int songId;
    String songTitle;
    String songSubtitle;
    String songLyrics;
    String songArtist;
    String songYouTubeURL;
    int songIconColor;
    Boolean isBookmark;

//    public SongObject(int songId, String songTitle, String songSubtitle, String songLyrics,
//                      String songArtist, String songYouTubeURL, Boolean isBookmark) {
//        this.songId = songId;
//        this.songTitle = songTitle;
//        this.songSubtitle = songSubtitle;
//        this.songLyrics = songLyrics;
//        this.songArtist = songArtist;
//        this.songYouTubeURL = songYouTubeURL;
//        this.isBookmark = isBookmark;
//    }
//
//    public SongObject(Parcel in){
//        this.songId = in.readInt();
//        this.songTitle = in.readString();
//        this.songSubtitle =  in.readString();
//        this.songLyrics =  in.readString();
//        this.songArtist =  in.readString();
//        this.songYouTubeURL =  in.readString();
//        this.isBookmark =  in.readBoolean();
//    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getSongSubtitle() {
        return songSubtitle;
    }

    public void setSongSubtitle(String songSubtitle) {
        this.songSubtitle = songSubtitle;
    }

    public String getSongLyrics() {
        return songLyrics;
    }

    public void setSongLyrics(String songLyrics) {
        this.songLyrics = songLyrics;
    }

    public String getSongArtist() {
        return songArtist;
    }

    public void setSongArtist(String songArtist) {
        this.songArtist = songArtist;
    }

    public String getSongYouTubeURL() {
        return songYouTubeURL;
    }

    public void setSongYouTubeURL(String songYouTubeURL) {
        this.songYouTubeURL = songYouTubeURL;
    }

    public int getSongIconColor() {
        return songIconColor;
    }

    public void setSongIconColor(int songIconColor) {
        this.songIconColor = songIconColor;
    }

    public Boolean getIsBookmark() {
        return isBookmark;
    }

    public void setIsBookmark(Boolean bookmark) {
        isBookmark = bookmark;
    }

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeInt(this.songId);
//        parcel.writeString(this.songTitle);
//        parcel.writeString(this.songSubtitle);
//        parcel.writeString(this.songLyrics);
//        parcel.writeString(this.songArtist);
//        parcel.writeString(this.songYouTubeURL);
//        parcel.writeBoolean(this.isBookmark);
//    }
}
