package com.Strong.personalchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class verifyNumber extends AppCompatActivity {

    AppCompatButton sendOtp, OtpVerificationButton;
    EditText MobileNumber, OtpVerification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_number);


        OtpVerificationButton=findViewById(R.id.OtpVerificationButton);
        sendOtp=findViewById(R.id.sendOtp);
        MobileNumber=findViewById(R.id.MobileNumber);
        OtpVerification=findViewById(R.id.OtpVerification);

        sendOtp.setOnClickListener(view ->{
            sendOtp.setEnabled(false);
            MobileNumber.setEnabled(false);
            OtpVerification.setVisibility(View.VISIBLE);
            OtpVerificationButton.setVisibility(View.VISIBLE);
        });
        OtpVerificationButton.setOnClickListener(view ->{
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