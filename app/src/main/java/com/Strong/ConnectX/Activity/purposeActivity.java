package com.Strong.ConnectX.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;

import com.Strong.ConnectX.databinding.ActivityPurposeBinding;


public class purposeActivity extends AppCompatActivity {

    ActivityPurposeBinding BindPurpose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindPurpose = ActivityPurposeBinding.inflate(getLayoutInflater());

        setContentView(BindPurpose.getRoot());

        BindPurpose.goLogin.setOnClickListener(view -> {
            hideKeyboard();
            Intent intent = new Intent(purposeActivity.this, loginActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });

        BindPurpose.goSignup.setOnClickListener(view -> {
            Intent intent = new Intent(purposeActivity.this, signupActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}