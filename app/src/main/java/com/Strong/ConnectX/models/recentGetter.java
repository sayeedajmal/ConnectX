package com.Strong.ConnectX.models;

public class recentGetter {
    String chatUserImage;

    public recentGetter() {

    }

    public String getChatUserImage() {
        return chatUserImage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userID) {
        this.userId = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    String userId;
    String username;
}
