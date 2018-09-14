package com.example.thebeast.afyahelp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Bleeding_main extends AppCompatActivity {

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bleeding_main);
        listView=(ListView) findViewById(R.id.list_res);



        String[]description=getResources().getStringArray(R.array.array_bleeding_problem);

        Integer[] imgid={
                R.drawable.noseblood,
                R.drawable.eyeinjur,
                R.drawable.brusinginjur,
                R.drawable.earbleeding
        };

        ListAdapter lady=new CustomAdapter(this,description,imgid);

        listView.setAdapter(lady);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int number=parent.getPositionForView(view);

                if(number==0){

                    Intent i=new Intent(getApplicationContext(),Nose_bleeding.class);
                    startActivity(i);
                }
                if(number==1){

                    Intent i=new Intent(getApplicationContext(),Eye_injury.class);
                    startActivity(i);
                }
                if(number==2){

                    Intent i=new Intent(getApplicationContext(),Bruising.class);
                    startActivity(i);
                }
                if(number==3){

                    Intent i=new Intent(getApplicationContext(),Ear_injury.class);
                    startActivity(i);
                }


            }
        });


    }
}
