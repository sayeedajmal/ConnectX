package com.Strong.personalchat;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.Strong.personalchat.models.newChatGetter;
import com.Strong.personalchat.Adaptors.newChatAdaptor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class newChatFragment extends Fragment {

        View view;
        FirebaseDatabase database;
        RecyclerView newContactRecyclerView;
        ArrayList<newChatGetter> arrayList = new ArrayList<>();
        @Override
        public void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);

        }

        @Override
        public void onViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState){
            super.onViewCreated(view, savedInstanceState);

        }


        @Override
        public View onCreateView (LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState){
            // Inflate the layout for this fragment
            view = inflater.inflate(R.layout.fragment_new_chat, container, false);

            newChatAdaptor adaptor = new newChatAdaptor(arrayList, getContext());
            newContactRecyclerView = view.findViewById(R.id.newContactRecyclerView);
            newContactRecyclerView.setAdapter(adaptor);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            newContactRecyclerView.setLayoutManager(linearLayoutManager);
            database = FirebaseDatabase.getInstance();
            database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    arrayList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        newChatGetter users = dataSnapshot.getValue(newChatGetter.class);
                        users.setUserId(dataSnapshot.getKey());
                        arrayList.add(users);
                    }
                    adaptor.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            return view;
        }
}