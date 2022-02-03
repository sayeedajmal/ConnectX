package com.Strong.personalchat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.Strong.personalchat.Adaptors.callsAdaptor;
import com.Strong.personalchat.models.callsGetter;

import java.util.ArrayList;
import java.util.List;

public class calls extends Fragment {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       List<callsGetter> callList = new ArrayList<>();
        //callList.add(new callsGetter("Sayeed Ajmal", "Missed","https://firebasestorage.googleapis.com/v0/b/personalchat-d14fe.appspot.com/o/IMG_20211113_163758_281.webp?alt=media&token=cea3f45d-5ae2-4e2f-9ee5-6b8ef8e48005"));
        //callList.add(new callsGetter("Shoaib Akhtar ", "OutGoing","https://firebasestorage.googleapis.com/v0/b/personalchat-d14fe.appspot.com/o/20211005_122253.jpg?alt=media&token=7c6ff5e5-bd40-457b-a21d-08eefc0a3429"));

        ListView chatListView = view.findViewById(R.id.callListView);
        callsAdaptor callsAdaptor = new callsAdaptor(getActivity(), callList);
        chatListView.setAdapter(callsAdaptor);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calls, container, false);
    }
}