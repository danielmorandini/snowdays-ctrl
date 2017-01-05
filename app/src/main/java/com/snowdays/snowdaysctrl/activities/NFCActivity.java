package com.snowdays.snowdaysctrl.activities;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.snowdays.snowdaysctrl.R;
import com.snowdays.snowdaysctrl.fragments.NFCProgressFragment;
import com.snowdays.snowdaysctrl.models.MainCard;
import com.snowdays.snowdaysctrl.models.ResponseData;
import com.snowdays.snowdaysctrl.models.UpdateResponse;
import com.snowdays.snowdaysctrl.utilities.KeyStore;
import com.snowdays.snowdaysctrl.utilities.NetworkService;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NFCActivity extends BaseNFCActivity  implements Callback<ResponseData<UpdateResponse>>, NFCProgressFragment.OnFragmentInteractionListener {

    // Global
    private Call<ResponseData<UpdateResponse>> mCall;
    public final static String EXTRA_CARD = "com.snowdays.snowdaysctrl.EXTRA_CARD";
    private MainCard mCard;
    private FragmentStack mStack;


    // UI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);

        // Retrieve card info from intent
        mCard = (MainCard) getIntent().getSerializableExtra(EXTRA_CARD);

        // Stack that will host the fragments that handle the visual responses of this view
        mStack = new FragmentStack();

    }

    //TODO: Remove this afterwards
    private void setMessage(String message) {
        Log.d("NFCActivity", message);
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
    }

    //NFC Tag discovery

    @Override
    public void readingStarted() {

        //remove every item first
        mStack.popAll();

        NFCProgressFragment item = createFragment("NFC Activity");
        mStack.push(item);
    }

    @Override
    public void responseData(String data) {

        NFCProgressFragment item = mStack.peek();
        item.taskDone();

        // TODO: Read the userId stored into the NFC tag and then make the HTTP request
        updateParticipant(data);
    }

    @Override
    public void responseError(IOException e) {
        setMessage("Error while reading the card");
        Log.e("NFCActivity", "Error reading the card", e);
    }


    //HTTP Requests

    public void updateParticipant(String participantId) {
        if (mCall != null) mCall.cancel();

        NFCProgressFragment item = createFragment("HTTP request");
        mStack.push(item);

        //TODO: Remove this afterwards
        participantId = "ea7iMYMnz2uCbnyDp";

        HashMap<String, HashMap<String, Boolean>> body = new HashMap<>();
        HashMap<String, Boolean> innerBody = new HashMap<>();
        innerBody.put(mCard.getActionKey(), true);
        body.put(mCard.getmDayKey(), innerBody);

        mCall = NetworkService.getInstance().updateParticipant(getHeaders(), participantId, body);
        mCall.enqueue(this);
    }

    @Override
    public void onResponse(Call<ResponseData<UpdateResponse>> call, Response<ResponseData<UpdateResponse>> response) {
        //TODO: Visually handle the response. If successfull, the staff member must know that he can pass to another participant

        NFCProgressFragment item = mStack.peek();
        item.taskDone();

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

    public NFCProgressFragment createFragment(String title) {
        NFCProgressFragment item = NFCProgressFragment.newInstance(title);
        return item;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.d("NFCActivity", "Fragment Interaction Called");
    }

    public class FragmentStack extends Stack<NFCProgressFragment> {

        @Override
        public NFCProgressFragment push(NFCProgressFragment item) {

            //add fragment to view
            addFragment(item);
            return super.push(item);
        }

        @Override
        public synchronized NFCProgressFragment pop() {
            //remove fragment from view
            NFCProgressFragment item = super.pop();
            removeFragment(item);
            return item;
        }

        public void popAll() {
            while (!mStack.isEmpty()) {
                NFCProgressFragment item = mStack.pop();
            }
        }

        private void addFragment(NFCProgressFragment item) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.nfc_fragment_container, item)
                    .commitAllowingStateLoss();
        }

        private void removeFragment(NFCProgressFragment item) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(item)
                    .commitAllowingStateLoss();
        }
    }
}
