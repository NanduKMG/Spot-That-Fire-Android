package com.example.spot_that_fire;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
//set content view AFTER ABOVE sequence (to avoid crash)
        setContentView(R.layout.activity_main);
    }
    public void onReport(View v)
    {
     //Report clicked, Trigger Report Activity
        Intent newActivity = new Intent(this,RehabActivity.class);
        startActivity(newActivity);
    }
}
