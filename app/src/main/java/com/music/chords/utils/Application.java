package com.music.chords.utils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Pradeep Jadhav on 23/10/2019.
 */

public class Application extends android.app.Application {
    private static Application mInstance;
//
//    public static UserDetails userDetails = new UserDetails();
//    public static ClientObject clientObject = new ClientObject();
//    public static CategoryObject categoryObject = new CategoryObject();
//    public static ProductObject productObject = new ProductObject();
//    //    public static CartObject cartObject = new CartObject();
//    public static ArrayList<CartObject> listCartItems = new ArrayList<CartObject>();
//    public static OrderDetailsObject orderDetailsObject = new OrderDetailsObject();
//
//    public static SMSGatewayObject smsGatewayObject;
//    public static AddressData locationAddressData;
//    public static AppSetting appSetting;
////    public static int MINIMUM_FREE_DELIVERY_AMOUNT;

    public static HashMap<String, String> mapBannerDetails = new HashMap<>();

    public Application getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

//        AppSignatureHelper appSignatureHelper = new AppSignatureHelper(this);
//        appSignatureHelper.getAppSignatures();
    }

//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        MultiDex.install(this);
//    }

}

