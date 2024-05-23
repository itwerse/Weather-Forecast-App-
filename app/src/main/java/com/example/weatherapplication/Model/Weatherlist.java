package com.example.weatherapplication.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Weatherlist {
    @SerializedName  ("list")
    private List<Weather> weathers;
    public List<Weather> getWeathers() {
        return weathers;
    }

    public void setWeathers(List<Weather> weathers) {
        this.weathers = weathers;
    }


}
