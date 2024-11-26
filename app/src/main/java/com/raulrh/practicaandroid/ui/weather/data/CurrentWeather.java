package com.raulrh.practicaandroid.ui.weather.data;

import com.google.gson.annotations.SerializedName;

public class CurrentWeather {
    @SerializedName("temperature_2m")
    private double temperature;

    @SerializedName("relative_humidity_2m")
    private int relativeHumidity;

    @SerializedName("wind_speed_10m")
    private double windSpeed;

    @SerializedName("weather_code")
    private int weatherCode;

    public double getTemperature() {
        return temperature;
    }

    public int getRelativeHumidity() {
        return relativeHumidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public int getWeatherCode() {
        return weatherCode;
    }
}