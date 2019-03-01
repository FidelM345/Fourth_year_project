package com.example.thebeast.afyahelp.Remote2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {


    private static Retrofit retrofit=null;

    public static Retrofit getClient (String baseurl)
    {

        Gson gson=new GsonBuilder().serializeNulls().create();
        if(retrofit==null){
            retrofit=new Retrofit.Builder()
                    .baseUrl(baseurl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

        }

        return retrofit;
    }
}
