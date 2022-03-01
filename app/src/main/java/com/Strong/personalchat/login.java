package com.Strong.personalchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.Strong.personalchat.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {
    FirebaseAuth auth;
    ActivityLoginBinding BindLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindLogin=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(BindLogin.getRoot());


        auth=FirebaseAuth.getInstance();


        BindLogin.loginButton.setOnClickListener(view -> {
            BindLogin.progressbar.setVisibility(View.VISIBLE);
            BindLogin.loginButton.setVisibility(View.INVISIBLE);
            BindLogin.goSignupButton.setVisibility(View.INVISIBLE);

            String email=BindLogin.getEmail.getText().toString();
                String password=BindLogin.getPassword.getText().toString();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                Toast.makeText(login.this, "Fill Email & Password Correctly", Toast.LENGTH_SHORT).show();
                BindLogin.progressbar.setVisibility(View.GONE);
                BindLogin.loginButton.setVisibility(View.VISIBLE);
                BindLogin.goSignupButton.setVisibility(View.VISIBLE);

            }else {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(login.this, "Logged Successfully", Toast.LENGTH_SHORT).show();
                        BindLogin.progressbar.setVisibility(View.GONE);
                        BindLogin.loginButton.setVisibility(View.INVISIBLE);
                        BindLogin.goSignupButton.setVisibility(View.INVISIBLE);
                        Intent intent = new Intent(login.this, recent.class);
                        startActivity(intent);
                        finish();
                    } else {
                        BindLogin.progressbar.setVisibility(View.GONE);
                        BindLogin.loginButton.setVisibility(View.VISIBLE);
                        BindLogin.goSignupButton.setVisibility(View.INVISIBLE);
                        Toast.makeText(login.this, "Invalid UserName or Password", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        BindLogin.goSignupButton.setOnClickListener(view -> {
            Intent intent=new Intent(login.this, signup.class);
            startActivity(intent);
        });
    }
}