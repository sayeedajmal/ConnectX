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

    UserGetter(){

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setChatUserImage(String chatUserImage) {
        this.chatUserImage = chatUserImage;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UserGetter(String username, String email, String password, String status) {
        this.username = username;
        this.email = email;
        this.password=password;
        this.status=status;
    }
}
