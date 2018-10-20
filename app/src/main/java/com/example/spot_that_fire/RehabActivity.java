package com.example.spot_that_fire;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.Locale;

public class RehabActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rehab);
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
        Double latitude = 8.531, longitude = 76.9139;

        String uri = String.format(Locale.ENGLISH, "google.navigation:q=%f,%f", latitude, longitude);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);

    }
}
