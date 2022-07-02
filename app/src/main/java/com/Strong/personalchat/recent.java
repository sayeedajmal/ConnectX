package com.Strong.personalchat;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.Strong.personalchat.Fragments.callsFragment;
import com.Strong.personalchat.Fragments.recentFragment;
import com.Strong.personalchat.Fragments.requestFragment;
import com.Strong.personalchat.databinding.ActivityRecentBinding;

import java.util.Objects;

public class recent extends AppCompatActivity {
    ViewPagerSection viewPagerAdaptor;
    ActivityRecentBinding BindRecent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindRecent =ActivityRecentBinding.inflate(getLayoutInflater());
        setContentView(BindRecent.getRoot());


        recentFragment recentFragment =new recentFragment();
        callsFragment callsFragment =new callsFragment();
        requestFragment requestFragment =new requestFragment();

        viewPagerAdaptor=new ViewPagerSection(getSupportFragmentManager(), 0);

        viewPagerAdaptor.addFragment(recentFragment, "Primary");
        viewPagerAdaptor.addFragment(callsFragment, "Calls");
        viewPagerAdaptor.addFragment(requestFragment, "Requests");

        BindRecent.dashboardPager.setAdapter(viewPagerAdaptor);
        BindRecent.tabLayoutDashboard.setupWithViewPager(BindRecent.dashboardPager);
        Objects.requireNonNull(BindRecent.tabLayoutDashboard.getTabAt(0)).setIcon(R.drawable.sendbutton);
        Objects.requireNonNull(BindRecent.tabLayoutDashboard.getTabAt(1)).setIcon(R.drawable.call_icon);
        Objects.requireNonNull(BindRecent.tabLayoutDashboard.getTabAt(2)).setIcon(R.drawable.request_icon);

        BindRecent.Setting.setOnClickListener(view ->{
            startActivity(new Intent(this, Setting.class));
        });

        BindRecent.floatNewChat.setOnClickListener(view -> {
            startActivity(new Intent(recent.this, newChat.class));
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
