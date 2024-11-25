package com.raulrh.practicaandroid.ui.weather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.raulrh.practicaandroid.R;
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
    private List<Integer> weathercode;
    private List<Integer> relativeHumidity;

    @NonNull
    @Override
    public HourlyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hourly_weather, parent, false);
        return new HourlyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyViewHolder holder, int position) {
        if (time != null && temperature2m != null && precipitationProbability != null && windSpeed10m != null && weathercode != null && relativeHumidity != null) {
            String timeStr = time.get(position);
            double temperature = temperature2m.get(position);
            int precipitation = precipitationProbability.get(position);
            double windSpeed = windSpeed10m.get(position);
            int code = weathercode.get(position);
            int humidity = relativeHumidity.get(position);

            // Formatear la hora
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            try {
                Date date = inputFormat.parse(timeStr);
                timeStr = outputFormat.format(date);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            holder.timeTextView.setText(timeStr);
            holder.temperatureTextView.setText(String.format(Locale.getDefault(), "%.1f °C", temperature));
            holder.precipitationTextView.setText(String.format(Locale.getDefault(), "Precipitación: %d %%", precipitation));
            holder.windSpeedTextView.setText(String.format(Locale.getDefault(), "Viento: %.1f km/h", windSpeed));
            holder.weatherCodeTextView.setText(getWeatherDescription(code));
            holder.humidityTextView.setText(String.format(Locale.getDefault(), "Humedad: %d %%", humidity));
        }
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
        this.weathercode = hourly.getWeathercode();
        this.relativeHumidity = hourly.getRelativeHumidity();
        notifyDataSetChanged();
    }

    private String getWeatherDescription(int code) {
        return switch (code) {
            case 0 -> "Despejado";
            case 1, 2, 3 -> "Parcialmente nublado";
            case 45, 48 -> "Niebla";
            case 51, 53, 55 -> "Llovizna";
            case 56, 57 -> "Llovizna en gran volumen";
            case 61, 63, 65 -> "Lluvia";
            case 66, 67 -> "Lluvia en gran volumen";
            case 71, 73, 75 -> "Granizo";
            case 77 -> "Granizo en gran volumen";
            case 80, 81, 82 -> "Chubascos";
            case 85, 86 -> "Nieve";
            case 95, 96, 99 -> "Tormenta";
            default -> "Desconocido";
        };
    }

    public static class HourlyViewHolder extends RecyclerView.ViewHolder {
        TextView timeTextView;
        TextView temperatureTextView;
        TextView precipitationTextView;
        TextView windSpeedTextView;
        TextView weatherCodeTextView;
        TextView humidityTextView;

        HourlyViewHolder(View itemView) {
            super(itemView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            temperatureTextView = itemView.findViewById(R.id.temperatureTextView);
            precipitationTextView = itemView.findViewById(R.id.precipitationTextView);
            windSpeedTextView = itemView.findViewById(R.id.windSpeedTextView);
            weatherCodeTextView = itemView.findViewById(R.id.weatherCodeTextView);
            humidityTextView = itemView.findViewById(R.id.humidityTextView);
        }
    }
}