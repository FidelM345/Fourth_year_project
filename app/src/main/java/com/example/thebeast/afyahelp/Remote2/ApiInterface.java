package com.example.thebeast.afyahelp.Remote2;

import com.example.thebeast.afyahelp.Model.Myplaces;
import com.example.thebeast.afyahelp.Model2.MyPojo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ApiInterface {

    @GET
    Call<MyPojo> getPlaceDetails(@Url String url);

}
