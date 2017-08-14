package com.gta.zssx.mobileOA.presenter;

import android.util.Log;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.ZSSXApplication;
import com.gta.zssx.mobileOA.model.OAMainModel;
import com.gta.zssx.mobileOA.model.bean.OfficeNoticeInfo;
import com.gta.zssx.mobileOA.view.OfficailRemindView;
import com.gta.zssx.pub.common.Constant;
import com.gta.zssx.pub.exception.CustomException;

import rx.Subscriber;
import rx.Subscription;


/**
 * Created by lan.zheng on 2016/11/3.
 */
public class OfficialRemindPresenter extends BasePresenter<OfficailRemindView> {
    public Subscription mSubscribe;
    public Subscription mSubscribeDocument;
    public static final int REFRESH = 0;
    public static final int MORE = 1;
    private String UserId;

    public OfficialRemindPresenter() {
        UserId = ZSSXApplication.instance.getUser().getUserId();
    }


    public void getOfficialData(int action, int pageIndex) {
        if (!isViewAttached()) {
            return;
        }
        getView().showLoadingDialog();
        mSubscribe = OAMainModel.getOfficialData(UserId, pageIndex, Constant.LOAD_DATA_SIZE, null)
                .subscribe(new Subscriber<OfficeNoticeInfo>() {
                    @Override
                    public void onCompleted() {
                        if (isViewAttached()) {
                            getView().hideDialog();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        if (!isViewAttached()) {
                            return;
                        }
                        getView().hideDialog();
                        if (throwable instanceof CustomException) {   //抛出自定义的异常
                            CustomException lE = (CustomException) throwable;
                            if (lE.getCode() == CustomException.NETWORK_UNAVAILABLE) {
                                getView().showError(throwable.getMessage());
                            } else {
                                getView().onErrorHandle(throwable);  //抛出默认异常
                            }
                        } else {
                            getView().onErrorHandle(throwable);  //抛出默认异常
                        }
                        //返回界面显示
                        if (action == REFRESH) {
                            getView().onRefreshError();
                        } else {
                            getView().onLoadMoreError();
                        }
                    }

                    @Override
                    public void onNext(OfficeNoticeInfo officeNoticeInfo) {
                        Log.e("notice", officeNoticeInfo.toString());
                        if (!isViewAttached()) {
                            return;
                        }
                        if (officeNoticeInfo.getOfficeNoticeReminds().size() == 0) {
                            if (action == REFRESH) {
                                getView().showEmptyView();
                            } else {
                                getView().onLoadMoreEmpty();
                            }
                        } else {
                            getView().showRemindList(officeNoticeInfo, action);
                        }

                    }
                });
        mCompositeSubscription.add(mSubscribe);
    }

    public void getOfficialDocumentData(int action, int pageIndex) {
        if (!isViewAttached()) {
            return;
        }
        getView().showLoadingDialog();
        mSubscribeDocument = OAMainModel.getOfficialDocumentData(UserId, pageIndex, Constant.LOAD_DATA_SIZE, null)
                .subscribe(new Subscriber<OfficeNoticeInfo>() {
                    @Override
                    public void onCompleted() {
                        if (isViewAttached()) {
                            getView().hideDialog();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        if (!isViewAttached()) {
                            return;
                        }
                        getView().hideDialog();
                        if (throwable instanceof CustomException) {   //抛出自定义的异常
                            CustomException lE = (CustomException) throwable;
                            if (lE.getCode() == CustomException.NETWORK_UNAVAILABLE) {
                                getView().showError(throwable.getMessage());
                            } else {
                                getView().onErrorHandle(throwable);  //抛出默认异常
                            }
                        } else {
                            getView().onErrorHandle(throwable);  //抛出默认异常
                        }
                        //返回界面显示
                        if (action == REFRESH) {
                            getView().onRefreshError();
                        } else {
                            getView().onLoadMoreError();
                        }
                    }

                    @Override
                    public void onNext(OfficeNoticeInfo officeNoticeInfo) {
                        if (!isViewAttached()) {
                            return;
                        }
                        if (officeNoticeInfo.getOfficeNoticeReminds().size() == 0) {
                            if (action == REFRESH) {
                                getView().showEmptyView();
                            } else {
                                getView().onLoadMoreEmpty();
                            }
                        } else {
                            getView().showRemindList(officeNoticeInfo, action);
                        }
                    }
                });
        mCompositeSubscription.add(mSubscribeDocument);
    }

    public void setMessageRead(int id, int position) {
        if (getView() == null)
            return;
        getView().showLoadingDialog();
        mCompositeSubscription.add(OAMainModel.setMeetingRead(UserId, id)
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        getView().hideDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            getView().hideDialog();
                            getView().onErrorHandle(e);
                        }
                    }

                    @Override
                    public void onNext(String s) {
                        getView().setReadSuccessful(id, position);
                    }
                }));
    }

    /*private String getNewContent(String htmltext){

        Document doc=Jsoup.parse(htmltext);
        Elements elements=doc.getElementsByTag("img");
        for (Element element : elements) {
            element.attr("width","100%").attr("height","auto");
        }

        return doc.toString();
    }*/

//    public OfficeNoticeInfo testData(){
//        OfficeNoticeInfo eventNoticeInfo = new OfficeNoticeInfo();
//        OfficeNoticeInfo.OfficeNoticeRemindEntity remind1 = new OfficeNoticeInfo.OfficeNoticeRemindEntity();
//        OfficeNoticeInfo.OfficeNoticeRemindEntity remind2 = new OfficeNoticeInfo.OfficeNoticeRemindEntity();
//
//        remind1.setContent("未读学校公告：XX请假流程结束 1流程结束：XX请假流程结束 流程结束：XX请假流程结束");
//        remind1.setTime("2016-11-22 13:37");
//        remind1.setId(1);
//        remind1.setStatus(0);
//        remind2.setContent("未读学校公告：XX请假流程结束 2流程结束：XX请假流程结束 流程结束：XX请假流程结束");
//        remind2.setTime("2016-11-13 13:37");
//        remind2.setId(2);
//        remind2.setStatus(1);
//        List<OfficeNoticeInfo.OfficeNoticeRemindEntity> list = new ArrayList<>();
//        list.add(remind1);
//        list.add(remind1);
//        for(int i = 0;i < 6;i++){
//            list.add(remind2);
//        }
//        eventNoticeInfo.setOfficeNoticeReminds(list);
//        eventNoticeInfo.setServerTime("2016-11-22 10:37:00");
//        return eventNoticeInfo;
//    }
//
//    public OfficeNoticeInfo testDataOfficial(){
//        OfficeNoticeInfo eventNoticeInfo = new OfficeNoticeInfo();
//        OfficeNoticeInfo.OfficeNoticeRemindEntity remind1 = new OfficeNoticeInfo.OfficeNoticeRemindEntity();
//        OfficeNoticeInfo.OfficeNoticeRemindEntity remind2 = new OfficeNoticeInfo.OfficeNoticeRemindEntity();
//
//        remind1.setContent("未读学校公文：XX请假流程结束 1流程结束：XX请假流程结束 流程结束：XX请假流程结束");
//        remind1.setTime("2016-11-19 13:37");
//        remind1.setId(11);
//        remind1.setStatus(0);
//        remind2.setContent("未读学校公文：XX请假流程结束 2流程结束：XX请假流程结束 流程结束：XX请假流程结束");
//        remind2.setTime("2016-10-30 13:37");
//        remind2.setId(12);
//        remind2.setStatus(1);
//
//        List<OfficeNoticeInfo.OfficeNoticeRemindEntity> list = new ArrayList<>();
//        list.add(remind1);
//        list.add(remind1);
//        for(int i = 0;i < 6;i++){
//            list.add(remind2);
//        }
//        eventNoticeInfo.setOfficeNoticeReminds(list);
//        eventNoticeInfo.setServerTime("2016-11-22 10:37:00");
//        return eventNoticeInfo;
//    }
}
