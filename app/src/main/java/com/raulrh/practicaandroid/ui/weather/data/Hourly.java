package com.raulrh.practicaandroid.ui.weather.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Hourly {
    @SerializedName("time")
    private List<String> time;

    @SerializedName("temperature_2m")
    private List<Double> temperature2m;

    @SerializedName("precipitation_probability")
    private List<Integer> precipitationProbability;

    @SerializedName("windspeed_10m")
    private List<Double> windSpeed10m;

    @SerializedName("weathercode")
    private List<Integer> weathercode;

    @SerializedName("relativehumidity_2m")
    private List<Integer> relativeHumidity;

    public List<String> getTime() {
        return time;
    }

    public List<Double> getTemperature2m() {
        return temperature2m;
    }

    public List<Integer> getPrecipitationProbability() {
        return precipitationProbability;
    }

    public List<Double> getWindSpeed10m() {
        return windSpeed10m;
    }

    public List<Integer> getWeathercode() {
        return weathercode;
    }

    public List<Integer> getRelativeHumidity() {
        return relativeHumidity;
    }
}