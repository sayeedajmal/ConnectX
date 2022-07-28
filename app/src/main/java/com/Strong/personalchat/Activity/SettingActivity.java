package com.Strong.personalchat.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.Strong.personalchat.BuildConfig;
import com.Strong.personalchat.Utilities.status;
import com.Strong.personalchat.databinding.ActivitySettingBinding;
import com.Strong.personalchat.models.CurrentUser;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class SettingActivity extends status {
    ActivitySettingBinding BindSet;
    FirebaseAuth firebaseAuth;
    status status;
    String UserImage;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindSet = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(BindSet.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        BindSet.logoutButton.setOnClickListener(view -> {
            status = new status();
            status.onLogout();
            firebaseAuth.signOut();
            Toast.makeText(this, "We Are Waiting For You..", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, purposeActivity.class));
        });

        UserImage = CurrentUser.getChatUserImage();
        Picasso.get().load(UserImage).into(BindSet.SettingUserImage);
        BindSet.Username.setText(CurrentUser.getUsername());

        BindSet.SettingUserImage.setOnClickListener(view -> {
            Intent intent = new Intent(this, userDataImage.class);
            intent.putExtra("Image", UserImage);
            startActivity(intent);
        });
        BindSet.backButton.setOnClickListener(view -> onBackPressed());

        BindSet.EditProfileButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, ChangeUserData.class);
            startActivity(intent);
        });

        BindSet.AppVersion.setText("App Version: " + BuildConfig.VERSION_NAME);
    }
}