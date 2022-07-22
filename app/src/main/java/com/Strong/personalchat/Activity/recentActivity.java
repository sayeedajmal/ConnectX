package com.Strong.personalchat.Activity;

import android.content.Intent;
import android.os.Bundle;

import com.Strong.personalchat.Fragments.callsFragment;
import com.Strong.personalchat.Fragments.recentFragment;
import com.Strong.personalchat.Fragments.requestFragment;
import com.Strong.personalchat.R;
import com.Strong.personalchat.ViewPagerSection;
import com.Strong.personalchat.databinding.ActivityRecentBinding;
import com.Strong.personalchat.status;

import java.util.Objects;

public class recentActivity extends status {
    ViewPagerSection viewPagerAdaptor;
    ActivityRecentBinding BindRecent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindRecent = ActivityRecentBinding.inflate(getLayoutInflater());
        setContentView(BindRecent.getRoot());


        recentFragment recentFragment = new recentFragment();
        callsFragment callsFragment = new callsFragment();
        requestFragment requestFragment = new requestFragment();

        viewPagerAdaptor = new ViewPagerSection(getSupportFragmentManager(), 0);

        viewPagerAdaptor.addFragment(recentFragment, "Primary");
        viewPagerAdaptor.addFragment(callsFragment, "Calls");
        viewPagerAdaptor.addFragment(requestFragment, "Requests");

        BindRecent.dashboardPager.setAdapter(viewPagerAdaptor);
        BindRecent.tabLayoutDashboard.setupWithViewPager(BindRecent.dashboardPager);
        Objects.requireNonNull(BindRecent.tabLayoutDashboard.getTabAt(0)).setIcon(R.drawable.sendbutton);
        Objects.requireNonNull(BindRecent.tabLayoutDashboard.getTabAt(1)).setIcon(R.drawable.call_icon);
        Objects.requireNonNull(BindRecent.tabLayoutDashboard.getTabAt(2)).setIcon(R.drawable.request_icon);

        BindRecent.Setting.setOnClickListener(view -> startActivity(new Intent(this, SettingActivity.class)));

        BindRecent.floatNewChat.setOnClickListener(view -> startActivity(new Intent(recentActivity.this, newChatActivity.class)));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }


}
