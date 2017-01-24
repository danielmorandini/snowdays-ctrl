package com.snowdays.snowdaysctrl.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.snowdays.snowdaysctrl.R;
import com.snowdays.snowdaysctrl.activities.ParticipantDetail;
import com.snowdays.snowdaysctrl.activities.ParticipantListActivity;
import com.snowdays.snowdaysctrl.models.Participant;

import java.util.ArrayList;

/**
 * Created by danielmorandini on 11/01/2017.
 */

public class ParticipantsListAdapter extends RecyclerView.Adapter<ParticipantsListAdapter.ViewHolder> {

    private ArrayList<Participant> mDataset;
    private Context c;
    private Boolean flag; // Determines if we should put the done icon or the todo icon

    public ParticipantsListAdapter(Context c, ArrayList<Participant> participants) {
        mDataset = participants;
        this.c = c;
    }

    public void resetmDataset() {
        this.mDataset = new ArrayList<>();
    }

    public ArrayList<Participant> getmDataset() {
        return this.mDataset;
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

        holder.mTextView.setText(element.getFirstName() + " " + element.getLastName());
        holder.mTextViewDorm.setText(element.getId());

        ParticipantListener participantListener = new ParticipantListener(element.getFirstName() + " " + element.getLastName(), element.getEmail(), element.getOwner(), c);
        holder.itemView.setOnClickListener(participantListener);
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
        public TextView mTextViewDorm;
        public ImageView mIconView;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.participant_list_text);
            mIconView = (ImageView) v.findViewById(R.id.participant_list_icon);
            mTextViewDorm = (TextView) v.findViewById(R.id.participant_list_dorm);
        }

    }

    public class ParticipantListener implements View.OnClickListener {
        Context c;
        String extraDorm;
        String extraName;
        String extraMobile;

        public ParticipantListener(String extraName, String extraMobile, String extraDorm, Context myContext) {
            this.c = myContext;
            this.extraDorm = extraDorm;
            this.extraName = extraName;
            this.extraMobile = extraMobile;
        }
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(c, ParticipantDetail.class);
            intent.putExtra("Dorm", extraDorm);
            intent.putExtra("Name", extraName);
            intent.putExtra("Mobile", extraMobile);
            c.startActivity(intent);
        }
    }
}
