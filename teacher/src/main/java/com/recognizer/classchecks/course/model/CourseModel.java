package com.recognizer.classchecks.course.model;

import com.library.pojo.BasicEntity;
import com.library.utils.GsonUtil;
import com.library.utils.HttpUtils;
import com.library.utils.LogUtil;
import com.recognizer.classchecks.course.model.bean.CourseBean;
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
 * @date 2017/5/23 00:19
 */

public class CourseModel {

    public void obtainCourse(String account, String week, final Callback callback) {

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("jwAccount", account);
        queryParams.put("week", week);
        HttpUtils.doPost(HttpAddrConstants.REQ_COURSE_BY_WEEK, queryParams, new okhttp3.Callback() {
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
                        CourseBean courseBean = GsonUtil.GsonToBean(responseBody, CourseBean.class);
                        callback.onSuccess(courseBean);
                    } else {
                        callback.onFailed(new BasicEntity(""+response.code(), "请求码：" + response.code()));
                    }
                } else {
                    callback.onFailed(new BasicEntity(""+response.code(), "请求码：" + response.code()));
                }
            }
        });
    }

}
