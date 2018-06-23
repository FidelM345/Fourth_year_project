package com.example.thebeast.afyahelp.Remote;

import com.example.thebeast.afyahelp.Model.Myplaces;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface GoogleApiService {

    @GET
    Call<Myplaces>getNearByPlaces(@Url String url);

}
