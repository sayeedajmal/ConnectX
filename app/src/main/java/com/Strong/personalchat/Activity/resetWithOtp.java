package com.Strong.personalchat.Activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.Strong.personalchat.databinding.ActivityResetWithOtpBinding;
import com.google.android.material.snackbar.Snackbar;

public class resetWithOtp extends AppCompatActivity {
    ActivityResetWithOtpBinding BindOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindOtp = ActivityResetWithOtpBinding.inflate(getLayoutInflater());
        String resetEmail=getIntent().getStringExtra("resetEmail");
        setContentView(BindOtp.getRoot());
        sendingRecovery();
        Snackbar.make(BindOtp.ResetButton, "We have sent the password recovery to your email", Snackbar.LENGTH_SHORT).show();
    }
    private void sendingRecovery(){

    }
}