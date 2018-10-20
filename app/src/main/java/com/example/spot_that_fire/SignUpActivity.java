package com.example.spot_that_fire;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.spot_that_fire.Models.ApiResponse;
import com.example.spot_that_fire.Utils.ApiService;
import com.example.spot_that_fire.Utils.RestApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity{

    EditText name, phone;
    Button signup;

    CheckBox cb;

    String userName, userPhone;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private LocationManager locationManager;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle("SIGN UP");

        setContentView(R.layout.activity_signin);

        name = (EditText) findViewById(R.id.name);
        phone = (EditText) findViewById(R.id.phone);

        signup = (Button) findViewById(R.id.signup);

        cb = (CheckBox) findViewById(R.id.checkBox);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        LocationListener locationListenerGPS=new LocationListener() {
            @Override
            public void onLocationChanged(android.location.Location location) {
                editor.putString("lat",String.valueOf(location.getLatitude()));
                editor.putString("lng",String.valueOf(location.getLongitude()));
                editor.commit();
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

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000,
                1, locationListenerGPS);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, locationListenerGPS);

        isLocationEnabled();


        sharedPreferences = getSharedPreferences("userData",MODE_PRIVATE);
        editor = sharedPreferences.edit();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName = name.getText().toString();
                userPhone = phone.getText().toString();

                if(userName.isEmpty())
                    return;

                if(userPhone.isEmpty())
                    return;


                Log.d("UserName",userName);
                Log.d("UserPhone",userPhone);

                editor.putString("name",userName);
                editor.putString("phone",userPhone);

                editor.commit();

                if(cb.isChecked())
                {
                    RestApiInterface service = ApiService.getClient();
                    Call<ApiResponse> call = service.callOTP(userPhone);

                    Log.d("OTP","CALLING");

                    call.enqueue(new Callback<ApiResponse>() {
                        @Override
                        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                            if(response.isSuccessful())
                            {
                                ApiResponse apiResponse = response.body();
                                Log.d("RESPONDE",apiResponse.toString());

                                startActivity(new Intent(SignUpActivity.this,OTPActivity.class));
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Error Generating OTP At Server",Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse> call, Throwable t) {
                            Toast.makeText(getApplicationContext(),"Request Error",Toast.LENGTH_LONG).show();
                        }
                    });
                }


            }
        });
    }

    private void isLocationEnabled() {

        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
            alertDialog.setTitle("Enable Location");
            alertDialog.setMessage("Your locations setting is not enabled. Please enabled it in settings menu.");
            alertDialog.setPositiveButton("Location Settings", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            AlertDialog alert=alertDialog.create();
            alert.show();
        }
        else{
            AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
            alertDialog.setTitle("Confirm Location");
            alertDialog.setMessage("Your Location is enabled, please enjoy");
            alertDialog.setNegativeButton("Back to interface",new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            AlertDialog alert=alertDialog.create();
            alert.show();
        }
    }


}
