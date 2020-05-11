package com.example.appnewspaper;

import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.appnewspaper.model.Article;
import com.example.appnewspaper.utils.network.exceptions.ServerCommunicationError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONException;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {

    SharedPreferences rememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        rememberMe = getSharedPreferences("rememberMe", Context.MODE_PRIVATE);


        FloatingActionButton fab = findViewById(R.id.floatingActionButtonMain);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goLogin = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(goLogin);
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Map<String, ?> map = rememberMe.getAll();
        Boolean mantenerSesion = (Boolean) map.get("stayLogged");
        String user = (String) map.get("user");
        String password = (String) map.get("password");
        String apiKey = (String) map.get("apiKey");
        String authType = (String) map.get("authUser");

        if (mantenerSesion == null) {
            SharedPreferences.Editor editorTwo = rememberMe.edit();
            editorTwo.putBoolean("stayLogged", false);
            mantenerSesion = false;
            editorTwo.commit();
        }
        // boolean param = rememberMe.getBoolean("stayLogged", false); //recuperar el valor de una variable
        if (mantenerSesion) {
            LoadLoginTask loginTask = new LoadLoginTask();
            loginTask.stayLoggin = mantenerSesion;
            loginTask.user = user;
            loginTask.password = password;
            loginTask.apiKey = apiKey;
            loginTask.authType = authType;
            loginTask.execute();
            try {
                String result = loginTask.get();
                //Aqui llamar a login y hacer un stayloggin con user, password y apikey
                Intent goMainPage = new Intent(getBaseContext(), MainActivityAfterLogin.class);
                startActivity(goMainPage);
            } catch (ExecutionException e) {

                e.printStackTrace();
            } catch (InterruptedException e) {

                e.printStackTrace();
            }


        } else {
            SharedPreferences.Editor editor = rememberMe.edit();
            editor.putBoolean("stayLogged", false);
            editor.commit();
            AsyncTask<Void, Void, List<Article>> p = new LoadArticlesTask().execute();
            //new FetchDataTask().execute("http://sanger.dia.fi.upm.es/pmd-task/articles");
            //new FetchDataTask().execute("https://DEV_TEAM_07:89423@sanger.dia.fi.upm.es/pmd-task/");
            try {
                final List<Article> articles2 = p.get();
                ListView listNews = findViewById(R.id.newsList);
                ArrayList<Article> myData = (ArrayList<Article>) articles2;
                AdapterBeforeLogin adapter = new AdapterBeforeLogin(this, myData);
                listNews.setAdapter(adapter);

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
