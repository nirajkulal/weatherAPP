package com.example.weather.model.localDatabase;

import com.example.weather.model.localDatabase.data.ViewData;
import com.example.weather.model.network.networkModels.WeatherModel;

public interface DatabaseFunctions {

    void storeData(WeatherModel body);

    ViewData fetch();

}
