package com.gta.zssx.mobileOA.presenter;

import android.util.Log;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.ZSSXApplication;
import com.gta.zssx.mobileOA.model.OAMainModel;
import com.gta.zssx.mobileOA.model.bean.MeetingInfo;
import com.gta.zssx.mobileOA.view.MeetingMainView;
import com.gta.zssx.pub.common.Constant;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by xiaoxia.rang on 2016/10/26.
 * 会议Presenter
 */

public class MeetingMainPresenter extends BasePresenter<MeetingMainView> {

    private int pageIndex = 1;
    private int totalCount;
    boolean isFirst = true;
    public Subscription mSubscribe;
    private String UserId;

    public MeetingMainPresenter() {
        UserId = ZSSXApplication.instance.getUser().getUserId();
    }


    public void getMeetingList(int opStatus) {
        if (!isViewAttached()) {
            return;
        }
        if (opStatus == Constant.REFRESH) {
            pageIndex = 1;
        } else {
            if (Constant.LOAD_DATA_SIZE * pageIndex >= totalCount) {
                getView().onLoadMoreEmpty();
                return;
            } else {
                pageIndex += 1;
            }
        }
        getView().showLoadingDialog();
        mSubscribe = OAMainModel.getMeetingList(UserId, pageIndex, Constant.LOAD_DATA_SIZE, null).subscribe(new Subscriber<MeetingInfo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().hideDialog();
                    Log.e("error", e.toString());
                    if (opStatus == Constant.LOAD_MORE) {
                        getView().onLoadMoreError();
                    } else {
                        if (isFirst) {
                            getView().showError("请求失败");
                        } else {
                            getView().onRefreshError();
                        }
                    }
                    isFirst = false;
                }
            }

            @Override
            public void onNext(MeetingInfo meetingInfo) {
                if (isViewAttached()) {
                    getView().hideDialog();
                    isFirst = false;
                    totalCount = meetingInfo.getTotalCount();

                    if (meetingInfo.getMeetings() == null || meetingInfo.getMeetings().isEmpty()) {
                        if (opStatus == Constant.LOAD_MORE) {
                            getView().onLoadMoreEmpty();
                        } else {
                            getView().showEmpty();
                        }
                    } else {
                        if (opStatus == Constant.LOAD_MORE) {
                            getView().appendMeetingList(meetingInfo.getMeetings());
                        } else {
                            getView().setServerTime(meetingInfo.getServerTime());
                            getView().refreshMeetingList(meetingInfo.getMeetings());
                        }
                    }

                }
            }
        });
        mCompositeSubscription.add(mSubscribe);
    }
}
