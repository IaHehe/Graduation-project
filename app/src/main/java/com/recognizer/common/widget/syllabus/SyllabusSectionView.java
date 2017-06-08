package com.recognizer.common.widget.syllabus;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.recognizer.R;

/**
 * @author Dongjun Zou
 * @Description 课程表最左边的节次， 显示每天的课程节数,默认显示10节课
 * @email 984147586@qq.com
 * @date 2017/5/7 01:36
 */

public class SyllabusSectionView extends LinearLayout{
    private static int DEFAULT_COURSE_DAY_NUMBER = 10;

    public SyllabusSectionView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.setOrientation(HORIZONTAL);

        LinearLayout ll = new LinearLayout(context);
        LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        ll.setOrientation(VERTICAL);
        this.addView(ll,llParams);

        // 获取自定义属性
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SyllabusSectionView);
        // 字体大小
        float textSzie = ta.getDimension(R.styleable.SyllabusSectionView_numTextSize, 12);
        // 表格宽度
        int textViewWitdh = (int) ta.getDimension(R.styleable.SyllabusSectionView_numTextWidth, 0);
        // 表格高度
        int textViewHeight = (int) ta.getDimension(R.styleable.SyllabusSectionView_numTextHeight, 0);
        // 字体颜色
        int textColor = ta.getColor(R.styleable.SyllabusSectionView_numTextColor, 0);
        // 单个课时表格的背景
        Drawable textViewDrawable = ta.getDrawable(R.styleable.SyllabusSectionView_numTextBackground);
        // 每天的课时数（节数）
        int courseNumber = ta.getInteger(R.styleable.SyllabusSectionView_numTextNumber, DEFAULT_COURSE_DAY_NUMBER);
        ta.recycle();
        //        int colorDarkBlue = getResources().getColor(R.color.colorDarkBlue);
        int colorGray = getResources().getColor(R.color.colorGray);

        LayoutParams tvParams = new LayoutParams(textViewWitdh != 0 ? textViewWitdh : LayoutParams.MATCH_PARENT, textViewHeight);
        LayoutParams tvRowLineParams = new LayoutParams(textViewWitdh != 0 ? textViewWitdh : LayoutParams.MATCH_PARENT, 1);

        for (int i = 1; i <= courseNumber; i++) {

            TextView tv = new TextView(context);
            tv.setBackground(textViewDrawable);
            tv.setTextSize(textSzie);
            tv.getPaint().setTextSize(textSzie);
            tv.setTextColor(textColor);
            tv.setGravity(Gravity.CENTER);
            tv.setText("" + (i));

            TextView tvLine = new TextView(context);
            tvLine.setBackgroundColor(colorGray);

            ll.addView(tv, tvParams);
            ll.addView(tvLine, tvRowLineParams);
        }
    }
}
