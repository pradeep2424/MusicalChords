package com.music.chords.service.retrofit;

import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Created by Pradeep Jadhav.
 */

public interface ApiInterface {

    @GET("GetQuestionDetails/350/1366")
    Call<ResponseBody> testAnalyzeRespponse();


    @POST("insertUserDetails")
    Call<ResponseBody> insertUserDetails(@Body JsonObject jsonObj);

    @GET("GetUserDetails/{Username}/{Password}")
    Call<ResponseBody> getUserDetails(@Path("Username") String username,
                                      @Path("Password") String password);

    @GET("getSMSDetails")
    Call<ResponseBody> getSMSDetails();

    @POST("insUserAddress")
    Call<ResponseBody> insertUserAddress(@Body JsonObject jsonObj);

    @GET("getUserAddress/{mobileNo}")
    Call<ResponseBody> getUserAddress(@Path("mobileNo") String mobileNo);

    @POST("delUserAddress/{AddressID}")
    Call<ResponseBody> deleteUserAddress(@Path("AddressID") int addressID);

    @GET("getAreaDetails")
    Call<ResponseBody> getAreaDetails();


    @GET("getOrderNo/{hotelID}")
    Call<ResponseBody> getOrderNo(@Path("hotelID") String restaurantID);

//    @GET("getProductDetails/{ClientID}/{foodtypeid}/{CategoryID}")
//    Call<ResponseBody> getProductDetailsData(@Path("ClientID") int clientID,
//                                             @Path("foodtypeid") int foodTypeID,
//                                             @Path("CategoryID") int categoryID);

    @POST("insCartItem")
    Call<ResponseBody> addItemToCart(@Body JsonObject jsonObj);

    @POST("delcartItem/{userids}/{ClientIDs}")
    Call<ResponseBody> deleteCartItem(@Path("userids") int userID, @Path("ClientIDs") int clientID);


    @GET("getcartItem/{userids}/{ClientIDs}")
    Call<ResponseBody> getCartItem(@Path("userids") int userID, @Path("ClientIDs") int clientID);

//    @POST("insCartItem")
//    Call<ResponseBody> addItemToCart(@Body DishObject dishObject);


    @POST("insorder")
    Call<ResponseBody> placeOrder(@Body JsonObject jsonObj);


//    @POST("insorder")
//    Call<ResponseBody> placeOrder(@Body OrderDetailsObject orderDetails);


//    @POST("insorder")
//    Call<ResponseBody> placeOrder(@Body ArrayList<OrderDetailsObject> list);

//    @POST("insertUserDetails")
//    Call<ResponseBody> insertUserDetails(@Body UserDetails body);insHotelDetails


    @GET("getCompletedOrder/{userids}")
    Call<ResponseBody> getCompletedOrders(@Path("userids") int userID);

    @GET("getPendingOrder/{userids}")
    Call<ResponseBody> getPendingOrders(@Path("userids") int userID);

    @GET("getOrderStatus/{OrderNo}")
    Call<ResponseBody> getOrderStatus(@Path("OrderNo") int orderNo);

    @POST
    Call<ResponseBody> getOtpSMS(@Url String url);

    @POST("smsapi/{user}/{pass}/{channel}/{number}/{message}/{SenderID}/{Campaign}")
    Call<ResponseBody> getOtpSMS(@Path("user") String smsUsername,
                                 @Path("pass") String smsPassword,
                                 @Path("channel") String smsChannel,
                                 @Path("number") String smsNumber,
                                 @Path("message") String smsMessage,
                                 @Path("SenderID") String senderID,
                                 @Path("Campaign") String campaign);


    @POST("AddReferal/{NewUserMobNo}/{ReferedMobNo}")
    Call<ResponseBody> addReferral(@Path("NewUserMobNo") String newMobileNo,
                                   @Path("ReferedMobNo") String referredMobNo);

    @POST("setReferrelPoint/{UserID}/{amount}")
    Call<ResponseBody> setReferralPoint(@Path("UserID") int userID,
                                        @Path("amount") double amount);

    @GET("getReferralDetails/{UserID}")
    Call<ResponseBody> getReferralDetails(@Path("UserID") int userID);


    @GET("getPhotoGallary/{HotelID}/{Type}")
    Call<ResponseBody> getSlidingPhotoDetails(@Path("HotelID") int hotelID,
                                              @Path("Type") String type);

    @GET("getCompanyDetails/{Zip}")
    Call<ResponseBody> getCompanyDetails(@Path("Zip") String zipCode);

    @GET("getTopProduct")
    Call<ResponseBody> getTopProducts();

    @GET("getCategory")
    Call<ResponseBody> getCategory();

    @GET("getProductDetails/{Usertypeid}/{ClientID}/{foodtypeid}/{CategoryID}")
    Call<ResponseBody> getProductDetailsData(@Path("Usertypeid") int userTypeID,
                                             @Path("ClientID") int clientID,
                                             @Path("foodtypeid") int foodTypeID,
                                             @Path("CategoryID") int categoryID);

    @GET("getItemRate/{ProductID}")
    Call<ResponseBody> getProductRate(@Path("ProductID") int productID);

    @GET("ItemSearch/{SearchTExt}")
    Call<ResponseBody> getSearchItem(@Path("SearchTExt") String searchText);

    @POST("UpdateUserDetails/{UserID}/{Email}/{FirstName}/{LastName}")
    Call<ResponseBody> updateUserProfile(@Path("UserID") int userTypeID,
                                     @Path("Email") String email,
                                     @Path("FirstName") String firstName,
                                     @Path("LastName") String lastName);

    @GET("getSetting")
    Call<ResponseBody> getAppSetting();

    @POST("RejectOrder/{Reason}/{OrderNo}")
    Call<ResponseBody> cancelOrder(@Path("Reason") String reason,
                                     @Path("OrderNo") int orderNo);

//    @Multipart
//    @POST("UploadFile")
//    Call<ResponseBody> uploadImage(@Part MultipartBody.Part file);

}
