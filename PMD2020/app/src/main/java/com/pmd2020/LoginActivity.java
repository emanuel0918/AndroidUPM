package com.pmd2020;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {
    private Button login_btn;
    private TextInputEditText user_editText;
    private TextInputEditText password_editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user_editText =(TextInputEditText)findViewById(R.id.user);
        password_editText =(TextInputEditText)findViewById(R.id.password);

        login_btn=(Button)findViewById(R.id.login_btn);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(logIn()){
                    String usr= user_editText.getText().toString();
                    Intent intent= new Intent(LoginActivity.this,MainActivity.class);
                    intent.putExtra("user",usr);
                    startActivity(intent);
                }
            }
        });
    }

    private boolean logIn() {
        boolean login=false;
        String usr= user_editText.getText().toString();
        String pass= password_editText.getText().toString();
        if(usr.equals("user") && (pass.equals("") || pass.equals("0000"))){
            login=true;
        }
        return login;
    }
}
