package com.Strong.personalchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.Strong.personalchat.databinding.ActivitySettingBinding;
import com.google.firebase.auth.FirebaseAuth;

public class Setting extends AppCompatActivity {
    ActivitySettingBinding BindSet;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindSet=ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(BindSet.getRoot());

        BindSet.logoutButton.setOnClickListener(view -> {
            firebaseAuth= FirebaseAuth.getInstance();
            firebaseAuth.signOut();
            Toast.makeText(this, "SignOut", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, purpose.class));
        });
    }
}