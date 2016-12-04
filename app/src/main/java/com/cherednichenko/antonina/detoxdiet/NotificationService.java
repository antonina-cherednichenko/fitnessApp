package com.cherednichenko.antonina.detoxdiet;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.cherednichenko.antonina.detoxdiet.detox_diet_data.ProgramInfo;
import com.cherednichenko.antonina.detoxdiet.detox_diet_program_info.ProgramInfoActivity;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by tonya on 10/9/16.
 */
public class NotificationService extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent alarmIntent) {
        Resources resources = context.getResources();
        ProgramInfo receipe = (ProgramInfo) alarmIntent.getSerializableExtra(resources.getString(R.string.extra_receipe));
        if (receipe == null) {

            //There is some mistake in new Android version, added this workaround
            //check here http://stackoverflow.com/questions/38775285/android-7-broadcastreceiver-onreceive-intent-getextras-missing-data
            DateFormat dateFormat = new SimpleDateFormat(resources.getString(R.string.preferences_date_format));
            Calendar cal = Calendar.getInstance();
            String receipeKey = dateFormat.format(cal.getTime());

            SharedPreferences sharedPref = context.getSharedPreferences(resources.getString(R.string.schedule_prefernces), Context.MODE_PRIVATE);
            String json = sharedPref.getString(receipeKey, "No program found");
            try {
                Gson gson = new Gson();
                receipe = gson.fromJson(json, ProgramInfo.class);
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }

        if (receipe != null) {
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
}
