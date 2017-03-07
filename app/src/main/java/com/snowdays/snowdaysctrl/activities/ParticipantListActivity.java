package com.snowdays.snowdaysctrl.activities;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.snowdays.snowdaysctrl.R;
import com.snowdays.snowdaysctrl.activities.base.BaseNetworkActivity;
import com.snowdays.snowdaysctrl.adapters.ParticipantsListAdapter;
import com.snowdays.snowdaysctrl.models.Participant;
import com.snowdays.snowdaysctrl.models.ResponseData;
import com.snowdays.snowdaysctrl.utilities.NetworkService;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Response;


public class ParticipantListActivity extends BaseNetworkActivity<Participant[]> {
    public static final String ARG_ACTION_KEY = "ARG_ACTION_KEY";


    private static ArrayList<Participant> newDataSet;
    private static String[] dorms = {"Univercity", "Benedikt", "Marianum", "Carducci"};
    private String actionKey;
    private String title;
    private static SearchView searchView = null;
    private Boolean switch_value = false; // Value that decides if to fetch users that have already done this activity of not

    private static ArrayList<Participant> dataSet;
    private ParticipantsListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve String from intent
        actionKey = getIntent().getStringExtra(ARG_ACTION_KEY);
        title = getIntent().getStringExtra("myTitle");

        loadToolbar(title);

        // Adapter
        // specify an adapter
        mAdapter = new ParticipantsListAdapter(this, new ArrayList<Participant>());
        mRecyclerView.setAdapter(mAdapter);

        loadData();
    }


    public void loadData() {
        mCall = NetworkService.getInstance().getParticipantsWithFields(getHeaders(), actionKey, switch_value);
        loadData(mCall);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_list, menu);

        // Sets the action of the search item
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) ParticipantListActivity.this.getSystemService(Context.SEARCH_SERVICE);

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
                newDataSet = new ArrayList<Participant>();

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

                return true;
            case R.id.action_reload:
                loadData();
                dataSet = mAdapter.getmDataset();
                mAdapter.addItems(dataSet, switch_value);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    // Update

    @Override
    public void onResponse(Call<ResponseData<Participant[]>> call, Response<ResponseData<Participant[]>> response) {
        super.onResponse(call, response);

        if (response.isSuccessful()) {
            ArrayList<Participant> data = new ArrayList<Participant>(Arrays.asList(response.body().getData()));

            mAdapter.addItems(data, switch_value);
        }
    }

    @Override
    public void onFailure(Call<ResponseData<Participant[]>> call, Throwable t) {
        super.onFailure(call, t);
    }

    public Dialog onCreateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ParticipantListActivity.this);

        builder.setTitle("Filter by dorm")
                .setItems(dorms, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(newDataSet == null) {
                            newDataSet = new ArrayList<Participant>();
                        } else {
                            for(int i = 0; i < newDataSet.size(); i++) {
                                newDataSet.remove(i);
                            }
                        }

                        if(!dataSet.isEmpty()) {
                            for (int i = 0; i < dataSet.size(); i++) {

                                Participant current = dataSet.get(i);

                                if (current.getDorm() != null) {
                                    if (current.getDorm().equals(dorms[which])) {
                                        newDataSet.add(current);
                                    }
                                } else {
                                    setMessage("current.getDorm() returns null");
                                    mAdapter.resetmDataset();
                                    mAdapter.addItems(dataSet, switch_value);
                                    break;
                                }

                            }


                            if (!newDataSet.isEmpty()) {
                                mAdapter.addItems(newDataSet, switch_value);
                            }
                        }
                    }
                });
        return builder.create();
    }
}
