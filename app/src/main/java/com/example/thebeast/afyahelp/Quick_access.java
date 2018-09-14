package com.example.thebeast.afyahelp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Quick_access extends AppCompatActivity {
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_access);
        listView=(ListView) findViewById(R.id.id_list);

        String[]description=getResources().getStringArray(R.array.quick_access);

        Integer[] imgid={
                R.drawable.cprtips,
                R.drawable.cpr_breathing,
                R.drawable.child_cpr,
                R.drawable.infant_breathing,
                R.drawable.recovery_two,
                R.drawable.adult_chock,
                R.drawable.infabt_chock,
                R.drawable.chest_wound,
                R.drawable.asthma2,
                R.drawable.fume2,
                R.drawable.food,
                R.drawable.swallowed,
                R.drawable.alcohol,
                R.drawable.snake,
                R.drawable.spider,
                R.drawable.waspsting,
                R.drawable.jelly,
                R.drawable.dogbite,
                R.drawable.noseblood,
                R.drawable.eyeinjur,
                R.drawable.brusinginjur,
                R.drawable.earbleeding,
                R.drawable.heartattack,
                R.drawable.agina,
                R.drawable.fainting,
                R.drawable.shock,
                R.drawable.burn,
                R.drawable.electric,
                R.drawable.chemical,
                R.drawable.sun_burn,
                R.drawable.hypothermia,
                R.drawable.seizure,
                R.drawable.stroke,
                R.drawable.cerebral
        };


        ListAdapter lady=new CustomAdapter(this,description,imgid);
        listView.setAdapter(lady);

        cpr_content();


    }


    public void cpr_content(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int number=parent.getPositionForView(view);

                if(number==0){

                    Intent i=new Intent(getApplicationContext(),General_cpr.class);
                    startActivity(i);
                }else
                if(number==1){

                    Intent i=new Intent(getApplicationContext(),Adult_cpr.class);
                    startActivity(i);
                }else
                if(number==2){

                    Intent i=new Intent(getApplicationContext(),Child_cpr.class);
                    startActivity(i);
                }
               else
                if(number==3){

                    Intent i=new Intent(getApplicationContext(),Infant_cpr.class);
                    startActivity(i);
                }
               else
                if(number==4){

                    Intent i=new Intent(getApplicationContext(),Recovery_position.class);
                    startActivity(i);
                }

                else
                if(number==5){

                    Intent i=new Intent(getApplicationContext(),Adult_Chock.class);
                    startActivity(i);
                }else
                if(number==6){

                    Intent i=new Intent(getApplicationContext(),Child_chock.class);
                    startActivity(i);
                }else
                if(number==7){

                    Intent i=new Intent(getApplicationContext(),Chest_wound.class);
                    startActivity(i);
                }else
                if(number==8){

                    Intent i=new Intent(getApplicationContext(),Asthma.class);
                    startActivity(i);
                }else
                if(number==9){

                    Intent i=new Intent(getApplicationContext(),Fume_inhalation.class);
                    startActivity(i);
                }else
                if(number==10){

                    Intent i=new Intent(getApplicationContext(),Food_poisoning.class);
                    startActivity(i);
                }else
                if(number==11){

                    Intent i=new Intent(getApplicationContext(),Swallowed_poison.class);
                    startActivity(i);
                }else
                if(number==12){

                    Intent i=new Intent(getApplicationContext(),Alcohol_poisoning.class);
                    startActivity(i);
                }else
                if(number==13){

                    Intent i=new Intent(getApplicationContext(),Snake_bite.class);
                    startActivity(i);
                }else
                if(number==14){

                    Intent i=new Intent(getApplicationContext(),Spider_bite.class);
                    startActivity(i);
                }else
                if(number==15){

                    Intent i=new Intent(getApplicationContext(),Bee_sting.class);
                    startActivity(i);
                }else
                if(number==16){

                    Intent i=new Intent(getApplicationContext(),Jelly_sting.class);
                    startActivity(i);
                }else
                if(number==17){

                    Intent i=new Intent(getApplicationContext(),Dog_bite.class);
                    startActivity(i);
                }else
                if(number==18){

                    Intent i=new Intent(getApplicationContext(),Nose_bleeding.class);
                    startActivity(i);
                }else

                if(number==19){

                    Intent i=new Intent(getApplicationContext(),Eye_injury.class);
                    startActivity(i);
                }else
                if(number==20){

                    Intent i=new Intent(getApplicationContext(),Bruising.class);
                    startActivity(i);
                }else
                if(number==21){

                    Intent i=new Intent(getApplicationContext(),Ear_injury.class);
                    startActivity(i);
                }else
                if(number==22){

                    Intent i=new Intent(getApplicationContext(),Heart_Attack.class);
                    startActivity(i);
                }else
                if(number==23){

                    Intent i=new Intent(getApplicationContext(),Agina.class);
                    startActivity(i);
                }
                if(number==24){

                    Intent i=new Intent(getApplicationContext(),Fainting.class);
                    startActivity(i);
                }else
                if(number==25){

                    Intent i=new Intent(getApplicationContext(),Shock.class);
                    startActivity(i);
                }else
                if(number==26){

                    Intent i=new Intent(getApplicationContext(),Burn_scalds.class);
                    startActivity(i);
                }else
                if(number==27){

                    Intent i=new Intent(getApplicationContext(),Electrical_burns.class);
                    startActivity(i);
                }else
                if(number==28){

                    Intent i=new Intent(getApplicationContext(),Chemical_burns.class);
                    startActivity(i);
                }
                else
                if(number==29){

                    Intent i=new Intent(getApplicationContext(),Surn_burn.class);
                    startActivity(i);
                }
                else
                if(number==30){

                    Intent i=new Intent(getApplicationContext(),Hypothermia.class);
                    startActivity(i);
                }
                else
                if(number==31){

                    Intent i=new Intent(getApplicationContext(),Seizure.class);
                    startActivity(i);
                }
                else
                if(number==32){

                    Intent i=new Intent(getApplicationContext(),Stroke.class);
                    startActivity(i);
                }
                else
                if(number==33){

                    Intent i=new Intent(getApplicationContext(),Cerebral_compression.class);
                    startActivity(i);
                }



            }
        });


    }















}
