package com.library.mvp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.library.basic.BasicActivity;

/**
 * @author Dongjun Zou
 * @Description ${todo}
 * @email 984147586@qq.com
 * @date 2017/4/10 10:46
 */

public abstract class BaseActivity <V extends BaseView,T extends BasePresenter<V>> extends BasicActivity {
    public T presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = createPresenter(); // 创建presenter
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.attachView((V)this);
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    // 实例化presenter
    public abstract T createPresenter();
}

