package com.gta.zssx.fun.coursedaily.registercourse.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.LogStatisticsBean;


/**
 * Created by xiao.peng on 2016/11/9.
 */
public interface LogStatisticsView extends BaseView {

    void showResult(LogStatisticsBean loogStatisticsBean);

    void emptyUI(boolean isEmpty);
}
