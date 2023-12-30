package com.amcwustl.dailytarot.utilities;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.amcwustl.dailytarot.MainActivity;
import com.amcwustl.dailytarot.R;

import java.util.Random;

public class NotificationHelper {
    private static final String[] notificationMessages = {
            "Don't forget to check your daily tarot reading!",
            "Your daily tarot reading is waiting for you!",
            "Curious about today's tarot reading?",
            "Unlock the secrets of the day with your tarot reading!",
            "It's time to see what the cards have in store for you!"
    };

    public static void scheduleNotification(Context context, long delay, int notificationId, String channelId) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("notification_id", notificationId);
        intent.putExtra("channel_id", channelId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);



        long futureInMillis = System.currentTimeMillis() + delay;
        alarmManager.set(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent);
    }

    public static void cancelScheduledNotification(Context context, int notificationId) {
        Intent intent = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);


        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }


    public static void buildNotification(Context mContext, int notificationId, String channelId) {
        String randomMessage = notificationMessages[new Random().nextInt(notificationMessages.length)];

        // Create a NotificationCompat.Builder object
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, channelId)
                .setContentTitle("Daily Tarot Reminder")
                .setContentText(randomMessage)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis());

        // For Android Oreo and above, set the channel ID
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder.setChannelId(channelId);
        }

        // Create an intent to open when the notification is clicked
        Intent intent = new Intent(mContext, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Set the intent that will fire when the user taps the notification
        builder.setContentIntent(pendingIntent);

        // Build the notification
        Notification notification = builder.build();

        // Show the notification
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, notification);
    }

}
