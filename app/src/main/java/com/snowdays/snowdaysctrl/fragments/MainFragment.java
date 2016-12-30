package com.snowdays.snowdaysctrl.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.snowdays.snowdaysctrl.R;
import com.snowdays.snowdaysctrl.activities.NFCActivity;
import com.snowdays.snowdaysctrl.adapters.MainCardListAdapter;
import com.snowdays.snowdaysctrl.models.MainCard;

/**
 * Created by danielmorandini on 19/12/2016.
 */

public class MainFragment extends Fragment implements MainCardListAdapter.OnClickListerner {

    private RecyclerView mRecyclerView;
    private MainCardListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private MainCard[] mDatasource;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MainCardListAdapter(mDatasource);
        mAdapter.setDelegate(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    //Update
    @Override
    public void onButtonClick(MainCard cardModel) {
        Log.d("MainActivity", "onClick called: " + cardModel.getTitle());

        Intent intent = new Intent(getActivity(), NFCActivity.class);
        intent.putExtra(NFCActivity.EXTRA_CARD, cardModel);
        startActivity(intent);

    }

    //Helpers
    public void setDatasource(MainCard[] mDatasource) {
        this.mDatasource = mDatasource;
    }
}
