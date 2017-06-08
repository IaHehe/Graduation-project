package com.recognizer.common.widget.syllabus;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.library.utils.LogUtil;
import com.library.widget.AlertDialogUtils;
import com.recognizer.R;
import com.recognizer.common.util.ColorDrawableUtils;
import com.recognizer.common.widget.syllabus.model.SimpleSection;

import java.util.List;

/**
 * @author Dongjun Zou
 * @Description 课程表主体表格
 * @email 984147586@qq.com
 * @date 2017/5/7 01:43
 */

public class SyllabusBodyView extends RelativeLayout {

    private final static String [] weeks = {"一", "二", "三", "四", "五", "六", "日"};

    private Context context;
    private int height;
    private int width;
    private int avgHeight;
    private int avgWidth;
    //MarkerView的宽高
    private int markerWidth;
    private int markerHeight;

    // 课程View
    private List<SimpleSection> sections;

    private int columnNum; // 课程表区域的列数
    private int rowNum; // 课程表区域的行数
    private float textSize; // 课程表表格的字体大小
    private int sheetNum; //课表表格的数量

    public SyllabusBodyView(Context context) {
        super(context);
    }

    public SyllabusBodyView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SyllabusBodyView);
        textSize = ta.getDimension(R.styleable.SyllabusBodyView_TextSize, 12);
        columnNum = ta.getInteger(R.styleable.SyllabusBodyView_column, 7);
        rowNum = ta.getInteger(R.styleable.SyllabusBodyView_row, 10);
        ta.recycle();
        sheetNum = columnNum * rowNum;
        /**
         * 先添加子view:markerView
         */
        markerHeight = 12;
        markerWidth = 12;
        LayoutParams markerParams = new LayoutParams(markerWidth, markerHeight);
        // 课程表主体的列数(column)*行数(row)
        for (int i = 0; i < sheetNum; i++) {
            MarkerView mv = new MarkerView(context);
            this.addView(mv, markerParams);
        }
    }

    public SyllabusBodyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * 测量父布局容器
     * 默认是MATCH_PARENT模式下的宽高
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i("zou->onMeasure: ", "this.getWidth:"+this.getWidth()
                + " | this.getHeight"+this.getHeight());
        /**
         *获取父容器的宽和高、平均值：
         */
        width = this.getWidth();
        height = this.getHeight();
        avgWidth = width / columnNum;
        avgHeight = height / rowNum;
    }

    /**
     * 布局子view
     *
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //super.onLayout(changed, l, t, r, b);
        Log.i("onLayout-changed:", ""+changed);
        int childCount = getChildCount();
        Log.i("CourseContentView:zou->", ""+childCount);
        /**
         * markerView定位排列算法
         * 0  -- 6
         * 7  -- 13
         * 14 -- 20
         * 21 -- 27
         * ......
         */
        int left, top, right, bottom;//MarkerView坐标
        int columnNumMarker = 0;//列号
        int rowNumMarker = 0;//行号
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (i < sheetNum) {

                //行号自加
                if (i % columnNum == 0) {
                    columnNumMarker++;
                }
                //列号满量程清零
                if (rowNumMarker == columnNum) {
                    rowNumMarker = 0;
                }
                //列号自加(1~7)
                rowNumMarker++;
                //计算MarkerView位置坐标
                left = avgWidth * rowNumMarker - markerWidth / 2;
                right = avgWidth * rowNumMarker + markerWidth / 2;
                top = avgHeight * columnNumMarker - markerHeight / 2;
                bottom = avgHeight * columnNumMarker + markerHeight / 2;
                //布局MarkerView
                childView.layout(left, top, right, bottom);
            }
            else if(i >= sheetNum) {
                setText((TextView) childView, changed, i);
            }
        }
    }

    private void setText(TextView tv, boolean changed, int index) {
        SimpleSection section = sections.get(index - sheetNum);
        // 设置TextView的宽高,  根据课程节次的开始节到结束节计算TextView的高度
        LayoutParams params = new LayoutParams(avgWidth,
                (section.getEndSection() - section.getStartSection() + 1) * avgHeight);

        tv.setTextSize(textSize);
        tv.setText(section.getContent() + " @" + section.getClassRoom()); //+ " @" + section.getTeacher()
        tv.setLayoutParams(params);
        if(!changed) {
            tv.setGravity(Gravity.CENTER);

            /**
             * 调用layout方法实现布局
             * 该方法是View的放置方法，在View类实现。调用该方法需要传入放置View的
             * 矩形空间左上角left、top值和右下角right、bottom值。这四个值是相对于父控件而言的
             */
            //最后+1dp/-1dp来设置间隔即间隔为2dp
            tv.layout(
                    (section.getDay() - 1) * avgWidth + 1,
                    (section.getStartSection() - 1) * avgHeight + 1,
                    (section.getDay() * avgWidth) -1,
                    (section.getEndSection() * avgHeight - 1)
            );
        }
    }

    /**
     * 设置课程节次List
     * @param sections
     */
    public void setSections(List<SimpleSection> sections) {
        this.sections = sections;
        createSectionView();
    }

    // 根据传入的课程节次，创建TextView用于显示课程内容、任课教师、教室
    private void createSectionView() {
        // 每次设置数据时计算当前的子view个数
        int thizChildCount = getChildCount();
        // 如果有课程数据，清除
        if(thizChildCount > sheetNum) {
            for(int i = thizChildCount-1; i >= sheetNum; i --) {
                this.removeViewAt(i);
            }
        }
        for(int i = 0; i < sections.size(); i ++) {
            final int index = i;
            int bgIdIndex = (int) (Math.random() * ColorDrawableUtils.getColorsLength());
            TextView tv = new TextView(context);
            tv.setBackgroundResource(ColorDrawableUtils.getDrawableBgColor(bgIdIndex));
            tv.setPadding(3, 3, 3, 3);
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(Color.WHITE);
            tv.setTextSize(textSize);
            tv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    SimpleSection section = sections.get(index);
                    String dialogContent = "课程名称：" + section.getContent() + "\n"
                            //+ "任课教师：" + section.getTeacher() + "\n"
                            + "上课地点：" + section.getClassRoom() + "\n"
                            + "上课时间：" + "星期" + weeks[section.getDay()-1]
                            +" / 第" + section.getStartSection() + "-" + section.getEndSection() + "节";
                    AlertDialogUtils.alert(context, AlertDialogUtils.TYPE_INFO, "课程详情", dialogContent, null, "确定", null, null);
                    //Toast.makeText(context, sections.get(index).getContent(), Toast.LENGTH_SHORT).show();
                }
            });
            this.addView(tv);
        }
    }

}