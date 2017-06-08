package com.recognizer.classchecks.report.view;

import com.library.mvp.BaseView;

/**
 * @author Dongjun Zou
 * @Description ${todo}
 * @email 984147586@qq.com
 * @date 2017/6/1 20:22
 */

public interface GenerateReportView extends BaseView {

    int EXPORT_SUCCESS =1;
    int EXPORT_FAILED =2;
    int SHOW_MSG = 3;

    void showLoadding();
    void hideLoadding();

    String getJWAccount();
    String getEmail();
    void showResult(int what, String msg);
}
