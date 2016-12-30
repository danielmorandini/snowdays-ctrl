package com.snowdays.snowdaysctrl.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.snowdays.snowdaysctrl.R;
import com.snowdays.snowdaysctrl.utilities.KeyStore;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        // Starting point of our application. Check if token is already saved,
        // otherwise open login view
        Intent intent;

        if(KeyStore.getToken(this) != "notFound" && KeyStore.getUserId(this) != "notFound") {
            intent = new Intent(this, MainActivity.class);
            finish();
            startActivity(intent);
        } else {
            intent = new Intent(this, LoginActivity.class);
            finish();
            startActivity(intent);
        }
    }
}
