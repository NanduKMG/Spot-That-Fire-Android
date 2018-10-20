package com.example.spot_that_fire;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.spot_that_fire.Models.ApiResponse;
import com.example.spot_that_fire.Models.Rehab_min;
import com.example.spot_that_fire.Utils.ApiService;
import com.example.spot_that_fire.Utils.RestApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NearestRehabs extends AppCompatActivity {

    String lat, lng;

    List<Rehab_min> listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearest_rehabs);

//        Intent intent = getIntent();
//        lat = intent.getStringExtra("lat");
//        lng = intent.getStringExtra("long");

        if(lat == null)
            lat = "8.563616";

        if(lng == null)
            lng = "76.860115";

        getRehabsNearBy();
    }

    public void getRehabsNearBy()
    {
        RestApiInterface service = ApiService.getClient();
        Call<List<Rehab_min>> call = service.getNearbyRehabs(lat, lng);

        Log.d("API CALLLING","ONNUM NADANILLA"+lat+lng);
        call.enqueue(new Callback<List<Rehab_min>>() {
            @Override
            public void onResponse(Call<List<Rehab_min>> call, Response<List<Rehab_min>> response) {
                if(response.isSuccessful())
                {
                    listData = response.body();
                    for(Rehab_min rm : listData)
                        Log.d("Desc",rm.desc);
                }
            }

            @Override
            public void onFailure(Call<List<Rehab_min>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Server Error",Toast.LENGTH_LONG).show();
            }
        });
    }
}
