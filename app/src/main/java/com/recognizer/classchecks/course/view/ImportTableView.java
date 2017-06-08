package com.recognizer.classchecks.course.view;

import com.library.mvp.BaseView;

/**
 * @author Dongjun Zou
 * @Description ${todo}
 * @email 984147586@qq.com
 * @date 2017/5/22 17:06
 */

public interface ImportTableView extends BaseView {

    void showLoading();
    void hideLoading();
    // 教务网学号
    String getJWAccount();
    // 教务网登陆密码
    String getJWPwd();
    void showMsg(String msg);
    String getLoginAccount();
    /**
     * 当课表导入成功后做什么？
     */
    void skip();
}
