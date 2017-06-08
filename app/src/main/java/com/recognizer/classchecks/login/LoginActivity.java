package com.recognizer.classchecks.login;


import android.content.Intent;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;

import com.library.basic.BasicActivity;
import com.library.utils.CameraPermissionCompat;
import com.library.utils.NetworkUtils;
import com.library.widget.CustomToolbar;
import com.recognizer.R;
import com.recognizer.classchecks.camera.TakePictureActivity;
import com.recognizer.classchecks.home.HomeActivity;
import com.recognizer.classchecks.login.fragment.LoginFragment;
import com.recognizer.classchecks.login.fragment.RegisterFragment;

public class LoginActivity extends BasicActivity implements View.OnClickListener{

    private String [] mFaceInfoSavePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        // from是从TakePictureActivity传过来的，表示需要跳到注册界面
        String from = intent.getStringExtra("from");
        // from为null，表示启动程序，从启动页传入
        if(from == null) {
            if(null == savedInstanceState) {
                addFragment(new LoginFragment());
            }
            if(!NetworkUtils.isConnected(getApplicationContext())) {
                customToast("请检查网络是否连接", LONG_SHOW);
            }
        } else {
            // faceInfoSavePath是从TakePictureActivity人脸收集完后传过来的人脸图片的路径数组
            mFaceInfoSavePath = intent.getStringArrayExtra("faceInfoSavePath");
            addFragment(new RegisterFragment());
        }
    }

    @Override
    protected void createToolbar() {
        mCustomToolbar = (CustomToolbar) findViewById(R.id.custom_toolbar_login);
        mCustomToolbar.setMainTitleLeftDrawable(null);
        mCustomToolbar.setMainTitle(getString(R.string.activity_login_main_title));
        mCustomToolbar.setMainTitleRightText(getString(R.string.register_text));
        mCustomToolbar.setMainTitleRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.fl_fragment_login_container, new RegisterFragment())
//                        .addToBackStack(null)
//                        .commit();
                // 检查相机权限
                CameraPermissionCompat.checkCameraPermission(LoginActivity.this, new CameraPermissionCompat.OnCameraPermissionListener() {
                    @Override
                    public void onGrantResult(boolean granted) {
                        if(granted == true) {
                            Intent intent = new Intent(getApplicationContext(), TakePictureActivity.class);
                            startActivity(intent);
                        } else {
                            customToast("请检查是否允许使用摄像头", SHORT_SHOW);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        CameraPermissionCompat.onRequestPermissionsResult(this,requestCode,permissions,grantResults);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_login) {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        }
    }

    private void addFragment(Fragment fragment) {
        FragmentManager fm= getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fl_fragment_login_container, fragment);
        ft.commit();
    }

    public String[] getFaceInfoSavePath() {
        return this.mFaceInfoSavePath;
    }

}
