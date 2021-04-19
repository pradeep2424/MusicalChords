package com.music.chords.sharedPreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.music.chords.R;
import com.music.chords.chordsReader.chords.NoteNaming;
import com.music.chords.chordsReader.data.ColorScheme;
import com.music.chords.chordsReader.utils.UtilLogger;
import com.music.chords.interfaces.Constants;

import java.util.Set;

public class AppSharedPreference implements Constants {
    private static final AppSharedPreference sharedPreference = new AppSharedPreference();

    //	preference name is already given
//    private String LETS_TALK_PREF = "com.myletstalk.abcdef";

//    private String LETS_TALK_PREF = "com.myletstalk.abcdef";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public static final AppSharedPreference SSP() {
        return sharedPreference;
    }

    public void init(Context appContext) {
        if (sharedPreference != null) {
            sharedPreferences = appContext.getSharedPreferences(APP_SHARED_PREFERENCE, 0);
            editor = sharedPreferences.edit();
        }
    }

    public void putString(String key, String value) {
        editor.putString(key, value);
        commit();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }

    public void putInt(String key, int value) {
        editor.putInt(key, value);
        commit();
    }

    public int getInt(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    public void putFloat(String key, float value) {
        editor.putFloat(key, value);
        commit();
    }

    public float getFloat(String key) {
        return sharedPreferences.getFloat(key, 0);
    }

    public Set<String> getStringSet(String key) {
        return sharedPreferences.getStringSet(key, null);
    }

    public void putLong(String key, Long value) {
        editor.putLong(key, value);
        commit();
    }

    public Long getLong(String key, Long value) {
        return sharedPreferences.getLong(key, 0);
    }

    public void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        commit();
    }

    public boolean getBoolean(String key, boolean value) {
        return sharedPreferences.getBoolean(key, value);
    }

    public void commit() {
        editor.commit();
    }

    public int getFontSize() {
      int fontSize =  sharedPreferences.getInt(Constants.KEY_FONT_SIZE, VALUE_DEFAULT_FONT_SIZE);
      return fontSize;
    }

    public float getAutoScrollSpeed() {
        float fontSize =  sharedPreferences.getFloat(Constants.KEY_AUTO_SCROLL_SPEED, VALUE_DEFAULT_SCROLL_SPEED);
        return fontSize;
    }

}
