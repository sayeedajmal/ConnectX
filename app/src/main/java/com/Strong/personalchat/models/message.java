package com.Strong.personalchat.models;

public class message {
    String uId;
    String message;
    String seen;
    String messageType;
    Long timeStamp;

    public message(String senderId, String message) {
        this.uId = senderId;
        this.message = message;
    }

    public message(String senderId, String message, String messageType) {
        this.uId = senderId;
        this.message = message;
        this.messageType=messageType;
    }

    public String getuId() {
        return uId;
    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

    public String getMessageType() {
        return messageType;
    }

    public String getMessage() {
        return message;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public message() {

    }
}
