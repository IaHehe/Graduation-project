package com.recognizer.classchecks.login.presenter;

import com.library.mvp.BasePresenter;
import com.library.pojo.BasicEntity;
import com.library.utils.StringUtils;
import com.recognizer.classchecks.login.model.RegisterFragmentModel;
import com.recognizer.classchecks.login.view.RegisterFragmentView;
import com.recognizer.common.global.HttpRequestCode;

/**
 * @author Dongjun Zou
 * @Description ${todo}
 * @email 984147586@qq.com
 * @date 2017/5/13 16:43
 */

public class RegisterFragmentPresenter extends BasePresenter<RegisterFragmentView> {
    private RegisterFragmentModel mRegisterModel;
    private RegisterFragmentView mRegisterView;


    public RegisterFragmentPresenter(RegisterFragmentModel registerModel) {
        this.mRegisterModel = registerModel;
    }

    private void initRegisterView() {
        if(null == mRegisterView) {
            checkViewAttached();
            mRegisterView = getView();
        }
    }

    public void register() {
        initRegisterView();
        String phone = mRegisterView.getPhone();
        if(StringUtils.isEmpty(phone)) {
            mRegisterView.showResult(false, "手机号码不能为空");
            return;
        }
        if(!StringUtils.isMobile(phone)) {
            mRegisterView.showResult(false, "请输入正确的手机号码");
            return;
        }
        String smscode = getView().getSMSCode();
        if(StringUtils.isEmpty(smscode)) {
            mRegisterView.showResult(false, "验证码不能为空");
            return;
        }
        String regID = getView().getRegistrationID();
        if(StringUtils.isEmpty(regID)) {
            mRegisterView.showResult(false, "获取RegID失败");
            return;
        }
        mRegisterView.showLoading();
        mRegisterModel.register(phone, mRegisterView.getSMSCode(), regID, mRegisterView.getFaceCollect(), new RegisterFragmentModel.Callback() {
            @Override
            public void onSuccess(BasicEntity be) {
                mRegisterView.hideLoading();
                // "1000"由服务器返回，表示注册成功
                if("1000".equals(be.getCode())) {
                    mRegisterView.showResult(true, be.getMessage());
                } else { // 其他返回码，暂时没有处理，直接显示
                    mRegisterView.showResult(false, be.getMessage());
                }
            }

            @Override
            public void onFailed(BasicEntity be) {
                mRegisterView.hideLoading();
                getView().showMsg(be.getMessage());
            }
        });
    }

    public void reqSMSCode() {
        initRegisterView();
        String phone = mRegisterView.getPhone();
        if(StringUtils.isEmpty(phone)) {
            mRegisterView.reqSMSStatus(false, "输入手机号码");
            return;
        }
        if(!StringUtils.isMobile(phone)) {
            mRegisterView.reqSMSStatus(false, "请输入正确的手机号码");
            return;
        }
        mRegisterModel.reqSMSCode(phone, new RegisterFragmentModel.Callback() {

            @Override
            public void onSuccess(BasicEntity be) {
                // 3000：验证码获取成功 3001：请求验证间隔时间太短 3002：获取验证错误 3003：手机号已注册
                if("3000".equals(be.getCode())) {
                    mRegisterView.reqSMSStatus(true, be.getMessage());
                } else {
                    mRegisterView.reqSMSStatus(false, be.getMessage());
                }
            }

            @Override
            public void onFailed(BasicEntity be) {
                mRegisterView.showMsg(be.getMessage());
            }
        });
    }

}
