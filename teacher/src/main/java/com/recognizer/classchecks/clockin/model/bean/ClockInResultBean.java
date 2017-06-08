package com.recognizer.classchecks.clockin.model.bean;

/**
 * @author Dongjun Zou
 * @Description ${todo}
 * @email 984147586@qq.com
 * @date 2017/5/31 22:45
 */

public class ClockInResultBean {

    private Integer id;
    private String studentName;
    private String stuJWAccount;
    private String loginAccount;
    private Integer stuFaceLabel;
    private String regID; // 极光推送唯一标识
    private Integer clockinType; // 记录考勤类型 1 表示正常、2表示缺勤、3表示其他
    private String stuClass; // 行政班级

    public ClockInResultBean() {

    }

    public ClockInResultBean(Integer id, String studentName, String stuJWAccount, String loginAccount, Integer stuFaceLabel, String regID, Integer clockinType, String stuClass) {
        this.id = id;
        this.studentName = studentName;
        this.stuJWAccount = stuJWAccount;
        this.loginAccount = loginAccount;
        this.stuFaceLabel = stuFaceLabel;
        this.regID = regID;
        this.clockinType = clockinType;
        this.stuClass = stuClass;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStuClass() {
        return stuClass;
    }

    public void setStuClass(String stuClass) {
        this.stuClass = stuClass;
    }

    public String getStuJWAccount() {
        return stuJWAccount;
    }

    public void setStuJWAccount(String stuJWAccount) {
        this.stuJWAccount = stuJWAccount;
    }

    public Integer getStuFaceLabel() {
        return stuFaceLabel;
    }

    public void setStuFaceLabel(Integer stuFaceLabel) {
        this.stuFaceLabel = stuFaceLabel;
    }

    public String getRegID() {
        return regID;
    }

    public void setRegID(String regID) {
        this.regID = regID;
    }

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public Integer getClockinType() {
        return clockinType;
    }

    public void setClockinType(Integer clockinType) {
        this.clockinType = clockinType;
    }

    @Override
    public String toString() {
        return "ClockInResultBean{" +
                "id=" + id +
                ", studentName='" + studentName + '\'' +
                ", stuJWAccount='" + stuJWAccount + '\'' +
                ", loginAccount='" + loginAccount + '\'' +
                ", stuFaceLabel=" + stuFaceLabel +
                ", regID='" + regID + '\'' +
                ", clockinType=" + clockinType +
                ", stuClass='" + stuClass + '\'' +
                '}';
    }
}
