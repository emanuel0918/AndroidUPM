package com.example.notifications;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class basicActivity extends AppCompatActivity {

    TextView titulo;
    TextView mensaje;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);

        titulo = findViewById(R.id.tituloFinal);
        mensaje = findViewById(R.id.msgFinal);

        if (getIntent()!=null) {
            titulo.setText(getIntent().getStringExtra("title"));
            mensaje.setText(getIntent().getStringExtra("msg"));
        }

    }



}
