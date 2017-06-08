package com.recognizer.classchecks.course.presenter;

import android.content.Context;

import com.library.mvp.BasePresenter;
import com.library.pojo.BasicEntity;
import com.library.utils.NetworkUtils;
import com.library.utils.StringUtils;
import com.recognizer.classchecks.course.model.Callback;
import com.recognizer.classchecks.course.model.CourseModel;
import com.recognizer.classchecks.course.model.bean.CourseBean;
import com.recognizer.classchecks.course.view.CourseView;

/**
 * @author Dongjun Zou
 * @Description ${todo}
 * @email 984147586@qq.com
 * @date 2017/5/23 00:17
 */

public class CoursePresenter extends BasePresenter<CourseView> {

    private CourseModel model;

    public CoursePresenter(CourseModel model) {
        this.model = model;
    }

    public void obtainCourse() {

        final int week = getView().getTeachingWeek();
        String jwAccount = getView().getJWAccount();
        if(StringUtils.isEmpty(jwAccount)) {
            getView().showResult(CourseView.NOT_HAVE_JW_ACCOUNT, "您还没有导入教务课表，请先导入");
            return;
        }
        getView().showLoading();
        model.obtainCourse(jwAccount, week == -1 ? "" : String.valueOf(week) , new Callback() {
            @Override
            public void onSuccess(Object object) {
                getView().hideLoading();
                CourseBean courseBean = (CourseBean) object;
                if("0".equals(courseBean.getApi_code())) {  // 请求成功
                    if(week == -1) { // 初始化页面顶部下拉列表
                        getView().initPageTop(courseBean.getApi_body());
                    }
                    getView().showMsg(courseBean.getApi_body().getCourseData().size() == 0 ? "这周没课(⊙o⊙)哦" : "已加载课表");
                    getView().setData(courseBean);
                } else {
                    getView().showMsg(courseBean.getApi_message());
                }
            }

            @Override
            public void onFailed(Object object) {
                getView().hideLoading();

                BasicEntity basicEntity = (BasicEntity) object;
                getView().showMsg(basicEntity.getMessage());
            }
        });
    }
}
