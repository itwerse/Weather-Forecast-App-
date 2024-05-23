package com.example.weatherapplication.Modelss;

public class WeatherDataFor {
    private String dt;
    private double longDayTemp;
    private double longNightTemp;
    private float rainfall;
    private String weatherIcon;

    public WeatherDataFor(String dt, double longDayTemp, double longNightTemp, float rainfall, String weatherIcon) {
        this.dt = dt;
        this.longDayTemp = longDayTemp;
        this.longNightTemp = longNightTemp;
        this.rainfall = rainfall;
        this.weatherIcon = weatherIcon;
    }

    // Getters and setters
    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public double getLongDayTemp() {
        return longDayTemp;
    }

    public void setLongDayTemp(double longDayTemp) {
        this.longDayTemp = longDayTemp;
    }

    public double getLongNightTemp() {
        return longNightTemp;
    }

    public void setLongNightTemp(double longNightTemp) {
        this.longNightTemp = longNightTemp;
    }

    public float getRainfall() {
        return rainfall;
    }

    public void setRainfall(float rainfall) {
        this.rainfall = rainfall;
    }

    public String getWeatherIcon() {
        return weatherIcon;
    }

    public void setWeatherIcon(String weatherIcon) {
        this.weatherIcon = weatherIcon;
    }
}
