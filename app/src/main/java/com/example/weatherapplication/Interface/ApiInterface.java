package com.example.weatherapplication.Interface;

import com.example.weatherapplication.Model.ForecastData;
import com.example.weatherapplication.Model.WeatherData;
import com.example.weatherapplication.Modelss.WeatherDataFor;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
public interface ApiInterface {
    //Get  Weather By City name
    @GET ("weather?APPID=97d2333e744ccbbfd2fbb8dd808b2d20&units=metric")
    Call<WeatherData> getWeatherData( @Query ("q") String name);
    @GET ("forecast?APPID=97d2333e744ccbbfd2fbb8dd808b2d20&units=metric&lang=en")
    Call<ForecastData> getForecast_Data(@Query ("q") String name);
    @GET ("weather?APPID=97d2333e744ccbbfd2fbb8dd808b2d20&units=metric")
    Call<WeatherData> getWeatherByLatLng(@Query ("lat") double lat, @Query ( "lon" ) double lng);
    @GET ("forecast?APPID=97d2333e744ccbbfd2fbb8dd808b2d20&units=metric")
    Call<ForecastData> forecast_DataByLatLng(@Query ("lat") double lat,@Query ( "lon" ) double lng);
    @GET ("forecast/daily?&cnt=16&appid=97d2333e744ccbbfd2fbb8dd808b2d20&units=metric")
    Call<JsonObject> forecastDataByLatLng(@Query ("lat") double lat,@Query ( "lon" ) double lng);
    @GET("forecast/daily?&cnt=16&appid=97d2333e744ccbbfd2fbb8dd808b2d20&units=metric")
    Call<WeatherDataFor> getWeatherForecast(@Query("q") String cityName);


}