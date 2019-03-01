package com.example.thebeast.afyahelp;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.thebeast.afyahelp.MarkerInfo.MarkerInfo;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class CustomInfoWindow implements
        GoogleMap.InfoWindowAdapter {

    //custom info window adapter
    private Activity context;
    StringBuilder sb;
    String photo_ref;


    public CustomInfoWindow(Activity context){
        this.context = context;

       /* String mystring = context.getResources().getString(R.string.browser_key);
        sb=new StringBuilder("https://maps.googleapis.com/maps/api/place/photo?maxwidth=80");
        sb.append("&photoreference="+photo_ref);
        sb.append("&key=AIzaSyCBwIexTvqu8sAQ6_2Ycgq6Rs8AzUZSfuk");
*/

    }


    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {


        View view = context.getLayoutInflater().inflate(R.layout.info_window, null);

        TextView tvTitle =  view.findViewById(R.id.tv_title);
        ImageView imageView=view.findViewById(R.id.info_pic);


        Gson gson = new Gson();
        final MarkerInfo aMarkerInfo = gson.fromJson(marker.getSnippet(), MarkerInfo.class);


        try {

            if(aMarkerInfo.getPoto_reference()==null){

                if(aMarkerInfo.getPlaceType()=="hospital"){

                    imageView.setImageResource(R.drawable.agina);
                }
                else if (aMarkerInfo.getPlaceType()=="pharmacy"){

                    imageView.setImageResource(R.drawable.adhesive);

                }else if (aMarkerInfo.getPlaceType()=="restaurant"){

                    imageView.setImageResource(R.drawable.adult_chock);
                }

                else if(marker.getTitle()=="your position"){

                    imageView.setVisibility(View.GONE);
                }

            }else {
                photo_ref=aMarkerInfo.getPoto_reference();

                Picasso.get()
                        .load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=120&photoreference="+photo_ref+"&key=AIzaSyCBwIexTvqu8sAQ6_2Ycgq6Rs8AzUZSfuk")
                        .into(imageView);


            }

        }catch (Exception e){

        }



        tvTitle.setText(marker.getTitle());


        /*RequestOptions placeHolder=new RequestOptions();
        placeHolder.placeholder(R.drawable.profile_placeholder);
        Glide.with(context).applyDefaultRequestOptions(placeHolder)
                .load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+photo_ref+"&key=AIzaSyCBwIexTvqu8sAQ6_2Ycgq6Rs8AzUZSfuk").
                into(imageView);*/


        return view;




    }
}
