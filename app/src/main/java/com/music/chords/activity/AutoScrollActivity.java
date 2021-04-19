package com.music.chords.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.music.chords.R;
import com.music.chords.interfaces.Constants;
import com.music.chords.sharedPreference.AppSharedPreference;
import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Listener.DefaultValueChangedListener;
import com.travijuu.numberpicker.library.NumberPicker;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

public class AutoScrollActivity extends AppCompatActivity {
    LinearLayout llDone;
    IndicatorSeekBar seekBar;
    TextView tvScrollSpeed;

    float scrollSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_scroll);

        init();
        events();
    }

    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle(R.string.settings_auto_scroll);

        llDone = findViewById(R.id.ll_done);
        tvScrollSpeed = findViewById(R.id.tv_speedLinePerSecond);
        seekBar = findViewById(R.id.seekBar_speed);
        seekBar.setIndicatorTextFormat("${TICK_TEXT} --");

//        tvSampleText = findViewById(R.id.tv_sampleText);
    }

    private void events() {
        seekBar.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                scrollSpeed = seekParams.progressFloat;
                setScrollSpeed();
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
            }
        });

        llDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppSharedPreference.SSP().putFloat(Constants.KEY_AUTO_SCROLL_SPEED, scrollSpeed);

                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void setScrollSpeed() {
        tvScrollSpeed.setText(scrollSpeed + getString(R.string.line_per_second));
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