package com.recognizer.classchecks.clockrecord.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.recognizer.classchecks.clockrecord.bean.ClockRecord;

import cn.lemon.view.adapter.BaseViewHolder;
import cn.lemon.view.adapter.RecyclerAdapter;

/**
 * @author Dongjun Zou
 * @Description ${todo}
 * @email 984147586@qq.com
 * @date 2017/5/8 14:59
 */

public class ClockRecordAdapter extends RecyclerAdapter<ClockRecord> {
    private Context context;

    public ClockRecordAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public BaseViewHolder<ClockRecord> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new ClockRecordHolder(parent, context);
    }
}
