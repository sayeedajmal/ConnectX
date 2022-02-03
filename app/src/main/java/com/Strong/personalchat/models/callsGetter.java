package com.Strong.personalchat.models;

public class callsGetter {
    String callUsername;
    String callInform;
    String callUserImage;

    public String getCallUsername() {
        return callUsername;
    }

    public String getCallInform() {
        return callInform;
    }

    public String getCallUserImage() {
        return callUserImage;
    }

    public callsGetter(String callUsername, String callInform, String callUserImage) {
        this.callUsername = callUsername;
        this.callInform = callInform;
        this.callUserImage = callUserImage;
    }



}
