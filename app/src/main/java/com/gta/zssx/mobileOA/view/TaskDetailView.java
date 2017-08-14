package com.gta.zssx.mobileOA.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.mobileOA.model.bean.TaskDetail;

/**
 * Created by xiaoxia.rang on 2016/10/26.
 */

public interface TaskDetailView  extends BaseView{

    void showTaskDetail(TaskDetail detail);

    void showChangeSuccess();

    void  showChangeFail(String msg);
}
