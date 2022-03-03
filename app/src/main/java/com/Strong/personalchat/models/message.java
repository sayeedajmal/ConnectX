package com.Strong.personalchat.models;

public class message {
    String Uid;
    String message;
    String receiverImage;
    Long timeStamp;

    public message(String senderId, String message) {
        this.Uid =senderId;
        this.message=message;
    }

    public void setUid(String uid) {
        this.Uid = uid;
    }

    public String getReceiverImage() {
        return receiverImage;
    }

    public void setReceiverImage(String receiverImage) {
        this.receiverImage = receiverImage;
    }

    public String getUid() {
        return Uid;
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

    public message(){

    }
}
