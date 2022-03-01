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

    public void setCallUsername(String callUsername) {
        this.callUsername = callUsername;
    }

    public void setCallInform(String callInform) {
        this.callInform = callInform;
    }

    public void setCallUserImage(String callUserImage) {
        this.callUserImage = callUserImage;
    }

    public callsGetter(){

    }

}
