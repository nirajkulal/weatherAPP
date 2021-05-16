package com.example.weather.model.localDatabase;

import com.example.weather.model.localDatabase.realm.RealDatabase;

public class LocalDatabaseFactory {
    public static DatabaseFunctions getDatabase() {
        return new RealDatabase();
    }
}
