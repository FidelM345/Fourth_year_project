package com.example.thebeast.afyahelp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class EmergencyLine_View extends AppCompatActivity implements View.OnClickListener {
    TextView e_no, e_email, e_website,e_title;
    ImageView imageView, Btn_call, Btn_email, Btn_web, Btn_location;
    Toolbar toolbar;

    String Mobileno,Weburl,Email,Latitude,Longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_line__view);
        /*toolbar = findViewById(R.id.emergency_toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Emergency Services");*/

        e_no = findViewById(R.id.ambulance_no);
        e_website = findViewById(R.id.ambulance_web);
        e_email = findViewById(R.id.ambulance_email);
        e_title = findViewById(R.id.toolbar_title);

        imageView = findViewById(R.id.emergency_pic);
        Btn_call = findViewById(R.id.btn_call);
        Btn_email = findViewById(R.id.btn_email);
        Btn_web = findViewById(R.id.btn_web);
        Btn_location = findViewById(R.id.btn_location);





        getIcomingExtra();
        Btn_call.setOnClickListener(this);
        Btn_email.setOnClickListener(this);
        Btn_location.setOnClickListener(this);
        Btn_web.setOnClickListener(this);

    }


    public void getIcomingExtra() {

        if (getIntent().hasExtra("image_uri") && getIntent().hasExtra("title") && getIntent().hasExtra("weburl") && getIntent()
                .hasExtra("thumb_uri")) {

            String image_uri = getIntent().getStringExtra("image_uri");
            String Title = getIntent().getStringExtra("title");
            Weburl = getIntent().getStringExtra("weburl");
            String Thumb = getIntent().getStringExtra("thumb_uri");
            Email = getIntent().getStringExtra("email");
            Mobileno = getIntent().getStringExtra("mobileno");
            Latitude = getIntent().getStringExtra("latitude");
            Longitude = getIntent().getStringExtra("longitude");


            RequestOptions placeHolder = new RequestOptions();
            placeHolder.placeholder(R.drawable.profile_placeholder);

            Glide.with(this).applyDefaultRequestOptions(placeHolder).load(image_uri).thumbnail(
                    //loads the thumbnail if the image has not loaded first
                    Glide.with(this).load(Thumb)
            ).into(imageView);

            e_email.setText(Email);
            e_website.setText(Weburl);
            e_no.setText(Mobileno);
            e_title.setText(Title);

        } else {

            Toast.makeText(this, "No content", Toast.LENGTH_LONG).show();

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_call:
                //do the following when image is clicked

                callMe();
                break;

             case R.id.btn_email:
              mailMe();
                break;

            case  R.id.btn_location:
                ambulance_location();
                break;

            case  R.id.btn_web:
                ambulance_website();
                break;
        }

    }



    private void ambulance_website() {



        Intent intent=new Intent(getApplicationContext(),Web_View.class);
        intent.putExtra("weburl",Weburl);
        startActivity(intent);


    }

    private void ambulance_location() {
        Intent intent=new Intent(EmergencyLine_View.this,Ambulance_Location.class);
        intent.putExtra("latitude",Latitude);
        intent.putExtra("longitude",Longitude);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==1){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){

                //we use an array of index number 0 because only one permission ise being requested
                //this method is called so that one the permission is granted for the first time then the code on the else block will execute immediately
                //helps yo not to preee the button twice inorder to make the phone call
                callMe();
            }else {

                Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
            }
        }

    }

    private void callMe() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            if(ContextCompat.checkSelfPermission(EmergencyLine_View.this, android.Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                // if the permission has been denied allow user to request for the permission
                ActivityCompat.requestPermissions(EmergencyLine_View.this, new String[]{Manifest.permission.CALL_PHONE},1);

            }else{

                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setTitle("Make call");
                builder1.setMessage("Are you sure you want to make the call");
                builder1.setCancelable(true);
                builder1.setIcon(R.mipmap.call_icon);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            @SuppressLint("MissingPermission")
                            public void onClick(DialogInterface dialog, int id) {
                                String dial="tel:"+Mobileno;
                                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));

                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();





            }

        }

    }


    private void mailMe() {


        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Send Mail");
        builder1.setMessage("Are you sure you want to send Mail");
        builder1.setCancelable(true);
        builder1.setIcon(R.mipmap.email_icon);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    @SuppressLint("MissingPermission")
                    public void onClick(DialogInterface dialog, int id) {
                        String[]recepients= Email.split(",");

                        Intent intent=new Intent(Intent.ACTION_SEND);
                        intent.putExtra(Intent.EXTRA_EMAIL,recepients);
                        //EXTRA_SUBJECT,EXTRA_TEXT

                        intent.setType("message/rfc822");//opening email clients
                        startActivity(Intent.createChooser(intent,"Choose an email app to send mail"));
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();



    }





}
