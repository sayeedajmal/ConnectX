package com.Strong.personalchat;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.Strong.personalchat.Fragments.callsFragment;
import com.Strong.personalchat.Fragments.recentFragment;
import com.Strong.personalchat.Fragments.requestFragment;
import com.Strong.personalchat.databinding.ActivityDashboardBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class dashboard extends BaseActivity {
    ViewPagerSection viewPagerAdaptor;
    FirebaseAuth firebaseAuth;
    ActivityDashboardBinding BindDashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindDashboard=ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(BindDashboard.getRoot());

        recentFragment recentFragment =new recentFragment();
        callsFragment callsFragment =new callsFragment();
        requestFragment requestFragment =new requestFragment();

        viewPagerAdaptor=new ViewPagerSection(getSupportFragmentManager(), 0);

        viewPagerAdaptor.addFragment(recentFragment, "Primary");
        viewPagerAdaptor.addFragment(callsFragment, "Calls");
        viewPagerAdaptor.addFragment(requestFragment, "Requests");

        BindDashboard.dashboardPager.setAdapter(viewPagerAdaptor);
        BindDashboard.tabLayoutDashboard.setupWithViewPager(BindDashboard.dashboardPager);
        Objects.requireNonNull(BindDashboard.tabLayoutDashboard.getTabAt(0)).setIcon(R.drawable.primar_icon);
        Objects.requireNonNull(BindDashboard.tabLayoutDashboard.getTabAt(1)).setIcon(R.drawable.call_icon);
        Objects.requireNonNull(BindDashboard.tabLayoutDashboard.getTabAt(2)).setIcon(R.drawable.request_icon);

        BindDashboard.LogoutButton.setOnClickListener(view -> {
            firebaseAuth=FirebaseAuth.getInstance();
            firebaseAuth.signOut();
            Toast.makeText(this, "SignOut", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, purpose.class));
        });

        BindDashboard.floatNewChat.setOnClickListener(view -> {
            Toast.makeText(this, "You Clicked on Float Icon Button", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(dashboard.this, newChat.class));
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
