package com.example.rp_android;

import android.content.SharedPreferences;
import android.util.Log;

import com.example.rp_android.connection.ConnectionFile;
import com.pusher.client.channel.PrivateChannel;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

public class SendMessage {
    SharedPreferences sharedPreferences;

    private static final String TAG = "SendMessage";
    private static final String BASE_URL = ConnectionFile.returnURL()+"chatify/api/sendMessage";
    private static final String JWT_TOKEN = "YOUR_JWT_TOKEN";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();

    public void sendMessage(String message, int receiverId, int sender, String token) {
        Log.e("token", BASE_URL);
        String json = "{"
                + "\"temporaryMsgId\":\"" + 1 + "\","
                + "\"type\":\"" + 1 + "\","
                + "\"message\":\"" + message + "\","
                + "\"sender_id\":\"" + sender + "\","
                + "\"id\":" + receiverId
                + "}";

        RequestBody body = RequestBody.create(JSON, json);

        Request request = new Request.Builder()
                .url(BASE_URL)
                .post(body)
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("Accept", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Failed: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Success: " + response.body().string());
                    //triggerPusherEvent(message, sender, receiverId);
                } else {
                    Log.e(TAG, "Error: " + response.code());
                }
            }
        });

    }
    /*private void triggerPusherEvent(String message, int senderId, int receiverId, PrivateChannel channel) {
        try {
            // Create the event data
            JSONObject data = new JSONObject();
            data.put("message", message);
            data.put("sender_id", senderId);
            data.put("receiver_id", receiverId);
            data.put("type", 1);

            // Send trigger to private channel
            if (channel != null) {
                channel.trigger("client-messaging", data.toString());
                Log.d(TAG, "Pusher event triggered.");
            } else {
                Log.e(TAG, "Pusher channel not initialized.");
            }
        } catch (JSONException e) {
            Log.e(TAG, "JSON Exception: " + e.getMessage());
        }
    }*/
}