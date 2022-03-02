package com.Strong.personalchat.models;

public class recentGetter {
    String chatUserImage;

    public recentGetter(){
        
    }
    public recentGetter(String chatUserImage, String userId, String username) {
        this.chatUserImage = chatUserImage;
        this.userId = userId;
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
