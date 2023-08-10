package com.Strong.ConnectX.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.Strong.ConnectX.BuildConfig;
import com.Strong.ConnectX.databinding.ActivitySettingBinding;
import com.Strong.ConnectX.models.CurrentUser;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

public class SettingActivity extends AppCompatActivity {
    ActivitySettingBinding BindSet;
    FirebaseAuth firebaseAuth;
    String UserImage;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindSet = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(BindSet.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        BindSet.logoutButton.setOnClickListener(view -> {
            firebaseAuth.signOut();
            Toast.makeText(this, "SomeOne is Waiting For You..", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, purposeActivity.class));
        });

        UserImage = CurrentUser.getChatUserImage();
        Glide.with(this).load(UserImage).into(BindSet.SettingUserImage);
        BindSet.Username.setText(CurrentUser.getUsername());

        BindSet.SettingUserImage.setOnClickListener(view -> {
            Intent intent = new Intent(this, userDataImage.class);
            intent.putExtra("Image", UserImage);
            startActivity(intent);
        });
        BindSet.backButton.setOnClickListener(view -> onBackPressed());

        BindSet.EditProfileButton.setOnClickListener(view -> startActivity(new Intent(this, ChangeUserData.class)));

        BindSet.AppVersion.setText("App Version: " + BuildConfig.VERSION_NAME);

        BindSet.UpdateApp.setOnClickListener(view -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://sayeedthedev.web.app"))));
    }
}