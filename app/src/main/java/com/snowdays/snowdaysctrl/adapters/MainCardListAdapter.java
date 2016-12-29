package com.snowdays.snowdaysctrl.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.snowdays.snowdaysctrl.R;
import com.snowdays.snowdaysctrl.models.MainCard;

/**
 * Created by danielmorandini on 19/12/2016.
 */

public class MainCardListAdapter extends RecyclerView.Adapter<MainCardListAdapter.ViewHolder> {
    private MainCard[] mDataset;
    private OnClickListerner mDelegate;

    public MainCardListAdapter(MainCard[] dataset) {
        mDataset = dataset;
    }

    @Override
    public MainCardListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_main, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MainCardListAdapter.ViewHolder holder, int position) {
        MainCard element = mDataset[position];
        holder.mTextView.setText(element.getTitle());
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    public void setDelegate(OnClickListerner mDelegate) {
        this.mDelegate = mDelegate;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView mTextView;
        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.card_main);
            v.findViewById(R.id.card_scan_button).setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            if (mDelegate == null || getAdapterPosition() == RecyclerView.NO_POSITION) return;
            mDelegate.onButtonClick(mDataset[getAdapterPosition()]);
        }
    }

    public interface OnClickListerner {
        void onButtonClick(MainCard cardModel);
    }
}
