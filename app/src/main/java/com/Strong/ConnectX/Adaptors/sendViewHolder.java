package com.Strong.ConnectX.Adaptors;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Strong.ConnectX.R;

public class sendViewHolder extends RecyclerView.ViewHolder {
    TextView messageSen, messageSenTime, img_sen_time, sendEmoji;
    ImageView sendImage, seen, ImageSeen;
    // VoicePlayerView sendRecord;


    public sendViewHolder(@NonNull View itemView) {
        super(itemView);
        messageSen = itemView.findViewById(R.id.messageSen);
        messageSenTime = itemView.findViewById(R.id.messageSenTime);
        sendImage = itemView.findViewById(R.id.sendImage);
        img_sen_time = itemView.findViewById(R.id.img_sen_time);
        sendEmoji = itemView.findViewById(R.id.sendEmoji);
        seen = itemView.findViewById(R.id.seen);
        ImageSeen = itemView.findViewById(R.id.ImageSeen);
        //  sendRecord = itemView.findViewById(R.id.SendVoicePlayerView);
    }
}
