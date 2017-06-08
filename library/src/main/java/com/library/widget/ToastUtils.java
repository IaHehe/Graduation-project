package com.library.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.library.R;


/**
 * @author Dongjun Zou
 * @Description ${todo}
 * @email 984147586@qq.com
 * @date 2017/5/2 01:22
 */

public class ToastUtils {
    public final static int LONG_TIME = 1;
    public final static int SHORT_TIME = 2;

    private static Toast mToast;
    private static Toast mToastCenter;

    /**
     * 对toast的简易封装。线程不安全，不可以在非UI线程调用。
     */
    private static void showToast(Context appContext, String str, int showTime, int gravity) {
        if (appContext == null) {
            throw new RuntimeException("ToastUtils not initialized!");
        }

        if(gravity == Gravity.CENTER) {
            if (mToastCenter == null) {
                mToastCenter = Toast.makeText(appContext, str, showTime);
                LayoutInflater inflate = (LayoutInflater)
                        appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflate.inflate(R.layout.custom_toast, null);
                mToastCenter.setView(view);
                mToastCenter.setGravity(gravity, 0, 0);
            }
            mToast = mToastCenter;
            mToast.setText(str);
            mToast.show();
        }
    }


    /**
     * 长时间居中位置显示。
     */
    private static void showToastLong(Context context, final String str) {
        showToast(context, str, Toast.LENGTH_LONG, Gravity.CENTER);
    }

    /**
     * 短时间居中位置显示。
     */
    private static void showToastShort(Context context, final String str) {
        showToast(context, str, Toast.LENGTH_SHORT, Gravity.CENTER);
    }

    /**
     * 对外接口
     * @param context
     * @param str
     */
    public static void showToast(Context context, final String str,  int duration ) {
        if(duration == SHORT_TIME) {
            showToastShort(context, str);
        } else {
            showToastLong(context, str);
        }
    }
}
