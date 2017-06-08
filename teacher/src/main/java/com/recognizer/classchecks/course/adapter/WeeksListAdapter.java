package com.recognizer.classchecks.course.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.recognizer.R;


/**
 * @className:      WeeksListAdapter
 * @description:
 * @author:         Dongjun Zou
 * @email:          984147586@qq.com
 * @date:           2017/5/9 16:17
 * @version:
 */
public class WeeksListAdapter extends BaseAdapter {
    private String[] weeks = {"第一周", "第二周", "第三周", "第四周", "第五周", "第六周", "第七周"
            , "第八周", "第九周", "第十周", "第十一周", "第十二周", "第十三周", "第十四周", "第十五周",
            "第十六周", "第十七周", "第十八周", "第十九周", "第二十周"};
    private int weekNumber; // 当前第几周
    private Context context;

    public WeeksListAdapter(Context context, int weekNumber) {
        this.context = context;
        this.weekNumber = weekNumber;
    }

    @Override
    public int getCount() {
        return weeks.length;
    }

    @Override
    public Object getItem(int position) {
        return weeks[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if(convertView == null) {
            view = View.inflate(context, R.layout.weeks_listview_item, null);
        } else {
            view = convertView;
        }

        TextView tvWeeks = (TextView) view.findViewById(R.id.tv_weeks);
        tvWeeks.setText(weeks[position]);
        tvWeeks.setTextColor(context.getResources().getColor(R.color.colorDarkBlue));
        if (position == this.weekNumber-1) {
            tvWeeks.setBackgroundResource(R.drawable.ic_dropdown_week_cur_select_bg);
            tvWeeks.setTextColor(Color.WHITE);
            tvWeeks.setText(weeks[position] + "（本周）");
        } else {
            tvWeeks.setBackgroundResource(R.drawable.btn_chooce_week_bg);
        }
        return view;
        /*TextView tvWeeks;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.weeks_listview_item, null);
            tvWeeks = (TextView) convertView.findViewById(R.id.tv_weeks);
            convertView.setTag(tvWeeks);
        } else {
            tvWeeks = (TextView) convertView.getTag();
        }
        tvWeeks.setText(weeks[position]);
        if (position == this.weekNumber-1) {
            tvWeeks.setBackgroundResource(R.drawable.ic_dropdown_week_cur_select_bg);
            tvWeeks.setTextColor(Color.WHITE);
            tvWeeks.setText(weeks[position] + "（本周）");
        } else {
            tvWeeks.setBackgroundResource(R.drawable.btn_chooce_week_bg);
        }*/

    }
}
