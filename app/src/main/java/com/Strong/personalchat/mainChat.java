package com.Strong.personalchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.Strong.personalchat.Adaptors.messageAdaptor;
import com.Strong.personalchat.databinding.ActivityMainChatBinding;
import com.Strong.personalchat.models.message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;


public class mainChat extends status {
    FirebaseAuth fAuth;
    String MineId;
    String YourID;
    FirebaseDatabase database;
    ActivityMainChatBinding BindMainChat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindMainChat=ActivityMainChatBinding.inflate(getLayoutInflater());
        setContentView(BindMainChat.getRoot());


        fAuth=FirebaseAuth.getInstance();

        MineId=fAuth.getUid();
        //RECEIVING THE DATA OF USER FROM NEW CHAT ADAPTOR
        YourID=getIntent().getStringExtra("userId");
        String receiveName=getIntent().getStringExtra("username");
        String chatUserImage=getIntent().getStringExtra("newChatUserImage");

        BindMainChat.mainChatUsername.setText(receiveName);
        Picasso.get().load(chatUserImage).into(BindMainChat.mainChatImage);

        final ArrayList<message> messageModels=new ArrayList<>();

        final messageAdaptor messageAdaptor=new messageAdaptor(messageModels, this);
            int count=messageModels.size();
        database=FirebaseDatabase.getInstance();

        //Showing Status
        database.getReference().child("Users").child(YourID).addValueEventListener(new ValueEventListener() {
            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                   if (Objects.requireNonNull(dataSnapshot.getKey()).equals("status")){
                       String status=dataSnapshot.getValue(String.class);
                       assert status != null;
                       if (status.equals("online")){
                       BindMainChat.ActiveStatus.setText("Active Now");
                       }else{
                           BindMainChat.ActiveStatus.setText("");
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
                for (DataSnapshot dataSnapshot1: snapshot.getChildren()){
                    message model=dataSnapshot1.getValue(message.class);
                    messageModels.add(model);
                    if (count==0){
                        messageAdaptor.notifyDataSetChanged();
                    }else{
                        // Getting Shown the last message when open the chat section
                        messageAdaptor.notifyItemRangeChanged(messageModels.size(), messageModels.size());
                        BindMainChat.mainChatRecyclerView.smoothScrollToPosition(messageModels.size()-1);
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
            String message= Objects.requireNonNull(BindMainChat.TypeMessage.getText()).toString();
            if (!message.equals("")){
                final message conversation=new message(MineId, message);
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

        BindMainChat.sendButton.setOnLongClickListener(view ->{
            Toast.makeText(this, "Long Clicked", Toast.LENGTH_SHORT).show();
            return false;
        });

        BindMainChat.mainchatbackButton.setOnClickListener(view -> onBackPressed());

        database.getReference().keepSynced(true);
    }

}