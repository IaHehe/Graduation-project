package com.recognizer.common.util;

import android.os.CountDownTimer;
import android.os.Handler;
import android.widget.Button;

/**
 * @author Dongjun Zou
 * @Description ${todo}
 * @email 984147586@qq.com
 * @date 2017/5/13 19:28
 */

public class SMSCodeCountdown extends CountDownTimer{

    private static Handler mHandler;
    public static final int IN_RUNNING = 1001;
    public static final int END_RUNNING = 1002;


    /**
     * @param millisInFuture
     *            // 倒计时的时长
     * @param countDownInterval
     *            // 间隔时间
     * @param handler
     *            // 通知进度的Handler
     */
    public SMSCodeCountdown(long millisInFuture, long countDownInterval,
                             Handler handler) {
        super(millisInFuture, countDownInterval);
        mHandler = handler;
    }

    // 结束
    @Override
    public void onFinish() {
        if (mHandler != null)
            mHandler.obtainMessage(END_RUNNING, "重获验证码").sendToTarget();
    }

    // 开始
    @Override
    public void onTick(long millisUntilFinished) {
        if (mHandler != null)
            mHandler.obtainMessage(IN_RUNNING,
                    ("重获验证码(" + millisUntilFinished / 1000) + ")").sendToTarget();
    }
}
