package com.Strong.personalchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.Strong.personalchat.databinding.ActivityVerifyNumberBinding;

public class verifyNumber extends AppCompatActivity {

   ActivityVerifyNumberBinding BindVerifyNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindVerifyNumber=ActivityVerifyNumberBinding.inflate(getLayoutInflater());
        setContentView(BindVerifyNumber.getRoot());



        BindVerifyNumber.sendOtp.setOnClickListener(view ->{
            BindVerifyNumber.sendOtp.setEnabled(false);
            BindVerifyNumber.MobileNumber.setEnabled(false);
            BindVerifyNumber.OtpVerification.setVisibility(View.VISIBLE);
            BindVerifyNumber.OtpVerificationButton.setVisibility(View.VISIBLE);
        });
        BindVerifyNumber.OtpVerificationButton.setOnClickListener(view ->{
            Intent intent=new Intent(verifyNumber.this, dashboard.class);
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}