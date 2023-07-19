package com.Strong.ConnectX.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.Strong.ConnectX.databinding.ActivityLoginBinding;
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

        BindLogin.getEmail.requestFocus();
        BindLogin.Login.setOnClickListener(view -> {
            hideKeyboard();
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

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}