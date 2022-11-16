package com.Strong.personalchat.Adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Strong.personalchat.R;
import com.Strong.personalchat.models.message;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class messageAdaptor extends RecyclerView.Adapter {
    ArrayList<message> messageModels;
    Context context;
    int SENDER_VIEW_TYPE = 1;
    int RECEIVE_VIEW_TYPE = 2;/*
    int SENDER_AUDIO_RECORD = 3;
    int RECEIVER_AUDIO_RECORD = 4;*/
    int SENDER_IMAGE = 3;
    int RECEIVER_IMAGE = 4;
    BottomSheetDialog bottomSheetDialog;

    public messageAdaptor(ArrayList<message> messageModels, Context context) {
        this.messageModels = messageModels;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == SENDER_VIEW_TYPE) {
            view = LayoutInflater.from(context).inflate(R.layout.sample_send, parent, false);
            return new sendViewHolder(view);
        } else if (viewType == RECEIVER_IMAGE) {
            view = LayoutInflater.from(context).inflate(R.layout.recie_image_layout, parent, false);
            return new receiveViewHolder(view);
        } else if (viewType == SENDER_IMAGE) {
            view = LayoutInflater.from(context).inflate(R.layout.send_image_layout, parent, false);
            return new sendViewHolder(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.sample_recieve, parent, false);
            return new receiveViewHolder(view);
        }/*else if (viewType == RECEIVER_AUDIO_RECORD) {
            view = LayoutInflater.from(context).inflate(R.layout.sample_audiorecieve, parent, false);
            return new receiveViewHolder(view);

        } else if (viewType == SENDER_AUDIO_RECORD) {
            view = LayoutInflater.from(context).inflate(R.layout.sample_audiosend, parent, false);
            return new sendViewHolder(view);

        } */
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        message message = messageModels.get(position);
        String ImagePics = messageModels.get(position).getMessageType();
        //String audioMessage = messageModels.get(position).getMessageType();

        switch (getItemViewType(position)) {
            case 1:
                ((sendViewHolder) holder).messageSen.setText(message.getMessage());
                Date sendTime = new Date(message.getTimeStamp());
                ((sendViewHolder) holder).messageSenTime.setText(ShowDateTime(sendTime));
                break;
            case 2:
                ((receiveViewHolder) holder).messageRec.setText(message.getMessage());
                Date receiveTime = new Date(message.getTimeStamp());
                ((receiveViewHolder) holder).messageRecTime.setText(ShowDateTime(receiveTime));
                break;
            case 3:
                if (ImagePics != null && ImagePics.equals("ImagePics"))
                    Picasso.get().load(message.getMessage()).into(((sendViewHolder) holder).sendImage);
                Date sendImgTime = new Date(message.getTimeStamp());
                ((sendViewHolder) holder).img_sen_time.setText(ShowDateTime(sendImgTime));
                break;
            case 4:
                if (ImagePics != null && ImagePics.equals("ImagePics"))
                    Picasso.get().load(message.getMessage()).into(((receiveViewHolder) holder).recImage);
                Date recImgTime = new Date(message.getTimeStamp());
                ((receiveViewHolder) holder).img_rec_time.setText(ShowDateTime(recImgTime));
                break;

           /* case 3:
                if (audioMessage != null && audioMessage.equals("RecordAudio"))
                    ((sendViewHolder) holder).sendRecord.setAudio(message.getMessage());
                break;
            case 4:
                if (audioMessage != null && audioMessage.equals("RecordAudio"))
                    ((receiveViewHolder) holder).receiveRecord.setAudio(message.getMessage());
                break;*/
        }
        holder.itemView.setOnLongClickListener(view -> {
            deleteMessage();
            return false;
        });
    }

    private void deleteMessage() {
        bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.fragment_bottom_sheet_delete);
        Button cancel = bottomSheetDialog.findViewById(R.id.Cancel);
        Button delete = bottomSheetDialog.findViewById(R.id.Delete);

        bottomSheetDialog.show();
        assert cancel != null;
        cancel.setOnClickListener(v -> bottomSheetDialog.dismiss());
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        bottomSheetDialog.setOnDismissListener(dialogInterface -> bottomSheetDialog.dismiss());
        assert delete != null;
        delete.setOnClickListener(v -> {

        });
    }

    private String ShowDateTime(Date date) {
        return new SimpleDateFormat("dd,MM,yyyy - hh:mm a", Locale.getDefault()).format(date);
    }

    @Override
    public int getItemViewType(int position) {
        if (messageModels.get(position).getuId().equals(FirebaseAuth.getInstance().getUid())) {
            String message = messageModels.get(position).getMessageType();
            if (message != null && message.equals("ImagePics")) return SENDER_IMAGE;
            //if (message != null && message.equals("RecordAudio")) return SENDER_AUDIO_RECORD;
            return SENDER_VIEW_TYPE;
        } else {
            String message = messageModels.get(position).getMessageType();
            if (message != null && message.equals("ImagePics")) return RECEIVER_IMAGE;
            //  if (message != null && message.equals("RecordAudio")) return RECEIVER_AUDIO_RECORD;
            return RECEIVE_VIEW_TYPE;
        }
    }

    @Override
    public int getItemCount() {
        return messageModels.size();
    }

    public static class receiveViewHolder extends RecyclerView.ViewHolder {
        TextView messageRec, messageRecTime, img_rec_time;
        ImageView recImage;
        //VoicePlayerView receiveRecord;

        public receiveViewHolder(@NonNull View itemView) {
            super(itemView);
            messageRec = itemView.findViewById(R.id.messageRec);
            messageRecTime = itemView.findViewById(R.id.messageRecTime);
            recImage = itemView.findViewById(R.id.recImage);
            img_rec_time = itemView.findViewById(R.id.img_rec_time);
            // receiveRecord = itemView.findViewById(R.id.RecVoicePlayerView);
        }
    }

    public static class sendViewHolder extends RecyclerView.ViewHolder {
        TextView messageSen, messageSenTime, img_sen_time;
        ImageView sendImage;
        // VoicePlayerView sendRecord;


        public sendViewHolder(@NonNull View itemView) {
            super(itemView);
            messageSen = itemView.findViewById(R.id.messageSen);
            messageSenTime = itemView.findViewById(R.id.messageSenTime);
            sendImage = itemView.findViewById(R.id.sendImage);
            img_sen_time = itemView.findViewById(R.id.img_sen_time);
            //  sendRecord = itemView.findViewById(R.id.SendVoicePlayerView);
        }
    }
}
