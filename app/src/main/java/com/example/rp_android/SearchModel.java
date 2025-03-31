package com.example.rp_android;

public class SearchModel {
     String search;
     int admin;
     int manager;
     int fullTime;
     int partTime;

    public SearchModel(String search, int admin, int manager, int fullTime, int partTime) {
        this.search = search;
        this.admin = admin;
        this.manager = manager;
        this.fullTime = fullTime;
        this.partTime = partTime;
    }

}
