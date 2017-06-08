package com.library.basic;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.library.widget.ToastUtils;

/**
 * @author Dongjun Zou
 * @Description ${todo}
 * @email 984147586@qq.com
 * @date 2017/5/3 01:28
 */

public abstract class BasicFragment extends Fragment {
    protected View mView; // Fragment 对应的布局的View
    protected int SHORT_SHOW = 1;
    protected int LONG_SHOW = 2;

    public BasicFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(null == mView) {
            mView = inflater.inflate(getLayout(), container, false);
        }
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    /**
     * 在Acvitity创建后调用updateToolbar方法修改Activity的导航栏标题及图标
     */
    @Override
    public void onStart() {
        super.onStart();
        updateToolbarMainTitle();
    }

    /**
     * 获取子页面的布局
     * @return
     */
    @LayoutRes
    public abstract int getLayout();

    /**
     * 子页面实现自己的视图
     */
    public abstract void initView();

    /**
     * 子页面可以覆写本方法修改所在Activity的导航栏标题
     */
    public void updateToolbarMainTitle() {

    }

    public void replaceFragment(@IdRes int resId, Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(resId, fragment);
        // 将fragment加入返回栈，当下一个fragment点击返回按钮时不会返回主界面，而是回到当前界面的Fragment
        ft.addToBackStack(null).commit();
    }

    public void customToast(Activity activity, final String content, final int duration) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showToast(getContext(), content,
                        duration == SHORT_SHOW ? ToastUtils.SHORT_TIME : ToastUtils.LONG_TIME);
            }
        });
    }

    public void toast(Activity activity, final String content, final int duration) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), content,
                        duration == SHORT_SHOW ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG).show();
            }
        });
    }
}
