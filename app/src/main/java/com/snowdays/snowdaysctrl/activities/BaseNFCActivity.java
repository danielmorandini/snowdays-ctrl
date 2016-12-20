package com.snowdays.snowdaysctrl.activities;

import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;

import nordpol.Apdu;
import nordpol.IsoCard;
import nordpol.android.AndroidCard;
import nordpol.android.OnDiscoveredTagListener;
import nordpol.android.TagArbiter;
import nordpol.android.TagDispatcher;
import nordpol.android.TagDispatcherBuilder;

/**
 * Created by danielmorandini on 20/12/2016.
 */

public abstract class BaseNFCActivity extends AppCompatActivity implements OnDiscoveredTagListener {

    public TagDispatcher tagDispatcher;

    //public API
    public abstract void responseData(String data);
    public abstract void responseError(IOException e);
    public abstract void readingStarted();

    @Override
    protected void onResume() {
        super.onResume();
        tagDispatcher = TagDispatcher.get(this, this);
        tagDispatcher.enableExclusiveNfc();
    }

    @Override
    public void onPause() {
        super.onPause();
        tagDispatcher.disableExclusiveNfc();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        tagDispatcher.interceptIntent(intent);
    }

    @Override
    public void tagDiscovered(Tag tag) {
        readingStarted();
        try {
            IsoCard isoCard = AndroidCard.get(tag);
            interactWithCard(isoCard);
        } catch(IOException e) {
            responseError(e);
        }
    }

    //Utils
    private void interactWithCard(IsoCard isoCard) {
        try {
            isoCard.connect();
            byte[] payload = isoCard.transceive(Apdu.select("", ""));
            String printableResponse = new String();
            for (int i=0; i<payload.length; i++) printableResponse += (char)payload[i];
            responseData(printableResponse);
            isoCard.close();
        } catch (IOException e) {
            responseError(e);
        }
    }
}

