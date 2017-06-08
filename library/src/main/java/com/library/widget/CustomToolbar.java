package com.library.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.library.R;

/**
 * @author Dongjun Zou
 * @Description 扩展Toolbar(自定义导航栏，包含左边按钮、中间标题、右边按钮)
 * @email 984147586@qq.com
 * @date 2017/4/30 23:41
 */

public class CustomToolbar extends Toolbar {

    public CustomToolbar(Context context) {
        super(context);
        this.setTitle("");
    }

    public CustomToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setTitle("");
    }

    public CustomToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.setTitle("");
    }

    private TextView mTvMainTitleLeft;
    private TextView mTvMainTitle;
    private TextView mTvMainTitleRight;

    private View mViewToolbar;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
        mTvMainTitleLeft = (TextView) findViewById(R.id.lt_main_title_left);
        mTvMainTitle = (TextView) findViewById(R.id.lt_main_title);
        mTvMainTitleRight = (TextView) findViewById(R.id.lt_main_title_right);
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mViewToolbar = inflater.inflate(R.layout.custom_toolbar, null);
        this.addView(mViewToolbar, layoutParams);
    }

    //设置主title的内容
    public void setMainTitle(String text) {
        mTvMainTitle.setVisibility(View.VISIBLE);
        mTvMainTitle.setText(text);
    }

    //设置主title的内容文字的颜色
    public void setMainTitleColor(int color) {
        mTvMainTitle.setTextColor(color);
    }

    //设置title左边文字
    public void setMainTitleLeftText(String text) {
        mTvMainTitleLeft.setVisibility(View.VISIBLE);
        mTvMainTitleLeft.setText(text);
    }

    //设置title左边文字颜色
    public void setMainTitleLeftColor(int color) {
        mTvMainTitleLeft.setTextColor(color);
    }

    //设置title左边图标
    public void setMainTitleLeftDrawable(int res) {
        Drawable dwLeft = ContextCompat.getDrawable(getContext(), res);
        dwLeft.setBounds(0, 0, dwLeft.getMinimumWidth(), dwLeft.getMinimumHeight());
        mTvMainTitleLeft.setCompoundDrawables(dwLeft, null, null, null);
    }

    public void setMainTitleLeftDrawable(Drawable drawable) {
        mTvMainTitleLeft.setCompoundDrawables(drawable, null, null, null);
    }

    // 设置title左边点击事件
    public void setMainTitleLeftOnClickListener(View.OnClickListener clickListener) {
        this.mTvMainTitleLeft.setOnClickListener(clickListener);
    }

    //设置title右边文字
    public void setMainTitleRightText(String text) {
        mTvMainTitleRight.setVisibility(View.VISIBLE);
        mTvMainTitleRight.setText(text);
    }

    //设置title右边文字颜色
    public void setMainTitleRightColor(int color) {
        mTvMainTitleRight.setTextColor(color);
    }

    //设置title右边图标
    public void setMainTitleRightDrawable(int res) {
        Drawable dwRight = ContextCompat.getDrawable(getContext(), res);
        dwRight.setBounds(0, 0, dwRight.getMinimumWidth(), dwRight.getMinimumHeight());
        mTvMainTitleRight.setCompoundDrawables(null, null, dwRight, null);
    }

    public void setMainTitleRightDrawable(Drawable drawable) {
        mTvMainTitleRight.setCompoundDrawables(null, null, drawable, null);
    }

    // 设置title右边点击事件
    public void setMainTitleRightOnClickListener(View.OnClickListener clickListener) {
        this.mTvMainTitleRight.setOnClickListener(clickListener);
    }

    //设置toolbar状态栏颜色
    public void setToolbarBackground(int res) {
        this.setBackgroundResource(res);
    }

    //设置toolbar左边图标
    public void setToolbarLeftBackImageRes(int res) {
        this.setNavigationIcon(res);
    }

    //设置toolbar左边文字
    public void setToolbarLeftText(String text) {
        this.setNavigationContentDescription(text);
    }

    //设置toolbar的标题
    public void setToolbarTitle(String text) {
        this.setTitle(text);
    }

    //设置toolbar标题的颜色
    public void setToolbarTitleColor(int color) {
        this.setTitleTextColor(color);
    }

    //设置toolbar子标题
    public void setToolbarSubTitleText(String text) {
        this.setSubtitle(text);
    }

    //设置toolbar子标题颜色
    public void setToolbarSubTitleTextColor(int color) {
        this.setSubtitleTextColor(color);
    }
}
