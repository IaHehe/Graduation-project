package com.recognizer.classchecks.report.model;

/**
 * @author Dongjun Zou
 * @Description ${todo}
 * @email 984147586@qq.com
 * @date 2017/6/1 22:42
 */

public interface Callback {

    void onSuccess(Object obj);
    void onFailed(Object obj);

}
