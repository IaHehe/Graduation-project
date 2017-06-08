package com.recognizer.classchecks.course.model;

import com.library.pojo.BasicEntity;
import com.library.utils.GsonUtil;
import com.library.utils.HttpUtils;
import com.library.utils.LogUtil;
import com.recognizer.classchecks.course.model.bean.ImportBean;
import com.recognizer.common.global.HttpAddrConstants;
import com.recognizer.common.global.HttpRequestCode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * @author Dongjun Zou
 * @Description ${todo}
 * @email 984147586@qq.com
 * @date 2017/5/22 17:08
 */

public class ImportTableModel {

    public void importTable(String jwAccount, String jwPwd, String loginAccount, final Callback callback) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("jwAccount", jwAccount);
        queryParams.put("jwPwd", jwPwd);
        queryParams.put("loginAccount", loginAccount);
        HttpUtils.doPost(HttpAddrConstants.IMPORT_TABLE, queryParams, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.i("onFailure", e);
                callback.onFailed(new BasicEntity(HttpRequestCode.REQUEST_CONNECT_OVERTIME[0], HttpRequestCode.REQUEST_CONNECT_OVERTIME[1]));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                LogUtil.i(responseBody);
                if(response.isSuccessful()) {
                    if(response.code() == 200) {
                        ImportBean bean =  GsonUtil.GsonToBean(responseBody, ImportBean.class);
                        callback.onSuccess(bean);
                    } else {
                        callback.onFailed(new BasicEntity(String.valueOf(response.code()), "请求错误,请求码："+response.code()));
                    }
                } else {
                    callback.onFailed(new BasicEntity(String.valueOf(response.code()), "请求错误,请求码："+response.code()));
                }
            }
        });
    }
}
