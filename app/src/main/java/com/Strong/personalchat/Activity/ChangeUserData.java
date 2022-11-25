package com.Strong.personalchat.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.Strong.personalchat.Utilities.Constants;
import com.Strong.personalchat.databinding.ActivityChangeUserDataBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class ChangeUserData extends AppCompatActivity {
    ActivityChangeUserDataBinding BindUserData;
    private Uri filePath;
    String Username;
    private final int PICK_IMAGE_REQUEST = 20;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindUserData = ActivityChangeUserDataBinding.inflate(getLayoutInflater());
        setContentView(BindUserData.getRoot());
        BindUserData.backButton.setOnClickListener(view -> {
            onBackPressed();
        });

        BindUserData.buttonPic.setOnClickListener(view -> {
            SelectImage();
        });

        BindUserData.updateButton.setOnClickListener(view -> {
            try {
                updateUserData();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void SelectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Choose an Image.."), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //get The Uri of Data
            filePath = data.getData();
            //Setting image on imageview using bitmap
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                BindUserData.updateImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateUserData() throws IOException {
        if (filePath != null) {
            Toast.makeText(this, "Uploading Profile Pic..", Toast.LENGTH_SHORT).show();

            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            storageReference = FirebaseStorage.getInstance().getReference();
            String id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
            storageReference = storageReference.child("ProfileImages/" + id);
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            ByteArrayOutputStream bas = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, bas);
            byte[] data = bas.toByteArray();
            Username = BindUserData.updateUserName.getText().toString();
            storageReference.putBytes(data).addOnSuccessListener(taskSnapshot ->
                    storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put(Constants.KEY_URI, uri);
                        hashMap.put(Constants.KEY_USERNAME, Username);
                        database.getReference().child("Users").child(id).setValue(hashMap);
                        startActivity(new Intent(this, recentActivity.class));
                        finishAffinity();
                    })
            );
        } else {
            Toast.makeText(this, "Select A Pic", Toast.LENGTH_SHORT).show();
        }
    }
}