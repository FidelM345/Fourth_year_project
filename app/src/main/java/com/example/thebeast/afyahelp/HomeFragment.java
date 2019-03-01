package com.example.thebeast.afyahelp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import com.example.thebeast.afyahelp.Adapters.Forum_Adapter;
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

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;


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

    //Connection_Detector connection_detector;

    RecyclerView recyclerView; //Declaring android widget known as a Recyclview used
    //to depict data elements inform of  a list

     ListenerRegistration firestoreListener;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home, container, false);



        mAuth= FirebaseAuth.getInstance(); //initializing Firebase authentication
        firestore= FirebaseFirestore.getInstance(); //initializing Firebase cloud firestore databse

        bloglist=new ArrayList<>();//initializing the List data structure will be used to hold
        //the posted blog contents
        userlist=new ArrayList<>();

        // Linking the recycler view to its XML definition
        recyclerView=view.findViewById(R.id.blog_recycler_view);
        recyclerView.setNestedScrollingEnabled(false);//allows for smooth scrolling

        //blog_list_view=view.findViewById(R.id.blog_recycler_view);
       // blog_list_view.setLayoutManager(new LinearLayoutManager(getActivity()));

       /* GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),2);
        blog_list_view.setLayoutManager(gridLayoutManager);*/

       LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
       linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);


        forum_adapter=new Forum_Adapter(bloglist);//initializing adapter and loading content from
        //the blog list to it

        recyclerView.setHasFixedSize(true);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setLayoutManager(linearLayoutManager);

       /* AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(forum_adapter);
        alphaAdapter.setDuration(500);*/

        recyclerView.setAdapter(forum_adapter);


        
       // blog_list_view.setHasFixedSize(true);

        return view;//returns the inflated view
    }


    @Override
    public void onStart() {
        super.onStart();
        bloglist.clear();

        //Loading content fetched from cloud firestore to the recyclerview.
        dataLoader();


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        firestoreListener.remove();

    }

    public void dataLoader(){

        //the method is used to query data from cloud firestore and load the data into the List
        //data structure. The List data structre through the help of the adapter displays the
        //contents on a recycler view.
        Query firstQuery=firestore.collection("Forum_Posts").orderBy("timestamp", Query.Direction.DESCENDING);

     firestoreListener=firstQuery.addSnapshotListener(getActivity(),new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {

                for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {

                    if (documentChange.getType() == DocumentChange.Type.ADDED) {

                        String PostId=documentChange.getDocument().getId();

                        final Forum_Adapter_model blogPost_model_class = documentChange.getDocument().toObject(Forum_Adapter_model.class).withId(PostId);

                        String forum_user_id=documentChange.getDocument().getString("user_id");

                        bloglist.add(blogPost_model_class);
                        forum_adapter.notifyDataSetChanged();//notify adapter when data set is changed

                    }

                }


            }
        });


    }

}
