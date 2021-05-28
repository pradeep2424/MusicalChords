package com.music.chords.service.retrofit;

import com.google.gson.JsonObject;
import com.music.chords.objects.SongObject;

import java.util.List;

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

    @GET("GetUserDetails/{Username}/{Password}")
    Call<ResponseBody> getUserDetails(@Path("Username") String username,
                                      @Path("Password") String password);

    @GET("getSongDetails")
    Call<ResponseBody> getSongDetails();

//    @Multipart
//    @POST("UploadFile")
//    Call<ResponseBody> uploadImage(@Part MultipartBody.Part file);

}
