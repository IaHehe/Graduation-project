package com.library.pojo;

/**
 * Created by DELL on 2017/4/2.
 */

public class BasicEntity {

    private String code;
    private String message;


    public BasicEntity() {
    }

    public BasicEntity(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "BasicEntity{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
