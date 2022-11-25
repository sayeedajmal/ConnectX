package com.Strong.personalchat.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.Strong.personalchat.Utilities.Constants;
import com.Strong.personalchat.databinding.ActivitySignupBinding;

import java.util.Objects;

public class signupActivity extends AppCompatActivity {
    ActivitySignupBinding BindSignup;
    String username, email, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindSignup = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(BindSignup.getRoot());

        BindSignup.backButtonSignup.setOnClickListener(view -> {
            onBackPressed();
            finish();
        });

        BindSignup.goLoginButton.setOnClickListener(view -> {
            Intent intent = new Intent(signupActivity.this, loginActivity.class);
            startActivity(intent);
        });

        signUp();
    }

    private void signUp() {
        BindSignup.SignUp.setOnClickListener(view -> {
            hideKeyboard();
            username = Objects.requireNonNull(BindSignup.signUsername.getText()).toString().trim();
            email = Objects.requireNonNull(BindSignup.signEmail.getText()).toString().trim();
            pass = Objects.requireNonNull(BindSignup.signPassword.getText()).toString().trim();

            if (TextUtils.isEmpty(username)) {
                showToast("Please enter Username.");
                return;

            } else if (TextUtils.isEmpty(email)) {
                showToast("Please enter email.");
                return;
            }
            if (TextUtils.isEmpty(pass)) {
                showToast("Please enter password.");
                return;
            }

            Intent intent = new Intent(signupActivity.this, uploadProfileActivity.class);
            intent.putExtra(Constants.KEY_USERNAME, username);
            intent.putExtra(Constants.KEY_EMAIL, email);
            intent.putExtra(Constants.KEY_PASSWORD, pass);
            startActivity(intent);

        });
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
