package com.Strong.personalchat.Adaptors;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.Strong.personalchat.models.newChatGetter;
import com.Strong.personalchat.R;
import com.squareup.picasso.Picasso;
import com.Strong.personalchat.mainChat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class newChatAdaptor extends RecyclerView.Adapter<newChatAdaptor.ViewHolder>{

    ArrayList<newChatGetter> newUserList;
    Context context;

    public newChatAdaptor(ArrayList<newChatGetter> newUserList, Context context) {
        this.newUserList = newUserList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.new_contact, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        newChatGetter users=newUserList.get(position);

        Picasso.get().load(users.getChatUserImage()).into(holder.newContactImage);
        holder.newChatUsername.setText(users.getUsername());

        holder.itemView.setOnClickListener(view -> {
            Intent intent=new Intent(context, mainChat.class);
            intent.putExtra("userId", users.getUserId());
            intent.putExtra("username", users.getUsername());
            intent.putExtra("newChatUserImage", Uri.parse(users.getChatUserImage()).toString());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return newUserList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView newContactImage;
        TextView newChatUsername;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            newContactImage=itemView.findViewById(R.id.newContactImage);
            newChatUsername=itemView.findViewById(R.id.newChatUsername);
        }
    }

}