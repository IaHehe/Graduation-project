package com.recognizer.classchecks.course.view;

import android.content.Context;

import com.library.mvp.BaseView;
import com.recognizer.classchecks.course.model.bean.CourseBean;
import com.recognizer.common.widget.syllabus.model.SimpleSection;

import java.util.List;

/**
 * @author Dongjun Zou
 * @Description ${todo}
 * @email 984147586@qq.com
 * @date 2017/5/23 00:18
 */

public interface CourseView extends BaseView {

    int NOT_HAVE_JW_ACCOUNT = 1;

    void showLoading();
    void hideLoading();
    void showMsg(String msg);

    /**
     * 初始化页面顶部，即显示年份和选择教学周的下拉列表
     * @param body
     */
    void initPageTop(CourseBean.ApiBody body);
    void setData(CourseBean courseBean);
    /**
     * 获取用户选择的教学周
     */
    int getTeachingWeek();
    String getJWAccount();

    void showResult(int what, String msg);


}
