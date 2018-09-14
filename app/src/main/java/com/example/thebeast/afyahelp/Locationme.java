package com.example.thebeast.afyahelp;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.joda.time.LocalDate;
import org.joda.time.Years;

import java.text.DateFormat;
import java.util.Calendar;

public class Locationme extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener ,DatePickerDialog.OnDateSetListener{

    private final String Log_Tag = "Afyahelp";
    private TextView textView,viewDate;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    Button button;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locationme);

        textView=findViewById(R.id.latitude);
        viewDate=findViewById(R.id.id_date);
        button=findViewById(R.id.id_dateclick);

        //google api client that allows us to talk to google services
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePick dialogFragment=new DatePick();
                dialogFragment.show(getSupportFragmentManager(),"Date Picker");



            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {


        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setFastestInterval(1000);
        locationRequest.setInterval(2000); //update location after every one second


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(Locationme.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

        }

        else{
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);

        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
      textView.setText(Double.toString(location.getLatitude()));

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode==1){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){

                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(Locationme.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

                }

                else{
                    LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);

                }




            }else {

                Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        //connects the api client to the google location services
        googleApiClient.connect();
    }


    @Override
    protected void onResume() {
        super.onResume();

        if(googleApiClient.isConnected()){

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(Locationme.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

            }

            else{
                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);

            }

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

        String currentDate= DateFormat.getDateInstance().format(calendar.getTime());

         //saves date of birth in milliseconds
         Long timestamp=calendar.getTimeInMillis();




        java.util.Date time1=new java.util.Date((long)timestamp);

        java.util.Date time=new java.util.Date((long)1531492844*1000);

        LocalDate birthdate = new LocalDate(time1);

        LocalDate now= new LocalDate(time); // test, in real world without args
        Years age = Years.yearsBetween(birthdate, now);

        int year_diff  = Years.yearsBetween(birthdate, now).getYears();



        viewDate.setText(""+year_diff);

    }
}
