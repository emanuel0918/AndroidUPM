package com.example.notifications;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText titulo;
    EditText mensaje;

    int counter = 0;
    private NotificationHandler notificationHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notificationHandler = new NotificationHandler(this);

        titulo = findViewById(R.id.tTitle);
        mensaje = findViewById(R.id.tMsg);
        Button bHigh = findViewById(R.id.bHigh);
        bHigh.setOnClickListener(this);
        Button bLow = findViewById(R.id.bLow);
        bLow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bHigh) {
            //Toast.makeText(this, "BOTON HIGH", Toast.LENGTH_LONG).show();
            // LOGICA PROGRAMACION BOTON HIGH
            sendNotification(true);
        } else {
            // LOGICA PROGRAMACION BOTON LOW
            sendNotification(false);
        }
    }

    private void sendNotification (boolean priority) {
        String title = titulo.getText().toString();
        String msg = mensaje.getText().toString();
        Notification.Builder nb = notificationHandler.createNotification(title, msg, priority);
        notificationHandler.getManager().notify(counter++, nb.build());
        notificationHandler.publishNotificationGroup(priority);

    }


}















