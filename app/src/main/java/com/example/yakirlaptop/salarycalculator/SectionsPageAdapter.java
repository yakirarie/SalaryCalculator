package com.example.yakirlaptop.salarycalculator;


import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SectionsPageAdapter extends FragmentPagerAdapter {
    private final List<Fragment> FragementList =  new ArrayList<>();
    private final List<String> FragementTitleList =  new ArrayList<>();

    public void addFragment(Fragment f , String s){
        FragementList.add(f);
        FragementTitleList.add(s);
    }
    public SectionsPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return FragementTitleList.get(position);
    }

    @Override
    public Fragment getItem(int i) {
        return FragementList.get(i);
    }

    @Override
    public int getCount() {
        return FragementList.size();
    }
}