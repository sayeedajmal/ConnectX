package com.Strong.personalchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

public class newChat extends AppCompatActivity {

    ViewPager newContactPager;
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}