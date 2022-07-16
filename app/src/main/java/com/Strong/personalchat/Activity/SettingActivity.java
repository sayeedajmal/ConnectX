package com.Strong.personalchat.Activity;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.Strong.personalchat.databinding.ActivitySettingBinding;
import com.Strong.personalchat.status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class SettingActivity extends status {
    ActivitySettingBinding BindSet;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindSet=ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(BindSet.getRoot());

         firebaseAuth= FirebaseAuth.getInstance();

        BindSet.logoutButton.setOnClickListener(view -> {
            firebaseAuth.signOut();
            Toast.makeText(this, "SignOut", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, purposeActivity.class));
        });

        FirebaseDatabase database=FirebaseDatabase.getInstance();

        //SETTING IMAGE OF USER
        database.getReference().child("Users").child(Objects.requireNonNull(firebaseAuth.getUid())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            if(Objects.equals(dataSnapshot.getKey(), "chatUserImage")){
                            Picasso.get().load(dataSnapshot.getValue(String.class)).into(BindSet.SettingUserImage);
                            }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        BindSet.backButton.setOnClickListener(view -> onBackPressed());
    }
}