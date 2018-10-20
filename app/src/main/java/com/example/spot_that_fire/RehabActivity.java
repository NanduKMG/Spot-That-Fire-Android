package com.example.spot_that_fire;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spot_that_fire.Models.Rehab_Detail;
import com.example.spot_that_fire.Models.Rehab_min;
import com.example.spot_that_fire.Utils.ApiService;
import com.example.spot_that_fire.Utils.RestApiInterface;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RehabActivity extends AppCompatActivity {

    String key, lat, lng;
    Button phone, contribute, directions;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rehab);

        Intent intent = getIntent();
        key = intent.getStringExtra("key");
        lat = intent.getStringExtra("lat");
        lng = intent.getStringExtra("lng");

        textView = (TextView)findViewById(R.id.rehab_name);
        contribute = (Button)findViewById(R.id.contrib);

        getFullData(key);
    }

    public void getFullData(String key)
    {
        RestApiInterface service = ApiService.getClient();
        Call<Rehab_Detail> call = service.getSpecificRehab(lat, lng, key);

        Log.d("CAlling ","SPECIFIC " + key + " " + lat + " " + lng);

        call.enqueue(new Callback<Rehab_Detail>() {
            @Override
            public void onResponse(Call<Rehab_Detail> call, Response<Rehab_Detail> response) {
                if(response.isSuccessful())
                {
                    Rehab_Detail rehab_detail = response.body();
                    textView.setText(rehab_detail.description+"\n"+rehab_detail.address);
                }
                else
                {
                    Log.d("ERROR","AA");
                    Toast.makeText(getApplicationContext(),"Response Failure",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Rehab_Detail> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"ERROR IN Server",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void dialPhone(View v)
    {
        //Call rehab, change dial to call if possible
        String phoneNumber = "7034240550";
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
    public void onGetDirections(View v)
    {
        //get lat, long of this rehab from backend
        Double latitude = Double.parseDouble(lat), longitude = Double.parseDouble(lng);

        String uri = String.format(Locale.ENGLISH, "google.navigation:q=%f,%f", latitude, longitude);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);

    }
}
