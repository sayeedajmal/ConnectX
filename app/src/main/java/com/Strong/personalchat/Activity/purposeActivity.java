package com.Strong.personalchat.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

;import com.Strong.personalchat.databinding.ActivityPurposeBinding;

public class purposeActivity extends AppCompatActivity {

    ActivityPurposeBinding BindPurpose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindPurpose = ActivityPurposeBinding.inflate(getLayoutInflater());

        setContentView(BindPurpose.getRoot());

        BindPurpose.goLogin.setOnClickListener(view -> {
            Intent intent = new Intent(purposeActivity.this, loginActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });

        BindPurpose.goSignup.setOnClickListener(view -> {
            Intent intent = new Intent(purposeActivity.this, signupActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}