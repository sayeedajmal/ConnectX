package com.Strong.personalchat.models;

public class primaryGetter {
    String username;
    String email;
    String password;
    String lastMessage;
    String chatUserImage;
    String userId;

    primaryGetter(){

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getChatUserImage() {
        return chatUserImage;
    }

    public void setChatUserImage(String chatUserImage) {
        this.chatUserImage = chatUserImage;
    }

    public String getUserId(String key) {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public primaryGetter(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
