package com.example.weatherapplication.Model;

import com.google.gson.annotations.SerializedName;

public class Main {


    public float getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(float temp_max) {
        this.temp_max = temp_max;
    }

    public float getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(float temp_min) {
        this.temp_min = temp_min;
    }

    @SerializedName("temp_max")
    float temp_max;
    @SerializedName("temp_min")
    float temp_min;

    @SerializedName("temp")
    float temp;

    @SerializedName("humidity")
    String humidity;

    @SerializedName("feels_like")
    String feels_like;

    public Integer getPressure() {
        return pressure;
    }

    public void setPressure(Integer pressure) {
        this.pressure = pressure;
    }

    @SerializedName("pressure")
    private Integer pressure;

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getFeels_like() {
        return feels_like;
    }

    public void setFeels_like(String feels_like) {
        this.feels_like = feels_like;
    }

}
