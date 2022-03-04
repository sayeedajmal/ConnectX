package com.Strong.personalchat.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.Strong.personalchat.Adaptors.recentChatAdaptor;
import com.Strong.personalchat.databinding.FragmentRecyclerviewBinding;
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
    ArrayList<recentGetter> getters =new ArrayList<>();
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

      recentChatAdaptor adaptor=new recentChatAdaptor(getters, getContext());
        BindRecycle.RecyclerView.setAdapter(adaptor);
      LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        BindRecycle.RecyclerView.setLayoutManager(linearLayoutManager);

      database=FirebaseDatabase.getInstance().getReference();
        database.keepSynced(true);

        String currentUser= FirebaseAuth.getInstance().getCurrentUser().getUid();
       database.child("Users").child(currentUser).child("Chats").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    getters.clear();
                    String Uid = dataSnapshot.getKey();
                    assert Uid != null;
                        database.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    recentGetter users = dataSnapshot.getValue(recentGetter.class);
                                    if (Objects.equals(dataSnapshot.getKey(), Uid)) {
                                        users.setUserId(dataSnapshot.getKey());
                                        getters.add(users);
                                    }
                                }
                                adaptor.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                adaptor.notifyDataSetChanged();
                            }
                        });
                    adaptor.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                adaptor.notifyDataSetChanged();
            }
        });
        return BindRecycle.getRoot();
    }
}