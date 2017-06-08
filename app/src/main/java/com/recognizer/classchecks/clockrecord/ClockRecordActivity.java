package com.recognizer.classchecks.clockrecord;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.library.basic.BasicActivity;
import com.library.widget.CustomToolbar;
import com.library.widget.PopupView;
import com.library.widget.adapter.PopupViewAdapter;
import com.recognizer.R;
import com.recognizer.classchecks.clockrecord.adapter.ClockRecordAdapter;
import com.recognizer.classchecks.clockrecord.bean.ClockRecord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.lemon.view.RefreshRecyclerView;
import cn.lemon.view.adapter.Action;

public class ClockRecordActivity extends BasicActivity {

    private PopupView mPopupView;

    private RefreshRecyclerView mRecyclerView;
    private ClockRecordAdapter mClockAdapter;
    private Handler mHandler;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initRefreshView();
    }

    private void initView() {
        mPopupView = (PopupView) findViewById(R.id.dv_clock_record);
        initPopupData();
    }

    @Override
    protected void createToolbar() {
        mCustomToolbar = (CustomToolbar) findViewById(R.id.custom_toolbar_clockrecord);
        mCustomToolbar.setMainTitle(getString(R.string.activity_clockrecord_main_title));
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_clock_record;
    }

    private void initPopupData() {
        String [] menuDataWeek = {"第一周", "第二周", "第三周", "第四周", "第五周", "第六周", "第七周", "第八周", "第九周", "第十周"
                , "第十一周", "第十二周", "第十三周", "第十四周", "第十五周", "第十六周", "第十七周", "第十八周", "第九十周", "第二十周"};
        String [] menuDateNormal = {"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};//
        String [] menuDataAbsence = {"正常", "缺勤"};//

        PopupViewAdapter adapterWeek = new PopupViewAdapter(ClockRecordActivity.this, menuDataWeek);
        PopupViewAdapter adapterNormal = new PopupViewAdapter(ClockRecordActivity.this, menuDateNormal);
        PopupViewAdapter adapterAbsence = new PopupViewAdapter(ClockRecordActivity.this, menuDataAbsence);

        List<PopupViewAdapter> adapters = new ArrayList<>();
        adapters.add(adapterWeek);
        adapters.add(adapterNormal);
        adapters.add(adapterAbsence);
        mPopupView.setPopupViewAdapter(adapters, new PopupView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(String[] result) {
                Toast.makeText(ClockRecordActivity.this, Arrays.toString(result), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initRefreshView() {

        mHandler = new Handler();

        mClockAdapter = new ClockRecordAdapter(this);
        mRecyclerView = (RefreshRecyclerView) findViewById(R.id.recycler_view_clock_record);
        //mRecyclerView.setSwipeRefreshColors(0xFF437845, 0xFFE44F98, 0xFF2FAC21);
        mRecyclerView.setSwipeRefreshColorsFromRes(R.color.colorPrimary, R.color.colorPrimary, R.color.colorPrimary);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mClockAdapter);
        mRecyclerView.setRefreshAction(new Action() {
            @Override
            public void onAction() {
                getData(true);
                page ++;
            }
        });

        mRecyclerView.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                getData(false);
                page++;
            }
        });

        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.showSwipeRefresh();
                getData(true);
            }
        });
    }

    public void getData(final boolean isRefresh) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isRefresh) {
                    page = 1;
                    mClockAdapter.clear();
                    mClockAdapter.addAll(initVirtualData());
                    mRecyclerView.dismissSwipeRefresh();
                    mRecyclerView.getRecyclerView().scrollToPosition(0);
                } else {
                    mClockAdapter.addAll(initVirtualData());
                    if (page >= 3) {
                        mRecyclerView.showNoMore();
                    }
                }
            }
        }, 1500);
    }

    public ClockRecord[] initVirtualData() {
        return new ClockRecord[] {
                new ClockRecord(1, "大学物理BI", "徐毅", "A102", "缺勤", "第三周", "星期一", "2017-2-15"),
                new ClockRecord(2, "Android开发艺术探索", "刘艺", "B306", "正常", "第三周", "星期二", "2017-03-05"),
                new ClockRecord(3, "数据库概论", "王帅", "D205", "正常", "第二周", "星期三", "2017-02-22"),
                new ClockRecord(4, "大学英语II", "李勇", "B501", "正常", "第二周", "星期一", "2017-02-03"),
                new ClockRecord(5, "C程序分析与设计", "刘晨", "C102", "正常", "第二周", "星期二", "2017-02-01"),
                new ClockRecord(6, "数字逻辑基础", "王敏", "E312", "缺勤", "第二周", "第二周", "2017-02-02"),
                new ClockRecord(7, "中国近代史", "张立", "E205", "正常", "第二周", "星期五", "2017-02-17"),
                new ClockRecord(8, "大学物理BI", "徐毅", "B208", "正常", "第三周",  "星期五", "2017-2-17"),
                new ClockRecord(9, "数据库概论", "王帅", "D205", "正常", "第二周", "星期四", "2017-02-24"),
                new ClockRecord(10, "Android开发艺术探索", "刘艺", "D201", "缺勤", "第三周", "星期五", "2017-03-07"),
                new ClockRecord(11, "大学英语II", "李勇", "D205", "正常", "第二周", "星期一", "2017-02-02"),
        };
    }
}
