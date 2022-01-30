package com.Strong.personalchat;

import static android.content.Intent.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.Strong.personalchat.models.primaryGetter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class uploadProfile extends AppCompatActivity {

    AppCompatButton uploadProfile;
    CircleImageView newProfileImage;
    AppCompatImageButton newProfileImageButton;
    //Uri Indicates where the image wil be picked from!
    private Uri filePath;
    //Request Code
    private final int PICK_IMAGE_REQUEST=22;
    //Firebase Instance
    FirebaseStorage storage;
    StorageReference storageReferance;
    ProgressBar profileProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        uploadProfile=findViewById(R.id.uploadProfile);
        newProfileImage=findViewById(R.id.newProfileImage);
        newProfileImageButton=findViewById(R.id.newProfileImageButton);
        profileProgress=findViewById(R.id.profileProgress);

        newProfileImageButton.setOnClickListener(view ->{
            SelectImage();
        });


        uploadProfile.setOnClickListener(view -> {
            uploadImage();
            Intent intent=new Intent(uploadProfile.this, verifyNumber.class);
            startActivity(intent);
        });
    }
    private void SelectImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(ACTION_GET_CONTENT);
        startActivityForResult(createChooser(intent, "Select Image From Here"),PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICK_IMAGE_REQUEST && resultCode ==RESULT_OK && data!=null && data.getData() !=null){
            //get The Uri of Data
            filePath=data.getData();
                //Setting image on imageview using bitmap
                try {
                    Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                    newProfileImage.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    private void uploadImage() {
        if (filePath!=null){
            Toast.makeText(uploadProfile.this, "Uploading Profile Pic", Toast.LENGTH_SHORT).show();
            profileProgress.setVisibility(View.VISIBLE);

            storage=FirebaseStorage.getInstance();
            storageReferance=storage.getReference();
            String fileUid=UUID.randomUUID().toString();
            storageReferance=storageReferance.child("Images/"+filePath);

            //Storing Image String to The Database
            primaryGetter storageImage=new primaryGetter(fileUid);
            FirebaseDatabase database=FirebaseDatabase.getInstance();
            String CurrentID=getIntent().getStringExtra("userId");

            database.getReference().child("Users").child(CurrentID).setValue(storageImage);
            //Adding Listener on uploading  or failure of image
            storageReferance.putFile(filePath).addOnSuccessListener(taskSnapshot -> {
                profileProgress.setVisibility(View.GONE);
                Toast.makeText(uploadProfile.this, "Image Uploaded!", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                         profileProgress.setVisibility(View.GONE);
                    Toast.makeText(uploadProfile.this, "Failed"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}