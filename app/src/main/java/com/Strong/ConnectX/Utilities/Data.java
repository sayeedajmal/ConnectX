package com.Strong.ConnectX.Utilities;

public class Data {
    private String sendName, SendMessage, SendImage, SendID;

    public Data(String sendName, String sendMessage, String sendImage, String sendID) {
        this.sendName = sendName;
        SendMessage = sendMessage;
        SendImage = sendImage;
        SendID = sendID;
    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public String getSendMessage() {
        return SendMessage;
    }

    public void setSendMessage(String sendMessage) {
        SendMessage = sendMessage;
    }

    public String getSendImage() {
        return SendImage;
    }

    public void setSendImage(String sendImage) {
        SendImage = sendImage;
    }

    public String getSendID() {
        return SendID;
    }

    public void setSendID(String sendID) {
        SendID = sendID;
    }
}
