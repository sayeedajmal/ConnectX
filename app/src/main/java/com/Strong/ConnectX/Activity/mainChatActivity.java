package com.Strong.ConnectX.Activity;

import static android.content.Intent.ACTION_PICK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.Strong.ConnectX.Adaptors.messageAdaptor;
import com.Strong.ConnectX.R;
import com.Strong.ConnectX.Utilities.APIService;
import com.Strong.ConnectX.Utilities.Client;
import com.Strong.ConnectX.Utilities.Constants;
import com.Strong.ConnectX.Utilities.Data;
import com.Strong.ConnectX.Utilities.MyResponse;
import com.Strong.ConnectX.Utilities.NotificationSender;
import com.Strong.ConnectX.databinding.ActivityMainChatBinding;
import com.Strong.ConnectX.models.message;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class mainChatActivity extends AppCompatActivity {
    static int seen;
    private final int REQ_IMAGE = 500;
    FirebaseAuth fAuth;
    private static String MineId;
    private static String YourID;
    FirebaseDatabase database;
    DatabaseReference reference;
    StorageReference StoreRef;
    ActivityMainChatBinding BindMainChat;
    private APIService apiService;
    private static String Token;
    private static String YourName, YourImage;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindMainChat = ActivityMainChatBinding.inflate(getLayoutInflater());
        setContentView(BindMainChat.getRoot());


//      Getting Intent Extra from RecentActivity
        fAuth = FirebaseAuth.getInstance();
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        MineId = fAuth.getUid();

        YourName = getIntent().getStringExtra("username");
        YourImage = getIntent().getStringExtra("UserImage");
        YourID = getIntent().getStringExtra("userId");

        getIntent().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        if (YourName == null) {
            YourName = getIntent().getStringExtra("RecName");
            YourImage = getIntent().getStringExtra("RecImage");
            YourID = getIntent().getStringExtra("RecID");
        }

        BindMainChat.mainChatUsername.setText(YourName);
        Glide.with(this).load(YourImage).into(BindMainChat.mainChatImage);
        BindMainChat.TypeMessage.requestFocus();

        final ArrayList<message> messageModels = new ArrayList<>();
        final messageAdaptor messageAdaptor = new messageAdaptor(messageModels, this);
        int count = messageModels.size();
        database = FirebaseDatabase.getInstance();

        /*Getting Token From Specific User*/
        database.getReference("Users").child(YourID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (Objects.equals(dataSnapshot.getKey(), "Token")) {
                        Token = dataSnapshot.getValue(String.class);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Showing Message
        showMessage(messageAdaptor, messageModels, count);

        //Checking Room available to show seen message & Active Status
        database.getReference().child("Users").child(MineId).child("ChatRoom").child(YourID).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    /*ChatRoom*/
                    if (Objects.equals(dataSnapshot.getKey(), "chatRoom")) {
                        String room = dataSnapshot.getValue(String.class);
                        assert room != null;
                        if (room.equals("1")) {
                            seen = 1;
                            BindMainChat.ActiveStatus.setText("Active Now");
                            //   seenInit();
                        } else {
                            seen = 0;
                            BindMainChat.ActiveStatus.setText(null);
                        }
                        /*Typing*/
                    } else if (Objects.requireNonNull(dataSnapshot.getKey()).equals("Typing")) {
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

        //Sending the message and storing in the database
        initSendMessage();

        // MESSAGE TYPING SHOW TYPING ON ACTIVE STATUS OPTION
        initTyping();


        BindMainChat.mainchatbackButton.setOnClickListener(view -> {
            BindMainChat.TypeMessage.setText(null);
            setRoom("0");
            onBackPressed();
        });

        BindMainChat.constraint.setOnClickListener(view -> {
            Intent intent = new Intent(this, UserDataShow.class);
            intent.putExtra("Image", YourImage);
            intent.putExtra("username", YourName);
            startActivity(intent);
        });

        // OPTION MENU FOR DELETING AND SOME OTHER STUFF
        initOption();

        /*BindMainChat.audioRecord.setListenForRecord(false);
        AudioRecordButton();*/

        BindMainChat.videCallButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, VideoCallOutgoing.class);
            intent.putExtra("Uid", YourID);
            intent.putExtra("OutName", YourName);
            intent.putExtra("OutImage", YourImage);
            startActivity(intent);
        });

        BindMainChat.chooseImage.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
          /*  intent.setType("image/* video/*");
            intent.setType("image/*");
            intent.setAction(ACTION_GET_CONTENT);*/
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                FileChooser.launch(Intent.createChooser(intent, "Select Image To Send"));
            } else
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQ_IMAGE);
        });

        BindMainChat.swipeRefresh.setOnRefreshListener(() -> {
            showMessage(messageAdaptor, messageModels, count);
            BindMainChat.swipeRefresh.setRefreshing(false);
        });

        database.getReference().keepSynced(true);

    }


    //ResultLauncher
    private final ActivityResultLauncher<Intent> FileChooser = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            Intent data = result.getData();
            assert data != null;
            Uri filePath = data.getData();
            /*String mimeType = getContentResolver().getType(filePath);
            if (mimeType.startsWith("image/")) {
            }*/
            BindMainChat.progressUpload.setVisibility(View.VISIBLE);
            BindMainChat.chooseImage.setVisibility(View.INVISIBLE);
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

                Toast.makeText(this, "Sending Image", Toast.LENGTH_SHORT).show();
                StoreRef.putBytes(byteData).addOnSuccessListener(success -> {
                    Task<Uri> ImageUrl = success.getStorage().getDownloadUrl();
                    ImageUrl.addOnCompleteListener(path -> {
                        if (path.isSuccessful()) {
                            String url = path.getResult().toString();
                            message conversation = new message(MineId, url, "ImagePics");
                            conversation.setTimeStamp(new Date().getTime());
                            if (seen == 1) {
                                conversation.setSeen("yes");
                            } else {
                                conversation.setSeen("no");
                                sendNotification(Token, "It's A Picture");
                            }
                            BindMainChat.progressUpload.setVisibility(View.INVISIBLE);
                            BindMainChat.chooseImage.setVisibility(View.VISIBLE);
                            // Feeding AudioMessage to Sender and Receiver Database
                            database.getReference().child("Users").child(MineId).child("Chats").child(YourID).push().setValue(conversation).addOnSuccessListener(e -> database.getReference().child("Users").child(YourID).child("Chats").child(MineId).push().setValue(conversation));

                        }
                    });
                });

            } catch (IOException e) {
                BindMainChat.progressUpload.setVisibility(View.INVISIBLE);
                BindMainChat.chooseImage.setVisibility(View.VISIBLE);
                Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    });

    public void sendNotification(String userToken, String YourMessage) {
        SharedPreferences shared = getSharedPreferences("ConnectX", MODE_PRIVATE);
        String MineName = shared.getString(Constants.KEY_USERNAME, "");
        String MineID = shared.getString(Constants.KEY_ID, "");
        String MineImage = shared.getString(Constants.CHAT_USER_IMAGE, "");

        Data data = new Data(MineName, YourMessage, MineImage, MineID);
        NotificationSender sender = new NotificationSender(data, userToken);
        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(@NonNull Call<MyResponse> call, @NonNull Response<MyResponse> response) {
                if (response.code() == 200) {
                    assert response.body() != null;
                    if (response.body().success != 1) {
                        Toast.makeText(mainChatActivity.this, response.body().success + " Error Sending Notification", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MyResponse> call, @NonNull Throwable t) {
                Toast.makeText(mainChatActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    //SET ROOM VALUE TO 1 WHILE PRESENT IN THIS ACTIVITY
    private void setRoom(String value) {
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(YourID).child("ChatRoom").child(MineId);
        HashMap<String, Object> map = new HashMap<>();
        map.put("chatRoom", value);
        reference.updateChildren(map);
        reference.keepSynced(true);
    }

    private void showMessage(messageAdaptor messageAdaptor, ArrayList<message> messageModels, int count) {
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
                Toast.makeText(mainChatActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                messageAdaptor.notifyDataSetChanged();
            }

        });

    }

    private void initTyping() {
        DatabaseReference refer = FirebaseDatabase.getInstance().getReference().child("Users").child(YourID).child("ChatRoom").child(MineId);
        HashMap<String, Object> hashmap = new HashMap<>();
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
                refer.updateChildren(hashmap);
                refer.keepSynced(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @SuppressLint("NonConstantResourceId")
    private void initOption() {
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
                        return false;
                    default:
                        return false;
                }
            });
        });
    }

    private void initSendMessage() {
        BindMainChat.sendButton.setOnClickListener(view -> {
            String message = Objects.requireNonNull(BindMainChat.TypeMessage.getText()).toString().trim();
            if (!message.equals("")) {
                message conversation = new message(MineId, message);
                conversation.setTimeStamp(new Date().getTime());
                if (seen == 1) {
                    conversation.setSeen("yes");
                } else {
                    conversation.setSeen("no");
                    sendNotification(Token, message);
                }
                BindMainChat.TypeMessage.setText(null);

                // Feeding Message to Sender and Receiver Database
                FirebaseDatabase.getInstance().getReference().child("Users").child(MineId).child("Chats").child(YourID).push().setValue(conversation).addOnSuccessListener(e -> database.getReference().child("Users").child(YourID).child("Chats").child(MineId).push().setValue(conversation).addOnFailureListener(e1 -> Toast.makeText(mainChatActivity.this, e1.getLocalizedMessage(), Toast.LENGTH_SHORT).show())).addOnFailureListener(e -> Toast.makeText(mainChatActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }

    public void deleteChat() {
        StorageReference StoreRef = FirebaseStorage.getInstance().getReference().child("/Media/ImagePics/3AbizhbGLuYwQ1sWE5Av6OBV3di2/DP8DPJnZeMXk0rNhe6ngqxmNT122");
        StoreRef.listAll().onSuccessTask(listResult -> {
            for (StorageReference item : listResult.getItems()) {
                item.delete().addOnSuccessListener(unused -> {
                    database.getReference().child("Users").child(MineId).child("Chats").child(YourID).removeValue();
                    Toast.makeText(mainChatActivity.this, "Chat Deleted Successfully", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }).addOnFailureListener(e -> Toast.makeText(mainChatActivity.this, "Can't Delete this Chat. " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
            }
            return null;
        }).addOnFailureListener(e -> {
            if (e.getLocalizedMessage().equals("Continuation returned null")) {
                database.getReference().child("Users").child(MineId).child("Chats").child(YourID).removeValue();
                Toast.makeText(mainChatActivity.this, "Chat Deleted Successfully", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(YourID).child("ChatRoom").child(MineId);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Typing", "");
        reference.updateChildren(hashMap);
        reference.keepSynced(true);
        setRoom("0");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setRoom("0");
    }

    @Override
    protected void onResume() {
        super.onResume();
        setRoom("1");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // IMAGE ON PERMISSION
        if (requestCode == REQ_IMAGE) {
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Denied. Please Allow Permission By App Info", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Intent intent = new Intent(ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                FileChooser.launch(Intent.createChooser(intent, "Select Image To Send"));
            }
        }
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