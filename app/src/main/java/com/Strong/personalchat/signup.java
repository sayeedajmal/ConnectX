package com.Strong.personalchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.Strong.personalchat.models.*;

import java.util.Objects;

public class signup extends AppCompatActivity {

    AppCompatButton goLoginButton, ContinueToUploadImage;
    TextView chatUserMessage;
    AppCompatImageButton backButtonSignup;
    EditText signUsername, signEmail,signPassword ;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progressBar);
        goLoginButton=findViewById(R.id.goLoginButton);
        ContinueToUploadImage=findViewById(R.id.ContinueToUploadImage);
        backButtonSignup=findViewById(R.id.backButtonSignup);

        backButtonSignup.setOnClickListener(view ->{
            onBackPressed();
        });

        goLoginButton.setOnClickListener(view -> {
                Intent intent = new Intent(signup.this, login.class);
                startActivity(intent);
        });

        ContinueToUploadImage.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);

            signUsername=findViewById(R.id.signUsername);
            signEmail=findViewById(R.id.signEmail);
            signPassword=findViewById(R.id.signPassword);
            chatUserMessage=findViewById(R.id.chatLastMessage);

            String username=signUsername.getText().toString();
            String email=signEmail.getText().toString();
            String password=signPassword.getText().toString();

            if (TextUtils.isEmpty(username)) {
                Toast.makeText(getApplicationContext(),
                        "Please enter Username.",
                        Toast.LENGTH_LONG)
                        .show();
                progressBar.setVisibility(View.GONE);
                return;
            }
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getApplicationContext(),
                        "Please enter email.",
                        Toast.LENGTH_LONG)
                        .show();
                progressBar.setVisibility(View.GONE);
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(getApplicationContext(),
                        "Please enter password.",
                        Toast.LENGTH_LONG)
                        .show();
                progressBar.setVisibility(View.GONE);
                return;
            }

            String status="offline";
            // create new user or register new user
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    primaryGetter storeData=new primaryGetter(signUsername.getText().toString(), signEmail.getText().toString(), signPassword.getText().toString(),status);
                    String id= Objects.requireNonNull(task.getResult().getUser()).getUid();
                    // Storing Data to Database..
                    FirebaseDatabase database=FirebaseDatabase.getInstance();
                    database.getReference().child("Users").child(id).setValue(storeData);

                    Toast.makeText(signup.this, "Upload Your Profile Pic!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(signup.this,uploadProfile.class);
                    //Sending UerID to UploadProfile Class
                    intent.putExtra("userId", id);
                    startActivity(intent);
                }
                else {
                    // Registration failed
                    Toast.makeText(signup.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    // hide the progress bar
                    progressBar.setVisibility(View.GONE);
                }
            });
        });
    }
}
