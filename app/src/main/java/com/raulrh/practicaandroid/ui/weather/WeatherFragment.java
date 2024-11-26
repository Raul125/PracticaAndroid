package com.raulrh.practicaandroid.ui.weather;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.raulrh.practicaandroid.R;
import com.raulrh.practicaandroid.databinding.WeatherFragmentBinding;
import com.raulrh.practicaandroid.ui.weather.data.CurrentWeather;
import com.raulrh.practicaandroid.ui.weather.data.Hourly;
import com.raulrh.practicaandroid.ui.weather.data.WeatherResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WeatherFragment extends Fragment {

    private static final String API_URL = "https://api.open-meteo.com/v1/forecast?latitude=%.2f&longitude=%.2f&current=temperature_2m,weather_code,relative_humidity_2m,wind_speed_10m&hourly=temperature_2m,precipitation_probability,windspeed_10m,relativehumidity_2m,weathercode&forecast_days=1";

    private FusedLocationProviderClient fusedLocationClient;
    private HourlyWeatherAdapter hourlyWeatherAdapter;

    private WeatherFragmentBinding binding;

    private ExecutorService executorService;
    private Handler mainHandler;
    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            result -> {
                if (result) {
                    getLastLocation();
                } else {
                    Toast.makeText(requireContext(), getString(R.string.no_location_permission), Toast.LENGTH_SHORT).show();
                }
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = WeatherFragmentBinding.inflate(inflater, container, false);

        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());

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
                Toast.makeText(requireContext(), getString(R.string.no_location), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchWeather(double latitude, double longitude) {
        executorService.execute(() -> {
            try {
                String urlString = String.format(Locale.US, API_URL, latitude, longitude);
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
                    mainHandler.post(() -> {
                        displayWeatherData(weatherResponse);
                    });
                }
            } catch (IOException e) {
                mainHandler.post(() -> Toast.makeText(requireContext(), getString(R.string.network_error), Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void displayWeatherData(WeatherResponse weatherResponse) {
        CurrentWeather currentWeather = weatherResponse.getCurrentWeather();
        binding.temperatureTextView.setText(String.format(Locale.getDefault(), getString(R.string.temperature_format), currentWeather.getTemperature()));
        binding.humidityTextView.setText(String.format(Locale.getDefault(), getString(R.string.humidity_format), currentWeather.getRelativeHumidity()));
        binding.windSpeedTextView.setText(String.format(Locale.getDefault(), getString(R.string.wind_speed_format), currentWeather.getWindSpeed()));
        binding.weatherImage.setImageResource(getWeatherIcon(currentWeather.getWeatherCode()));

        Hourly hourly = weatherResponse.getHourly();
        if (hourly != null) {
            hourlyWeatherAdapter.setHourlyData(hourly);
        }

        binding.loadingLayout.setVisibility(View.GONE);
        binding.dataLayout.setVisibility(View.VISIBLE);
    }

    private int getWeatherIcon(int code) {
        return switch (code) {
            case 1, 2, 3 -> R.drawable.cloudy_day;
            case 45, 48 -> R.drawable.fog;
            case 56, 57, 51, 53, 55 -> R.drawable.haze;
            case 61, 63, 65, 80, 81, 82 -> R.drawable.rainy_1;
            case 66, 67 -> R.drawable.rainy_2;
            case 71, 73, 75, 77 -> R.drawable.rain_and_sleet_mix;
            case 85, 86 -> R.drawable.snowy_3;
            case 95, 96, 99 -> R.drawable.thunderstorms;
            default -> R.drawable.clear_day;
        };
    }
}