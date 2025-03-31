package com.example.rp_android.ui.chat_detail;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rp_android.adapters.ChatPhotosAdapter;
import com.example.rp_android.api_routes.ApiChatService;
import com.example.rp_android.api_routes.ApiDataService;
import com.example.rp_android.AuthChannel;
import com.example.rp_android.connection.ConnectionFile;
import com.example.rp_android.PusherManager;
import com.example.rp_android.R;
import com.example.rp_android.SendMessage;
import com.example.rp_android.adapters.ChatMessageAdapter;
import com.example.rp_android.connection.PusherConnectionFile;
import com.example.rp_android.databinding.FragmentChatDetailBinding;
import com.example.rp_android.models.ChannelAuthRequest;
import com.example.rp_android.models.ChatGetModel;
import com.example.rp_android.models.ChatMessageModel;
import com.example.rp_android.models.DetailModel;
import com.example.rp_android.models.PhotosModel;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.pusher.client.channel.PrivateChannel;
import com.pusher.client.channel.PrivateChannelEventListener;
import com.pusher.client.channel.PusherEvent;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;
import com.pusher.client.util.HttpAuthorizer;

public class ChatDetailFragment extends Fragment implements ChatMessageAdapter.OnItemClickListener {

    private ImageView infoButton;
    private ImageButton imageButton;
    private Integer employeeId;
    private OkHttpClient client = new OkHttpClient();
    SharedPreferences sharedPreferences;
    private static Retrofit retrofit = null;
    private FragmentChatDetailBinding binding;
    private TextView userTitle;
    private TextView sideName;
    private TextView message;
    private ChatMessageAdapter adapter;
    private ChatPhotosAdapter adapterPhotos;
    private RecyclerView recyclerView;
    private RecyclerView photosRecyclerView;
    private Pusher pusher;
    private Pusher pusherSender;

    //private Channel channel;
    private PusherManager pusherManager;
    private SendMessage sendMessage;
    private AuthChannel authChannel;
    private ImageView sendButton;

    private ImageView favoriteButton;
    private TextView deleteConversation;
    private EditText messageInput;
    private boolean favoriteboolean;
    public PrivateChannel channel;

    private int firstMessageLoad;

    private List<ChatMessageModel> messageList = new ArrayList<>();
    private List<PhotosModel> photoList = new ArrayList<>();



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ChatDetailViewModel chatDetailViewModel =
                new ViewModelProvider(this).get(ChatDetailViewModel.class);

        binding = FragmentChatDetailBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        retrofit = new Retrofit.Builder()
                .baseUrl(ConnectionFile.returnURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FrameLayout sideFrameLayout = binding.sideSheetContainer;
        infoButton = binding.infoButton;
        imageButton = binding.closeButton;
        sharedPreferences = requireContext().getSharedPreferences("com.rp_android.app", Context.MODE_PRIVATE);
        userTitle = binding.userTitle;
        sendButton = binding.sendButton;
        sideName = binding.sideName;
        recyclerView = binding.conversationRecyclerView;
        favoriteButton = binding.favoriteButton;
        messageInput = binding.messageInput;
        favoriteboolean = false;
        deleteConversation = binding.deleteConversation;
        photosRecyclerView = binding.photosRecyclerView;
        firstMessageLoad = 0;
        userTitle.setWidth((int) (getScreenWidthPixels(getResources())/1.5));

        SpannableString spannable = new SpannableString("Hello dsadsahkldsajjk hka hdhkjas hjkdsa hjkh djkahsk hkjsa jdsakj jkdh jkas djkasj  kda World"); // Reserve space for icon


        Drawable drawable = getResources().getDrawable(R.drawable.check_solid_white);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());


        ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
        spannable.setSpan(imageSpan, spannable.length()-1, spannable.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);





        Animation slideIn = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_right);
        Animation slideOut = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_out_right);
        infoButton.setOnClickListener(v ->{
            sideFrameLayout.setVisibility(View.VISIBLE);
            sideFrameLayout.startAnimation(slideIn);
        });
        imageButton.setOnClickListener(v -> {
            sideFrameLayout.setVisibility(View.INVISIBLE);
            sideFrameLayout.startAnimation(slideOut);
        });
        favoriteButton.setOnClickListener(v -> {
            if(favoriteboolean){
                showCustomDialog("Do you want to remove user from favorite ?", "Please confirm your action ", "bi_question_circle", 1);
            }else{
                setStar();
            }
        });
        deleteConversation.setOnClickListener(v -> {
            showCustomDialog("Do you want to delete conversation ?", "Please confirm your action ", "bi_question_circle", 2);

                }
        );
        sendMessage = new SendMessage();

        sendButton.setOnClickListener(v -> {
            String getText = messageInput.getText().toString();
            if(!getText.isEmpty()){
                String fixedText = getText.replace("\r", "").replace("\n", "\\n");
                sendMessage.sendMessage(fixedText, employeeId, sharedPreferences.getInt("id", 0), sharedPreferences.getString("plainToken", ""));
                messageInput.setText("");
                if(employeeId == sharedPreferences.getInt("id", 0)){
                    loadMessages();

                }
            }
        });
        if (getArguments() != null) {
            employeeId = getArguments().getInt("employee_id");

        }

        authChannel = new AuthChannel();

        PusherManager(sharedPreferences.getString("plainToken", ""), "private-chatify."+employeeId);
        PusherManagerReceiver(sharedPreferences.getString("plainToken", ""), "private-chatify."+sharedPreferences.getInt("id",0));





        adapter = new ChatMessageAdapter(messageList,
                ChatDetailFragment.this,
                getParentFragmentManager(),
                requireContext()
        );

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);





        ApiChatService authService = retrofit.create(ApiChatService.class);



        Call<ResponseBody> callAuth = authService.authorizeChannel("Bearer "+sharedPreferences.getString("plainToken", ""),
                new ChannelAuthRequest(sharedPreferences.getInt("id",0) , "private-chatify.3", "13456.982346"));
        callAuth.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("Response", response.toString());
                if (response.isSuccessful()) {
                    String res = null;
                    try {
                        res = response.body().string();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    try {
                        JSONObject jsonObject = new JSONObject(res);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("failled push","---");

            }

        });

        getFavoriteStatus();
        ApiDataService getDetail = retrofit.create(ApiDataService.class);
        loadMessages();
        loadSharedPhotos();
        DetailModel detailModel = new DetailModel(String.valueOf(employeeId));
        String token = sharedPreferences.getString("token", "");
        Call<ResponseBody> callEmployee = getDetail.postEmployeeDetail("Bearer "+ sharedPreferences.getString("plainToken", ""), detailModel);
        callEmployee.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("res", response.toString());
                if (response.isSuccessful()) {
                    String res = null;
                    try {
                        res = response.body().string();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    Log.e("bodyssssss", response.message());
                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        userTitle.setText(jsonObject.getString("firstname") + " " + jsonObject.getString("middlename") + " " +  jsonObject.getString("lastname"));
                        sideName.setText(jsonObject.getString("firstname") + " " + jsonObject.getString("middlename") + " " +  jsonObject.getString("lastname"));
                        Glide.with(requireContext()).load(ConnectionFile.returnURLRaw() + jsonObject.getString("profile_url"))
                                .into(binding.profileImage);
                        Glide.with(requireContext()).load(ConnectionFile.returnURLRaw() + jsonObject.getString("profile_url"))
                                .into(binding.profileImageSider);






                    } catch (JSONException e) {
                        Log.e("JSON_ERROR", "Invalid JSON format: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("failled","---");
                Toast.makeText(requireContext(), "Error: " + throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });




        return root;
    }


    @Override
    public void onDestroyView() {
        if (pusher != null) {
            pusher.disconnect();
        }
        if (pusherSender != null) {
            pusherSender.disconnect();
        }
        super.onDestroyView();
        binding = null;
    }

    public void loadMessages(){
        ApiChatService getMessage= retrofit.create(ApiChatService.class);

        ChatGetModel getMessages = new ChatGetModel(employeeId);
        String token = sharedPreferences.getString("token", "");
        DetailModel detailModel = new DetailModel(String.valueOf(employeeId));
        GetSendModel getSendModel = new GetSendModel(sharedPreferences.getInt("id",0),employeeId);
        Call<ResponseBody> callMesaege = getMessage.chatifyGetMessages2("Bearer "+ sharedPreferences.getString("plainToken", ""), getSendModel);
        callMesaege.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("Response", response.toString());
                if (response.isSuccessful()) {
                    String res = null;
                    try {
                        res = response.body().string();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        if (jsonObject.has("messages")) {
                            Object dataObject = jsonObject.get("messages");
                            messageList = new ArrayList<>();
                            if (dataObject instanceof JSONObject) {
                                JSONObject jsonObjectData = (JSONObject) dataObject;
                                Log.d("JSON", "Data is an object: " + jsonObjectData.toString());
                            }
                            else if (dataObject instanceof JSONArray) {
                                Log.d("JS465654ON", "Data is an object: " + ((JSONArray) dataObject).length());

                                JSONArray jsonArrayData = (JSONArray) dataObject;
                                for (int i = 0; i < jsonArrayData.length(); i++) {
                                    JSONObject item = jsonArrayData.getJSONObject(i);
                                    int type;
                                    String seen = "&#xF842;";
                                    long timestamp = (System.currentTimeMillis()-convertStringToTimestamp(item.getString("updated_at").replace("T", " ").substring(0,19)))/1000;

                                    SpannableString spannable = new SpannableString(Html.fromHtml(item.getString("body").replace("\n", "<br>")+ "&#160;&#160;&#160;<small>" + convertSeconds(timestamp) + " </small>")); // Reserve space for the icon
                                    Drawable drawable = getResources().getDrawable(R.drawable.check_solid_white);
                                    ImageSpan imageSpan;

                                    if(item.getInt("to_id") == employeeId){
                                        type = 1;
                                        if(item.getInt("seen") == 0){
                                            drawable = getResources().getDrawable(R.drawable.check_solid_white);
                                            drawable.setBounds(0, 10, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                                        }else{
                                            drawable = getResources().getDrawable(R.drawable.check_all_white);
                                            drawable.setBounds(0, 10, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                                        }
                                        imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);

                                        spannable.setSpan(imageSpan, spannable.length()-convertSeconds(timestamp).length()-3, spannable.length()-convertSeconds(timestamp).length()-2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                                    }else{
                                        type = 0;
                                        imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
                                        spannable.setSpan(imageSpan, 0, 0, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                                    }
                                    int visibilitity = View.GONE;
                                    String attechmentURL = "";
                                    if(!item.getString("attachment").equals("null")){
                                        visibilitity = View.VISIBLE;

                                        JSONObject jsonObjectAttechment = new JSONObject(item.getString("attachment"));

                                        String newName = jsonObjectAttechment.getString("new_name");
                                        attechmentURL = newName;

                                    }

                                    messageList.add(new ChatMessageModel(item.getString("id"), item.getInt("from_id"), item.getInt("to_id"), item.getString("body"), attechmentURL, item.getInt("seen"), spannable, convertSeconds(timestamp),type, visibilitity ));
                                }


                            }
                        }
                        if(firstMessageLoad == 0) {


                            adapter = new ChatMessageAdapter(messageList,
                                    ChatDetailFragment.this,
                                    getParentFragmentManager(),
                                    requireContext()
                            );

                            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                            recyclerView.setAdapter(adapter);
                            firstMessageLoad++;
                        }else{
                            for (int i = 0; i < messageList.size(); i++) {
                                adapter.changeData(i, messageList.get(i));
                            }
                        }


                        recyclerView.post(() -> recyclerView.scrollToPosition(adapter.getItemCount()-1));


                    } catch (JSONException e) {
                        Log.e("JSON_ERROR", "Invalid JSON format: " + e.getMessage());
                        //  // Handle invalid JSON
                    }
                } else {
                    Toast.makeText(requireContext(), "Error in success response", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Toast.makeText(requireContext(), "Error: " + throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void deleteChat(int position, int id) {

    }
    public static String convertSeconds(long seconds) {
        if (seconds < 60) {
            return seconds + "s ago" ;
        }

        long minutes = seconds / 60;
        if (minutes < 60) {
            return minutes + "m ago" ;
        }

        long hours = minutes / 60;
        if (hours < 24) {
            return hours + "h ago" ;
        }

        long days = hours / 24;
        if (days < 7) {
            return days + "d ago" ;
        }

        long weeks = days / 7;
        if (weeks < 52) {
            return weeks + "w ago" ;
        }

        long years = weeks / 52;
        return years + "y ago" ;
    }
    public long convertStringToTimestamp(String dateTimeString) {
        try {
            // Define the format of your input string
            DateTimeFormatter formatter = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            }

            // Parse the string into a LocalDateTime object
            LocalDateTime localDateTime = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                localDateTime = LocalDateTime.parse(dateTimeString, formatter);
            }

            // Convert LocalDateTime to timestamp (milliseconds since epoch)
            long timestamp = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                timestamp = localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
            }

            return timestamp;

        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Or handle the error appropriately
        }
    }

    public void PusherManager(String token, String channelName) {
        PusherOptions options = new PusherOptions();
        options.setCluster(PusherConnectionFile.returnCluster()); // Example: eu, mt1
        HttpAuthorizer authorizer = new HttpAuthorizer(ConnectionFile.returnURL()+ "chatify/api/chat/auth") {


            public Map<String, String> getQueryParameters() {
                return null;
            }

            public Map<String, String> getBodyParameters() {
                Map<String, String> bodyParams = new HashMap<>();
                Random r = new Random();
                bodyParams.put("channel_name", channelName); // Replace channelName with the actual channel name
                String ee = String.valueOf(r.nextInt(11));
                bodyParams.put("socket_id", ee); // Replace socketId with the actual socket ID
                return bodyParams;
            }
        };
        Map<String, String> headerss = new HashMap<>();
        headerss.put("Authorization", "Bearer " + token);
        headerss.put("Accept", "application/json");
        authorizer.setHeaders(headerss);
        options.setChannelAuthorizer(authorizer);
        pusher = new Pusher(PusherConnectionFile.returnKey(), options);



        PrivateChannel channel = pusher.subscribePrivate(channelName, new PrivateChannelEventListener() {
            @Override
            public void onSubscriptionSucceeded(String channelName) {
                Log.d("Pusher", "Successfully subscribed 2 " + channelName);


            }

            @Override
            public void onEvent(PusherEvent event) {
                Log.d("Pusher", "Received event: " + event.getData());
               // triggerClientEvent(channel);
                if (event.getEventName().equals("messaging")) {
                    Log.e("|||", ":::: Messaging event received!");

                }
            }


            @Override
            public void onAuthenticationFailure(String message, Exception e){
                Log.e("Pusher", "Authentication failure: " + message, e);
            }
        });

        channel.bind("messaging", new PrivateChannelEventListener() {
            @Override
            public void onSubscriptionSucceeded(String channelName) {



            }

            @Override
            public void onAuthenticationFailure(String message, Exception e) {
                Log.e("Pusher", "::::");

            }

            @Override
            public void onEvent(PusherEvent event) {
                Log.e("|||", "::---::");
                Log.e("|||", "::---::");

                loadMessages();


            }

            @Override
            public void onError(String message, Exception e) {
                PrivateChannelEventListener.super.onError(message, e);
            }


        });

        pusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                System.out.println("State changed to " + change.getCurrentState());
            }

            @Override
            public void onError(String message, String code, Exception e) {
                System.out.println("There was a problem connecting! " + message + " code: " + code + " exception: " + e);
            }
        }, ConnectionState.ALL);
    }

    private void triggerClientEvent(PrivateChannel channel) {
        try {
            channel.trigger("state_change", "{\"message\": \"Hello from Android!\"}");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void PusherManagerReceiver(String token, String channelName) {
        PusherOptions options = new PusherOptions();
        options.setCluster(PusherConnectionFile.returnCluster()); // Example: eu, mt1
        HttpAuthorizer authorizer = new HttpAuthorizer(ConnectionFile.returnURL()+ "chatify/api/chat/auth") {


            public Map<String, String> getQueryParameters() {
                // Add query parameters if needed
                return null;
            }

            public Map<String, String> getBodyParameters() {
                // Add channel_name and socket_id to the request body
                Map<String, String> bodyParams = new HashMap<>();
                Random r = new Random();
                bodyParams.put("channel_name", channelName); // Replace channelName with the actual channel name
                bodyParams.put("socket_id", String.valueOf(r.nextInt(11))); // Replace socketId with the actual socket ID
                return bodyParams;
            }
        };
        Map<String, String> headerss = new HashMap<>();
        headerss.put("Authorization", "Bearer " + token);
        headerss.put("Accept", "application/json");
        authorizer.setHeaders(headerss);
        options.setChannelAuthorizer(authorizer);
        pusherSender = new Pusher(PusherConnectionFile.returnKey(), options);



        PrivateChannel channel = pusherSender.subscribePrivate(channelName, new PrivateChannelEventListener() {
            @Override
            public void onSubscriptionSucceeded(String channelName) {
                Log.d("Pusher", "Successfully subscribed sender " + channelName);
                makeSeen();

            }

            @Override
            public void onEvent(PusherEvent event) {
                Log.d("Pusher", "Received event: " + event.getData());
                // Handle the event data here.
            }



            @Override
            public void onAuthenticationFailure(String message, Exception e){
                Log.e("Pusher", "Authentication failure: " + message, e);
            }
        });
        channel.bind("client-seen", new PrivateChannelEventListener() {

            @Override
            public void onEvent(PusherEvent event) {
                loadMessages();
            }

            @Override
            public void onSubscriptionSucceeded(String channelName) {

            }

            @Override
            public void onAuthenticationFailure(String message, Exception e) {

            }
        });
        channel.bind("messaging", new PrivateChannelEventListener() {
            @Override
            public void onSubscriptionSucceeded(String channelName) {

            }

            @Override
            public void onAuthenticationFailure(String message, Exception e) {
                Log.e("Pusher", "::::");

            }

            @Override
            public void onEvent(PusherEvent event) {
                Log.e("|||", "::---::");
                Log.e("|||", "::---::");

                loadMessages();
                makeSeen();


            }

            @Override
            public void onError(String message, Exception e) {
                PrivateChannelEventListener.super.onError(message, e);
            }


        });


        pusherSender.connect();

    }

     public void getFavoriteStatus(){
         ApiChatService favoritestatus = retrofit.create(ApiChatService.class);



         Call<ResponseBody> callFavorite = favoritestatus.getFavoriteStatus("Bearer "+sharedPreferences.getString("plainToken", ""),
                 new GetSendModel(sharedPreferences.getInt("id",0) ,employeeId));
         callFavorite.enqueue(new Callback<ResponseBody>() {
             @Override
             public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                 Log.e("Response:", response.toString());
                 if (response.isSuccessful()) {
                     String res = null;
                     try {
                         res = response.body().string();
                     } catch (IOException e) {
                         throw new RuntimeException(e);
                     }

                     try {
                         JSONObject jsonObject = new JSONObject(res);
                         if (jsonObject.has("status")) {
                             favoriteboolean = jsonObject.getBoolean("status");

                             if(favoriteboolean){

                                 favoriteButton.setColorFilter(ContextCompat.getColor(requireContext(), R.color.warning), PorterDuff.Mode.SRC_IN);
                             }
                         }
                     } catch (JSONException e) {
                         throw new RuntimeException(e);
                     }
                 }
             }

             @Override
             public void onFailure(Call<ResponseBody> call, Throwable throwable) {

             }
         });
     }
    private void showCustomDialog(String header, String text, String icon, int typeConfirm) {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.confirmation_dialog);
        dialog.setCancelable(false);
        int resourceId = getResources().getIdentifier(icon, "drawable", requireContext().getPackageName());
        Drawable draw;
        if (resourceId != 0) {
            draw = ContextCompat.getDrawable(requireContext(), resourceId);
        }else{
            draw = ContextCompat.getDrawable(requireContext(), R.drawable.bi_house);
        }
        ImageView imageDialog = dialog.findViewById(R.id.imageDialog);
        TextView dialogTextHeader = dialog.findViewById(R.id.dialogTextHeader);
        TextView dialogText = dialog.findViewById(R.id.dialogText);
        Drawable dialogIcon = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_check_circle_outline_24);
        dialogIcon.setTint(ContextCompat.getColor(requireContext(), R.color.danger));
            draw.setTint(ContextCompat.getColor(requireContext(), R.color.warning));

        imageDialog.setImageDrawable(draw);
        dialogTextHeader.setText(header);
        dialogText.setText(text);

        Button btnClose = dialog.findViewById(R.id.buttonClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss(); // Close the dialog
            }
        });
        Button buttonConfirm = dialog.findViewById(R.id.buttonConfirm);
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(typeConfirm == 1) {
                    ApiChatService favoriteReomove = retrofit.create(ApiChatService.class);
                    Call<ResponseBody> callRemoveFavorite = favoriteReomove.removeFavoriteStatus("Bearer " + sharedPreferences.getString("plainToken", ""),
                            new GetSendModel(sharedPreferences.getInt("id", 0), employeeId));
                    callRemoveFavorite.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Log.e("Response:", response.toString());
                            if (response.isSuccessful()) {
                                favoriteButton.clearColorFilter();
                                favoriteboolean = false;
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable throwable) {

                        }
                    });
                    dialog.dismiss(); // Close the dialog
                }else{
                    ApiChatService deleteChat= retrofit.create(ApiChatService.class);
                    Call<ResponseBody> callDelete = deleteChat.deleteConversation("Bearer "+sharedPreferences.getString("plainToken", ""),
                            new GetSendModel(sharedPreferences.getInt("id",0) ,employeeId));
                    callDelete.enqueue(new Callback<ResponseBody>() {

                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Log.e("Response:", response.toString());
                            if (response.isSuccessful()) {
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                fragmentManager.popBackStack();
                            }

                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable throwable) {

                        }
                    });
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }
    private void setStar(){
        ApiChatService favoriteSet= retrofit.create(ApiChatService.class);
        Call<ResponseBody> callSetFavorite = favoriteSet.setStar("Bearer "+sharedPreferences.getString("plainToken", ""),
                new GetSendModel(sharedPreferences.getInt("id",0) ,employeeId));
        callSetFavorite.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("Response:", response.toString());
                if (response.isSuccessful()) {
                    favoriteButton.setColorFilter(ContextCompat.getColor(requireContext(), R.color.warning), PorterDuff.Mode.SRC_IN);

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

            }
        });
    }
    private int getScreenWidthPixels(Resources resources) {
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        return displayMetrics.widthPixels;
    }
    private void makeSeen(){
        ApiChatService seenSet= retrofit.create(ApiChatService.class);
        Call<ResponseBody> callseenSet= seenSet.makeSeen("Bearer "+sharedPreferences.getString("plainToken", ""),
                new GetSendModel(sharedPreferences.getInt("id",0) ,employeeId));
        callseenSet.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("Response:", response.toString());
                if (response.isSuccessful()) {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

            }
        });
    }
    private void loadSharedPhotos(){
        ApiChatService getPhotos= retrofit.create(ApiChatService.class);
        Call<ResponseBody> callPhotos= getPhotos.sharedPhotos("Bearer "+sharedPreferences.getString("plainToken", ""),
                new GetSendModel(sharedPreferences.getInt("id",0) ,employeeId));
        callPhotos.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("Response:", response.toString());
                if (response.isSuccessful()) {
                    String res = null;
                    try {
                        res = response.body().string();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        photoList = new ArrayList<>();
                        if (jsonObject.has("shared")) {
                            Object dataObject = jsonObject.get("shared");

                            if (dataObject instanceof JSONObject) {
                                JSONObject jsonObjectData = (JSONObject) dataObject;

                                Log.d("JSON", "Data is an img: " + jsonObjectData.toString());
                            }
                            // Check if data is a JSON Array
                            else if (dataObject instanceof JSONArray) {
                                Log.d("JS465654ON", "Data is an img: " + ((JSONArray) dataObject).length());
                                JSONArray jsonArrayData = (JSONArray) dataObject;
                                for (int i = 0; i < jsonArrayData.length(); i++) {
                                    photoList.add(new PhotosModel(jsonArrayData.getString(i)));
                                }

                            }
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    adapterPhotos = new ChatPhotosAdapter(photoList,
                            getParentFragmentManager(),
                            requireContext()
                    );

                    photosRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                    photosRecyclerView.setAdapter(adapterPhotos);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

            }
        });
    }







}