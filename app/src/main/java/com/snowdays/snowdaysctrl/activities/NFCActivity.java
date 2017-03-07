package com.snowdays.snowdaysctrl.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import com.snowdays.snowdaysctrl.R;
import com.snowdays.snowdaysctrl.fragments.NFCProgressFragment;
import com.snowdays.snowdaysctrl.models.APIErrorResponse;
import com.snowdays.snowdaysctrl.models.ResponseData;
import com.snowdays.snowdaysctrl.utilities.ErrorUtils;

import java.util.Stack;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by danielmorandini on 07/03/2017.
 */

public class NFCActivity extends BaseNFCActivity implements Callback<ResponseData<String>> {

    public FragmentStack mStack;
    protected TextView subtitleTV;
    public Call<ResponseData<String>> mCall;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);

        // Stack that will host the fragments that handle the visual responses of this view
        mStack = new FragmentStack();

        subtitleTV = (TextView) findViewById(R.id.subtitle);
    }

    //NFC Tag discovery

    @Override
    public void responseData(String data) {
        NFCProgressFragment item = mStack.peek();
        item.taskDone();
    }

    @Override
    public void responseError(Exception e) {
        setMessage("Error while reading the card");
        Log.e("NFCReadActivity", "Error reading the card", e);

        NFCProgressFragment item = mStack.peek();
        item.taskFailed();
    }

    @Override
    public void responseError() {
        setMessage("Error while reading the card");

        NFCProgressFragment item = mStack.peek();
        item.taskFailed();
    }

    @Override
    public void readingStarted() {
        //remove every item first
        mStack.popAll();

        NFCProgressFragment item = createFragment("NFC Activity");
        mStack.push(item);
    }

    // Network

    @Override
    public void onResponse(Call<ResponseData<String>> call, Response<ResponseData<String>> response) {
        if (response.isSuccessful()) {
            NFCProgressFragment item = mStack.peek();
            item.taskDone();

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
        setMessage("Error while contacting the server");

        NFCProgressFragment item = mStack.peek();
        item.taskFailed();
    }

    // UI
    public NFCProgressFragment createFragment(String title) {
        NFCProgressFragment item = NFCProgressFragment.newInstance(title);
        return item;
    }

    // UI Stack component - logic

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
