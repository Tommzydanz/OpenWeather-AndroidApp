package com.example.openweather.services;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CityApiService {

    private static final String URL = "https://api.openweathermap.org/";

    private Retrofit retrofit;
    private static CityApiService cityApiService;
    OkHttpClient.Builder okHttPClient;



    private CityApiService(){

        okHttPClient = new OkHttpClient.Builder().readTimeout(500, TimeUnit.MINUTES);
        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized CityApiService getInstance(){
        if(cityApiService == null){
            cityApiService = new CityApiService();
        }

        return cityApiService;
    }

    public CityApiInterface getApi(){
        return retrofit.create(CityApiInterface.class);
    }
}
