package com.example.weather.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;

import com.example.weather.BuildConfig;
import com.example.weather.model.localDatabase.DatabaseFunctions;
import com.example.weather.model.localDatabase.LocalDatabaseFactory;
import com.example.weather.model.network.RetrofitClient;
import com.example.weather.model.network.WeatherService;
import com.example.weather.model.network.networkModels.WeatherModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.common.util.concurrent.ListenableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Worker extends ListenableWorker {

    public Worker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public ListenableFuture<Result> startWork() {
        return CallbackToFutureAdapter.getFuture(this::getCurrentLocation);
    }

    LocationCallback locationCallback;

    private LocationCallback getCurrentLocation(CallbackToFutureAdapter.Completer<Result> completer) {
        FusedLocationProviderClient fusedLocationClient = LocationServices
                .getFusedLocationProviderClient(getApplicationContext());
        LocationRequest locationRequest = getLocReq();
        locationCallback
                = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResultOut) {
                if (locationResultOut == null) {
                    return;

                }
                //only first value considered.
                fusedLocationClient.removeLocationUpdates(this);
                handleLocation(locationResultOut, completer);
            }
        };
        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
        return locationCallback;
    }

    private LocationRequest getLocReq() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(100);
        locationRequest.setSmallestDisplacement(0.01f);
        return locationRequest;
    }

    private void handleLocation(LocationResult locationResultOut, CallbackToFutureAdapter.Completer<Result> completer) {
        WeatherService weatherService = RetrofitClient
                .getRetrofitInstance()
                .create(WeatherService.class);
        Call<WeatherModel> call = weatherService.getWeather(locationResultOut.getLastLocation().getLatitude() + "",
                locationResultOut.getLastLocation().getLongitude() + "",
                BuildConfig.ID);
        call.enqueue(new Callback<WeatherModel>() {
            @Override
            public void onResponse(Call<WeatherModel> call, Response<WeatherModel> response) {
                handleLocalDB(response, completer);
            }

            @Override
            public void onFailure(Call<WeatherModel> call, Throwable t) {
                completer.set(Result.failure());
            }
        });
    }

    private void handleLocalDB(Response<WeatherModel> response, CallbackToFutureAdapter.Completer<Result> completer) {
        DatabaseFunctions localDatabaseHandler = LocalDatabaseFactory.getDatabase();
        localDatabaseHandler.storeData(response.body());
        completer.set(Result.success());
    }

}
