package com.recognizer.classchecks.login.view;

import com.library.mvp.BaseView;

import java.io.File;

/**
 * @author Dongjun Zou
 * @Description ${todo}
 * @email 984147586@qq.com
 * @date 2017/5/13 16:41
 */

public interface RegisterFragmentView extends BaseView {

    void showLoading();
    void hideLoading();
    String getPhone();
    String getSMSCode();
    void showMsg(String msg);
    void showResult(boolean loginResult, String msg);
    void reqSMSStatus(boolean isSuccess, String msg);
    String[] getFaceCollect();
    // 获取极光推送的设备/应用唯一标识
    String getRegistrationID();

}
