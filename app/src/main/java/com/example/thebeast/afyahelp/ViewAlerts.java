package com.example.thebeast.afyahelp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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


    public void showNotification() {
        NotificationCompat.Builder builder=new NotificationCompat.Builder( this);
        builder.setSmallIcon(R.mipmap.send_arrow);
        builder.setContentTitle("Smart First Aid Notification");
        builder.setContentText("An new Admin notification has been posted");

        Intent intent=new Intent(getApplicationContext(),ViewAlerts.class);

        TaskStackBuilder stackBuilder=TaskStackBuilder.create(this);
        stackBuilder.addParentStack(ViewAlerts.class);
        stackBuilder.addNextIntent(intent);

        PendingIntent pendingIntent=stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0,builder.build());


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

                        if (documentChange.getType() == DocumentChange.Type.ADDED ) {

                            showNotification();
                        String PostId=documentChange.getDocument().getId();

                        Alert_Adapter_model forum_adapter_model= documentChange.getDocument().toObject(Alert_Adapter_model.class).withId(PostId);

                        bloglist.add(forum_adapter_model);
                        AlertFragment_adapter.notifyDataSetChanged();//notify adapter when data set is changed


                    }else if(documentChange.getType() == DocumentChange.Type.REMOVED ){
                        bloglist.clear();
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
