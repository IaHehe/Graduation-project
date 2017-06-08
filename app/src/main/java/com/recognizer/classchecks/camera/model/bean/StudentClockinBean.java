package com.recognizer.classchecks.camera.model.bean;

/**
 * @author Dongjun Zou
 * @Description ${todo}
 * @email 984147586@qq.com
 * @date 2017/6/1 07:04
 */

public class StudentClockinBean {
    private String stuName;
    private String curTime;

    public StudentClockinBean() {

    }

    public StudentClockinBean(String stuName, String curTime) {
        super();
        this.stuName = stuName;
        this.curTime = curTime;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getCurTime() {
        return curTime;
    }

    public void setCurTime(String curTime) {
        this.curTime = curTime;
    }


    @Override
    public String toString() {
        return "StudentClockinBean{" +
                "stuName='" + stuName + '\'' +
                ", curTime='" + curTime + '\'' +
                '}';
    }
}
