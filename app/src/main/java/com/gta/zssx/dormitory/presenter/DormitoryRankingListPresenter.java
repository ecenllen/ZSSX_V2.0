package com.gta.zssx.dormitory.presenter;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.dormitory.model.DormitoryModel;
import com.gta.zssx.dormitory.model.bean.DormitoryOrClassSingleInfoBean;
import com.gta.zssx.dormitory.model.bean.DormitoryRankingBean;
import com.gta.zssx.dormitory.view.DormitoryRankingListView;
import com.gta.zssx.dormitory.view.page.DormitoryRankingListFragment;
import com.gta.zssx.pub.common.Constant;
import com.gta.zssx.pub.exception.CustomException;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by lan.zheng on 2017/6/26.
 */

public class DormitoryRankingListPresenter extends BasePresenter<DormitoryRankingListView>{
    public Subscription mSubscribeList;
    public Subscription mDeleteOrSubmitSubscribeList;
    public static final int PageSize = 10;

    /**
     * 获取列表
     * @param UserId  用户Id
     * @param SendToAuditState 状态
     * @param PageIndex 分页，第几页
     */
    public void getRankingList(final String UserId,int SendToAuditState,int PageIndex,int Action) {
        if (!isViewAttached()) {
            return;
        }
        getView().showLoadingDialog();
        mSubscribeList = DormitoryModel.getDormitoryRankingList(UserId,SendToAuditState,PageIndex, PageSize)
                .subscribe(new Subscriber<List<DormitoryRankingBean>>() {

                    @Override
                    public void onCompleted() {
                        if(isViewAttached()) {
                            getView().hideDialog();
                        }

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        if(isViewAttached()) {
                            getView().hideDialog();
                            if (throwable instanceof CustomException) {   //抛出自定义的异常
                                CustomException lE = (CustomException) throwable;
                                if (lE.getCode() == CustomException.NETWORK_UNAVAILABLE) {
                                    getView().showError(throwable.getMessage());
                                }else {
                                    getView().onErrorHandle(throwable);  //抛出默认异常
                                }
                            }else {
                                getView().onErrorHandle(throwable);  //抛出默认异常
                            }
                            if(Action == DormitoryRankingListFragment.ACTION_REFRESH) {
                                getView().showEmpty(Action, false);
                            }
                        }

                    }

                    @Override
                    public void onNext(List<DormitoryRankingBean> dormitoryRankingBeanList) {
                        if(isViewAttached()) {
                            if( dormitoryRankingBeanList == null || dormitoryRankingBeanList.size() == 0 ){
                                if(Action == DormitoryRankingListFragment.ACTION_REFRESH){
                                    //刷新的显示空页面
                                    getView().showEmpty(Action, true);
                                }else {
                                    //弹出提示五更多数据
                                    getView().showToast("无更多数据",Action);
                                }
                            }else {
                                getView().getListSuccess(dormitoryRankingBeanList,Action);
                            }
                        }
                    }
                });
        mCompositeSubscription.add(mSubscribeList);
    }

    public void deleteOrSubmitRankingListItem(int RecordId,int ActionType,String UserId) {
        if (!isViewAttached()) {
            return;
        }
        getView().showLoadingDialog();
        mDeleteOrSubmitSubscribeList = DormitoryModel.deleteOrSubmitRecord(RecordId,ActionType,UserId)
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        if(isViewAttached()) {
                            getView().hideDialog();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        if(isViewAttached()) {
                            getView().hideDialog();
//                            getView().onErrorHandle(throwable);
                            String msg = "";
                            if(ActionType == Constant.DORMITORY_ACTION_SUBMIT){
                                msg = "送审失败";
                            }else {
                                msg = "删除失败";
                            }
                            getView().showToast(msg,ActionType);
                        }
                    }

                    @Override
                    public void onNext(String s) {
                        if(isViewAttached()) {
                            getView().deleteOrSubmitSuccess(ActionType);
                        }
                    }
                });
    }
}
