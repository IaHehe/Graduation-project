package com.recognizer.classchecks.course.presenter;

import com.library.mvp.BasePresenter;
import com.library.pojo.BasicEntity;
import com.library.utils.StringUtils;
import com.recognizer.classchecks.course.model.Callback;
import com.recognizer.classchecks.course.model.ImportTableModel;
import com.recognizer.classchecks.course.model.bean.ImportBean;
import com.recognizer.classchecks.course.view.ImportTableView;

/**
 * @author Dongjun Zou
 * @Description ${todo}
 * @email 984147586@qq.com
 * @date 2017/5/22 17:05
 */

public class ImportTablePresenter extends BasePresenter<ImportTableView> {

    private ImportTableModel model;

    public ImportTablePresenter(ImportTableModel model) {
        this.model = model;
    }

    public void importTable() {
        if(checkViewAttached()) {
            String jwAccount = getView().getJWAccount();
            String jwPwd = getView().getJWPwd();
            if(StringUtils.isEmpty(jwAccount)) {
                getView().showMsg("请输入学号");
                return ;
            }
            if(!StringUtils.isNumeric(jwAccount)) {
                getView().showMsg("输入正确的学号");
                return;
            }
            if(jwAccount.length() < 10 || jwAccount.length() > 10) {
                getView().showMsg("输入正确的学号");
                return;
            }
            if(StringUtils.isEmpty(jwPwd)) {
                getView().showMsg("请输入教务密码");
                return;
            }
            String loginAccount = getView().getLoginAccount();
            if(StringUtils.isEmpty(loginAccount)) {
                return;
            }
            getView().showLoading();
            model.importTable(jwAccount, jwPwd, loginAccount, new Callback() {
                @Override
                public void onSuccess(Object object) {
                    getView().hideLoading();
                    ImportBean bean = (ImportBean) object;
                    if("0".equals(bean.getApi_code())) {
                        getView().showMsg(bean.getApi_message());
                        getView().skip(); // 导入成功后，执行的任务
                    } else if("-1".equals(bean.getApi_message())) {
                        getView().showMsg(bean.getApi_message());
                    }
                }

                @Override
                public void onFailed(Object object) {
                    getView().hideLoading();

                    BasicEntity basicEntity = (BasicEntity) object;
                    getView().showMsg(basicEntity.getMessage());

                }
            });
        }
    }

}
