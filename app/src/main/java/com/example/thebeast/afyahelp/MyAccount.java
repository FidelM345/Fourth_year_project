package com.example.thebeast.afyahelp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAccount extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    Button back, update;
    TextView names, email, phoneno, bd_group, gender, weight, age, location, rhesus;
    CircleImageView circleImageView;
    String user_id;
    CheckBox checkBox;
    boolean check_box_value = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        names = findViewById(R.id.account_flname);
        email = findViewById(R.id.account_mail);
        phoneno = findViewById(R.id.account_phone_no);
        bd_group = findViewById(R.id.account_bloodgroup);
        weight = findViewById(R.id.user_weight);
        age = findViewById(R.id.user_age);
        location = findViewById(R.id.user_location);
        gender = findViewById(R.id.gender);
        rhesus = findViewById(R.id.account_rhesus);
        circleImageView = findViewById(R.id.profile_circular);
        checkBox = findViewById(R.id.profile_check);


        //firebase objects
        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();//gets user current id
        firestore = FirebaseFirestore.getInstance();

        donor_registration();


    }


    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.profile_check:
                if (checked) {
                    check_box_value = true;

                    Map<String, Object> map = new HashMap<>();
                    map.put("donor", check_box_value);

                    firestore.collection("user_table").document(user_id).update(map).addOnCompleteListener(
                            new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        Toast.makeText(MyAccount.this, "Your are now a donor", Toast.LENGTH_LONG).show();

                                    }
                                }
                            }
                    );


                } else if (!checked) {
                    check_box_value = false;

                    Map<String, Object> map = new HashMap<>();
                    map.put("donor", check_box_value);

                    firestore.collection("user_table").document(user_id).update(map).addOnCompleteListener(
                            new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(MyAccount.this, "Your are now not a donor", Toast.LENGTH_LONG).show();

                                }
                            }
                    );


                    break;

                }

        }
    }

        public void donor_registration () {

            firestore.collection("user_table").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if (task.isSuccessful()) {

                        if (task.getResult().exists()) {
                            String fname = task.getResult().getString("fname");
                            String lname = task.getResult().getString("lname");
                            String blood_group1 = task.getResult().getString("blood_group");
                            String email1 = task.getResult().getString("email");
                            String gender1 = task.getResult().getString("gender");
                            String rhesus1 = task.getResult().getString("rhesus");
                            String image = task.getResult().getString("imageuri");
                            boolean donor = task.getResult().getBoolean("donor");
                            String PhoneNO = task.getResult().getString("phone_no");

                            names.setText(fname + " " + lname);
                            String age1 = task.getResult().getString("age");
                            String weight1 = task.getResult().getString("weight");
                            weight.setText(weight1);
                            age.setText(age1);
                            bd_group.setText(blood_group1);
                            email.setText(email1);
                            gender.setText(gender1);
                            rhesus.setText(rhesus1);
                            phoneno.setText(PhoneNO);

                            RequestOptions placeHolder = new RequestOptions();
                            placeHolder.placeholder(R.drawable.profile_placeholder);

                            Glide.with(MyAccount.this).setDefaultRequestOptions(placeHolder).load(image).into(circleImageView);

                            if (donor == true) {
                                checkBox.setChecked(true);

                            }


                        }

                    } else {
                        String exception = task.getException().getMessage();

                        Toast.makeText(MyAccount.this, "Error is: " + exception, Toast.LENGTH_LONG).show();

                    }


                }
            });

        }


    }
