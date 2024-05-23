package com.example.weatherapplication.Model;

import com.google.gson.annotations.SerializedName;

public class Coord {
    @SerializedName  ("lon")
    private Double lon;
    @SerializedName("lat")
    private Double lat;


    @SerializedName("lon")
    public Double getLon() {
        return lon;
    }

    @SerializedName("lon")
    public void setLon(Double lon) {
        this.lon = lon;
    }

    @SerializedName("lat")
    public Double getLat() {
        return lat;
    }

    @SerializedName("lat")
    public void setLat(Double lat) {
        this.lat = lat;
    }



}
