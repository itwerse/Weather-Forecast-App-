package com.example.weatherapplication.Interface;

import com.example.weatherapplication.Model.ForecastData;

import retrofit2.Response;

public interface SendData {
    void sendCityName(double latitude, double longitude);

}