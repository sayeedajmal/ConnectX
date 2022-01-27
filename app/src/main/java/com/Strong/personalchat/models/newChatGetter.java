package com.Strong.personalchat.models;

public class newChatGetter {
    String username;
    String lastSeen;
    String newChatUserImage;
    String userId;

    public String getUserId() {
        return userId;
    }


    public void setUserId(String userId) {
        this.userId = userId;
    }

    public newChatGetter() {
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getLastSeen() {
        return lastSeen;
    }
    public void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
    }

    public newChatGetter(String username, String lastSeen, String newChatUserImage, String userId) {
        this.username = username;
        this.lastSeen = lastSeen;
        this.newChatUserImage = newChatUserImage;
        this.userId = userId;
    }

    public String getNewChatUserImage() {
        return newChatUserImage;
    }
    public void setNewChatUserImage(String newChatUserImage) {
        this.newChatUserImage = newChatUserImage;
    }
}
