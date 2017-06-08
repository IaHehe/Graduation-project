package com.recognizer.classchecks.course;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.library.mvp.BaseActivity;
import com.library.utils.CalendarUtils;
import com.library.utils.StringUtils;
import com.library.widget.CustomToolbar;
import com.library.widget.LoadingDialogUtils;
import com.recognizer.R;
import com.recognizer.classchecks.course.adapter.WeeksListAdapter;
import com.recognizer.classchecks.course.model.CourseModel;
import com.recognizer.classchecks.course.model.bean.CourseBean;
import com.recognizer.classchecks.course.presenter.CoursePresenter;
import com.recognizer.classchecks.course.view.CourseView;
import com.recognizer.common.global.pref.PrefManager;
import com.recognizer.common.widget.syllabus.SyllabusBodyView;


public class CourseActivity extends BaseActivity<CourseView, CoursePresenter>
        implements View.OnClickListener, View.OnTouchListener, CourseView {

    private final static String[] weeks = {"第一周", "第二周", "第三周", "第四周", "第五周", "第六周", "第七周"
            , "第八周", "第九周", "第十周", "第十一周", "第十二周", "第十三周", "第十四周", "第十五周",
            "第十六周", "第十七周", "第十八周", "第十九周", "第二十周"};

    private SyllabusBodyView mSyllabusBodyView;
    private TextView mTextViewTitleYear;  // 顶部显示当前年

    private View mSyllabusSelectListView;   // 顶部教学周下拉列表
    private PopupWindow mWeekListPopup;
    private LinearLayout mLinearLayoutWeek;
    private TextView mTvSelectList;

    private Dialog mDialog;
    // 记录用户选择的教学周的标记(0~19)
    private int mTeachingWeek = -1;
    // 标记是否加载课表(及是否通过点击菜单‘课表’进入)，如果没有，在屏幕可见时加载数据，
    // 如果已经加载过了，下次屏幕可见时不在加载，减少Http请求
    private boolean isLoadCourse = false;
    private String mJWAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSysabusSelectView();

        mSyllabusBodyView = (SyllabusBodyView) findViewById(R.id.sbv_add_course);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 标记是否加载课表(及是否通过点击菜单‘课表’进入)，如果没有，在屏幕可见时加载数据，
        // 如果已经加载过了，下次屏幕可见时不在加载，减少Http请求
        if(!isLoadCourse) {
            isLoadCourse = true;
            presenter.obtainCourse();
        }
    }

    @Override
    public CoursePresenter createPresenter() {
        return new CoursePresenter(new CourseModel());
    }

    @Override
    protected void createToolbar() {
        mCustomToolbar = (CustomToolbar) findViewById(R.id.custom_toolbar_course);
        mCustomToolbar.setMainTitle(getString(R.string.activity_course_main_title));
        mCustomToolbar.setMainTitleRightText("导入课表");
        mCustomToolbar.setMainTitleRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ImportTableActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_course;
    }

    /**
     * 页面顶部的下列选择框（选择周次用于查看某一周次的课表）
     */
    private void initSysabusSelectView() {
        mTextViewTitleYear = (TextView) findViewById(R.id.title_year);
        mTextViewTitleYear.setText(CalendarUtils.getCurrentYear()+"年");//

        mLinearLayoutWeek = (LinearLayout) findViewById(R.id.ll_show_week_list_course);
        mLinearLayoutWeek.setOnClickListener(this);

        mTvSelectList = (TextView) findViewById(R.id.title_select_week);
        mTvSelectList.setText("教学周");

        // 填充下拉选择框
        mSyllabusSelectListView = View.inflate(CourseActivity.this, R.layout.week_list_dropwindow, null);
        // 初始化Android PopupWindow对象
        mWeekListPopup = new PopupWindow(mSyllabusSelectListView);
        mWeekListPopup.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        mWeekListPopup.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        mWeekListPopup.setFocusable(true);
        mWeekListPopup.setOutsideTouchable(true); // 设置非PopupWindow区域可触摸

        // 这句话必须有，否则
        // 按返回键
        // popwindow不能消失
        mWeekListPopup.setBackgroundDrawable(new BitmapDrawable(getResources()));
        mSyllabusSelectListView.setOnTouchListener(this);
    }

    /**
     * 设置下拉列表需要显示的ListView
     * @param curWeek
     */
    private void setWeekPopupListView(int curWeek) {
        //ppw里的ListView
        final ListView ppwListView = (ListView) mSyllabusSelectListView.findViewById(R.id.weeks_list);
        ppwListView.setDividerHeight(0);//隐藏分隔线
        final WeeksListAdapter wlAdapter = new WeeksListAdapter(CourseActivity.this, curWeek);
        ppwListView.setAdapter(wlAdapter);
        ppwListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mWeekListPopup.dismiss();
                mTvSelectList.setText(String.valueOf(wlAdapter.getItem(position)));
                mTeachingWeek = position + 1; // +1是因为WeeksListAdapter中使用的数组
                presenter.obtainCourse();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_show_week_list_course: // 下拉方式选周数
                if (mWeekListPopup.isShowing()) {
                    mWeekListPopup.dismiss();
                } else {
                    mWeekListPopup.showAsDropDown(mLinearLayoutWeek);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()) {
            //设置外部点击关闭window
            case R.id.ppw_layout:
                if (mWeekListPopup.isShowing()) {
                    mWeekListPopup.dismiss();
                }
                break;
        }
        return false;
    }

    @Override
    public void showLoading() {
        mDialog = LoadingDialogUtils.createLoadingDialog(CourseActivity.this, "正在加载课表...");
    }

    @Override
    public void hideLoading() {
        LoadingDialogUtils.closeDialog(mDialog);
    }

    @Override
    public void showMsg(String msg) {
        toast(msg, SHORT_SHOW);
    }

    @Override
    public void initPageTop(final CourseBean.ApiBody body) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int curWeek = Integer.parseInt(body.getCurWeek());
                mTvSelectList.setText(weeks[curWeek-1]);
                setWeekPopupListView(curWeek);
            }
        });
    }

    @Override
    public void setData(final CourseBean mCourseData) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSyllabusBodyView.setSections(mCourseData.getApi_body().getCourseData());
            }
        });
    }

    @Override
    public int getTeachingWeek() {
        return mTeachingWeek;
    }

    @Override
    public String getJWAccount() {
        mJWAccount = PrefManager.getString(getApplicationContext(), PrefManager.JW_ACCOUNT, "");
        return this.mJWAccount;
    }

    @Override
    public void showResult(int what, String msg) {
        if(NOT_HAVE_JW_ACCOUNT == what) {
            customToast(msg, SHORT_SHOW);
        }
    }
}
