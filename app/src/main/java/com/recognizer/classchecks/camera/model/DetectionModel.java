package com.recognizer.classchecks.camera.model;

import com.google.gson.reflect.TypeToken;
import com.library.pojo.BasicEntityData;
import com.library.utils.GsonUtil;
import com.library.utils.HttpUtils;
import com.library.utils.LogUtil;
import com.recognizer.classchecks.camera.model.bean.StudentClockinBean;
import com.recognizer.common.global.HttpAddrConstants;
import com.recognizer.common.global.HttpRequestCode;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * @author Dongjun Zou
 * @Description ${todo}
 * @email 984147586@qq.com
 * @date 2017/6/1 02:35
 */

public class DetectionModel {

    public void clockin(String jwAccount, String loginAccount,
                        double lng, double lat, byte[] imgByte, final Callback callback) {

        Map<String, String> reqParams = new HashMap<>();
        reqParams.put("jwAccount", jwAccount);
        reqParams.put("loginAccount", loginAccount);
        reqParams.put("lng", String.valueOf(lng));
        reqParams.put("lat", String.valueOf(lat));

        HttpUtils.doPostFromByStream(HttpAddrConstants.STUDENT_CLOCK_IN, reqParams, "clockinImg", imgByte, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.i("onFilure", e);
                if(e.getClass().equals(SocketTimeoutException.class)) {
                    // 当请求服务器出错时
                    callback.onFailed(new BasicEntityData<>(HttpRequestCode.REQUEST_OVERTIME[0], HttpRequestCode.REQUEST_OVERTIME[1]));
                } else { // java.net.ConnectException、
                    callback.onFailed(new BasicEntityData<>(HttpRequestCode.CHECK_NETWORK_AVAILABLE[0], HttpRequestCode.CHECK_NETWORK_AVAILABLE[1]));
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                LogUtil.i(responseBody);
                LogUtil.i("okhttp->Response->message() : "+response.message());
                if(response.isSuccessful()) {
                    if(response.code() == 200) {
                        Type jsonType = new TypeToken<BasicEntityData<StudentClockinBean>>() {}.getType();
                        BasicEntityData<StudentClockinBean> basicEntityData = GsonUtil.GsonToBean(responseBody, BasicEntityData.class, jsonType);
                        if(null != basicEntityData.getData()) {
                            LogUtil.i(basicEntityData.getData().toString());
                        }
                        callback.onSuccess(basicEntityData);
                    } else {
                        callback.onFailed(new BasicEntityData<>(String.valueOf(response.code()), "请求码："+response.code()));
                    }
                } else {
                    callback.onFailed(new BasicEntityData<>(String.valueOf(response.code()), "请求码："+response.code()));
                }
            }
        });
    }

}
