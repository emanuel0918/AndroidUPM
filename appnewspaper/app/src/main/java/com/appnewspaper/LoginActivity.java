package com.example.appnewspaper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;


import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.appnewspaper.utils.network.ModelManager;
import com.example.appnewspaper.utils.network.exceptions.AuthenticationError;

import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Button buttonLogin = (Button) findViewById(R.id.login_btn);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Obtener los valores introducidos en los campos
                TextView usernameForm = (TextView) findViewById(R.id.user);
                String usermane = usernameForm.getText().toString();
                EditText passwordFrom = (EditText) findViewById(R.id.password);
                String password = passwordFrom.getText().toString();


                System.out.println("Username " + usermane + " " + " Password " + password);
                if (usermane.equals("") || password.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle("Attention!");
                    builder.setMessage("Please, fill the user and password");

                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    try {
                        LoadLoginTask loginTask = new LoadLoginTask();
                        CheckBox rememberMe = (CheckBox) findViewById(R.id.rememberMe);
                        loginTask.password = password;
                        loginTask.user = usermane;
                        loginTask.execute();
                        String userLogger = loginTask.get();
                        if (userLogger.equals("0")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setTitle("Attention!");
                            builder.setMessage("Data Incorect");
                            builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        } else {
                            //  DEV_TEAM_07", "89423"
                            Intent goMainPage = new Intent(getBaseContext(), MainActivityAfterLogin.class);
                            startActivity(goMainPage);
                            if (rememberMe.isChecked()) {
                                SharedPreferences rememberMeTwo = getSharedPreferences("rememberMe", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editorTwo = rememberMeTwo.edit();
                                editorTwo.putBoolean("stayLogged", true);
                                editorTwo.putString("user", usermane);
                                editorTwo.putString("password", password);
                                editorTwo.putString("apiKey", password);
                                editorTwo.putString("authUser", password);
                                editorTwo.commit();
                                //System.out.println("EEEEEE QUE LO HE PUESTO");
                            } else {
                                SharedPreferences rememberMeTwo = getSharedPreferences("rememberMe", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editorTwo = rememberMeTwo.edit();
                                editorTwo.putString("idUser", userLogger);
                                editorTwo.commit();
                            }
                        }
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setTitle("Attention!");
                        builder.setMessage("Data Incorect");

                        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setTitle("Attention!");
                        builder.setMessage("Data Incorect");

                        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }

                }


            }
        });
    }


}
