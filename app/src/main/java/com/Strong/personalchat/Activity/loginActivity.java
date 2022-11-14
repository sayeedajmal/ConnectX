package com.Strong.personalchat.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.Strong.personalchat.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;

public class loginActivity extends AppCompatActivity {
    FirebaseAuth auth;
    ActivityLoginBinding BindLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindLogin = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(BindLogin.getRoot());

        auth = FirebaseAuth.getInstance();

        BindLogin.Login.setOnClickListener(view -> {
            BindLogin.progressbar.setVisibility(View.VISIBLE);
            BindLogin.Login.setVisibility(View.INVISIBLE);
            BindLogin.goSignupButton.setVisibility(View.INVISIBLE);

            String email = BindLogin.getEmail.getText().toString();
            String password = BindLogin.getPassword.getText().toString();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                showToast("Fill Email & Password Correctly");
                BindLogin.progressbar.setVisibility(View.GONE);
                BindLogin.Login.setVisibility(View.VISIBLE);
                BindLogin.goSignupButton.setVisibility(View.VISIBLE);

            } else {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        showToast("Logged Successfully");
                        BindLogin.progressbar.setVisibility(View.GONE);
                        BindLogin.Login.setVisibility(View.INVISIBLE);
                        BindLogin.goSignupButton.setVisibility(View.INVISIBLE);
                        Intent intent = new Intent(loginActivity.this, recentActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        BindLogin.progressbar.setVisibility(View.GONE);
                        BindLogin.Login.setVisibility(View.VISIBLE);
                        BindLogin.goSignupButton.setVisibility(View.VISIBLE);
                        showToast("Invalid UserName or Password");
                    }
                });
            }
        });

        BindLogin.goSignupButton.setOnClickListener(view -> {
            Intent intent = new Intent(loginActivity.this, signupActivity.class);
            startActivity(intent);
        });

        BindLogin.backButton.setOnClickListener(view -> onBackPressed());
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}