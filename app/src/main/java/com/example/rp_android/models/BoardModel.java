package com.example.rp_android.models;

/**
 * Model pro informacni tabuly
 */
public class BoardModel {
    int id;
    String content;
    String caption;
    String color;
    String creaton;
    String boardURL;
    String profileURL;
    String name;
    int visibility;

    public BoardModel(int id, String content, String caption, String color, String creaton, String boardURL, String profileURL, String name, int visibility) {
        this.id = id;
        this.content = content;
        this.caption = caption;
        this.color = color;
        this.creaton = creaton;
        this.boardURL = boardURL;
        this.profileURL = profileURL;
        this.name = name;
        this.visibility = visibility;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getCaption() {
        return caption;
    }

    public String getColor() {
        return color;
    }

    public String getCreaton() {
        return creaton;
    }

    public String getBoardURL() {
        return boardURL;
    }

    public String getProfileURL() {
        return profileURL;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setCreaton(String creaton) {
        this.creaton = creaton;
    }

    public void setBoardURL(String boardURL) {
        this.boardURL = boardURL;
    }

    public void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }
}
