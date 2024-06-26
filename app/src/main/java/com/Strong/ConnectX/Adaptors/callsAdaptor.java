package com.Strong.ConnectX.Adaptors;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.Strong.ConnectX.R;
import com.Strong.ConnectX.models.callsGetter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class callsAdaptor extends RecyclerView.Adapter<callsAdaptor.ViewHolder> {
    ArrayList<callsGetter> userCall;
    Context context;

    public callsAdaptor(Context context, ArrayList<callsGetter> userCall) {
        this.context = context;
        this.userCall = userCall;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.call_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        callsGetter callsGetter = userCall.get(position);

        Glide.with(context).load(callsGetter.getCallUserImage()).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                holder.callUserImage.setBackground(resource);
            }
        });
        holder.callUsername.setText(callsGetter.getCallUsername());
        holder.callInform.setText(callsGetter.getCallInform() + "." + "Today");
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

            callUsername = itemView.findViewById(R.id.callUsername);
            callInform = itemView.findViewById(R.id.callInform);
            callUserImage = (CircleImageView) itemView.findViewById(R.id.callUserImage);
        }
    }
}