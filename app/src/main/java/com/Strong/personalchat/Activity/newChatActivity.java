package com.Strong.personalchat.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.Strong.personalchat.Adaptors.newChatAdaptor;
import com.Strong.personalchat.databinding.ActivityNewchatBinding;
import com.Strong.personalchat.models.newChatGetter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class newChatActivity extends AppCompatActivity {
    ActivityNewchatBinding BindNewChat;
    FirebaseDatabase database;
    ArrayList<newChatGetter> arrayList = new ArrayList<>();

    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindNewChat = ActivityNewchatBinding.inflate(getLayoutInflater());
        setContentView(BindNewChat.getRoot());

        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        database = FirebaseDatabase.getInstance();

        BindNewChat.SearchPerson.requestFocus();

        newChatAdaptor adaptor = new newChatAdaptor(arrayList, this);
        BindNewChat.chatBackButton.setOnClickListener(view -> onBackPressed());

        BindNewChat.SearchPerson.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String searchUser = editable.toString();
                if (!searchUser.isEmpty()) {
                    Query query = reference.orderByChild("username").startAt(searchUser).endAt(searchUser + "\uf8ff");
                    query.addValueEventListener(new ValueEventListener() {
                        @SuppressLint("NotifyDataSetChanged")
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
            }
        });

        BindNewChat.RecyclerView.setAdapter(adaptor);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        BindNewChat.SearchPerson.setText(null);
    }


}