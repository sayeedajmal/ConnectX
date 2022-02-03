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

import com.Strong.personalchat.R;
import com.Strong.personalchat.models.callsGetter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class callsAdaptor extends ArrayAdapter<callsGetter> {

    public Context context;
    public callsAdaptor(Context context, List<callsGetter> userCall) {
        super(context, R.layout.call_list, userCall);
        this.context=context;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        @SuppressLint("ViewHolder") View view=layoutInflater.inflate(R.layout.call_list, null);

        TextView callUsername=view.findViewById(R.id.callUsername);
        TextView callInform=view.findViewById(R.id.callInform);

        CircleImageView callUserImage=(CircleImageView)view.findViewById(R.id.callUserImage);
        callsGetter callsGetter =getItem(position);

        Glide.with(context).load(callsGetter.getCallUserImage()).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                callUserImage.setBackground(resource);
            }
        });
        callUsername.setText(callsGetter.getCallUsername());
        callInform.setText(callsGetter.getCallInform()+"."+"Today");

        return view;
    }
}