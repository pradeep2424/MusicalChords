package com.music.chords.bottomMenu;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.music.chords.BuildConfig;
import com.music.chords.R;


public class SettingsFragment extends Fragment {

    private View view;
    private LinearLayout llTheme;
    private LinearLayout llFontSize;
    private LinearLayout llShare;
    private LinearLayout llRateUs;
    private LinearLayout llContactUs;
//    private ImageView ivThemeIcon;

    private final int REQUEST_CODE_LANGUAGE_SELECTION = 100;

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
//                startActivityForResult(intent, REQUEST_CODE_LANGUAGE_SELECTION);
////                finish();
            }
        });

        llFontSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DialogFragment dialogFragment = SetThemeDialogFragment.newInstance(mCurrentTheme);
//                DialogUtils.showDialogFragment(getActivity().getSupportFragmentManager(), dialogFragment);
            }
        });

        llShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), ShareActivity.class);
//                startActivity(intent);
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
            case REQUEST_CODE_LANGUAGE_SELECTION:
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
