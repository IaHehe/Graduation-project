package com.recognizer.classchecks.clockin.presenter;

import com.library.mvp.BasePresenter;
import com.library.pojo.BasicEntityList;
import com.library.utils.StringUtils;
import com.recognizer.classchecks.clockin.model.Callback;
import com.recognizer.classchecks.clockin.model.PhotographModel;
import com.recognizer.classchecks.clockin.model.bean.ClockInResultBean;
import com.recognizer.classchecks.clockin.view.PhotographView;

/**
 * @author Dongjun Zou
 * @Description ${todo}
 * @email 984147586@qq.com
 * @date 2017/5/24 21:21
 */

public class PhotographPresenter extends BasePresenter<PhotographView> {

    private PhotographModel model;

    public PhotographPresenter(PhotographModel model) {
        this.model = model;
    }

    // 上传图片考勤
    public void uploadAsClockIn() {

        String jwAccount = getView().getJWAccount();
        if(StringUtils.isEmpty(jwAccount)) {
            getView().showMsg("没有获取到教务账号");
            return;
        }
        String loginAccount = getView().getLoginAccount();
        if(StringUtils.isEmpty(loginAccount)) {
            getView().showMsg("没有获取到登录账号");
            return;
        }
        double lng = getView().getLongitude();
        double lat = getView().getLatitude();
        if(lng <= 0.0 || lat <= 0.0) {
            getView().showMsg("没有获取到定位信息");
            return;
        }
        String mImagePath = getView().getImagePath();
        if(StringUtils.isEmpty(mImagePath)) {
            getView().showMsg("图片地址错误");
            return;
        }
        int byWhat = getView().getByWhat();
        getView().showLoading();
        model.uploadImageAsClockIn(jwAccount, loginAccount, lng, lat, mImagePath, byWhat, new Callback() {
            @Override
            public void onSuccess(Object object) {
                getView().hideLoading();
                BasicEntityList<ClockInResultBean> beList = (BasicEntityList<ClockInResultBean>) object;
                if("4000".equals(beList.getCode())) {
                    getView().hanldResult(beList.getDataList());
                } else {
                    getView().showMsg(beList.getMessage());
                }
            }

            @Override
            public void onFailed(Object object) {
                getView().hideLoading();
                BasicEntityList basicEntityList = (BasicEntityList) object;
                getView().showMsg(basicEntityList.getMessage());
            }
        });
    }
}
