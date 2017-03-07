package com.snowdays.snowdaysctrl.activities;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import com.snowdays.snowdaysctrl.R;
import com.snowdays.snowdaysctrl.models.Participant;

/**
 * Created by danielmorandini on 07/03/2017.
 */

public class NFCWriteActivity extends BaseNFCActivity   {

    public final static String EXTRA_PARTICIPANT = "com.snowdays.snowdaysctrl.EXTRA_PARTICIPANT";
    private Participant participant;

    // UI
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);

        // Retrieve card info from intent
        participant = (Participant) getIntent().getParcelableExtra(EXTRA_PARTICIPANT);

        // UI
        loadToolbar(participant.getFirstName());
        TextView subtitleV = (TextView) findViewById(R.id.subtitle);
        subtitleV.setText("This will write " + participant.getLastName() + "'s ID into the TAG");
    }
    // NFC

    public void onNewIntent(Intent intent) {
        readingStarted();

        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag == null) {
            responseError();
        }

        boolean worked = writeTag(tag, participant.getId());
        if (worked) {
            responseData("Tag successfully written!");
        }
    }

    @Override
    public void responseData(String data) {
        setMessage(data);
    }

    @Override
    public void responseError(Exception e) {
        setMessage("Error while reading the card");
        Log.e("NFCActivity", "Error reading the card", e);
    }

    @Override
    public void responseError() {
        setMessage("Error while writing in the card");
    }

    @Override
    public void readingStarted() {

    }
}
