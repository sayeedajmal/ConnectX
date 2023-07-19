package com.Strong.ConnectX.Activity;


import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.Strong.ConnectX.Fragments.ViewPagerSection;
import com.Strong.ConnectX.Fragments.callsFragment;
import com.Strong.ConnectX.Fragments.recentFragment;
import com.Strong.ConnectX.Fragments.requestFragment;
import com.Strong.ConnectX.R;
import com.Strong.ConnectX.databinding.ActivityRecentBinding;
import com.Strong.ConnectX.models.CurrentUser;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Objects;

public class recentActivity extends AppCompatActivity {
    ViewPagerSection viewPagerAdaptor;
    ActivityRecentBinding BindRecent;
    private SensorManager sensorManage;
    public static int feedBackFlag = 0;
    float acceleration, accelerationNow, accelerationLast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindRecent = ActivityRecentBinding.inflate(getLayoutInflater());
        setContentView(BindRecent.getRoot());

        sensorManage = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        ShakeToFeedBack();

        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(Token -> {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
            HashMap<String, Object> object = new HashMap<>();
            object.put("Token", Token);
            reference.updateChildren(object);
        });

        if (CurrentUser.getChatUserImage() != null) {
            Glide.with(this).load(CurrentUser.getChatUserImage()).into(BindRecent.setting);
        }

        recentFragment recentFragment = new recentFragment();
        callsFragment callsFragment = new callsFragment();
        requestFragment requestFragment = new requestFragment();

        viewPagerAdaptor = new ViewPagerSection(getSupportFragmentManager(), 0);

        viewPagerAdaptor.addFragment(recentFragment, "Messages");
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
            Glide.with(this).load(CurrentUser.getChatUserImage()).into(BindRecent.setting);
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
            if (feedBackFlag != 1) if (acceleration > 12) {
                goFeedback();
                feedBackFlag = 1;
            } else feedBackFlag = 0;
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private void goFeedback() {
        startActivity(new Intent(this, feedBack.class));
    }
}
