package com.example.weather;


import android.os.Build;

import com.example.weather.model.network.RetrofitClient;
import com.example.weather.model.network.WeatherService;
import com.example.weather.model.network.networkModels.WeatherModel;
import com.google.gson.Gson;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.annotation.Config;

import java.io.IOException;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import retrofit2.Call;
import retrofit2.Response;

public class NetworkUnitTests {

    @Test
    public void checkAPIRequestNotNull() throws IOException {
        WeatherService weatherService = RetrofitClient
                .getRetrofitInstance()
                .create(WeatherService.class);
        Call<WeatherModel> call = weatherService.getWeather("35",
                "139",
                BuildConfig.ID);
        Response<WeatherModel> response = call.execute();
        Assert.assertNotNull(response.body());
    }

    @Test
    public void checkAPIResponseHasData() throws IOException {
        WeatherService weatherService = RetrofitClient
                .getRetrofitInstance()
                .create(WeatherService.class);
        Call<WeatherModel> call = weatherService.getWeather("35",
                "139",
                BuildConfig.ID);
        Response<WeatherModel> response = call.execute();

        Assert.assertNotNull(response.body().getName());
        Assert.assertNotNull(response.body().getMain());
    }

    @Test
    public void checkAPIResponseDataExpected() throws IOException {
        WeatherService weatherService = RetrofitClient
                .getRetrofitInstance()
                .create(WeatherService.class);
        Call<WeatherModel> call = weatherService.getWeather("35",
                "139",
                BuildConfig.ID);
        Response<WeatherModel> response = call.execute();
        StringBuilder stringBuilder = FileReader.getInstance().readFile("resposesucces.json");
        String out = stringBuilder.toString();
        WeatherModel weatherModel = new Gson().fromJson(out, WeatherModel.class);
        Assert.assertEquals(response.body().getName(), weatherModel.getName());
    }

    @Test
    public void checkAPIParsedAsExpected() throws IOException {
        WeatherService weatherService = RetrofitClient
                .getRetrofitInstance()
                .create(WeatherService.class);
        Call<WeatherModel> call = weatherService.getWeather("35",
                "139",
                BuildConfig.ID);
        Response<WeatherModel> response = call.execute();
        StringBuilder stringBuilder = FileReader.getInstance().readFile("resposesucces.json");
        String out = stringBuilder.toString();
        WeatherModel weatherModel = new Gson().fromJson(out, WeatherModel.class);
        Assert.assertNotNull(weatherModel.getName());
    }

}
