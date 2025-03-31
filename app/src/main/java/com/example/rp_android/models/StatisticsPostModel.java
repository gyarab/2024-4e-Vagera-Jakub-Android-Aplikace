package com.example.rp_android.models;

public class StatisticsPostModel {
    int id;
    int month;
    int year;

    public StatisticsPostModel(int id, int month, int year) {
        this.id = id;
        this.month = month;
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
