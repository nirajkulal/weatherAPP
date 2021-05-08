package com.example.weather.model.localDatabase.realm;

import com.example.weather.model.localDatabase.data.ViewData;
import com.example.weather.model.localDatabase.DatabaseFunctions;
import com.example.weather.model.localDatabase.realm.database.RealmHandler;
import com.example.weather.model.network.networkModels.WeatherModel;

public class RealDatabase implements DatabaseFunctions {

    RealmHandler realmHandler;

    public RealDatabase() {
        realmHandler = new RealmHandler();
    }

    @Override
    public void storeData(WeatherModel body) {
        realmHandler.storeWeatherData(body);
    }

    @Override
    public ViewData fetch() {
        return realmHandler.fetch();
    }
}
