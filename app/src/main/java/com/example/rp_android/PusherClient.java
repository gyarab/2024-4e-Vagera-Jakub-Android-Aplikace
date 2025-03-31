package com.example.rp_android;

import android.util.Log;

import com.example.rp_android.connection.ConnectionFile;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.PrivateChannel;
import com.pusher.client.channel.PrivateChannelEventListener;
import com.pusher.client.channel.PusherEvent;
import com.pusher.client.util.HttpAuthorizer;

import okhttp3.*;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PusherClient {

    private static final String PUSHER_KEY = "372c7499d55d1cd7cd8b";
    private static final String PUSHER_CLUSTER = "eu";
    private static final String BASE_URL = ConnectionFile.returnURLRaw();
    private static final String AUTH_ENDPOINT = BASE_URL + "/api/chatt";
    private Pusher pusher;
    private String token; // Sanctum Token

    public PusherClient(String token) {
        this.token = token;
        initPusher();
    }

    private void initPusher() {
        HttpAuthorizer authorizer = new HttpAuthorizer(ConnectionFile.returnURL()+"api/chatt");
        PusherOptions options = new PusherOptions();
        options.setCluster(PUSHER_CLUSTER);
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
        headers.put("Accept", "application/json");
        authorizer.setHeaders(headers);
        options.setAuthorizer(authorizer);
        options.setAuthorizer((channelName, socketId) -> {
            try {
                Log.e("ee", "ee");

                return authenticateChannel("private-chatify.3", "13456.982346");
            } catch (IOException e) {
                Log.e("ee", String.valueOf(e));

                e.printStackTrace();

                return null;
            } catch (JSONException e) {
                Log.e("ee", String.valueOf(e));

                throw new RuntimeException(e);

            }
        });
        pusher = new Pusher(PUSHER_KEY, options);
        Log.e("uu", "uu");
    }

    private String authenticateChannel(String channelName, String socketId) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("channel_name", channelName);
        jsonBody.put("socket_id", socketId);

        RequestBody body = RequestBody.create(MediaType.get("application/json; charset=utf-8"), jsonBody.toString());

        Request request = new Request.Builder()
                .url(AUTH_ENDPOINT)
                .addHeader("Authorization", "Bearer " + token)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            assert response.body() != null;
            return response.body().string();
        }
    }

    public void subscribeToChannel(String channelName) {
        PrivateChannel channel = pusher.subscribePrivate("private-chatify.3", new PrivateChannelEventListener() {
            @Override
            public void onEvent(PusherEvent event) {
                Log.e("uu", "onEvent");

            }

            @Override
            public void onError(String message, Exception e) {
                Log.e("uu", "onError");

                PrivateChannelEventListener.super.onError(message, e);
            }

            @Override
            public void onSubscriptionSucceeded(String s) {
                //bindToEvents();
                Log.e("Success", "success");
            }
            @Override
            public void onAuthenticationFailure(String s, Exception e) {
                Log.e("Auth", String.valueOf(e));

            }

        });
        Log.e("ain", "main_error");

        channel.bind("messaging", new PrivateChannelEventListener() {
            /*@Override
            public void onEvent(PusherEvent event) {

            }*/

            @Override
            public void onEvent(PusherEvent event) {
                Log.e("ain", "main_error");
            }

            @Override
            public void onError(String message, Exception e) {
                //PrivateChannelEventListener.super.onError(message, e);
                Log.e("ain", "main_error2");

            }

            /*@Override
            public void onEvent(String channelName, String eventName, String data, PusherEvent event) {
                Log.d("Pusher", "Message received: " + data);
            }*/

            @Override
            public void onSubscriptionSucceeded(String channelName) {
                Log.d("Pusher", "Subscribed to " + channelName);
            }

            @Override
            public void onAuthenticationFailure(String message, Exception e) {
                Log.e("Pusher", "Auth failed: " + message);
                e.printStackTrace();
            }
        });
        pusher.connect();

        /*PrivateChannel channel = pusher.subscribePrivate(channelName);

        channel.bind("messaging", new PrivateChannelEventListener() {
            @Override
            public void onEvent(PusherEvent event) {

            }

            /*@Override
            public void onEvent(String channelName, String eventName, String data) {
                System.out.println("Message Received: " + data);
            }*/

            /*@Override
            public void onSubscriptionSucceeded(String channelName) {
                System.out.println("Subscribed to " + channelName);
            }

            @Override
            public void onAuthenticationFailure(String message, Exception e) {
                System.err.println("Authentication Failed: " + message);
                e.printStackTrace();
            }
        });

        pusher.connect();*/
    }
}
