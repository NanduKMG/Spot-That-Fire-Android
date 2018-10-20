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
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FireReportActivity extends AppCompatActivity {

    PlacePicker placePicker;
    int PLACE_PICKER_REQUEST = 1;

    Button audio, video, photo, chooseLoc, report;

    LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle("REPORT FIRE");

        setContentView(R.layout.activity_fire_report);

        //Report FIRE
        audio = (Button)findViewById(R.id.audio);
        video = (Button)findViewById(R.id.video);
        photo = (Button)findViewById(R.id.picture);
        chooseLoc = (Button)findViewById(R.id.chooseloc);
        report = (Button)findViewById(R.id.report);


        chooseLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseLocation();
            }
        });

    }

    public void chooseLocation()
    {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                latLng = place.getLatLng();
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }
}
