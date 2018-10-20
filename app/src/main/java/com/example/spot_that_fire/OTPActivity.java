package com.example.spot_that_fire;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.spot_that_fire.Models.ApiResponse;
import com.example.spot_that_fire.Utils.ApiService;
import com.example.spot_that_fire.Utils.RestApiInterface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPActivity extends AppCompatActivity {

    EditText otp;
    Button submit;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        submit = (Button)findViewById(R.id.submit);
        otp = (EditText)findViewById(R.id.otp);

        sharedPreferences = getSharedPreferences("userData",MODE_PRIVATE);
        editor = sharedPreferences.edit();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String OTP = otp.getText().toString();
                String phone = sharedPreferences.getString("phone",null).toString();

                RestApiInterface service = ApiService.getClient();
                Call<ApiResponse> call = service.verifyOTP(phone, OTP);

                Log.d("OTP","CALLING");

                call.enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if(response.isSuccessful())
                        {
                            ApiResponse apiResponse = response.body();
                            uploadPersonData();
                            Toast.makeText(getApplicationContext(),"SUCCESSFULLY CREATED ACCOUND",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(OTPActivity.this,MainActivity.class));
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Incorrect OTP",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),"Request Error",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    public void uploadPersonData()
    {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.show();

        String name = sharedPreferences.getString("name",null);
        String phone = sharedPreferences.getString("phone",null);
        String lat = sharedPreferences.getString("lat",null);
        String lng = sharedPreferences.getString("lng",null);

        RestApiInterface service = ApiService.getClient();
        Call<ApiResponse> call = service.signUp(lat,lng,name,phone);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if(response.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(), "Registration Complete",Toast.LENGTH_LONG).show();

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Response Failure",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Server Error",Toast.LENGTH_LONG).show();
            }
        });

        progressDialog.dismiss();
    }
}
