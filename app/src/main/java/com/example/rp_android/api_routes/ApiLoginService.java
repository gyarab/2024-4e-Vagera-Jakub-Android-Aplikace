package com.example.rp_android.api_routes;

import com.example.rp_android.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiLoginService {
    @POST("/api/register_mobile")
    Call<ResponseBody> postRegister(
            @Body User user
    );


    @Headers({"Content-Type: application/json"})
    @POST("api/login_mobile")
    Call<ResponseBody> postLogin(
            @Body User user
    );




}
