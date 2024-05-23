package com.example.weatherapplication.Classes;

import java.util.ArrayList;

public class Weather {

    public String weather_icon;
    public String humidity;
    public String rain_descr;
    public String time;
    public String getWeather_icon() {
        return weather_icon;
    }

    public void setWeather_icon(String weather_icon) {
        this.weather_icon = weather_icon;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public void setRain_descr(String rain_descr) {
        this.rain_descr = rain_descr;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }



    public String getRain_descr() {
        return rain_descr;
    }

    public Weather(String weather_icon , String humidity , String rain_descr , String time) {
        this.weather_icon = weather_icon;
        this.humidity = humidity;
        this.rain_descr = rain_descr;
        this.time = time;
    }

}
