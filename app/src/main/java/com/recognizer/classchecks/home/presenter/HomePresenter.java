package com.recognizer.classchecks.home.presenter;

import com.library.mvp.BasePresenter;
import com.library.pojo.BasicEntity;
import com.library.utils.StringUtils;
import com.recognizer.classchecks.home.model.HomeModel;
import com.recognizer.classchecks.home.model.bean.CheckCourseBean;
import com.recognizer.classchecks.home.view.HomeView;
import com.recognizer.common.global.HttpRequestCode;

/**
 * @author Dongjun Zou
 * @Description ${todo}
 * @email 984147586@qq.com
 * @date 2017/6/1 01:52
 */

public class HomePresenter extends BasePresenter<HomeView> {
    private HomeModel mHomeModel;

    public HomePresenter(HomeModel model) {
        this.mHomeModel = model;
    }

    public void checkHasCourse() {
        String jwAccount = getView().getJWAccount();

        if(StringUtils.isEmpty(jwAccount)) {
            getView().showResult(HomeView.NOT_IMPORT_COURSE, "您还没有导入教务课表,去导入?");
            return;
        }

        mHomeModel.checkHasCourse(jwAccount, new HomeModel.Callback() {
            @Override
            public void onSuccess(Object obj) {
                CheckCourseBean ccb = (CheckCourseBean) obj;
                if("0".equals(ccb.getApi_code())) { // 
                    if(ccb.getApi_body() == true) { // 有课，可以考勤
                        getView().showResult(HomeView.CAN_OK_CLOCKIN, ccb.getApi_message());
                    } else { // 服务器返回当前没课
                        getView().showResult(HomeView.CURR_NO_COURSE, "你当前没有要上的课，不能考勤");
                    }
                } else if("-1".equals(ccb.getApi_code())) {
                    getView().showResult(HomeView.SHOW_MSG, HttpRequestCode.SERVER_EXCEPTION[1]);
                }
            }

            @Override
            public void onFailed(Object obj) {
                BasicEntity basicEntity = (BasicEntity) obj;
                if(HttpRequestCode.REQUEST_OVERTIME[0].equals(basicEntity.getCode())) {
                    getView().showResult(HomeView.SHOW_MSG, basicEntity.getMessage());
                } else {
                    getView().showResult(HomeView.SHOW_MSG, basicEntity.getMessage());
                }
            }
        });
    }

}
