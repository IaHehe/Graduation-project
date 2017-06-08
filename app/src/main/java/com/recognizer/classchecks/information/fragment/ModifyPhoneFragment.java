package com.recognizer.classchecks.information.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.library.basic.BasicFragment;
import com.recognizer.R;
import com.recognizer.classchecks.information.InformationActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ModifyPhoneFragment extends BasicFragment {
    private InformationActivity informationActivity;

    public ModifyPhoneFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        informationActivity = (InformationActivity) getActivity();
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_modify_phone;
    }

    @Override
    public void initView() {

    }

    @Override
    public void updateToolbarMainTitle() {
        super.updateToolbarMainTitle();
        informationActivity.updateToolbarTitle(getString(R.string.modify_phone_fragment_main_title));
    }
}
