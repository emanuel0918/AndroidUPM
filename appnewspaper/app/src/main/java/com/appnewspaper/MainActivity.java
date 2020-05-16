package com.appnewspaper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.design.widget.NavigationView;


import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private boolean session;
    private boolean stayLogged;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setToolbar();
        //Sesion
        SharedPreferences rememberMe = getSharedPreferences("rememberMe", Context.MODE_PRIVATE);
        Map<String, ?> map = rememberMe.getAll();
        Boolean mantenerSesion;
        try {
            mantenerSesion = (Boolean) map.get("stayLogged");
        }catch (Exception e){
            mantenerSesion=null;
        }
        Boolean sesion1;
        try{
            sesion1=(Boolean)map.get("session");
        }catch (Exception e){
            sesion1=false;
        }
        if (mantenerSesion == null) {
            SharedPreferences.Editor editorTwo = rememberMe.edit();
            editorTwo = rememberMe.edit();
            editorTwo.putBoolean("session", false);
            session=false;
            editorTwo.commit();
        }else{
            stayLogged=mantenerSesion;
            session=sesion1;
            if(!sesion1) {
                session = mantenerSesion;
            }
        }
        //
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().clear();
        if(session){
            navigationView.inflateMenu(R.menu.options_user);
        }else{
            navigationView.inflateMenu(R.menu.options_general);
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new ArticleListFragment()).commit();
        navigationView.getMenu().getItem(1).setChecked(true);
        getSupportActionBar().setTitle(navigationView.getMenu().getItem(1).getTitle());

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment f = null;
                switch (item.getItemId()) {
                    case R.id.menu_login:
                        logIn();
                        break;
                    case R.id.menu_1:
                        f = new PublishArticleFragment();
                        break;
                    case R.id.menu_2:
                        f = new ArticleListFragment();
                        break;
                    case R.id.menu_logout:
                        drawerLayout.closeDrawers();
                        stayLogged=false;
                        SharedPreferences rememberMeTwo = getSharedPreferences("rememberMe", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editorTwo = rememberMeTwo.edit();
                        editorTwo.clear();
                        editorTwo.commit();
                        navigationView.getMenu().clear();
                        navigationView.inflateMenu(R.menu.options_general);
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new ArticleListFragment()).commit();
                        navigationView.getMenu().getItem(1).setChecked(true);
                        getSupportActionBar().setTitle(navigationView.getMenu().getItem(1).getTitle());
                        break;
                    //case R.id.otras_1:
                        // f = new MyArticleListFragment();
                    //    break;
                }
                if (f != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, f).commit();
                    item.setChecked(true);
                    getSupportActionBar().setTitle(item.getTitle());
                    drawerLayout.closeDrawers();
                }
                return false;
            }
        });
    }

    private void logIn() {
        Intent login_intent =
                new Intent(MainActivity.this,LoginActivity.class);
        startActivity(login_intent);
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Abrir menu
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_home);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void reload_articles(){
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new ArticleListFragment()).commit();
        navigationView.getMenu().getItem(1).setChecked(true);
        getSupportActionBar().setTitle(navigationView.getMenu().getItem(1).getTitle());

    }

    public void publishArticle(){
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new PublishArticleFragment()).commit();
        navigationView.getMenu().getItem(0).setChecked(true);
        getSupportActionBar().setTitle(navigationView.getMenu().getItem(0).getTitle());

    }



    @Override
    protected void onDestroy() {
        SharedPreferences rememberMeTwo = getSharedPreferences("rememberMe", Context.MODE_PRIVATE);
        SharedPreferences.Editor editorTwo = rememberMeTwo.edit();
        editorTwo.putBoolean("session", false);
        editorTwo.putBoolean("stayLogged", stayLogged);
        editorTwo.commit();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        SharedPreferences rememberMeTwo = getSharedPreferences("rememberMe", Context.MODE_PRIVATE);
        SharedPreferences.Editor editorTwo = rememberMeTwo.edit();
        //editorTwo.putBoolean("session", false);
        editorTwo.putBoolean("stayLogged", stayLogged);
        editorTwo.commit();
        super.onPause();
    }

    @Override
    protected void onStop() {
        SharedPreferences rememberMeTwo = getSharedPreferences("rememberMe", Context.MODE_PRIVATE);
        SharedPreferences.Editor editorTwo = rememberMeTwo.edit();
        //editorTwo.putBoolean("session", false);
        editorTwo.putBoolean("stayLogged", stayLogged);
        editorTwo.commit();
        super.onStop();
    }
}
