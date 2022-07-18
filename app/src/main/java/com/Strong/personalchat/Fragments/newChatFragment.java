package com.Strong.personalchat.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.Strong.personalchat.Adaptors.newChatAdaptor;
import com.Strong.personalchat.databinding.FragmentRecyclerviewBinding;
import com.Strong.personalchat.models.newChatGetter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class newChatFragment extends Fragment {
    FragmentRecyclerviewBinding BindRecycle;
    DatabaseReference databaseReference;
    String searchUser;
    ArrayList<newChatGetter> arrayList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        BindRecycle = FragmentRecyclerviewBinding.inflate(inflater, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        newChatAdaptor adaptor = new newChatAdaptor(arrayList, getContext());
        BindRecycle.RecyclerView.setAdapter(adaptor);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.hasChildren()) {
                        searchUser = dataSnapshot.child("searchUser").getValue(String.class);
                        if (!Objects.equals(searchUser, "") & !Objects.equals(searchUser, null)) {
                            searchUser = dataSnapshot.child("searchUser").getValue(String.class);
                            Query query = databaseReference.orderByChild("username").startAt(searchUser).endAt(searchUser + "\uf8ff");
                            query.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChildren()) {
                                        arrayList.clear();
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            newChatGetter getter = snapshot.getValue(newChatGetter.class);
                                            assert getter != null;
                                            getter.setUserId(snapshot.getKey());
                                            arrayList.add(getter);
                                        }
                                        adaptor.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                        }
                        adaptor.notifyDataSetChanged();
                    }

                }
                newChatAdaptor adaptor = new newChatAdaptor(arrayList, getContext());
                BindRecycle.RecyclerView.setAdapter(adaptor);
                adaptor.notifyDataSetChanged();
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                adaptor.notifyDataSetChanged();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        BindRecycle.RecyclerView.setLayoutManager(linearLayoutManager);
        return BindRecycle.getRoot();
    }

}