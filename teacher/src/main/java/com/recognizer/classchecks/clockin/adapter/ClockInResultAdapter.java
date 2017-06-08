package com.recognizer.classchecks.clockin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.recognizer.R;
import com.recognizer.classchecks.clockin.model.bean.ClockInResultBean;

import java.util.List;

/**
 * @author Dongjun Zou
 * @Description ${todo}
 * @email 984147586@qq.com
 * @date 2017/5/25 01:45
 */

public class ClockInResultAdapter extends BaseAdapter{

    private List<ClockInResultBean> mClockInResultBeans;
    private Context context;

    public ClockInResultAdapter(Context context, List<ClockInResultBean> clockInResultBean) {
        this.context = context;
        this.mClockInResultBeans = clockInResultBean;
    }

    @Override
    public int getCount() {
        return mClockInResultBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return mClockInResultBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if(convertView == null) { //View.inflate(context, R.layout.clock_in_result_item, null);
            convertView =  LayoutInflater.from(context).inflate(R.layout.clock_in_result_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mTVStuNoLabel.setText("学号：");
        viewHolder.mTVStuNo.setText(mClockInResultBeans.get(position).getStuJWAccount());
        if(mClockInResultBeans.get(position).getClockinType() == 1) {
            viewHolder.mTVStuClockType.setText("正常");
            viewHolder.mTVStuClockType.setTextColor(context.getResources().getColor(R.color.colorLightYellow));
            viewHolder.mTVStuClockType.setBackground(context.getResources().getDrawable(R.drawable.rect_lightyellow_broder_shade));
        } else {
            viewHolder.mTVStuClockType.setText("已通知学生考勤");
            viewHolder.mTVStuClockType.setTextColor(context.getResources().getColor(R.color.colorBloodOrange));
            viewHolder.mTVStuClockType.setBackground(context.getResources().getDrawable(R.drawable.rect_bloodorange_broder_shade));
        }


        viewHolder.mTVStuNameLabel.setText("姓名：");
        viewHolder.mTVStuName.setText(mClockInResultBeans.get(position).getStudentName());
        viewHolder.mTVStuClassLabel.setText("班级：");
        viewHolder.mTVStuClass.setText(mClockInResultBeans.get(position).getStuClass());

        return convertView;
    }

    class ViewHolder {
        public TextView mTVStuNoLabel;
        public TextView mTVStuNo;
        public TextView mTVStuClockType; // 正常、缺勤
        public TextView mTVStuNameLabel;
        public TextView mTVStuName;
        public TextView mTVStuClassLabel;
        public TextView mTVStuClass;

        public ViewHolder(View v) {
            mTVStuNoLabel = (TextView) v.findViewById(R.id.tv_stu_no_label);
            mTVStuNo = (TextView) v.findViewById(R.id.tv_clock_in_result_stu_no);
            mTVStuClockType = (TextView) v.findViewById(R.id.tv_stu_clock_type);
            mTVStuNameLabel = (TextView) v.findViewById(R.id.tv_stu_name_label);
            mTVStuName = (TextView) v.findViewById(R.id.tv_clock_in_result_stu_name);
            mTVStuClassLabel = (TextView) v.findViewById(R.id.tv_stu_class_label);
            mTVStuClass = (TextView) v.findViewById(R.id.tv_clock_in_result_stu_class);
        }
    }
}
