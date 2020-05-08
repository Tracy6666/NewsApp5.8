package com.example.newsapplication;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseInstallation;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity  {

    private NewsAdapter adapter;
    private String JsonLink = "http://newsapi.org/v2/top-headlines?country=us&category=health&apiKey=84499478883a4727bfba30a6a52cc320";
    private String LOG_TAG = MainActivity.class.getName();
    private TextView emptyView;
    private static final int newsId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseInstallation.getCurrentInstallation().saveInBackground();

        BottomNavigationView bottomNav = findViewById(R.id.navigation);
        bottomNav.setOnNavigationItemSelectedListener(naviListener);
        bottomNav.setSelectedItemId(R.id.nav_home);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener naviListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {

                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            break;

                        case R.id.nav_search:
                            selectedFragment = new SearchFragment();
                            break;

                        case R.id.nav_chat:
                            selectedFragment = new ChatFragment();
                            break;

                        case R.id.nav_info:
                            selectedFragment = new InfoFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };


}
