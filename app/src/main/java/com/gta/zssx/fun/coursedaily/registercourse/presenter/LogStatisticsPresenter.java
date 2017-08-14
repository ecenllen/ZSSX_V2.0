package com.gta.zssx.fun.coursedaily.registercourse.presenter;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.fun.coursedaily.registercourse.model.ClassDataManager;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.LogChartBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.LogListBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.LogStatisticsBean;
import com.gta.zssx.fun.coursedaily.registercourse.view.LogStatisticsView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by xiao.peng on 2016/11/9.
 */
public class LogStatisticsPresenter extends BasePresenter<LogStatisticsView> {

    private boolean firstIni = true; //是否第一次加载数据
    private LogStatisticsBean mLogStatisticsBean = new LogStatisticsBean();
    public Subscription mSubscription;
    public Subscription mSubscriptionList;

    /**
     * 日志统计-图表数据
     */
    public void getLogChart(String date) {
        if (getView() == null)
            return;
        getView().showLoadingDialog();

        // 格式化当前时间，并转换为年月日整型数据
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentTime = sdf.format(new Date(System.currentTimeMillis()));
        mSubscription = ClassDataManager.getLogChart(currentTime)
                .subscribe(new Subscriber<LogChartBean>() {
                    @Override
                    public void onCompleted() {
//                        getView().hideDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            getView().emptyUI(mLogStatisticsBean.getChartBean() == null);
                        }
                    }

                    @Override
                    public void onNext(LogChartBean logChartBean) {
                        if (logChartBean == null) {
                            getView().emptyUI(true);
                            return;
                        }
                        //调整顺序
                        Collections.reverse(logChartBean.getLate());
                        Collections.reverse(logChartBean.getVacate());
                        Collections.reverse(logChartBean.getTruant());
                        Collections.reverse(logChartBean.getSabbaticals());
                        //最后面添加一个最小值
                        logChartBean.getLate().add("0");
                        logChartBean.getVacate().add("0");
                        logChartBean.getTruant().add("0");
                        logChartBean.getSabbaticals().add("0");

                        mLogStatisticsBean.setChartBean(logChartBean);
                        getLogList(date);
                    }
                });
    }

    /**
     * 日志统计-部门列表
     */
    public void getLogList(String date) {
        if (getView() == null)
            return;
        getView().showLoadingDialog();

        mSubscriptionList = ClassDataManager.getLogList(date)
                .subscribe(new Subscriber<List<LogListBean>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            List<LogListBean> logListBeen = new ArrayList<>();
                            mLogStatisticsBean.setListBean(logListBeen);
                            getView().showResult(mLogStatisticsBean);
                        }
                    }

                    @Override
                    public void onNext(List<LogListBean> listBean) {
                        mLogStatisticsBean.setListBean(sortList(listBean));
                        getView().showResult(mLogStatisticsBean);
                    }
                });
    }

    private List<LogListBean> sortList(List<LogListBean> list) {
        //记录当前最大值
        int late = 0;
        int vacate = 0;
        int truant = 0;
        int sabbaticals = 0;
        for (int i = 0; i < list.size(); i++) {
            int late1 = Integer.valueOf(list.get(i).getLate());
            late = late > late1 ? late : late1;
            int truant1 = Integer.valueOf(list.get(i).getTruant());
            truant = truant > truant1 ? truant : truant1;
            int vacate1 = Integer.valueOf(list.get(i).getVacate());
            vacate = vacate > vacate1 ? vacate : vacate1;
            int sabbaticals1 = Integer.valueOf(list.get(i).getSabbaticals());
            sabbaticals = sabbaticals > sabbaticals1 ? sabbaticals : sabbaticals1;
        }
        for (int i = 0; i < list.size(); i++) {
            if (late == Integer.valueOf(list.get(i).getLate())) {
                list.get(i).setMaxLate(true);
            }
            if (vacate == Integer.valueOf(list.get(i).getVacate())) {
                list.get(i).setMaxVacate(true);
            }
            if (truant == Integer.valueOf(list.get(i).getTruant())) {
                list.get(i).setMaxTruant(true);
            }
            if (sabbaticals == Integer.valueOf(list.get(i).getSabbaticals())) {
                list.get(i).setMaxSabbaticals(true);
            }
        }
        return list;
    }
}
