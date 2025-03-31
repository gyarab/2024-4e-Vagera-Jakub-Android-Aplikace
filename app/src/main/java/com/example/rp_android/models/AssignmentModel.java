package com.example.rp_android.models;

import android.graphics.drawable.Drawable;

/**
 * Model pro pridelene smeny
 */
public class AssignmentModel {
    int id;
    String color;
    String name;
    Drawable icon;

    public AssignmentModel(int id, String name,String color, Drawable icon) {
        this.id = id;
        this.color = color;
        this.name = name;
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public String getColor() {
        return color;
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

    public void setColor(String color) {
        this.color = color;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
}
