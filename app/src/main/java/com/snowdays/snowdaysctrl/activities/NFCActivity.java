package com.snowdays.snowdaysctrl.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.snowdays.snowdaysctrl.R;
import com.snowdays.snowdaysctrl.models.MainCard;
import com.snowdays.snowdaysctrl.models.ResponseData;
import com.snowdays.snowdaysctrl.models.UpdateResponse;
import com.snowdays.snowdaysctrl.utilities.KeyStore;
import com.snowdays.snowdaysctrl.utilities.NetworkService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NFCActivity extends BaseNFCActivity  implements Callback<ResponseData<UpdateResponse>> {

    // Global
    private Call<ResponseData<UpdateResponse>> mCall;
    public final static String EXTRA_CARD = "com.snowdays.snowdaysctrl.EXTRA_CARD";
    private MainCard mCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);

        // Retrieve card info from intent
        mCard = (MainCard) getIntent().getSerializableExtra(EXTRA_CARD);
    }

    //TODO: Remove this afterwards
    private void setMessage(String message) {
        Log.d("NFCActivity", message);
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void responseData(String data) {
        setMessage(data);

        // TODO: Read the userId stored into the NFC tag and then make the HTTP request
        updateParticipant(data);
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

    public void updateParticipant(String participantId) {
        if (mCall != null) mCall.cancel();

        //TODO: Remove this afterwards
        participantId = "WjjuytkdwZrK3RZoE";

        HashMap<String, Boolean> body = new HashMap<>();
        body.put(mCard.getActionKey(), true);
        mCall = NetworkService.getInstance().updateParticipant(getHeaders(), participantId, body);
        mCall.enqueue(this);
    }

    @Override
    public void onResponse(Call<ResponseData<UpdateResponse>> call, Response<ResponseData<UpdateResponse>> response) {
        //TODO: Visually handle the response. If successfull, the staff member must know that he can pass to another participant
        setMessage("PARTICIPANT UPDATED WITH KEY " + mCard.getActionKey());
    }

    @Override
    public void onFailure(Call<ResponseData<UpdateResponse>> call, Throwable t) {
        //TODO: visually handle this failure and help the staff member to understand if there is a possibility to make this work in the near future
    }

    // Utils
    public Map<String, String> getHeaders() {
        Map<String, String> map = new HashMap<>();

        map.put("X-Auth-Token", KeyStore.getToken(this));
        map.put("X-User-Id", KeyStore.getUserId(this));
        return map;
    }
}
