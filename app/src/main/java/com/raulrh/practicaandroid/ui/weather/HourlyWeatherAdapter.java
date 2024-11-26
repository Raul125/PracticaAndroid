package com.raulrh.practicaandroid.ui.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.raulrh.practicaandroid.R;
import com.raulrh.practicaandroid.databinding.ItemHourlyWeatherBinding;
import com.raulrh.practicaandroid.ui.weather.data.Hourly;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HourlyWeatherAdapter extends RecyclerView.Adapter<HourlyWeatherAdapter.HourlyViewHolder> {

    private List<String> time;
    private List<Double> temperature2m;
    private List<Integer> precipitationProbability;
    private List<Double> windSpeed10m;
    private List<Integer> weatherCode;
    private List<Integer> relativeHumidity;

    @NonNull
    @Override
    public HourlyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHourlyWeatherBinding binding = ItemHourlyWeatherBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new HourlyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyViewHolder holder, int position) {
        double temperature = temperature2m.get(position);
        int precipitation = precipitationProbability.get(position);
        double windSpeed = windSpeed10m.get(position);
        int code = weatherCode.get(position);
        int humidity = relativeHumidity.get(position);

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        String timeStr = time.get(position);
        try {
            Date date = inputFormat.parse(timeStr);
            timeStr = outputFormat.format(date);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        holder.bind(temperature, precipitation, windSpeed, code, humidity, timeStr);
    }

    @Override
    public int getItemCount() {
        if (time == null)
            return 0;

        return time.size();
    }

    public void setHourlyData(Hourly hourly) {
        this.time = hourly.getTime();
        this.temperature2m = hourly.getTemperature2m();
        this.precipitationProbability = hourly.getPrecipitationProbability();
        this.windSpeed10m = hourly.getWindSpeed10m();
        this.weatherCode = hourly.getWeathercode();
        this.relativeHumidity = hourly.getRelativeHumidity();
        notifyDataSetChanged();
    }

    public static class HourlyViewHolder extends RecyclerView.ViewHolder {
        private final ItemHourlyWeatherBinding binding;
        private final Context context;

        public HourlyViewHolder(ItemHourlyWeatherBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.context = binding.getRoot().getContext();
        }

        public void bind(double temperature, int precipitation, double windSpeed, int code,
                         int humidity, String timeStr) {
            binding.timeTextView.setText(timeStr);
            binding.temperatureTextView.setText(String.format(Locale.getDefault(), context.getString(R.string.temperature_format), temperature));
            binding.precipitationTextView.setText(String.format(Locale.getDefault(), context.getString(R.string.precipitation_format), precipitation));
            binding.windSpeedTextView.setText(String.format(Locale.getDefault(), context.getString(R.string.wind_speed_format), windSpeed));
            binding.weatherCodeTextView.setText(getWeatherDescription(code));
            binding.humidityTextView.setText(String.format(Locale.getDefault(), context.getString(R.string.humidity_format), humidity));
        }

        private String getWeatherDescription(int code) {
            return switch (code) {
                case 0 -> context.getString(R.string.weather_clear);
                case 1, 2, 3 -> context.getString(R.string.weather_partly_cloudy);
                case 45, 48 -> context.getString(R.string.weather_fog);
                case 51, 53, 55 -> context.getString(R.string.weather_drizzle);
                case 56, 57 -> context.getString(R.string.weather_heavy_drizzle);
                case 61, 63, 65 -> context.getString(R.string.weather_rain);
                case 66, 67 -> context.getString(R.string.weather_heavy_rain);
                case 71, 73, 75 -> context.getString(R.string.weather_snow_grains);
                case 77 -> context.getString(R.string.weather_hail);
                case 80, 81, 82 -> context.getString(R.string.weather_shower);
                case 85, 86 -> context.getString(R.string.weather_snow);
                case 95, 96, 99 -> context.getString(R.string.weather_thunderstorm);
                default -> context.getString(R.string.weather_unknown);
            };
        }
    }
}