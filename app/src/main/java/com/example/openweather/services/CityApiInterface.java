package com.example.openweather.services;

import com.example.openweather.model.WeatherList;
import com.example.openweather.model.WeatherModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CityApiInterface {

    @GET("/data/2.5/forecast")
    Call<WeatherModel> getList(@Query("id") int id,
                                       @Query("appid") String appid,
                                       @Query("units") String units);
}
