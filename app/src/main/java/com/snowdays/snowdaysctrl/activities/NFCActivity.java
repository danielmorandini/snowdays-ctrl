package com.snowdays.snowdaysctrl.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.snowdays.snowdaysctrl.R;

import java.io.IOException;

public class NFCActivity extends BaseNFCActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
    }

    private void setMessage(String message) {
        Log.d("NFCActivity", message);
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void responseData(String data) {
        setMessage(data);
    }

    @Override
    public void responseError(IOException e) {
        setMessage("Error while reading the card");
        Log.e("NFCActivity", "Error reading the card", e);
    }

    @Override
    public void readingStarted() {
        Log.d("NFCActivity", "Reading started");
    }
}

