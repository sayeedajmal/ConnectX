package com.Strong.personalchat.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.Strong.personalchat.Adaptors.recentChatAdaptor;
import com.Strong.personalchat.databinding.FragmentRecyclerviewBinding;
import com.Strong.personalchat.models.CurrentUser;
import com.Strong.personalchat.models.recentGetter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class recentFragment extends Fragment {

    FragmentRecyclerviewBinding BindRecycle;
    DatabaseReference database;
    String uid;
    ArrayList<recentGetter> getters = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        BindRecycle = FragmentRecyclerviewBinding.inflate(inflater, container, false);

        recentChatAdaptor adaptor = new recentChatAdaptor(getters, getContext());
        BindRecycle.RecyclerView.setAdapter(adaptor);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        BindRecycle.RecyclerView.setLayoutManager(linearLayoutManager);

        database = FirebaseDatabase.getInstance().getReference();
        uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        initRecent(adaptor);

        //Getting Current User data
        database.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (Objects.equals(dataSnapshot.getKey(), FirebaseAuth.getInstance().getUid())) {
                        CurrentUser.setUsername(dataSnapshot.child("username").getValue(String.class));
                        CurrentUser.setEmail(dataSnapshot.child("email").getValue(String.class));
                        CurrentUser.setPassword(dataSnapshot.child("password").getValue(String.class));
                        CurrentUser.setChatUserImage(dataSnapshot.child("chatUserImage").getValue(String.class));
                        CurrentUser.setUserId(dataSnapshot.child("userId").getValue(String.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        database.keepSynced(true);


        BindRecycle.swipeRefresh.setOnRefreshListener(() -> {
            getters.clear();
            initRecent(adaptor);
            BindRecycle.swipeRefresh.setRefreshing(false);
        });

        return BindRecycle.getRoot();
    }

    private void initRecent(recentChatAdaptor adaptor) {
        database.child("Users").child(uid).child("Chats").addValueEventListener(new ValueEventListener() {
            //Getting UID of particular chat user
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    getters.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String Uid = dataSnapshot.getKey();
                    assert Uid != null;

                    //Getting data of particular user by taking their id
                    database.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                recentGetter users = dataSnapshot.getValue(recentGetter.class);
                                if (Objects.equals(dataSnapshot.getKey(), Uid)) {
                                    assert users != null;
                                    users.setUserId(dataSnapshot.getKey());
                                    getters.add(users);
                                }
                            }
                            adaptor.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}