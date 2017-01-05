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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NFCProgressFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NFCProgressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NFCProgressFragment extends Fragment {
    private static final String ARG_TITLE = "title";

    private String mTitle;
    private ProgressBar mProgressBar;
    private TextView mTitleView;
    private ImageView mImageView;

    private Boolean isDone = false;

    private OnFragmentInteractionListener mListener;

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
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // Update
    public void taskDone() {
        isDone = true;

        if (mProgressBar == null || mImageView == null) return;

        mProgressBar.setVisibility(ProgressBar.INVISIBLE);
        mImageView.setImageResource(R.drawable.ic_check_box_checked);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
