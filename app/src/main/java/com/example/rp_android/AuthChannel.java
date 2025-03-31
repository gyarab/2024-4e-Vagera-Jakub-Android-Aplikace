package com.example.rp_android;

import android.content.SharedPreferences;
import android.util.Log;

import com.example.rp_android.connection.ConnectionFile;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AuthChannel {
    /**
     * Strary kod pro prihlasovani do Pusheru
     */
    private static final String TAG = "AUTHCANNEL";
    private static final String BASE_URL = ConnectionFile.returnURL()+"chatify/chat/auth";
    private static final String JWT_TOKEN = "YOUR_JWT_TOKEN";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();

    public void authChannel(int user,String token) {
        Log.e("token", BASE_URL);
        String json = "{"
                + "\"channel_name\":\"" + "private-chatify."+ user+ "\","
                + "\"socket_id\":" + "433"
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
                } else {
                    Log.e(TAG, "Error: " + response.code());
                }
            }
        });
    }
}
