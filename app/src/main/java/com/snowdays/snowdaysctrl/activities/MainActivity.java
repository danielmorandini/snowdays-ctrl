package com.snowdays.snowdaysctrl.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.snowdays.snowdaysctrl.fragments.MainFragment;
import com.snowdays.snowdaysctrl.models.Activities;
import com.snowdays.snowdaysctrl.R;
import com.snowdays.snowdaysctrl.utilities.KeyStore;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {

    private Activities activities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // load data
        loadData();

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        setFragmentWithTabId(R.id.tab_first_day);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        setFragmentWithTabId(item.getItemId());
                        return true;
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                // Remove saved token and userId
                KeyStore.clearAll(this);

                // Return to the base activity
                Intent intent = new Intent(this, StartActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setFragmentWithTabId(int tabId) {
        MainFragment fragment = new MainFragment();

        switch (tabId) {
            case R.id.tab_first_day:
                fragment.setDatasource(activities.getThursday());
                break;
            case R.id.tab_second_day:
                fragment.setDatasource(activities.getFriday());
                break;
            case R.id.tab_third_day:
                fragment.setDatasource(activities.getSaturday());
                break;
            default:
                fragment.setDatasource(activities.getUtilities());
        }

        //set current fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commitAllowingStateLoss();
    }

    public void loadData() {
        Gson gson = new Gson();

        try {
            InputStream stream = getApplicationContext().getResources().openRawResource(R.raw.activities);
            JsonReader json = new JsonReader(new InputStreamReader(stream, "UTF-8"));
            activities =  gson.fromJson(json, Activities.class);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}

