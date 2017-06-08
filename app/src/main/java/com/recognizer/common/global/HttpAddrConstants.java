package com.recognizer.common.global;

/**
 * @author Dongjun Zou
 * @Description ${todo}
 * @email 984147586@qq.com
 * @date 2017/5/15 00:24
 */

public class HttpAddrConstants {
    // http://119.29.246.196   http://192.168.155.1:8080
    private static String HTTP_SERVER_URL_API= "http://119.29.246.196/classchecks";
    private static String HTTP_SERVER_URL_MANAGER = "http://119.29.246.196/WorkAttendance";

    /**
     * 注册请求短信验证码
     */
    public static String REQ_REGISTER_SMS_CODE = HTTP_SERVER_URL_API + "/global/sms/register-sms-code";
    /**
     * 登录请求短信验证码
     */
    public static String REQ_LOGIN_SMS_CODE = HTTP_SERVER_URL_API + "/global/sms/login-sms-code";
    /**
     * 学生注册
     */
    public static String USER_REGISTER = HTTP_SERVER_URL_API + "/student/register";
    /**
     * 学生登录
     */
    public static String USER_LOGIN = HTTP_SERVER_URL_API + "/student/login";
    /**
     * 学生考勤
     */
    public static String STUDENT_CLOCK_IN = HTTP_SERVER_URL_API + "/student/clock-in";
    /**
     * 导入课表
     */
    public static String IMPORT_TABLE = HTTP_SERVER_URL_MANAGER + "/course/student/import";
    /**
     * 获取课表，按照教学周获取 http://192.168.155.1:8080/WorkAttendance/course/student/week
     */
    public static String REQ_COURSE_BY_WEEK = HTTP_SERVER_URL_MANAGER + "/course/student/week";

    /**
     * 检查学生是否可以考勤
     */
    public static String STUDENT_ABLE_CLOCK = HTTP_SERVER_URL_MANAGER + "/attendance/student/needAttendance";
}
