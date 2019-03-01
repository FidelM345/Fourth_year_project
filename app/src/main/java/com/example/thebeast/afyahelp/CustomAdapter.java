package com.example.thebeast.afyahelp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Fidel M Omolo on 11/25/2017.
 */

class CustomAdapter extends ArrayAdapter<String>{

    //private final Activity context;
    private final String[]description;

    private final Integer[]imgid;



    public CustomAdapter(@NonNull Context context, String[]description, Integer[] imgid1) {
        super(context,R.layout.recycle_view, description);


        // this.context= (Activity) context;

        this.description = description;
        this.imgid=imgid1;

    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //the code represents a single row on the list view widget
        LayoutInflater layoutInflater=LayoutInflater.from(getContext());
        View customView=layoutInflater.inflate(R.layout.recycle_view,parent,false);


        //code for getting each single text item on the list
        // String getfooditem=getItem(position);

        TextView textView1=(TextView)customView.findViewById(R.id.text345);
        CircleImageView circleImageView=(CircleImageView)customView.findViewById(R.id.profile_image1);


        textView1.setText(description[position]);

        circleImageView.setImageResource(imgid[position]);


        return customView;

    }

}
