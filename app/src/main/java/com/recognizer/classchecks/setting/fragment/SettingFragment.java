package com.recognizer.classchecks.setting.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.library.basic.BasicFragment;
import com.recognizer.R;
import com.recognizer.classchecks.login.LoginActivity;
import com.recognizer.classchecks.setting.SettingActivity;
import com.recognizer.common.global.pref.PrefManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends BasicFragment {

    private SettingActivity mSettingActivity;

    private TextView mTVExitLogin;

    public SettingFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSettingActivity = (SettingActivity) getActivity();
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_setting;
    }

    @Override
    public void initView() {
        mTVExitLogin = (TextView) mView.findViewById(R.id.tv_exit_login);
        mTVExitLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitLogin();
            }
        });

        mView.findViewById(R.id.tv_setting_about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(R.id.fl_fragment_setting_container, new AboutFragment());
            }
        });

        mView.findViewById(R.id.tv_setting_question).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast(mSettingActivity, "功能暂未开放", SHORT_SHOW);
            }
        });
    }

    private void exitLogin() {
        PrefManager.removeAll(getContext());
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        mSettingActivity.finish();
    }

}
