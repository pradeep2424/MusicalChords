package com.music.chords.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.music.chords.R;
import com.music.chords.interfaces.Constants;
import com.music.chords.sharedPreference.AppSharedPreference;
import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Listener.DefaultValueChangedListener;
import com.travijuu.numberpicker.library.NumberPicker;

import belka.us.androidtoggleswitch.widgets.BaseToggleSwitch;
import belka.us.androidtoggleswitch.widgets.ToggleSwitch;
import moe.xing.daynightmode.BaseDayNightModeActivity;
import moe.xing.daynightmode.DayNightMode;

public class DarkModeActivity extends AppCompatActivity {
    RelativeLayout rlRootLayout;
    LinearLayout llDone;
    ToggleSwitch toggleSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dark_mode);

        init();
        events();
        setFontSizeToNumberPicker();
    }

    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle(R.string.settings_font_size);

        rlRootLayout = findViewById(R.id.rl_rootLayout);
        llDone = findViewById(R.id.ll_done);
        toggleSwitch = findViewById(R.id.switch_dayNight);
    }

    private void events() {
        llDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AppSharedPreference.SSP().putInt(Constants.KEY_FONT_SIZE, fontSize);

                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        toggleSwitch.setOnToggleSwitchChangeListener(new BaseToggleSwitch.OnToggleSwitchChangeListener() {
            @Override
            public void onToggleSwitchChangeListener(int position, boolean isChecked) {
                checkTabSelection(position);
            }
        });
    }

    private void checkTabSelection(int position) {
//        light mode
        if (position == 0) {
//            setNightMode(DayNightMode.MODE_NIGHT_NO);
            lightModeSelected();
        } else {
//            setNightMode(DayNightMode.MODE_NIGHT_YES);
            darkModeSelected();
        }
    }

    private void lightModeSelected() {
        toggleSwitch.setActiveBgColor(R.color.white);
        toggleSwitch.setActiveTextColor(R.color.main_dark_text);

        toggleSwitch.setInactiveBgColor(R.color.color_divider);
        toggleSwitch.setInactiveTextColor(R.color.gray);

        rlRootLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
    }

    private void darkModeSelected() {
        toggleSwitch.setActiveBgColor(R.color.main_text);
        toggleSwitch.setActiveTextColor(R.color.white);

        toggleSwitch.setInactiveBgColor(R.color.color_divider);
        toggleSwitch.setInactiveTextColor(R.color.setting_subtitles);

        rlRootLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.main_text));
    }

    private void setFontSizeToNumberPicker() {
        int fontSize = AppSharedPreference.SSP().getFontSize();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}