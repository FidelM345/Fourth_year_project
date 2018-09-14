package com.example.thebeast.afyahelp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class Ambulance_Location extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    boolean mapReady=false;
    Toolbar toolbar;
    double Latitude,Longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ambulance__location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        toolbar = findViewById(R.id.toolbar_myaccount);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getLatLong();

    }


    public void getLatLong(){
        if (getIntent().hasExtra("latitude") && getIntent().hasExtra("longitude")) {
            Latitude = Double.parseDouble(getIntent().getStringExtra("latitude"));
            Longitude = Double.parseDouble(getIntent().getStringExtra("longitude"));


        }
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
        mapReady=true;
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(Latitude, Longitude);
        //camera position builder




        CameraPosition cameraPosition=CameraPosition.builder().target(sydney).tilt(65).zoom(14).build();


        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.map_hybrid) {

            if(mapReady){

                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            }

        }

        if (id == R.id.map_satellite) {

            if(mapReady){

                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            }


        }

        if (id == R.id.map_std) {

            if(mapReady){

                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }


        }



        return true;
    }





}
