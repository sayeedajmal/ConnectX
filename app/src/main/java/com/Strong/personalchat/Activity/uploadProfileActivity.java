package com.Strong.personalchat.Activity;

import static android.content.Intent.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.Strong.personalchat.Utilities.Constants;
import com.Strong.personalchat.databinding.ActivityProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class uploadProfileActivity extends AppCompatActivity {

    ActivityProfileBinding BindProfile;
    //Uri Indicates where the image wil be picked from!
    private Uri filePath;

    //Request Code
    private final int PICK_IMAGE_REQUEST = 22;
    //Firebase Instance
    StorageReference storageReference;
    private FirebaseAuth mAuth;
    String username, email, pass, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        BindProfile = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(BindProfile.getRoot());

        username = getIntent().getStringExtra(Constants.KEY_USERNAME);
        email = getIntent().getStringExtra(Constants.KEY_EMAIL);
        pass = getIntent().getStringExtra(Constants.KEY_PASSWORD);
        id = getIntent().getStringExtra(Constants.KEY_ID);

        BindProfile.newProfileImage.setOnClickListener(view -> SelectImage());


        BindProfile.uploadProfile.setOnClickListener(view -> {
            try {
                uploadImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void SelectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(ACTION_GET_CONTENT);
        startActivityForResult(createChooser(intent, "Select Image From Here"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //get The Uri of Data
            filePath = data.getData();
            //Setting image on imageview using bitmap
            try {
                BindProfile.pickImage.setVisibility(View.INVISIBLE);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                BindProfile.newProfileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() throws IOException {
        if (filePath != null) {
            showToast("Uploading Profile Pic");
            visibility(true);

            //Storing Image String to The Database
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            ByteArrayOutputStream bas = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, bas);
            byte[] data = bas.toByteArray();

            mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    storageReference = FirebaseStorage.getInstance().getReference();
                    String id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                    storageReference = storageReference.child("ProfileImages/" + id);

                    storageReference.putBytes(data).addOnSuccessListener(taskSnapshot -> {
                        storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put(Constants.KEY_USERNAME, username);
                            hashMap.put(Constants.KEY_EMAIL, email);
                            hashMap.put(Constants.KEY_PASSWORD, pass);
                            hashMap.put(Constants.KEY_URI, uri.toString());
                            hashMap.put(Constants.KEY_ID, id);
                            database.getReference().child("Users").child(id).setValue(hashMap);
                            showToast("Image Uploaded!");
                        });
                        showToast("Welcome " + username + " PersonalChat.");
                        Intent intent = new Intent(uploadProfileActivity.this, recentActivity.class);
                        startActivity(intent);
                    }).addOnFailureListener(e -> {
                        visibility(false);
                        showToast("Failed" + e.getMessage());
                    });
                } else {
                    // Registration failed
                    showToast(Objects.requireNonNull(task.getException()).getMessage());
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void visibility(boolean key) {
        if (key) {
            BindProfile.profileProgress.setVisibility(View.VISIBLE);
            BindProfile.uploadProfile.setVisibility(View.INVISIBLE);
        } else {
            BindProfile.profileProgress.setVisibility(View.GONE);
            BindProfile.uploadProfile.setVisibility(View.VISIBLE);
        }
    }
}