package com.Strong.ConnectX.Fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerSection extends FragmentPagerAdapter {

    List<Fragment> fragmentList = new ArrayList<>();
    List<String> fragmentTitles = new ArrayList<>();

    public ViewPagerSection(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    public void addFragment(Fragment fragment, String title) {
        fragmentList.add(fragment);
        fragmentTitles.add(title);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @NonNull
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitles.get(position);
    }
}