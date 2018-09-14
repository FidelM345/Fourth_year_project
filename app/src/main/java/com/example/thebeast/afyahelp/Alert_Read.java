package com.example.thebeast.afyahelp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class Alert_Read extends AppCompatActivity {
    TextView alert_title,alert_content;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert__read);
       alert_title = findViewById(R.id.alert_title);
       alert_content = findViewById(R.id.alert_content);
       imageView = findViewById(R.id.forum_image);



       getIcomingExtra();
    }



    public void getIcomingExtra(){

        if(getIntent().hasExtra("image_uri")&&getIntent().hasExtra("title")&&getIntent().hasExtra("description")&&getIntent()
                .hasExtra("thumb_uri")){

            String image_uri=getIntent().getStringExtra("image_uri");
            String Title=getIntent().getStringExtra("title");
            String Description=getIntent().getStringExtra("description");
            String Thumb=getIntent().getStringExtra("thumb_uri");


            RequestOptions placeHolder=new RequestOptions();
            placeHolder.placeholder(R.drawable.profile_placeholder);

            Glide.with(this).applyDefaultRequestOptions(placeHolder).load(image_uri).thumbnail(
                    //loads the thumbnail if the image has not loaded first
                    Glide.with(this).load(Thumb)
            ).into(imageView);

            alert_title.setText(Title);
            alert_content.setText(Description);

        }else{

            Toast.makeText(this,"No content",Toast.LENGTH_LONG).show();

        }

    }
}
