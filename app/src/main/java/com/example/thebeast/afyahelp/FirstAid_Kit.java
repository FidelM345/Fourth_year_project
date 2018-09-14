package com.example.thebeast.afyahelp;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeStandalonePlayer;

import java.util.Locale;

public class FirstAid_Kit extends AppCompatActivity implements TextToSpeech.OnInitListener{

    CardView card_scissors,card_adhesive,card_cotton,card_gauze,card_gloves,
            card_elastic,card_adhesive_tape,card_soap,card_tweezers,card_thermometer;
    ImageView video,TTs,back_btn;

     AlertDialog.Builder builder;
     View view;

    TextToSpeech engine;
    float pitchRate=1f,speedRate=0.8f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_aid__kit);

        engine=new TextToSpeech(this,this);//initiallizing the TTS engine
        engine.setPitch(pitchRate);
        engine.setSpeechRate(speedRate);


        card_adhesive=findViewById(R.id.Adhesive_card);
        card_cotton=findViewById(R.id.Cotton_card);
        card_gauze=findViewById(R.id.Gauze_card);
        card_gloves=findViewById(R.id.Gloves_card);
        card_elastic=findViewById(R.id.Elastic_card);
        card_adhesive_tape=findViewById(R.id.Adhesive_tape_card);
        card_soap=findViewById(R.id.Soap_card);
        card_tweezers=findViewById(R.id.Tweezers_card);
        card_thermometer =findViewById(R.id.Thermometer_card);
        card_scissors=findViewById(R.id.Scissors_card);

        back_btn=findViewById(R.id.id_backmain);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(in);

            }
        });





        //alert dialog
        builder=new AlertDialog.Builder(FirstAid_Kit.this);




      first_five();

      last_five();

    }


    @Override
    public void onInit(int status) {

        //It hosts the tts engine
        if(status==TextToSpeech.SUCCESS){

            engine.setLanguage(Locale.UK);
        }

    }


    private void speak() {



        String about=getResources().getString(R.string.tool_scissors);

        engine.speak(about,
                TextToSpeech.QUEUE_ADD,null,null);

    }



    private void first_five() {
        card_scissors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                view= LayoutInflater.from(FirstAid_Kit.this).inflate(R.layout.activity_scissors,null);

                video=view.findViewById(R.id.id_vids);
                TTs=view.findViewById(R.id.id_tts);

                video.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = YouTubeStandalonePlayer.createVideoIntent( FirstAid_Kit.this, YoutubeApiKey_Holder.getApiKey(),"cAw0P9z6A10",100,true,true);
                        startActivity(intent);
                    }
                });

                TTs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        speak();



                    }
                });




                builder.setView(view);
                builder.show();


            }
        });


        card_adhesive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                view= LayoutInflater.from(FirstAid_Kit.this).inflate(R.layout.adhesive_bandage,null);

                video=view.findViewById(R.id.id_adhesive);
                TTs=view.findViewById(R.id.id_tts);

                video.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = YouTubeStandalonePlayer.createVideoIntent( FirstAid_Kit.this, YoutubeApiKey_Holder.getApiKey(),"Edsd79NR3Pg",100,true,true);
                        startActivity(intent);

                    }
                });


                TTs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String about=getResources().getString(R.string.tool_adhesive_bandage);

                        engine.speak(about,
                                TextToSpeech.QUEUE_ADD,null,null);         }
                });

                builder.setView(view);
                builder.show();

            }
        });



        card_cotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                view= LayoutInflater.from(FirstAid_Kit.this).inflate(R.layout.cotton_ball,null);

                video=view.findViewById(R.id.id_vids);
                TTs=view.findViewById(R.id.id_tts);

                video.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = YouTubeStandalonePlayer.createVideoIntent( FirstAid_Kit.this, YoutubeApiKey_Holder.getApiKey(),"Ba6vugDXfuo",100,true,true);
                        startActivity(intent);

                    }
                });


                TTs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        String about=getResources().getString(R.string.tool_cotton);

                        engine.speak(about,
                                TextToSpeech.QUEUE_ADD,null,null);



                    }
                });

                builder.setView(view);
                builder.show();

            }
        });

        card_gauze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                view= LayoutInflater.from(FirstAid_Kit.this).inflate(R.layout.gauze_roll,null);

                video=view.findViewById(R.id.id_vids);
                TTs=view.findViewById(R.id.id_tts);

                video.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = YouTubeStandalonePlayer.createVideoIntent( FirstAid_Kit.this, YoutubeApiKey_Holder.getApiKey(),"TxPx9jjCxOI",100,true,true);
                        startActivity(intent);



                    }
                });


                TTs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String about=getResources().getString(R.string.tool_gauze);

                        engine.speak(about,
                                TextToSpeech.QUEUE_ADD,null,null);



                    }
                });

                builder.setView(view);
                builder.show();

            }
        });


        card_gloves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                view= LayoutInflater.from(FirstAid_Kit.this).inflate(R.layout.gloves,null);

                video=view.findViewById(R.id.id_vids);
                TTs=view.findViewById(R.id.id_tts);

                video.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Intent intent = YouTubeStandalonePlayer.createVideoIntent( FirstAid_Kit.this, YoutubeApiKey_Holder.getApiKey(),"BXGhzaMBco8",100,true,true);
                        startActivity(intent);




                    }
                });


                TTs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String about=getResources().getString(R.string.tool_gloves);

                        engine.speak(about,
                                TextToSpeech.QUEUE_ADD,null,null);



                    }
                });

                builder.setView(view);
                builder.show();

            }
        });



    }


    private void last_five() {

        card_elastic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                view= LayoutInflater.from(FirstAid_Kit.this).inflate(R.layout.elastic_bandage,null);

                video=view.findViewById(R.id.id_vids);
                TTs=view.findViewById(R.id.id_tts);

                video.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = YouTubeStandalonePlayer.createVideoIntent( FirstAid_Kit.this, YoutubeApiKey_Holder.getApiKey(),"PwfBGkBXkFA",100,true,true);
                        startActivity(intent);


                    }
                });

                TTs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String about=getResources().getString(R.string.tool_elastic);

                        engine.speak(about,
                                TextToSpeech.QUEUE_ADD,null,null);

                    }
                });




                builder.setView(view);
                builder.show();


            }
        });


        card_adhesive_tape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                view= LayoutInflater.from(FirstAid_Kit.this).inflate(R.layout.adhesive_tape,null);

                video=view.findViewById(R.id.id_vids);
                TTs=view.findViewById(R.id.id_tts);

                video.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = YouTubeStandalonePlayer.createVideoIntent( FirstAid_Kit.this, YoutubeApiKey_Holder.getApiKey(),"LohYs-U1wEw",100,true,true);
                        startActivity(intent);

                    }
                });

                TTs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String about=getResources().getString(R.string.tool_adhesive_tape);

                        engine.speak(about,
                                TextToSpeech.QUEUE_ADD,null,null);
                    }
                });




                builder.setView(view);
                builder.show();


            }
        });


        card_soap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                view= LayoutInflater.from(FirstAid_Kit.this).inflate(R.layout.soap,null);

                video=view.findViewById(R.id.id_vids);
                TTs=view.findViewById(R.id.id_tts);

                video.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = YouTubeStandalonePlayer.createVideoIntent( FirstAid_Kit.this, YoutubeApiKey_Holder.getApiKey(),"tNf-ZBl-LdE",100,true,true);
                        startActivity(intent);

                    }
                });

                TTs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String about=getResources().getString(R.string.tool_soap);

                        engine.speak(about,
                                TextToSpeech.QUEUE_ADD,null,null); }
                });




                builder.setView(view);
                builder.show();


            }
        });


        card_tweezers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                view= LayoutInflater.from(FirstAid_Kit.this).inflate(R.layout.tweezers,null);

                video=view.findViewById(R.id.id_vids);
                TTs=view.findViewById(R.id.id_tts);

                video.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = YouTubeStandalonePlayer.createVideoIntent( FirstAid_Kit.this, YoutubeApiKey_Holder.getApiKey(),"3E2M4sdIgk8",100,true,true);
                        startActivity(intent);

                    }
                });

                TTs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String about=getResources().getString(R.string.tool_tweezer);

                        engine.speak(about,
                                TextToSpeech.QUEUE_ADD,null,null);}
                });




                builder.setView(view);
                builder.show();


            }
        });

        card_thermometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                view= LayoutInflater.from(FirstAid_Kit.this).inflate(R.layout.thermometer,null);

                video=view.findViewById(R.id.id_vids);
                TTs=view.findViewById(R.id.id_tts);

                video.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = YouTubeStandalonePlayer.createVideoIntent( FirstAid_Kit.this, YoutubeApiKey_Holder.getApiKey(),"YipRpSJ9X6k",100,true,true);
                        startActivity(intent);
                    }
                });

                TTs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String about=getResources().getString(R.string.tool_thermometer);

                        engine.speak(about,
                                TextToSpeech.QUEUE_ADD,null,null);}
                });




                builder.setView(view);
                builder.show();


            }
        });






    }

}
