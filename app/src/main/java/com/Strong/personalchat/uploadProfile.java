package com.Strong.personalchat;

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

import com.Strong.personalchat.databinding.ActivityProfileBinding;
import com.Strong.personalchat.models.signupSetter;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class uploadProfile extends AppCompatActivity {

    ActivityProfileBinding BindProfile;
    //Uri Indicates where the image wil be picked from!
    private Uri filePath;
    //Request Code
    private final int PICK_IMAGE_REQUEST=22;
    //Firebase Instance
    FirebaseStorage storage;
    StorageReference storageReference;
    String username, email,pass, id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindProfile=ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(BindProfile.getRoot());

        username=getIntent().getStringExtra("username");
        email=getIntent().getStringExtra("email");
        pass=getIntent().getStringExtra("pass");
        id=getIntent().getStringExtra("userId");
        BindProfile.newProfileImage.setOnClickListener(view ->{
            SelectImage();
        });


        BindProfile.uploadProfile.setOnClickListener(view -> {
           try {
                uploadImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
                    BindProfile.newProfileImage.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    private void uploadImage() throws IOException {
        if (filePath!=null){
            Toast.makeText(uploadProfile.this, "Uploading Profile Pic", Toast.LENGTH_SHORT).show();
            BindProfile.profileProgress.setVisibility(View.VISIBLE);
            BindProfile.uploadProfile.setVisibility(View.INVISIBLE);

            storage=FirebaseStorage.getInstance();
            storageReference =storage.getReference();
            storageReference = storageReference.child("ProfileImages/"+id);

            //Storing Image String to The Database
            FirebaseDatabase database=FirebaseDatabase.getInstance();

            Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            ByteArrayOutputStream bas = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 25, bas);
            byte[] data = bas.toByteArray();

            storageReference.putBytes(data).addOnSuccessListener(taskSnapshot -> {
                storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    signupSetter userData=new signupSetter(username,email,pass,uri.toString(),id);
                    database.getReference().child("Users").child(id).setValue(userData);
                });

                BindProfile.profileProgress.setVisibility(View.GONE);
                BindProfile.uploadProfile.setVisibility(View.VISIBLE);

                Toast.makeText(uploadProfile.this, "Image Uploaded!", Toast.LENGTH_SHORT).show();

                Intent intent=new Intent(uploadProfile.this, verifyNumber.class);
                startActivity(intent);
            }).addOnFailureListener(e -> {
                BindProfile.profileProgress.setVisibility(View.GONE);
                BindProfile.uploadProfile.setVisibility(View.VISIBLE);

                Toast.makeText(uploadProfile.this, "Failed"+e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }
        else
            Toast.makeText(uploadProfile.this, "Please Select Profile Pic..", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "You cant Go BacK ", Toast.LENGTH_SHORT).show();
    }
}