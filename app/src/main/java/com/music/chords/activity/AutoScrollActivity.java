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

public class AutoScrollActivity extends AppCompatActivity {
    LinearLayout llDone;
    TextView tvSampleText;

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

//        llDone = findViewById(R.id.ll_done);
//        tvSampleText = findViewById(R.id.tv_sampleText);
    }

    private void events() {

//        llDone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int fontSize = numberPicker.getValue();
//                AppSharedPreference.SSP().putInt(Constants.KEY_FONT_SIZE, fontSize);
//
//                Intent intent = new Intent();
//                setResult(RESULT_OK, intent);
//                finish();
//            }
//        });

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