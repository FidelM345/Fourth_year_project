package com.example.thebeast.afyahelp;


import com.example.thebeast.afyahelp.Remote2.ApiClient;
import com.example.thebeast.afyahelp.Remote2.ApiInterface;

public class ClientInterface_Connection {
    private static final String BASE_URL="https://maps.googleapis.com/";

    public static ApiInterface getGoogleApiService(){

        return ApiClient.getClient(BASE_URL).create(ApiInterface.class);
    }



}
