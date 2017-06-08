package com.recognizer.classchecks.course;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.library.mvp.BaseActivity;
import com.library.widget.CustomToolbar;
import com.library.widget.LoadingDialogUtils;
import com.recognizer.R;
import com.recognizer.classchecks.course.model.ImportTableModel;
import com.recognizer.classchecks.course.presenter.ImportTablePresenter;
import com.recognizer.classchecks.course.view.ImportTableView;
import com.recognizer.common.global.pref.PrefManager;

public class ImportTableActivity extends BaseActivity<ImportTableView, ImportTablePresenter>
        implements ImportTableView, View.OnClickListener {

    private EditText etJWAccount;
    private EditText etJWPwd;
    private Button btnImportTable;

    private Dialog mDialog;

    private String mJWAccount; // 教务账号ImportTableView

    private void initViews() {
        etJWAccount = (EditText) findViewById(R.id.et_teacher_jw_account);
        etJWPwd = (EditText) findViewById(R.id.et_teacher_jw_pwd);
        btnImportTable = (Button) findViewById(R.id.btn_import_table);
        btnImportTable.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    @Override
    public ImportTablePresenter createPresenter() {
        return new ImportTablePresenter(new ImportTableModel());
    }

    @Override
    protected void createToolbar() {
        mCustomToolbar = (CustomToolbar) findViewById(R.id.custom_toolbar_import_table);
        mCustomToolbar.setMainTitle(getString(R.string.activity_import_table_main_title));
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_import_table;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_import_table:
            {
                mJWAccount = etJWAccount.getText().toString();
                presenter.importTable(); // 执行导入课表
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void showLoading() {
        mDialog = LoadingDialogUtils.createLoadingDialog(this, "正在导入...");
    }

    @Override
    public void hideLoading() {
        LoadingDialogUtils.closeDialog(mDialog);
    }

    @Override
    public String getJWAccount() {
        return etJWAccount.getText().toString();
    }

    @Override
    public String getJWPwd() {
        return etJWPwd.getText().toString();
    }

    @Override
    public void showMsg(String msg) {
        customToast(msg, SHORT_SHOW);
    }

    @Override
    public String getLoginAccount() {
        return PrefManager.getString(getApplicationContext(), PrefManager.LOGIN_ACCOUNT, "");
    }

    /**
     * 课表导入成功后，跳转回课表页面
     */
    @Override
    public void skip() {
        PrefManager.setString(getApplicationContext(), PrefManager.JW_ACCOUNT, mJWAccount);
        Intent intent = new Intent(this, CourseActivity.class);
        startActivity(intent);
        finish();
        //onBackPressed();
    }

    @Override
    public void showResult(int what, String msg) {
        if(what == NOT_OBTAIN_LOGIN_ACCOUNT) {
            customToast(msg, SHORT_SHOW);
        }
    }
}
