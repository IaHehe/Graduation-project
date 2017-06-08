package com.library.widget;

import android.content.Context;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.library.R;


/**
 * @author Dongjun Zou
 * @Description ${todo}
 * @email 984147586@qq.com
 * @date 2017/5/2 00:09
 */

public class AlertDialog {
    Context context;
    android.app.AlertDialog ad;
    TextView titleView;
    TextView messageView;

    Button positiveButton;
    Button negativeButton;

    public AlertDialog(Context context) {
        this.context=context;
        ad=new android.app.AlertDialog.Builder(context).create();
        ad.show();
        //关键在下面的两行,使用window.setContentView,替换整个对话框窗口的布局
        Window window = ad.getWindow();
        window.setContentView(R.layout.custom_alert_diglog);
        titleView=(TextView)window.findViewById(R.id.title);
        messageView=(TextView)window.findViewById(R.id.message);
        positiveButton = (Button) window.findViewById(R.id.positiveButton);
        negativeButton = (Button) window.findViewById(R.id.negativeButton);
    }
    public void setTitle(int resId)
    {
        titleView.setText(resId);
    }
    public void setTitle(String title) {
        titleView.setText(title);
    }
    public void setMessage(@StringRes int resId) {
        messageView.setVisibility(View.VISIBLE);
        messageView.setText(resId);
    }

    public void setMessage(String message)
    {
        if(!TextUtils.isEmpty(message)) {
            messageView.setVisibility(View.VISIBLE);
            messageView.setText(message);
        }
    }
    /**
     * 设置按钮
     * @param text
     * @param listener
     */
    public void setPositiveButton(String text,final View.OnClickListener listener)
    {
        positiveButton.setText(text);

        positiveButton.setOnClickListener(listener);
    }

    /**
     * 设置按钮
     * @param text
     * @param listener
     */
    public void setNegativeButton(String text,final View.OnClickListener listener)
    {
        negativeButton.setText(text);
        negativeButton.setOnClickListener(listener);
    }
    /**
     * 关闭对话框
     */
    public void dismiss() {
        ad.dismiss();
    }
}
