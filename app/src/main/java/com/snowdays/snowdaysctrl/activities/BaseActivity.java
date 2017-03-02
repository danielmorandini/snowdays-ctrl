package com.snowdays.snowdaysctrl.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.snowdays.snowdaysctrl.R;
import com.snowdays.snowdaysctrl.utilities.KeyStore;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by danielmorandini on 11/01/2017.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Utils
    public Map<String, String> getHeaders() {
        Map<String, String> map = new HashMap<>();

        map.put("X-Auth-Token", KeyStore.getToken(this));
        map.put("X-User-Id", KeyStore.getUserId(this));
        return map;
    }

    public void setMessage(String message) {
        Log.d("BaseActivity", message);
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void loadToolbar() {
        Toolbar actionToolbar = (Toolbar) findViewById(R.id.snowdays_toolbar);
        setSupportActionBar(actionToolbar);
    }
}
