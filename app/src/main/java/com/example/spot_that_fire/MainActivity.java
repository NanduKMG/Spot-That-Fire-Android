package com.example.spot_that_fire;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spot_that_fire.Models.ApiResponse;
import com.example.spot_that_fire.Models.LocData;
import com.example.spot_that_fire.Models.WeatherInfo;
import com.example.spot_that_fire.Utils.ApiService;
import com.example.spot_that_fire.Utils.RestApiInterface;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements LocationListener{

    SharedPreferences sharedPreferences;
    String lat, lng;
    private LocationManager locationManager;
    TextView textView;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        //set content view AFTER ABOVE sequence (to avoid crash)
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        FirebaseMessaging.getInstance().subscribeToTopic("2");


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        textView = (TextView)findViewById(R.id.airqual);
        textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textView.setSelected(true);
        textView.setSingleLine(true);

        LocationListener locationListenerGPS = new LocationListener() {
            @Override
            public void onLocationChanged(android.location.Location location) {
                Log.d("Location Changed 1","Loc ");
                lat = String.valueOf(location.getLatitude());
                lng = String.valueOf(location.getLongitude());
                weatherData();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };


        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                1,
                1, this);


        sharedPreferences = getSharedPreferences("userData",MODE_PRIVATE);

        if(!sharedPreferences.contains("name"))
            startActivity(new Intent(MainActivity.this,SignUpActivity.class));


    }

    public void weatherData()
    {
        String url = "https://api.breezometer.com/baqi/?lat="+lat+"&lon="+lng+"&key=6238d7c3e69b40ec9e34b439e3ea4370";
        RestApiInterface service = ApiService.getClient();
        Call<WeatherInfo> call = service.getAirQuality(url);

        Log.d("WEATHER CALLED","CALLED");

        call.enqueue(new Callback<WeatherInfo>() {
            @Override
            public void onResponse(Call<WeatherInfo> call, Response<WeatherInfo> response) {
                if(response != null)
                {
                    WeatherInfo weatherInfo = response.body();
                    String S = "";
                    S += "Breezometer : " + weatherInfo.breezometer_description;
                    S += " Children : " + weatherInfo.random_recommendations.children;
                    S += " Health : " + weatherInfo.random_recommendations.health;
                    S += " Sport : " + weatherInfo.random_recommendations.sport;
                    S += " Inside : " + weatherInfo.random_recommendations.inside;
                    S += " Outside : " + weatherInfo.random_recommendations.outside;
                    S += " Dominant Pollution Description : " + weatherInfo.dominant_pollutant_description;

                    Log.d("WeatherData",S);
                    textView.setText(S);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Invalid Response",Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<WeatherInfo> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"ERROR CALLING API",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onReport(View v)
    {
        Intent newActivity = new Intent(this,FireReportActivity.class);
        startActivity(newActivity);
    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        lat = String.valueOf(location.getLatitude());
        lng = String.valueOf(location.getLongitude());
        Log.d("Location Changed 2","Loc "+lat+lng);
        weatherData();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
