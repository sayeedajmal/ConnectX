package com.Strong.personalchat.models;

public class message {
    String uId;
    String message;
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
    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getuId() {
        return uId;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
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
