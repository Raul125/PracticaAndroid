package com.raulrh.practicaandroid.ui.weather.data;

import com.google.gson.annotations.SerializedName;

public class WeatherResponse {
    @SerializedName("current")
    private CurrentWeather currentWeather;

    private Hourly hourly;

    public CurrentWeather getCurrentWeather() {
        return currentWeather;
    }

    public Hourly getHourly() {
        return hourly;
    }
}