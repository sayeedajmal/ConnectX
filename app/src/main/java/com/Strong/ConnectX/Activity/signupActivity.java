package com.Strong.ConnectX.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.Strong.ConnectX.Utilities.Constants;
import com.Strong.ConnectX.databinding.ActivitySignupBinding;

import java.util.Objects;
import java.util.regex.Pattern;

public class signupActivity extends AppCompatActivity {
    ActivitySignupBinding BindSignup;
    String username, email, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindSignup = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(BindSignup.getRoot());

        BindSignup.signUsername.requestFocus();
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
            if (!isValidPassword(pass)) {
                Toast.makeText(this, "Password doesn't match the rules.", Toast.LENGTH_LONG).show();
                return;
            }
            Intent intent = new Intent(signupActivity.this, uploadProfileActivity.class);
            intent.putExtra(Constants.KEY_USERNAME, username);
            intent.putExtra(Constants.KEY_EMAIL, email);
            intent.putExtra(Constants.KEY_PASSWORD, pass);
            startActivity(intent);

        });
    }

    Pattern lowerCase = Pattern.compile("^.*[a-z].*$");
    Pattern UpperCase = Pattern.compile("^.*[A-Z].*$");
    Pattern Numbers = Pattern.compile("^.*[0-9].*$");
    Pattern SpecialChar = Pattern.compile("^.*[a-zA-Z0-9].*$");

    private boolean isValidPassword(String Password) {
        if (Password.length() < 8) {
            BindSignup.passwordLayout.setError("Password must be upto 8 Characters");
            return false;
        }
        if (!lowerCase.matcher(Password).matches()) {
            BindSignup.passwordLayout.setError("Password must contain LowerCase letters");
            return false;
        }
        if (!UpperCase.matcher(Password).matches()) {
            BindSignup.passwordLayout.setError("Password must contain UpperCase letters");
            return false;
        }
        if (!Numbers.matcher(Password).matches()) {
            BindSignup.passwordLayout.setError("Password must contain Numbers");
            return false;
        }
        if (!SpecialChar.matcher(Password).matches()) {
            BindSignup.passwordLayout.setError("Password must contain Special Characters");
            return false;
        }
        return true;
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
