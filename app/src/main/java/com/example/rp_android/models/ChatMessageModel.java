package com.example.rp_android.models;

import android.text.SpannableString;

public class ChatMessageModel {
    String id;
    int from_id;
    int to_id;
    String body;
    String attechment;
    int seen;
    SpannableString icon;
    String updated_at;
    int type;
    int visibility;
    public ChatMessageModel(){}

    public ChatMessageModel(String id, int from_id, int to_id, String body, String attechment, Integer seen, SpannableString icon, String updated_at, Integer type, int visibility){
        this.id = id;
        this.from_id = from_id;
        this.to_id = to_id;
        this.body = body;
        this.attechment = attechment;
        this.seen = seen;
        this.icon = icon;
        this.updated_at = updated_at;
        this.type = type;
        this.visibility = visibility;
    }

    public String getId() {
        return id;
    }

    public int getFrom_id() {
        return from_id;
    }

    public int getTo_id() {
        return to_id;
    }

    public String getBody() {
        return body;
    }

    public String getAttechment() {
        return attechment;
    }

    public Integer getSeen() {
        return seen;
    }

    public SpannableString getIcon() {
        return icon;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFrom_id(int from_id) {
        this.from_id = from_id;
    }

    public void setTo_id(int to_id) {
        this.to_id = to_id;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setAttechment(String attechment) {
        this.attechment = attechment;
    }

    public void setSeen(Integer seen) {
        this.seen = seen;
    }

    public void setIcon(SpannableString icon) {
        this.icon = icon;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setSeen(int seen) {
        this.seen = seen;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }
}
