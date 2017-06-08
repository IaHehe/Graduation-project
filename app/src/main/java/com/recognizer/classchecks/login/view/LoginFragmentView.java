package com.recognizer.classchecks.login.view;

import com.library.mvp.BaseView;
import com.recognizer.classchecks.login.model.bean.StudentBean;

/**
 * @author Dongjun Zou
 * @Description ${todo}
 * @email 984147586@qq.com
 * @date 2017/5/13 15:51
 */

public interface LoginFragmentView extends BaseView{

    public final static int SMS_CODE_SUCESS = 1;
    public final static int SMS_CODE_FAILED = 2;
    public final static int LOGIN_SUCESS = 3;
    public final static int LOGIN_ERROR = 4;
    public final static int LOGIN_FAILED = 5;

    void showLoading();
    void hideLoading();
    String getPhone();
    String getSMSCode();
    void infoCache(StudentBean studentBean);
    /**
     * 获取是否自动登录
     * @return
     */
    boolean getIsAutoLogin();
    void showResult(int what, String message);
    void skip();

    String getRegId();

}
