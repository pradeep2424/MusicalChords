package com.music.chords.bottomMenu;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.music.chords.R;
import com.music.chords.interfaces.TriggerTabChangeListener;
import com.music.chords.loader.DialogLoadingIndicator;


public class BookmarksFragment extends Fragment {
    DialogLoadingIndicator progressIndicator;
    View rootView;
    LinearLayout llBrowseMenu;
    TextView tvPaymentButton;

    RelativeLayout rlCartItemDetails;
    View viewEmptyCart;

//    LinearLayout llReferralPointsLayout;
//    SwitchButton switchButtonApplyPoints;
//    TextView tvBalancePoints;
//    TextView tvSaveReferralPointsMessage;

//    private RelativeLayout rlAddReferralBalBillDetails;
//    private RelativeLayout rlAddReferralBalTotalPay;
//    private TextView tvAddReferralMoneyBillDetails;
//    private TextView tvAddReferralMoneyTotalPay;

    private LinearLayout llChangeAddress;
    private TextView tvAddress;

    private TextView tvItemTotal;
    //    private TextView tvRestaurantCharges;
    private TextView tvDeliveryFee;
    private TextView tvDeliveryFreeText;
    private TextView tvDeliveryFreeMessageText;
    private TextView tvTotalPaymentAmount;
    private TextView tvPaymentButtonAmount;

    private RecyclerView rvOrderedItems;

    TriggerTabChangeListener triggerTabChangeListener;

    //    double appliedReferralPoints;
    double totalPayment;
    String mobileNo;
    int userID;
    //    double referralPoints;
    int restaurantID;
    int orderNumber;

    private int MINIMUM_AMOUNT_FOR_FREE_DELIVERY = 0;
    private String ORDER_SUCCESS_SMS_TEMPLATE = "";

    private final int REQUEST_CODE_MOBILE_NO_ACTIVITY = 100;
    private final int REQUEST_CODE_LOCATION = 101;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        triggerTabChangeListener = (TriggerTabChangeListener) context;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_cart, container, false);

//        init();
//        componentEvents();

//            if (Application.listCartItems.size() > 0) {
//                listCartDish.addAll(Application.listCartItems);
//                successOnGetCartItems();
//
//            } else {
//                showEmptyCart();
//            }


        return rootView;
    }

////    private void init() {
////        progressIndicator = DialogLoadingIndicator.getInstance();
////
////        rlCartItemDetails = rootView.findViewById(R.id.rl_cartItemLayout);
////        viewEmptyCart = rootView.findViewById(R.id.view_emptyCart);
////        llBrowseMenu = rootView.findViewById(R.id.ll_browseMenu);
////        tvPaymentButton = rootView.findViewById(R.id.tv_paymentButton);
////        rvOrderedItems = (RecyclerView) rootView.findViewById(R.id.recyclerView_orderedItems);
////
////        llChangeAddress = rootView.findViewById(R.id.ll_changeAddress);
////        tvAddress = rootView.findViewById(R.id.tv_address);
////
////        tvItemTotal = rootView.findViewById(R.id.tv_itemTotalText);
//////        tvRestaurantCharges = rootView.findViewById(R.id.tv_restaurantChargesText);
////        tvDeliveryFee = rootView.findViewById(R.id.tv_deliveryFeeText);
////        tvDeliveryFreeText = rootView.findViewById(R.id.tv_deliveryFeeTextFree);
////        tvDeliveryFreeMessageText = rootView.findViewById(R.id.tv_deliveryFeeMessageText);
////        tvTotalPaymentAmount = rootView.findViewById(R.id.tv_totalPayText);
////        tvPaymentButtonAmount = rootView.findViewById(R.id.tv_paymentButtonAmount);
////
////        String freeDeliveryMsg = getString(R.string.free_delivery_for_orders_above) + " "
////                + MINIMUM_AMOUNT_FOR_FREE_DELIVERY;
////        tvDeliveryFreeMessageText.setText(freeDeliveryMsg);
////    }
//
//    private void componentEvents() {
//        llBrowseMenu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                triggerTabChangeListener.setTab(0);
//            }
//        });
//
//        llChangeAddress.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), LocationGoogleMapActivity.class);
//                intent.putExtra("CalledFrom", ConstantValues.ACTIVITY_ACTION_CART);
//                startActivityForResult(intent, REQUEST_CODE_LOCATION);
//            }
//        });
//
//        tvPaymentButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mobileNo != null) {
//                    placeOrder();
//
//                } else {
//                    Intent intent = new Intent(getActivity(), GetStartedMobileNumberActivity.class);
//                    intent.putExtra("CalledFrom", ConstantValues.ACTIVITY_CART_ACTION_PLACE_ORDER);
//                    startActivityForResult(intent, REQUEST_CODE_MOBILE_NO_ACTIVITY);
//                }
//            }
//        });
//    }

}
