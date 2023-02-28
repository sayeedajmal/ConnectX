package com.Strong.personalchat.Activity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import androidx.appcompat.app.AppCompatActivity;

import com.Strong.personalchat.databinding.ActivityUserDataImageBinding;
import com.bumptech.glide.Glide;

public class userDataImage extends AppCompatActivity {
    private ScaleGestureDetector scaleGestureDetector;
    private float ScaleFactor = 1.0f;

    ActivityUserDataImageBinding BindImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindImage = ActivityUserDataImageBinding.inflate(getLayoutInflater());
        setContentView(BindImage.getRoot());

        String image = getIntent().getStringExtra("Image");

        Glide.with(this).load(image).into(BindImage.Image);


        BindImage.backButton.setOnClickListener(view -> {
            onBackPressed();
        });

        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        scaleGestureDetector.onTouchEvent(motionEvent);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            ScaleFactor *= scaleGestureDetector.getScaleFactor();
            ScaleFactor = Math.max(0.1f, Math.min(ScaleFactor, 10.0f));
            BindImage.Image.setScaleX(ScaleFactor);
            BindImage.Image.setScaleY(ScaleFactor);
            return true;
        }
    }
}