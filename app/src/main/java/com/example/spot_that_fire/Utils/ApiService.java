package com.example.spot_that_fire.Utils;

import com.example.spot_that_fire.Global;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService
{
    static RestApiInterface apiClient;
    public static RestApiInterface getClient()
    {
        if (apiClient==null) {
            apiClient = new Retrofit.Builder()
                    .baseUrl(Global.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(RestApiInterface.class);
        }
        return apiClient;
    }
}
