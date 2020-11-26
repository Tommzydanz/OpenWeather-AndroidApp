package com.example.openweather.model;

import com.google.gson.annotations.SerializedName;

public class Weather {

    @SerializedName("id")
    private int weather_id;

    @SerializedName("description")
    private String cloud_desc;

    @SerializedName("icon")
    private String icon;


    public int getWeather_id() {
        return weather_id;
    }

    public void setWeather_id(int weather_id) {
        this.weather_id = weather_id;
    }

    public String getCloud_desc() {
        return cloud_desc;
    }

    public void setCloud_desc(String cloud_desc) {
        this.cloud_desc = cloud_desc;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }


}
