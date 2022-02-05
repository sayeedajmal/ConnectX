package com.Strong.personalchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;;import com.Strong.personalchat.databinding.ActivityNewchatBinding;
import com.Strong.personalchat.databinding.ActivityPurposeBinding;

public class purpose extends AppCompatActivity {

    ActivityPurposeBinding BindPurpose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindPurpose= ActivityPurposeBinding.inflate(getLayoutInflater());

        setContentView(BindPurpose.getRoot());

        BindPurpose.goLogin.setOnClickListener(view -> {
            Intent intent=new Intent(purpose.this, login.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });

        BindPurpose.goSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(purpose.this, signup.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}