package com.snowdays.snowdaysctrl.activities;

import android.content.Intent;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.snowdays.snowdaysctrl.R;
import com.snowdays.snowdaysctrl.activities.base.BaseActivity;
import com.snowdays.snowdaysctrl.models.APIErrorResponse;
import com.snowdays.snowdaysctrl.models.LoginResponse;
import com.snowdays.snowdaysctrl.models.ResponseData;
import com.snowdays.snowdaysctrl.utilities.ErrorUtils;
import com.snowdays.snowdaysctrl.utilities.KeyStore;
import com.snowdays.snowdaysctrl.utilities.NetworkService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements Callback<ResponseData<LoginResponse>> {

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private ProgressBar mProgressBar;

    // Global
    private Call<ResponseData<LoginResponse>> mCall;
    private LoginResponse mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loadToolbar(getString(R.string.app_name));

        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);

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

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mCall != null) mCall.cancel();
        // Start Progress view
        showProgress();

        mCall = NetworkService.getInstance().login(mEmailView.getText().toString(), mPasswordView.getText().toString());
        mCall.enqueue(this);
    }

    @Override
    public void onResponse(Call<ResponseData<LoginResponse>> call, Response<ResponseData<LoginResponse>> response) {
        hideProgress();

        if (response.isSuccessful() && response.body() != null) {
            mUser = response.body().getData();
            saveTokenAndUserId(mUser.getToken(), mUser.getId());

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        } else if (response.isSuccessful()) {
            finish();
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
    public void onFailure(Call<ResponseData<LoginResponse>> call, Throwable t) {
        hideProgress();

        if (call.isCanceled()) return;
        t.printStackTrace();
    }

    private void saveTokenAndUserId(String token, String userId) {
        KeyStore.saveToken(this, token);
        KeyStore.saveUserId(this, userId);
    }

    private void showProgress() {
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);
    }

    private void hideProgress() {
        mProgressBar.setVisibility(ProgressBar.VISIBLE);
    }
}

