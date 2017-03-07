package com.snowdays.snowdaysctrl.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;

import com.snowdays.snowdaysctrl.R;
import com.snowdays.snowdaysctrl.models.Participant;

import java.security.Permission;
import java.security.Permissions;


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

        loadToolbar(getString(R.string.app_name));

        participant = getIntent().getParcelableExtra(PARTICIPANT);
        //participantInfo = getIntent().getParcelableExtra(PARTICIPANT);

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

        mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + participant.getDorm()));
                int checkPermission = ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.CALL_PHONE);
                TelephonyManager telMgr = (TelephonyManager) getSystemService(v.getContext().TELEPHONY_SERVICE);
                int simState = telMgr.getSimState();
                if (checkPermission != PackageManager.PERMISSION_GRANTED && simState == TelephonyManager.SIM_STATE_READY) {
                    startActivity(callIntent);
                } else {
                    setMessage("Error while trying to call");
                }
            }
        });

        // Add data
        name.setText(participant.getFirstName());
        surname.setText(participant.getLastName());
        university.setText(participant.getUniversity());
        gender.setText(participant.getGender());

        mobile.setText(participant.getPhone());
        email.setText(participant.getEmail());

        dorm.setText(participant.getDorm());
    }
}
