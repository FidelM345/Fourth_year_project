package com.example.thebeast.afyahelp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Fidel M Omolo on 3/31/2018.
 */

public class BlogRecycler_Adapter extends RecyclerView.Adapter<BlogRecycler_Adapter.ViewHolder> {
    List<BlogPost_model_class> bloglist;
    List<User_model_class> userlist;

    Context context;
    FirebaseFirestore firestore;
    FirebaseAuth mAuth;

    public BlogRecycler_Adapter(List<BlogPost_model_class> bloglist, List<User_model_class> userlist, Context context) {
        //the constructor is receiving data from the list data structure in HomeFragment java class
        this.bloglist = bloglist;
        this.userlist = userlist;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //it inflates the custom made Layout file for list items
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_list_item, parent, false);
        // context=parent.getContext();
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);//makes the recycler views not to be recycled
        //gets data stored in the bloglist List data structure, the getDescription model class is found in the model class
        final String userid = bloglist.get(position).BlogPostIdString;
        String fname_data = bloglist.get(position).getFname();
        String lname = bloglist.get(position).getLname();

        String image_uri = bloglist.get(position).getImageuri();
        String thumburi= bloglist.get(position).getThumburi();
        final Long age=bloglist.get(position).getAge();


        String location = bloglist.get(position).getLocation();
        String rhesus=bloglist.get(position).getRhesus();

        String blood_group = bloglist.get(position).getBlood_group();


        //it accesses the setDescription(description_data) method from view holder class
        holder.setImage(image_uri,thumburi);
        holder.setFname(fname_data, lname);

        holder.setLocation(location);
        holder.setBlood_group(blood_group);
        holder.Rhesus.setText(rhesus);

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context,GalleryActivity.class);
                intent.putExtra("image_url",bloglist.get(position).getImageuri());
                intent.putExtra("phoneno",bloglist.get(position).getPhone_no());
                intent.putExtra("fname",bloglist.get(position).getFname());
                intent.putExtra("lname",bloglist.get(position).getLname());
                intent.putExtra("email",bloglist.get(position).getEmail());
                intent.putExtra("blood_group",bloglist.get(position).getBlood_group());
                intent.putExtra("age",age);
                intent.putExtra("gender",bloglist.get(position).getGender());
                intent.putExtra("location",bloglist.get(position).getLocation());
                intent.putExtra("weight",bloglist.get(position).getWeight());
                intent.putExtra("thumburi",bloglist.get(position).getThumburi());
                intent.putExtra("rhesus",bloglist.get(position).getRhesus());
                intent.putExtra("userid", userid);

                context.startActivity(intent);


            }
        });


             }






    @Override
    public int getItemCount() {

        return bloglist.size();//number of items to be populated in the recycler view
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;
        TextView Fname,Phone_no,Location,Blood_group,Rhesus;
        CircleImageView profile_pic;
        ConstraintLayout parentLayout;



        public ViewHolder(View itemView) {
            super(itemView);

            mView=itemView;
            parentLayout=mView.findViewById(R.id.parent_layout);
            Rhesus= mView.findViewById(R.id.id_rhesus);

        }

        public  void setFname(String fname,String lname){

            Fname=mView.findViewById(R.id.mypost_title);
            Fname.setText(fname+" "+lname);
        }


        public void setImage(String image_uri,String thumb){

            profile_pic=mView.findViewById(R.id.mypost_image);

            RequestOptions placeHolder=new RequestOptions();
            placeHolder.placeholder(R.drawable.profile_placeholder);
            Glide.with(context).applyDefaultRequestOptions(placeHolder).load(image_uri).thumbnail(
                    //loads the thumbnail if the image has not loaded first
                    Glide.with(context).load(thumb)
            ).into(profile_pic);

        }


        public void setBlood_group(String bd_group){
            Blood_group=mView.findViewById(R.id.mypost_unlike_count);
            Blood_group.setText("group: "+bd_group);

        }

        public void setLocation(String location){
            Location=mView.findViewById(R.id.mypost_date);
            Location.setText(location);

        }





    }
}
