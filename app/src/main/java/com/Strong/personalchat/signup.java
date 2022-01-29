package com.Strong.personalchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.Strong.personalchat.models.*;

import de.hdodenhof.circleimageview.CircleImageView;

public class signup extends AppCompatActivity {

    AppCompatButton goLoginButton, Register;
    TextView chatUserMessage;
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
        Register=findViewById(R.id.Register);

        goLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(signup.this, login.class);
                    startActivity(intent);
            }
        });
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);


                signUsername=findViewById(R.id.signUsername);
                signEmail=findViewById(R.id.signEmail);
                signPassword=findViewById(R.id.signPassword);
                chatUserMessage=findViewById(R.id.chatUserMessage);

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

                // create new user or register new user
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            primaryGetter storeData=new primaryGetter(signUsername.getText().toString(), signEmail.getText().toString(), signPassword.getText().toString());
                            String id=task.getResult().getUser().getUid();
                            // Storing Data to Database..
                            FirebaseDatabase database=FirebaseDatabase.getInstance();
                            database.getReference().child("Users").child(id).setValue(storeData);

                            Toast.makeText(signup.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            Intent intent = new Intent(signup.this, dashboard.class);
                            startActivity(intent);
                        }
                        else {
                            // Registration failed
                            Toast.makeText(signup.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            // hide the progress bar
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent=new Intent(signup.this, purpose.class);
        startActivity(intent);
    }

    /* <de.hdodenhof.circleimageview.CircleImageView
        android:id="@+id/choose_ProfileImage"
        android:layout_width="100dp"
        android:layout_height="100dp"
        android:layout_marginTop="24dp"
        android:src="@mipmap/avtar"
        android:background="@drawable/image_corner"
        app:layout_constraintBottom_toTopOf="@+id/signUsername"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.498"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/textView3"
        app:layout_constraintVertical_bias="0.129"
        tools:ignore="MissingConstraints" />
 */
}
