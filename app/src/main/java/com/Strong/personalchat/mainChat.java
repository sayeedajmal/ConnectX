package com.Strong.personalchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.Strong.personalchat.Adaptors.messageAdaptor;
import com.Strong.personalchat.models.message;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.EventListener;
import java.util.Objects;

public class mainChat extends AppCompatActivity {

    TextView mainChatUsername;
    FirebaseAuth fAuth;
    RecyclerView mainChatRecyclerView;
    AppCompatEditText TypeMessage;
    FirebaseDatabase database;
    AppCompatImageButton sendButton, mainchatbackButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);

        mainChatUsername=findViewById(R.id.mainChatUsername);
        mainChatRecyclerView=findViewById(R.id.mainChatRecyclerView);
        TypeMessage=findViewById(R.id.TypeMessage);
        sendButton=findViewById(R.id.sendButton);
        mainchatbackButton=findViewById(R.id.mainchatbackButton);

        fAuth=FirebaseAuth.getInstance();

        final String senderId=fAuth.getUid();

        String receiveId=getIntent().getStringExtra("userId");
        String receiveName=getIntent().getStringExtra("username");


        mainChatUsername.setText(receiveName);

        final ArrayList<message> messageModels=new ArrayList<>();

        final messageAdaptor messageAdaptor=new messageAdaptor(messageModels, this);
            int count=messageModels.size();
        database=FirebaseDatabase.getInstance();

        database.getReference().child("Users").child(senderId).child(receiveId).addValueEventListener(new ValueEventListener() {
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
                        mainChatRecyclerView.smoothScrollToPosition(messageModels.size()-1);
                    }
                    mainChatRecyclerView.setAdapter(messageAdaptor);
                }
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                messageAdaptor.notifyDataSetChanged();
            }

        });
        //Sending the message and storing in the database
        sendButton.setOnClickListener(view -> {
            database=FirebaseDatabase.getInstance();

            String message= Objects.requireNonNull(TypeMessage.getText()).toString();
            if (!message.equals("")){
                final message model=new message(senderId, message);
                model.setTimeStamp(new Date().getTime());
                TypeMessage.setText(null);

                database.getReference().
                        child("Users").
                        child(senderId).
                        child(receiveId).
                        push().setValue(model).addOnSuccessListener(unused -> database.getReference().
                        child("Users").
                        child(receiveId).
                        child(senderId).
                        push().setValue(model));
            }
        });

        mainchatbackButton.setOnClickListener(view -> {
            onBackPressed();
        });
    }
}