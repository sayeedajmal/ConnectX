package com.Strong.ConnectX.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.Strong.ConnectX.databinding.ActivityVideoCallOutgoingBinding;
import com.bumptech.glide.Glide;
import com.google.firebase.database.FirebaseDatabase;

public class VideoCallOutgoing extends AppCompatActivity {
    ActivityVideoCallOutgoingBinding BindOut;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindOut = ActivityVideoCallOutgoingBinding.inflate(getLayoutInflater());
        setContentView(BindOut.getRoot());

//        Getting Intent Extra from MainChatActivity
        String Username = getIntent().getStringExtra("OutName");
        String Rec_Uid = getIntent().getStringExtra("Uid");
        String UserImage = getIntent().getStringExtra("OutImage");

        Glide.with(this).load(UserImage).into(BindOut.outImage);
        BindOut.outName.setText(Username);

        database = FirebaseDatabase.getInstance();

        // reference = database.getReference("Users").child(Rec_Uid);

        BindOut.outDrop.setOnClickListener(view -> finish());
    }
}