package com.Strong.personalchat.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.Strong.personalchat.databinding.FragmentRecyclerviewBinding;
import com.Strong.personalchat.models.newChatGetter;
import com.Strong.personalchat.Adaptors.newChatAdaptor;
import com.Strong.personalchat.models.recentGetter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class newChatFragment extends Fragment {
        FragmentRecyclerviewBinding BindRecycle;
        FirebaseDatabase database;
        ArrayList<newChatGetter> arrayList = new ArrayList<>();
        @Override
        public View onCreateView (@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
            // Inflate the layout for this fragment
            BindRecycle=FragmentRecyclerviewBinding.inflate(inflater,  container, false);
            newChatAdaptor adaptor = new newChatAdaptor(arrayList, getContext());
            BindRecycle.RecyclerView.setAdapter(adaptor);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            BindRecycle.RecyclerView.setLayoutManager(linearLayoutManager);
            database = FirebaseDatabase.getInstance();

            String currentUser= FirebaseAuth.getInstance().getCurrentUser().getUid();
            database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    arrayList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        newChatGetter users = dataSnapshot.getValue(newChatGetter.class);
                        if (!Objects.equals(dataSnapshot.getKey(), currentUser)) {
                            assert users != null;
                            users.setUserId(dataSnapshot.getKey());
                            arrayList.add(users);
                        }
                    }
                    adaptor.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

            return BindRecycle.getRoot();
        }
}