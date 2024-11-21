package com.raulrh.practicaandroid.ui.weather;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.raulrh.practicaandroid.databinding.WeatherFragmentBinding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;

public class WeatherFragment extends Fragment {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private HourlyWeatherAdapter hourlyWeatherAdapter;

    private WeatherFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = WeatherFragmentBinding.inflate(inflater, container, false);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        binding.hourlyRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        hourlyWeatherAdapter = new HourlyWeatherAdapter();
        binding.hourlyRecyclerView.setAdapter(hourlyWeatherAdapter);

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Permiso concedido, obtener la ubicación y mostrar el clima
            getLastLocation();
        }

        return binding.getRoot();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, obtener la ubicación y mostrar el clima
                getLastLocation();
            } else {
                // Permiso denegado, mostrar un mensaje o manejar la situación
                Toast.makeText(requireContext(), "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Task<Location> locationTask = fusedLocationClient.getLastLocation();
        locationTask.addOnSuccessListener(requireActivity(), location -> {
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                fetchWeather(latitude, longitude);
            } else {
                Toast.makeText(requireContext(), "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchWeather(double latitude, double longitude) {
        new Thread(() -> {
            try {
                String startDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                String endDate = startDate; // Same as start date for daily data
                String urlString = String.format(
                        "https://api.open-meteo.com/v1/forecast?latitude=%.2f&longitude=%.2f&current_weather=true&hourly=temperature_2m,precipitation_probability,visibility,windspeed_10m,relativehumidity_2m,weathercode&start_date=%s&end_date=%s",
                        latitude, longitude, startDate, endDate);

                Log.d("URL", urlString);
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    String response = convertStreamToString(inputStream);
                    inputStream.close();

                    Gson gson = new Gson();
                    List<WeatherResponse> weatherResponses = gson.fromJson(response, new TypeToken<List<WeatherResponse>>(){}.getType());

                    new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                        if (weatherResponses != null && !weatherResponses.isEmpty()) {
                            displayWeatherData(weatherResponses.get(0));
                        } else {
                            Toast.makeText(requireContext(), "Error al obtener el clima", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                        Toast.makeText(requireContext(), "Error al obtener el clima", Toast.LENGTH_SHORT).show();
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
                new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                    Toast.makeText(requireContext(), "Error de red", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    private void displayWeatherData(WeatherResponse weatherResponse) {
        CurrentWeather currentWeather = weatherResponse.getCurrentWeather();
        if (currentWeather != null) {
            binding.temperatureTextView.setText(String.format("%.1f °C", currentWeather.getTemperature()));
            binding.humidityTextView.setText(String.format("Humedad: %d %%", currentWeather.getRelativeHumidity()));
            binding.windSpeedTextView.setText(String.format("Viento: %.1f km/h", currentWeather.getWindSpeed()));
        } else {
            Toast.makeText(requireContext(), "No se pudo obtener los datos del clima", Toast.LENGTH_SHORT).show();
        }

        Hourly hourly = weatherResponse.getHourly();
        if (hourly != null) {
            hourlyWeatherAdapter.setHourlyData(hourly);
        }
    }

    static class WeatherResponse {
        @SerializedName("current_weather")
        private CurrentWeather currentWeather;

        @SerializedName("hourly")
        private Hourly hourly;

        public CurrentWeather getCurrentWeather() {
            return currentWeather;
        }

        public Hourly getHourly() {
            return hourly;
        }
    }

    static class CurrentWeather {
        @SerializedName("temperature")
        private double temperature;

        @SerializedName("relativehumidity_2m")
        private int relativeHumidity;

        @SerializedName("windspeed_10m")
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

    static class Hourly {
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
}