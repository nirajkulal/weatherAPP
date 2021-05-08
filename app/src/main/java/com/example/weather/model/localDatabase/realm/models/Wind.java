package com.example.weather.model.localDatabase.realm.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Wind extends RealmObject {
    public void setPrimaryID(int primaryID) {
        this.primaryID = primaryID;
    }

    @PrimaryKey
    private int primaryID;

    private Double speed;

    private Double deg;

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Double getDeg() {
        return deg;
    }

    public void setDeg(Double deg) {
        this.deg = deg;
    }

}
