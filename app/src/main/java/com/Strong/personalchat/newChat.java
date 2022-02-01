package com.Strong.personalchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class newChat extends AppCompatActivity {

    ViewPager newContactPager;
    FirebaseAuth firebaseAuth;
    DatabaseReference reference;
    AppCompatImageButton chatBackButton;
    ViewPagerSection viewPagerAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newchat);

        newChatFragment newChatFragment=new newChatFragment();
        newContactPager=findViewById(R.id.newContactPager);
        viewPagerAdaptor=new ViewPagerSection(getSupportFragmentManager(), 0);
        viewPagerAdaptor.addFragment(newChatFragment,"");
        newContactPager.setAdapter(viewPagerAdaptor);

        chatBackButton=findViewById(R.id.chatBackButton);
        chatBackButton.setOnClickListener(view -> {
            onBackPressed();
        });
    }

    private void status(String status){
        firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid());
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
    }
}