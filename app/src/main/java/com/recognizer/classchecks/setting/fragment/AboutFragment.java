package com.recognizer.classchecks.setting.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.library.basic.BasicFragment;
import com.recognizer.R;
import com.recognizer.classchecks.setting.SettingActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends BasicFragment {

    private SettingActivity mSettingActivity;

    public AboutFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSettingActivity = (SettingActivity) getActivity();
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_about;
    }

    @Override
    public void initView() {

    }

    @Override
    public void updateToolbarMainTitle() {
        super.updateToolbarMainTitle();
        mSettingActivity.updateToolbarTitle(getString(R.string.settings_about));
    }
}
