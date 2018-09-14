package com.example.thebeast.afyahelp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.ListAdapter;
import android.widget.ListView;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/*import java.util.ArrayList;
import java.util.List;*/

public class Cpr_main extends AppCompatActivity {

    ListView listView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpr_main);
        listView=(ListView) findViewById(R.id.id_list);



      ImageView back_btn=findViewById(R.id.id_backmain);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(in);

            }
        });







        String[]description=getResources().getStringArray(R.array.array_cpr1_problem);

        Integer[] imgid={
                R.drawable.cprtips,
                R.drawable.cpr_breathing,
                R.drawable.child_cpr,
                R.drawable.infant_breathing,
                R.drawable.recovery_two
        };



   /*     for (int i=0;i<imgid.length;i++)


        {


            Search_model search_model=new Search_model(imgid[i],description[i]);

            bloglist.add(search_model);

        }
*/

        final ListAdapter lady=new CustomAdapter(this,description,imgid);




        listView.setAdapter(lady);





        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int number=parent.getPositionForView(view);

                if(number==0){

                    Intent i=new Intent(getApplicationContext(),General_cpr.class);
                    startActivity(i);
                }
                if(number==1){

                    Intent i=new Intent(getApplicationContext(),Adult_cpr.class);
                    startActivity(i);
                }
                if(number==2){

                    Intent i=new Intent(getApplicationContext(),Child_cpr.class);
                    startActivity(i);
                }

                if(number==3){

                    Intent i=new Intent(getApplicationContext(),Infant_cpr.class);
                    startActivity(i);
                }

                if(number==4){

                    Intent i=new Intent(getApplicationContext(),Recovery_position.class);
                    startActivity(i);
                }



            }
        });


    }


}
