package com.Strong.personalchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.Strong.personalchat.databinding.ActivitySignupBinding;
import com.google.firebase.auth.FirebaseAuth;

public class signup extends AppCompatActivity {
    ActivitySignupBinding BindSignup;
    private FirebaseAuth mAuth;
    String username, email, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindSignup=ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(BindSignup.getRoot());

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        BindSignup.backButtonSignup.setOnClickListener(view ->{
            onBackPressed();
        });

        BindSignup.goLoginButton.setOnClickListener(view -> {
                Intent intent = new Intent(signup.this, login.class);
                startActivity(intent);
        });

        BindSignup.ContinueToUploadImage.setOnClickListener(view -> {
            BindSignup.progressBar.setVisibility(View.VISIBLE);
            BindSignup.ContinueToUploadImage.setVisibility(View.GONE);
            BindSignup.goLoginButton.setVisibility(View.GONE);



            username=BindSignup.signUsername.getText().toString();
            email=BindSignup.signEmail.getText().toString();
            pass=BindSignup.signPassword.getText().toString();

            if (TextUtils.isEmpty(username)) {
                Toast.makeText(getApplicationContext(),
                        "Please enter Username.",
                        Toast.LENGTH_LONG)
                        .show();
                BindSignup.progressBar.setVisibility(View.GONE);
                BindSignup.ContinueToUploadImage.setVisibility(View.VISIBLE);
                BindSignup.goLoginButton.setVisibility(View.VISIBLE);
                return;
            }
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getApplicationContext(),
                        "Please enter email.",
                        Toast.LENGTH_LONG)
                        .show();
                BindSignup.progressBar.setVisibility(View.GONE);
                BindSignup.ContinueToUploadImage.setVisibility(View.VISIBLE);
                BindSignup.goLoginButton.setVisibility(View.VISIBLE);
                return;
            }
            if (TextUtils.isEmpty(pass)) {
                Toast.makeText(getApplicationContext(),
                        "Please enter password.",
                        Toast.LENGTH_LONG)
                        .show();
                BindSignup.progressBar.setVisibility(View.GONE);
                BindSignup.ContinueToUploadImage.setVisibility(View.VISIBLE);
                BindSignup.goLoginButton.setVisibility(View.VISIBLE);
                return;
            }
            // create new user or register new user
            mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(signup.this, "Upload Your Profile Pic!", Toast.LENGTH_SHORT).show();
                    BindSignup.progressBar.setVisibility(View.GONE);
                    BindSignup.ContinueToUploadImage.setVisibility(View.GONE);
                    BindSignup.goLoginButton.setVisibility(View.GONE);
                    String id=mAuth.getCurrentUser().getUid();


                    Intent intent = new Intent(signup.this,uploadProfile.class);

                    intent.putExtra("username", username);
                    intent.putExtra("email", email);
                    intent.putExtra("pass", pass);
                    intent.putExtra("userId", id);
                    intent.putExtra("status","offline");
                    startActivity(intent);
                    finish();
                }
                else {
                    // Registration failed
                    Toast.makeText(signup.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    // hide the progress bar
                    BindSignup.progressBar.setVisibility(View.GONE);
                    BindSignup.ContinueToUploadImage.setVisibility(View.VISIBLE);
                    BindSignup.goLoginButton.setVisibility(View.VISIBLE);
                }
            });
        });
    }
}
