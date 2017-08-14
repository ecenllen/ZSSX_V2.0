package com.gta.zssx.fun.coursedaily.registercourse.model.bean;

import java.util.List;

/**
 * Created by xiao.peng on 2016/11/10.
 */
public class LogStatisticsBean {

    private LogChartBean chartBean;

    private List<LogListBean> listBean;

    public LogChartBean getChartBean() {
        return chartBean;
    }

    public void setChartBean(LogChartBean chartBean) {
        this.chartBean = chartBean;
    }

    public List<LogListBean> getListBean() {
        return listBean;
    }

    public void setListBean(List<LogListBean> listBean) {
        this.listBean = listBean;
    }
}
