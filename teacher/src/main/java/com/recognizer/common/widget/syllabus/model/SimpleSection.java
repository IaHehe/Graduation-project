package com.recognizer.common.widget.syllabus.model;

/**
 * @author Dongjun Zou
 * @Description ${todo}
 * @email 984147586@qq.com
 * @date 2017/5/7 01:55
 */

public class SimpleSection {

    int id;          // 课程ID
    int day;        // 周几
    String content; // 课程名称
    String teacher; // 任课教师
    String classRoom; // 教室
    int startSection;// 开始节
    int endSection;// 结束节
    int weekStart;// 开始周次
    int weekEnd;// 结束周次

    public SimpleSection() {
    }

    public SimpleSection(int id, int day, String content, String teacher, String classRoom,
                   int startSection, int endSection) {
        this.id = id;
        this.day = day;
        this.content = content;
        this.teacher = teacher;
        this.classRoom = classRoom;
        this.startSection = startSection;
        this.endSection = endSection;
    }

    public SimpleSection(int id, int day, String content, String teacher, String classRoom,
                   int startSection, int endSection, int weekStart, int weekEnd) {
        this.id = id;
        this.day = day;
        this.content = content;
        this.teacher = teacher;
        this.classRoom = classRoom;
        this.startSection = startSection;
        this.endSection = endSection;
        this.weekStart = weekStart;
        this.weekEnd = weekEnd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }

    public int getStartSection() {
        return startSection;
    }

    public void setStartSection(int startSection) {
        this.startSection = startSection;
    }

    public int getEndSection() {
        return endSection;
    }

    public void setEndSection(int endSection) {
        this.endSection = endSection;
    }

    public int getWeekStart() {
        return weekStart;
    }

    public void setWeekStart(int weekStart) {
        this.weekStart = weekStart;
    }

    public int getWeekEnd() {
        return weekEnd;
    }

    public void setWeekEnd(int weekEnd) {
        this.weekEnd = weekEnd;
    }

    @Override
    public String toString() {
        return "SimpleSection{" +
                "id=" + id +
                ", day=" + day +
                ", content='" + content + '\'' +
                ", teacher='" + teacher + '\'' +
                ", classRoom='" + classRoom + '\'' +
                ", startSection=" + startSection +
                ", endSection=" + endSection +
                ", weekStart=" + weekStart +
                ", weekEnd=" + weekEnd +
                '}';
    }

}
