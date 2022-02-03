package com.Strong.personalchat;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

public class newChat extends BaseActivity {

    ViewPager newContactPager;
    AppCompatImageButton chatBackButton;
    ViewPagerSection viewPagerAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newchat);

        newChatFragment newChatFragment=new newChatFragment();
        newContactPager=findViewById(R.id.newContactPager);
        viewPagerAdaptor=new ViewPagerSection(getSupportFragmentManager(), 0);
        viewPagerAdaptor.addFragment(newChatFragment,"");
        newContactPager.setAdapter(viewPagerAdaptor);

        chatBackButton=findViewById(R.id.chatBackButton);
        chatBackButton.setOnClickListener(view -> {
            onBackPressed();
        });
    }
}