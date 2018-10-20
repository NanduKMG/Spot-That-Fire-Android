package com.example.spot_that_fire;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RehabMap extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    String lat,lng;

    List<Rehab_min> listData;

    HashMap<Marker, Rehab_min> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rehab_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        Intent intent = getIntent();
        lat = intent.getStringExtra("lat");
        lng = intent.getStringExtra("lng");

        map = new HashMap<>();

        if(lat == null)
            lat = "8.563616";

        if(lng == null)
            lng = "76.860115";

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        addMarkers();
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
        LatLng sydney = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
        mMap.addMarker(new MarkerOptions().position(sydney).title("Fire Reported Area"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12.0f));

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Log.d("INFOWINDOW","CLICKED");
                Intent intent = new Intent(RehabMap.this, RehabActivity.class);

                Rehab_min rehab_min = map.get(marker);
                intent.putExtra("key",rehab_min.key);
                intent.putExtra("lat",rehab_min.lat);
                intent.putExtra("lng",rehab_min.lng);
                startActivity(intent);
            }
        });
    }


    public void addMarkers()
    {

        RestApiInterface service = ApiService.getClient();
        Call<List<Rehab_min>> call = service.getNearbyRehabs(lat, lng);

        Log.d("API CALLLING","ONNUM NADANILLA"+lat+lng);
        call.enqueue(new Callback<List<Rehab_min>>() {
            @Override
            public void onResponse(Call<List<Rehab_min>> call, Response<List<Rehab_min>> response) {
                if(response.isSuccessful())
                {
                    listData = response.body();
                    for(Rehab_min rm : listData)
                    {
                        Log.d("LATLNG",rm.lat + " " + rm.lng + " " + rm.desc);

                        if(rm.lat != "" && rm.lng != "" && rm.desc != "")
                        {
                            LatLng latLng = new LatLng(Double.parseDouble(rm.lat), Double.parseDouble(rm.lng));
                            Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(rm.desc).snippet("Click Here For More !"));
                            map.put(marker,rm);
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<List<Rehab_min>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Server Error",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onMarkerClick(Marker marker)
    {
        return false;
    }
}
