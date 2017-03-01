package com.snowdays.snowdaysctrl.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.snowdays.snowdaysctrl.R;
import com.snowdays.snowdaysctrl.models.APIErrorResponse;
import com.snowdays.snowdaysctrl.models.ResponseData;
import com.snowdays.snowdaysctrl.utilities.ErrorUtils;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by danielmorandini on 01/03/2017.
 */

public class BaseNetworkActivity<T> extends BaseActivity implements Callback<ResponseData<T>> {

    public RecyclerView mRecyclerView;
    public RecyclerView.LayoutManager mLayoutManager;
    public ProgressBar mSpinner;
    public TextView mEmptyView;

    public Call<ResponseData<T>> mCall;

    // In subclasses remember to instantiate and set the adapter.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.participant_list_recycler_view);
        mSpinner = (ProgressBar) findViewById(R.id.progress_bar_list);
        mEmptyView = (TextView) findViewById(R.id.empty_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    public void loadData(Call<ResponseData<T>> call) {
        mEmptyView.setVisibility(View.GONE);
        mSpinner.setVisibility(ProgressBar.VISIBLE);
        mCall = call;
        mCall.enqueue(this);
    }

    @Override
    public void onResponse(Call<ResponseData<T>> call, Response<ResponseData<T>> response) {
        mSpinner.setVisibility(ProgressBar.GONE);

        if (response.isSuccessful()) {
            ArrayList<T> data = new ArrayList<T>(Arrays.asList(response.body().getData()));

            if (data.isEmpty()) {
                mEmptyView.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            } else {
                mEmptyView.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
            }

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
    public void onFailure(Call<ResponseData<T>> call, Throwable t) {
        mSpinner.setVisibility(ProgressBar.GONE);

        setMessage("Error while contacting the server");
    }
}
