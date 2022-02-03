package com.Strong.personalchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {

    AppCompatButton loginButton,goSignupButton;
    ProgressBar progressbar;
    FirebaseAuth auth;
    EditText getEmail,getPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton=findViewById(R.id.loginButton);
        goSignupButton=findViewById(R.id.goSignupButton);
        progressbar=findViewById(R.id.progressbar);

        getEmail=findViewById(R.id.getEmail);
        getPassword=findViewById(R.id.getPassword);
        auth=FirebaseAuth.getInstance();


        loginButton.setOnClickListener(view -> {
                progressbar.setVisibility(View.VISIBLE);
                String email=getEmail.getText().toString();
                String password=getPassword.getText().toString();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                Toast.makeText(login.this, "Fill Email & Password Correctly", Toast.LENGTH_SHORT).show();
                progressbar.setVisibility(View.GONE);
            }else {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(login.this, "Logged Successfully", Toast.LENGTH_SHORT).show();
                        progressbar.setVisibility(View.GONE);
                        Intent intent = new Intent(login.this, dashboard.class);
                        startActivity(intent);
                        finish();
                    } else {
                        progressbar.setVisibility(View.GONE);
                        Toast.makeText(login.this, "Invalid UserName or Password", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        goSignupButton.setOnClickListener(view -> {
            Intent intent=new Intent(login.this, signup.class);
            startActivity(intent);
        });
    }
}