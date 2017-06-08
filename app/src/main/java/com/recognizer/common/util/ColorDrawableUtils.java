package com.recognizer.common.util;

import com.recognizer.R;

/**
 * @author Dongjun Zou
 * @Description 用于课表表格的背景图片
 * @email 984147586@qq.com
 * @date 2017/5/7 01:58
 */

public class ColorDrawableUtils {
    private static int[] bgIds = {R.drawable.ic_course_bg_bohelv, R.drawable.ic_course_bg_lan,
            R.drawable.ic_course_bg_tao, R.drawable.ic_course_bg_zi,
            R.drawable.ic_course_bg_tuhuang, R.drawable.ic_course_bg_bohelv,
            R.drawable.ic_course_deep_orange, R.drawable.ic_course_bg_ponceau
    };

    public static int getColorsLength() {
        return bgIds.length;
    }

    public static int getDrawableBgColor(int index) {
        return bgIds[index];
    }

}
