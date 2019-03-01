package com.example.thebeast.afyahelp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.example.thebeast.afyahelp.MarkerInfo.MarkerInfo;
import com.example.thebeast.afyahelp.Model.Myplaces;
import com.example.thebeast.afyahelp.Model.Results;
import com.example.thebeast.afyahelp.Model2.MyPojo;
import com.example.thebeast.afyahelp.Model2.Photo;
import com.example.thebeast.afyahelp.Model2.Result;
import com.example.thebeast.afyahelp.Remote.GoogleApiService;
import com.example.thebeast.afyahelp.Remote2.ApiInterface;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener,RoutingListener
        ,GoogleMap.OnInfoWindowClickListener{


    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    boolean mapReady = false;

    double latitude, longitude;
    Location mLastLocation;
    Marker marker;
    LocationRequest locationRequest;
    GoogleApiService mService;
    ApiInterface apiInterface;
    BottomNavigationView bottomNavigationView;
    ProgressDialog progressDialog;

    ArrayList<Photo> data;

    MarkerOptions myLocation;

    //polylines on the map
    private List<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.colorPrimary};

    /*FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;*/
    GoogleApiClient mGoogleApiClient;
    Toolbar toolbar;
    List<Photo> posts;
    String photo_reference;


    //atert dialog code
    AlertDialog.Builder builder;
    View view;
    ImageView distance,call;
    TextView text_call;
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


        posts = new ArrayList<>();


        //initializing polylines
        polylines = new ArrayList<>();

        //alert dialog
        builder=new AlertDialog.Builder(MapsActivity.this);
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
        apiInterface=ClientInterface_Connection.getGoogleApiService();



    }



    private void nearByPlace(final String placeType) {
        mMap.clear();
        latitude=mLastLocation.getLatitude();
        longitude=mLastLocation.getLongitude();

        LatLng latLng=new LatLng(latitude,longitude);
        myLocation=new MarkerOptions()
                .position(latLng)
                .title("your position")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

        //camera position builder
        CameraPosition cameraPosition=CameraPosition.builder().target(latLng).tilt(65).build();
        mMap.addMarker(myLocation);
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));



        final String url=getUrl(latitude,longitude,placeType);

        mService.getNearByPlaces(url)
                .enqueue(new Callback<Myplaces>() {
                    @Override
                    public void onResponse(Call<Myplaces> call, Response<Myplaces> response) {
                        if (response.isSuccessful()){
                            for(int i = 0; i<response.body().getResults().length; i++){
                                final MarkerOptions markerOptions=new MarkerOptions();
                                Results googlePlace=response.body().getResults()[i];
                                String status=response.body().getStatus();


                                if(status.equals("OK")){

                                    //OK indicates that no errors occurred;
                                    // the place was successfully detected and at least one result was returned.

                                    final double lat=Double.parseDouble(googlePlace.getGeometry().getLocation().getLat());
                                    final double lng=Double.parseDouble(googlePlace.getGeometry().getLocation().getLng());

                                    //final boolean opening_hrs= Boolean.parseBoolean(googlePlace.getOpening_hours().getOpen_now());

                                    //photo_reference=googlePlace.getPhotos().getPhoto_reference();

                                    if(placeType.equals("hospital")) {
                                        //  markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.hospitaly));
                                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                                    }else  if(placeType.equals("pharmacy")) {
                                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));


                                    }  else  if(placeType.equals("restaurant")) {
                                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

                                    }

                                    final String placeName=googlePlace.getName();
                                    //  googlePlace.getOpening_hours()
                                    String vicinity=googlePlace.getVicinity();
                                    final String placeId=googlePlace.getPlace_id();


                                    String url2=getDetails(placeId);

                                    apiInterface.getPlaceDetails(url2).enqueue(new Callback<MyPojo>() {
                                        @Override
                                        public void onResponse(Call<MyPojo> call, Response<MyPojo> response) {

                                            if (response.isSuccessful()) {
                                                String ref="CmRaAAAAAX3wg00supoJiDG79tmiY_57DAwivoinYOP9HfOsuptuvGvUIWTVLsgWdWjyWD7lsg0u4Whg6V2i1pHd48kHyFiRPLRFZC-z7eJo0aRcSZK18i3fM-kh632pDc6pua1XEhDP0Vtd7AKSpezT9GHLxsxCGhRsE5coP1QFyJXMTgzyXszLarD1kQ";
                                                Result googlePlace=response.body().getResult();
                                                String number=googlePlace.getInternationalPhoneNumber();
                                                String name=googlePlace.getName();


                                                posts = googlePlace.getPhotos();

                                                String ref_m = null;

                                                if(posts!=null) {

                                                    for(Photo photo:posts){

                                                    ref_m=photo.getPhotoReference();

                                                    //Log.d("MapsActivity","photo_ref:"+ref_m);

                                                }


                                                }


                                                Log.d("MapsActivity",placeName+":\n"+"photo_ref:"+ref_m);
                                                
                                                // Log.d("MapsActivity", "photo_ref:" + posts);

                                                // Log.d("MapsActivity","name:"+name+"\n"+"phone_no:"+number);

                                                LatLng latLng=new LatLng(lat,lng);

                                                markerOptions.position(latLng);
                                                markerOptions.title(placeName);

                                                MarkerInfo markerInfo = new MarkerInfo();
                                             //   markerInfo.setOpening_Hrs(opening_hrs);
                                                markerInfo.setPhone_number(number);
                                                markerInfo.setPlaceType(placeType);
                                                markerInfo.setPoto_reference(ref_m);
                                                Gson gson = new Gson();
                                                String markerInfoString = gson.toJson(markerInfo);
                                                //adding complex data to map snippet
                                                markerOptions.snippet(markerInfoString);

                                                mMap.addMarker(markerOptions);
                                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                                mMap.animateCamera(CameraUpdateFactory.zoomTo(14));

                                            }

                                        }

                                        @Override
                                        public void onFailure(Call<MyPojo> call, Throwable t) {

                                        }
                                    });




                                }else if(status.equals("ZERO_RESULTS")){

                                  //  indicates that the search was successful but returned no results.
                                    // This may occur if the search was passed a latlng in a remote location.


                                }else if(status.equals("UNKNOWN_ERROR")){
                                //indicates a server-side error; trying again may be successful.

                                    Toast.makeText(getApplicationContext(),"Server error please try again later",Toast.LENGTH_LONG).show();

                                }


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Myplaces> call, Throwable t) {

                     String error= t.getMessage().toString();

                        Toast.makeText(getApplicationContext(),error,Toast.LENGTH_LONG).show();


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

    private String getDetails(String place_id) {

        StringBuilder googlePlacesUri=new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");
        googlePlacesUri.append("placeid="+place_id);
        googlePlacesUri.append("&fields=name,international_phone_number,place_id,photo");
        googlePlacesUri.append("&key="+getResources().getString(R.string.browser_key));


        Log.d("get_url",googlePlacesUri.toString());

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
        CustomInfoWindow adapter = new CustomInfoWindow(MapsActivity.this);
        mMap.setInfoWindowAdapter(adapter);
        mMap.setOnInfoWindowClickListener(this);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {


            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiclient();
               // mMap.setMyLocationEnabled(true);
            }
            else{
                ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);
            }

        }
        else {
            buildGoogleApiclient();
            //mMap.setMyLocationEnabled(true);
        }


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

    @Override
    public void onRoutingFailure(RouteException e) {

        progressDialog.dismiss();
        if(e != null) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        progressDialog.dismiss();


        if(polylines.size()>0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i <route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);


            mMap.animateCamera(CameraUpdateFactory.zoomTo(14));


            myLocation.title("Distance: "+ route.get(i).getDistanceText()+"\n"+"Duration: "+ route.get(i).getDurationText());
            mMap.addMarker(myLocation).showInfoWindow();
            //Toast.makeText(this,"Route "+ (i+1) +": distance - "+ route.get(i).getDistanceText()+": duration - "+ route.get(i).getDurationText(),Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRoutingCancelled() {

    }

    //methods for dealing with the directions api
    private void calculateDirections(Marker marker){
        Log.d("MapsActivity", "calculateDirections: calculating directions.");

        com.google.android.gms.maps.model.LatLng destination = new com.google.android.gms.maps.model.LatLng(
                marker.getPosition().latitude,
                marker.getPosition().longitude
        );


        com.google.android.gms.maps.model.LatLng origin = new com.google.android.gms.maps.model.LatLng(
                mLastLocation.getLatitude(),
                mLastLocation.getLongitude()

        );


        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Fetching route information.", true);

        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(false)
                .waypoints(origin, destination)
                .key(getString(R.string.browser_key))
                .build();
        routing.execute();

    }


    @Override
    public void onInfoWindowClick(final Marker marker) {
        if(marker.getTitle().equals("your position")){
            marker.hideInfoWindow();

        }
        else{


            view= LayoutInflater.from(MapsActivity.this).inflate(R.layout.alertbox_distance,null);
            distance=view.findViewById(R.id.id_distance);
            call=view.findViewById(R.id.id_call);
            text_call=view.findViewById(R.id.text_call);
            builder.setView(view);
            final AlertDialog testDialog = builder.create();
            testDialog.show();


            Gson gson = new Gson();
            final MarkerInfo aMarkerInfo = gson.fromJson(marker.getSnippet(), MarkerInfo.class);

            try{
                if(aMarkerInfo.getPhone_number()==null){
                    call.setVisibility(View.GONE);
                    text_call.setVisibility(View.GONE);
                }

            }catch (Exception e){}



            distance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    testDialog.dismiss();
                    calculateDirections(marker);


                }
            });

            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    testDialog.dismiss();
                  callMe(aMarkerInfo);


                }
            });


        }

    }



    private void callMe(MarkerInfo aMarkerInfo) {

        if(ContextCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
            // if the permission has been denied allow user to request for the permission
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.CALL_PHONE},2);

        }else{

            String dial="tel:"+aMarkerInfo.getPhone_number();
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));

        }
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
