package com.Strong.personalchat.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.Strong.personalchat.databinding.ActivityResetPasswordBinding;

public class resetPasswordActivity extends AppCompatActivity {
    ActivityResetPasswordBinding BindReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindReset = ActivityResetPasswordBinding.inflate(getLayoutInflater());
        sendLinkReset();
        setContentView(BindReset.getRoot());
    }

    private void sendLinkReset() {
        BindReset.ResetButton.setOnClickListener(v -> {
            String resetEmail = BindReset.resetEmail.getText().toString();
            Intent intent = new Intent(this, resetWithOtp.class);
            if (!TextUtils.isEmpty(resetEmail)) {
                intent.putExtra("resetEmail", resetEmail);
                startActivity(intent);
            } else Toast.makeText(this, "Enter Your Email Address", Toast.LENGTH_SHORT).show();
        });
    }
}