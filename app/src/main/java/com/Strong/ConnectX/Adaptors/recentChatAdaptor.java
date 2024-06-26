package com.Strong.ConnectX.Adaptors;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Strong.ConnectX.Activity.mainChatActivity;
import com.Strong.ConnectX.R;
import com.Strong.ConnectX.models.recentGetter;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class recentChatAdaptor extends RecyclerView.Adapter<recentChatAdaptor.ViewHolder> {
    ArrayList<recentGetter> chatUserList;
    Context context;

    public recentChatAdaptor(ArrayList<recentGetter> chatUserList, Context context) {
        this.chatUserList = chatUserList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        recentGetter users = chatUserList.get(position);

        //Last Message to Shown
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference().child("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child("Chats").child(users.getUserId()).orderByChild("timestamp").limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String message = dataSnapshot.child("message").getValue(String.class);
                    assert message != null;
                    //  System.out.println("<<<<<<<<<<" + users.getUserId() + "<<<<<" + CurrentUser.getUserId());
                    if (message.startsWith("https://firebasestorage.googleapis.com/v0/b/personalchat-d14fe.appspot.com/o/Media%2FImagePics")) {
                        holder.chatLastMessage.setText("✓ " + "Image");
                    } else {
                        holder.chatLastMessage.setText("✓ " + dataSnapshot.child("message").getValue(String.class));
                    }
                    Long fetchingTime = dataSnapshot.child("timeStamp").getValue(Long.class);
                    Date time = new Date(fetchingTime);
                    holder.lastMessageTime.setText(ShowDateTime(time));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //Showing Chat Details
        Glide.with(context).

                load(users.getChatUserImage()).

                into(holder.chatUserImage);
        holder.ChatUsername.setText(users.getUsername());
        holder.itemView.setOnClickListener(view ->

        {
            Intent intent = new Intent(context, mainChatActivity.class);
            intent.putExtra("userId", users.getUserId());
            intent.putExtra("username", users.getUsername());
            intent.putExtra("UserImage", users.getChatUserImage());
            context.startActivity(intent);
        });
    }

    private String ShowDateTime(Date date) {
        return new SimpleDateFormat("hh:mm a dd/MM", Locale.getDefault()).format(date);
    }

    @Override
    public int getItemCount() {
        return chatUserList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView chatUserImage;
        TextView ChatUsername, chatLastMessage, lastMessageTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chatUserImage = itemView.findViewById(R.id.chatUserImage);
            ChatUsername = itemView.findViewById(R.id.ChatUsername);
            chatLastMessage = itemView.findViewById(R.id.chatLastMessage);
            lastMessageTime = itemView.findViewById(R.id.lastMessageTime);
        }
    }
}