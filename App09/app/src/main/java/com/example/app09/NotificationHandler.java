package com.example.app09;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationHandler extends ContextWrapper {

    private NotificationManager manager;

    public final static String CHANNEL_HIGH_ID="1";
    public final static String CHANNEL_HIGH_NAME="HIGH CHANNEL";
    public final static String CHANNEL_LOW_ID="2";
    public final static String CHANNEL_LOW_NAME="LOW CHANNEL";

    public NotificationHandler(Context base) {
        super(base);
        createChannels();
    }

    private void createChannels() {
        if(Build.VERSION.SDK_INT>=26){
            NotificationChannel highChannel=
                    new NotificationChannel(CHANNEL_HIGH_ID,CHANNEL_HIGH_NAME,NotificationManager.IMPORTANCE_HIGH);
            highChannel.enableLights(true);
            highChannel.setShowBadge(true);
            highChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationChannel lowChannel=
                    new NotificationChannel(CHANNEL_LOW_ID,CHANNEL_LOW_NAME,NotificationManager.IMPORTANCE_LOW);
            lowChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            getManager().createNotificationChannel(highChannel);
            getManager().createNotificationChannel(lowChannel);
        }
    }

    private Notification.Builder createNotification(String title,String text,boolean priority){
        if(Build.VERSION.SDK_INT==Build.VERSION_CODES.O){
            if(priority){
                return this.createNotificationsChannels(title,text,CHANNEL_HIGH_ID);
            }else{
                return this.createNotificationsChannels(title,text,CHANNEL_LOW_ID);

            }

        }else{
            return this.createNotificationsWithoutChannels(title,text);
        }
    }

    public Notification.Builder createNotificationsWithoutChannels(String title, String text) {
        return null;
    }

    public Notification.Builder createNotificationsChannels(String title, String text, String channelID) {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            /*
            Intent intent =new Intent(this,DetailActivity.class);
            intent.putExtra("title",title);
            intent.putExtra("text",text);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent= PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
            return new Notification.Builder(getApplicationContext(),channelID).setContentTitle(title).
                    setContentText(text).setSmallIcon(R.drawable.ic_launcher_background).setAutoCancel(true);

             */

            Notification.Builder builder = new Notification.Builder(this,channelID)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle(title)
                    .setContentText(text);

            return builder;
        }
        return null;

    }


    public NotificationManager getManager(){
        if(manager==null){
            manager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }
}