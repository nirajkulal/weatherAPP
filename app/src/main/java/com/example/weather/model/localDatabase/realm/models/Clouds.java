package com.example.weather.model.localDatabase.realm.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Clouds extends RealmObject {


    private Integer all;

    public Integer getAll() {
        return all;
    }

    public void setAll(Integer all) {
        this.all = all;
    }


    public void setPrimaryID(int primaryID) {
        this.primaryID = primaryID;
    }

    @PrimaryKey
    private int primaryID;

}
