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

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.raulrh.practicaandroid.ui.news.data.Response;
import com.raulrh.practicaandroid.ui.weather.data.CurrentWeather;
import com.raulrh.practicaandroid.ui.weather.data.Hourly;
import com.raulrh.practicaandroid.ui.weather.data.WeatherResponse;

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
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        } else {
            getLastLocation();
        }

        return binding.getRoot();
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            result -> {
                if (result) {
                    getLastLocation();
                } else {
                    Toast.makeText(requireContext(), "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
                }
            }
    );

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
                String urlString = String.format(
                        Locale.US,
                        "https://api.open-meteo.com/v1/forecast?latitude=%.2f&longitude=%.2f&current=temperature_2m,relative_humidity_2m,wind_speed_10m&hourly=temperature_2m,precipitation_probability,windspeed_10m,relativehumidity_2m,weathercode&forecast_days=1",
                        latitude, longitude);

                Log.d("URL", urlString);
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream responseStream = connection.getInputStream();
                    InputStreamReader reader = new InputStreamReader(responseStream);

                    Gson gson = new Gson();
                    WeatherResponse weatherResponse = gson.fromJson(reader, WeatherResponse.class);

                    reader.close();
                    responseStream.close();
                    new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                        if (weatherResponse != null) {
                            displayWeatherData(weatherResponse);
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
                new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                    Toast.makeText(requireContext(), "Error de red", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    private void displayWeatherData(WeatherResponse weatherResponse) {
        CurrentWeather currentWeather = weatherResponse.getCurrentWeather();
        if (currentWeather != null) {
            binding.temperatureTextView.setText(String.format(Locale.getDefault(), "%.1f °C", currentWeather.getTemperature()));
            binding.humidityTextView.setText(String.format(Locale.getDefault(), "Humedad: %d %%", currentWeather.getRelativeHumidity()));
            binding.windSpeedTextView.setText(String.format(Locale.getDefault(), "Viento: %.1f km/h", currentWeather.getWindSpeed()));
        } else {
            Toast.makeText(requireContext(), "No se pudo obtener los datos del clima", Toast.LENGTH_SHORT).show();
        }

        Hourly hourly = weatherResponse.getHourly();
        if (hourly != null) {
            hourlyWeatherAdapter.setHourlyData(hourly);
        }

        binding.loadingLayout.setVisibility(View.GONE);
        binding.dataLayout.setVisibility(View.VISIBLE);
    }
}