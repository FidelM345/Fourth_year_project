package com.example.thebeast.afyahelp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {
    FirebaseAuth mAuth;
    NavigationView navigationView;
    String user_id;
    FirebaseFirestore mfirestore;
    TextView user_names,user_location,gallery,alert;
    CircleImageView user_profile_pic;
    Connection_Detector cd;
    int currentPost=0;


    CardView card_cpr,card_respiratory,card_stings,card_bleeding,card_circulatory,card_poison,card_heat,card_head;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        mAuth = FirebaseAuth.getInstance();
        mfirestore = FirebaseFirestore.getInstance();



        cd=new Connection_Detector(this);


        gallery=(TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                findItem(R.id.nav_slideshow));
        alert=(TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                findItem(R.id.nav_alerts));


        //referencing cardviews
        card_respiratory=findViewById(R.id.Respiratory_card);
        card_circulatory=findViewById(R.id.Circulatory_card);
        card_bleeding=findViewById(R.id.Bleeding_card);
        card_stings=findViewById(R.id.Stings_card);
        card_head=findViewById(R.id.Head_card);
        card_heat=findViewById(R.id.Head_card);
        card_poison=findViewById(R.id.Poison_card);
        card_cpr=findViewById(R.id.CPR_card);


        card_circulatory.setOnClickListener(this);



    }



    private void initializeCountDrawer(){

        //Gravity property aligns the text
        gallery.setGravity(Gravity.CENTER_VERTICAL);
        gallery.setTypeface(null, Typeface.BOLD);
        gallery.setTextColor(getResources().getColor(R.color.colorAccent));

        alert.setGravity(Gravity.CENTER_VERTICAL);
        alert.setTypeface(null, Typeface.BOLD);
        alert.setTextColor(getResources().getColor(R.color.colorAccent));


        mfirestore.collection("Forum_Posts").addSnapshotListener(MainActivity.this,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    // Log.w("Beast", "Listen failed.", e);

                    return;
                }

                if (!queryDocumentSnapshots.isEmpty()){

                    currentPost=queryDocumentSnapshots.size();
                    gallery.setText(Integer.toString(currentPost));

                }


            }
        });

        mfirestore.collection("Alert_Posts").addSnapshotListener(MainActivity.this,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    // Log.w("Beast", "Listen failed.", e);

                    return;
                }

                if (!queryDocumentSnapshots.isEmpty()){

                   int currentAlert=queryDocumentSnapshots.size();
                    alert.setText(Integer.toString(currentAlert));

                }


            }
        });



    }





    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser()==null){
            Intent intent=new Intent(MainActivity.this,Login.class);
            startActivity(intent);
        }

        user_profile_details();

        initializeCountDrawer();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action

            if(cd.isConnected()){

                Toast.makeText(MainActivity.this, "Connected ", Toast.LENGTH_LONG).show();

            }else {

                Toast.makeText(MainActivity.this, "Not Connected ", Toast.LENGTH_LONG).show();

            }


        } else if (id == R.id.nav_gallery) {
            Intent intent=new Intent(MainActivity.this,BloodActivity.class);
            startActivity(intent);


        } else if (id == R.id.nav_slideshow) {


           // currentPost=0;
            Intent intent=new Intent(MainActivity.this,Forum_page.class);
            startActivity(intent);




        } else if (id == R.id.nav_manage) {

            Intent intent=new Intent(MainActivity.this,MapsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_share) {
            Intent intent=new Intent(MainActivity.this,MyAccount.class);
            startActivity(intent);


        } else if (id == R.id.nav_send) {
            mAuth.signOut();
            Intent intent=new Intent(MainActivity.this,Login.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_alerts) {
            Intent intent=new Intent(MainActivity.this,ViewAlerts.class);
            startActivity(intent);


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void user_profile_details(){
        if(mAuth.getCurrentUser()!=null){
            user_id=mAuth.getCurrentUser().getUid();
            mfirestore.collection("user_table").document(user_id).get().addOnCompleteListener(this,new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if (task.isSuccessful()){

                        if (task.getResult().exists()){

                            String fname=task.getResult().getString("fname");
                            String lname=task.getResult().getString("lname");
                            String location=task.getResult().getString("location");
                            String image=task.getResult().getString("imageuri");

                            //ou need to inflate the header view as it is not inflated automatically .
                            View header = navigationView.getHeaderView(0);
                            user_names = (TextView) header.findViewById(R.id.main_name);
                            user_location=header.findViewById(R.id.main_location);
                            user_profile_pic=header.findViewById(R.id.main_profile_pic);

                            user_names.setText(fname+" "+lname);
                            user_location.setText(location);

                            RequestOptions placeHolder=new RequestOptions();
                            placeHolder.placeholder(R.drawable.profile_placeholder);

                            Glide.with(MainActivity.this).setDefaultRequestOptions(placeHolder).load(image).into(user_profile_pic);
                        }
                        else{
                            Toast.makeText(MainActivity.this, "No data has been saved", Toast.LENGTH_LONG).show();

                        }
                    }

                    else{
                        String exception=task.getException().getMessage();

                        Toast.makeText(MainActivity.this, "Data Retreival Error is: "+exception, Toast.LENGTH_LONG).show();

                    }


                }
            });


        }




    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.Circulatory_card:
                Intent i=new Intent(getApplicationContext(),Circulatory_Problem.class);
                startActivity(i);

                break;

                default:
                    break;

        }

    }
}
