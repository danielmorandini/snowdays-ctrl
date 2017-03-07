package com.snowdays.snowdaysctrl.activities;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.snowdays.snowdaysctrl.activities.base.BaseNetworkActivity;
import com.snowdays.snowdaysctrl.adapters.MainCardListAdapter;
import com.snowdays.snowdaysctrl.R;
import com.snowdays.snowdaysctrl.fragments.NFCProgressFragment;
import com.snowdays.snowdaysctrl.models.APIErrorResponse;
import com.snowdays.snowdaysctrl.models.MainCard;
import com.snowdays.snowdaysctrl.models.Participant;
import com.snowdays.snowdaysctrl.models.ResponseData;
import com.snowdays.snowdaysctrl.utilities.ErrorUtils;
import com.snowdays.snowdaysctrl.utilities.KeyStore;
import com.snowdays.snowdaysctrl.utilities.NetworkService;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseNetworkActivity<MainCard[]> {

    private MainCardListAdapter mAdapter;

    // NFC Stuff
    private PendingIntent pendingIntent;
    private IntentFilter[] intentFiltersArray;
    private String[][] techListsArray;
    private NfcAdapter mNFCAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadToolbar(getString(R.string.app_name));

        // specify an adapter
        mAdapter = new MainCardListAdapter(new ArrayList<MainCard>(), this);
        mRecyclerView.setAdapter(mAdapter);

        // load data
        loadData();

        // NFC - copy pasted code from BaseNFCActivity - Could not extend multiple classes
        // TODO: Should revisit the inheritance tree. There is a lot of redundancy between this class and BaseNFCActivity
        //Initialize the adapter
        mNFCAdapter = NfcAdapter.getDefaultAdapter(this);

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

                AlertDialog dialog = (AlertDialog) onCreateDialog();
                dialog.show();

                return true;

            case R.id.action_part_list:

                Intent intent = new Intent(this, ParticipantListActivity.class);
                intent.putExtra("myTitle", "Participant List");
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void loadData() {
        mCall = NetworkService.getInstance().getActivities(getHeaders());
        loadData(mCall);
    }

    // Update

    @Override
    public void onResponse(Call<ResponseData<MainCard[]>> call, Response<ResponseData<MainCard[]>> response) {
        super.onResponse(call, response);
        if (response.isSuccessful()) {
            ArrayList<MainCard> data = new ArrayList<MainCard>(Arrays.asList(response.body().getData()));
            mAdapter.addItems(data);

            entriesTextView.setVisibility(View.VISIBLE);
            entriesTextView.setText("Entries " + mAdapter.getItemCount());
        }
    }

    @Override
    public void onFailure(Call<ResponseData<MainCard[]>> call, Throwable t) {
        super.onFailure(call, t);
    }

    public Dialog onCreateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(R.string.logout_alert_title)
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Remove saved token and userId
                        KeyStore.clearAll(MainActivity.this);

                        // Return to the base activity
                        Intent intent = new Intent(MainActivity.this, StartActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        return builder.create();
    }

    // NFC Discovery/Handling
    public void onPause() {
        super.onPause();
        mNFCAdapter.disableForegroundDispatch(this);
    }

    public void onResume() {
        super.onResume();
        mNFCAdapter.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, techListsArray);
    }

    public void onNewIntent(Intent intent) {

        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag == null) {
            responseError();
        }

        String value = readTag(tag);
        if (value != null) {
            responseData(value);
        }
    }

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
            responseError();
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

    public void responseData(String data) {

        Call<ResponseData<Participant>> call = NetworkService.getInstance().getParticipant(getHeaders(), data);
        call.enqueue(new Callback<ResponseData<Participant>>() {
            @Override
            public void onResponse(Call<ResponseData<Participant>> call, Response<ResponseData<Participant>> response) {
                if (response.isSuccessful()) {
                    Participant participant = response.body().getData();

                    Intent intent = new Intent(MainActivity.this, ParticipantDetail.class);
                    intent.putExtra("Participant", participant);
                    startActivity(intent);

                } else {
                    APIErrorResponse error = ErrorUtils.parseError(response);
                    if (error.message() != null && error.message().length() > 0) {
                        setMessage(error.message());
                    } else {
                        setMessage("Error while reading server's response");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseData<Participant>> call, Throwable t) {

            }
        });
    }

    public void responseError(Exception e) {
        setMessage("Error while reading the card");
        Log.e("NFCReadActivity", "Error reading the card", e);
    }

    public void responseError() {
        setMessage("Error while reading the card");
    }
}

