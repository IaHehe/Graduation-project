package com.recognizer.classchecks.information.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.library.basic.BasicFragment;
import com.recognizer.R;
import com.recognizer.classchecks.information.InformationActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class InformationFragment extends BasicFragment {
    private InformationActivity informationActivity;

    public InformationFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        informationActivity = (InformationActivity) getActivity();
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_information;
    }

    @Override
    public void initView() {

        mView.findViewById(R.id.tv_information_modify_phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(R.id.fl_fragment_information_container, new ModifyPhoneFragment());
            }
        });
    }
}
