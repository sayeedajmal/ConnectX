package com.Strong.personalchat.Activity;


import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Toast;

import com.Strong.personalchat.Fragments.ViewPagerSection;
import com.Strong.personalchat.Fragments.callsFragment;
import com.Strong.personalchat.Fragments.recentFragment;
import com.Strong.personalchat.Fragments.requestFragment;
import com.Strong.personalchat.R;
import com.Strong.personalchat.Utilities.status;
import com.Strong.personalchat.databinding.ActivityRecentBinding;
import com.Strong.personalchat.models.CurrentUser;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class recentActivity extends status {
    ViewPagerSection viewPagerAdaptor;
    ActivityRecentBinding BindRecent;
    private SensorManager sensorManage;
    float acceleration, accelerationNow, accelerationLast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindRecent = ActivityRecentBinding.inflate(getLayoutInflater());
        setContentView(BindRecent.getRoot());

        sensorManage = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        ShakeToFeedBack();

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

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManage.unregisterListener(sensorEventListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        (sensorManage).registerListener(sensorEventListener, sensorManage.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        if (CurrentUser.getChatUserImage() != null) {
            Picasso.get().load(CurrentUser.getChatUserImage()).into(BindRecent.setting);
        }
    }

    private void ShakeToFeedBack() {
        acceleration = 10f;
        accelerationLast = SensorManager.GRAVITY_EARTH;
        accelerationNow = SensorManager.GRAVITY_EARTH;
        (sensorManage).registerListener(sensorEventListener, sensorManage.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    private final SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            accelerationLast = accelerationNow;
            accelerationNow = (float) Math.sqrt(((double) x * x + y * y + z * z));
            float delta = accelerationNow - accelerationLast;
            acceleration = acceleration * 0.9f + delta;
            if (acceleration > 12) {
                goFeedback();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private void goFeedback() {
        startActivity(new Intent(this, feedBack.class));
    }
}
