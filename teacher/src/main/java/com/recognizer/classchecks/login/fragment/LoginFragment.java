package com.recognizer.classchecks.login.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.library.mvp.BaseFragment;
import com.library.utils.EditTextClearTools;
import com.library.widget.LoadingDialogUtils;
import com.recognizer.R;
import com.recognizer.classchecks.login.LoginActivity;
import com.recognizer.classchecks.login.model.LoginFragmentModel;
import com.recognizer.classchecks.login.model.bean.TeacherBean;
import com.recognizer.classchecks.login.presenter.LoginFragmentPresenter;
import com.recognizer.classchecks.login.view.LoginFragmentView;
import com.recognizer.common.global.pref.PrefManager;
import com.recognizer.common.util.SMSCodeCountdown;

import cn.jpush.android.api.JPushInterface;


public class LoginFragment extends BaseFragment<LoginFragmentView, LoginFragmentPresenter>
        implements LoginFragmentView{

    private static final int START_REQ_SMS = 3; // 请求短信验证码标识

    private LoginActivity mLoginActivity;

    private EditText etPhone;
    private EditText etSMSCode;
    private Button btnLogin;
    private Button btnSMSCode;

    private SMSCodeCountdown mCountDown;

    private Dialog mDialog;

    private String phone;
    private String smscode;
    private boolean isAutoLogin;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case START_REQ_SMS :
                {
                    btnSMSCode.setEnabled(false); // button不可用
                    btnSMSCode.setBackground(getResources().getDrawable(R.drawable.shade_sms_code_gray_bg_border_radius));
                    mCountDown.start();
                }
                break;
                case SMSCodeCountdown.IN_RUNNING : // 正在倒计时
                {
                    btnSMSCode.setText(msg.obj.toString());
                }
                break;
                case SMSCodeCountdown.END_RUNNING : // 完成倒计时
                {
                    btnSMSCode.setBackground(getResources().getDrawable(R.drawable.shade_sms_code_green_bg_border_radius));
                    btnSMSCode.setText(msg.obj.toString());
                    btnSMSCode.setEnabled(true);
                }
                break;
                default:
                    break;
            }
        }
    };

    public LoginFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mLoginActivity = (LoginActivity) getActivity();
    }

    @Override
    public void onStart() {
        super.onStart();
        isAutoLogin = PrefManager.getBoolean(getContext(), PrefManager.AUTO_LOGIN_SIGN, false);
        phone = PrefManager.getString(getContext(), PrefManager.LOGIN_ACCOUNT, "");
        etPhone.setText(phone);
        smscode = PrefManager.getString(getContext(), PrefManager.LOGIN_SMS_CODE, "");
        etSMSCode.setText(smscode);
        if(isAutoLogin == true) {
            mPresenter.login();
        }
        mCountDown = new SMSCodeCountdown(60000, 1000, mHandler);
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_login;
    }

    @Override
    public void initView() {
        etPhone = (EditText) mView.findViewById(R.id.et_login_phone);
        ImageView mUserClearImage = (ImageView) mView.findViewById(R.id.iv_user_clear);
        etSMSCode = (EditText) mView.findViewById(R.id.et_login_sms_code);

        btnLogin = (Button) mView.findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                phone = etPhone.getText().toString();
//                smscode = etSMSCode.getText().toString();
                mPresenter.login();
            }
        });

        btnSMSCode = (Button) mView.findViewById(R.id.btn_req_sms_code);
        btnSMSCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                phone = etPhone.getText().toString();
                mPresenter.reqSMSCode();
            }
        });

        EditTextClearTools.addClearListener(etPhone, mUserClearImage);
    }

    @Override
    public void updateToolbarMainTitle() {
        mLoginActivity.updateToobarTitle(0, null, getString(R.string.activity_login_main_title), 0, getString(R.string.register_text));
    }

    @Override
    public LoginFragmentPresenter createPresenter() {
        return new LoginFragmentPresenter(new LoginFragmentModel());
    }

    @Override
    public void showLoading() {
        mDialog = LoadingDialogUtils.createLoadingDialog(mLoginActivity, "");
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
    public boolean getIsAutoLogin() {
        // 在缓存中获取到key为isAutoLogin的值为true时，对应后台不需要执行短信接口验证
        // 如果key为isAutoLogin的值为false时，表示停留登录界面，对应后台需要执行短息接口验证手机号和验证码
        return isAutoLogin == false ? true : false;
    }

    @Override
    public void showResult(int what, String msg) {
        if(SMS_CODE_SUCESS == what) {
            mHandler.obtainMessage(START_REQ_SMS, msg).sendToTarget();
        } else if(LOGIN_SUCESS == what) {
            toast(mLoginActivity, msg, SHORT_SHOW);
            skip();
        } else {
            toast(mLoginActivity, msg, SHORT_SHOW);
        }
    }

    @Override
    public String getRegId() {
        return JPushInterface.getRegistrationID(getContext());
    }

    @Override
    public void skip() {
        mLoginActivity.onClick(btnLogin);
    }

    @Override
    public void infoCache(TeacherBean teacherBean) {
        PrefManager.setBoolean(getContext(), PrefManager.AUTO_LOGIN_SIGN, true);
        PrefManager.setString(getContext(), PrefManager.LOGIN_ACCOUNT, teacherBean.getSecurityAccount());
        PrefManager.setString(getContext(), PrefManager.LOGIN_SMS_CODE, teacherBean.getSecuritSmsCode());
        PrefManager.setInt(getContext(), PrefManager.USER_TYPE, teacherBean.getSecuritType());
        PrefManager.setInt(getContext(), PrefManager.FACE_LABLE, teacherBean.getFaceLabel());
        PrefManager.setString(getContext(), PrefManager.JW_ACCOUNT, teacherBean.getJwAccount());
    }
}
