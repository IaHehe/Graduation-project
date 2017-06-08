package com.recognizer.classchecks.clockin.model;

/**
 * @author Dongjun Zou
 * @Description ${todo}
 * @email 984147586@qq.com
 * @date 2017/5/24 23:52
 */

public interface Callback {

    void onSuccess(Object object);
    void onFailed(Object  object);
}
