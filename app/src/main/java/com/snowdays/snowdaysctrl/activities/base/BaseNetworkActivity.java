package com.snowdays.snowdaysctrl.activities.base;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.snowdays.snowdaysctrl.R;
import com.snowdays.snowdaysctrl.activities.base.BaseActivity;
import com.snowdays.snowdaysctrl.models.APIErrorResponse;
import com.snowdays.snowdaysctrl.models.ResponseData;
import com.snowdays.snowdaysctrl.utilities.ErrorUtils;
import com.snowdays.snowdaysctrl.utilities.NetworkService;

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
    public View mEmptyView;

    public Call<ResponseData<T>> mCall;

    // In subclasses remember to instantiate and set the adapter.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.cards_list);
        mSpinner = (ProgressBar) findViewById(R.id.progress_bar_list);
        mEmptyView = (View) findViewById(R.id.empty_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = (AlertDialog) onCreateCommentDialog();
                dialog.show();
            }
        });
    }

    public Dialog onCreateCommentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Comments");

        final EditText input = new EditText(this);
        input.setLines(4);
        input.setPadding(70,0,70,0);
        input.setBackgroundColor(Color.TRANSPARENT);
        input.setHint("Insert your message here");

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        builder.setView(input);

        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendReport(String.valueOf(input.getText()));
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return builder.create();
    }

    public void sendReport(String message) {
        Call<ResponseData<String>> reportCall = NetworkService.getInstance().sendReport(getHeaders(), message);
        reportCall.enqueue(new Callback<ResponseData<String>>() {
            @Override
            public void onResponse(Call<ResponseData<String>> call, Response<ResponseData<String>> response) {
                if (response.isSuccessful()) {
                    ArrayList<String> data = new ArrayList<String>(Arrays.asList(response.body().getData()));

                    showEmptyView(data.isEmpty());
                    setMessage("Thanks for your report!");

                } else {
                    APIErrorResponse error = ErrorUtils.parseError(response);
                    if (error.message() != null && error.message().length() > 0) {
                        setMessage(error.message());
                    } else {
                        setMessage("Error while reading server's response");
                    }
                    showEmptyView(mRecyclerView.getAdapter().getItemCount() == 0);
                }
            }

            @Override
            public void onFailure(Call<ResponseData<String>> call, Throwable t) {

            }
        });
    }

    public void loadData(Call<ResponseData<T>> call) {
        mRecyclerView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.GONE);
        mSpinner.setVisibility(ProgressBar.VISIBLE);

        mCall = call;
        mCall.enqueue(this);
    }

    // Network tasks
    @Override
    public void onResponse(Call<ResponseData<T>> call, Response<ResponseData<T>> response) {
        mSpinner.setVisibility(ProgressBar.GONE);

        if (response.isSuccessful()) {
            ArrayList<T> data = new ArrayList<T>(Arrays.asList(response.body().getData()));

            showEmptyView(data.isEmpty());

        } else {
            APIErrorResponse error = ErrorUtils.parseError(response);
            if (error.message() != null && error.message().length() > 0) {
                setMessage(error.message());
            } else {
                setMessage("Error while reading server's response");
            }
            showEmptyView(mRecyclerView.getAdapter().getItemCount() == 0);
        }
    }

    @Override
    public void onFailure(Call<ResponseData<T>> call, Throwable t) {
        mSpinner.setVisibility(ProgressBar.GONE);

        setMessage("Error while contacting the server");
        showEmptyView(mRecyclerView.getAdapter().getItemCount() == 0);
    }

    private void showEmptyView(boolean doIt) {
        if (doIt) {
            mEmptyView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mEmptyView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

}
