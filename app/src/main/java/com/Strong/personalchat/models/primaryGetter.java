package com.Strong.personalchat.models;

public class primaryGetter {
    String username;
    String email;
    String password;
    String chatUserImage;
    String userId;
    String status;

    public String getStatus() {
        return status;
    }

    primaryGetter(){

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getChatUserImage() {
        return chatUserImage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public primaryGetter(String username, String email, String password, String status) {
        this.username = username;
        this.email = email;
        this.password=password;
        this.status=status;
    }
}
