package com.snowdays.snowdaysctrl.activities;

import android.os.Bundle;
import android.widget.TextView;

import com.snowdays.snowdaysctrl.R;
import com.snowdays.snowdaysctrl.models.Participant;


/**
 * Created by alessandropiccoli on 21/01/17.
 */

public class ParticipantDetail extends BaseActivity {

    public static final String PARTICIPANT = "Participant";

    Participant participant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.participant_detail);

        participant = getIntent().getParcelableExtra(PARTICIPANT);

        initUI();
    }

    public void initUI() {
        TextView name = (TextView) findViewById(R.id.participant_name);
        TextView dorm = (TextView) findViewById(R.id.participant_dorm);
        TextView mobile = (TextView) findViewById(R.id.participant_mobile);

        name.setText(participant.getFirstName() + " " + participant.getLastName());
        dorm.setText(participant.getId());
        mobile.setText(participant.getEmail());

    }
}
