package com.example.weather.model.network;

import com.example.weather.model.network.networkModels.WeatherModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WeatherService {
    @GET("/data/2.5/weather")
    Call<WeatherModel> getWeather(@Query("lat") String lat,
                                  @Query("lon") String lon,
                                  @Query("appid") String appid);
}
