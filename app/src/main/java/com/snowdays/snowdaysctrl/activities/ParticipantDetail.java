package com.snowdays.snowdaysctrl.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.snowdays.snowdaysctrl.R;
import com.snowdays.snowdaysctrl.models.APIErrorResponse;
import com.snowdays.snowdaysctrl.models.Participant;
import com.snowdays.snowdaysctrl.models.ResponseData;
import com.snowdays.snowdaysctrl.utilities.ErrorUtils;
import com.snowdays.snowdaysctrl.utilities.NetworkService;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alessandropiccoli on 21/01/17.
 */

public class ParticipantDetail extends BaseActivity {

    public static final String DORM = "Dorm";
    public static final String NAME = "Name";
    public static final String MOBILE = "Mobile";

    private String participantID;
    private String participantName;
    private String participantMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.participant_detail);

        participantID = getIntent().getStringExtra(DORM);
        participantName = getIntent().getStringExtra(NAME);
        participantMobile = getIntent().getStringExtra(MOBILE);

        initUI();
    }

    public void initUI() {
        TextView name = (TextView) findViewById(R.id.participant_name);
        TextView dorm = (TextView) findViewById(R.id.participant_dorm);
        TextView mobile = (TextView) findViewById(R.id.participant_mobile);

        name.setText(participantName);
        dorm.setText(participantID);
        mobile.setText(participantMobile);

    }
}
