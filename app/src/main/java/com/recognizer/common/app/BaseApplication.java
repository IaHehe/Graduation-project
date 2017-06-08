package com.recognizer.common.app;

import android.app.Application;
import android.util.Log;

import java.util.LinkedHashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * @author Dongjun Zou
 * @Description ${todo}
 * @email 984147586@qq.com
 * @date 2017/5/12 13:16
 */

public class BaseApplication extends Application {
    private static final String TAG = "JPush";
    @Override
    public void onCreate() {
        super.onCreate();

        //极光推送调试模式
        JPushInterface.setDebugMode(true);
        //极光推送初始化
        JPushInterface.init(this);


        // 设置Tag
        String currentAppTag = "student";
        Set<String> tagSet = new LinkedHashSet<String>();
        tagSet.add(currentAppTag);
        JPushInterface.setTags(getApplicationContext(), tagSet, new TagAliasCallback() {

            @Override
            public void gotResult(int code, String s, Set<String> set) {
                String logs;
                switch (code) {
                    case 0:
                        logs = "Set tag and alias success";
                        Log.i(TAG, logs);
                        break;

                    case 6002:
                        logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                        Log.i(TAG, logs);
                        /*if (ExampleUtil.isConnected(getApplicationContext())) {
                            mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 60);
                        } else {
                            Logger.i(TAG, "No network");
                        }*/
                        break;

                    default:
                        logs = "Failed with errorCode = " + code;
                        Log.e(TAG, logs);
                }
            }
        });
    }
}
