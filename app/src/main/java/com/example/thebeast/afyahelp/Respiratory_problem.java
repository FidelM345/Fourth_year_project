package com.example.thebeast.afyahelp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Respiratory_problem extends AppCompatActivity {

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respiratory_problem);
        listView=(ListView) findViewById(R.id.list_res);

        ImageView back_btn=findViewById(R.id.id_backmain);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(in);

            }
        });

        String[]description=getResources().getStringArray(R.array.array_respiratory_problem);

        Integer[] imgid={
                R.drawable.adult_chock,
                R.drawable.infabt_chock,
                R.drawable.chest_wound,
                R.drawable.asthma2,
                R.drawable.fume2

        };

        ListAdapter lady=new CustomAdapter(this,description,imgid);

        listView.setAdapter(lady);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int number=parent.getPositionForView(view);

                if(number==0){

                    Intent i=new Intent(getApplicationContext(),Adult_Chock.class);
                    startActivity(i);
                }
                if(number==1){

                    Intent i=new Intent(getApplicationContext(),Child_chock.class);
                    startActivity(i);
                }
                if(number==2){

                    Intent i=new Intent(getApplicationContext(),Chest_wound.class);
                    startActivity(i);
                }
                if(number==3){

                    Intent i=new Intent(getApplicationContext(),Asthma.class);
                    startActivity(i);
                }
                if(number==4){

                    Intent i=new Intent(getApplicationContext(),Fume_inhalation.class);
                    startActivity(i);
                }

            }
        });


    }
}
