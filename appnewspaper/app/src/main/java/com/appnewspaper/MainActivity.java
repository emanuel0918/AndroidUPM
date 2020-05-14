package com.appnewspaper;

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
import android.view.MenuItem;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences rememberMe;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setToolbar();
        //Sesion
        rememberMe = getSharedPreferences("rememberMe", Context.MODE_PRIVATE);
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
        }else{
            if(mantenerSesion) {
                Intent login_intent =
                        new Intent(MainActivity.this,LoginActivity.class);
                startActivity(login_intent);
                finish();
            }else{
                //
                drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                navigationView = (NavigationView) findViewById(R.id.nav_view);

                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new NewListFragment()).commit();
                navigationView.getMenu().getItem(1).setChecked(true);
                getSupportActionBar().setTitle(navigationView.getMenu().getItem(1).getTitle());

                navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment f = null;
                        switch (item.getItemId()) {
                            case R.id.menu_1:
                                f = new PublishNewFragment();
                                break;
                            case R.id.menu_2:
                                f = new NewListFragment();
                                break;
                            case R.id.menu_3:
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                intent.putExtra("user", "");
                                startActivity(intent);
                                finish();
                                break;
                            case R.id.otras_1:
                                f = new MyNewListFragment();
                                break;
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
        }
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
}
