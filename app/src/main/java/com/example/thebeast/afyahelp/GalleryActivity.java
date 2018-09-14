package com.example.thebeast.afyahelp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.joda.time.LocalDate;
import org.joda.time.Years;

public class GalleryActivity extends AppCompatActivity {
TextView user_name,phone_no,user_mail,blood_group,user_weight,
        user_location,age,gender,rhesus;
ImageView imageView;
LinearLayout call_button,mail_button;
String phone_no_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        imageView= findViewById(R.id.gallery_image);
        user_name= findViewById(R.id.account_flname);
        user_mail= findViewById(R.id.user_mail);
        blood_group = findViewById(R.id.gall_blood);
        user_weight  = findViewById(R.id.user_weight);
        user_location = findViewById(R.id.user_location);
        age  = findViewById(R.id.user_age);
        gender = findViewById(R.id.gender);
        rhesus=findViewById(R.id.id_rhesus);
        phone_no=findViewById(R.id.phone_no);


        call_button  = findViewById(R.id.call_button);
        mail_button = findViewById(R.id.mail_button);


        call_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callMe();
            }
        });

        mail_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mailMe();
            }
        });


        getIcomingExtra();
    }


    private void mailMe() {
        final String email=getIntent().getStringExtra("email");

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
                        String[]recepients= email.split(",");

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



    private void callMe() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            if(ContextCompat.checkSelfPermission(GalleryActivity.this, android.Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                // if the permission has been denied allow user to request for the permission
                ActivityCompat.requestPermissions(GalleryActivity.this, new String[]{Manifest.permission.CALL_PHONE},1);

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
                                String dial="tel:"+phone_no_value;
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

    public void getIcomingExtra(){

        if(getIntent().hasExtra("image_url")&&getIntent().hasExtra("fname")&&getIntent().hasExtra("lname")&&getIntent()
                .hasExtra("age")&&getIntent().hasExtra("email")&&getIntent().hasExtra("gender")&&getIntent()
                .hasExtra("weight")&&getIntent().hasExtra("location")&&getIntent().hasExtra("blood_group")){

            String image_uri=getIntent().getStringExtra("image_url");
            String thumburi=getIntent().getStringExtra("thumburi");
            String fname=getIntent().getStringExtra("fname");
            String lname=getIntent().getStringExtra("lname");
            String email=getIntent().getStringExtra("email");
            String blood_group1=getIntent().getStringExtra("blood_group");
            Long weight1=getIntent().getLongExtra("weight",0);
            Long bith_timestamp=getIntent().getLongExtra("age",0);
            String gender1=getIntent().getStringExtra("gender");
            String location1=getIntent().getStringExtra("location");
            String rhesus1=getIntent().getStringExtra("rhesus");
            phone_no_value=getIntent().getStringExtra("phoneno");


             //handles date of birth
            java.util.Date time1=new java.util.Date((long)bith_timestamp);

            java.util.Date time=new java.util.Date((long)timeStamp_now());

            LocalDate birthdate = new LocalDate(time1);

            LocalDate now= new LocalDate(time);
            int year_diff  = Years.yearsBetween(birthdate, now).getYears();


                //used for loading profile pic
            RequestOptions placeHolder=new RequestOptions();
            placeHolder.placeholder(R.drawable.profile_placeholder);
            Glide.with(this).applyDefaultRequestOptions(placeHolder).load(image_uri).thumbnail(
                    //loads the thumbnail if the image has not loaded first
                    Glide.with(this).load(thumburi)
            ).into(imageView);



            //used to set the string contents

            user_name.setText(fname+" "+lname);

            user_location.setText(location1);
            user_mail.setText(email);
            blood_group.setText(blood_group1);
            user_weight.setText(""+weight1.intValue());
            age.setText(""+year_diff);
            gender.setText(gender1);
            rhesus.setText(rhesus1);
            phone_no.setText(phone_no_value);
        }

    }


    private Object timeStamp_now() {
        Long timestamp=System.currentTimeMillis();
        return timestamp;
    }
}
