package com.gta.zssx.mobileOA.presenter;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.ZSSXApplication;
import com.gta.zssx.mobileOA.model.OAMainModel;
import com.gta.zssx.mobileOA.model.bean.SemessterAndWeeklyInfo;
import com.gta.zssx.mobileOA.model.bean.TermInfo;
import com.gta.zssx.mobileOA.model.bean.TermWeekInfo;
import com.gta.zssx.mobileOA.view.SemesterScheduleView;
import com.gta.zssx.pub.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by lan.zheng on 2016/11/2.
 */
public class SemesterSchedulePresenter extends BasePresenter<SemesterScheduleView> {
    public Subscription mSubscribe;
    private Subscription mSubscribeSemester;
    private Subscription mSubscribeWeek;
    private Subscription mSubscribeDetail;
    private String UserId;

    public SemesterSchedulePresenter() {
        UserId = ZSSXApplication.instance.getUser().getUserId();
    }

    /**
     * 获取学期选项列表
     */
    public void getSemesterData() {
        if (!isViewAttached()) {
            return;
        }
        getView().showLoadingDialog();
        mSubscribeSemester = OAMainModel.getSemesterList()
                .subscribe(new Subscriber<List<TermInfo>>() {
                    @Override
                    public void onCompleted() {
                        if(isViewAttached()){
                            getView().hideDialog();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(!isViewAttached()){
                            return;
                        }
                        getView().hideDialog();
                        getView().onErrorHandle(e);
                        getView().showAllEmptyPageWhenGetSemesterFailed();
                    }

                    @Override
                    public void onNext(List<TermInfo> termInfos) {
                        if(!isViewAttached()){
                            return;
                        }
                        getView().getSemesterData(termInfos);
                    }
                });
        mCompositeSubscription.add(mSubscribeSemester);
    }

    /**
     *获取周选项列表
     */
    public void getWeekData(int termId) {
        if (!isViewAttached()) {
            return;
        }
        getView().showLoadingDialog();
        mSubscribeWeek = OAMainModel.getSemesterWeekList(termId)
                .subscribe(new Subscriber<List<TermWeekInfo>>() {

                    @Override
                    public void onCompleted() {
                        if(isViewAttached()){
                            getView().hideDialog();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (!isViewAttached()) {
                            return;
                        }
                        getView().hideDialog();
                        getView().onErrorHandle(e);
                        getView().showListEmptyPage(false);
                    }

                    @Override
                    public void onNext(List<TermWeekInfo> termWeekInfos) {
                        if (!isViewAttached()) {
                            return;
                        }
                        getView().getWeekData(termWeekInfos.get(0));
                    }
                });
        mCompositeSubscription.add(mSubscribeWeek);
    }

    /**
     * 获取学期计划内容列表
     */
    public void getDetailInfoData(String BeginDate, String EndDate,boolean isRefresh) {
        if (!isViewAttached()) {
            return;
        }
        getView().showLoadingDialog();
        //TODO 获取学期计划
        mSubscribeDetail = OAMainModel.getWeekPlanList(UserId,BeginDate,EndDate)
                .subscribe(new Subscriber<List<SemessterAndWeeklyInfo>>() {

                    @Override
                    public void onCompleted() {
                        if(isViewAttached()){
                            getView().hideDialog();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (!isViewAttached()) {
                            return;
                        }
                        getView().hideDialog();
                        getView().onErrorHandle(e);

                        if(!isRefresh){  //不是刷新才显示空页面
                            getView().showListEmptyPage(true);
                        }else {  //刷新失败保留该页面
                            getView().onRefreshError();
                        }

                    }

                    @Override
                    public void onNext(List<SemessterAndWeeklyInfo> infoList) {
                        if (!isViewAttached()) {
                            return;
                        }
                        getView().hideDialog();
                        LogUtil.Log("lenita","infoList.size = "+infoList.size());
                        getView().getWeeklyScheduleData(infoList);
                    }
                });
        mCompositeSubscription.add(mSubscribeDetail);
    }

    public List<String> getSemesterStringList(List<TermInfo> termInfos){
        List<String> list = new ArrayList<>();
        for(int i = 0;i < termInfos.size();i++){
            String term = termInfos.get(i).getSemesterName();
            list.add(term);
        }
        return list;
    }

    public int getCurrentSemesterPosition(List<TermInfo> termInfos){
        int position = 0;
        for(int i = 0;i < termInfos.size();i++){
            if(termInfos.get(i).getIsCurrentSemester()){
                position = i;
            }
        }
        return position;
    }

    public List<String> getWeekStringList(List<TermWeekInfo.WeekEntity> weekEntities){
        List<String> list = new ArrayList<>();
        for(int i = 0;i < weekEntities.size();i++){
            String beginDate = weekEntities.get(i).getWeekStartDate();
            String endDate = weekEntities.get(i).getWeekEndDate();
            String date = datePeroid(beginDate,endDate);
            String week = "第"+weekEntities.get(i).getWeek()+"周"+date;
            list.add(week);
        }
        return list;
    }

    private String datePeroid(String beginDate,String endDate){
        String date = "";
        String[] begin = beginDate.split("-");
        String[] end = endDate.split("-");
        if(begin.length == 3 && end.length == 3){
            date = "（"+begin[1]+"/"+begin[2]+"-"+end[1]+"/"+end[2]+"）";
        }
        return date;
    }

    public ArrayList<String> semesterData(){
        ArrayList<String> list = new ArrayList<>();
        list.add("2016-2017-1");
        list.add("2016-2017-2");
        list.add("2016-2017-3");
        list.add("2016-2017-4");
        list.add("2016-2017-5");
        return list;
    }

    public ArrayList<String> weekData(){
        ArrayList<String> list = new ArrayList<>();
        list.add("第1周");
        list.add("第2周");
        list.add("第3周");
        list.add("第4周");
        list.add("第5周");
        return list;
    }
}
