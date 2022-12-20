package com.Strong.personalchat.Activity;


import android.content.Intent;
import android.os.Bundle;

import com.Strong.personalchat.Fragments.ViewPagerSection;
import com.Strong.personalchat.Fragments.callsFragment;
import com.Strong.personalchat.Fragments.recentFragment;
import com.Strong.personalchat.Fragments.requestFragment;
import com.Strong.personalchat.R;
import com.Strong.personalchat.Utilities.status;
import com.Strong.personalchat.databinding.ActivityRecentBinding;
import com.Strong.personalchat.models.CurrentUser;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

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

        //FirebaseMessaging.getInstance().getToken().addOnSuccessListener(s -> System.out.println("<<<<<<<<<<<<<<" + s));

        viewPagerAdaptor.addFragment(recentFragment, "Primary");
        viewPagerAdaptor.addFragment(callsFragment, "Calls");
        viewPagerAdaptor.addFragment(requestFragment, "Requests");

        BindRecent.dashboardPager.setAdapter(viewPagerAdaptor);
        BindRecent.tabLayoutDashboard.setupWithViewPager(BindRecent.dashboardPager);
        Objects.requireNonNull(BindRecent.tabLayoutDashboard.getTabAt(0)).setIcon(R.drawable.sendbutton);
        Objects.requireNonNull(BindRecent.tabLayoutDashboard.getTabAt(1)).setIcon(R.drawable.call_icon);
        Objects.requireNonNull(BindRecent.tabLayoutDashboard.getTabAt(2)).setIcon(R.drawable.request_icon);

        BindRecent.setting.setOnClickListener(view -> startActivity(new Intent(this, SettingActivity.class)));

        BindRecent.floatNewChat.setOnClickListener(view -> startActivity(new Intent(recentActivity.this, newChatActivity.class)));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (CurrentUser.getChatUserImage() != null) {
            Picasso.get().load(CurrentUser.getChatUserImage()).into(BindRecent.setting);
        }
    }
}
