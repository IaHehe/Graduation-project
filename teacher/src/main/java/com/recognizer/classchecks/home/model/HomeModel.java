package com.recognizer.classchecks.home.model;

import com.library.pojo.BasicEntity;
import com.library.utils.GsonUtil;
import com.library.utils.HttpUtils;
import com.library.utils.LogUtil;
import com.recognizer.classchecks.home.model.bean.CheckCourseBean;
import com.recognizer.common.global.HttpAddrConstants;
import com.recognizer.common.global.HttpRequestCode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author Dongjun Zou
 * @Description ${todo}
 * @email 984147586@qq.com
 * @date 2017/5/30 20:43
 */

public class HomeModel {

    public void checkHasCourse(String jwAccount, final Callback callback) {
        Map<String, String> reqParams = new HashMap<>();
        reqParams.put("teacherNo", jwAccount);
        HttpUtils.doPost(HttpAddrConstants.TEACHER_HAS_COURSE, reqParams, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.i("onFailure", e);
                callback.onFailed(new BasicEntity(HttpRequestCode.REQUEST_OVERTIME[0], HttpRequestCode.REQUEST_OVERTIME[1]));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                if(response.isSuccessful()) {
                    if(response.code() == 200) {
                        LogUtil.i(responseBody);
                        CheckCourseBean ccb = GsonUtil.GsonToBean(responseBody, CheckCourseBean.class);
                        callback.onSuccess(ccb);
                    } else {
                        LogUtil.i(responseBody+"  请求码："+ response.code());
                    }
                } else {
                    LogUtil.i(responseBody+"  请求码："+response.code());
                     callback.onFailed(new BasicEntity(String.valueOf(response.code()), "请求码："+response.code()));
                }
            }
        });
    }

    public interface Callback {
        void onSuccess(Object obj);
        void onFailed(Object obj);
    }

}
