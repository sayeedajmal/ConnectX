package com.Strong.ConnectX.Adaptors;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.Strong.ConnectX.Activity.userDataImage;
import com.Strong.ConnectX.R;
import com.Strong.ConnectX.models.message;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class messageAdaptor extends RecyclerView.Adapter {
    ArrayList<message> messageModels;
    Context context;
    Intent intent;
    int SENDER_VIEW_TYPE = 1;
    int RECEIVE_VIEW_TYPE = 2;/*
    int SENDER_AUDIO_RECORD = 3;
    int RECEIVER_AUDIO_RECORD = 4;*/
    int SENDER_IMAGE = 3;
    int RECEIVER_IMAGE = 4;
    int SENDER_EMOJI = 5;
    int RECEIVER_EMOJI = 6;
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
        }/* else if (viewType == SENDER_EMOJI) {
            view = LayoutInflater.from(context).inflate(R.layout.sample_send_emoji, parent, false);
            return new receiveViewHolder(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.sample_recieve_emoji, parent, false);
            return new receiveViewHolder(view);
        }
        else if (viewType == RECEIVER_AUDIO_RECORD) {
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
        String Seen = messageModels.get(position).getSeen();
        //String audioMessage = messageModels.get(position).getMessageType();
        int linkColor= ContextCompat.getColor(context,R.color.lightBlue);

        //Show Messages according to there sides
        switch (getItemViewType(position)) {
            case 1:
                if (isLink(message.getMessage())){
                    ((sendViewHolder)holder).messageSen.setTextColor(linkColor);
                    ((sendViewHolder) holder).messageSen.setText(message.getMessage());
                }else
                    ((sendViewHolder) holder).messageSen.setText(message.getMessage());
                Date sendTime = new Date(message.getTimeStamp());
                ((sendViewHolder) holder).messageSenTime.setText(ShowSend(sendTime));
                if (Seen != null && Seen.equals("yes"))
                    ((sendViewHolder) holder).seen.setVisibility(View.VISIBLE);
                break;
            case 2:
                if (isLink(message.getMessage())){
                    ((receiveViewHolder)holder).messageRec.setTextColor(linkColor);
                    ((receiveViewHolder) holder).messageRec.setText(message.getMessage());
                }else
                    ((receiveViewHolder)holder).messageRec.setText(message.getMessage());
                Date receiveTime = new Date(message.getTimeStamp());
                ((receiveViewHolder) holder).messageRecTime.setText(ShowDateTime(receiveTime));
                break;
            case 3:
                if (ImagePics != null && ImagePics.equals("ImagePics"))
                    Glide.with(context).load(message.getMessage()).into(((sendViewHolder) holder).sendImage);
                Date sendImgTime = new Date(message.getTimeStamp());
                ((sendViewHolder) holder).img_sen_time.setText(ShowSend(sendImgTime));
                break;
            case 4:
                if (ImagePics != null && ImagePics.equals("ImagePics"))
                    Glide.with(context).load(message.getMessage()).into(((receiveViewHolder) holder).recImage);
                Date recImgTime = new Date(message.getTimeStamp());
                ((receiveViewHolder) holder).img_rec_time.setText(ShowDateTime(recImgTime));
                break;
          /*  case 5:
                if (message.getMessage().startsWith(emojiStart)) {
                    ((sendViewHolder) holder).sendEmoji.setText(message.getMessage());
                }
            case 6:
                if (message.getMessage().startsWith(emojiStart)) {
                    ((receiveViewHolder) holder).ReceiveEmoji.setText(message.getMessage());
                }
           case 3:
                if (audioMessage != null && audioMessage.equals("RecordAudio"))
                    ((sendViewHolder) holder).sendRecord.setAudio(message.getMessage());
                break;
            case 4:
                if (audioMessage != null && audioMessage.equals("RecordAudio"))
                    ((receiveViewHolder) holder).receiveRecord.setAudio(message.getMessage());
                break;*/
        }
        holder.itemView.setOnClickListener(view -> {
            if (ImagePics != null && ImagePics.equals("ImagePics")) {
                intent = new Intent(context, userDataImage.class);
                intent.putExtra("Image", message.getMessage());
                context.startActivity(intent);
            }else if (isLink(message.getMessage())){

                Pattern pattern=Pattern.compile("(https://)?[a-zA-Z0-9]+(\\.[a-zA-Z]+)+$");
                Matcher matcher=pattern.matcher(message.getMessage());
                if (matcher.find()) {
                    String Url=matcher.group();
                    if (!Url.startsWith("http://") || !Url.startsWith("https://")) {
                       Url= "https://"+Url;
                       context.startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(Url)));
                    }
                }
            }
        });

        switch (getItemViewType(position)){
            case 1:
                holder.itemView.setOnLongClickListener(view -> {
                    TextOptions(view,Gravity.END,message.getMessage());
                    return false;
                });
                break;
            case 2:
                holder.itemView.setOnLongClickListener(view -> {
                    TextOptions(view,Gravity.START,message.getMessage());
                    return false;
                });
                break;
        }
    }

    private boolean isLink(String text){
        Pattern pattern=Pattern.compile("(https://)?[a-zA-Z0-9]+(\\.[a-zA-Z]+)+$");
        return pattern.matcher(text).find();
    }

    private void TextOptions(View view, int gravity,String text){
        PopupMenu popupMenu = new PopupMenu(context, view, gravity);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.textoption, popupMenu.getMenu());
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.copyText:
                    copyText(text);
                    Toast.makeText(context, "Text Copied!", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.deleteText:
                    Toast.makeText(context, "You Clicked on Delete", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.shareText:
                    Toast.makeText(context,"You Clicked On Share", Toast.LENGTH_SHORT).show();
                default:
                    return false;
            }
        });
    }

    private void copyText(String text){
        ClipboardManager clipboardManager= (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData=ClipData.newPlainText("ConnectX",text);
        clipboardManager.setPrimaryClip(clipData);
    }
    //BottomSheet
 /*   private void deleteChat() {
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
    }*/

    private String ShowDateTime(Date date) {
        return new SimpleDateFormat("dd.MM.yy - hh:mm a", Locale.getDefault()).format(date);
    }

    private String ShowSend(Date date) {
        return new SimpleDateFormat("hh:mm a- dd.MM.yy", Locale.getDefault()).format(date);
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
            String emoji = messageModels.get(position).getMessage();
            if (message != null && message.equals("ImagePics")) return RECEIVER_IMAGE;
            //  if (message != null && message.equals("RecordAudio")) return RECEIVER_AUDIO_RECORD;
            return RECEIVE_VIEW_TYPE;
        }
    }

    @Override
    public int getItemCount() {
        return messageModels.size();
    }
}
