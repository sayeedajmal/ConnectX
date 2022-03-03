package com.Strong.personalchat.Adaptors;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.Strong.personalchat.models.message;
import com.Strong.personalchat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class messageAdaptor extends  RecyclerView.Adapter{
    ArrayList<message> messageModels;
    Context context;

    int SENDER_VIEW_TYPE=1;
    int RECEIVE_VIEW_TYPE =2;
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
            return new receiveViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        message message=messageModels.get(position);

        if (holder.getClass()==sendViewHolder.class){
            ((sendViewHolder)holder).messageSen.setText(message.getMessage());
            Date timeD = new Date(message.getTimeStamp());
            ((sendViewHolder)holder).messageSenTime.setText(ShowDateTime(timeD));
        }
        else{
            ((receiveViewHolder)holder).messageRec.setText(message.getMessage());
            Date timeD = new Date(message.getTimeStamp());
            ((receiveViewHolder)holder).messageRecTime.setText(ShowDateTime(timeD));

            //Setting Sender Image to the chat's left Side
            /*database=FirebaseDatabase.getInstance();
            String senderId=message.getuId();
            database.getReference().child("Users").child(senderId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                            String receiverImage=dataSnapshot.child("chatUserImage").getValue(String.class);
                           // ((receiveViewHolder)holder).receiverImage.setImageBitmap(receiverImage);
                            Picasso.get().load(receiverImage).into(((receiveViewHolder) holder).receiverImage);
                        }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            }); */
        }
    }

    private String ShowDateTime(Date date){
        return new SimpleDateFormat("dd,MM,yyyy - hh:mm a", Locale.getDefault()).format(date);
    }
    @Override
    public int getItemViewType(int position) {
        if (messageModels.get(position).getUid().equals(FirebaseAuth.getInstance().getUid())){
            return SENDER_VIEW_TYPE;
        }
        else{
            return RECEIVE_VIEW_TYPE;
        }
    }

    @Override
    public int getItemCount() {
        return messageModels.size();
    }

    public static class receiveViewHolder extends RecyclerView.ViewHolder{
        TextView messageRec, messageRecTime;
        CircleImageView receiverImage;
        public receiveViewHolder(@NonNull View itemView) {
            super(itemView);
            receiverImage=itemView.findViewById(R.id.receiverImage);
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
