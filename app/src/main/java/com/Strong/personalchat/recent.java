package com.Strong.personalchat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.Strong.personalchat.Fragments.callsFragment;
import com.Strong.personalchat.Fragments.recentFragment;
import com.Strong.personalchat.Fragments.requestFragment;
import com.Strong.personalchat.databinding.ActivityRecentBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class recent extends BaseActivity {
    ViewPagerSection viewPagerAdaptor;
    FirebaseAuth firebaseAuth;
    ActivityRecentBinding BindRecent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindRecent =ActivityRecentBinding.inflate(getLayoutInflater());
        setContentView(BindRecent.getRoot());

        firebaseAuth=FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser()==null){
            startActivity(new Intent(this, purpose.class));

        }

        recentFragment recentFragment =new recentFragment();
        callsFragment callsFragment =new callsFragment();
        requestFragment requestFragment =new requestFragment();

        viewPagerAdaptor=new ViewPagerSection(getSupportFragmentManager(), 0);

        viewPagerAdaptor.addFragment(recentFragment, "Primary");
        viewPagerAdaptor.addFragment(callsFragment, "Calls");
        viewPagerAdaptor.addFragment(requestFragment, "Requests");

        BindRecent.dashboardPager.setAdapter(viewPagerAdaptor);
        BindRecent.tabLayoutDashboard.setupWithViewPager(BindRecent.dashboardPager);
        Objects.requireNonNull(BindRecent.tabLayoutDashboard.getTabAt(0)).setIcon(R.drawable.primar_icon);
        Objects.requireNonNull(BindRecent.tabLayoutDashboard.getTabAt(1)).setIcon(R.drawable.call_icon);
        Objects.requireNonNull(BindRecent.tabLayoutDashboard.getTabAt(2)).setIcon(R.drawable.request_icon);

        BindRecent.LogoutButton.setOnClickListener(view -> {
            firebaseAuth=FirebaseAuth.getInstance();
            firebaseAuth.signOut();
            Toast.makeText(this, "SignOut", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, purpose.class));
        });

        BindRecent.floatNewChat.setOnClickListener(view -> {
            Toast.makeText(this, "You Clicked on Float Icon Button", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(recent.this, newChat.class));
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
