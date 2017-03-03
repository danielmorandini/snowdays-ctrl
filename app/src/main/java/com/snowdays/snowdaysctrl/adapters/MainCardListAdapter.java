package com.snowdays.snowdaysctrl.adapters;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.snowdays.snowdaysctrl.R;
import com.snowdays.snowdaysctrl.activities.BaseActivity;
import com.snowdays.snowdaysctrl.activities.NFCActivity;
import com.snowdays.snowdaysctrl.activities.ParticipantDetail;
import com.snowdays.snowdaysctrl.models.MainCard;
import com.snowdays.snowdaysctrl.models.Participant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by danielmorandini on 19/12/2016.
 */

public class MainCardListAdapter extends RecyclerView.Adapter<MainCardListAdapter.ViewHolder> {
    private ArrayList<MainCard> mDataset;
    private BaseActivity mContext;

    public MainCardListAdapter(ArrayList<MainCard> dataset, BaseActivity context) {
        mDataset = dataset;
        mContext = context;
    }

    @Override
    public MainCardListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_main, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MainCardListAdapter.ViewHolder holder, int position) {
        MainCard element = mDataset.get(position);

        holder.mTitleView.setText(element.getName());
        holder.mSubtitleView.setText(element.getSubtitle());

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM '@' HH:mm");
        holder.mDateView.setText(formatter.format(element.getStartDate()) + " - " + formatter.format(element.getEndDate()));
        holder.mImageView.setImageResource(element.getIconID());
        holder.itemView.setOnClickListener(new CardListener(element, mContext));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void addItems(ArrayList<MainCard> cards) {
        mDataset.clear();
        mDataset.addAll(cards);
        notifyDataSetChanged();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTitleView;
        public TextView mSubtitleView;
        public TextView mDateView;
        public ImageView mImageView;

        public ViewHolder(View v) {
            super(v);
            mTitleView = (TextView) v.findViewById(R.id.title);
            mSubtitleView = (TextView) v.findViewById(R.id.subtitle);
            mDateView = (TextView) v.findViewById(R.id.date);
            mImageView = (ImageView) v.findViewById(R.id.image_view);
        }
    }

    public class CardListener implements View.OnClickListener {
        Context c;
        MainCard card;

        public CardListener(MainCard card, Context myContext) {
            this.c = myContext;
            this.card = card;

        }
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(c, NFCActivity.class);
            intent.putExtra(NFCActivity.EXTRA_CARD, card);
            c.startActivity(intent);
        }
    }
}
