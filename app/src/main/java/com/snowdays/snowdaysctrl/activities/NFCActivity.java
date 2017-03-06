package com.snowdays.snowdaysctrl.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.snowdays.snowdaysctrl.R;
import com.snowdays.snowdaysctrl.fragments.NFCProgressFragment;
import com.snowdays.snowdaysctrl.models.APIErrorResponse;
import com.snowdays.snowdaysctrl.models.MainCard;
import com.snowdays.snowdaysctrl.models.ResponseData;
import com.snowdays.snowdaysctrl.utilities.ErrorUtils;
import com.snowdays.snowdaysctrl.utilities.NetworkService;

import java.util.HashMap;
import java.util.Stack;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NFCActivity extends BaseNFCActivity  implements Callback<ResponseData<String>> {

    // Global
    private Call<ResponseData<String>> mCall;
    public final static String EXTRA_CARD = "com.snowdays.snowdaysctrl.EXTRA_CARD";
    private MainCard mCard;
    private FragmentStack mStack;
    // UI
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);

        // Retrieve card info from intent
        mCard = (MainCard) getIntent().getSerializableExtra(EXTRA_CARD);

        loadToolbar(mCard.getName());

        // Stack that will host the fragments that handle the visual responses of this view
        mStack = new FragmentStack();
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

        updateParticipant(data);
    }

    @Override
    public void responseError(Exception e) {
        setMessage("Error while reading the card");
        Log.e("NFCActivity", "Error reading the card", e);

        NFCProgressFragment item = mStack.peek();
        item.taskFailed();
    }

    @Override
    public void responseError() {
        setMessage("Error while reading the card");

        NFCProgressFragment item = mStack.peek();
        item.taskFailed();
    }

    //HTTP Requests

    public void updateParticipant(String participantId) {
        if (mCall != null) mCall.cancel();

        NFCProgressFragment item = createFragment("HTTP request");
        mStack.push(item);

        HashMap<String, HashMap<String, Boolean>> body = new HashMap<>();
        HashMap<String, Boolean> innerBody = new HashMap<>();
        innerBody.put(mCard.getCheckAction(), true);

        mCall = NetworkService.getInstance().updateParticipant(getHeaders(), participantId, body);
        mCall.enqueue(this);
    }

    @Override
    public void onResponse(Call<ResponseData<String>> call, Response<ResponseData<String>> response) {
        //TODO: Visually handle the response. If successfull, the staff member must know that he can pass to another participant

        if (response.isSuccessful()) {
            NFCProgressFragment item = mStack.peek();
            item.taskDone();
            setMessage("PARTICIPANT UPDATED WITH KEY " + mCard.getCheckAction());
        } else {
            APIErrorResponse error = ErrorUtils.parseError(response);
            if (error.message() != null && error.message().length() > 0) {
                setMessage(error.message());
            }

            NFCProgressFragment item = mStack.peek();
            item.taskFailed();
        }
    }

    @Override
    public void onFailure(Call<ResponseData<String>> call, Throwable t) {
        //TODO: visually handle this failure and help the staff member to understand if there is a possibility to make this work in the near future
        setMessage("Error while contacting the server");

        NFCProgressFragment item = mStack.peek();
        item.taskFailed();
    }

    public NFCProgressFragment createFragment(String title) {
        NFCProgressFragment item = NFCProgressFragment.newInstance(title);
        return item;
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
