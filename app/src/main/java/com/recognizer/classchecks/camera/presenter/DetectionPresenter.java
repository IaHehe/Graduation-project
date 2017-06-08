package com.recognizer.classchecks.camera.presenter;

import com.library.mvp.BasePresenter;
import com.library.pojo.BasicEntityData;
import com.library.utils.LogUtil;
import com.library.utils.StringUtils;
import com.recognizer.classchecks.camera.model.Callback;
import com.recognizer.classchecks.camera.model.DetectionModel;
import com.recognizer.classchecks.camera.view.DetectionView;

/**
 * @author Dongjun Zou
 * @Description ${todo}
 * @email 984147586@qq.com
 * @date 2017/6/1 02:34
 */

public class DetectionPresenter extends BasePresenter<DetectionView> {

    private DetectionModel mDetectionModel;

    public DetectionPresenter(DetectionModel model) {
        mDetectionModel = model;
    }

    public void clockin() {
        byte[] captureByte = getView().getCaptureByte();
        if((captureByte.length == 0)) {
            getView().showResult(DetectionView.SHOW_MSG, "没有捕获到人脸信息");
            return;
        }
        String jwAccount = getView().getJWAccount();
        if(StringUtils.isEmpty(jwAccount)) {
            getView().showResult(DetectionView.SHOW_MSG, "没有获取到学号");
            return;
        }
        String loginAccount = getView().getLoginAccount();
        if(StringUtils.isEmpty(loginAccount)) {
            getView().showResult(DetectionView.SHOW_MSG, "没有获取到登录信息");
        }
        double lng = getView().getLng();
        double lat = getView().getLat();
        if(lng == 0.0 || lat == 0.0) {
            getView().showResult(DetectionView.SHOW_MSG, "没有定位信息");
        }
        getView().showLoading();
        mDetectionModel.clockin(jwAccount, loginAccount, lng, lat, captureByte, new Callback() {
            @Override
            public void onSuccess(Object obj) {
                getView().hideLoading();
                LogUtil.i("onSuccess");
                BasicEntityData bedata = (BasicEntityData) obj;
                if("5000".equals(bedata.getCode())) { // 5000 表示学生考勤成功
                    getView().showResult(DetectionView.CLOCK_IN_SUCCESS, bedata.getData());
                } else if("5002".equals(bedata.getCode())) { // 没有检测到人脸，请重试
                    getView().showResult(DetectionView.CLOCK_IN_FAILED, bedata.getMessage());
                } else if("5003".equals(bedata.getCode())) { // 上传的图片保存失败
                    getView().showResult(DetectionView.CLOCK_IN_FAILED, bedata.getMessage());
                } else if("5004".equals(bedata.getCode())) { // 上传的图片为空
                    getView().showResult(DetectionView.CLOCK_IN_FAILED, bedata.getMessage());
                } else {
                    getView().showResult(DetectionView.SHOW_MSG, bedata.getMessage());
                }
            }

            @Override
            public void onFailed(Object obj) {
                getView().hideLoading();
                BasicEntityData basicEntityData = (BasicEntityData) obj;
                getView().showResult(DetectionView.SHOW_MSG, basicEntityData.getMessage());
            }
        });

    }

}
