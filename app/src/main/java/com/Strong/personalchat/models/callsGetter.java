package com.Strong.personalchat.models;

public class callsGetter {
    String callUsername;
    String callInform;
    String callUserImage;

    public String getCallUsername() {
        return callUsername;
    }

    public void setCallUsername(String callUsername) {
        this.callUsername = callUsername;
    }

    public String getCallInform() {
        return callInform;
    }

    public void setCallInform(String callInform) {
        this.callInform = callInform;
    }

    public String getCallUserImage() {
        return callUserImage;
    }

    public void setCallUserImage(String callUserImage) {
        this.callUserImage = callUserImage;
    }

    public callsGetter(String callUsername, String callInform, String callUserImage) {
        this.callUsername = callUsername;
        this.callInform = callInform;
        this.callUserImage = callUserImage;
    }



}
