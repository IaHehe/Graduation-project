package com.recognizer.classchecks.startup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.library.utils.DeviceInfoUtils;

import com.recognizer.R;
import com.recognizer.classchecks.login.LoginActivity;

public class StartupActivity extends Activity implements Animation.AnimationListener{

    private ImageView mImageView = null;
    private TextView mTextView = null;
    private LinearLayout mLinearLayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏状态栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_startup);
        mLinearLayout = (LinearLayout) this.findViewById(R.id.ll_launcher);

        int statusBarHeight = DeviceInfoUtils.getStatusBarHeight(this);
        Log.i("zou-statusBarHeight:", statusBarHeight+"");
        int toYDelta = (int) (DeviceInfoUtils.getScreenHeight(getApplicationContext()) / 2);
        toYDelta = toYDelta - statusBarHeight * 2;
        Log.i("zou-toYdelta:", toYDelta+"");
        // 位移动画
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, toYDelta);
        translateAnimation.setRepeatCount(0);
        translateAnimation.setDuration(2000);
        // Animation可以设置插值器,BounceInterpolator(回弹)
        translateAnimation.setInterpolator(new BounceInterpolator());
        //动画之后停留在动画后的界面
        translateAnimation.setFillAfter(true);
        translateAnimation.setAnimationListener(this);
        mLinearLayout.setAnimation(translateAnimation);

    }

    private void skip() {
        startActivity(new Intent(this, LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_right ,0);//android.R.anim.slide_in_left
        finish();
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        skip();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
