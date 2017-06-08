package com.recognizer.common.global.pref;

import android.content.Context;

import com.library.utils.cache.PrefUtils;

/**
 * @author Dongjun Zou
 * @Description ${todo}
 * @email 984147586@qq.com
 * @date 2017/5/30 22:53
 */

public class PrefManager {

    // isAutoLogin phone smscode jwAccount userType faceLable

    public final static String AUTO_LOGIN_SIGN = "AUTO_LOGIN_SIGN";
    public final static String LOGIN_ACCOUNT = "LOGIN_ACCOUNT";
    public final static String LOGIN_SMS_CODE = "LOGIN_SMS_CODE";
    public final static String JW_ACCOUNT = "JW_ACCOUNT";
    public final static String USER_TYPE = "USER_TYPE";
    public final static String FACE_LABLE = "FACE_LABLE";

    public static void setString(Context ctx, String what, String value) {
        PrefUtils.setString(ctx, what, value);
    }

    public static void setBoolean(Context ctx, String what, boolean value) {
        PrefUtils.setBoolean(ctx, what, value);
    }

    public static void setInt(Context ctx, String what, int value) {
        PrefUtils.setInt(ctx, what, value);
    }

    public static boolean getBoolean(Context ctx, String what, boolean defValue) {
        return PrefUtils.getBoolean(ctx, what, defValue);
    }

    public static String getString(Context ctx, String what, String defValue) {
        return PrefUtils.getString(ctx, what, defValue);
    }

    public static int getInt(Context ctx, String what, int defVaule) {
        return PrefUtils.getInt(ctx, what, defVaule);
    }

    public static void removeAll(Context ctx) {
        PrefUtils.clear(ctx);
    }

    public static void remove(Context ctx, String ... what) {
        for(String s : what) {
            PrefUtils.remove(ctx, s);
        }
    }

}
