package com.Strong.personalchat.Activity;

import static android.content.Intent.ACTION_GET_CONTENT;
import static android.content.Intent.createChooser;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuInflater;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.Strong.personalchat.Adaptors.messageAdaptor;
import com.Strong.personalchat.R;
import com.Strong.personalchat.Utilities.status;
import com.Strong.personalchat.databinding.ActivityMainChatBinding;
import com.Strong.personalchat.models.message;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;


public class mainChatActivity extends status {
    FirebaseAuth fAuth;
    String MineId;
    String YourID;
    private static final int WRITE_REQUEST_CODE = 8;
    FirebaseDatabase database;
    DatabaseReference reference;
    StorageReference StoreRef;
    ActivityMainChatBinding BindMainChat;
    private final int REQ_IMAGE = 500;
    private Uri filePath;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindMainChat = ActivityMainChatBinding.inflate(getLayoutInflater());
        setContentView(BindMainChat.getRoot());

        YourID = getIntent().getStringExtra("userId");
        fAuth = FirebaseAuth.getInstance();
        MineId = fAuth.getUid();
        String username = getIntent().getStringExtra("username");
        String chatUserImage = getIntent().getStringExtra("UserImage");

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(YourID).child("Typing").child(MineId);

        final HashMap<String, Object> hashmap = new HashMap<>();

        BindMainChat.mainChatUsername.setText(username);
        Picasso.get().load(chatUserImage).into(BindMainChat.mainChatImage);

        final ArrayList<message> messageModels = new ArrayList<>();

        final messageAdaptor messageAdaptor = new messageAdaptor(messageModels, this);
        int count = messageModels.size();
        database = FirebaseDatabase.getInstance();

        //Showing Status
        database.getReference().child("Users").child(YourID).addValueEventListener(new ValueEventListener() {
            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (Objects.requireNonNull(dataSnapshot.getKey()).equals("status")) {
                        String status = dataSnapshot.getValue(String.class);
                        assert status != null;
                        if (status.equals("online")) {
                            BindMainChat.ActiveStatus.setText("Active Now");
                        } else {
                            BindMainChat.ActiveStatus.setText("");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //SHOWING TYPING
        database.getReference().child("Users").child(MineId).child("Typing").child(YourID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (Objects.requireNonNull(dataSnapshot.getKey()).equals("Typing")) {
                        String typing = dataSnapshot.getValue(String.class);
                        assert typing != null;
                        if (typing.length() != 0) {
                            BindMainChat.ActiveStatus.setVisibility(View.GONE);
                            BindMainChat.TypingStatus.setVisibility(View.VISIBLE);
                        } else {
                            BindMainChat.TypingStatus.setVisibility(View.GONE);
                            BindMainChat.ActiveStatus.setVisibility(View.VISIBLE);
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Showing Messages
        assert MineId != null;
        database.getReference().child("Users").child(MineId).child("Chats").child(YourID).addValueEventListener(new ValueEventListener() {

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageModels.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    message model = dataSnapshot.getValue(message.class);
                    messageModels.add(model);
                    if (count == 0) {
                        messageAdaptor.notifyDataSetChanged();
                    } else {
                        // Getting Shown the last message when open the chat section
                        messageAdaptor.notifyItemRangeChanged(messageModels.size(), messageModels.size());
                        BindMainChat.mainChatRecyclerView.smoothScrollToPosition(messageModels.size() - 1);
                    }
                    BindMainChat.mainChatRecyclerView.setAdapter(messageAdaptor);
                }
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                messageAdaptor.notifyDataSetChanged();
            }

        });

        //Sending the message and storing in the database
        BindMainChat.sendButton.setOnClickListener(view -> {
            String message = Objects.requireNonNull(BindMainChat.TypeMessage.getText()).toString();
            if (!message.equals("")) {
                message conversation = new message(MineId, message);
                conversation.setTimeStamp(new Date().getTime());
                BindMainChat.TypeMessage.setText(null);

                // Feeding Message to Sender and Receiver Database
                database.getReference().child("Users").child(MineId).child("Chats").child(YourID).push().setValue(conversation).addOnSuccessListener(e -> database.getReference().child("Users").child(YourID).child("Chats").child(MineId).push().setValue(conversation));
            }
        });

        // MESSAGE TYPING SHOW TYPING ON ACTIVE STATUS OPTION
        BindMainChat.TypeMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) {
                    hashmap.put("Typing", charSequence.toString());
                    BindMainChat.chooseImage.setVisibility(View.INVISIBLE);
                    BindMainChat.sendButton.setVisibility(View.VISIBLE);
                } else {
                    hashmap.put("Typing", "");
                    BindMainChat.chooseImage.setVisibility(View.VISIBLE);
                    BindMainChat.sendButton.setVisibility(View.INVISIBLE);
                }
                reference.updateChildren(hashmap);
                reference.keepSynced(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        BindMainChat.mainchatbackButton.setOnClickListener(view -> {
            onBackPressed();
            BindMainChat.TypeMessage.setText(null);
        });

        BindMainChat.constraint.setOnClickListener(view -> {
            Intent intent = new Intent(this, UserDataShow.class);
            intent.putExtra("Image", chatUserImage);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        /* OPTION MENU FOR DELETING AND SOME OTHER STUFF*/
        BindMainChat.optionButton.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.option, popupMenu.getMenu());
            popupMenu.show();

            popupMenu.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()) {
                    case R.id.report:
                        Toast.makeText(getApplicationContext(), "You Clicked on Report", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.block:
                        Toast.makeText(getApplicationContext(), "You Clicked on Block", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.deleteChat:
                        deleteChat();
                        Toast.makeText(getApplicationContext(), "Chats of " + username + " Deleted..", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return false;
                }
            });
        });
        database.getReference().keepSynced(true);
        /*BindMainChat.audioRecord.setListenForRecord(false);

        AudioRecordButton();*/

        BindMainChat.videCallButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, VideoCallOutgoing.class);
            intent.putExtra("Uid", YourID);
            intent.putExtra("OutName", username);
            intent.putExtra("OutImage", chatUserImage);
            startActivity(intent);
        });

        BindMainChat.chooseImage.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(ACTION_GET_CONTENT);
                startActivityForResult(createChooser(intent, "Select Image From Here"), REQ_IMAGE);
            } else {
                AskToRead();
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            database = FirebaseDatabase.getInstance();
            StoreRef = FirebaseStorage.getInstance().getReference();
            long milliTime = System.currentTimeMillis();

            //Reference of ImagePics
            StoreRef = StoreRef.child("Media").child("ImagePics").child(MineId).child(YourID).child(Long.toString(milliTime));
            try {
                Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ByteArrayOutputStream bas = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 50, bas);
                byte[] byteData = bas.toByteArray();

                StoreRef.putBytes(byteData).addOnSuccessListener(success -> {
                    Task<Uri> audioUrl = success.getStorage().getDownloadUrl();
                    audioUrl.addOnCompleteListener(path -> {
                        if (path.isSuccessful()) {
                            String url = path.getResult().toString();
                            message conversation = new message(MineId, url, "ImagePics");
                            conversation.setTimeStamp(new Date().getTime());
                            BindMainChat.TypeMessage.setText(null);
                            // Feeding AudioMessage to Sender and Receiver Database
                            database.getReference().child("Users").child(MineId).child("Chats").child(YourID).push().setValue(conversation).addOnSuccessListener(e -> database.getReference().child("Users").child(YourID).child("Chats").child(MineId).push().setValue(conversation));
                        }
                    });
                });

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private void deleteChat() {
        database.getReference().child("Users").child(MineId).child("Chats").child(YourID).removeValue();
        /*StorageReference firebaseAudioPath = FirebaseStorage.getInstance().getReference();
        firebaseAudioPath=firebaseAudioPath.child("Media").child("RecordAudio").child(YourID);
        firebaseAudioPath.delete().addOnSuccessListener(unused -> Toast.makeText(mainChatActivity.this, "Chat Deleted.", Toast.LENGTH_SHORT).show()).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mainChatActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });*/
        onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(YourID).child("Typing").child(MineId);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Typing", "");
        reference.updateChildren(hashMap);
        reference.keepSynced(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {

        if (requestCode == REQ_IMAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void AskToRead() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQ_IMAGE);
    }
     /*   private boolean isRecordingOk() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestRecording() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 8080);
    }

    private void sendAudioMessage(String audioPath) {
        long milliTime = System.currentTimeMillis();

        StorageReference firebaseAudioPath = FirebaseStorage.getInstance().getReference();
        firebaseAudioPath = firebaseAudioPath.child("Media").child("RecordAudio").child(YourID).child(Long.toString(milliTime));
        Uri uri = Uri.fromFile(new File(audioPath));
        firebaseAudioPath.putFile(uri).addOnSuccessListener(success -> {
            Task<Uri> audioUrl = success.getStorage().getDownloadUrl();
            audioUrl.addOnCompleteListener(path -> {
                if (path.isSuccessful()) {
                    String url = path.getResult().toString();

                    message conversation = new message(MineId, url, "RecordAudio");
                    conversation.setTimeStamp(new Date().getTime());
                    BindMainChat.TypeMessage.setText(null);

                    // Feeding AudioMessage to Sender and Receiver Database
                    database.getReference().child("Users").child(MineId).child("Chats").child(YourID).push().setValue(conversation).addOnSuccessListener(e -> database.getReference().child("Users").child(YourID).child("Chats").child(MineId).push().setValue(conversation));
                }
            });
        });
    }

    private void AudioRecordButton() {
        BindMainChat.audioRecord.setRecordView(BindMainChat.recordView);
        BindMainChat.recordView.setSmallMicColor(Color.parseColor("#2196F3"));
        BindMainChat.recordView.setSmallMicIcon(R.drawable.microphone);
        BindMainChat.recordView.setLessThanSecondAllowed(false);
        BindMainChat.recordView.setSoundEnabled(false);


        BindMainChat.audioRecord.setOnClickListener(view -> {
            //Create Folder
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                if (isRecordingOk()) {
                    BindMainChat.audioRecord.setListenForRecord(true);
                    Toast.makeText(this, "Ready..? Hold The Button..", Toast.LENGTH_SHORT).show();
                    createDirectory();
                    RecordAudio();
                } else requestRecording();
            } else {
                askPermissionTOWrite();
            }
        });
    }

    private void RecordAudio() {
        File AudioFile = new File(CreateFile());
        MediaRecorder mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);
        mediaRecorder.setOutputFile(AudioFile.getPath());

        BindMainChat.recordView.setOnRecordListener(new OnRecordListener() {
            @Override
            public void onStart() {
                try {
                    mediaRecorder.prepare();
                    mediaRecorder.start();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
                BindMainChat.TypeMessage.setVisibility(View.INVISIBLE);
                BindMainChat.sendButton.setVisibility(View.INVISIBLE);
                BindMainChat.recordView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancel() {
                mediaRecorder.reset();
                mediaRecorder.release();
                if (AudioFile.exists()) AudioFile.delete();
                Toast.makeText(mainChatActivity.this, "Canceled...", Toast.LENGTH_SHORT).show();

                BindMainChat.TypeMessage.setVisibility(View.VISIBLE);
                BindMainChat.recordView.setVisibility(View.INVISIBLE);
                BindMainChat.audioRecord.setListenForRecord(false);

            }

            @Override
            public void onFinish(long recordTime) {
                mediaRecorder.stop();
                mediaRecorder.release();
                sendAudioMessage(AudioFile.getPath());
                BindMainChat.TypeMessage.setVisibility(View.VISIBLE);
                BindMainChat.recordView.setVisibility(View.INVISIBLE);
                BindMainChat.audioRecord.setListenForRecord(false);

            }

            @Override
            public void onLessThanSecond() {
                mediaRecorder.reset();
                mediaRecorder.release();
                if (AudioFile.exists()) AudioFile.delete();
                BindMainChat.TypeMessage.setVisibility(View.VISIBLE);
                BindMainChat.recordView.setVisibility(View.INVISIBLE);
                BindMainChat.audioRecord.setListenForRecord(false);
            }
        });
    }

    private String CreateFile() {
        String Name = +System.currentTimeMillis() + ".mp3";
        File file = new File(Environment.getExternalStoragePublicDirectory("Music") + File.separator + "PersonalChat", Name);
        // Toast.makeText(this, file.getPath(), Toast.LENGTH_SHORT).show();
        return file.getPath();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {

        if (requestCode == WRITE_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                createDirectory();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void createDirectory() {
        new File(Environment.getExternalStoragePublicDirectory("Music"), "PersonalChat");
    }

    private void askPermissionTOWrite() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_REQUEST_CODE);
    }*/
}