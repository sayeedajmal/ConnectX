package com.Strong.personalchat.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.Strong.personalchat.databinding.ActivityChangeUserDataBinding;
import com.Strong.personalchat.databinding.ActivityPurposeBinding;

public class ChangeUserData extends AppCompatActivity {
    ActivityChangeUserDataBinding BindUserData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindUserData = ActivityChangeUserDataBinding.inflate(getLayoutInflater());
        setContentView(BindUserData.getRoot());

        BindUserData.backButton.setOnClickListener(view -> {
            onBackPressed();
        });
    }
}