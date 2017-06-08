package com.recognizer.classchecks.clockrecord.adapter;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;


import com.recognizer.R;
import com.recognizer.classchecks.clockrecord.bean.ClockRecord;

import cn.lemon.view.adapter.BaseViewHolder;

/**
 * @author Dongjun Zou
 * @Description ${todo}
 * @email 984147586@qq.com
 * @date 2017/5/8 15:00
 */

public class ClockRecordHolder extends BaseViewHolder<ClockRecord> {

    private Context context;

    private TextView tv_course_name;
    private TextView tv_clock_type;
    private TextView tv_teacher;
    private TextView tv_class_room;
    private TextView tv_week_and_weekonday;
    private TextView tv_date_time;

    public ClockRecordHolder(ViewGroup parent, Context context) {
        super(parent, R.layout.holder_clockrecord);
        this.context = context;
    }

    @Override
    public void onInitializeView() {
        super.onInitializeView();
        tv_course_name = findViewById(R.id.tv_course_name);
        tv_clock_type = findViewById(R.id.tv_clock_type);
        ((TextView)findViewById(R.id.tv_teacher_flag)).setText("任课教师: ");
        tv_teacher = findViewById(R.id.tv_teacher);
        ((TextView)findViewById(R.id.tv_class_room_flag)).setText("上课地点: ");
        tv_class_room = findViewById(R.id.tv_class_room);
        tv_week_and_weekonday = findViewById(R.id.tv_week_and_weekonday);
        tv_date_time = findViewById(R.id.tv_data_time);
    }

    @Override
    public void setData(ClockRecord data) {
        super.setData(data);
        tv_course_name.setText(data.getCourseName());
        tv_clock_type.setText(data.getClockSituation());
        if("正常".equals(data.getClockSituation())) {
            tv_clock_type.setTextColor(context.getResources().getColor(R.color.colorLightYellow));
            tv_clock_type.setBackground(context.getResources().getDrawable(R.drawable.rect_lightyellow_broder_shade));
        } else {
            tv_clock_type.setTextColor(context.getResources().getColor(R.color.colorBloodOrange));
            tv_clock_type.setBackground(context.getResources().getDrawable(R.drawable.rect_bloodorange_broder_shade));
        }

        tv_teacher.setText(data.getTeacherName());
        tv_class_room.setText(data.getClassRoom());
        tv_week_and_weekonday.setText(data.getWeek() + " / " + data.getDayOnWeek());
        tv_date_time.setText(data.getDate());
    }

    @Override
    public void onItemViewClick(ClockRecord data) {
        super.onItemViewClick(data);
        //点击事件
        Log.i("ClockRecordHolder","onItemViewClick");
    }
}
