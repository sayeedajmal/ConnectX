package com.Strong.ConnectX.Activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.Strong.ConnectX.databinding.ActivityUserDataImageBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;

import java.util.Date;

public class userDataImage extends AppCompatActivity {
    ActivityUserDataImageBinding BindImage;
    String time;
    private ScaleGestureDetector scaleGestureDetector;
    private float ScaleFactor = 1.0f;


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

        assert image != null;
        time= String.valueOf(new Date().getTime());
        BindImage.DownImage.setOnClickListener(v->{
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 105);
            } else {
                saveImage(image);
            }
           });
    }

    private void saveImage(String imageUrl) {
        Glide.with(this).asBitmap().load(imageUrl).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                saveImageToGallery(userDataImage.this, resource, time);
            }
            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {}

            @Override public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        Toast.makeText(userDataImage.this, "Failed to load image", Toast.LENGTH_SHORT).show();             }
        });
    }

    private void saveImageToGallery(Context context, Bitmap bitmap, String title) {
        String savedImageURL = MediaStore.Images.Media.insertImage(
                context.getContentResolver(),
                bitmap,
                title,
                "PersonaChat"
        );

        if (savedImageURL != null) {
            // If saving to the gallery was successful, notify the media scanner to update the gallery
            MediaScannerConnection.scanFile(context, new String[]{savedImageURL}, null, null);
            Toast.makeText(this, "Image saved to gallery", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        scaleGestureDetector.onTouchEvent(motionEvent);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 105) {
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
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