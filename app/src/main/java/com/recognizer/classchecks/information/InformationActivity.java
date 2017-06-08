package com.recognizer.classchecks.information;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.library.basic.BasicActivity;
import com.library.widget.CustomToolbar;
import com.library.widget.ToastUtils;
import com.recognizer.R;
import com.recognizer.classchecks.information.fragment.InformationFragment;


public class InformationActivity extends BasicActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(null == savedInstanceState) {
            addFragment(new InformationFragment());
        }
    }

    @Override
    protected void createToolbar() {
        mCustomToolbar = (CustomToolbar) findViewById(R.id.custom_toolbar_information);
        mCustomToolbar.setMainTitle(getString(R.string.activity_information_main_title));
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_information;
    }

    private void addFragment(Fragment fragment) {
        FragmentManager fm= getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fl_fragment_information_container, fragment);
        ft.commit();
    }
}
