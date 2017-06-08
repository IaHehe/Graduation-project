package com.recognizer.classchecks.clockin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.library.basic.BasicActivity;
import com.library.widget.CustomToolbar;
import com.recognizer.R;
import com.recognizer.classchecks.clockin.fragment.PhotographFragment;

public class ClockInActivity extends BasicActivity {

    private double mLongitude;
    private double mLatitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mLongitude = intent.getDoubleExtra("longitude", -0.0);
        mLatitude = intent.getDoubleExtra("latitude", -0.0);
        if(null == savedInstanceState) {
            addFragment(new PhotographFragment()); // 调用系统相机或者选择系统相册图片
        }
    }

    @Override
    protected void createToolbar() {
        mCustomToolbar = (CustomToolbar) findViewById(R.id.custom_toolbar_clockin);
        mCustomToolbar.setMainTitle(getString(R.string.activity_clickin_main_title));
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_clock_in;
    }

    private void addFragment(Fragment fragment) {
        FragmentManager fm= getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fl_fragment_clockin_container, fragment);
        ft.commit();
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }
}
