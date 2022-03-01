package com.Strong.personalchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;

public class Splash extends AppCompatActivity {
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
            new Handler().postDelayed(() -> {
                auth=FirebaseAuth.getInstance();
                if (auth.getCurrentUser()!=null){
                    startActivity(new Intent(Splash.this, recent.class));
                }else{
                    startActivity(new Intent(Splash.this, purpose.class));
                }
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }, 180);
    }
}