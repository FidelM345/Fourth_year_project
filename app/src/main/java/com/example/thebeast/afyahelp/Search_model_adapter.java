package com.example.thebeast.afyahelp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Fidel M Omolo on 5/10/2018.
 */

public class Search_model_adapter extends RecyclerView.Adapter<Search_model_adapter.ViewHolder>
{

    List<Search_model> bloglist;
    Context context;


    public Search_model_adapter(List<Search_model> bloglist) {

        this.bloglist = bloglist;
    }


    @NonNull
    @Override
    public Search_model_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //it inflates the custom made Layout file for list items
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_view,parent,false);
        context=parent.getContext();


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Search_model_adapter.ViewHolder holder, int position) {

        final int image=bloglist.get(position).getImage();//gets the blog post id
        final String description=bloglist.get(position).getDescription();









      /*  holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, Object> likeMap = new HashMap<>();
                likeMap.put("TimeStamp", FieldValue.serverTimestamp());

                firestore.collection("Alert_Posts").document(blogpostid).collection("Alert_Views").document(currentUserId).
                        set(likeMap);

                Intent intent=new Intent(context,Alert_Read.class);
                intent.putExtra("image_uri",image_uri);
                intent.putExtra("title",title);
                intent.putExtra("description",description);
                intent.putExtra("thumb_uri",thumb_uri);

                context.startActivity(intent);

            }
        });
*/



        holder.setImageTitle(image,description);

        //notifying adapter incase an item has been deleted




    }

    @Override
    public int getItemCount() {
        return bloglist.size();

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;
        TextView title,like_count,delete,time_diasplay;
        ImageView profile_pic;
        ImageView delete_btn;
        LinearLayout parentLayout;




        public ViewHolder(View itemView) {
            super(itemView);

            mView=itemView;
            parentLayout=mView.findViewById(R.id.alert_parent_layout);



        }



        public void setImageTitle(int Thumburi,String title1){

            title=mView.findViewById(R.id.text345);
            title.setText(title1);

            profile_pic=mView.findViewById(R.id.profile_image1);

            profile_pic.setImageResource(Thumburi);



        }





    }

}
