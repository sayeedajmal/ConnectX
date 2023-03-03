package com.Strong.personalchat.Utilities;

public class Data {
    private String Title, Message;

    public Data(String title, String message) {
        Title = title;
        Message = message;
    }

    public Data() {
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }


}
