package com.polijecs.petsyapp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


class ApiClient {

    private static final String BASE_URL = "http://192.168.1.4/demo_pets/";
    private static Retrofit retrofit = null;

    public static Retrofit getApiClient(){

        if (retrofit==null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }
        return retrofit;
    }

}
