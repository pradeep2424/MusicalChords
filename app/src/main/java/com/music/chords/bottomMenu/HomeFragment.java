package com.music.chords.bottomMenu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.music.chords.R;
import com.music.chords.loader.DialogLoadingIndicator;


public class HomeFragment extends Fragment {
    DialogLoadingIndicator progressIndicator;
    View rootView;

    View viewToolbarLocation;
    TextView tvToolbarTitle;
    LinearLayout llToolbarLocation;
    LinearLayout llToolbarAddress;

    private final int REQUEST_CODE_LOCATION = 99;

    int userID;
    int restaurantID;
    int zipCode;
//    double referralPoints;

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        triggerTabChangeListener = (TriggerTabChangeListener) context;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        initComponents();
        componentEvents();
//        setToolbarDetails();

        return rootView;
    }

    private void initComponents() {
        progressIndicator = DialogLoadingIndicator.getInstance();
        viewToolbarLocation = rootView.findViewById(R.id.view_toolbarLocation);

        llToolbarLocation = viewToolbarLocation.findViewById(R.id.ll_toolbarLocation);
        llToolbarAddress = viewToolbarLocation.findViewById(R.id.ll_toolbarAddress);
        tvToolbarTitle = viewToolbarLocation.findViewById(R.id.tv_toolbarTitle);
    }

    private void componentEvents() {

        llToolbarAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), LocationGoogleMapActivity.class);
//                intent.putExtra("CalledFrom", ConstantValues.ACTIVITY_ACTION_HOME);
//                startActivityForResult(intent, REQUEST_CODE_LOCATION);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_LOCATION) {
            if (resultCode == Activity.RESULT_OK && data != null) {
//
//                String flag = data.getExtras().getString("MESSAGE");
//                if (flag.equalsIgnoreCase("VIEW_CART")) {
//                    triggerTabChangeListener.setTab(2);
////                    triggerTabChangeListener.setTab(2);
//
//                } else if (flag.equalsIgnoreCase("UPDATE_CART_COUNT")) {
//                    int noOfItems = data.getExtras().getInt("CART_ITEM_COUNT");
//                    triggerTabChangeListener.setBadgeCount(noOfItems);
//                }
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
