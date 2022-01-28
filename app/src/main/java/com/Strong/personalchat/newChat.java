package com.Strong.personalchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

public class newChat extends AppCompatActivity {

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