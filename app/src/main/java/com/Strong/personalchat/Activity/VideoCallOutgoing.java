package com.Strong.personalchat.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.Strong.personalchat.databinding.ActivityVideoCallOutgoingBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class VideoCallOutgoing extends AppCompatActivity {
    ActivityVideoCallOutgoingBinding BindOut;
    DatabaseReference reference;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindOut = ActivityVideoCallOutgoingBinding.inflate(getLayoutInflater());
        setContentView(BindOut.getRoot());

        String Username = getIntent().getStringExtra("OutName");
        String Rec_Uid = getIntent().getStringExtra("Uid");
        String UserImage = getIntent().getStringExtra("OutImage");

        Picasso.get().load(UserImage).into(BindOut.outImage);
        BindOut.outName.setText(Username);

        database = FirebaseDatabase.getInstance();

        // reference = database.getReference("Users").child(Rec_Uid);

        BindOut.outDrop.setOnClickListener(view -> finish());
    }
}