package com.Strong.personalchat;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class status extends AppCompatActivity {
    private void status(String status){
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().
                child("Users").
                child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid());

        HashMap<String, Object> hashmap=new HashMap<>();
        hashmap.put("status", status);

        reference.updateChildren(hashmap);
        reference.keepSynced(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        onResume();
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
    }
}
