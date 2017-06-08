package com.library.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * @author Dongjun Zou
 * @Description ${todo}
 * @email 984147586@qq.com
 * @date 2017/5/5 00:11
 */

public class CalendarUtils {

    public final static String DATE_FORMAT_HMS = "HH:mm:ss";
    public final static String DATE_FORMAT_YMD = "yyyy-MM-dd";
    public final static String DATE_FORMAT_YMD_HMS = "yyyy-MM-dd HH:mm:ss";
    public final static int WEEK_BY_INT = 1;
    public final static int WEEK_BY_STRING = 2;

    private static String [] WEEKS = {"日", "一", "二", "三", "四", "五", "六"};

    private static Calendar mCalendar;

    private static void init() {
        mCalendar = Calendar.getInstance();
        mCalendar.setTimeZone(TimeZone.getDefault());
    }

    public static int getCurrentYear() {
        init();
        return mCalendar.get(Calendar.YEAR);
    }

    public static int getCurrentMonth() {
        init();
        return mCalendar.get(Calendar.MONTH)+1;
    }

    public static int getCurrentDay() {
        init();
        return mCalendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取当前时间
     * @return
     */
    public static String getCurrentTime(String format) {
        init();

        SimpleDateFormat fmt = new SimpleDateFormat(format);

        return fmt.format(mCalendar.getTime());
    }

    public static int getCurrentWeek(int byInt) {
        init();
        int mWeek = mCalendar.get(Calendar.DAY_OF_WEEK);
        return mWeek;
    }

    public static String getCurrentWeek() {
        init();
        int mWeek = mCalendar.get(Calendar.DAY_OF_WEEK);

        return WEEKS[mWeek - 1];
    }

    public static String getCurrentDateAndWeek() {
        return getCurrentTime(DATE_FORMAT_YMD+ " 星期" + getCurrentWeek());
    }
}
