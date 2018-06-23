package com.example.thebeast.afyahelp;

import com.example.thebeast.afyahelp.Remote.GoogleApiService;
import com.example.thebeast.afyahelp.Remote.RetrofitClient;

public class Common {
    private static final String GOOGLE_API_URL="https://maps.googleapis.com/";

    public static GoogleApiService getGoogleApiService(){

        return RetrofitClient.getClient(GOOGLE_API_URL).create(GoogleApiService.class);
    }


}
