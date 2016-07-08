package com.thehrmsinc.trapped.notification;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.thehrmsinc.trapped.MainPage;
import com.thehrmsinc.trapped.R;
import com.thehrmsinc.trapped.storyblock.Bot;
import com.thehrmsinc.trapped.storyblock.StoryBlock;

import java.util.ArrayList;

public class NotificationPublisher extends BroadcastReceiver {
    private static final String TAG = NotificationPublisher.class.getSimpleName();
    public static final String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";

    public void onReceive(Context context, Intent intent) {
/*
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = intent.getParcelableExtra(NOTIFICATION);

        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        notificationManager.notify(id, notification);*/


        NotificationManager notificationManager;
        Notification myNotification;
        Intent myIntent = new Intent(context, MainPage.class);
        Bundle bundle=new Bundle();
        SharedPreferences settings=context.getSharedPreferences(MainPage.MyPREFERENCES, Context.MODE_PRIVATE);
        if(intent.hasExtra("clickedButton")&&settings.contains("block"))
        {
            Log.e(TAG,"Changing block status in shared pref");

            int clickedButton=intent.getIntExtra("clickedButton",0);

                String jsonItems = settings.getString("block", null);
                Gson gson = new Gson();
                StoryBlock block = gson.fromJson(jsonItems, StoryBlock.class);
                ArrayList<Bot> options= block.getOptions().get(clickedButton).getBot();
                for(Bot b:options)
                {
                    if(b.getDelay()>0&&b.isDelayCompleted()==false)
                    {
                        b.setDelayCompleted(true);
                        b.setDelay(0);
                    }
                }
            block.getOptions().get(clickedButton).setBot(options);
            SharedPreferences.Editor editor;
            editor = settings.edit();
            editor.putInt("clickedButton",clickedButton);
            editor.putBoolean("notification",true);
            editor.putString("block", new Gson().toJson(block));
            editor.apply();

        }


        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                myIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        myNotification = new NotificationCompat.Builder(context)
                .setContentTitle("New message from Amena")
                .setContentText("Amena is waiting for you")
                .setTicker("Notification!")
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_launcher)
                .build();

        Log.e(TAG,"In notification publisher");
        notificationManager =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, myNotification);

    }
}