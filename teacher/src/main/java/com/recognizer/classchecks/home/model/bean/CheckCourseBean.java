package com.recognizer.classchecks.home.model.bean;

/**
 * @author Dongjun Zou
 * @Description ${todo}
 * @email 984147586@qq.com
 * @date 2017/5/30 21:40
 */

public class CheckCourseBean {

    private String api_code;
    private APIBody api_body;
    private String api_message;

    public CheckCourseBean() {
    }

    public CheckCourseBean(String api_code, APIBody api_body, String api_message) {
        this.api_code = api_code;
        this.api_body = api_body;
        this.api_message = api_message;
    }

    public String getApi_code() {
        return api_code;
    }

    public void setApi_code(String api_code) {
        this.api_code = api_code;
    }

    public APIBody getApi_body() {
        return api_body;
    }

    public void setApi_body(APIBody api_body) {
        this.api_body = api_body;
    }

    public String getApi_message() {
        return api_message;
    }

    public void setApi_message(String api_message) {
        this.api_message = api_message;
    }

    public class APIBody {
        private boolean hasCourse;
        private boolean needAttendance;

        public APIBody() {
        }

        public APIBody(boolean hasCourse, boolean needAttendance) {
            this.hasCourse = hasCourse;
            this.needAttendance = needAttendance;
        }

        public boolean getHasCourse() {
            return hasCourse;
        }

        public void setHasCourse(boolean hasCourse) {
            this.hasCourse = hasCourse;
        }

        public boolean getNeedAttendance() {
            return needAttendance;
        }

        public void setNeedAttendance(boolean needAttendance) {
            this.needAttendance = needAttendance;
        }

        @Override
        public String toString() {
            return "APIBody{" +
                    "hasCourse=" + hasCourse +
                    ", needAttendance=" + needAttendance +
                    '}';
        }
    }

}
