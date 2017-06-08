package com.library.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.library.R;

/**
 * @author Dongjun Zou
 * @Description ${todo}
 * @email 984147586@qq.com
 * @date 2017/5/2 11:28
 */

public class AlertDialogUtils {
    public final static int TYPE_INFO = 1;
    public final static int TYPE_WARNING = 2;
    public final static int TYPE_ERROR = 3;
    public final static int TYPE_OK = 4;

    static AlertDialog alertDialog = null;
    static AlertDialog promptDialog = null;

    public void dismiss(){
        alertDialog.dismiss();
    }

    // 确定，取消，回调
    public static void alert(Context context,int type, String title, String content,
                             final DialogListener positiveListener,
                             String positive, final DialogListener negativeListener, String negative) {
        LayoutInflater inflater = LayoutInflater.from(context);
        final View dialog_view = inflater.inflate(R.layout.layout_dialog_alert, null);
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.show();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(true);
        Window window = alertDialog.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);// AlertDialog中的EditText获取焦点时，弹起软键盘
        window.setContentView(dialog_view);

        chooseIcon(type,(ImageView) dialog_view.findViewById(R.id.icon));

        ((TextView) dialog_view.findViewById(R.id.title)).setText(title);
        if(!TextUtils.isEmpty(content)) {
            TextView tvContent = (TextView) dialog_view.findViewById(R.id.content);
            //tvContent.setGravity(Gravity.CENTER);
            tvContent.setVisibility(View.VISIBLE);
            tvContent.setText(content);
        }
        // 确定
        Button positiveButton = (Button) dialog_view.findViewById(R.id.positiveButton);
        if (positive != null) {
            positiveButton.setText(positive);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    if (positiveListener != null) {
                        positiveListener.callback(new String[]{"",""});
                    }
                }
            });
        } else {
            positiveButton.setVisibility(View.GONE);
        }

        // 取消
        Button negativeButton = (Button) dialog_view.findViewById(R.id.negativeButton);
        if (negative != null) {
            negativeButton.setText(negative);
            negativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    if (negativeListener != null) {
                        negativeListener.callback(new String[]{"",""});
                    }
                }
            });
        } else {
            negativeButton.setVisibility(View.GONE);
        }
        if (positive == null || negative == null) {
            ((View) dialog_view.findViewById(R.id.bottom_separator)).setVisibility(View.GONE);
        }
        if (positive == null && negative == null) {
            ((View) dialog_view.findViewById(R.id.bottom)).setVisibility(View.GONE);
        }
    }


    /**
     * 根据类型使用不同的icon
     * @param type
     * @param imageView
     */
    private static void chooseIcon(int type,ImageView imageView){
        if(imageView!=null){
            switch(type) {
                case TYPE_INFO:
                    imageView.setImageResource(R.mipmap.icon_info);
                    break;
                case TYPE_WARNING:
                    imageView.setImageResource(R.mipmap.icon_warning);
                    break;
                case TYPE_ERROR:
                    imageView.setImageResource(R.mipmap.icon_error);
                    break;
                case TYPE_OK:
                    imageView.setImageResource(R.mipmap.icon_success);
                    break;
            }
        }
    }

    public interface DialogListener {
        public void callback(String[] array);
    }


}
