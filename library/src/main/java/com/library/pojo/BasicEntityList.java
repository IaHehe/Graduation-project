package com.library.pojo;

import java.util.List;

/**
 * @author Dongjun Zou
 * @Description ${todo}
 * @email 984147586@qq.com
 * @date 2017/5/31 22:41
 */

public class BasicEntityList<T> {
    private String code;
    private String message;
    private List<T> dataList;

    public BasicEntityList() {
    }

    public BasicEntityList(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public BasicEntityList(String code, String message, List<T> dataList) {
        this.code = code;
        this.message = message;
        this.dataList = dataList;
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

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

}
