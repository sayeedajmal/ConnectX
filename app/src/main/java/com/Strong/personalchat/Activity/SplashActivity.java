package com.Strong.personalchat.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.Strong.personalchat.databinding.ActivitySplashBinding;
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
        new Handler().postDelayed(() -> {
            if (auth.getUid() == null) {
                startActivity(new Intent(SplashActivity.this, purposeActivity.class));
            } else {
                startActivity(new Intent(SplashActivity.this, recentActivity.class));
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }, 300);
        setContentView(BindSplash.getRoot());
    }
}