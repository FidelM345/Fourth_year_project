package com.example.thebeast.afyahelp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Heat_main extends AppCompatActivity {

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heat_main);
        listView=(ListView) findViewById(R.id.list_res);



        ImageView back_btn=findViewById(R.id.id_backmain);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in=new Intent(getApplicationContext(),Bites_problem.class);
                startActivity(in);

            }
        });



        String[]description=getResources().getStringArray(R.array.array_heat_problem);

        Integer[] imgid={
                R.drawable.burn,
                R.drawable.electric,
                R.drawable.chemical,
                R.drawable.sun_burn,
                R.drawable.hypothermia
        };

        ListAdapter lady=new CustomAdapter(this,description,imgid);

        listView.setAdapter(lady);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int number=parent.getPositionForView(view);

                if(number==0){

                    Intent i=new Intent(getApplicationContext(),Burn_scalds.class);
                    startActivity(i);
                }
                if(number==1){

                    Intent i=new Intent(getApplicationContext(),Electrical_burns.class);
                    startActivity(i);
                }
                if(number==2){

                    Intent i=new Intent(getApplicationContext(),Chemical_burns.class);
                    startActivity(i);
                }
                if(number==3){

                    Intent i=new Intent(getApplicationContext(),Surn_burn.class);
                    startActivity(i);
                }
                if(number==4){

                    Intent i=new Intent(getApplicationContext(),Hypothermia.class);
                    startActivity(i);
                }

            }
        });


    }
}
