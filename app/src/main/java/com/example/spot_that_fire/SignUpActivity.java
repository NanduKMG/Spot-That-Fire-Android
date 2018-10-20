package com.example.spot_that_fire;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class SignUpActivity extends AppCompatActivity {

    EditText name,phone;
    Button signup;

    CheckBox cb;

    String userName, userPhone;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle("SIGN UP");

        setContentView(R.layout.activity_signin);

        name = (EditText)findViewById(R.id.name);
        phone = (EditText)findViewById(R.id.phone);

        signup = (Button)findViewById(R.id.signup);

        cb = (CheckBox)findViewById(R.id.checkBox);

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

                editor.putString("name",userName);
                editor.putString("phone",userPhone);

                editor.commit();

                if(cb.isChecked())
                {
                    RestApiInterface service = ApiService.getClient();
                    Call<ApiResponse> call = service.callOTP(userPhone);

                    call.enqueue(new Callback<ApiResponse>() {
                        @Override
                        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                            if(response.body().success)
                            {
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
}
