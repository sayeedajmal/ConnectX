package com.Strong.ConnectX.models;

public class newChatGetter {
    String username;
    static String PersonSearch;

    public newChatGetter() {

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

    String chatUserImage;
    String userId;
}
