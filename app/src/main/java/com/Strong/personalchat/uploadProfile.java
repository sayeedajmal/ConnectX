package com.Strong.personalchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class uploadProfile extends AppCompatActivity {

    AppCompatButton uploadProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        uploadProfile=findViewById(R.id.uploadProfile);

        uploadProfile.setOnClickListener(view -> {
            Intent intent=new Intent(uploadProfile.this, verifyNumber.class);
            startActivity(intent);
        });
    }
}