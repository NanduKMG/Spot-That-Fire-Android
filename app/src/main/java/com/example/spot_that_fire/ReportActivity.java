package com.example.spot_that_fire;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ReportActivity extends AppCompatActivity {

    Button home, rehab;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        intent = getIntent();

        home = (Button)findViewById(R.id.home);
        rehab = (Button)findViewById(R.id.rehab);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReportActivity.this, MainActivity.class));
            }
        });

        rehab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lat = intent.getStringExtra("lat");
                String lng = intent.getStringExtra("long");

                Intent changeIntent = new Intent(ReportActivity.this, RehabActivity.class);
                changeIntent.putExtra("lat",lat);
                changeIntent.putExtra("long",lng);
                startActivity(changeIntent);
            }
        });
    }

    //Upload data, gps and stuff
}
