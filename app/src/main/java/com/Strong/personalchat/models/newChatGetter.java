package com.Strong.personalchat.models;

public class newChatGetter {
    String username;
    static String PersonSearch;

    public newChatGetter(){

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

    public static String getPersonSearch() {
        return PersonSearch;
    }

    public static void setPersonSearch(String personSearch) {
        System.out.println("<<<<<<<<<<<"+newChatGetter.getPersonSearch());
        PersonSearch = personSearch;
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
