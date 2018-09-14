package com.example.thebeast.afyahelp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BloodActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<BlogPost_model_class> bloglist;//list of type blog post model class.
    List<User_model_class> userlist;
    BlogRecycler_Adapter blogRecycler_adapter;
    Toolbar toolbar;

    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    ListenerRegistration listenerRegistration;
    FloatingActionButton floatingActionButton;
    Boolean isFirstPageLoad=true;
    Query firstQuery;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        toolbar=findViewById(R.id.blood_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Blood Donors");

        bloglist = new ArrayList<>();
        userlist=new ArrayList<>();

        blogRecycler_adapter= new BlogRecycler_Adapter(bloglist,userlist,this);//initializing adapter

        recyclerView = findViewById(R.id.blog_recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(blogRecycler_adapter);

        firestore=FirebaseFirestore.getInstance();

        if(mAuth.getCurrentUser()!=null) {
            //if user is logged in then show all members in the donor table

              firstQuery = firestore.collection("user_table").whereEqualTo("donor",true);

              showAllgroups();

        }


        }

    public void showAllgroups(){

        firstQuery.addSnapshotListener(this,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {

                if (e != null) {
                    // Log.w("Beast", "Listen failed.", e);

                    Toast.makeText(BloodActivity.this, "No Donor has been registered", Toast.LENGTH_LONG).show();
                    return;
                }

                for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                    String  blogpostid=documentChange.getDocument().getId();


                    //converts data feteched from firebase into a form similar to the model class created
                    BlogPost_model_class blogPost_model_class = documentChange.getDocument().toObject(BlogPost_model_class.class).withId(blogpostid);
                    bloglist.add(blogPost_model_class); //data added to the List data structure the process is repeated
                    //Every time new data is received
                    blogRecycler_adapter.notifyDataSetChanged();//notify adapter when data set is changed

                }

            }

        });
    }



    public void maxo(String a){

        firstQuery.whereEqualTo("blood_group",""+a).addSnapshotListener(this,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {


                if (e != null) {
                    // Log.w("Beast", "Listen failed.", e);

                    Toast.makeText(BloodActivity.this, "No Donor has been registered", Toast.LENGTH_LONG).show();
                    return;
                }

                for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {

                    String  blogpostid=documentChange.getDocument().getId();

                    //converts data feteched from firebase into a form similar to the model class created
                    BlogPost_model_class blogPost_model_class = documentChange.getDocument().toObject(BlogPost_model_class.class).withId(blogpostid);
                    bloglist.add(blogPost_model_class); //data added to the List data structure the process is repeated
                    //Every time new data is received

                    blogRecycler_adapter.notifyDataSetChanged();//notify adapter when data set is changed

                }

            }


        });

    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.blood_group, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.group_all) {
            //displays all blood donors
            bloglist.clear();
         showAllgroups();


        }

        if (id == R.id.group_a) {
            //displays blood type A
            bloglist.clear();
            maxo("A");
            blogRecycler_adapter.notifyDataSetChanged();//notify adapter when data set is changed

        }
        if (id == R.id.group_b) {
            bloglist.clear();
            //displays blood Type B
            maxo("B");
            blogRecycler_adapter.notifyDataSetChanged();//notify adapter when data set is changed


        }
        if (id == R.id.group_l) {
            bloglist.clear();
            //displays blood type O
            maxo("O");

            blogRecycler_adapter.notifyDataSetChanged();//notify adapter when data set is changed


        }


        if (id == R.id.group_ab) {
            bloglist.clear();

            //displays blood type AB
            maxo("AB");
            blogRecycler_adapter.notifyDataSetChanged();//notify adapter when data set is changed


        }


        return true;
    }



}