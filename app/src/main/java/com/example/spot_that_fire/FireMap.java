package com.example.spot_that_fire;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.spot_that_fire.Models.EonetFire;
import com.example.spot_that_fire.Models.Events;
import com.example.spot_that_fire.Models.Rehab_min;
import com.example.spot_that_fire.Utils.ApiService;
import com.example.spot_that_fire.Utils.RestApiInterface;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FireMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    HashMap<Marker, Rehab_min> map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        addMarkers();
    }

    public void addMarkers()
    {

        RestApiInterface service = ApiService.getClient();
        Call<EonetFire> call = service.eonetFire();

        Log.d("API CALLLING","EONET FIRE");
        call.enqueue(new Callback<EonetFire>() {
            @Override
            public void onResponse(Call<EonetFire> call, Response<EonetFire> response) {
                if(response.isSuccessful())
                {
                    EonetFire listData = response.body();
                    for(Events rm : listData.events)
                    {
                        Log.d("LATLNG",String.valueOf(rm.geometries.get(0).coordinates.get(1)));
                        LatLng latLng = new LatLng(rm.geometries.get(0).coordinates.get(1), rm.geometries.get(0).coordinates.get(0));
                        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(rm.title).snippet("Click Here For More !"));
                    }
                }
            }

            @Override
            public void onFailure(Call<EonetFire> call, Throwable t) {
                Log.d("Server Error",t.toString());
                Toast.makeText(getApplicationContext(),"Server Error",Toast.LENGTH_LONG).show();
            }
        });
    }
}
