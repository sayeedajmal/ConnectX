package com.Strong.personalchat.models;

public class newChatGetter {
    String username;

    public newChatGetter(){

    }

    public newChatGetter(String username, String chatUserImage, String userId) {
        this.username = username;
        this.chatUserImage = chatUserImage;
        this.userId = userId;
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

    public void setChatUserImage(String chatUserImage) {
        this.chatUserImage = chatUserImage;
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
