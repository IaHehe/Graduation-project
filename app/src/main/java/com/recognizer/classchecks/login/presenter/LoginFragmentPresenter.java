package com.recognizer.classchecks.login.presenter;

import android.os.Handler;

import com.library.mvp.BasePresenter;
import com.library.pojo.BasicEntity;
import com.library.pojo.BasicEntityData;
import com.library.utils.LogUtil;
import com.library.utils.StringUtils;
import com.recognizer.classchecks.login.model.LoginFragmentModel;
import com.recognizer.classchecks.login.model.bean.StudentBean;
import com.recognizer.classchecks.login.view.LoginFragmentView;

/**
 * @author Dongjun Zou
 * @Description ${todo}
 * @email 984147586@qq.com
 * @date 2017/5/13 15:54
 */

public class LoginFragmentPresenter extends BasePresenter<LoginFragmentView>{

    private LoginFragmentModel mLoginModel;
    private LoginFragmentView mLoginView;
    private Handler mHandler = new Handler();

    public LoginFragmentPresenter(LoginFragmentModel loginModel) {
        this.mLoginModel = loginModel;
    }

    private void initRegisterView() {
        if(null == mLoginView) {
            checkViewAttached();
            mLoginView = getView();
        }
    }

    public void login() {
        initRegisterView();
        String phone = mLoginView.getPhone();
        if(StringUtils.isEmpty(phone)) {
            mLoginView.showResult(LoginFragmentView.LOGIN_ERROR, "手机号码不能为空");
            return;
        }
        if(!StringUtils.isMobile(phone)) {
            mLoginView.showResult(LoginFragmentView.LOGIN_ERROR, "请输入正确的手机号码");
            return;
        }
        String smscode = mLoginView.getSMSCode();
        if(StringUtils.isEmpty(smscode)) {
            mLoginView.showResult(LoginFragmentView.LOGIN_ERROR, "验证码不能为空");
            return;
        }
        String regId = getView().getRegId();
        if(StringUtils.isEmpty(regId)) {

        }
        LogUtil.i("regId=" + regId);
        mLoginView.showLoading();
        mLoginModel.login(mLoginView.getPhone(), smscode, getView().getIsAutoLogin(), regId, new LoginFragmentModel.Callback() {
            @Override
            public void onSuccess(Object obj) {
                mLoginView.hideLoading();
                BasicEntityData<StudentBean> basicEntityData = (BasicEntityData) obj;
                // 服务器返回2000:登录成功 2001:数据库异常  2002:验证码错误  2003:手机号不存在
                if("2000".equals(basicEntityData.getCode())) {
                    mLoginView.showResult(LoginFragmentView.LOGIN_SUCESS, basicEntityData.getMessage());
                    mLoginView.infoCache(basicEntityData.getData());
                } else {
                    mLoginView.showResult(LoginFragmentView.LOGIN_ERROR, basicEntityData.getMessage());
                }
            }

            @Override
            public void onFailed(Object obj) {
                mLoginView.hideLoading();
                BasicEntityData basicEntityData = (BasicEntityData) obj;
                mLoginView.showResult(LoginFragmentView.LOGIN_ERROR, basicEntityData.getMessage());
            }
        });
    }

    public void reqSMSCode() {
        initRegisterView();
        String phone = mLoginView.getPhone();
        if(StringUtils.isEmpty(phone)) {
            mLoginView.showResult(LoginFragmentView.SMS_CODE_FAILED, "手机号码不能为空");
            return;
        }
        if(!StringUtils.isMobile(phone)) {
            mLoginView.showResult(LoginFragmentView.SMS_CODE_FAILED, "请输入正确的手机号码");
            return;
        }
        mLoginModel.reqSMSCode(phone, new LoginFragmentModel.Callback() {

            @Override
            public void onSuccess(Object obj) {
                // 3000：验证码获取成功 3001：请求验证间隔时间太短 3002：获取验证错误 3003：手机号已注册
                BasicEntity be = (BasicEntity) obj;
                if("3000".equals(be.getCode())) {
                    mLoginView.showResult(LoginFragmentView.SMS_CODE_SUCESS, be.getMessage());
                } else {
                    mLoginView.showResult(LoginFragmentView.SMS_CODE_FAILED, be.getMessage());
                }
            }

            @Override
            public void onFailed(Object obj) {
                BasicEntity b = (BasicEntity) obj;
                mLoginView.showResult(LoginFragmentView.SMS_CODE_FAILED, b.getMessage());
            }
        });
    }

}
