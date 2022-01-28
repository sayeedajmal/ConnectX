package com.Strong.personalchat.Adaptors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.Strong.personalchat.models.message;
import com.Strong.personalchat.R;
import com.google.firebase.auth.FirebaseAuth;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class messageAdaptor extends  RecyclerView.Adapter{
    ArrayList<message> messageModels;
    Context context;

    int SENDER_VIEW_TYPE=1;
    int RECIEVE_VIEW_TYPE=2;

    public messageAdaptor(ArrayList<message> messageModels, Context context) {
        this.messageModels = messageModels;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType==SENDER_VIEW_TYPE){
            view = LayoutInflater.from(context).inflate(R.layout.sample_send, parent, false);
            return new sendViewHolder(view);
        }else{
            view = LayoutInflater.from(context).inflate(R.layout.sample_recieve, parent, false);
            return new recieveViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        message message=messageModels.get(position);

        if (holder.getClass()==sendViewHolder.class){
            ((sendViewHolder)holder).messageSen.setText(message.getMessage());

            Date timeD = new Date(message.getTimeStamp() * 1000);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            String Time = sdf. format(timeD);
            ((sendViewHolder)holder).messageSenTime.setText(Time);

        }
        else{
            ((recieveViewHolder)holder).messageRec.setText(message.getMessage());
            Date timeD = new Date(message.getTimeStamp() * 1000);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            String Time = sdf. format(timeD);
            ((recieveViewHolder)holder).messageRecTime.setText(Time);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (messageModels.get(position).getuId().equals(FirebaseAuth.getInstance().getUid())){
            return SENDER_VIEW_TYPE;
        }
        else{
            return RECIEVE_VIEW_TYPE;
        }
    }

    @Override
    public int getItemCount() {
        return messageModels.size();
    }

    public static class recieveViewHolder extends RecyclerView.ViewHolder{
        TextView messageRec, messageRecTime;
        public recieveViewHolder(@NonNull View itemView) {
            super(itemView);

            messageRec=itemView.findViewById(R.id.messageRec);
            messageRecTime=itemView.findViewById(R.id.messageRecTime);
        }
    }

    public static class sendViewHolder extends RecyclerView.ViewHolder{
        TextView messageSen, messageSenTime;
        public sendViewHolder(@NonNull View itemView) {
            super(itemView);

            messageSen=itemView.findViewById(R.id.messageSen);
            messageSenTime=itemView.findViewById(R.id.messageSenTime);
        }
    }
}
