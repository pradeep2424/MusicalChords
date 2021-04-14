package com.music.chords.bottomMenu;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.fonts.Font;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.music.chords.BuildConfig;
import com.music.chords.R;
import com.music.chords.activity.FontSizeActivity;
import com.music.chords.activity.ItemDetailsActivity;


public class SettingsFragment extends Fragment {

    private View view;
    private LinearLayout llTheme;
    private LinearLayout llFontSize;
    private LinearLayout llShare;
    private LinearLayout llRateUs;
    private LinearLayout llContactUs;
//    private ImageView ivThemeIcon;

    private final int REQUEST_CODE_THEME = 100;
    private final int REQUEST_CODE_FONT_SIZE = 200;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);

        init();
        componentEvents();

        return view;
    }

    private void init() {
        llTheme = (LinearLayout) view.findViewById(R.id.ll_theme);
        llFontSize = (LinearLayout) view.findViewById(R.id.ll_fontSize);
        llShare = (LinearLayout) view.findViewById(R.id.ll_share);
        llRateUs = (LinearLayout) view.findViewById(R.id.ll_rateUs);
        llContactUs = (LinearLayout) view.findViewById(R.id.ll_contactUs);

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
        llTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), ApplicationLanguageActivity.class);
//                intent.putExtra("RedirectedFromSettings", true);
//                startActivityForResult(intent, REQUEST_CODE_THEME);
////                finish();
            }
        });

        llFontSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FontSizeActivity.class);
                startActivityForResult(intent, REQUEST_CODE_FONT_SIZE);
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

        llContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), AboutUsActivity.class);
//                startActivity(intent);
            }
        });
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
