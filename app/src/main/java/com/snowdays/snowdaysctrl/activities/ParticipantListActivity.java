package com.snowdays.snowdaysctrl.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ProgressBar;

import com.snowdays.snowdaysctrl.R;
import com.snowdays.snowdaysctrl.adapters.ParticipantsListAdapter;
import com.snowdays.snowdaysctrl.models.APIErrorResponse;
import com.snowdays.snowdaysctrl.models.Participant;
import com.snowdays.snowdaysctrl.models.ResponseData;
import com.snowdays.snowdaysctrl.utilities.ErrorUtils;
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

        // specify an adapter (see also next example)
        mAdapter = new ParticipantsListAdapter(new ArrayList<Participant>());
        mRecyclerView.setAdapter(mAdapter);

        loadData();
    }

    private void loadData() {
        mSpinner.setVisibility(ProgressBar.VISIBLE);
        mCall = NetworkService.getInstance().getParticipantsWithFields(getHeaders(), "all", dayKey + "." + actionKey, false);
        mCall.enqueue(this);
    }

    // Update

    @Override
    public void onResponse(Call<ResponseData<Participant[]>> call, Response<ResponseData<Participant[]>> response) {
        mSpinner.setVisibility(ProgressBar.GONE);

        if (response.isSuccessful()) {
            ArrayList<Participant> data = new ArrayList<Participant>(Arrays.asList(response.body().getData()));
            mAdapter.addItems(data);

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
