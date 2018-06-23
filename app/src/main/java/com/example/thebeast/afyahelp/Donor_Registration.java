package com.example.thebeast.afyahelp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Donor_Registration extends AppCompatActivity implements View.OnClickListener{
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    EditText email,weight,age;
    Button register,update;
    Spinner donor_group,donor_gender,donor_rhesus;
    TextView names;
    CircleImageView circleImageView;
    String user_id;
    String fname,lname,location,image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donor_registration);

        donor_group =findViewById(R.id.donor_group);
        donor_gender=findViewById(R.id.donor_gender);
        donor_rhesus=findViewById(R.id.donor_rhesus);
        weight=findViewById(R.id.donor_weight);
        age=findViewById(R.id.donor_age);


        register=findViewById(R.id.donor_btn_donate);
        circleImageView=findViewById(R.id.donor_profile_pic);
        names=findViewById(R.id.donor_names);


        //firebase objects
        mAuth= FirebaseAuth.getInstance();
        user_id=mAuth.getCurrentUser().getUid();//gets user current id
        firestore= FirebaseFirestore.getInstance();


        register.setOnClickListener(this);


        donor_registration();

    }






    public void donor_registration(){

        firestore.collection("user_table").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()){

                    if (task.getResult().exists()){
                        fname=task.getResult().getString("fname");
                        lname=task.getResult().getString("lname");
                        image=task.getResult().getString("imageuri");
                        RequestOptions placeHolder=new RequestOptions();
                        placeHolder.placeholder(R.drawable.profile_placeholder);

                        Glide.with(Donor_Registration.this).setDefaultRequestOptions(placeHolder).load(image).into(circleImageView);
                    }

                    if ( !task.getResult().getString("age").isEmpty()){

                        //ou need to inflate the header view as it is not inflated automatically .
                        names.setText(fname+" "+lname);
                        String age1 = task.getResult().getString("age");
                        String weight1 = task.getResult().getString("weight");
                        weight.setText(weight1);
                        age.setText(age1);
                    }

                }

                else{
                    String exception=task.getException().getMessage();

                    Toast.makeText(Donor_Registration.this, "Error is: "+exception, Toast.LENGTH_LONG).show();

                }


            }
        });

    }

    private void storedata() {

        Map<String,Object> user_details=new HashMap<>();
        user_details.put("blood_group",donor_group.getSelectedItem().toString().trim());
        user_details.put("gender",donor_gender.getSelectedItem().toString().trim());
        user_details.put("weight",weight.getText().toString().trim());
        user_details.put("age",age.getText().toString().trim());
        user_details.put("rhesus",donor_rhesus.getSelectedItem().toString().trim());
        user_details.put("donor",true);



       // DocumentReference documentReference=firestore.collection("donor_table").document();


       firestore.collection("user_table").document(user_id).update(user_details).addOnCompleteListener(new OnCompleteListener<Void>() {
           @Override
           public void onComplete(@NonNull Task<Void> task) {

               if(task.isSuccessful()){

                   Toast.makeText(Donor_Registration.this,"Data has been saved successfully: ",Toast.LENGTH_LONG).show();

                   Intent intent=new Intent(Donor_Registration.this,BloodActivity.class);
                   startActivity(intent);



               }else{

                   Toast.makeText(Donor_Registration.this,"Error message: "+task.getException().getMessage(),Toast.LENGTH_LONG).show();
               }


           }
       });



    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case  R.id.donor_btn_donate:

                if(!TextUtils.isEmpty(donor_group.getSelectedItem().toString().trim())&&!TextUtils.isEmpty(weight.getText().toString().trim())&&!TextUtils.isEmpty(age.getText().toString().trim())&&!TextUtils.isEmpty(donor_rhesus.getSelectedItem().toString().trim())&&!TextUtils.isEmpty(donor_gender.getSelectedItem().toString().trim())){

                    storedata();


                }else{

                    Toast.makeText(Donor_Registration.this,"Please fill all fields",Toast.LENGTH_LONG).show();


                }

            break;

        }


    }
}
