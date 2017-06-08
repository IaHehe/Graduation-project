package com.recognizer.classchecks.course.model;

/**
 * @author Dongjun Zou
 * @Description ${todo}
 * @email 984147586@qq.com
 * @date 2017/5/22 17:45
 */

public interface Callback {

    void onSuccess(Object object);
    void onFailed(Object object);

}
