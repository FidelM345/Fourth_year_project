package com.example.thebeast.afyahelp;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    int a = 98;
    int b = 0;
    TextView textView,text_location;

    int marks[] = {a, b};

    String students[] = {"Fidel", "Brian"};

    FirebaseFirestore mfirestore;
    ArrayList<PieEntry> yValues = new ArrayList<>();
    LocationManager locationManager;
    LocationListener locationListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        textView = findViewById(R.id.id_fidel);
        text_location= findViewById(R.id.id_location);

        mfirestore = FirebaseFirestore.getInstance();


        mfirestore.collection("Forum_Posts").addSnapshotListener(Main2Activity.this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    // Log.w("Beast", "Listen failed.", e);

                    return;
                }
                if (!queryDocumentSnapshots.isEmpty()) {


                    b = queryDocumentSnapshots.size();

                    textView.setText(Integer.toString(b));

                    yValues.add(new PieEntry(a, "Fidel"));
                    yValues.add(new PieEntry(b, "Brian"));


                    pieCharttwo();


                }

            }
        });

// Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

// Define a listener that responds to location updates
         locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                makeUseOfNewLocation(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

// Register the listener with the Location Manager to receive location updates


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        String locationProvider = LocationManager.NETWORK_PROVIDER;
// Or use LocationManager.GPS_PROVIDER
       // Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);

        //text_location.setText(""+lastKnownLocation.getLongitude()+" "+lastKnownLocation.getLatitude());
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);




    }




    private void makeUseOfNewLocation(Location location) {
        text_location.setText(""+location.getLatitude()+" "+location.getLongitude());
    }


    @Override
    protected void onStop() {
        super.onStop();
        // Remove the listener you previously added
        locationManager.removeUpdates(locationListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Remove the listener you previously added
        locationManager.removeUpdates(locationListener);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        String locationProvider = LocationManager.NETWORK_PROVIDER;
// Or use LocationManager.GPS_PROVIDER
      //  Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);

       // text_location.setText(""+lastKnownLocation.getLongitude()+" "+lastKnownLocation.getLatitude());
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

    }

    public  void pieCharttwo(){


        PieChart pieChart=findViewById(R.id.chatmax);

        pieChart.setUsePercentValues(false);
        pieChart.getDescription().setEnabled(false);
        //pieChart.setExtraOffsets(5,10,5,5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleColor(Color.BLUE);
        pieChart.setTransparentCircleRadius(61f);





        PieDataSet dataSet=new PieDataSet(yValues,"Mathematics Test marks");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData pieData=new PieData(dataSet);
        pieData.setValueTextSize(10f);
        pieData.setValueTextColor(Color.WHITE);
        pieChart.setData(pieData);
        pieChart.animateXY(1000,1000);
        pieChart.invalidate();




    }
}
