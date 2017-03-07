package com.snowdays.snowdaysctrl.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.snowdays.snowdaysctrl.adapters.MainCardListAdapter;
import com.snowdays.snowdaysctrl.R;
import com.snowdays.snowdaysctrl.models.MainCard;
import com.snowdays.snowdaysctrl.models.ResponseData;
import com.snowdays.snowdaysctrl.utilities.KeyStore;
import com.snowdays.snowdaysctrl.utilities.NetworkService;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends BaseNetworkActivity<MainCard[]> {

    private MainCardListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadToolbar(getString(R.string.app_name));

        // specify an adapter
        mAdapter = new MainCardListAdapter(new ArrayList<MainCard>(), this);
        mRecyclerView.setAdapter(mAdapter);

        /*// Floating button actions
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = (AlertDialog) onCreateCommentDialog();
                dialog.show();
            }
        });*/

        // load data
        loadData();
    }

    /*public Dialog onCreateCommentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle("Comments");

        final EditText input = new EditText(MainActivity.this);
        input.setLines(4);
        input.setPadding(70,0,70,0);
        input.setBackgroundColor(Color.TRANSPARENT);
        input.setHint("Insert your comment here");

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        builder.setView(input);

        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return builder.create();
    }*/

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

                AlertDialog dialog = (AlertDialog) onCreateDialog();
                dialog.show();

                return true;

            case R.id.action_part_list:

                Intent intent = new Intent(this, ParticipantListActivity.class);
                intent.putExtra("myTitle", "Participant List");
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void loadData() {
        mCall = NetworkService.getInstance().getActivities(getHeaders());
        loadData(mCall);
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

    public Dialog onCreateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(R.string.logout_alert_title)
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Remove saved token and userId
                        KeyStore.clearAll(MainActivity.this);

                        // Return to the base activity
                        Intent intent = new Intent(MainActivity.this, StartActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        return builder.create();
    }
}

