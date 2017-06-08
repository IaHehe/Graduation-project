package com.library.pojo;

/**
 * @author Dongjun Zou
 * @Description ${todo}
 * @email 984147586@qq.com
 * @date 2017/5/29 04:57
 */

public class BasicEntityData<T> {
    private String code;
    private String message;
    private T data = null;

    public BasicEntityData() {
        super();
    }

    public BasicEntityData(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public BasicEntityData(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
