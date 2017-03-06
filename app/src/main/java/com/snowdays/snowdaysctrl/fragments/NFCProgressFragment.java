package com.snowdays.snowdaysctrl.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.snowdays.snowdaysctrl.R;

public class NFCProgressFragment extends Fragment {
    private static final String ARG_TITLE = "title";

    private String mTitle;
    private ProgressBar mProgressBar;
    private TextView mTitleView;
    private ImageView mImageView;

    // variables used to protect the view in case that the task ended before the view was actually created
    private Boolean isDone = false;
    private Boolean failed = false;

    public NFCProgressFragment() {
        // Required empty public constructor
    }

    public static NFCProgressFragment newInstance(String title) {
        NFCProgressFragment fragment = new NFCProgressFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(ARG_TITLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nfcprogress, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // get view references
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        mImageView = (ImageView) view.findViewById(R.id.progress_image);
        mTitleView = (TextView) view.findViewById(R.id.progress_text);

        // Update title
        mTitleView.setText(mTitle);

        if (isDone) taskDone();
        if (failed) taskFailed();
    }

    // Update
    public void taskDone() {
        isDone = true;
        if (mProgressBar == null || mImageView == null) return;

        mProgressBar.setVisibility(ProgressBar.INVISIBLE);
        mImageView.setImageResource(R.drawable.ic_success);
    }

    public void taskFailed() {
        failed = true;
        if (mProgressBar == null || mImageView == null) return;

        mProgressBar.setVisibility(ProgressBar.INVISIBLE);
        mImageView.setImageResource(R.drawable.ic_002_error);
    }
}
