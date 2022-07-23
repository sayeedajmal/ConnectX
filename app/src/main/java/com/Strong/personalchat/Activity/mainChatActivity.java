package com.Strong.personalchat.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.Strong.personalchat.Adaptors.messageAdaptor;
import com.Strong.personalchat.databinding.ActivityMainChatBinding;
import com.Strong.personalchat.models.message;
import com.Strong.personalchat.Utilities.status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;


public class mainChatActivity extends status {
    FirebaseAuth fAuth;
    String MineId;
    String YourID;
    FirebaseDatabase database;
    ActivityMainChatBinding BindMainChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindMainChat = ActivityMainChatBinding.inflate(getLayoutInflater());
        setContentView(BindMainChat.getRoot());

        YourID = getIntent().getStringExtra("userId");
        fAuth = FirebaseAuth.getInstance();
        MineId = fAuth.getUid();
        String username = getIntent().getStringExtra("username");
        String chatUserImage = getIntent().getStringExtra("UserImage");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().
                child("Users").
                child(YourID).child("Chats").child(MineId);


        final HashMap<String, Object> hashmap = new HashMap<>();


        //RECEIVING THE DATA OF USER FROM NEW CHAT ADAPTOR


        BindMainChat.mainChatUsername.setText(username);
        Picasso.get().load(chatUserImage).into(BindMainChat.mainChatImage);

        final ArrayList<message> messageModels = new ArrayList<>();

        final messageAdaptor messageAdaptor = new messageAdaptor(messageModels, this);
        int count = messageModels.size();
        database = FirebaseDatabase.getInstance();

        //Showing Status
        database.getReference().child("Users").child(YourID).addValueEventListener(new ValueEventListener() {
            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (Objects.requireNonNull(dataSnapshot.getKey()).equals("status")) {
                        String status = dataSnapshot.getValue(String.class);
                        assert status != null;
                        if (status.equals("online")) {
                            BindMainChat.ActiveStatus.setText("Active Now");
                        } else {
                            BindMainChat.ActiveStatus.setText("");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //SHOWING TYPING
        database.getReference().child("Users").child(MineId).child("Chats").child(YourID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (Objects.requireNonNull(dataSnapshot.getKey()).equals("Typing")) {
                        String typing = dataSnapshot.getValue(String.class);
                        assert typing != null;
                        if (typing.length() != 0) {
                            BindMainChat.ActiveStatus.setVisibility(View.GONE);
                            BindMainChat.TypingStatus.setVisibility(View.VISIBLE);
                        } else {
                            BindMainChat.TypingStatus.setVisibility(View.GONE);
                            BindMainChat.ActiveStatus.setVisibility(View.VISIBLE);
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Showing Messages
        assert MineId != null;
        database.getReference().child("Users").child(MineId).child("Chats").child(YourID).addValueEventListener(new ValueEventListener() {

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageModels.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (!Objects.requireNonNull(dataSnapshot.getKey()).equals("Typing")) {
                        message model = dataSnapshot.getValue(message.class);
                        messageModels.add(model);
                        if (count == 0) {
                            messageAdaptor.notifyDataSetChanged();
                        } else {
                            // Getting Shown the last message when open the chat section
                            messageAdaptor.notifyItemRangeChanged(messageModels.size(), messageModels.size());
                            BindMainChat.mainChatRecyclerView.smoothScrollToPosition(messageModels.size() - 1);
                        }
                    }
                    BindMainChat.mainChatRecyclerView.setAdapter(messageAdaptor);
                }
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                messageAdaptor.notifyDataSetChanged();
            }

        });

        //Sending the message and storing in the database
        BindMainChat.sendButton.setOnClickListener(view -> {
            String message = Objects.requireNonNull(BindMainChat.TypeMessage.getText()).toString();
            if (!message.equals("")) {
                final message conversation = new message(MineId, message);
                conversation.setTimeStamp(new Date().getTime());
                BindMainChat.TypeMessage.setText(null);

                // Feeding Message to Sender and Receiver Database
                database.getReference().
                        child("Users").
                        child(MineId).
                        child("Chats").
                        child(YourID).
                        push().setValue(conversation).addOnSuccessListener(e -> database.getReference().
                                child("Users").
                                child(YourID).
                                child("Chats").
                                child(MineId).
                                push().setValue(conversation)
                        );
            }
        });

        // MESSAGE TYPING SHOW TYPING ON ACTIVE STATUS OPTION
        BindMainChat.TypeMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) {
                    hashmap.put("Typing", charSequence.toString());
                } else {
                    hashmap.put("Typing", "");
                }
                reference.updateChildren(hashmap);
                reference.keepSynced(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        BindMainChat.sendButton.setOnLongClickListener(view -> {
            Toast.makeText(this, "Long Clicked", Toast.LENGTH_SHORT).show();
            return false;
        });

        BindMainChat.mainchatbackButton.setOnClickListener(view -> {
            onBackPressed();
            BindMainChat.TypeMessage.setText(null);
        });

        BindMainChat.constraint.setOnClickListener(view -> {
            Intent intent = new Intent(this, UserDataShow.class);
            intent.putExtra("Image", chatUserImage);
            intent.putExtra("username", username);
            startActivity(intent);
        });
        database.getReference().keepSynced(true);

    }

    @Override
    protected void onPause() {
        super.onPause();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().
                child("Users").
                child(YourID).child("Chats").child(MineId);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Typing", "");
        reference.updateChildren(hashMap);
        reference.keepSynced(true);
    }
}