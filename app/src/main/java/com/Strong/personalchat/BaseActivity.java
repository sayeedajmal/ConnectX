package com.Strong.personalchat;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;


public class BaseActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    DatabaseReference reference;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

   /* private void status(String status){
        firebaseAuth= FirebaseAuth.getInstance();
        reference= FirebaseDatabase.getInstance().getReference().child("Users").child(Objects.requireNonNull(firebaseAuth.getCurrentUser().getUid()));
        HashMap<String, Object> hashmap=new HashMap<>();
        hashmap.put("status", status);
        reference.updateChildren(hashmap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    } */
}
