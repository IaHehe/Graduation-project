package com.library.mvp;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 *
 * @author Dongjun Zou
 * @email 984147586@qq.com
 */

public abstract class BasePresenter<V extends BaseView> {
    protected Reference<V> mViewRef;//View接口类型弱引用

    /**
     * @Method:         绑定View
     * @description:
     * @author:         Dongjun Zou
     * @date:           2017/4/28 0:14
     */
    public void attachView(V view) {
        mViewRef = new WeakReference<V>(view);
    }

    /**
     * @Method:         获取View
     * @description:
     * @author:         Dongjun Zou
     * @date:           2017/4/28 0:15
     */
    public V getView() {
        return mViewRef.get();
    }

    /**
     * @Method:         判断是否与View建立了关联
     * @description:
     * @author:         Dongjun Zou
     * @date:           2017/4/28 0:16
     */
    public boolean checkViewAttached() {
        return null != mViewRef && null != mViewRef.get();
    }

    /**
     * @Method:         解除关联
     * @description:
     * @author:         Dongjun Zou
     * @date:           2017/4/28 0:17
     */
    public void detachView() {
        if(null != mViewRef) {
            mViewRef.clear();
            mViewRef = null;
        }
    }
}
