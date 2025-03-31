package com.example.rp_android.models;

public class ChatFavoriteModel {
    int userId;
    String name;
    String imageURL;

    public ChatFavoriteModel(int userId, String name, String imageURL){
        this.userId = userId;
        this.name = name;
        this.imageURL = imageURL;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getImageURL() {
        return imageURL;
    }
}
