package com.example.thebeast.afyahelp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    RecyclerView blog_list_view;
    List<Forum_Adapter_model> bloglist; //list of type blog post model class.
    List<User_model_class> userlist; //list of type blog post model class.

    FirebaseFirestore firestore;
    Forum_Adapter forum_adapter;
    FirebaseAuth mAuth;
    DocumentSnapshot lastVisible;
    Boolean isFirstPageLoad=true;//true when data is loaded for the first time

    Connection_Detector connection_detector;



    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home, container, false);

        mAuth= FirebaseAuth.getInstance();
        firestore= FirebaseFirestore.getInstance();

        bloglist=new ArrayList<>();
        userlist=new ArrayList<>();


        blog_list_view=view.findViewById(R.id.blog_recycler_view);
        blog_list_view.setLayoutManager(new LinearLayoutManager(getActivity()));
/*
        blog_list_view.setHasFixedSize(true);
*/
        forum_adapter=new Forum_Adapter(bloglist,userlist);//initializing adapter

        blog_list_view.setAdapter(forum_adapter);



        return view;//returns the inflated view
    }


    @Override
    public void onStart() {
        super.onStart();
        bloglist.clear();

        dataLoader();

    }

    public void dataLoader(){
        Query firstQuery=firestore.collection("Forum_Posts");

        firstQuery.addSnapshotListener(getActivity(),new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {


                for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {



                    if (documentChange.getType() == DocumentChange.Type.ADDED) {

                        String PostId=documentChange.getDocument().getId();





                        final Forum_Adapter_model blogPost_model_class = documentChange.getDocument().toObject(Forum_Adapter_model.class).withId(PostId);

                        String forum_user_id=documentChange.getDocument().getString("user_id");
                       // Toast.makeText(getActivity(), "User id is "+forum_user_id, Toast.LENGTH_LONG).show();



                        firestore.collection("user_table").document(forum_user_id).addSnapshotListener(getActivity(),new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {

                                if(documentSnapshot.exists()){

                                    User_model_class user_model_class=documentSnapshot.toObject(User_model_class.class);

                                    userlist.add(user_model_class);
                                    bloglist.add(blogPost_model_class);
                                    forum_adapter.notifyDataSetChanged();//notify adapter when data set is changed

                                }

                            }
                        });









                    }

                }





            }
        });


    }
}
