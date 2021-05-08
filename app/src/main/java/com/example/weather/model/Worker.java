package com.example.weather.model;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
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
        return CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver<Result>() {
            @Nullable
            @Override
            public Object attachCompleter(@NonNull CallbackToFutureAdapter.Completer<Result> completer) throws Exception {
                FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
                OnSuccessListener<Location> onSuccessListener = new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location == null) {
                            Log.v("WorkerLocation", "location retry");
                            completer.set(Result.retry());
                            return;
                        }
                        Log.v("WorkerLocation", "location" + location.toString());
                        WeatherService weatherService = RetrofitClient
                                .getRetrofitInstance()
                                .create(WeatherService.class);
                        Call<WeatherModel> call = weatherService.getWeather(location.getLatitude() + "",
                                location.getLongitude() + "",
                                BuildConfig.ID);
                        call.enqueue(new Callback<WeatherModel>() {
                            @Override
                            public void onResponse(Call<WeatherModel> call, Response<WeatherModel> response) {
                                DatabaseFunctions localDatabaseHandler = LocalDatabaseFactory.getDatabase();
                                localDatabaseHandler.storeData(response.body());
                                completer.set(Result.success());
                            }

                            @Override
                            public void onFailure(Call<WeatherModel> call, Throwable t) {
                                completer.set(Result.failure());
                            }
                        });
                    }
                };
                fusedLocationClient.getLastLocation().addOnSuccessListener(onSuccessListener);
                return onSuccessListener;
            }
        });
    }
}
