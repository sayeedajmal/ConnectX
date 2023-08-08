package com.Strong.ConnectX;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.Strong.ConnectX.Activity.mainChatActivity;
import com.Strong.ConnectX.Activity.recentActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class notification extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        String auth = FirebaseAuth.getInstance().getUid();
        if (auth != null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
            HashMap<String, Object> object = new HashMap<>();
            object.put("Token", token);
            reference.updateChildren(object);
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String UserName=remoteMessage.getData().get("Title");
        String UID=remoteMessage.getData().get("UID");
        String Image=remoteMessage.getData().get("Image");
        String Message=remoteMessage.getData().get("Message");

        pushMessageNotification(UserName,Message,UID,Image);
    }

    private void pushMessageNotification(String UserName, String Message,String UID,String Image) {
        String channel_id = "MESSAGE";
        Intent intent = new Intent(this, mainChatActivity.class);
        intent.putExtra("username",UserName);
        intent.putExtra("userId",UID);
        intent.putExtra("UserImage",Image);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        Bitmap bitmap = BitmapFactory.decodeResource(Resources.getSystem(), R.mipmap.ic_launcher_foreground);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channel_id)
                .setColorized(true)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE)
                .setChronometerCountDown(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setLargeIcon(bitmap)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setOnlyAlertOnce(false)
                .setSilent(false)
                .setContentTitle(UserName)
               .setContentText(Message)
                .setContentIntent(pendingIntent);


        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(channel_id, "PersonalChat", NotificationManager.IMPORTANCE_HIGH);
        channel.setLockscreenVisibility(1);
        channel.setShowBadge(true);
        channel.enableVibration(true);
        channel.enableLights(true);
        builder.setAutoCancel(true);
        manager.createNotificationChannel(channel);
        manager.notify(0, builder.build());
    }
}
