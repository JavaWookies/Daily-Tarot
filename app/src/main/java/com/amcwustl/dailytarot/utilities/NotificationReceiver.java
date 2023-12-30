package com.amcwustl.dailytarot.utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {

    private static final String TAG = "NotificationReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        int notificationId = intent.getIntExtra("notification_id", 0);
        String channelId = intent.getStringExtra("channel_id");


        // Pass the channel ID to the buildNotification method
        NotificationHelper.buildNotification(context, notificationId, channelId);
    }
}


