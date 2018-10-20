package com.example.spot_that_fire;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spot_that_fire.Models.ApiResponse;
import com.example.spot_that_fire.Models.LocData;
import com.example.spot_that_fire.Utils.ApiService;
import com.example.spot_that_fire.Utils.RestApiInterface;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FireReportActivity extends AppCompatActivity {

    PlacePicker placePicker;
    TextView file;
    EditText description;

    int PLACE_PICKER_REQUEST = 1;
    int RQS_RECORDING = 2;
    int ACTION_TAKE_VIDEO = 3;
    int TAKE_PIC = 4;

    Button audio, video, photo, chooseLoc, report;

    LatLng latLng;

    FirebaseStorage storage;
    StorageReference storageRef;

    LocData locData;
    Uri fileUri;

    SharedPreferences sharedPreferences;



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
        file = (TextView)findViewById(R.id.filePath);
        description = (EditText)findViewById(R.id.description);

        sharedPreferences = getSharedPreferences("userData",MODE_PRIVATE);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        chooseLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseLocation();
            }
        });

        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =
                        new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
                startActivityForResult(intent, RQS_RECORDING);
            }
        });

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30);
                startActivityForResult(takeVideoIntent, ACTION_TAKE_VIDEO);
            }
        });

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =
                        new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, TAKE_PIC);
            }
        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reportFire();
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                latLng = place.getLatLng();
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                getLocation(latLng);
            }
        }

        if(requestCode == RQS_RECORDING || requestCode == ACTION_TAKE_VIDEO || requestCode == TAKE_PIC){
            fileUri = data.getData();
            Toast.makeText(FireReportActivity.this,
                    "Saved: " + fileUri.getPath(),
                    Toast.LENGTH_LONG).show();
            file.setText(file.getText().toString() + " " + fileUri.getPath());
        }
    }


    void reportFire()
    {
        if(locData != null)
        {
            if(locData.country != null)
            {
                storageRef = storageRef.child(locData.country);
                if(locData.state != null)
                {
                    storageRef = storageRef.child(locData.state);
                    if(locData.district != null)
                        storageRef = storageRef.child(locData.district);
                }
            }

            storageRef = storageRef.child(sharedPreferences.getString("phone",null));
            if(locData.district != null)
                Log.d("LOCATION",locData.district);

            Log.d("STORAGEREF",storageRef.getPath());
            Log.d("FILEURI",fileUri.toString());
            UploadTask uploadTask = storageRef.putFile(fileUri);
            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                    Log.d("SUCCESSFOOL","FILE");
                    Toast.makeText(getApplicationContext(),taskSnapshot.toString(),Toast.LENGTH_LONG).show();
                }
            });

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return storageRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        Toast.makeText(getApplicationContext(),downloadUri.toString(),Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(),"FAILED DOWNLOAD LINK GEN",Toast.LENGTH_LONG).show();
                        // ...
                    }
                }
            });
        }

    }

    void getLocation(LatLng latLng)
    {
        RestApiInterface service = ApiService.getClient();
        Call<LocData> call = service.getLocData(String.valueOf(latLng.latitude), String.valueOf(latLng.longitude));

        Log.d("LOCDATA","CALLING");

        call.enqueue(new Callback<LocData>() {
            @Override
            public void onResponse(Call<LocData> call, Response<LocData> response) {
                if(response.isSuccessful())
                {
                    locData = response.body();
                    Log.d("LOCDATA",locData.toString());
                }
            }

            @Override
            public void onFailure(Call<LocData> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"SERVER ERR",Toast.LENGTH_LONG).show();
            }
        });
    }
}
