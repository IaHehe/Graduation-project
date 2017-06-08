package com.recognizer.classchecks.course.model.bean;

/**
 * @author Dongjun Zou
 * @Description ${todo}
 * @email 984147586@qq.com
 * @date 2017/5/22 23:13
 */

public class ImportBean {
    private String api_code;
    private String api_message;

    public ImportBean() {
    }

    public ImportBean(String api_code, String api_message) {
        this.api_code = api_code;
        this.api_message = api_message;
    }

    public String getApi_code() {
        return api_code;
    }

    public void setApi_code(String api_code) {
        this.api_code = api_code;
    }

    public String getApi_message() {
        return api_message;
    }

    public void setApi_message(String api_message) {
        this.api_message = api_message;
    }
}
