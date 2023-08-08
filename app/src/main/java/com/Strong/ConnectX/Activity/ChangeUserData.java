package com.Strong.ConnectX.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.Strong.ConnectX.Utilities.Constants;
import com.Strong.ConnectX.databinding.ActivityChangeUserDataBinding;
import com.Strong.ConnectX.models.CurrentUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class ChangeUserData extends AppCompatActivity {
    private final int PICK_IMAGE_REQUEST = 20;
    ActivityChangeUserDataBinding BindUserData;
    String Username;
    StorageReference storageReference;
    private Uri filePath;

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
                uploadImage();
            } catch (IOException e) {
                throw new RuntimeException(e);
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

    private void uploadImage() throws IOException {
        if (filePath != null) {
            BindUserData.updateButton.setVisibility(View.INVISIBLE);
            BindUserData.uploadProgress.setVisibility(View.VISIBLE);
            //Storing Image String to The Database
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            storageReference = FirebaseStorage.getInstance().getReference();

            Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            ByteArrayOutputStream bas = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, bas);
            byte[] data = bas.toByteArray();

            storageReference = storageReference.child("ProfileImages/" + CurrentUser.userId);
            storageReference.putBytes(data).addOnSuccessListener(taskSnapshot -> {
                storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put(Constants.CHAT_USER_IMAGE, uri.toString());
                    database.getReference().child("Users").child(CurrentUser.userId).updateChildren(hashMap);
                    Toast.makeText(this, "Profile Image Uploaded", Toast.LENGTH_SHORT).show();
                    BindUserData.updateButton.setVisibility(View.VISIBLE);
                    BindUserData.uploadProgress.setVisibility(View.INVISIBLE);
                });
                Intent intent = new Intent(this, recentActivity.class);
                startActivity(intent);
                finishAffinity();
            }).addOnFailureListener(e -> {
                Toast.makeText(this, "Uploading Failed " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                BindUserData.updateButton.setVisibility(View.VISIBLE);
                BindUserData.uploadProgress.setVisibility(View.INVISIBLE);
            });
        }
    }
}