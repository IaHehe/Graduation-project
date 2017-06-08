package com.recognizer.classchecks.report.presenter;

import com.library.mvp.BasePresenter;
import com.library.pojo.BasicEntity;
import com.library.utils.StringUtils;
import com.recognizer.classchecks.report.model.Callback;
import com.recognizer.classchecks.report.model.GenerateReportModel;
import com.recognizer.classchecks.report.model.bean.ReportResultBean;
import com.recognizer.classchecks.report.view.GenerateReportView;

/**
 * @author Dongjun Zou
 * @Description ${todo}
 * @email 984147586@qq.com
 * @date 2017/6/1 20:22
 */

public class GenerateReportPresenter extends BasePresenter<GenerateReportView> {
    private GenerateReportModel mGenerateReportModel;

    public GenerateReportPresenter(GenerateReportModel model) {
        this.mGenerateReportModel = model;
    }

    public void exportReport() {

        String jwAccount = getView().getJWAccount();
        if(StringUtils.isEmpty(jwAccount)) {
            getView().showResult(GenerateReportView.SHOW_MSG, "没有获取到教务账号");
            return;
        }
        String email = getView().getEmail();
        if(StringUtils.isEmpty(email)) {
            getView().showResult(GenerateReportView.SHOW_MSG, "没有输入邮箱");
            return;
        }
        getView().showLoadding();
        mGenerateReportModel.exportReport(jwAccount, email, new Callback() {
            @Override
            public void onSuccess(Object obj) {
                getView().hideLoadding();
                ReportResultBean rrb = (ReportResultBean) obj;
                if("0".equals(rrb.getApi_code())) {
                    if(true == rrb.getApi_body()) {
                        getView().showResult(GenerateReportView.EXPORT_SUCCESS, "导出学期报表成功");
                    } else {
                        getView().showResult(GenerateReportView.EXPORT_FAILED, "导出失败");
                    }
                } else {
                    getView().showResult(GenerateReportView.EXPORT_FAILED, "服务器错误");
                }
            }

            @Override
            public void onFailed(Object obj) {
                getView().hideLoadding();
                BasicEntity be = (BasicEntity) obj;
                getView().showResult(GenerateReportView.SHOW_MSG, be.getMessage());
            }
        });
    }


}
