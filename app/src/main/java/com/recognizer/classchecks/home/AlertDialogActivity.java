package com.recognizer.classchecks.home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.library.widget.AlertDialogUtils;
import com.library.widget.LoadingDialogUtils;
import com.library.widget.AlertDialog;
import com.library.widget.ToastUtils;
import com.recognizer.R;


public class AlertDialogActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnAlertDialog;
    Button btnLoadingDialog;
    Button btnCustomDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_dialog);
        btnAlertDialog = (Button) findViewById(R.id.btn_alert_dialog);
        btnAlertDialog.setOnClickListener(this);

        btnLoadingDialog = (Button) findViewById(R.id.btn_loading_dialog);
        btnLoadingDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingDialogUtils.createLoadingDialog(AlertDialogActivity.this, "请稍等...");
            }
        });

        btnCustomDialog = (Button) findViewById(R.id.btn_custom_alert_dialog);
        btnCustomDialog.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_alert_dialog) {
            final AlertDialog ad=new AlertDialog(AlertDialogActivity.this);
            ad.setTitle("确定退出");
            ad.setMessage("内容sdfsafdasf内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容");
            ad.setPositiveButton("确定退出", new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    ad.dismiss();
                    ToastUtils.showToast(AlertDialogActivity.this,"点击了确定", ToastUtils.SHORT_TIME);
                }
            });

            ad.setNegativeButton("取消", new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    ad.dismiss();
                    ToastUtils.showToast(AlertDialogActivity.this,"点击了取消", ToastUtils.SHORT_TIME);
                }
            });
        } else if(v.getId() == R.id.btn_custom_alert_dialog) {
            AlertDialogUtils.DialogListener listener1 = new AlertDialogUtils.DialogListener() {
                @Override
                public void callback(String[] array) {
                    Toast.makeText(getApplicationContext(), "确定退出", Toast.LENGTH_SHORT).show();
                }
            };

            AlertDialogUtils.DialogListener listener2 = new AlertDialogUtils.DialogListener() {
                @Override
                public void callback(String[] array) {
                    Toast.makeText(getApplicationContext(), "取消", Toast.LENGTH_SHORT).show();
                }
            };

           AlertDialogUtils.alert(AlertDialogActivity.this,AlertDialogUtils.TYPE_WARNING,"确定退出登录吗？",
                   "我这里使用了一个handler延迟两秒钟让它消失不见。当然项目中大家可以监听自己所需要的方法，使加载框消失。" +
                           "这只是简单的两种实现方式，当然还有更好的其他效果，具体可以自己实现以下，也可以去网上搜索一下。",
                   listener1,"确定",listener2,"取消");
        }
    }
}
