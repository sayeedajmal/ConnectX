package com.Strong.personalchat.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.Strong.personalchat.Fragments.callsFragment;
import com.Strong.personalchat.Fragments.recentFragment;
import com.Strong.personalchat.Fragments.requestFragment;
import com.Strong.personalchat.R;
import com.Strong.personalchat.Fragments.ViewPagerSection;
import com.Strong.personalchat.databinding.ActivityRecentBinding;
import com.Strong.personalchat.Utilities.status;
import com.Strong.personalchat.models.CurrentUser;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Objects;

public class recentActivity extends status {
    ViewPagerSection viewPagerAdaptor;
    ActivityRecentBinding BindRecent;
    String FileName;
    private static final int PERMISSION_REQUEST_CODE = 7;

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

        BindRecent.setting.setOnClickListener(view -> startActivity(new Intent(this, SettingActivity.class)));

        BindRecent.floatNewChat.setOnClickListener(view -> startActivity(new Intent(recentActivity.this, newChatActivity.class)));

        if (CurrentUser.getChatUserImage() != null) {
            Picasso.get().load(CurrentUser.getChatUserImage()).into(BindRecent.setting);
        }

        FileName = "PersonalChat" + File.separator + "Media";
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            createDirectory(FileName);
        } else {
            askPermission();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                createDirectory(FileName);
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void createDirectory(String folderName) {
        File file = new File(Environment.getExternalStoragePublicDirectory("MUSIC"), folderName);
        file.mkdir();
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }
}
