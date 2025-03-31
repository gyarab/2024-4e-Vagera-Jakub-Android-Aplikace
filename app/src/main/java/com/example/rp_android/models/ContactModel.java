package com.example.rp_android.models;

public class ContactModel {
    int id;

    String who;
    String imageURL;
    String name;
    String time;
    String message;
    int counter;
    int visibility;


    public ContactModel(int id, String who, String imageURL, String name, String time, String message, int counter, int visibility) {
        this.id = id;
        this.who = who;
        this.imageURL = imageURL;
        this.name = name;
        this.time = time;
        this.message = message;
        this.counter = counter;
        this.visibility = visibility;
    }

    public int getId() {
        return id;
    }

    public String getWho() {
        return who;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getMessage() {
        return message;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCounter() {
        return counter;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }
}
