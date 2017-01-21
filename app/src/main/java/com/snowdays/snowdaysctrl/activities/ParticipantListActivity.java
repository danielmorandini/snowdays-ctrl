package com.snowdays.snowdaysctrl.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.snowdays.snowdaysctrl.R;
import com.snowdays.snowdaysctrl.adapters.ParticipantsListAdapter;
import com.snowdays.snowdaysctrl.models.APIErrorResponse;
import com.snowdays.snowdaysctrl.models.Participant;
import com.snowdays.snowdaysctrl.models.ResponseData;
import com.snowdays.snowdaysctrl.utilities.ErrorUtils;
import com.snowdays.snowdaysctrl.utilities.KeyStore;
import com.snowdays.snowdaysctrl.utilities.NetworkService;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ParticipantListActivity extends BaseActivity implements Callback<ResponseData<Participant[]>> {
    public static final String ARG_ACTION_KEY = "ARG_ACTION_KEY";
    public static final String ARG_DAY_KEY = "ARG_DAY_KEY";

    private Call<ResponseData<Participant[]>> mCall;
    private RecyclerView mRecyclerView;
    private ParticipantsListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar mSpinner;
    private String actionKey;
    private String dayKey;
    private Boolean switch_value = false; // Value that decides if to fetch users that have already done this activity of not


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant_list);

        // Retrieve String from intent
        actionKey = getIntent().getStringExtra(ARG_ACTION_KEY);
        dayKey = getIntent().getStringExtra(ARG_DAY_KEY);

        mRecyclerView = (RecyclerView) findViewById(R.id.participant_list_recycler_view);
        mSpinner = (ProgressBar) findViewById(R.id.progress_bar_list);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        // specify an adapter
        mAdapter = new ParticipantsListAdapter(this, new ArrayList<Participant>());
        mRecyclerView.setAdapter(mAdapter);

        loadData();
    }

    private void loadData() {
        mSpinner.setVisibility(ProgressBar.VISIBLE);
        mCall = NetworkService.getInstance().getParticipantsWithFields(getHeaders(), dayKey + "." + actionKey, switch_value);
        mCall.enqueue(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_switch_list:
                if (switch_value) {
                    switch_value = false;
                } else {
                    switch_value = true;
                }
                loadData();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Update

    @Override
    public void onResponse(Call<ResponseData<Participant[]>> call, Response<ResponseData<Participant[]>> response) {
        mSpinner.setVisibility(ProgressBar.GONE);

        if (response.isSuccessful()) {
            ArrayList<Participant> data = new ArrayList<Participant>(Arrays.asList(response.body().getData()));
            mAdapter.addItems(data, switch_value);

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
    public void onFailure(Call<ResponseData<Participant[]>> call, Throwable t) {
        mSpinner.setVisibility(ProgressBar.GONE);

        setMessage("Error while contacting the server");
    }
}
