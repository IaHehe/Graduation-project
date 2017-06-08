package com.recognizer.classchecks.report;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.alertdialog.AlertView;
import com.alertdialog.OnItemClickListener;
import com.library.mvp.BaseActivity;
import com.library.utils.StringUtils;
import com.library.widget.CustomToolbar;
import com.library.widget.LoadingDialogUtils;
import com.recognizer.R;
import com.recognizer.classchecks.home.HomeActivity;
import com.recognizer.classchecks.report.model.GenerateReportModel;
import com.recognizer.classchecks.report.presenter.GenerateReportPresenter;
import com.recognizer.classchecks.report.view.GenerateReportView;
import com.recognizer.common.global.pref.PrefManager;

public class GenerateReportActivity extends BaseActivity<GenerateReportView, GenerateReportPresenter>
    implements View.OnClickListener, GenerateReportView, OnItemClickListener{

    private TextView mTVExportReport; // 生成报表
    private TextView mTVEnsureExport; // 确认导出

    private AlertView mAlertViewExt;//窗口拓展例子
    private EditText etEmail;//拓展View内容

    String jsEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews() {
        mTVExportReport = (TextView) findViewById(R.id.tv_export_all);
        mTVExportReport.setOnClickListener(this);

        mTVEnsureExport = (TextView) findViewById(R.id.tv_ensure_export);
        mTVEnsureExport.setOnClickListener(this);

        //拓展窗口
        mAlertViewExt = new AlertView("提示", "请完善你的个人资料！", "取消", null, new String[]{"完成"}, this, AlertView.Style.Alert, this);
        ViewGroup extView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.alertext_form,null);
        etEmail = (EditText) extView.findViewById(R.id.etName);
        mAlertViewExt.addExtView(extView);
    }

    @Override
    protected void createToolbar() {
        mCustomToolbar = (CustomToolbar) findViewById(R.id.custom_toolbar_generate_report);
        mCustomToolbar.setMainTitle(getString(R.string.activity_generate_report_main_title));
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_generate_report;
    }

    @Override
    public GenerateReportPresenter createPresenter() {
        return new GenerateReportPresenter(new GenerateReportModel());
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.tv_export_all) {
            mAlertViewExt.show();
        } else if(v.getId() == R.id.tv_ensure_export) {
            presenter.exportReport();
        }
    }

    @Override
    public void onItemClick(Object o, int position) {
        //判断是否是拓展窗口View，而且点击的是非取消按钮
        if(o == mAlertViewExt && position != AlertView.CANCELPOSITION){
            jsEmail = etEmail.getText().toString();
            if(jsEmail.isEmpty()){
                toast("啥都没填呢", SHORT_SHOW);
            } else if(StringUtils.checkEmaile(jsEmail)) {
                toast("请确认导出学期报表", SHORT_SHOW);
            }
            else {
                 toast("不是正确的邮箱地址", SHORT_SHOW);
            }
            return;
        }
    }

    private Dialog mDialog;

    @Override
    public void showLoadding() {
        mDialog = LoadingDialogUtils.createLoadingDialog(this, "正在导出...");
    }

    @Override
    public void hideLoadding() {
        LoadingDialogUtils.closeDialog(mDialog);
    }

    @Override
    public String getJWAccount() {
        return PrefManager.getString(getApplicationContext(), PrefManager.JW_ACCOUNT, "");
    }

    @Override
    public String getEmail() {
        return jsEmail;
    }

    @Override
    public void showResult(int what, String msg) {
        if(what == EXPORT_SUCCESS) {
            customToast(msg, SHORT_SHOW);
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
            finish();
        } else if(what == EXPORT_FAILED){
            customToast(msg, SHORT_SHOW);
        } else if(what == SHOW_MSG) {
            toast(msg, SHORT_SHOW);
        }
    }
}
