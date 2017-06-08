package com.library.basic;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.library.R;
import com.library.widget.CustomToolbar;
import com.library.widget.ToastUtils;
import com.library.widget.util.StatusBarUtil;

public abstract class BasicActivity extends AppCompatActivity {

    protected CustomToolbar mCustomToolbar;
    protected int mStatusBarColor;
    protected int SHORT_SHOW = 1;
    protected int LONG_SHOW = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        createToolbar();
        setStatusBar();
    }

    /**
     * 因为Activity 左边的按钮大多用于返回，所以在父类中直接为自定义toolbar 的左边按钮设置点击事件
     * 当某一个Activity toolbar 左边按钮不用于返回时，在Activity 的 onResume() 方法中
     * 重写toolbar左边按钮的点击事件即可
     */
    @Override
    protected void onStart() {
        super.onStart();
        // 这里为自定义toolbar 左边的文字或图标设置点击事件，调用onBackPressed()方法
        mCustomToolbar.setMainTitleLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 初始化自定义的toolbar，并设置导航栏的左、中、右的标题和图标
     */
    protected abstract void createToolbar();

    /**
     * 子类返回子Activity对应的布局文件，用于父类初始化Activity的布局
     * @return
     */
    @LayoutRes
    protected abstract int getLayout();

    /**
    * 默认的状态栏颜色，子类可以覆写实现其状态栏的颜色
    * */
    protected void setStatusBar() {
        mStatusBarColor = getResources().getColor(R.color.colorPrimary);
        StatusBarUtil.setColor(this, mStatusBarColor);
    }

    /**
     * 更新Activity的toolbar的主标题
     * @param str
     */
    public void updateToolbarTitle(String str) {
        mCustomToolbar.setMainTitle(str);
    }

    public void updateToobarTitle(@DrawableRes int leftIconResId, String leftTitle, String mainTitle, @DrawableRes int rightIconResId, String rightTitle) {
        if(leftIconResId != 0) {
            mCustomToolbar.setMainTitleLeftDrawable(leftIconResId);
        } else {
            mCustomToolbar.setMainTitleLeftDrawable(null);
        }
        mCustomToolbar.setMainTitleLeftText(leftTitle);
        mCustomToolbar.setMainTitle(mainTitle);
        if(rightIconResId != 0) {
            mCustomToolbar.setMainTitleRightDrawable(rightIconResId);
        } else {
            mCustomToolbar.setMainTitleRightDrawable(null);
        }
        mCustomToolbar.setMainTitleRightText(rightTitle);
    }

    public void customToast(final String content, final int duration) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showToast(getApplicationContext(), content,
                        duration == SHORT_SHOW ? ToastUtils.SHORT_TIME : ToastUtils.LONG_TIME);
            }
        });
    }

    public void toast(final String content, final int duration) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), content,
                        duration == SHORT_SHOW ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG).show();
            }
        });
    }
}

