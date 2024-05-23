package com.example.weatherapplication.Modelss;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherResponse {
    @SerializedName("dt")
    private long dt;
    @SerializedName("sunrise")
    private long sunrise;
    @SerializedName("sunset")
    private long sunset;
    @SerializedName("temp")
    private TemperatureData temperatureData;
    @SerializedName("feels_like")
    private FeelsLikeData feelsLikeData;
    @SerializedName("pressure")
    private int pressure;
    @SerializedName("humidity")
    private int humidity;
    @SerializedName("weather")
    private List<WeatherDataFor> weatherData;
    @SerializedName("speed")
    private double speed;
    @SerializedName("deg")
    private int deg;
    @SerializedName("gust")
    private double gust;
    @SerializedName("clouds")
    private int clouds;
    @SerializedName("pop")
    private double pop;

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public long getSunrise() {
        return sunrise;
    }

    public void setSunrise(long sunrise) {
        this.sunrise = sunrise;
    }

    public long getSunset() {
        return sunset;
    }

    public void setSunset(long sunset) {
        this.sunset = sunset;
    }

    public TemperatureData getTemperatureData() {
        return temperatureData;
    }

    public void setTemperatureData(TemperatureData temperatureData) {
        this.temperatureData = temperatureData;
    }

    public FeelsLikeData getFeelsLikeData() {
        return feelsLikeData;
    }

    public void setFeelsLikeData(FeelsLikeData feelsLikeData) {
        this.feelsLikeData = feelsLikeData;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public List<WeatherDataFor> getWeatherData() {
        return weatherData;
    }

    public void setWeatherData(List<WeatherDataFor> weatherData) {
        this.weatherData = weatherData;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getDeg() {
        return deg;
    }

    public void setDeg(int deg) {
        this.deg = deg;
    }

    public double getGust() {
        return gust;
    }

    public void setGust(double gust) {
        this.gust = gust;
    }

    public int getClouds() {
        return clouds;
    }

    public void setClouds(int clouds) {
        this.clouds = clouds;
    }

    public double getPop() {
        return pop;
    }

    public void setPop(double pop) {
        this.pop = pop;
    }
// Getters and Setters
}
