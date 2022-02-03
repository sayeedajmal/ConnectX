package com.Strong.personalchat;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class dashboard extends BaseActivity {
    TabLayout tabLayoutDashboard;
    ViewPager dashboardPager;
    ViewPagerSection viewPagerAdaptor;
    FirebaseAuth firebaseAuth;
    AppCompatImageButton LogoutButton;
    AppCompatImageButton floatNewChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        tabLayoutDashboard = findViewById(R.id.tabLayoutDashboard);
        dashboardPager = findViewById(R.id.dashboardPager);
        LogoutButton=findViewById(R.id.LogoutButton);
        floatNewChat=findViewById(R.id.floatNewChat);

        primary primary=new primary();
        calls calls=new calls();
        request request=new request();

        viewPagerAdaptor=new ViewPagerSection(getSupportFragmentManager(), 0);
        viewPagerAdaptor.addFragment(primary, "Primary");
        viewPagerAdaptor.addFragment(calls, "Calls");
        viewPagerAdaptor.addFragment(request, "Requests");
        dashboardPager.setAdapter(viewPagerAdaptor);
        tabLayoutDashboard.setupWithViewPager(dashboardPager);
        Objects.requireNonNull(tabLayoutDashboard.getTabAt(0)).setIcon(R.drawable.primar_icon);
        Objects.requireNonNull(tabLayoutDashboard.getTabAt(1)).setIcon(R.drawable.call_icon);
        Objects.requireNonNull(tabLayoutDashboard.getTabAt(2)).setIcon(R.drawable.request_icon);

        LogoutButton.setOnClickListener(view -> {
            firebaseAuth=FirebaseAuth.getInstance();
            firebaseAuth.signOut();
            Toast.makeText(this, "SignOut", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, purpose.class));
        });

        floatNewChat.setOnClickListener(view -> {
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
