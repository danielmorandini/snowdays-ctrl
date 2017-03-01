package com.snowdays.snowdaysctrl.activities;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.snowdays.snowdaysctrl.R;
import com.snowdays.snowdaysctrl.adapters.ParticipantsListAdapter;
import com.snowdays.snowdaysctrl.models.APIErrorResponse;
import com.snowdays.snowdaysctrl.models.Participant;
import com.snowdays.snowdaysctrl.models.ResponseData;
import com.snowdays.snowdaysctrl.utilities.ErrorUtils;
import com.snowdays.snowdaysctrl.utilities.KeyStore;
import com.snowdays.snowdaysctrl.utilities.NetworkService;

import org.w3c.dom.Text;

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
    private TextView mEmptyView;
    private ParticipantsListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static ArrayList<Participant> dataSet;
    private ProgressBar mSpinner;
    private String actionKey;
    private String dayKey;
    private String title;
    private Boolean switch_value = false; // Value that decides if to fetch users that have already done this activity of not


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant_list);

        // Retrieve String from intent
        actionKey = getIntent().getStringExtra(ARG_ACTION_KEY);
        dayKey = getIntent().getStringExtra(ARG_DAY_KEY);
        title = getIntent().getStringExtra("myTitle");

        getSupportActionBar().setTitle(title);

        mRecyclerView = (RecyclerView) findViewById(R.id.participant_list_recycler_view);
        mSpinner = (ProgressBar) findViewById(R.id.progress_bar_list);
        mEmptyView = (TextView) findViewById(R.id.empty_view);

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
        mEmptyView.setVisibility(View.GONE);
        mSpinner.setVisibility(ProgressBar.VISIBLE);
        mCall = NetworkService.getInstance().getParticipantsWithFields(getHeaders(), dayKey + "." + actionKey, switch_value);
        mCall.enqueue(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_list, menu);

        // Sets the action of the search item
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) ParticipantListActivity.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(ParticipantListActivity.this.getComponentName()));
        }

        dataSet = mAdapter.getmDataset();

        // Here I set the TextListener for searching in the participant list
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            // On search command
            @Override
            public boolean onQueryTextSubmit(String query) {
                updateDataset(query);
                return false;
            }

            // On text change
            @Override
            public boolean onQueryTextChange(String newText) {
                updateDataset(newText);
                return false;
            }

            private void updateDataset(String query) {
                ArrayList<Participant> newDataSet = new ArrayList<Participant>();

                for(int i = 0; i < dataSet.size(); i++) {

                    Participant current = dataSet.get(i);
                    String name = current.getFirstName().toLowerCase();
                    String last = current.getLastName().toLowerCase();
                    query = query.toLowerCase();

                    // Each time the text changes I check if there is a participant whose name/surname/name + surname
                    // starts with the inserted text.
                    if(name.startsWith(query) || last.startsWith(query) ||  new String(name + " " + last).startsWith(query)) {
                        newDataSet.add(current);
                    }
                }

                mAdapter.resetmDataset();
                if(!newDataSet.isEmpty()) {
                    mAdapter.addItems(newDataSet, switch_value);
                } else {
                    mAdapter.addItems(dataSet, switch_value);
                }
            }
        });

        // With these two listeners I make sure that after the previous search or
        // before a new search the dataset is complete.
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mAdapter.resetmDataset();
                mAdapter.addItems(dataSet, switch_value);
                return false;
            }
        });

        searchView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mAdapter.resetmDataset();
                mAdapter.addItems(dataSet, switch_value);
                return false;
            }
        });

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
            case R.id.action_filter:

                AlertDialog dialog = (AlertDialog) onCreateDialog();
                dialog.show();


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public Dialog onCreateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ParticipantListActivity.this);

        String[] dorms = {"Univercity", "Benedikt", "Marianum", "Carducci"};

        builder.setTitle("Filter by dorm")
                .setItems(dorms, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ArrayList<Participant> newDataSet = new ArrayList<Participant>();

                        for(int i = 0; i < dataSet.size(); i++) {

                            Participant current = dataSet.get(i);

                        }
                    }
                });
        return builder.create();
    }

    // Update

    @Override
    public void onResponse(Call<ResponseData<Participant[]>> call, Response<ResponseData<Participant[]>> response) {
        mSpinner.setVisibility(ProgressBar.GONE);

        if (response.isSuccessful()) {
            ArrayList<Participant> data = new ArrayList<Participant>(Arrays.asList(response.body().getData()));

            if (data.isEmpty()) {
                mEmptyView.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            } else {
                mEmptyView.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
            }

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
