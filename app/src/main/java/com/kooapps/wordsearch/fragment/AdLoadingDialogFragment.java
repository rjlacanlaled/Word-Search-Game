package com.kooapps.wordsearch.fragment;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kooapps.wordsearch.R;

public class AdLoadingDialogFragment extends DialogFragment {

    public static final String TAG = "com.kooapps.AdLoadingDialogFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setCancelable(false);
        return inflater.inflate(R.layout.fragment_ad_loading_dialog, container, false);
    }

}