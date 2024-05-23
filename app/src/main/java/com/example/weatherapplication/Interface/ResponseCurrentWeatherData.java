package com.example.weatherapplication.Interface;

import com.example.weatherapplication.Model.ForecastData;
import com.example.weatherapplication.Model.WeatherData;

import retrofit2.Response;

public interface ResponseCurrentWeatherData {
     void onSetCurrentWeather(Response<WeatherData> weatherData,String mCity,double lat ,double lng);
  void  onSetForecastData(Response<ForecastData> forecastDataResponse);



}

