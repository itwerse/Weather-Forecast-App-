package com.example.weatherapplication.formatter;


import com.example.weatherapplication.Model.Time;

/**
 * 日出日落标签格式化
 */
public interface SunriseSunsetLabelFormatter {

    String formatSunriseLabel(Time sunrise);

    String formatSunsetLabel(Time sunset);
}
