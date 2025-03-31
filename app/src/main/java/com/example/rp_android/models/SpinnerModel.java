package com.example.rp_android.models;

import android.graphics.drawable.Drawable;

public class SpinnerModel {
    private int id;
    private String name;
    private Drawable icon;


    public SpinnerModel(int id, String name, Drawable icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    // Override toString() so the Spinner shows the name
    @Override
    public String toString() {
        return name;
    }
}

