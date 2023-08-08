package com.Strong.ConnectX.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    SharedPreferences sharedPreferences;

    public SharedPref(Context context) {
        sharedPreferences = context.getSharedPreferences("ConnectX", Context.MODE_PRIVATE);
    }

    public void SharedSave(String Username, String UID, String chatUserImage, String Email) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.KEY_USERNAME, Username);
        editor.putString(Constants.CHAT_USER_IMAGE, chatUserImage);
        editor.putString(Constants.KEY_EMAIL, Email);
        editor.putString(Constants.KEY_ID, UID);
        editor.apply();
    }
}
