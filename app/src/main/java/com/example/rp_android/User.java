package com.example.rp_android;

public class User {
    private String username, email, password;

    User(String email, String password){
        this.email  = email;
        this.password = password;
    }
    User(String username, String email, String password){
        this.username = username;
        this.email  = email;
        this.password = password;
    }

    public String getUsernam(){
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setUsernam(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
