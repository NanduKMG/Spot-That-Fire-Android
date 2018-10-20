package com.example.spot_that_fire;

import android.content.Intent;
import android.content.SharedPreferences;
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
}
