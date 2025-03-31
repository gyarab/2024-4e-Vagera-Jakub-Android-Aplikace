package com.example.rp_android.models;

import android.graphics.drawable.Drawable;
import android.view.View;

public class OffersModel {
    int id;
    String object;
    String createdBy;

    String mainObject;
    String from;
    String to;
    String shift;
    Drawable commentStatus;
    String comment;
    String created_at;
    String color;
    int visibility;
    int status;
    String buttonText;
    String buttonColor;




    public OffersModel() {

    }
    public OffersModel(int id,String createdBy, String setObject, String from, String to, String shift, Drawable commentStatus, String comment, String created_at, String color, int visibility, int status, String buttonText, String buttonColor) {
        this.id = id;
        this.createdBy = createdBy;
        this.object = setObject;
        this.from = from;
        this.to = to;
        this.shift = shift;
        this.commentStatus = commentStatus;
        this.comment = comment;
        this.created_at = created_at;
        this.color = color;
        this.visibility = visibility;
        this.status = status;
        this.buttonText = buttonText;
        this.buttonColor = buttonColor;

    }

    public void setId(int id) {
        this.id = id;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public void setMainObject(String mainObject) {
        this.mainObject = mainObject;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setCommentStatus(Drawable commentStatus) {
        this.commentStatus = commentStatus;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getShift() {
        return shift;
    }

    public String getColor() {
        return color;
    }

    public String getTo() {
        return to;
    }

    public String getFrom() {
        return from;
    }

    public Drawable getCommentStatus() {
        return commentStatus;
    }

    public String getComment() {
        return comment;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getMainObject() {
        return mainObject;
    }

    public String getObject() {
        return object;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getButtonColor() {
        return buttonColor;
    }

    public void setButtonColor(String buttonColor) {
        this.buttonColor = buttonColor;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public int getId() {
        return id;
    }
}