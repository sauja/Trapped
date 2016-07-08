package com.thehrmsinc.trapped.notification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.thehrmsinc.trapped.R;

/**
 * Created by Acer on 6/1/2016.
 */

public class NotificationHelper {
    private static final String TAG = NotificationHelper.class.getSimpleName();
    Context context;

    public NotificationHelper(Context context) {
        this.context = context;
    }

    public void scheduleNotification(Notification notification, long delay,int option) {

        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        notificationIntent.putExtra("clickedButton", option);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        Log.e(TAG,"in schedule notification");
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    public  Notification getNotification(String content) {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle("Scheduled Notification");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_launcher);
        return builder.build();
    }
}
