package com.Strong.ConnectX.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.Strong.ConnectX.databinding.ActivityUserDataShowBinding;
import com.bumptech.glide.Glide;

public class UserDataShow extends AppCompatActivity {
    ActivityUserDataShowBinding BindDataShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindDataShow = ActivityUserDataShowBinding.inflate(getLayoutInflater());
        setContentView(BindDataShow.getRoot());

        String username = getIntent().getStringExtra("username");
        String userImage = getIntent().getStringExtra("Image");

        BindDataShow.userDataName.setText(username);
        Glide.with(this).load(userImage).into(BindDataShow.userDataImage);
        BindDataShow.backButton.setOnClickListener(view -> onBackPressed());

        userImageShow(userImage);
        videoChatButton(username, userImage);
    }

    private void videoChatButton(String userName, String userImage) {
        BindDataShow.userDataVideoCall.setOnClickListener(v -> {
            Intent intent = new Intent(this, VideoCallOutgoing.class);
            intent.putExtra("OutName", userName);
            intent.putExtra("OutImage", userImage);
            startActivity(intent);
        });

    }

    private void userImageShow(String userImage) {
        BindDataShow.userDataImage.setOnClickListener(view -> {
            Intent intent = new Intent(this, userDataImage.class);
            intent.putExtra("Image", userImage);
            startActivity(intent);
        });
    }
}