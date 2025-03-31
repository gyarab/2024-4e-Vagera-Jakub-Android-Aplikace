package com.example.rp_android.models;

import android.graphics.drawable.Drawable;

public class ORGModel {
    int id;
    String from;
    String to;
    String logFrom;
    String logTo;
    String name;
    String color;
    String shift;
    String image;
    String status;

    Drawable icon;

    public ORGModel(int id, String from, String to, String logFrom, String logTo, String name, String color, String shift, String image, String status, Drawable icon) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.logFrom = logFrom;
        this.logTo = logTo;
        this.name = name;
        this.color = color;
        this.shift = shift;
        this.image = image;
        this.status = status;
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getLogFrom() {
        return logFrom;
    }

    public String getLogTo() {
        return logTo;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public String getShift() {
        return shift;
    }

    public String getImage() {
        return image;
    }

    public String getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setLogFrom(String logFrom) {
        this.logFrom = logFrom;
    }

    public void setLogTo(String logTo) {
        this.logTo = logTo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
}
