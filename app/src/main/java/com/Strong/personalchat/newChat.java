package com.Strong.personalchat;
import android.os.Bundle;

import com.Strong.personalchat.Fragments.newChatFragment;
import com.Strong.personalchat.databinding.ActivityNewchatBinding;

public class newChat extends BaseActivity {
 ActivityNewchatBinding BindNewChat;
    ViewPagerSection viewPagerAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindNewChat=ActivityNewchatBinding.inflate(getLayoutInflater());
        setContentView(BindNewChat.getRoot());

        newChatFragment newChatFragment=new newChatFragment();
        viewPagerAdaptor=new ViewPagerSection(getSupportFragmentManager(), 0);
        viewPagerAdaptor.addFragment(newChatFragment,"");
        BindNewChat.newContactPager.setAdapter(viewPagerAdaptor);

        BindNewChat.chatBackButton.setOnClickListener(view -> {
            onBackPressed();
        });
    }
}