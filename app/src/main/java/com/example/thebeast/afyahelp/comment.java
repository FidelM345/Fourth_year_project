package com.example.thebeast.afyahelp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class comment extends AppCompatActivity {
    EditText comment_text;
    ImageView comment_btn;
    String blog_post_id,currentUser_id;
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    RecyclerView blog_list_view;
    List<Comment_model> bloglist; //list of type blog post model class.
    List<User_model_class> userlist;
    Comment_Adapter comment_adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);




        mAuth= FirebaseAuth.getInstance();
        firestore= FirebaseFirestore.getInstance();
        currentUser_id=mAuth.getCurrentUser().getUid();

        comment_btn=findViewById(R.id.comment_send);
        comment_text=findViewById(R.id.comment_text);


        bloglist = new ArrayList<>();//initializing content list
        userlist=new ArrayList<>();
        comment_adapter= new Comment_Adapter(bloglist,userlist);//initializing adapter



        blog_post_id=getIntent().getStringExtra("blogpostid");//geting post id



        blog_list_view=findViewById(R.id.comment_recycler);
        blog_list_view.setHasFixedSize(true);
        blog_list_view.setLayoutManager(new LinearLayoutManager(this));

        blog_list_view.setAdapter(comment_adapter);





        comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateTocommentTable();

            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        bloglist.clear();
        dataLoader();//loads data on the recycler view

    }

    public void dataLoader(){
       // Query firstQuery=;

        firestore.collection("Forum_Posts").document(blog_post_id).
                collection("Comments").orderBy("time_stamp", Query.Direction.DESCENDING).addSnapshotListener(this,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {

                if (e != null) {
                    // Log.w("Beast", "Listen failed.", e);

                    return;
                }


                for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {


                    if (documentChange.getType() == DocumentChange.Type.ADDED) {

                        String PostId=documentChange.getDocument().getId();
                        String comment_user_id=documentChange.getDocument().getString("user_id");

                        final Comment_model blogPost_model_class = documentChange.getDocument().toObject(Comment_model.class);


                        firestore.collection("user_table").document(comment_user_id).addSnapshotListener(comment.this,new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {


                                if (e != null) {
                                    // Log.w("Beast", "Listen failed.", e);

                                    return;
                                }

                                if(documentSnapshot.exists()){

                                    User_model_class user_model_class=documentSnapshot.toObject(User_model_class.class);

                                    userlist.add(user_model_class);
                                    bloglist.add(blogPost_model_class);

                                    comment_adapter.notifyDataSetChanged();//notify adapter when data set is changed

                                }

                            }
                        });





                    }

                }





            }
        });


    }

    private long timeStamp() {
        Long timestamp=System.currentTimeMillis()/1000;
        return timestamp;
    }



    private void updateTocommentTable() {

        if(!TextUtils.isEmpty(comment_text.getText())){

            Map<String,Object>contents=new HashMap<>();
            contents.put("message",comment_text.getText().toString().trim());
            contents.put("user_id",currentUser_id);
            contents.put("time_stamp", timeStamp());

            firestore.collection("Forum_Posts").document(blog_post_id).
                    collection("Comments").add(contents).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if(!task.isSuccessful()){
                        Toast.makeText(comment.this, "Comment Error: "+task.getException().getMessage(), Toast.LENGTH_LONG).show();

                    }else{
                        comment_btn.setEnabled(false);
                        comment_text.setText(" ");
                        Toast.makeText(comment.this, "you have clicked me ", Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
    }
}
