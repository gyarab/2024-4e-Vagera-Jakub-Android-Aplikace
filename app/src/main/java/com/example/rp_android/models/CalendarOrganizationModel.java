package com.example.rp_android.models;

public class CalendarOrganizationModel {
    private Integer id;
    private String from;
    private String to;
    private String color;
    private String shift;
    private String name;
    private String imageURL;

    public CalendarOrganizationModel(Integer id, String from, String to, String color, String shift, String name, String imageURL ){
        this.id = id;
        this.from = from;
        this.to = to;
        this.color = color;
        this.shift = shift;
        this.name = name;
        this.imageURL = imageURL;
    }

    public CalendarOrganizationModel() {

    }

    public Integer getId() {
        return id;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getColor() {
        return color;
    }

    public String getShift() {
        return shift;
    }

    public String getName() {
        return name;
    }

    public String getImageURL() {
        return imageURL;
    }


    public void setId(Integer id) {
        this.id = id;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }


}
