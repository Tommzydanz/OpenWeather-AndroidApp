package com.example.openweather.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherList {



    @SerializedName("dt")
    @Expose
    private long dt;

    @SerializedName("main")
    @Expose
    private Main mainList;

    @SerializedName("weather")
    @Expose
    private List<Weather> weather;

    @SerializedName("wind")
    @Expose
    private Wind wind;


    public WeatherList(Main mainList, List<Weather> weather, Wind wind) {
        this.mainList = mainList;
        this.weather = weather;
        this.wind = wind;
    }

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public Main getMainList() {
        return mainList;
    }

    public void setMainList(Main mainList) {
        this.mainList = mainList;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }





}
