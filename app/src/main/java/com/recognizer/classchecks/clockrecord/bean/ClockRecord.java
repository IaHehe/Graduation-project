package com.recognizer.classchecks.clockrecord.bean;

/**
 * @author Dongjun Zou
 * @Description ${todo}
 * @email 984147586@qq.com
 * @date 2017/5/8 14:34
 */

public class ClockRecord {

    private int id;
    private String courseName;
    private String teacherName;
    private String classRoom;
    private String clockSituation; // 考勤情况（正常、缺勤）
    private String week;
    private String dayOnWeek;
    private String date;

    public ClockRecord() {
    }

    public ClockRecord(int id, String courseName, String teacherName, String classRoom, String clockSituation, String week, String dayOnWeek, String date) {
        this.id = id;
        this.courseName = courseName;
        this.teacherName = teacherName;
        this.classRoom = classRoom;
        this.clockSituation = clockSituation;
        this.week = week;
        this.dayOnWeek = dayOnWeek;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }

    public String getClockSituation() {
        return clockSituation;
    }

    public void setClockSituation(String clockSituation) {
        this.clockSituation = clockSituation;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getDayOnWeek() {
        return dayOnWeek;
    }

    public void setDayOnWeek(String dayOnWeek) {
        this.dayOnWeek = dayOnWeek;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "ClockRecord{" +
                "id=" + id +
                ", courseName='" + courseName + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", classRoom='" + classRoom + '\'' +
                ", clockSituation='" + clockSituation + '\'' +
                ", week='" + week + '\'' +
                ", dayOnWeek='" + dayOnWeek + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
