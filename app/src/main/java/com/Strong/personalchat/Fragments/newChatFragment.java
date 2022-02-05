package com.Strong.personalchat.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.Strong.personalchat.databinding.FragmentNewChatBinding;
import com.Strong.personalchat.models.newChatGetter;
import com.Strong.personalchat.Adaptors.newChatAdaptor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class newChatFragment extends Fragment {

        FirebaseDatabase database;
        ArrayList<newChatGetter> arrayList = new ArrayList<>();
        @Override
        public View onCreateView (@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
            // Inflate the layout for this fragment
            FragmentNewChatBinding BindChat=FragmentNewChatBinding.inflate(inflater,  container, false);
            newChatAdaptor adaptor = new newChatAdaptor(arrayList, getContext());
            BindChat.newContactRecyclerView.setAdapter(adaptor);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            BindChat.newContactRecyclerView.setLayoutManager(linearLayoutManager);
            database = FirebaseDatabase.getInstance();
            String currentUser= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
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

            return BindChat.getRoot();
        }
}