package com.Strong.personalchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.Strong.personalchat.databinding.ActivitySignupBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.Strong.personalchat.models.*;

import java.util.Objects;

public class signup extends AppCompatActivity {
    ActivitySignupBinding BindSignup;
    private FirebaseAuth mAuth;

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


            String username=BindSignup.signUsername.getText().toString();
            String email=BindSignup.signEmail.getText().toString();
            String password=BindSignup.signPassword.getText().toString();

            if (TextUtils.isEmpty(username)) {
                Toast.makeText(getApplicationContext(),
                        "Please enter Username.",
                        Toast.LENGTH_LONG)
                        .show();
                BindSignup.progressBar.setVisibility(View.GONE);
                return;
            }
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getApplicationContext(),
                        "Please enter email.",
                        Toast.LENGTH_LONG)
                        .show();
                BindSignup.progressBar.setVisibility(View.GONE);
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(getApplicationContext(),
                        "Please enter password.",
                        Toast.LENGTH_LONG)
                        .show();
                BindSignup.progressBar.setVisibility(View.GONE);
                return;
            }

            String status="offline";
            // create new user or register new user
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    UserGetter storeData=new UserGetter(BindSignup.signUsername.getText().toString(), BindSignup.signEmail.getText().toString(), BindSignup.signPassword.getText().toString(),status);
                    String id= Objects.requireNonNull(task.getResult().getUser()).getUid();
                    // Storing Data to Database..
                    FirebaseDatabase database=FirebaseDatabase.getInstance();
                    database.getReference().child("Users").child(id).setValue(storeData);

                    Toast.makeText(signup.this, "Upload Your Profile Pic!", Toast.LENGTH_SHORT).show();
                    BindSignup.progressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(signup.this,uploadProfile.class);
                    //Sending UerID to UploadProfile Class
                    intent.putExtra("userId", id);
                    startActivity(intent);
                }
                else {
                    // Registration failed
                    Toast.makeText(signup.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    // hide the progress bar
                    BindSignup.progressBar.setVisibility(View.GONE);
                }
            });
        });
    }
}
