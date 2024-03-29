package com.music.chords.bottomMenu;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.fonts.Font;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.music.chords.BuildConfig;
import com.music.chords.R;
import com.music.chords.activity.AboutUsActivity;
import com.music.chords.activity.AutoScrollActivity;
import com.music.chords.activity.DarkModeActivity;
import com.music.chords.activity.FontSizeActivity;
import com.music.chords.activity.ItemDetailsChordsActivity;
import com.music.chords.interfaces.Constants;
import com.music.chords.sharedPreference.AppSharedPreference;

import es.dmoral.toasty.Toasty;

public class SettingsFragment extends Fragment {
    private View rootView;

    private LinearLayout llDarkTheme;
    private LinearLayout llFontSize;
    private LinearLayout llAutoScroll;
    private LinearLayout llShare;
    private LinearLayout llRateUs;
    private LinearLayout llAboutUs;

    private TextView tvDarkThemeSubtext;

    private SwitchCompat switchDarkTheme;
//    private ImageView ivThemeIcon;

    private final int REQUEST_CODE_THEME = 100;
    private final int REQUEST_CODE_FONT_SIZE = 200;
    private final int REQUEST_CODE_AUTO_SCROLL = 200;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        init();
        componentEvents();
        setNightModeSwitch();

        return rootView;
    }

    private void init() {
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).setTitle(R.string.settings);

        llDarkTheme = rootView.findViewById(R.id.ll_darkTheme);
        llFontSize = rootView.findViewById(R.id.ll_fontSize);
        llAutoScroll = rootView.findViewById(R.id.ll_autoScroll);
        llShare = rootView.findViewById(R.id.ll_share);
        llRateUs = rootView.findViewById(R.id.ll_rateUs);
        llAboutUs = rootView.findViewById(R.id.ll_aboutUs);
        switchDarkTheme = rootView.findViewById(R.id.switch_darkTheme);

        tvDarkThemeSubtext = rootView.findViewById(R.id.tv_darkThemeSubtext);

//        ivThemeIcon = view.findViewById(R.id.iv_themeIndicator);
//
//        TextDrawable themeTextDrawable = TextDrawable.builder()
//                .beginConfig()
//                .bold()
//                .toUpperCase()
////                .withBorder(3)
//                .endConfig()
////                        .buildRound(teamInitails, Color.parseColor(mTeamMember.getTeamMemberColor()));
//                .buildRound("", ContextCompat.getColor(getActivity(), R.color.colorPrimary));
//
//        ivThemeIcon.setImageDrawable(themeTextDrawable);
    }


    private void componentEvents() {
//        llTheme.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), DarkModeActivity.class);
//                startActivityForResult(intent, REQUEST_CODE_THEME);
//            }
//        });

        llFontSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FontSizeActivity.class);
                startActivityForResult(intent, REQUEST_CODE_FONT_SIZE);
            }
        });

        llAutoScroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AutoScrollActivity.class);
                startActivityForResult(intent, REQUEST_CODE_AUTO_SCROLL);
            }
        });

        llShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareApp();
            }
        });

        llRateUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateUsPlayStore();
            }
        });

        llAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AboutUsActivity.class);
                startActivity(intent);
            }
        });

        switchDarkTheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }

                AppSharedPreference.SSP().putBoolean(Constants.KEY_IS_DARK_THEME_ENABLED, isChecked);
            }
        });
    }

    private void setNightModeSwitch() {
        if (isNightModeEnabled()) {
            switchDarkTheme.setChecked(true);
            tvDarkThemeSubtext.setText(getString(R.string.dark_mode_active));
        } else {
            switchDarkTheme.setChecked(false);
            tvDarkThemeSubtext.setText(getString(R.string.light_mode_active));
        }
    }

    private boolean isNightModeEnabled() {
        boolean isDarkThemeEnabled = AppSharedPreference.SSP().getIsDarkThemeEnabled();
        return isDarkThemeEnabled;
    }

    private void shareApp() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
        String shareMessage = "\nLet me recommend you this application\n\n";
        shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);

        try {
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            Toasty.normal(getActivity(), getString(R.string.app_not_found_for_sharing), Toast.LENGTH_LONG).show();
        }
    }

    private void rateUsPlayStore() {
        Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_THEME:
                if (resultCode == Activity.RESULT_OK) {
//                    FragmentTransaction ft = getFragmentManager().beginTransaction();
//                    if (Build.VERSION.SDK_INT >= 26) {
//                        ft.setReorderingAllowed(false);
//                    }
//                    ft.detach(this).attach(this).commit();
                }

                break;
        }
    }
}
