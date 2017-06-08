package com.recognizer.classchecks.clockin.view;

import com.library.mvp.BaseView;
import com.recognizer.classchecks.clockin.model.bean.ClockInResultBean;

import java.util.List;

/**
 * @author Dongjun Zou
 * @Description ${todo}
 * @email 984147586@qq.com
 * @date 2017/5/24 21:19
 */

public interface PhotographView extends BaseView {
    void showLoading();
    void hideLoading();

    String getImagePath();
    double getLatitude();
    double getLongitude();
    String getLoginAccount();
    String getJWAccount();
    void showMsg(String msg);
    void hanldResult(List<ClockInResultBean> clockinResults);

    int getByWhat();

}
