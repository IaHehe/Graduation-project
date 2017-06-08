package com.recognizer.classchecks.login.model;

import com.google.gson.reflect.TypeToken;
import com.library.pojo.BasicEntity;
import com.library.pojo.BasicEntityData;
import com.library.utils.GsonUtil;
import com.library.utils.HttpUtils;
import com.library.utils.LogUtil;
import com.recognizer.classchecks.login.model.bean.TeacherBean;
import com.recognizer.common.global.HttpAddrConstants;
import com.recognizer.common.global.HttpRequestCode;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * @author Dongjun Zou
 * @Description ${todo}
 * @email 984147586@qq.com
 * @date 2017/5/13 15:56
 */

public class LoginFragmentModel {

    public void login(final String phone, final String smsCode, boolean isVerifySmsCode, String regId, final Callback callback) {
        Map<String, String> reqParams = new HashMap<>();
        reqParams.put("phone", phone);
        reqParams.put("smscode", smsCode);
        reqParams.put("isVerifySmsCode", String.valueOf(isVerifySmsCode));
        reqParams.put("regId", regId);
        reqParams.put("ts", String.valueOf(System.currentTimeMillis()));
        HttpUtils.doPost(HttpAddrConstants.TEACHER_LOGIN, reqParams, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) { // ConnectException
                LogUtil.i("", e);
                callback.onFailed(new BasicEntityData<>(HttpRequestCode.REQUEST_OVERTIME[0], HttpRequestCode.REQUEST_OVERTIME[1]));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                LogUtil.i("responseBody"+responseBody);
                if(response.isSuccessful()) {
                    if(response.code() == 200) {
                        Type jsonType = new TypeToken<BasicEntityData<TeacherBean>>() {}.getType();
                        BasicEntityData<TeacherBean> beData = GsonUtil.GsonToBean(responseBody, BasicEntityData.class, jsonType);
                        callback.onSuccess(beData);
                    } else {
                        BasicEntityData basicEntityData = new BasicEntityData<>(HttpRequestCode.REQUEST_NETWORK_ERROR[0], HttpRequestCode.REQUEST_NETWORK_ERROR[1]);
                        callback.onFailed(basicEntityData);
                    }
                } else {
                    callback.onFailed(new BasicEntityData<>(HttpRequestCode.REQUEST_NETWORK_ERROR[0], HttpRequestCode.REQUEST_NETWORK_ERROR[1]));
                }
            }
        });
    }


    public void reqSMSCode(String phone, final Callback callback) {
        final Map<String, String> reqParams = new HashMap<>();
        reqParams.put("phone", phone);
        HttpUtils.doPost(HttpAddrConstants.REQ_LOGIN_SMS_CODE, reqParams, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.i("onFailure");
                LogUtil.i(String.valueOf(e.getMessage()));

                callback.onFailed(new BasicEntity(HttpRequestCode.REQUEST_NETWORK_ERROR[0], HttpRequestCode.REQUEST_NETWORK_ERROR[1]));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBodyString = response.body().string();
                LogUtil.i("responseBodyString:" + responseBodyString);
                if(response.isSuccessful()) {
                    BasicEntity be = null;
                    if(response.code() == 200) {
                        be = GsonUtil.GsonToBean(responseBodyString, BasicEntity.class);
                        callback.onSuccess(be);
                    } else {
                        be = new BasicEntity(HttpRequestCode.REQUEST_CONNECT_ERROR[0], HttpRequestCode.REQUEST_CONNECT_ERROR[1]);
                        callback.onFailed(be);
                    }
                } else {
                    callback.onFailed(new BasicEntity(HttpRequestCode.REQUEST_NETWORK_ERROR[0], HttpRequestCode.REQUEST_NETWORK_ERROR[1]));
                }
            }
        });
    }

    public interface Callback {
        void onSuccess(Object obj);
        void onFailed(Object obj);
    }
}
