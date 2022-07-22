package com.Strong.personalchat.Activity;

import android.content.Intent;
import android.os.Bundle;

import com.Strong.personalchat.databinding.ActivityUserDataShowBinding;
import com.Strong.personalchat.Utilities.status;
import com.squareup.picasso.Picasso;

public class UserDataShow extends status {
    ActivityUserDataShowBinding BindDataShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindDataShow = ActivityUserDataShowBinding.inflate(getLayoutInflater());
        setContentView(BindDataShow.getRoot());

        String username = getIntent().getStringExtra("username");
        String userImage = getIntent().getStringExtra("Image");

        BindDataShow.userDataName.setText(username);
        Picasso.get().load(userImage).into(BindDataShow.userDataImage);
        BindDataShow.backButton.setOnClickListener(view -> {
            onBackPressed();
        });

        BindDataShow.userDataImage.setOnClickListener(view -> {
            Intent intent = new Intent(this, userDataImage.class);
            intent.putExtra("Image", userImage);
            startActivity(intent);
        });
    }
}