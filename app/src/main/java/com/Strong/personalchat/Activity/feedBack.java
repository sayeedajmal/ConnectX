package com.Strong.personalchat.Activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.Strong.personalchat.databinding.ActivityFeedBackBinding;
import com.Strong.personalchat.models.CurrentUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class feedBack extends AppCompatActivity {
    FirebaseDatabase reference;
    ActivityFeedBackBinding BindFeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindFeed = ActivityFeedBackBinding.inflate(getLayoutInflater());
        setContentView(BindFeed.getRoot());

        BindFeed.username.setText(CurrentUser.getUsername());
        BindFeed.backButton.setOnClickListener(v -> onBackPressed());

        reference = FirebaseDatabase.getInstance();
        BindFeed.sendFeed.setOnClickListener(v -> Send());
    }

    private void Send() {
        if (BindFeed.feedInput.getText().length() != 0) {
            List<String> list = new ArrayList<>();
            list.add(BindFeed.feedInput.getText().toString());
            reference.getReference().child("FeedBacks")
                    .child((String) BindFeed.username.getText()).setValue(list);
            Toast.makeText(this, "Thank You For Giving Valuable FeedBack.", Toast.LENGTH_SHORT).show();
            finish();
        }
        Toast.makeText(this, "We Can't Take Empty FeedBack.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}