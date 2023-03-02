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

import java.util.Objects;

public class notification extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }

/*
    public static void sendNotification(String Username, String Message, Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "TEST")
                .setColor(Color.rgb(255, 255, 255))
                .setColorized(true).setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.mipmap.icon_foreground)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setOnlyAlertOnce(false).setSilent(false).setContentTitle(Username)
                .setContentText(Message).setCustomBigContentView(getCustomDesign(Username, Message, context));

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel = new NotificationChannel("TEST", "PersonalChat", NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.enableVibration(true);
        notificationChannel.enableLights(true);
        builder.setAutoCancel(true);
        notificationManager.createNotificationChannel(notificationChannel);
        notificationManager.notify(0, builder.build());
    }

    // Method to get the custom Design for the display of notification.
    private static RemoteViews getCustomDesign(String UserName, String message, Context context) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification);
        remoteViews.setTextViewText(R.id.Nofi_UserName, UserName);
        remoteViews.setTextViewText(R.id.Noti_Message, message);

        remoteViews.setImageViewResource(R.id.Noti_Image, R.mipmap.icon);
        return remoteViews;
    }
*/

    // Method to display the notifications
    public void updateApplication(String title, String message) {
        Uri url = Uri.parse("https://github.com/sayeedajmal/Applications/raw/master/PersonalChat/app-release.apk");
        Intent intent = new Intent(Intent.ACTION_VIEW, url);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        String channel_id = "UPDATE";
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        Bitmap bitmap = BitmapFactory.decodeResource(Resources.getSystem(), R.mipmap.icon_foreground);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channel_id).setColor(Color.rgb(255, 255, 255)).setLargeIcon(bitmap).setColorized(true).setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE).setChronometerCountDown(true).setVisibility(NotificationCompat.VISIBILITY_PUBLIC).setSmallIcon(R.mipmap.icon_foreground).setVibrate(new long[]{1000, 1000, 1000, 1000, 1000}).setOnlyAlertOnce(false).setSilent(false).setContentTitle(title).setContentText(message).setContentIntent(pendingIntent);

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

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        /*Update Application*/
        if (Objects.equals(Objects.requireNonNull(remoteMessage.getNotification()).getChannelId(), "UPDATE"))
            if (remoteMessage.getNotification() != null) {
                updateApplication(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
            }

        /*Notify Message*/
      /*  if (remoteMessage.getNotification().getChannelId().equals("Message"))
            if (remoteMessage.getNotification() != null)
                Toast.makeText(this, "SomeOne is Online.", Toast.LENGTH_SHORT).show();*/
    }
}
