package com.snowdays.snowdaysctrl.activities.base;

import android.support.v7.app.ActionBar;
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

    public void loadToolbar(String title) {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        // Get the ActionBar here to configure the way it behaves.
        getSupportActionBar().setDisplayShowHomeEnabled(true); // show or hide the default home button;
        getSupportActionBar().setTitle("  " + title);
        getSupportActionBar().setIcon(R.drawable.ic_snowdays_snowflake);
    }
}
