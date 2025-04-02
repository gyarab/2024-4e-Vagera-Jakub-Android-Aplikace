package com.example.rp_android;

import android.util.Log;

import com.example.rp_android.connection.ConnectionFile;
import com.example.rp_android.connection.PusherConnectionFile;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.PrivateChannel;
import com.pusher.client.channel.PrivateChannelEventListener;
import com.pusher.client.channel.PusherEvent;
import com.pusher.client.util.HttpAuthorizer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Testovaci soubor slouzi pro prihlaseni uzivatele do Pusheru
 */
public class PusherManager {

    private Pusher pusher;
    /**
     * Tato cast byla castecne vygenerovana ChatGPT model 4.0
     */
    public PusherManager(String token) {
        PusherOptions options = new PusherOptions();
        options.setCluster(PusherConnectionFile.returnCluster()); // Example: eu, mt1
        HttpAuthorizer authorizer = new HttpAuthorizer(ConnectionFile.returnURL()+ "chatify/api/chat/auth") {

            /*public Map<String, String> getHeaders() {
                // Add any necessary headers (e.g., authentication headers)
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                headers.put("Accept", "application/json");

                // Example: headers.put("Authorization", "Bearer YOUR_AUTH_TOKEN");
                return headers;
            }*/

            public Map<String, String> getQueryParameters() {
                // Add query parameters if needed
                return null;
            }

            public Map<String, String> getBodyParameters() {
                // Add channel_name and socket_id to the request body
                Map<String, String> bodyParams = new HashMap<>();
                bodyParams.put("channel_name", "private-chatify.3"); // Replace channelName with the actual channel name
                bodyParams.put("socket_id", "13456.982346"); // Replace socketId with the actual socket ID
                return bodyParams;
            }
        };
        Map<String, String> headerss = new HashMap<>();
        headerss.put("Authorization", "Bearer " + token);
        headerss.put("Accept", "application/json");
        authorizer.setHeaders(headerss);
        options.setChannelAuthorizer(authorizer);
        pusher = new Pusher(PusherConnectionFile.returnKey(), options);
    }

    public void connect() {
        //PrivateChannel channel = pusher.subscribePrivate("private-chatify.3");
        PrivateChannel channel = pusher.subscribePrivate("private-chatify.3", new PrivateChannelEventListener() {
            @Override
            public void onSubscriptionSucceeded(String channelName) {
                Log.d("Pusher", "Successfully subscribed to " + channelName);
            }

            @Override
            public void onEvent(PusherEvent event) {
                Log.d("Pusher", "Received event: " + event.getData());
                if (event.getEventName().equals("messaging")) {
                    Log.e("|||", ":::: Messaging event received!");
                    //
                }
                // Handle the event data here.
            }

            /*@Override
            public void onSubscriptionError(String message, Exception e) {
                Log.e("Pusher", "Subscription error: " + message, e);
            }*/

            @Override
            public void onAuthenticationFailure(String message, Exception e){
                Log.e("Pusher", "Authentication failure: " + message, e);
            }
        });
        channel.bind("messaging", new PrivateChannelEventListener() {
            @Override
            public void onSubscriptionSucceeded(String channelName) {
               // Log.e("|||", "::::");

            }

            @Override
            public void onAuthenticationFailure(String message, Exception e) {
                Log.e("Pusher", "::::");

            }

            @Override
            public void onEvent(PusherEvent event) {
                Log.e("|||", "::::");
                Log.e("|||", "::::");

            }

            @Override
            public void onError(String message, Exception e) {
                PrivateChannelEventListener.super.onError(message, e);
            }


        });

        pusher.connect();
        //pusher.trigger("my-channel", "my-event", Collections.singletonMap("message", "Hello World"));
    }

    public void disconnect() {
        if (pusher != null) {
            pusher.disconnect();
        }
    }
}