package com.recognizer.classchecks.home.view;

import com.library.mvp.BaseView;

/**
 * @author Dongjun Zou
 * @Description ${todo}
 * @email 984147586@qq.com
 * @date 2017/6/1 01:51
 */

public interface HomeView extends BaseView {

    int CAN_OK_CLOCKIN = 1; // 查询当前有课，可以考勤
    int CURR_NO_COURSE = 2; // 没有课，不能考勤
    int SHOW_MSG = 3; // 显示消息
    int NOT_IMPORT_COURSE = 4; // 没有导入课表

    String getJWAccount(); // 获取教务账号
    void showResult(int what, String msg);

}
