package com.raulrh.practicaandroid.ui.weather.data;

import com.google.gson.annotations.SerializedName;

public class CurrentWeather {
    @SerializedName("temperature_2m")
    private double temperature;

    @SerializedName("relative_humidity_2m")
    private int relativeHumidity;

    @SerializedName("wind_speed_10m")
    private double windSpeed;

    public double getTemperature() {
        return temperature;
    }

    public int getRelativeHumidity() {
        return relativeHumidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }
}