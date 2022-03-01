package com.Strong.personalchat;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
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
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class mainChat extends BaseActivity {
    FirebaseAuth fAuth;
    FirebaseDatabase database;
    ActivityMainChatBinding BindMainChat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindMainChat=ActivityMainChatBinding.inflate(getLayoutInflater());
        setContentView(BindMainChat.getRoot());


        fAuth=FirebaseAuth.getInstance();

        final String senderId=fAuth.getUid();

        //RECEIVING THE DATA OF USER FROM NEW CHAT ADAPTOR
        String receiveId=getIntent().getStringExtra("userId");
        String receiveName=getIntent().getStringExtra("username");
        String uri=getIntent().getStringExtra("newChatUserImage");
        BindMainChat.mainChatUsername.setText(receiveName);
        Picasso.get().load(uri).into(BindMainChat.mainChatImage);

        final ArrayList<message> messageModels=new ArrayList<>();

        final messageAdaptor messageAdaptor=new messageAdaptor(messageModels, this);
            int count=messageModels.size();
        database=FirebaseDatabase.getInstance();

        //Showing Messages
        assert senderId != null;
        database.getReference().child("Users").child("Chats").child(senderId).child(receiveId).addValueEventListener(new ValueEventListener() {
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
            database=FirebaseDatabase.getInstance();
            String message= Objects.requireNonNull(BindMainChat.TypeMessage.getText()).toString();
            if (!message.equals("")){
                final message model=new message(senderId, message);
                model.setTimeStamp(new Date().getTime());
                BindMainChat.TypeMessage.setText(null);

                database.getReference().
                        child("Users").
                        child("Chats").
                        child(senderId).
                        child(receiveId).
                        push().setValue(model).addOnSuccessListener(unused -> database.getReference().
                        child("Users").
                        child("Chats").
                        child(receiveId).
                        child(senderId).
                        push().setValue(model));
            }
        });

        BindMainChat.mainchatbackButton.setOnClickListener(view -> onBackPressed());

    }
}