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

    private boolean isAfresh = true; // okhttp重新连接

    public void register(String phone, String SMSCode, String regID, String[] facesPath, final Callback callback) {
        Map<String, String> reqParams = new HashMap<String, String>();
        reqParams.put("phone", phone);
        reqParams.put("smscode", SMSCode);
        reqParams.put("regID", regID);
        List<String> files = new ArrayList<>();
        for(String s : facesPath) {
            files.add(s);
        }
        HttpUtils.doPostForm(HttpAddrConstants.USER_REGISTER, reqParams, "files", files, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.i("onFailure", e);
                if(e.getClass().equals(SocketTimeoutException.class)) {
                    // 当请求服务器出错时
                    callback.onFailed(new BasicEntity(HttpRequestCode.REQUEST_CONNECT_OVERTIME[0], HttpRequestCode.REQUEST_CONNECT_OVERTIME[1]));
                } else { // java.net.ConnectException、
                    callback.onFailed(new BasicEntity(HttpRequestCode.CHECK_NETWORK_AVAILABLE[0], HttpRequestCode.CHECK_NETWORK_AVAILABLE[1]));
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
                    callback.onFailed(new BasicEntity(String.valueOf(response.code()), "请求码："+response.code()));
                }
            }
        });
    }

    public void reqSMSCode(String phone, final RegisterFragmentModel.Callback callback) {
        Map<String, String> reqParams = new HashMap<>();
        reqParams.put("phone", phone);
        HttpUtils.doPost(HttpAddrConstants.REQ_REGISTER_SMS_CODE, reqParams, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.i(String.valueOf(e.getMessage()));
                LogUtil.i("onFailure", e);
                BasicEntity be = new BasicEntity(HttpRequestCode.REQUEST_NETWORK_ERROR[0], HttpRequestCode.REQUEST_NETWORK_ERROR[1]);
                callback.onFailed(be);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBodyString = response.body().string();
                LogUtil.i(responseBodyString);
                if(response.code() == 200) {
                    BasicEntity be = GsonUtil.GsonToBean(responseBodyString, BasicEntity.class);
                    callback.onSuccess(be);
                } else {
                    callback.onFailed(new BasicEntity(String.valueOf(response.code()), "请求码：" + response.code()));
                }
            }
        });
    }

    public interface Callback {
        void onSuccess(BasicEntity be);
        void onFailed(BasicEntity be);
    }

}
