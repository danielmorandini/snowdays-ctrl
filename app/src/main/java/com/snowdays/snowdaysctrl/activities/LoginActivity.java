package com.snowdays.snowdaysctrl.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.snowdays.snowdaysctrl.R;
import com.snowdays.snowdaysctrl.models.LoginResponse;
import com.snowdays.snowdaysctrl.models.ResponseData;
import com.snowdays.snowdaysctrl.utilities.KeyStore;
import com.snowdays.snowdaysctrl.utilities.NetworkService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements Callback<ResponseData<LoginResponse>> {

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    // Global
    private Call<ResponseData<LoginResponse>> mCall;
    private ResponseData<LoginResponse> mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mCall != null) mCall.cancel();
        // TODO: remove dummy data
        //mCall = NetworkService.getInstance().login(mEmailView.getText().toString(), mPasswordView.getText().toString());
        mCall = NetworkService.getInstance().login("daniel.morandini", "ciao");
        mCall.enqueue(this);
    }

    @Override
    public void onResponse(Call<ResponseData<LoginResponse>> call, Response<ResponseData<LoginResponse>> response) {
        if (response.isSuccessful() && response.body() != null) {
            mUser = response.body();
            saveToken(mUser.getData().getToken());

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        } else if (response.isSuccessful()) {
            finish();
        } else {
            // We encountered some errors. print (has to be deserialized, check - response.errorBody -)
        }
    }

    @Override
    public void onFailure(Call<ResponseData<LoginResponse>> call, Throwable t) {
        if (call.isCanceled()) return;
        t.printStackTrace();
    }

    private void saveToken(String token) {
        KeyStore.saveToken(this, token);
    }
}

