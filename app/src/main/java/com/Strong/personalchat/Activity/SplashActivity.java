package com.Strong.personalchat.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.Strong.personalchat.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        auth = FirebaseAuth.getInstance();
        new Handler().postDelayed(() -> {
            if (auth.getUid() == null) {
                startActivity(new Intent(SplashActivity.this, purposeActivity.class));
            } else {
                startActivity(new Intent(SplashActivity.this, recentActivity.class));
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }, 100);
    }
}