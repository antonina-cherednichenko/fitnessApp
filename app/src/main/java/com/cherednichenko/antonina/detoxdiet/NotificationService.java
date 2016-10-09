package com.cherednichenko.antonina.detoxdiet;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import com.cherednichenko.antonina.detoxdiet.detox_diet_data.ProgramInfo;
import com.cherednichenko.antonina.detoxdiet.detox_diet_program_info.ProgramInfoActivity;

/**
 * Created by tonya on 10/9/16.
 */
public class NotificationService extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent alarmIntent) {
        ProgramInfo receipe = (ProgramInfo) alarmIntent.getSerializableExtra("receipe_info");
        Intent intent = new Intent(context, ProgramInfoActivity.class);
        intent.putExtra("receipe_info", receipe);

        int requestID = (int) System.currentTimeMillis(); //unique requestID to differentiate between various notification with same NotifId
        int flags = PendingIntent.FLAG_CANCEL_CURRENT; // cancel old intent and create new one
        PendingIntent pIntent = PendingIntent.getActivity(context, requestID, intent, flags);

        Notification noti = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.glass)
                .setContentTitle("Detox and Diet reminds")
                .setContentText("Now is time for " + receipe.getName())
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .build();

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(0, noti);

    }
}
