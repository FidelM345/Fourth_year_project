package com.example.thebeast.afyahelp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FirstAidWeb extends AppCompatActivity {
    RecyclerView blog_list_view;
    List<FirstAidWeb_model> bloglist; //list of type blog post model class.
    FirebaseFirestore firestore;
    FirstAidWeb_Adapter AlertFragment_adapter;
    FirebaseAuth mAuth;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_aid_web);

        //toolbar = findViewById(R.id.emergency_toolbar);
        /*setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Emergency Services");
*/
        mAuth= FirebaseAuth.getInstance();
        firestore= FirebaseFirestore.getInstance();

        bloglist=new ArrayList<>();

        blog_list_view=findViewById(R.id.alert_recycler);
        blog_list_view.setLayoutManager(new LinearLayoutManager(FirstAidWeb.this));

        AlertFragment_adapter=new FirstAidWeb_Adapter(bloglist);//initializing adapter
        blog_list_view.setAdapter(AlertFragment_adapter);

        ImageView back_btn=findViewById(R.id.id_backmain);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(in);

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        bloglist.clear();
        dataLoader();
    }


    private void dataLoader() {

        Query firstQuery=firestore.collection("FirstAidWeb_Posts").orderBy("timestamp", Query.Direction.DESCENDING);

        firstQuery.addSnapshotListener(FirstAidWeb.this,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {


                if (e != null) {
                    // Log.w("Beast", "Listen failed.", e);

                    return;
                }

                for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {

                    if (documentChange.getType() == DocumentChange.Type.ADDED ) {

                        //String PostId=documentChange.getDocument().getId();

                        FirstAidWeb_model forum_adapter_model= documentChange.getDocument().toObject(FirstAidWeb_model.class);


                        bloglist.add(forum_adapter_model);
                        AlertFragment_adapter.notifyDataSetChanged();//notify adapter when data set is changed

                    }


                }


            }
        });

    }

}
