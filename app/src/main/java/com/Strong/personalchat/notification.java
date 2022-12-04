package com.Strong.personalchat;

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

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class notification extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {
            showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }
    }

    // Method to get the custom Design for the display of notification.
    /*private RemoteViews getCustomDesign(String title, String message) {
        @SuppressLint("RemoteViewLayout") RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.notification);
        remoteViews.setTextViewText(R.id.title, title);
        remoteViews.setTextViewText(R.id.message, message);
        remoteViews.setImageViewResource(R.id.icon, R.mipmap.icon_foreground);
        return remoteViews;
    }*/

    // Method to display the notifications
    public void showNotification(String title, String message) {
        Uri url = Uri.parse("https://firebasestorage.googleapis.com/v0/b/personalchat-d14fe.appspot.com/o/app-release.apk?alt=media&token=d48333c6-58f4-4c86-b154-0ae575ee4ae2");
        Intent intent = new Intent(Intent.ACTION_VIEW, url);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        String channel_id = "UPDATE";
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        Bitmap bitmap = BitmapFactory.decodeResource(Resources.getSystem(), R.mipmap.icon);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channel_id)
                .setColor(Color.rgb(255, 255, 255))
                .setLargeIcon(bitmap)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.mipmap.icon_foreground)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setOnlyAlertOnce(false).setSilent(false)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent);
        //builder.setContent(getCustomDesign(title, message));

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel = new NotificationChannel(channel_id, "PersonalChat", NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.enableVibration(true);
        notificationChannel.enableLights(true);
        builder.setAutoCancel(true);
        notificationManager.createNotificationChannel(notificationChannel);
        notificationManager.notify(0, builder.build());
    }
}
