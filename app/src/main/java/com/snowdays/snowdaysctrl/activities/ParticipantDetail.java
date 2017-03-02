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

        loadToolbar();

        participant = getIntent().getParcelableExtra(PARTICIPANT);

        initUI();
    }

    public void initUI() {
        // General
        TextView name = (TextView) findViewById(R.id.participant_name);
        TextView surname = (TextView) findViewById(R.id.participant_surname);
        TextView university = (TextView) findViewById(R.id.participant_university);
        TextView gender = (TextView) findViewById(R.id.participant_gender);

        // Contact
        TextView mobile = (TextView) findViewById(R.id.participant_phone);
        TextView email = (TextView) findViewById(R.id.participant_email);

        //Event
        TextView dorm = (TextView) findViewById(R.id.participant_dorm);

        // Add data
        name.setText(participant.getFirstName());
        surname.setText(participant.getLastName());
        university.setText(participant.getUniversity());
        gender.setText(participant.getGender());

        //TODO: add phone
        mobile.setText(participant.getPhone());
        email.setText(participant.getEmail());

        //TODO: add dorm
        dorm.setText(participant.getId());
    }
}
