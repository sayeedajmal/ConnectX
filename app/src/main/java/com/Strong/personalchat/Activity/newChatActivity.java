package com.Strong.personalchat.Activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import com.Strong.personalchat.Fragments.newChatFragment;
import com.Strong.personalchat.Fragments.ViewPagerSection;
import com.Strong.personalchat.databinding.ActivityNewchatBinding;
import com.Strong.personalchat.Utilities.status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class newChatActivity extends status {
    ActivityNewchatBinding BindNewChat;
    ViewPagerSection viewPagerAdaptor;
    FirebaseDatabase database;
    HashMap<String, Object> hashmap = new HashMap<>();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().
            child("Users").
            child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindNewChat = ActivityNewchatBinding.inflate(getLayoutInflater());
        setContentView(BindNewChat.getRoot());

        database = FirebaseDatabase.getInstance();

        newChatFragment newChatFragment = new newChatFragment();
        viewPagerAdaptor = new ViewPagerSection(getSupportFragmentManager(), 0);
        viewPagerAdaptor.addFragment(newChatFragment, "");
        BindNewChat.newContactPager.setAdapter(viewPagerAdaptor);
        BindNewChat.chatBackButton.setOnClickListener(view -> onBackPressed());

        BindNewChat.SearchPerson.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                hashmap.put("searchUser", editable.toString());
                reference.updateChildren(hashmap);
                reference.keepSynced(true);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        BindNewChat.SearchPerson.setText(null);
        hashmap.put("searchUser", "");
        reference.updateChildren(hashmap);
        reference.keepSynced(true);
    }


}