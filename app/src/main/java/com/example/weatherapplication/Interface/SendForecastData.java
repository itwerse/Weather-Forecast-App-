package com.example.weatherapplication.Interface;

import com.example.weatherapplication.Model.ForecastData;

import java.util.ArrayList;

import retrofit2.Response;

public interface SendForecastData {
    void sendCityData(Response<ForecastData> forecastDataResponse);
}
