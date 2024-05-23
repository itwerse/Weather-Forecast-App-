package com.example.weatherapplication.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class WeatherData {
    @SerializedName("sys")
    private Sys sys;
    @SerializedName("main")
    private Main main;
    @SerializedName("name")
    private String name;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @SerializedName ( "list" )

    public List<List> getList() {
        return list;
    }

    public void setList(List<List> list) {
        this.list = list;
    }

    private List<List> list = new ArrayList<List>();



    public Integer getTimezone() {
        return timezone;
    }

    public void setTimezone(Integer timezone) {
        this.timezone = timezone;
    }

    @SerializedName("timezone")
    private Integer timezone;
    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    @SerializedName("wind")
    private Wind wind;
    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    @SerializedName("coord")
    private Coord coord;
    public Sys getSys() {
        return sys;
    }

    public void setSys(Sys sys) {
        this.sys = sys;
    }

        @SerializedName("weather")
    private List<Weather> weather = null;
    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }


    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }}