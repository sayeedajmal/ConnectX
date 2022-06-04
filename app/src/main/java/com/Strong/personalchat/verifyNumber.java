/*
package com.Strong.personalchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.Strong.personalchat.databinding.ActivityVerifyNumberBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class verifyNumber extends AppCompatActivity {

   ActivityVerifyNumberBinding BindVerifyNumber;
   FirebaseAuth auth;
   Editable phoneNumber=BindVerifyNumber.MobileNumber.getText();
   Editable OtpVerification=BindVerifyNumber.OtpVerification.getText();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindVerifyNumber=ActivityVerifyNumberBinding.inflate(getLayoutInflater());
        setContentView(BindVerifyNumber.getRoot());


        */
/*SEND OTP BUTTON FOR sending otp*//*

        BindVerifyNumber.sendOtp.setOnClickListener(view ->{
         if(phoneNumber.toString().length()!=10 && TextUtils.isEmpty(phoneNumber.toString())){
                Toast.makeText(this, "Enter 10 digit number", Toast.LENGTH_SHORT).show();
            }
            BindVerifyNumber.sendOtp.setVisibility(View.INVISIBLE);
            BindVerifyNumber.MobileNumber.setEnabled(false);
            BindVerifyNumber.OtpVerification.setVisibility(View.VISIBLE);
            BindVerifyNumber.OtpVerificationButton.setVisibility(View.VISIBLE);
        });
        BindVerifyNumber.OtpVerificationButton.setOnClickListener(view ->{
            Intent intent=new Intent(verifyNumber.this, recent.class);
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    */
/*SEND OTP TO A PARTICULAR NUMBER FOR VERIFICATION*//*

    public void sendOtp(){
        PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + phoneNumber.toString(), 60, TimeUnit.SECONDS, this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onCodeSent(@NonNull String VerificationID, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken){
                super.onCodeSent(VerificationID, forceResendingToken);
                if (OtpVerification.toString().isEmpty()){
                    Toast.makeText(verifyNumber.this, "Enter Verification Code", Toast.LENGTH_SHORT).show();
                    BindVerifyNumber.progressBar.setVisibility(View.VISIBLE);

                    PhoneAuthCredential credential=PhoneAuthProvider.getCredential(VerificationID, OtpVerification.toString());
                    auth=FirebaseAuth.getInstance();
                    auth.signInWithCredential(credential).addOnCompleteListener(task -> {
                        if (task.isSuccessful()){

                        }
                    });
                }
            }
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            }
            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(verifyNumber.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                BindVerifyNumber.sendOtp.setVisibility(View.VISIBLE);
                BindVerifyNumber.MobileNumber.setEnabled(true);
                BindVerifyNumber.OtpVerification.setVisibility(View.INVISIBLE);
                BindVerifyNumber.OtpVerificationButton.setVisibility(View.INVISIBLE);
            }
        });

    }
}*/
