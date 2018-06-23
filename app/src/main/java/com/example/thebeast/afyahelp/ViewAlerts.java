package com.example.thebeast.afyahelp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewAlerts extends AppCompatActivity {
    RecyclerView blog_list_view;
    List<Alert_Adapter_model> bloglist; //list of type blog post model class.
    FirebaseFirestore firestore;
    AlertFragment_Adapter AlertFragment_adapter;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_alerts);
        mAuth= FirebaseAuth.getInstance();
        firestore= FirebaseFirestore.getInstance();

        bloglist=new ArrayList<>();

        blog_list_view=findViewById(R.id.alert_recycler);
        blog_list_view.setLayoutManager(new LinearLayoutManager(ViewAlerts.this));

        AlertFragment_adapter=new AlertFragment_Adapter(bloglist);//initializing adapter
        blog_list_view.setAdapter(AlertFragment_adapter);

        dataLoader();
    }






    private void dataLoader() {

        Query firstQuery=firestore.collection("Alert_Posts");

        firstQuery.addSnapshotListener(ViewAlerts.this,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {


                if (e != null) {
                    // Log.w("Beast", "Listen failed.", e);

                    return;
                }

                for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {

                    if (documentChange.getType() == DocumentChange.Type.ADDED) {

                        String PostId=documentChange.getDocument().getId();

                        Alert_Adapter_model forum_adapter_model= documentChange.getDocument().toObject(Alert_Adapter_model.class).withId(PostId);


                        bloglist.add(forum_adapter_model);
                        AlertFragment_adapter.notifyDataSetChanged();//notify adapter when data set is changed


                    }


                }


            }
        });

    }

}
