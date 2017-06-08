package com.recognizer.classchecks.login.model;

import android.util.Log;

import com.library.pojo.BasicEntity;
import com.library.utils.GsonUtil;
import com.library.utils.HttpUtils;
import com.library.utils.LogUtil;
import com.recognizer.common.global.HttpAddrConstants;
import com.recognizer.common.global.HttpRequestCode;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * @author Dongjun Zou
 * @Description ${todo}
 * @email 984147586@qq.com
 * @date 2017/5/13 16:44
 */

public class RegisterFragmentModel {


    public void register(String phone, String SMSCode, String regID, final Callback callback) {
        Map<String, String> reqParams = new HashMap<String, String>();
        reqParams.put("phone", phone);
        reqParams.put("smscode", SMSCode);
        reqParams.put("regID", regID);

        HttpUtils.doPost(HttpAddrConstants.TEACHER_REGISTER, reqParams, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                BasicEntity be = null;
                if(e.getClass().equals(SocketTimeoutException.class)) {
                    LogUtil.i("RegisterFragmentModel--zou--onFailure-2001", e);
                    // 当请求服务器出错时
                    be = new BasicEntity(HttpRequestCode.REQUEST_CONNECT_OVERTIME[0], HttpRequestCode.REQUEST_CONNECT_OVERTIME[1]);
                    callback.onFailed(be);
                } else { // java.net.ConnectException、
                    LogUtil.i("RegisterFragmentModel--zou-onFailure--2002", e);
                    be = new BasicEntity(HttpRequestCode.REQUEST_NETWORK_ERROR[0], HttpRequestCode.REQUEST_NETWORK_ERROR[1]);
                    callback.onFailed(be);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBodyString = response.body().string();
                LogUtil.i(responseBodyString);
                if(response.code() == 200) {
                    BasicEntity be = GsonUtil.GsonToBean(responseBodyString, BasicEntity.class);
                    callback.onSuccess(be);
                }
                else {
                    callback.onFailed(new BasicEntity(HttpRequestCode.REQUEST_NETWORK_ERROR[0], HttpRequestCode.REQUEST_NETWORK_ERROR[1]));
                }
            }
        });
    }

    public void reqSMSCode(String phone, final Callback callback) {
        Map<String, String> reqParams = new HashMap<>();
        reqParams.put("phone", phone);
        HttpUtils.doPost(HttpAddrConstants.REQ_REGISTER_SMS_CODE, reqParams, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("zou:", String.valueOf(e.getMessage()));
                Log.i("zou", "onFailure");
                BasicEntity be = new BasicEntity(HttpRequestCode.REQUEST_NETWORK_ERROR[0], HttpRequestCode.REQUEST_NETWORK_ERROR[1]);
                callback.onFailed(be);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBodyString = response.body().string();
                Log.i("zou", ""+ responseBodyString);
                Log.i("zou", "onResponse");
                if(response.code() == 200) {
                    BasicEntity be = GsonUtil.GsonToBean(responseBodyString, BasicEntity.class);
                    callback.onSuccess(be);
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
