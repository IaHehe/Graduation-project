package com.recognizer.classchecks.login.fragment;


import android.app.Dialog;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.library.basic.BasicFragment;
import com.library.mvp.BaseFragment;
import com.library.utils.EditTextClearTools;
import com.library.utils.LogUtil;
import com.library.utils.StringUtils;
import com.library.widget.LoadingDialogUtils;
import com.library.widget.ToastUtils;
import com.recognizer.R;
import com.recognizer.classchecks.login.LoginActivity;
import com.recognizer.classchecks.login.model.RegisterFragmentModel;
import com.recognizer.classchecks.login.presenter.RegisterFragmentPresenter;
import com.recognizer.classchecks.login.view.RegisterFragmentView;
import com.recognizer.common.util.SMSCodeCountdown;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import cn.jpush.android.api.JPushInterface;


public class RegisterFragment extends BaseFragment<RegisterFragmentView, RegisterFragmentPresenter> implements RegisterFragmentView{
    private LoginActivity mLoginActivity;

    private static final int START_REQ_SMS = 3;
    private static final int REGISTER_SUCCESS = 4;

    private EditText etPhone;
    private EditText etSMSCode;
    private Button btnRegister;
    private Button btnSMSCode;
    private ImageView ivRegisterPhoneClear;
    private Dialog mDialog;

    private SMSCodeCountdown mCountDown;

    // 用户拍照后传入的保存路径数组，从TakeCaptureActivity传入LoginActivity，
    private String [] mFaceCollect;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SMSCodeCountdown.IN_RUNNING : // 正在倒计时
                    btnSMSCode.setText(msg.obj.toString());
                    break;
                case SMSCodeCountdown.END_RUNNING : // 完成倒计时
                    btnSMSCode.setBackground(getResources().getDrawable(R.drawable.shade_sms_code_green_bg_border_radius));
                    btnSMSCode.setText(msg.obj.toString());
                    btnSMSCode.setEnabled(true);
                    break;
                case START_REQ_SMS :
                {
                    btnSMSCode.setEnabled(false); // button不可用
                    btnSMSCode.setBackground(getResources().getDrawable(R.drawable.shade_sms_code_gray_bg_border_radius));
                    mCountDown.start();
                }
                break;
                case REGISTER_SUCCESS : // 注册成功，返回登录页面
                {
                    //mCountDown.onFinish();
                    mCountDown.cancel();
                    //replaceFragment(R.id.fl_fragment_login_container, new LoginFragment());
                    onDestroy();
                    mLoginActivity.onBackPressed();
                }
                break;
                default:
                    break;
            }
        }
    };

    public RegisterFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoginActivity = (LoginActivity) getActivity();
        // 倒计时
        mCountDown = new SMSCodeCountdown(60000, 1000, mHandler);
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_register;
    }

    // 在父类BasicFragment的onActivityCreated中调用
    @Override
    public void initView() {
        mFaceCollect = mLoginActivity.getFaceInfoSavePath();

        etPhone = (EditText) mView.findViewById(R.id.et_register_phone);
        etSMSCode = (EditText) mView.findViewById(R.id.et_register_sms_code);

        ivRegisterPhoneClear = (ImageView) mView.findViewById(R.id.iv_register_phone_clear);
        EditTextClearTools.addClearListener(etPhone, ivRegisterPhoneClear);

        btnRegister = (Button) mView.findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //btnRegister.setEnabled(false);
               mPresenter.register(); // 注册
            }
        });

        btnSMSCode = (Button) mView.findViewById(R.id.btn_register_sms_code);
        btnSMSCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.reqSMSCode(); // 请求短信验证码
            }
        });
    }

    @Override
    public void updateToolbarMainTitle() {
        mLoginActivity.updateToobarTitle(R.drawable.icon_return, null, getString(R.string.register_text), 0, null);
    }

    @Override
    public RegisterFragmentPresenter createPresenter() {
        return new RegisterFragmentPresenter(new RegisterFragmentModel());
    }

    @Override
    public void showLoading() {
        mDialog = LoadingDialogUtils.createLoadingDialog(getContext(), "请稍后...");
    }

    @Override
    public void hideLoading() {
        LoadingDialogUtils.closeDialog(mDialog);
    }

    @Override
    public String getPhone() {
        return etPhone.getText().toString();
    }

    @Override
    public String getSMSCode() {
        return etSMSCode.getText().toString();
    }

    @Override
    public void showResult(boolean loginResult, String msg) {
        if(loginResult == true) {
            toast(mLoginActivity, msg, SHORT_SHOW);
            Message message = new Message();
            message.what = REGISTER_SUCCESS;
            mHandler.sendMessageDelayed(message, 2000);
            //mHandler.obtainMessage(REGISTER_SUCCESS).sendToTarget();
        } else {
            customToast(mLoginActivity, msg, SHORT_SHOW);
        }
    }

    @Override
    public void reqSMSStatus(boolean isSuccess, final String msg) {
        if(isSuccess) {
            mHandler.obtainMessage(START_REQ_SMS, msg).sendToTarget();
        } else {
            customToast(mLoginActivity, msg, SHORT_SHOW);
        }
    }

    @Override
    public String[] getFaceCollect() {
        return mFaceCollect;
    }

    @Override
    public void showMsg(String msg) {
        toast(mLoginActivity, msg, SHORT_SHOW);
    }

    @Override
    public String getRegistrationID() {
        String rid = JPushInterface.getRegistrationID(mLoginActivity.getApplicationContext());
        return rid;
    }
}
