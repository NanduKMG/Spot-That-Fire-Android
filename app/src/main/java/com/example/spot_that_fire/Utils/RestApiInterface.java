package com.example.spot_that_fire.Utils;

import com.example.spot_that_fire.Models.ApiResponse;
import com.example.spot_that_fire.Models.LocData;
import com.example.spot_that_fire.Models.Rehab_Detail;
import com.example.spot_that_fire.Models.Rehab_min;
import com.example.spot_that_fire.Models.WeatherInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface RestApiInterface {

    @FormUrlEncoded
    @POST("/auth/getOTP")
    Call<ApiResponse> callOTP(@Field("mobileNo") String mobile);

    @FormUrlEncoded
    @POST("/auth/verifyOTP")
    Call<ApiResponse> verifyOTP(@Field("mobileNo") String mobile, @Field("otp") String otp);

    @FormUrlEncoded
    @POST("/apis/user/signup")
    Call<ApiResponse> signUp(@Field("lat") String lat, @Field("long") String lng, @Field("name") String name, @Field("phone") String phone);

    @FormUrlEncoded
    @POST("/apis/fireLoc/reportFire")
    Call<ApiResponse> fireReport(@Field("lat") String lat, @Field("long") String lng, @Field("desc") String desc, @Field("phone") String phone,
                                 @Field("path") String path , @Field("other") int other);

    @FormUrlEncoded
    @POST("/apis/fireLoc/getLocDetails")
    Call<LocData> getLocData(@Field("lat") String lat, @Field("long") String lng);

    @FormUrlEncoded
    @POST("/apis/rehab/getDistrict")
    Call<List<Rehab_min>> getNearbyRehabs(@Field("lat") String lat, @Field("long") String lng);

    @FormUrlEncoded
    @POST("/apis/rehab/getSpecific")
    Call<Rehab_Detail> getSpecificRehab(@Field("lat") String lat, @Field("long") String lng, @Field("rehabID") String rehabID);

    @GET
    Call<WeatherInfo> getAirQuality(@Url String url);
}
