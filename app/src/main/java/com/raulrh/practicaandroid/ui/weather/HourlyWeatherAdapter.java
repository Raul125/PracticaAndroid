package com.raulrh.practicaandroid.ui.weather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.raulrh.practicaandroid.R;

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
                e.printStackTrace();
            }

            holder.timeTextView.setText(timeStr);
            holder.temperatureTextView.setText(String.format("%.1f °C", temperature));
            holder.precipitationTextView.setText(String.format("%d %%", precipitation));
            holder.windSpeedTextView.setText(String.format("%.1f km/h", windSpeed));
            holder.weatherCodeTextView.setText(getWeatherDescription(code));
            holder.humidityTextView.setText(String.format("Humedad: %d %%", humidity));
        }
    }

    @Override
    public int getItemCount() {
        if (time == null) return 0;
        return time.size();
    }

    public void setHourlyData(WeatherFragment.Hourly hourly) {
        this.time = hourly.getTime();
        this.temperature2m = hourly.getTemperature2m();
        this.precipitationProbability = hourly.getPrecipitationProbability();
        this.windSpeed10m = hourly.getWindSpeed10m();
        this.weathercode = hourly.getWeathercode();
        this.relativeHumidity = hourly.getRelativeHumidity();
        notifyDataSetChanged();
    }

    private String getWeatherDescription(int code) {
        switch (code) {
            case 0:
                return "Despejado";
            case 1:
            case 2:
            case 3:
                return "Parcialmente nublado";
            case 45:
            case 48:
                return "Niebla";
            case 51:
            case 53:
            case 55:
                return "Llovizna";
            case 56:
            case 57:
                return "Llovizna en gran volumen";
            case 61:
            case 63:
            case 65:
                return "Lluvia";
            case 66:
            case 67:
                return "Lluvia en gran volumen";
            case 71:
            case 73:
            case 75:
                return "Granizo";
            case 77:
                return "Granizo en gran volumen";
            case 80:
            case 81:
            case 82:
                return "Chubascos";
            case 85:
            case 86:
                return "Nieve";
            case 95:
            case 96:
            case 99:
                return "Tormenta";
            default:
                return "Desconocido";
        }
    }

    static class HourlyViewHolder extends RecyclerView.ViewHolder {
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