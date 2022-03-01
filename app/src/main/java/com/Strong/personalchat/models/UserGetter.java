package com.Strong.personalchat.models;

public class UserGetter {
    String username;
    String email;
    String password;
    String chatUserImage;
    String userId;
    String status;

    public String getStatus() {
        return status;
    }

    public UserGetter(){

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

    public UserGetter(String username, String email, String password, String chatUserImage, String userId, String status) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.chatUserImage = chatUserImage;
        this.userId = userId;
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


}
