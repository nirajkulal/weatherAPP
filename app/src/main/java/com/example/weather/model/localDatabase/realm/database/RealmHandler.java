package com.example.weather.model.localDatabase.realm.database;

import android.util.Log;

import com.example.weather.model.localDatabase.data.ViewData;
import com.example.weather.model.network.networkModels.WeatherModel;

import io.realm.Realm;

public class RealmHandler {
    /**
     * Store data realm
     * @param body
     */
    public void storeWeatherData(WeatherModel body) {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransactionAsync(realm1 -> {
                com.example.weather.model.localDatabase.realm.models.WeatherModel weatherModel =
                        RealmParser.getParsedModel(body);
                realm1.copyToRealmOrUpdate(weatherModel);
            }, () -> {
            }, error -> {
            });
        }
    }

    /**
     * Fetch from realm
     * @return
     */
    public ViewData fetch() {
        try (Realm realm = Realm.getDefaultInstance()) {
            com.example.weather.model.localDatabase.realm.models.WeatherModel weatherModel =
                    realm.where(com.example.weather.model.localDatabase.realm.models.WeatherModel.class).findFirst();
            return ViewDataParser.getParsedData(weatherModel);
        }
    }
}
