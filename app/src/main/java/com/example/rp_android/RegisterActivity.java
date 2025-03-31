package com.example.rp_android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rp_android.api_routes.ApiLoginService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    EditText editTextName, editTextEmail, editTextPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        Button button = findViewById(R.id.btnReg);
        button.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(), "Press", Toast.LENGTH_SHORT).show();
            regUser();
        });
    }

    void regUser(){
        String username, email, password;
        username = editTextName.getText().toString();
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();

        if(username.isEmpty()){
            Toast.makeText(getApplicationContext(), "Username is required", Toast.LENGTH_SHORT).show();
        }

        if(email.isEmpty()){
            Toast.makeText(getApplicationContext(), "Email is required", Toast.LENGTH_SHORT).show();
        }

        if(password.isEmpty()){
            Toast.makeText(getApplicationContext(), "Password is required", Toast.LENGTH_SHORT).show();
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.9.9.127:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiLoginService apiLoginService = retrofit.create(ApiLoginService.class);
        User user = new User(username, email, password);
        Call<ResponseBody> call = apiLoginService.postRegister(user);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("res", response.toString());
                if(response.isSuccessful()){
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Error is in success ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "ER" + throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

}