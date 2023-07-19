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

import com.Strong.ConnectX.Activity.recentActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

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

        /*//Get Update Notification
        if (Objects.equals(remoteMessage.getNotification().getTitle(), "Update Application")) {
            updateApplication(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        } else*/
        // Push Notification Message
        pushMessageNotification(remoteMessage.getData().get("Title"), remoteMessage.getData().get("Message"));
    }

    public void updateApplication(String title, String message) {
        Uri url = Uri.parse("https://github.com/sayeedajmal/Applications/raw/master/PersonalChat/app-release.apk");
        Intent intent = new Intent(Intent.ACTION_VIEW, url);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        String channel_id = "UPDATE";
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        Bitmap bitmap = BitmapFactory.decodeResource(Resources.getSystem(), R.mipmap.ic_launcher_foreground);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channel_id).setColor(Color.rgb(255, 255, 255)).setLargeIcon(bitmap).setColorized(true).setSmallIcon(R.mipmap.ic_launcher_foreground).setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE).setChronometerCountDown(true).setVisibility(NotificationCompat.VISIBILITY_PUBLIC).setVibrate(new long[]{1000, 1000, 1000, 1000, 1000}).setOnlyAlertOnce(false).setContentTitle(title).setContentText(message).setContentIntent(pendingIntent);

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

    private void pushMessageNotification(String Title, String Message) {
        String channel_id = "MESSAGE";
        Intent intent = new Intent(this, recentActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        Bitmap bitmap = BitmapFactory.decodeResource(Resources.getSystem(), R.mipmap.ic_launcher_foreground);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channel_id).setColor(Color.rgb(255, 255, 255)).setLargeIcon(bitmap).setColorized(true).setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE).setChronometerCountDown(true).setVisibility(NotificationCompat.VISIBILITY_PUBLIC).setSmallIcon(R.mipmap.ic_launcher_foreground).setVibrate(new long[]{1000, 1000, 1000, 1000, 1000}).setOnlyAlertOnce(false).setSilent(false).setContentTitle(Title).setContentText(Message).setContentIntent(pendingIntent);

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
}
