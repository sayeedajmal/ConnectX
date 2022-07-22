package com.Strong.personalchat.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.Strong.personalchat.R;
import com.Strong.personalchat.databinding.ActivityUserDataImageBinding;
import com.Strong.personalchat.status;
import com.squareup.picasso.Picasso;

public class userDataImage extends status {

    ActivityUserDataImageBinding BindImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindImage = ActivityUserDataImageBinding.inflate(getLayoutInflater());
        setContentView(BindImage.getRoot());

        String image = getIntent().getStringExtra("Image");

        Picasso.get().load(image).into(BindImage.Image);


        BindImage.backButton.setOnClickListener(view -> {
            onBackPressed();
        });
    }
}