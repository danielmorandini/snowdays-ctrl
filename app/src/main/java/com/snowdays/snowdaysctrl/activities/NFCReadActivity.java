package com.snowdays.snowdaysctrl.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import com.snowdays.snowdaysctrl.R;
import com.snowdays.snowdaysctrl.fragments.NFCProgressFragment;
import com.snowdays.snowdaysctrl.models.MainCard;
import com.snowdays.snowdaysctrl.utilities.NetworkService;

import java.util.HashMap;
import java.util.Map;


public final class NFCReadActivity extends NFCActivity {

    // Global
    public final static String EXTRA_CARD = "com.snowdays.snowdaysctrl.EXTRA_CARD";
    private MainCard mCard;
    public static int placesOnBus = 0;

    // UI
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve card info from intent
        mCard = (MainCard) getIntent().getSerializableExtra(EXTRA_CARD);

        loadToolbar(mCard.getName());
        subtitleTV.setText(mCard.getSubtitle());


        showDialog();
    }

    public void showDialog() {
        if(mCard.getType().equals("transportation") && placesOnBus == 0) {
            AlertDialog dialog = (AlertDialog) createDialog();
            dialog.show();
        }
    }

    public Dialog createDialog() {
        AlertDialog.Builder d = new AlertDialog.Builder(NFCReadActivity.this);
        d.setTitle("Transportation");
        d.setMessage("Please, select the number of places on the bus");


        final NumberPicker np = new NumberPicker(this);
        np.setMaxValue(100); // max value 100
        np.setMinValue(10);   // min value 0
        np.setWrapSelectorWheel(false);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        np.setLayoutParams(lp);
        d.setView(np);


        d.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        placesOnBus = np.getValue();
                        setMessage(String.valueOf(placesOnBus));
                    }
                });


        d.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

            }
        });

        return d.create();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_nfc, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:

                Intent intent = new Intent(this, ParticipantListActivity.class);
                intent.putExtra(ParticipantListActivity.ARG_ACTION_KEY, mCard.getCheckAction());
                intent.putExtra("myTitle", mCard.getName());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //NFC Tag discovery
    @Override
    public void responseData(String data) {
        super.responseData(data);
        updateParticipant(data);

    }

    //HTTP Requests

    public void updateParticipant(String participantId) {
        if (mCall != null) mCall.cancel();

        NFCProgressFragment item = createFragment("HTTP request");
        mStack.push(item);

        Map<String, Boolean> body = new HashMap<>();
        body.put(mCard.getCheckAction(), true);

        mCall = NetworkService.getInstance().updateParticipant(getHeaders(), participantId, body);
        mCall.enqueue(this);
    }

    

}
