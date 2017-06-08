package com.recognizer.classchecks.report.model;

import android.util.Log;

import com.library.pojo.BasicEntity;
import com.library.utils.GsonUtil;
import com.library.utils.HttpUtils;
import com.library.utils.LogUtil;
import com.recognizer.classchecks.report.model.bean.ReportResultBean;
import com.recognizer.common.global.HttpAddrConstants;
import com.recognizer.common.global.HttpRequestCode;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * @author Dongjun Zou
 * @Description ${todo}
 * @email 984147586@qq.com
 * @date 2017/6/1 20:22
 */

public class GenerateReportModel {

    public void exportReport(String jwAccount, String email, final Callback callback) {
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("jwAccount", jwAccount);
        mapParams.put("email", email);

        HttpUtils.doPost(HttpAddrConstants.EXPORT_SEMESTER_REPORT, mapParams, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.i("onFailure", e);
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
                String responseBody = response.body().string();
                LogUtil.i(responseBody);

                if(response.isSuccessful()) {
                    if(response.code() == 200) {
                        ReportResultBean bean = GsonUtil.GsonToBean(responseBody, ReportResultBean.class);
                        callback.onSuccess(bean);
                    } else {
                        callback.onFailed(new BasicEntity(String.valueOf(response.code()), "请求码："+response.code()));
                    }
                } else {
                    callback.onFailed(new BasicEntity(String.valueOf(response.code()), "请求码："+response.code()));
                }
            }
        });
    }

}
