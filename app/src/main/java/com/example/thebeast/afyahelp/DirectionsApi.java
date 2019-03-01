package com.example.thebeast.afyahelp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class DirectionsApi extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;

    LocationManager locationManager;
    LocationListener locationListener;
    boolean mapReady = false;

    double latitude, longitude;
    Location mLastLocation;
    Marker marker;
    LocationRequest locationRequest;

    GoogleApiClient mGoogleApiClient;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions_api);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {


            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiclient();
                mMap.setMyLocationEnabled(true);
            }
            else{
                ActivityCompat.requestPermissions(DirectionsApi.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);
            }

        }
        else {
            buildGoogleApiclient();
            mMap.setMyLocationEnabled(true);
        }

    }



    private  synchronized void buildGoogleApiclient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(1000); //update location after every one second
        locationRequest.setFastestInterval(1000);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DirectionsApi.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);

        }

        else{

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation=location;

        if(marker!=null)
            marker.remove();
        latitude=location.getLatitude();
        longitude=location.getLongitude();

        LatLng latLng=new LatLng(latitude,longitude);
        MarkerOptions markerOptions=new MarkerOptions()
                .position(latLng)
                .title("your position")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

        //camera position builder
        CameraPosition cameraPosition=CameraPosition.builder().target(latLng).tilt(65).build();
        marker=mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));

        if(mGoogleApiClient!=null)
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){


            case 1:

                if (grantResults.length>0){

                    if (grantResults[0]==PackageManager.PERMISSION_GRANTED){

                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
                            buildGoogleApiclient();
                            mMap.setMyLocationEnabled(true);
                        }



                    }else if(grantResults[0]==PackageManager.PERMISSION_DENIED){

                        Toast.makeText(getApplicationContext(),"Permission has been denied",Toast.LENGTH_LONG).show();

                    }
                }
        }

    }

}
