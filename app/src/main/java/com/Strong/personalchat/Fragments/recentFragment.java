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
import com.Strong.personalchat.models.UserGetter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class recentFragment extends Fragment {

    FragmentRecyclerviewBinding BindRecycle;
    FirebaseDatabase database;
    ArrayList<UserGetter> arrayList=new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        BindRecycle=FragmentRecyclerviewBinding.inflate(inflater,  container, false);

      recentChatAdaptor adaptor=new recentChatAdaptor(arrayList, getContext());
        BindRecycle.RecyclerView.setAdapter(adaptor);
      LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        BindRecycle.RecyclerView.setLayoutManager(linearLayoutManager);
      database=FirebaseDatabase.getInstance();

      String currentId= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
          @SuppressLint("NotifyDataSetChanged")
          @Override
          public void onDataChange(@NonNull DataSnapshot Snapshot) {
                    arrayList.clear();
                    for (DataSnapshot dataSnapshot: Snapshot.getChildren()) {
                        UserGetter users = dataSnapshot.getValue(UserGetter.class);
                        if (!Objects.equals(dataSnapshot.getKey(), currentId)) {
                            assert users != null;
                            users.setUserId(dataSnapshot.getKey());
                            arrayList.add(users);
                        }
                    }
                    adaptor.notifyDataSetChanged();
          }
          @SuppressLint("NotifyDataSetChanged")
          @Override
          public void onCancelled(@NonNull DatabaseError error) {
              adaptor.notifyDataSetChanged();
          }
      });
      return BindRecycle.getRoot();
    }
}