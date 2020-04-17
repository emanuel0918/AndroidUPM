package com.example.app09;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private final static String TAG="MainActivity";
    private NotificationHandler notificationHandler;
    private EditText titulo;
    private EditText mensaje;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notificationHandler = new NotificationHandler(this);
        titulo = findViewById(R.id.editText);
        mensaje = findViewById(R.id.editText2);
        Button high = (Button) findViewById(R.id.button);
        high.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification(true);
            }
        });
        Button low = (Button) findViewById(R.id.button2);
        low.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification(false);

            }
        });

    }

    private void sendNotification(boolean priority) {
        String title = titulo.getText().toString();
        String text= mensaje.getText().toString();
        String channelId=(priority)?NotificationHandler.CHANNEL_HIGH_ID:NotificationHandler.CHANNEL_LOW_ID;
        Notification.Builder nb = notificationHandler.createNotificationsChannels(title,text,channelId);
        notificationHandler.getManager().notify(001,nb.build());


    }
}
