package com.Strong.personalchat.models;

public class CurrentUser {

    public static String username, userId, status, searchUser, password, email, chatUserImage;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        CurrentUser.username = username;
    }

    public static String getUserId() {
        return userId;
    }

    public static void setUserId(String userId) {
        CurrentUser.userId = userId;
    }

    public static String getStatus() {
        return status;
    }

    public static void setStatus(String status) {
        CurrentUser.status = status;
    }

    public static String getSearchUser() {
        return searchUser;
    }

    public static void setSearchUser(String searchUser) {
        CurrentUser.searchUser = searchUser;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        CurrentUser.password = password;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        CurrentUser.email = email;
    }

    public static String getChatUserImage() {
        return chatUserImage;
    }

    public static void setChatUserImage(String chatUserImage) {
        CurrentUser.chatUserImage = chatUserImage;
    }

    public CurrentUser() {
    }
}
