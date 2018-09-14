package com.example.thebeast.afyahelp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.thebeast.afyahelp.Model.Myplaces;
import com.example.thebeast.afyahelp.Model.Results;
import com.example.thebeast.afyahelp.Remote.GoogleApiService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {


    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    boolean mapReady = false;

    double latitude, longitude;
    Location mLastLocation;
    Marker marker;
    LocationRequest locationRequest;
    GoogleApiService mService;
    BottomNavigationView bottomNavigationView;


    /*FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;*/
    GoogleApiClient mGoogleApiClient;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        toolbar = findViewById(R.id.nearby_toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        bottomNavigationView = findViewById(R.id.map_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.menu_hospital:
                        nearByPlace("hospital");
                        break;

                    case R.id.menu_market:
                        nearByPlace("pharmacy");
                        break;

                    case R.id.menu_restaurant:
                        nearByPlace("restaurant");
                        break;

                    default:
                        return false;
                }

                return true;
            }
        });

        //initialize service
        mService = Common.getGoogleApiService();



    }




    private void nearByPlace(final String placeType) {
        mMap.clear();

        String url=getUrl(latitude,longitude,placeType);

        mService.getNearByPlaces(url)
                .enqueue(new Callback<Myplaces>() {
                    @Override
                    public void onResponse(Call<Myplaces> call, Response<Myplaces> response) {
                        if (response.isSuccessful()){
                            for(int i=0;i<response.body().getResults().length;i++){
                                MarkerOptions markerOptions=new MarkerOptions();
                                Results googlePlace=response.body().getResults()[i];
                                double lat=Double.parseDouble(googlePlace.getGeometry().getLocation().getLat());
                                double lng=Double.parseDouble(googlePlace.getGeometry().getLocation().getLng());

                                String placeName=googlePlace.getName();
                                String vicinity=googlePlace.getVicinity();

                                LatLng latLng=new LatLng(lat,lng);
                                markerOptions.position(latLng);
                                markerOptions.title(placeName);

                                if(placeType.equals("hospital"))
                              //  markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.hospitaly));
                                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                                else  if(placeType.equals("pharmacy"))
                                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));

                                else  if(placeType.equals("restaurant"))
                                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

                                else{

                                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                                }


                                mMap.addMarker(markerOptions);
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(14));


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Myplaces> call, Throwable t) {

                    }
                });


    }

    private String getUrl(double latitude, double longitude, String placeType) {

        StringBuilder googlePlacesUri=new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUri.append("location="+latitude+","+longitude);
        googlePlacesUri.append("&radius="+5000);
        googlePlacesUri.append("&type="+placeType);
        googlePlacesUri.append("&sensor=true");
        googlePlacesUri.append("&key="+getResources().getString(R.string.browser_key));


       Log.d("get url",googlePlacesUri.toString());

        return googlePlacesUri.toString();
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
    public void onMapReady(GoogleMap googleMap) {
        mapReady=true;
        mMap = googleMap;


        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {


            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiclient();
                mMap.setMyLocationEnabled(true);
            }
            else{
                ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);
            }

        }
        else {
            buildGoogleApiclient();
            mMap.setMyLocationEnabled(true);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

       switch (requestCode){


           case 1:

               if (grantResults.length>0){

                   if (grantResults[0]==PackageManager.PERMISSION_GRANTED){


                   }else if(grantResults[0]==PackageManager.PERMISSION_DENIED){


                   }
               }
       }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(1000); //update location after every one second
        locationRequest.setFastestInterval(1000);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);

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

    /*
    public  void me(){
        if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            Toast.makeText(MapsActivity.this, "Network provider ", Toast.LENGTH_LONG).show();
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
                    Geocoder geocoder=new Geocoder(getApplicationContext());
                    try {
                        List<Address> addressList= geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                        String str=addressList.get(0).getCountryName();
                        mMap.addMarker(new MarkerOptions().position(latLng).title(str));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,10.2f));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
            });

        }else if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){

            Toast.makeText(MapsActivity.this, "GPS provider ", Toast.LENGTH_LONG).show();

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                    LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
                    Geocoder geocoder=new Geocoder(getApplicationContext());

                    try {
                        List<Address> addressList= geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                        String str=addressList.get(0).getCountryName();
                        mMap.addMarker(new MarkerOptions().position(latLng).title(str));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,10.2f));


                    } catch (IOException e) {
                        e.printStackTrace();
                   }

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
            });
                }}
*/
}
