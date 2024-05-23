package com.example.weatherapplication.Database;


import java.util.Locale;


public class City {

    private int cityId;
    private String cityName;
    private String countryCode;
    private float lon;
    private float lat;

    public City() {
    }

    public City( String cityName) {
        this.cityName = cityName;

    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

//    public City getCityName() {
//        return cityName;
//    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(),"%s, %s (%.2f / %.2f)", cityName, countryCode, lat, lon);
    }

    public void setLatitude(float latitude) {
        lat = latitude;
    }

    public float getLatitude() {
        return lat;
    }

    public float getLongitude() {
        return lon;
    }

    public void setLongitude(float lon) {
        this.lon = lon;
    }
}
