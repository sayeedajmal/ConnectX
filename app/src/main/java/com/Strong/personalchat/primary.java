package com.Strong.personalchat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.Strong.personalchat.Adaptors.primaryAdaptor;
import com.Strong.personalchat.models.primaryGetter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class primary extends Fragment {

    View view;
    FirebaseDatabase database;
    DatabaseReference reference;
    RecyclerView chatListView;
    ArrayList<primaryGetter> arrayList=new ArrayList<>();
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
        // Inflate the layout for this fragment
      view=inflater.inflate(R.layout.fragment_primary, container, false);

      primaryAdaptor adaptor=new primaryAdaptor(arrayList, getContext(), false);
      chatListView=view.findViewById(R.id.chatListView);
      chatListView.setAdapter(adaptor);
      LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
      chatListView.setLayoutManager(linearLayoutManager);
      database=FirebaseDatabase.getInstance();

      String currentId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
          @SuppressLint("NotifyDataSetChanged")
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
                    arrayList.clear();
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        primaryGetter users=dataSnapshot.getValue(primaryGetter.class);
                        if (!dataSnapshot.getKey().equals(currentId)) {
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
      return view;
    }
}