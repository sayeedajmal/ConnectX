package com.Strong.personalchat.Utilities;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class status extends AppCompatActivity {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().
            child("Users").
            child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid());

    HashMap<String, Object> hashmap = new HashMap<>();

    private void Available(String status) {
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
        Available("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        hashmap.put("searchUser", "");
        reference.updateChildren(hashmap);
        reference.keepSynced(true);
        Available("offline");
    }

    public void OnTyping(String YourID, String MineId) {
       /* ;*/
    }

    public void onLogout() {
        hashmap.put("status", "offline");
        reference.updateChildren(hashmap);
        reference.keepSynced(true);
    }
}
