package com.Strong.personalchat.Adaptors;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.Strong.personalchat.mainChat;
import com.Strong.personalchat.models.primaryGetter;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Strong.personalchat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class primaryAdaptor extends RecyclerView.Adapter<primaryAdaptor.ViewHolder>{

    ArrayList<primaryGetter> chatUserList;
    Context context;

    public primaryAdaptor(ArrayList<primaryGetter> chatUserList, Context context) {
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
        primaryGetter users=chatUserList.get(position);

        Picasso.get().load(users.getChatUserImage()).placeholder(R.mipmap.avtar).into(holder.chatUserImage);
        holder.ChatUsername.setText(users.getUsername());
        String currentUse=FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("Users").child(currentUse).child(users.getUserId()).orderByChild("timestamp").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (snapshot.hasChildren()){
                   for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                       holder.chatUserMessage.setText(dataSnapshot.child("message").getValue(String.class));
                   }
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });

        Picasso.get().load(users.getChatUserImage()).placeholder(R.mipmap.avtar).into(holder.chatUserImage);
        holder.ChatUsername.setText(users.getUsername());
        holder.chatUserMessage.setText(users.getLastMessage());
        holder.itemView.setOnClickListener(view -> {
            Intent intent=new Intent(context, mainChat.class);
            intent.putExtra("userId", users.getUserId());
            intent.putExtra("username", users.getUsername());
            intent.putExtra("newChatUserImage", users.getChatUserImage());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return chatUserList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView chatUserImage;
        TextView ChatUsername, chatUserMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chatUserImage=itemView.findViewById(R.id.chatUserImage);
            ChatUsername=itemView.findViewById(R.id.ChatUsername);
            chatUserMessage=itemView.findViewById(R.id.chatUserMessage);
        }
    }

}