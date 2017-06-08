package com.recognizer.classchecks.camera.view;

import com.library.mvp.BaseView;

/**
 * @author Dongjun Zou
 * @Description ${todo}
 * @email 984147586@qq.com
 * @date 2017/6/1 02:33
 */

public interface DetectionView extends BaseView {

    int CLOCK_IN_SUCCESS = 1;
    int SHOW_MSG = 2;
    int CLOCK_IN_FAILED = 3;

    void showLoading();
    void hideLoading();

    byte[] getCaptureByte();
    String getJWAccount();
    String getLoginAccount();
    double getLng();
    double getLat();
    void showResult(int what, String msg);
    void showResult(int what, Object obj);
}
