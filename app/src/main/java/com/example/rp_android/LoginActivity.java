package com.example.rp_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rp_android.api_routes.ApiLoginService;
import com.example.rp_android.connection.ConnectionFile;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LoginActivity extends AppCompatActivity {
    EditText editTextEmail, editTextPassword;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getApplication().getSharedPreferences("com.rp_android.app", MODE_PRIVATE);

        editor = sharedPreferences.edit();
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        Button button = findViewById(R.id.btnLogin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                loginUser();

            }
            // Toast.makeText(getApplicationContext(), "Press", Toast.LENGTH_SHORT).show();
            //loginUser();
        });

        if(sharedPreferences.getString("loginStatus", "false").equals("true")){
            startActivity(new Intent(getApplicationContext(), HomePage.class));
            finish();
        }

        TextView textView = findViewById(R.id.textReg);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                finish();
            }
        });

    }

    void loginUser(){
        TextInputLayout textInputLayout = findViewById(R.id.layoutHelper);
        String email, password;
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();


        if(email.isEmpty()){
            Toast.makeText(getApplicationContext(), "Email is required", Toast.LENGTH_SHORT).show();
            Log.w("email", "no pp");
            textInputLayout.setHelperText("* Ioncorrect login credentials");
            textInputLayout.setHelperTextColor(ColorStateList.valueOf(Color.RED));
            textInputLayout.setHelperTextEnabled(true);
        }

        if(password.isEmpty()){
            Toast.makeText(getApplicationContext(), "Password is required", Toast.LENGTH_SHORT).show();
            Log.w("password", "no pass");
            textInputLayout.setHelperText("* Ioncorrect login credentials");
            textInputLayout.setHelperTextColor(ColorStateList.valueOf(Color.RED));
            textInputLayout.setHelperTextEnabled(true);

        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConnectionFile.returnURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiLoginService apiLoginService = retrofit.create(ApiLoginService.class);
        User user = new User(email, password);
        Call<ResponseBody> call = apiLoginService.postLogin(user);
        /*LLOO request = new LLOO(email, password);
        ApiLoginService apiLoginService = retrofit.getApiService();*/
       //Call<ApiResponse> call = apiService.sendData(request);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("res", response.toString());
                Gson gson = new Gson();
                String jsonRequest = null;
                /*try {
                    jsonRequest = gson.toJson(response.body().string());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }*/
                //Log.e("resss", jsonRequest);

                if(response.isSuccessful() && response.body() != null){
                    Log.w("password", "3");

                    try {
                        //String res2 = response.body().string();
                        //Log.w("password", res2);

                        String res = response.body().string();
                        //JSONObject jsonObject = new JSONObject(res);
                            try {
                                JSONObject jsonObject = new JSONObject(res);
                                JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                                JSONObject jsonObjectUser = jsonObjectData.getJSONObject("user");
                                JSONObject jsonObjecToken = jsonObjectData.getJSONObject("token");

                                int id = jsonObjectUser.getInt("id");
                                String tokenAuth = jsonObjectData.getString("token");
                                String username = jsonObjectUser.getString("username");
                                String firstname = jsonObjectUser.getString("firstname");
                                String middlename = jsonObjectUser.getString("middlename");
                                String lastname = jsonObjectUser.getString("lastname");
                                String email = jsonObjectUser.getString("email");
                                String profileUrl = jsonObjectUser.getString("profile_url");
                                String role = jsonObjectUser.getString("role");

                                String status = jsonObject.getString("status");
                                String plainToken = jsonObjecToken.getString("plainTextToken");


                                editor.putInt("id", id);
                                editor.putString("token", tokenAuth.toString());
                                editor.putString("plainToken", plainToken);
                                editor.putString("username", username);
                                editor.putString("email", email);
                                editor.putString("firstname", firstname);
                                editor.putString("middlename", middlename);
                                editor.putString("lastname", lastname);
                                editor.putString("profileURL", profileUrl);
                                editor.putString("loginStatus", "true");
                                editor.putString("role", role);

                                editor.apply();
                                startActivity(new Intent(getApplicationContext(), HomePage.class));
                                finish();
                                Log.e("JSON_ERROR","---------------------------------" );

                                // Continue processing
                                Log.e("----",tokenAuth );

                            } catch (JSONException e) {
                                Log.e("JSON_ERROR", "Invalid JSON format: " + e.getMessage());
                                // Handle invalid JSON
                            }

                        /*JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                        JSONObject jsonObjectUser = jsonObjectData.getJSONObject("user");

                        String tokenAuth = jsonObjectData.getString("token");
                        String username = jsonObjectUser.getString("username");
                        String email = jsonObjectUser.getString("email");


                        String status = jsonObject.getString("status");*/



                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }else{
                    Log.w("password", String.valueOf(response.body()));

                    Toast.makeText(getApplicationContext(), "Error is in success ", Toast.LENGTH_SHORT).show();
                    textInputLayout.setHelperText("* Ioncorrect login credentials");
                    textInputLayout.setHelperTextColor(ColorStateList.valueOf(Color.RED));
                    textInputLayout.setHelperTextEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.w("password", throwable.getLocalizedMessage().toString());

                Toast.makeText(getApplicationContext(), "ER" + throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}