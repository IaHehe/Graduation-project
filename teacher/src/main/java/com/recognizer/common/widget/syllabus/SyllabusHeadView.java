package com.recognizer.common.widget.syllabus;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.library.utils.CalendarUtils;
import com.recognizer.R;


/**
 * @author Dongjun Zou
 * @Description 课程表表头，显示星期一至星期五
 * @email 984147586@qq.com
 * @date 2017/5/7 01:33
 */

public class SyllabusHeadView extends LinearLayout{

    private LayoutParams tvMonthsParams;
    private LayoutParams tvLineParams;
    private float mTextSize;
    private int mTextColor;
    private Drawable mTextBackground;

    private LayoutParams tvParams;

    public SyllabusHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);

        int colorGray = getResources().getColor(R.color.colorGray);
        //int colorLightBlue = getResources().getColor(R.color.colorLightBlue);
        int color = getResources().getColor(R.color.course_head_bg_color);

        //设置父布局为垂直布局
        this.setOrientation(this.VERTICAL);
        //上描线
        View culomnLine = new View(context);
        culomnLine.setBackgroundColor(colorGray);
        LayoutParams tclParams = new LayoutParams(LayoutParams.MATCH_PARENT,1);
        this.addView(culomnLine,tclParams);

        LinearLayout ll = new LinearLayout(context);
        LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,0,1);
        ll.setOrientation(HORIZONTAL);
        this.addView(ll,llParams);

        /**
         * 获取从XML映射而来的属性
         */
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SyllabusHeadView);
        mTextColor = ta.getColor(R.styleable.SyllabusHeadView_textColor, 0);
        mTextSize = ta.getDimension(R.styleable.SyllabusHeadView_textSize, 6);
        mTextBackground = ta.getDrawable(R.styleable.SyllabusHeadView_textBackground);
        //释放TypedArray所占用的资源
        ta.recycle();

        /**
         * 添加七个textView用于显示星期一到星期天
         */
        //设置textView的布局横向权重(第三个参数),高度wrap_content
        // 7个表示星期的格子权重为2，显示月份的格子权重为1.2
        tvParams = new LayoutParams(0, LayoutParams.MATCH_PARENT, 2);
        tvLineParams = new LayoutParams(1, LayoutParams.MATCH_PARENT);
        tvMonthsParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1.2f);


        String[] week = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};

        //添加月份栏
        TextView tvMonths = new TextView(context);
        tvMonths.setTextColor(mTextColor);
        tvMonths.setTextSize(mTextSize);
        // 解决自定义控件中setText()设置字体相同大小无法与原生控件一致的问题
        tvMonths.getPaint().setTextSize(mTextSize);
        Log.i("zoumTextSize:", "mTextSize:"+mTextSize);
        tvMonths.setGravity(Gravity.CENTER);
        tvMonths.setBackground(mTextBackground);
        tvMonths.setText(CalendarUtils.getCurrentMonth()+"月");
        ll.addView(tvMonths, tvMonthsParams);

        for (int i = 0; i < 7; i++) {

            TextView tv = new TextView(context);
            TextView tvLine = new TextView(context);

            tv.setTextSize(mTextSize);
            tv.getPaint().setTextSize(mTextSize);
            tv.setTextColor(mTextColor);
            //在API level16（4.1以上系统）以上才可使用
            tv.setBackground(mTextBackground);
            tv.setGravity(Gravity.CENTER);
            tv.setText(week[i]);
            tv.setPadding(0,0,0,0);

            tvLine.setBackgroundColor(colorGray);

            //向父布局添加子控件
            ll.addView(tvLine, tvLineParams);
            ll.addView(tv, tvParams);
        }
        //下描线
        View culomnLine2 = new View(context);
        culomnLine2.setBackgroundColor(colorGray);
        LayoutParams tclParams2 = new LayoutParams(LayoutParams.MATCH_PARENT,1);
        this.addView(culomnLine2,tclParams2);
        // 在SyllabusHeadView中放置的ll(LinearLayout)，ll放置了15个TextView
        int currentWeek = CalendarUtils.getCurrentWeek(CalendarUtils.WEEK_BY_INT);
        // 因为ll中放了16个TextView，第一个为月份，第二个为分割线，第三个为显示星期(从第1个开始以此推理)
        // 获取星期几的格子使用当前星期*2即可(currentWeek*2)
        TextView choosedTv = (TextView) ll.getChildAt(currentWeek*2);
        choosedTv.setText(CalendarUtils.getCurrentDay() +"\n" + week[currentWeek-1]);
        choosedTv.setBackgroundColor(colorGray);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
