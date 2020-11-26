package com.example.openweather.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Main {


    @SerializedName("temp")
    @Expose
    private double temp;

    @SerializedName("feels_like")
    @Expose
    private double feels_like;
    @SerializedName("temp_min")
    @Expose
    private double temp_min;

    @SerializedName("temp_max")
    @Expose
    private double temp_max;

    @SerializedName("humidity")
    @Expose
    private int humidity;

    public double getTemp() {
        return (int) temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getFeels_like() {
        return (int) feels_like;
    }

    public void setFeels_like(double feels_like) {
        this.feels_like = feels_like;
    }

    public double getTemp_min() {
        return (int)temp_min;
    }

    public double getTemp_max() {
        return (int) temp_max;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

}
