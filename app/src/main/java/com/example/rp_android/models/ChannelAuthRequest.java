package com.example.rp_android.models;

public class ChannelAuthRequest {
    private String channel_name;
    private String socket_id;

    public ChannelAuthRequest(int id, String channel_name, String socket_id) {
        this.channel_name= channel_name;
        this.socket_id = socket_id;
    }

    // Constructor, getters, setters
}
