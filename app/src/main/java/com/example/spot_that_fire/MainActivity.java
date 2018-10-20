package com.example.spot_that_fire;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        //set content view AFTER ABOVE sequence (to avoid crash)
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);

        sharedPreferences = getSharedPreferences("userData",MODE_PRIVATE);

        if(!sharedPreferences.contains("name"))
            startActivity(new Intent(MainActivity.this,SignUpActivity.class));
    }

    public void onReport(View v)
    {
        Intent newActivity = new Intent(this,FireReportActivity.class);
        startActivity(newActivity);
    }
}
