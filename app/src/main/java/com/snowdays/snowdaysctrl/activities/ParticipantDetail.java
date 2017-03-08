package com.snowdays.snowdaysctrl.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.snowdays.snowdaysctrl.R;
import com.snowdays.snowdaysctrl.activities.base.BaseActivity;
import com.snowdays.snowdaysctrl.models.APIErrorResponse;
import com.snowdays.snowdaysctrl.models.Participant;
import com.snowdays.snowdaysctrl.models.ResponseData;
import com.snowdays.snowdaysctrl.utilities.ErrorUtils;
import com.snowdays.snowdaysctrl.utilities.NetworkService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by alessandropiccoli on 21/01/17.
 */

public class ParticipantDetail extends BaseActivity implements Callback<ResponseData<Participant>> {

    public static final String PARTICIPANT_ID = "participantId";

    private static Participant participant;
    private String participantId;

    // UI
    public ProgressBar mSpinner;
    public ScrollView mContentView;

    //Network
    public Call<ResponseData<Participant>> mCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.participant_detail);

        loadToolbar(getString(R.string.app_name));
        mSpinner = (ProgressBar) findViewById(R.id.progress_bar_detail);
        mContentView = (ScrollView) findViewById(R.id.participant_content);

        participantId = getIntent().getStringExtra(PARTICIPANT_ID);

        loadData();
    }

    // Network Activity

    public void loadData() {
        mContentView.setVisibility(View.GONE);
        mSpinner.setVisibility(View.VISIBLE);

        mCall = NetworkService.getInstance().getParticipant(getHeaders(), participantId);
        mCall.enqueue(this);
    }

    @Override
    public void onResponse(Call<ResponseData<Participant>> call, Response<ResponseData<Participant>> response) {
        mContentView.setVisibility(View.VISIBLE);
        mSpinner.setVisibility(View.GONE);

        if (response.isSuccessful()) {
            participant = response.body().getData();
            initUI();

        } else {
            APIErrorResponse error = ErrorUtils.parseError(response);
            if (error.message() != null && error.message().length() > 0) {
                setMessage(error.message());
            } else {
                setMessage("Error while reading server's response");
            }
        }
    }

    @Override
    public void onFailure(Call<ResponseData<Participant>> call, Throwable t) {

        mContentView.setVisibility(View.VISIBLE);
        mSpinner.setVisibility(View.GONE);

        setMessage("Error while contacting the server");
    }


    // UI

    public void initUI() {

        // General
        TextView name = (TextView) findViewById(R.id.participant_name);
        TextView surname = (TextView) findViewById(R.id.participant_surname);
        TextView university = (TextView) findViewById(R.id.participant_university);
        TextView participantID = (TextView) findViewById(R.id.participant_id);

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
        participantID.setText(participant.getId());

        mobile.setText(participant.getPhone());
        email.setText(participant.getEmail());

        dorm.setText(participant.getDorm());

        // Floating button setup
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.nfc_write_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open NFCReadActivity with writing purposes
                Intent intent = new Intent(ParticipantDetail.this, NFCWriteActivity.class);
                intent.putExtra(NFCWriteActivity.EXTRA_PARTICIPANT, ParticipantDetail.getParticipant());
                startActivity(intent);
            }
        });
    }

    // Getters
    public static Participant getParticipant() {
        return participant;
    }
}
