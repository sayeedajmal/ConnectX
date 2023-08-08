package com.Strong.ConnectX.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.Strong.ConnectX.Adaptors.recentChatAdaptor;
import com.Strong.ConnectX.Utilities.SharedPref;
import com.Strong.ConnectX.databinding.FragmentRecyclerviewBinding;
import com.Strong.ConnectX.models.CurrentUser;
import com.Strong.ConnectX.models.recentGetter;
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
    DatabaseReference reference;
    recentChatAdaptor adaptor;
    ArrayList<recentGetter> getters = new ArrayList<>();
    private String MineId;

    private int FirstAppear = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        BindRecycle = FragmentRecyclerviewBinding.inflate(inflater, container, false);

        getters.clear();
        adaptor = new recentChatAdaptor(getters, getContext());
        BindRecycle.RecyclerView.setAdapter(adaptor);

        reference = FirebaseDatabase.getInstance().getReference();
        MineId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        initRecent(adaptor);

        //Getting Current User data
        reference.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (Objects.equals(dataSnapshot.getKey(), FirebaseAuth.getInstance().getUid())) {
                        String UserName = dataSnapshot.child("username").getValue(String.class);
                        String Email = dataSnapshot.child("email").getValue(String.class);
                        String chatUserImage = dataSnapshot.child("chatUserImage").getValue(String.class);
                        String UID = dataSnapshot.child("userId").getValue(String.class);

                        CurrentUser.setUsername(UserName);
                        CurrentUser.setEmail(Email);
                        CurrentUser.setChatUserImage(chatUserImage);
                        CurrentUser.setUserId(UID);

                        //SharedPreferences
                        if (FirstAppear != 1) {
                            SharedPref sharedPref = new SharedPref(requireContext());
                            sharedPref.SharedSave(UserName, UID, chatUserImage, Email);
                            FirstAppear = 1;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference.keepSynced(true);


        BindRecycle.swipeRefresh.setOnRefreshListener(() -> {
            getters.clear();
            initRecent(adaptor);
            BindRecycle.swipeRefresh.setRefreshing(false);
        });

        return BindRecycle.getRoot();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initRecent(recentChatAdaptor adaptor) {
        reference.child("Users").child(MineId).child("Chats").addValueEventListener(new ValueEventListener() {
            //Getting UID of particular chat user
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                getters.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String Uid = dataSnapshot.getKey();
                    assert Uid != null;

                    //Getting data of particular user by taking their id
                    reference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @SuppressLint("NotifyDataSetChanged")
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
        adaptor.notifyDataSetChanged();
    }
}