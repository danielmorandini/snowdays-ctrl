package com.snowdays.snowdaysctrl.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.snowdays.snowdaysctrl.adapters.MainCardListAdapter;
import com.snowdays.snowdaysctrl.R;
import com.snowdays.snowdaysctrl.models.MainCard;
import com.snowdays.snowdaysctrl.models.ResponseData;
import com.snowdays.snowdaysctrl.utilities.KeyStore;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends BaseNetworkActivity<MainCard[]> {

    private MainCardListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // specify an adapter
        mAdapter = new MainCardListAdapter(new ArrayList<MainCard>());
        mRecyclerView.setAdapter(mAdapter);

        // load data
        loadData();
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
                // Remove saved token and userId
                KeyStore.clearAll(this);

                // Return to the base activity
                Intent intent = new Intent(this, StartActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void loadData() {
        mEmptyView.setVisibility(View.GONE);
        mSpinner.setVisibility(ProgressBar.VISIBLE);
        //mCall = NetworkService.getInstance().getParticipantsWithFields(getHeaders(), dayKey + "." + actionKey, switch_value);
        mCall.enqueue(this);
    }

    // Update

    @Override
    public void onResponse(Call<ResponseData<MainCard[]>> call, Response<ResponseData<MainCard[]>> response) {
        super.onResponse(call, response);
        if (response.isSuccessful()) {
            ArrayList<MainCard> data = new ArrayList<MainCard>(Arrays.asList(response.body().getData()));

            mAdapter.addItems(data);
        }
    }

    @Override
    public void onFailure(Call<ResponseData<MainCard[]>> call, Throwable t) {
        super.onFailure(call, t);
    }
}

