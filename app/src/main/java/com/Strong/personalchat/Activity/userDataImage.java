package com.Strong.personalchat.Activity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.Strong.personalchat.databinding.ActivityUserDataImageBinding;
import com.Strong.personalchat.Utilities.status;
import com.squareup.picasso.Picasso;

public class userDataImage extends status {
    private ScaleGestureDetector scaleGestureDetector;
    private float mScaleFactor = 1.0f;

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
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
            BindImage.Image.setScaleX(mScaleFactor);
            BindImage.Image.setScaleY(mScaleFactor);
            return true;
        }
    }
}