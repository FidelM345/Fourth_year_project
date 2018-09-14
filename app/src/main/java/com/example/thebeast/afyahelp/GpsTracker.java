package com.example.thebeast.afyahelp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;


public class GpsTracker implements LocationListener {

    Context context;

    public GpsTracker(Context c) {
        context = c;

    }

    public Location getLocation() {


        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return null;
        }else{

            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            boolean isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);


            if (isGPSEnabled) {

                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6000, 10, this);
                Location location=lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);


            }else{

                Toast.makeText(context,"Please enable GPS",Toast.LENGTH_LONG).show();
            }


        }

        return null;

    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }






}
