package com.Strong.personalchat.models;

public class message {
    String uId;
    String message;
    Long timeStamp;

    public message(String senderId, String message) {
        this.uId=senderId;
        this.message=message;
    }

    public String getuId() {
        return uId;
    }


    public String getMessage() {
        return message;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public message(){

    }
}
