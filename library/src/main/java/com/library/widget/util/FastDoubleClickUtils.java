package com.library.widget.util;

/**
 * @author Dongjun Zou
 * @Description Android之有效防止按钮多次重复点击
 * @email 984147586@qq.com
 * @date 2017/4/28 22:23
 */
public class FastDoubleClickUtils {
    private static long lastClickTime = 0;
    private static long DIFF = 1000;
    private static int lastButtonId = -1;
    public static boolean isFastDoubleClick() {
        return isFastDoubleClick(-1, DIFF);
    }


    public static boolean isFastDoubleClick(int buttonId) {
        return isFastDoubleClick(buttonId, DIFF);
    }


    public static boolean isFastDoubleClick(int buttonId, long diff) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (lastButtonId == buttonId && lastClickTime > 0 && timeD < diff) {
            return true;
        }
        lastClickTime = time;
        lastButtonId = buttonId;
        return false;
    }
}
