package com.Strong.ConnectX.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.Strong.ConnectX.databinding.ActivitySplashBinding;
import com.google.firebase.auth.FirebaseAuth;


@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    FirebaseAuth auth;
    ActivitySplashBinding BindSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindSplash = ActivitySplashBinding.inflate(getLayoutInflater());
        auth = FirebaseAuth.getInstance();

        if (auth.getUid() == null) {
            startActivity(new Intent(SplashActivity.this, purposeActivity.class));
        } else startActivity(new Intent(SplashActivity.this, recentActivity.class));
        finish();
    }
}