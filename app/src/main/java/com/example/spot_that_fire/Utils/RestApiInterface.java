package com.example.spot_that_fire.Utils;

import com.example.spot_that_fire.Models.ApiResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RestApiInterface {

    @FormUrlEncoded
    @POST("/auth/getOTP")
    Call<ApiResponse> callOTP(@Field("mobileNo") String mobile);

    @FormUrlEncoded
    @POST("/auth/verifyOTP")
    Call<ApiResponse> verifyOTP(@Field("mobileNo") String mobile, @Field("otp") String otp);

}
