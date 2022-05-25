package com.Strong.personalchat.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.Strong.personalchat.Adaptors.callsAdaptor;
import com.Strong.personalchat.databinding.FragmentRecyclerviewBinding;
import com.Strong.personalchat.models.callsGetter;

import java.util.ArrayList;

public class callsFragment extends Fragment {

    FragmentRecyclerviewBinding BindRecycle;
    ArrayList<callsGetter> callList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        BindRecycle=FragmentRecyclerviewBinding.inflate(inflater,  container, false);

        callList.add(new callsGetter("Sayeed Ajmal", "Missed",""));
        callList.add(new callsGetter("Shoaib Akhtar ", "OutGoing",""));

        callsAdaptor callsAdaptor = new callsAdaptor(getContext(), callList);
        BindRecycle.RecyclerView.setAdapter(callsAdaptor);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        BindRecycle.RecyclerView.setLayoutManager(linearLayoutManager);

        return BindRecycle.getRoot();
    }
}