package com.snowdays.snowdaysctrl.activities;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.nio.charset.Charset;

import nordpol.Apdu;
import nordpol.IsoCard;
import nordpol.android.AndroidCard;
import nordpol.android.OnDiscoveredTagListener;
import nordpol.android.TagDispatcher;

/**
 * Created by danielmorandini on 20/12/2016.
 */

public abstract class BaseNFCActivity extends AppCompatActivity {

    private PendingIntent pendingIntent;
    private IntentFilter[] intentFiltersArray;
    private String[][] techListsArray;
    private NfcAdapter mAdapter;

    //public API
    public abstract void responseData(String data);
    public abstract void responseError(Exception e);
    public abstract void responseError();
    public abstract void readingStarted();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initialize the adapter
        mAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mAdapter == null) {
            Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        /*
        Copy and pasted code from https://developer.android.com/guide/topics/connectivity/nfc/advanced-nfc.html
        */
        pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndef.addDataType("*/*");    /* Handles all MIME based dispatches.
                                       You should specify only the ones that you need. */
        }
        catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }

        intentFiltersArray = new IntentFilter[]{ndef,};

        techListsArray = new String[][]{new String[]{NfcF.class.getName()}};
    }

    public void onPause() {
        super.onPause();
        mAdapter.disableForegroundDispatch(this);
    }

    public void onResume() {
        super.onResume();
        mAdapter.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, techListsArray);
    }

    public void onNewIntent(Intent intent) {
        readingStarted();

        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag == null) {
            responseError();
        }

        String value = readTag(tag);
        if (value != null) {
            responseData(value);
        }
    }

    // Actions with tag
    public String readTag(Tag tag) {
        Ndef ndef = Ndef.get(tag);
        try {
            ndef.connect();
            NdefMessage message = ndef.getNdefMessage();

            //get the actual message
            String value = new String(message.getRecords()[0].getPayload(), Charset.forName("US-ASCII"));
            if (value != null && value.length() > 2) {
                return value.substring(3); // remove language info
            }

        } catch (IOException e) {
            responseError(e);
        } catch (FormatException e) {
            responseError(e);
        } finally {
            if (ndef != null) {
                try {
                    ndef.close();
                }
                catch (IOException e) {
                    responseError(e);
                }
            }
        }
        return null;
    }
}

