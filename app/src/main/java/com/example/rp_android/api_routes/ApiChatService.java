package com.example.rp_android.api_routes;

import com.example.rp_android.models.ChannelAuthRequest;
import com.example.rp_android.models.ChatGetModel;
import com.example.rp_android.ui.chat_detail.GetSendModel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * API spojeni pro chat
 */
public interface ApiChatService {
    @Headers({"Content-Type: application/json"})
    @POST("/api/chatify_favorite")
    Call<ResponseBody> chatifyFavorite(
            @Header("Authorization") String authToken,
            @Body ChatGetModel model

    );

    @Headers({"Content-Type: application/json"})
    @POST("/api/chatify_contacts")
    Call<ResponseBody> chatifyGetContacts(
            @Header("Authorization") String authToken,
            @Body ChatGetModel model

    );

    @Headers({"Content-Type: application/json"})
    @POST("/api/chatify_messages")
    Call<ResponseBody> chatifyGetMessages2(
            @Header("Authorization") String authToken,
            @Body GetSendModel model

    );

    @POST("/chatify/api/chat/auth")
    Call<ResponseBody> authorizeChannel(
            @Header("Authorization") String authToken,
            @Body ChannelAuthRequest request
    );
    @POST("/chatify/api/getFavoriteStatus")
    Call<ResponseBody> getFavoriteStatus(
            @Header("Authorization") String authToken,
            @Body GetSendModel request
    );
    @POST("/chatify/api/removeFavoriteStatus")
    Call<ResponseBody> removeFavoriteStatus(
            @Header("Authorization") String authToken,
            @Body GetSendModel request
    );
    @POST("/chatify/api/star")
    Call<ResponseBody> setStar(
            @Header("Authorization") String authToken,
            @Body GetSendModel request
    );
    @POST("/chatify/api/deleteConversation")
    Call<ResponseBody> deleteConversation(
            @Header("Authorization") String authToken,
            @Body GetSendModel request
    );
    @POST("/chatify/api/makeSeen")
    Call<ResponseBody> makeSeen(
            @Header("Authorization") String authToken,
            @Body GetSendModel request
    );

    @POST("/chatify/api/shared")
    Call<ResponseBody> sharedPhotos(
            @Header("Authorization") String authToken,
            @Body GetSendModel request
    );

}
