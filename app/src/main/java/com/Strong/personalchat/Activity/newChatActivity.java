package com.Strong.personalchat.Activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import com.Strong.personalchat.Fragments.newChatFragment;
import com.Strong.personalchat.ViewPagerSection;
import com.Strong.personalchat.databinding.ActivityNewchatBinding;
import com.Strong.personalchat.models.newChatGetter;
import com.Strong.personalchat.status;

public class newChatActivity extends status {
    ActivityNewchatBinding BindNewChat;
    ViewPagerSection viewPagerAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindNewChat = ActivityNewchatBinding.inflate(getLayoutInflater());
        setContentView(BindNewChat.getRoot());

        newChatFragment newChatFragment = new newChatFragment();
        viewPagerAdaptor = new ViewPagerSection(getSupportFragmentManager(), 0);
        viewPagerAdaptor.addFragment(newChatFragment, "");
        BindNewChat.newContactPager.setAdapter(viewPagerAdaptor);
        BindNewChat.chatBackButton.setOnClickListener(view -> onBackPressed());

        BindNewChat.SearchPerson.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                newChatGetter.setPersonSearch(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}