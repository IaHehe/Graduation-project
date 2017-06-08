package com.recognizer.classchecks.setting;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.library.basic.BasicActivity;
import com.library.widget.CustomToolbar;
import com.recognizer.R;
import com.recognizer.classchecks.setting.fragment.SettingFragment;

public class SettingActivity extends BasicActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(null == savedInstanceState) {
            addFragment(new SettingFragment());
        }
    }

    @Override
    protected void createToolbar() {
        mCustomToolbar = (CustomToolbar) findViewById(R.id.custom_toolbar_setting);
        mCustomToolbar.setMainTitle(getString(R.string.activity_setting_main_title));
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_setting;
    }

    private void addFragment(Fragment fragment) {
        FragmentManager fm= getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fl_fragment_setting_container, fragment);
        ft.commit();
    }
}
