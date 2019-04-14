package com.example.yakirlaptop.salarycalculator;


import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;


public class MainActivity extends AppCompatActivity {
    DatabaseOpenHelper dbhelper;
    private ViewPager mViewPager;
    private SectionsPageAdapter adapter;
    private long backPressedTime;
    private Toast backToasty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbhelper = new DatabaseOpenHelper(this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(1);

    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new Tab2Fragment(), getString(R.string.View_Hours));
        adapter.addFragment(new Tab1Fragment(), getString(R.string.Add_Work_Day));
        viewPager.setAdapter(adapter);
    }

    public SectionsPageAdapter getAdapter(){
        return adapter;
    }

    @Override
    public void onBackPressed() {

        if(backPressedTime+2000>System.currentTimeMillis()){
            backToasty.cancel();
            super.onBackPressed();
            return;
        }
        else{
            backToasty = Toasty.info(getApplicationContext(), R.string.Press_back_again_to_exit,Toasty.LENGTH_LONG);
            backToasty.show();

        }
        backPressedTime = System.currentTimeMillis();
    }

}
