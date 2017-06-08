package com.recognizer.classchecks.clockin.model;

import com.google.gson.reflect.TypeToken;
import com.library.pojo.BasicEntity;
import com.library.pojo.BasicEntityList;
import com.library.utils.GsonUtil;
import com.library.utils.HttpUtils;
import com.library.utils.LogUtil;
import com.recognizer.classchecks.clockin.model.bean.ClockInResultBean;
import com.recognizer.common.global.HttpAddrConstants;
import com.recognizer.common.global.HttpRequestCode;

import java.io.IOException;
import java.lang.reflect.Type;
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
 * @date 2017/5/24 21:22
 */

public class PhotographModel {

    public void uploadImageAsClockIn(String jwAccount, String loginAccount, double lng, double lat,
                                     String imagePath, int byWhat, final Callback callback) {

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("jwAccount", jwAccount);
        queryParams.put("phone", loginAccount);
        queryParams.put("longitude", String.valueOf(lng));
        queryParams.put("latitude", String.valueOf(lat));
        queryParams.put("byWhat", String.valueOf(byWhat));
        List<String> filesPath = new ArrayList<>();
        filesPath.add(imagePath);

        HttpUtils.doPostForm(HttpAddrConstants.TEACHER_CLOCK_IN, queryParams, "clockinImg", filesPath, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.i("onFailure", e);
                if(e.getClass().equals(SocketTimeoutException.class)) {
                    LogUtil.i("RegisterFragmentModel--zou--onFailure-2001", e);
                    // 当请求服务器出错时
                    callback.onFailed(new BasicEntityList<>(HttpRequestCode.REQUEST_CONNECT_OVERTIME[0], HttpRequestCode.REQUEST_CONNECT_OVERTIME[1]));
                } else {
                    callback.onFailed(new BasicEntityList<>(HttpRequestCode.CHECK_NETWORK_AVAILABLE[0], HttpRequestCode.CHECK_NETWORK_AVAILABLE[1]));
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBodyString = response.body().string();
                LogUtil.i(responseBodyString);
                if(response.isSuccessful()) {
                    if(response.code() == 200) {
                        Type jsonType = new TypeToken<BasicEntityList<ClockInResultBean>>() {}.getType();
                        BasicEntityList<ClockInResultBean> basicEntityList = GsonUtil.GsonToBean(responseBodyString, BasicEntityList.class, jsonType);
                        callback.onSuccess(basicEntityList);
                    } else {
                        callback.onFailed(new BasicEntityList<>(String.valueOf(response.code()), "请求错误："+response.code()));
                    }
                } else {
                    callback.onFailed(new BasicEntityList<>(String.valueOf(response.code()), "请求错误："+response.code()));
                }
            }
        });
    }


}
