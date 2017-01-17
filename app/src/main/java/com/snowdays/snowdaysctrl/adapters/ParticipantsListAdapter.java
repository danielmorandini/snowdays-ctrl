package com.snowdays.snowdaysctrl.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.snowdays.snowdaysctrl.R;
import com.snowdays.snowdaysctrl.models.Participant;

import java.util.ArrayList;

/**
 * Created by danielmorandini on 11/01/2017.
 */

public class ParticipantsListAdapter extends RecyclerView.Adapter<ParticipantsListAdapter.ViewHolder> {

    private ArrayList<Participant> mDataset;
    private Boolean flag; // Determines if we should put the done icon or the todo icon

    public ParticipantsListAdapter(ArrayList<Participant> participants) {
        mDataset = participants;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.participant_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Participant element = mDataset.get(position);

        int drawableIconID = (flag) ? R.drawable.ic_done : R.drawable.ic_todo;
        holder.mIconView.setImageResource(drawableIconID);

        holder.mTextView.setText(element.getId());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void addItems(ArrayList<Participant> participants, boolean flag) {
        this.flag = flag;
        mDataset.clear();
        mDataset.addAll(participants);
        notifyDataSetChanged();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ImageView mIconView;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.participant_list_text);
            mIconView = (ImageView) v.findViewById(R.id.participant_list_icon);
        }

    }
}
