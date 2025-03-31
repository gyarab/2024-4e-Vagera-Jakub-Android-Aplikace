package com.example.rp_android.models;

import android.graphics.drawable.Drawable;

public class StatisticModel {
    String date;
    String shift;
    String main;
    String object;
    String scheduled;
    String log;
    Drawable mainIcon;
    Drawable subIcon;

    String logInTime;
    String logOutTime;
    String color;


    public StatisticModel(String date, String shift, String main, String object, String scheduled, String log, Drawable mainIcon, Drawable subIcon, String logInTime, String logOutTime, String color) {
        this.date = date;
        this.shift = shift;
        this.main = main;
        this.object = object;
        this.scheduled = scheduled;
        this.log = log;
        this.mainIcon = mainIcon;
        this.subIcon = subIcon;
        this.logInTime = logInTime;
        this.logOutTime = logOutTime;
        this.color = color;
    }

    public String getDate() {
        return date;
    }

    public String getShift() {
        return shift;
    }

    public String getMain() {
        return main;
    }

    public String getObject() {
        return object;
    }

    public String getScheduled() {
        return scheduled;
    }

    public String getLog() {
        return log;
    }

    public String getLogInTime() {
        return logInTime;
    }

    public String getLogOutTime() {
        return logOutTime;
    }

    public String getColor() {
        return color;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public void setScheduled(String scheduled) {
        this.scheduled = scheduled;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public void setLogInTime(String logInTime) {
        this.logInTime = logInTime;
    }

    public void setLogOutTime(String logOutTime) {
        this.logOutTime = logOutTime;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Drawable getMainIcon() {
        return mainIcon;
    }

    public Drawable getSubIcon() {
        return subIcon;
    }

    public void setMainIcon(Drawable mainIcon) {
        this.mainIcon = mainIcon;
    }

    public void setSubIcon(Drawable subIcon) {
        this.subIcon = subIcon;
    }
}
