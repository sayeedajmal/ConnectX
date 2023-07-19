package com.Strong.ConnectX.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.Strong.ConnectX.databinding.FragmentRequestBinding;

public class requestFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentRequestBinding BindRecent = FragmentRequestBinding.inflate(inflater, container, false);
        return BindRecent.getRoot();
    }
}