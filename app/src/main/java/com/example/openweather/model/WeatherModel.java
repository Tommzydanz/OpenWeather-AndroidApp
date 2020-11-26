package com.example.openweather.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import kotlin.ExperimentalStdlibApi;

public class WeatherModel {

    @SerializedName("list")
    @Expose
    private List<WeatherList> list;

    @SerializedName("city")
    @Expose
    private City city;

    public List<WeatherList> getList() {
        return list;
    }

    public void setList(List<WeatherList> list) {
        this.list = list;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }



}
