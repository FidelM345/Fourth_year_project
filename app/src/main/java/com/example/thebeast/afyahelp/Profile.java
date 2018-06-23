package com.example.thebeast.afyahelp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;




 class Profile extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    StorageReference mstorageReference;
    FirebaseAuth mAuth;
    FirebaseFirestore mfirestore;
    EditText Fname, Lname,phone_no,weight,age,gps_location;

     Spinner donor_group1,donor_gender1,donor_rhesus1;

     Button Okbutton;
    CircleImageView circleImageView,btn_location;
    Uri mainImageUri=null;
    boolean isChaanged = false;
    ProgressBar progressBar;
    String user_id;
    Bitmap compressedImageBitmap;
    CheckBox checkBox;
    boolean check_box_value=false;

     Location lastloaction;
     GoogleApiClient googleApiClient;
     LatLng latLng;
     String county=null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //initializing firebase critical objects
        mstorageReference = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mfirestore = FirebaseFirestore.getInstance();

        Fname = findViewById(R.id.profile_fname);
        gps_location = findViewById(R.id.profile_gps);
        Lname = findViewById(R.id.profile_lname);
        weight = findViewById(R.id.profile_weight);
        age = findViewById(R.id.profile_age);
        phone_no = findViewById(R.id.profile_phoneno);
        donor_group1 =findViewById(R.id.profile_blood_group);
        donor_gender1=findViewById(R.id.profile_gender);
        donor_rhesus1=findViewById(R.id.profile_rhesus);
        btn_location=findViewById(R.id.btn_location);




        checkBox=findViewById(R.id.profile_check);

        Okbutton = findViewById(R.id.profile_btn_submit);
        circleImageView = findViewById(R.id.profile_circular);
        progressBar= findViewById(R.id.profile_progressBar);
        progressBar.setVisibility(View.INVISIBLE);


        circleImageView.setOnClickListener(this);
        Okbutton.setOnClickListener(this);
        btn_location.setOnClickListener(this);

        if(checkBox.isChecked()){



        }



        //google api client that allows us to talk to google services
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();



    }


     public void onCheckboxClicked(View view) {
         // Is the view now checked?
         boolean checked = ((CheckBox) view).isChecked();

         // Check which checkbox was clicked
         switch (view.getId()) {
             case R.id.profile_check:
                 if (checked)
                     check_box_value = true;

                 else
                     check_box_value = false;

                 break;

         }

     }

         @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.profile_circular:
                //allow user to select profile pic from phone memory
                profilePicPicker();

                break;

            case R.id.btn_location:


                break;

            case R.id.profile_btn_submit:

                if(isChaanged){ //becomes true when new image is selected
                    final String first_name=Fname.getText().toString().trim();
                    final String user_phoneno=phone_no.getText().toString().trim();
                    final String last_name=Lname.getText().toString().trim();
                    final String user_location=gps_location.getText().toString().trim();
                    final String user_age=age.getText().toString().trim();
                    final String user_weight=weight.getText().toString().trim();

                    final String user_blood_group=donor_group1.getSelectedItem().toString().trim();
                    final String user_gender=donor_gender1.getSelectedItem().toString().trim();
                    final String user_blood_rhesus=donor_rhesus1.getSelectedItem().toString().trim();


                    user_id=mAuth.getCurrentUser().getUid();

                    progressBar.setVisibility(View.VISIBLE);

                    if (!TextUtils.isEmpty(first_name)&&!TextUtils.isEmpty(last_name)&&!TextUtils.isEmpty(user_location)&&
                    !TextUtils.isEmpty(user_age)&& !TextUtils.isEmpty(user_weight) && !TextUtils.isEmpty(user_blood_group)
                            && !TextUtils.isEmpty(user_gender)&& !TextUtils.isEmpty(user_blood_rhesus)&& mainImageUri !=null){


                        StorageReference image_path=mstorageReference.child("Profile_image").child(user_id+".jpg");

                        image_path.putFile(mainImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                if (task.isSuccessful()){
                                    final String main_profile_uri=task.getResult().getDownloadUrl().toString();
                                    storeProfileThumburi(first_name,last_name,user_phoneno,user_location,user_age,user_weight,user_blood_group,
                                            user_gender,user_blood_rhesus,main_profile_uri);

                                }else {
                                    String exception=task.getException().getMessage();
                                    Toast.makeText(Profile.this, "Image Upload Error is: "+exception, Toast.LENGTH_LONG).show();
                                }

                            }
                        });


                    }

                }else {
                    Toast.makeText(Profile.this, "Please select image first by clicking image icon above to setup your account", Toast.LENGTH_LONG).show();
                }
             break;

        }
    }

    private void storeProfileThumburi(final String fname, final String lname, final String phoneno, final String location1, final String age,
                                      final String weight, final String blood_group, final String gender, final String rhesus, final String main_profile_uri) {
        File actualImageFile=new File(mainImageUri.getPath());


        try {
            compressedImageBitmap = new Compressor(Profile.this)
                    //compressing image of high quality to a thumbnail bitmap for faster loading
                    .setMaxWidth(60)
                    .setMaxHeight(60)
                    .setQuality(5)
                    .setCompressFormat(Bitmap.CompressFormat.WEBP)
                    .compressToBitmap(actualImageFile);


        } catch (IOException e) {
            e.printStackTrace();
        }

        //compressing image of high quality to a thumbnail bitmap for faster loading
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        compressedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask thumbfilepath=mstorageReference.child("Forum_images/forum_thumbs").child(user_id+".jpg").putBytes(data);

        thumbfilepath.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                if(task.isSuccessful()){
                    //after the image thumbnail has been uploaded successfully to the cloud storage
                    //it publishes the contents to the firestore cloud storage
                    storeFirestore(task,fname,lname,phoneno,location1,age,weight,blood_group,gender,rhesus,main_profile_uri);//used for storing image and the user name in firebase
                }
                else{
                    String exception=task.getException().getMessage();
                    Toast.makeText(Profile.this, "Thumb Error is: "+exception, Toast.LENGTH_LONG).show();
                }

            }
        });





    }

    private void storeFirestore(Task<UploadTask.TaskSnapshot> task, String first_name, String last_name, String phoneno, String user_location, String age,
                                String weight, String blood_group, String gender, String rhesus, String main_profile_uri) {

        Uri download_uri;
        download_uri=task.getResult().getDownloadUrl();
        progressBar.setVisibility(View.INVISIBLE);
        Map<String,Object> user_details=new HashMap<>();
        user_details.put("fname",first_name);
        user_details.put("lname",last_name);
        user_details.put("phone_no",phoneno);
        user_details.put("email",mAuth.getCurrentUser().getEmail());
        user_details.put("location",user_location);
        user_details.put("age",age);
        user_details.put("weight",weight);
        user_details.put("blood_group",blood_group);
        user_details.put("gender",gender);
        user_details.put("donor",check_box_value);
        user_details.put("rhesus",rhesus);
        user_details.put("imageuri",main_profile_uri);
        user_details.put("thumburi",download_uri.toString());

        mfirestore.collection("user_table").document(user_id).set(user_details).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    gotMainActivity();

                }else{
                    String exception=task.getException().getMessage();
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(Profile.this, "Text Error is: "+exception, Toast.LENGTH_LONG).show();
                }

            }
        });





    }

    private void gotMainActivity() {
        Toast.makeText(this, "Your Account has been setup successfully", Toast.LENGTH_LONG).show();
        Intent intent=new Intent(Profile.this,MainActivity.class);
        startActivity(intent);
        finish();

    }

    private void profilePicPicker() {
        // the first if statement checks whether the user is running android Mash mellow and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(Profile.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // if the permission has been denied allow user to request for the permission

                ActivityCompat.requestPermissions(Profile.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            } else {

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .setScaleType(CropImageView.ScaleType.CENTER_INSIDE)
                        .start(this);

            }
        } else {

            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .setScaleType(CropImageView.ScaleType.CENTER_INSIDE)
                    .start(this);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mainImageUri = result.getUri();
                circleImageView.setImageURI(mainImageUri);
                isChaanged = true;

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }


        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Profile.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        } else {

            lastloaction = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

            if (lastloaction != null) {


                Geocoder geocoder=new Geocoder(getApplicationContext());
                try {
                    List<Address> addressList= geocoder.getFromLocation(lastloaction.getLatitude(),lastloaction.getLongitude(),1);
                    String county1=addressList.get(0).getLocality();
                    String county2=addressList.get(0).getAdminArea();


                    gps_location.setText(county1+", "+county2);

                    } catch (IOException e) {
                    e.printStackTrace();
                }


            }


        }
    }
    @Override
    public void onConnectionSuspended(int i) {

        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

     @Override
     protected void onStart() {
         super.onStart();
         googleApiClient.connect();
     }
 }

