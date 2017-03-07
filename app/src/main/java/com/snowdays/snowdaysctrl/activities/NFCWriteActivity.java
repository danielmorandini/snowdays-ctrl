package com.snowdays.snowdaysctrl.activities;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.snowdays.snowdaysctrl.R;
import com.snowdays.snowdaysctrl.fragments.NFCProgressFragment;
import com.snowdays.snowdaysctrl.models.Participant;
import com.snowdays.snowdaysctrl.utilities.NetworkService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by danielmorandini on 07/03/2017.
 */

public final class NFCWriteActivity extends NFCActivity {

    public final static String EXTRA_PARTICIPANT = "com.snowdays.snowdaysctrl.EXTRA_PARTICIPANT";
    private Participant participant;

    // UI
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve card info from intent
        participant = (Participant) getIntent().getParcelableExtra(EXTRA_PARTICIPANT);

        // UI
        loadToolbar(participant.getFirstName() + " " + participant.getLastName());
        subtitleTV.setText("This will write " + participant.getLastName() + "'s ID into the TAG");
    }

    //NFC Tag discovery

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
        super.responseData(data);
        setMessage(data);
        updateParticipant(data);
    }

    //HTTP Requests

    public void updateParticipant(String participantId) {
        if (mCall != null) mCall.cancel();

        NFCProgressFragment item = createFragment("HTTP request");
        mStack.push(item);

        Map<String, Boolean> body = new HashMap<>();
        body.put("checkedIn", true);

        mCall = NetworkService.getInstance().updateParticipant(getHeaders(), participant.getId(), body);
        mCall.enqueue(this);
    }
}
