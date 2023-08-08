package com.Strong.ConnectX.Adaptors;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Strong.ConnectX.R;

public class receiveViewHolder extends RecyclerView.ViewHolder {
        TextView messageRec, messageRecTime, img_rec_time, ReceiveEmoji;
        ImageView recImage;
        //VoicePlayerView receiveRecord;

        public receiveViewHolder(@NonNull View itemView) {
            super(itemView);
            messageRec = itemView.findViewById(R.id.messageRec);
            messageRecTime = itemView.findViewById(R.id.messageRecTime);
            recImage = itemView.findViewById(R.id.recImage);
            img_rec_time = itemView.findViewById(R.id.img_rec_time);
            ReceiveEmoji = itemView.findViewById(R.id.ReceiveEmoji);
            // receiveRecord = itemView.findViewById(R.id.RecVoicePlayerView);
        }
    }
