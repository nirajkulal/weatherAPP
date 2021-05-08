package com.example.weather.utils;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

import static com.example.weather.utils.Constants.realmName;
import static com.example.weather.utils.Constants.schema;

public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        initRealm();
    }

    /**
     * Init Realm
     */
    private void initRealm() {
        Realm.init(getApplicationContext());
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .schemaVersion(schema)
                .name(realmName)
                .build();
        Realm.setDefaultConfiguration(config);
    }
}
