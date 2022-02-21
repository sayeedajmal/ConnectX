package com.Strong.personalchat.Adaptors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.Strong.personalchat.R;
import com.Strong.personalchat.models.UserGetter;
import com.Strong.personalchat.models.callsGetter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class callsAdaptor extends RecyclerView.Adapter<callsAdaptor.ViewHolder> {
    ArrayList<callsGetter> userCall;
    Context context;
    public callsAdaptor(Context context, ArrayList<callsGetter> userCall) {
        this.context=context;
        this.userCall=userCall;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.fragment_calls, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        callsGetter callsGetter =userCall.get(position);

        Glide.with(context).load(callsGetter.getCallUserImage()).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                holder.callUserImage.setBackground(resource);
            }
        });
        holder.callUsername.setText(callsGetter.getCallUsername());
        holder.callInform.setText(callsGetter.getCallInform()+"."+"Today");
    }

    @Override
    public int getItemCount() {
        return userCall.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView callUsername;
        TextView callInform;
        CircleImageView callUserImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            callUsername=itemView.findViewById(R.id.callUsername);
            callInform=itemView.findViewById(R.id.callInform);
            callUserImage=(CircleImageView)itemView.findViewById(R.id.callUserImage);
        }
    }
}