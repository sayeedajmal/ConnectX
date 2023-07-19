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
import com.Strong.ConnectX.models.newChatGetter;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class newChatAdaptor extends RecyclerView.Adapter<newChatAdaptor.ViewHolder> {
    ArrayList<newChatGetter> newUserList;
    Context context;

    public newChatAdaptor(ArrayList<newChatGetter> newUserList, Context context) {
        this.newUserList = newUserList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.new_contact, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        newChatGetter users = newUserList.get(position);


        Glide.with(context).load(users.getChatUserImage()).into(holder.newContactImage);
        holder.newChatUsername.setText(users.getUsername());

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, mainChatActivity.class);
            intent.putExtra("userId", users.getUserId());
            intent.putExtra("username", users.getUsername());
            intent.putExtra("UserImage", users.getChatUserImage());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return newUserList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView newContactImage;
        TextView newChatUsername;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            newContactImage = itemView.findViewById(R.id.newContactImage);
            newChatUsername = itemView.findViewById(R.id.newChatUsername);
        }
    }

}