package com.Strong.personalchat.Adaptors;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.Strong.personalchat.mainChat;
import com.Strong.personalchat.models.UserGetter;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Strong.personalchat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class recentChatAdaptor extends RecyclerView.Adapter<recentChatAdaptor.ViewHolder>{

    ArrayList<UserGetter> chatUserList;
    Context context;
    public recentChatAdaptor(ArrayList<UserGetter> chatUserList, Context context) {
        this.chatUserList = chatUserList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.chat_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserGetter users=chatUserList.get(position);
        String currentUse=FirebaseAuth.getInstance().getCurrentUser().getUid();

        //Last Message to Shown
        FirebaseDatabase.getInstance().getReference().child("Users").child("Chats").child(currentUse).child(users.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (snapshot.hasChildren()){
                   for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                       holder.chatLastMessage.setText(dataSnapshot.child("message").getValue(String.class));
                       Long fetchingTime=dataSnapshot.child("timeStamp").getValue(Long.class);
                       Date time=new Date(fetchingTime);
                       holder.lastMessageTime.setText(ShowDateTime(time));
                   }
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {
           }
       });


        //Showing Chat Details
        Picasso.get().load(users.getChatUserImage()).into(holder.chatUserImage);
        holder.ChatUsername.setText(users.getUsername());
        holder.itemView.setOnClickListener(view -> {
            Intent intent=new Intent(context, mainChat.class);
            intent.putExtra("userId", users.getUserId());
            intent.putExtra("username", users.getUsername());
            intent.putExtra("newChatUserImage",  Uri.parse(users.getChatUserImage()).toString());
            context.startActivity(intent);
        });

        //SHOWING STATUS EITHER USER IS ONLINE OR OFFLINE
           /* if (users.getStatus().equals("online")) {
                holder.Active_status.setVisibility(View.VISIBLE);
            } else {
                holder.deActive_status.setVisibility(View.VISIBLE);
            } */
    }

    private String ShowDateTime(Date date) {
        return new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(date);
    }

    @Override
    public int getItemCount() {
        return chatUserList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView chatUserImage, Active_status, deActive_status;
        TextView ChatUsername, chatLastMessage, lastMessageTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Active_status=itemView.findViewById(R.id.Active_status);
            deActive_status=itemView.findViewById(R.id.deActive_status);
            chatUserImage=itemView.findViewById(R.id.chatUserImage);
            ChatUsername=itemView.findViewById(R.id.ChatUsername);
            chatLastMessage=itemView.findViewById(R.id.chatLastMessage);
            lastMessageTime=itemView.findViewById(R.id.lastMessageTime);
        }
    }
}