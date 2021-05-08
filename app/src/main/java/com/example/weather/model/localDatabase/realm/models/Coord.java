package com.example.weather.model.localDatabase.realm.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Coord extends RealmObject {


    public void setPrimaryID(int primaryID) {
        this.primaryID = primaryID;
    }

    @PrimaryKey
    private int primaryID;

    private Double lon;

    private Double lat;

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

}
