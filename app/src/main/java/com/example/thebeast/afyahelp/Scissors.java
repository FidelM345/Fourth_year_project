package com.example.thebeast.afyahelp;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Scissors extends AppCompatActivity {
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scissors);


        builder=new AlertDialog.Builder(Scissors.this);
        final AlertDialog dialog = builder.create();



    }
}
