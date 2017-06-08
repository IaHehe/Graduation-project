package com.recognizer.classchecks.course.model.bean;

import com.recognizer.common.widget.syllabus.model.SimpleSection;

import java.util.List;

/**
 * @author Dongjun Zou
 * @Description ${todo}
 * @email 984147586@qq.com
 * @date 2017/5/23 01:21
 */

public class CourseBean {

    private String api_code;
    private ApiBody api_body;
    private String api_message;

    public CourseBean() {
    }

    public CourseBean(String api_code, ApiBody api_body, String api_message) {
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

    public ApiBody getApi_body() {
        return api_body;
    }

    public void setApi_body(ApiBody api_body) {
        this.api_body = api_body;
    }

    public String getApi_message() {
        return api_message;
    }

    public void setApi_message(String api_message) {
        this.api_message = api_message;
    }


    public class ApiBody {
        private String year;
        private String semester;
        private List<SimpleSection> courseData;
        private String curWeek;

        public ApiBody() {
        }

        public ApiBody(String year, String semester, List<SimpleSection> courseData, String curWeek) {
            this.year = year;
            this.semester = semester;
            this.courseData = courseData;
            this.curWeek = curWeek;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getSemester() {
            return semester;
        }

        public void setSemester(String semester) {
            this.semester = semester;
        }

        public List<SimpleSection> getCourseData() {
            return courseData;
        }

        public void setCourseData(List<SimpleSection> courseData) {
            this.courseData = courseData;
        }

        public String getCurWeek() {
            return curWeek;
        }

        public void setCurWeek(String curWeek) {
            this.curWeek = curWeek;
        }
    }
}
