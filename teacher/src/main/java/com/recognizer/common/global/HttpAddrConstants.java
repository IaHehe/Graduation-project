package com.recognizer.common.global;

/**
 * @author Dongjun Zou
 * @Description ${todo}
 * @email 984147586@qq.com
 * @date 2017/5/15 00:24
 */

public class HttpAddrConstants {
    //  192.168.155.1:8080
    private static String HTTP_SERVER_URL_API= "http://119.29.246.196/classchecks";
    private static String HTTP_SERVER_URL_MANAGER = "http://119.29.246.196//WorkAttendance";

    /**
     * 请求注册短信验证码
     */
    public static String REQ_REGISTER_SMS_CODE = HTTP_SERVER_URL_API + "/global/sms/register-sms-code";
    /**
     * 登录请求短信验证码
     */
    public static String REQ_LOGIN_SMS_CODE = HTTP_SERVER_URL_API + "/global/sms/login-sms-code";

    /**
     * 教师注册
     */
    public static String TEACHER_REGISTER = HTTP_SERVER_URL_API + "/teacher/register";
    /**
     * 教师登录
     */
    public static String TEACHER_LOGIN = HTTP_SERVER_URL_API + "/teacher/login";

    /**
     * 导入课表
     */
    public static String TEACHER_IMPORT_TABLE = HTTP_SERVER_URL_MANAGER + "/course/teacher/import";
    /**
     * 获取课表，按照教学周获取 http://192.168.155.1:8080/WorkAttendance/course/student/week
     */
    public static String REQ_COURSE_BY_WEEK = HTTP_SERVER_URL_MANAGER + "/course/teacher/week";

    /**
     * 检查教师是否有课或是否可以考勤
     */
    public static String TEACHER_HAS_COURSE = HTTP_SERVER_URL_MANAGER + "/attendance/teacher/hascourse";

    /**
     * 教师拍照考勤
     */
    public static String TEACHER_CLOCK_IN = HTTP_SERVER_URL_API + "/teacher/clock-in";

    public static String EXPORT_SEMESTER_REPORT = HTTP_SERVER_URL_MANAGER + "/statistics/teacher/generate";
}
