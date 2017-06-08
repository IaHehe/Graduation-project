package com.recognizer.classchecks.clockin.fragment;

import android.os.Bundle;
import android.widget.ListView;

import com.library.basic.BasicFragment;
import com.recognizer.R;
import com.recognizer.classchecks.clockin.ClockInActivity;
import com.recognizer.classchecks.clockin.adapter.ClockInResultAdapter;
import com.recognizer.classchecks.clockin.model.bean.ClockInResultBean;

import java.util.List;

public class ClockInResultFragment extends BasicFragment {

    private ClockInActivity mClockInActivity;

    private ListView mListViewOfClockinResult;
    private ClockInResultAdapter mClockInResultAdapter;

    private List<ClockInResultBean> mListOfResultBeans;

    public ClockInResultFragment() {

    }


    public void setListOfResultBean(List<ClockInResultBean> lists) {
        mListOfResultBeans = lists;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mClockInActivity = (ClockInActivity) getActivity();
        mClockInResultAdapter = new ClockInResultAdapter(mClockInActivity, mListOfResultBeans);
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_clock_in_result;
    }

    @Override
    public void initView() {
        mListViewOfClockinResult = (ListView) mView.findViewById(R.id.lv_clock_in_result);
        mListViewOfClockinResult.setAdapter(mClockInResultAdapter);
    }

    @Override
    public void updateToolbarMainTitle() {
        mClockInActivity.updateToobarTitle(R.drawable.icon_return, null, getString(R.string.clockin_result_fragment_main_title), 0, null);
    }
}
