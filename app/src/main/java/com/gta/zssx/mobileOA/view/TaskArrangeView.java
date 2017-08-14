package com.gta.zssx.mobileOA.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.mobileOA.model.bean.TaskArrange;

import java.util.List;

/**
 * Created by xiaoxia.rang on 2016/11/4.
 * 任务安排
 */

public interface TaskArrangeView extends BaseView {
    void showTaskArranges(List<TaskArrange> arranges);
    void showToast(String msg);
    void showUnCompleteDialog();
}
