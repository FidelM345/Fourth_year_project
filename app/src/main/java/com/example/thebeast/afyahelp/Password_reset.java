package com.example.thebeast.afyahelp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

public class Password_reset extends AppCompatActivity implements View.OnClickListener{

    EditText forget_password;
    Button reset;
    ProgressBar progressBar;
    String email;
    TextView login;
    AwesomeValidation mAwesomeValidation;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);


        mAuth = FirebaseAuth.getInstance();
        forget_password = findViewById(R.id.reset_email);
        reset= findViewById(R.id.reset_btn);
        login= findViewById(R.id.id_login);
        progressBar= findViewById(R.id.login_progressBar);
        progressBar.setVisibility(View.INVISIBLE);




        mAwesomeValidation = new AwesomeValidation(BASIC);

        mAwesomeValidation.addValidation(Password_reset.this, R.id.reset_email, android.util.Patterns.EMAIL_ADDRESS, R.string.err_email);



        reset.setOnClickListener(this);
        login.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.reset_btn:

                email=forget_password.getText().toString();
                mAwesomeValidation.validate();

                if (!TextUtils.isEmpty(email)){

                    progressBar.setVisibility(View.VISIBLE);
                    methodReset();


            }else{

                    Toast.makeText(Password_reset.this, "Please enter your email in the text field", Toast.LENGTH_LONG).show();

                }



                break;

            case R.id.id_login:

               gotoMain();

                break;


            default:
                return;
        }
    }

    private void methodReset() {

      mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.INVISIBLE);

                            Toast.makeText(getApplicationContext(), "Password has been reset successfully please " +
                                    "check your mail for directions", Toast.LENGTH_LONG).show();


                            Intent intent=new Intent(Password_reset.this,Login.class);
                            startActivity(intent);
                            finish();

                        }else {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Login error: "+task.getException().getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }


    private void gotoMain() {
        Intent intent=new Intent(Password_reset.this,Login.class);
        startActivity(intent);
        finish();

    }
}
