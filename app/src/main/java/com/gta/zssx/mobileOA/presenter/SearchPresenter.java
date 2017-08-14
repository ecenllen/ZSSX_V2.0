package com.gta.zssx.mobileOA.presenter;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.ZSSXApplication;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.mobileOA.model.OAMainModel;
import com.gta.zssx.mobileOA.model.bean.BacklogInfo;
import com.gta.zssx.mobileOA.model.bean.MeetingInfo;
import com.gta.zssx.mobileOA.model.bean.OfficeNoticeInfo;
import com.gta.zssx.mobileOA.view.SearchView;
import com.gta.zssx.mobileOA.view.page.SearchActivity;
import com.gta.zssx.pub.common.Constant;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by xiaoxia.rang on 2016/10/26.
 * 搜索presenter
 */

public class SearchPresenter extends BasePresenter<SearchView> {
    int searchType;
    int backlogType;
    int totalCount;
    int pageIndex;
    boolean isFirst = true;
    int officialType; //公文公告类型
    private UserBean userBean;

    public SearchPresenter() {
        userBean = ZSSXApplication.instance.getUser();
    }

    public Subscription mSubscribe;

    public void getSearchType(SearchActivity activity) {
        Intent intent = activity.getIntent();
        int type = intent.getIntExtra(Constant.KEY_SEARCH_TYPE, 0);
        if (type != 0) {
            searchType = type;
            String hintStr = "";
            switch (searchType) {
                case Constant.SEARCH_TYPE_BACKLOG:
                    backlogType = intent.getIntExtra(Constant.KEY_BACKLOG_TYPE, Constant.BACKLOG_TYPE_UNDO);
                    if (backlogType == Constant.BACKLOG_TYPE_UNDO) {
                        hintStr = "在待办事项中搜索";
                    } else {
                        hintStr = "在已办事项中搜索";
                    }
                    break;
                case Constant.SEARCH_TYPE_MEETING:
                    hintStr = "搜索会议";
                    break;
                case Constant.SEARCH_TYPE_TASK:
                    hintStr = "搜索任务";
                    break;
                case Constant.SEARCH_TYPE_OFFICIAL:
                    int officialType = intent.getIntExtra(Constant.KEY_OFFICIAL_NOTICE_TYPE, Constant.OFFICIAL_NOTICE_TYPE_NOTICE);
                    if (officialType == Constant.OFFICIAL_NOTICE_TYPE_OFFICIAL) {
                        hintStr = "搜索公文";
                    } else {
                        hintStr = "搜索公告";
                    }
                    break;
            }

            getView().showSearchHint(hintStr);
        }
    }

    public int getSearchType() {
        return searchType;
    }

    /**
     * 搜索
     *
     * @param opStatus  上拉还是刷新
     * @param searchStr
     */
    public void doSearch(int opStatus, String searchStr) {
        if (TextUtils.isEmpty(searchStr)) {
            getView().showEmpty();
            return;
        }
        switch (searchType) {
            case Constant.SEARCH_TYPE_BACKLOG:
                searchBacklogs(opStatus, backlogType, searchStr);
                break;
            case Constant.SEARCH_TYPE_MEETING:
                searchMeetings(opStatus, searchStr);
                break;
            case Constant.SEARCH_TYPE_TASK:
                break;
            case Constant.SEARCH_TYPE_OFFICIAL:
                if (officialType == Constant.OFFICIAL_NOTICE_TYPE_NOTICE) {
                    searchOfficialNotice(opStatus, searchStr);
                } else {
                    searchOfficialDocument(opStatus, searchStr);
                }
                break;
        }
    }

    /**
     * 搜索待办/已办事项
     *
     * @param opStatus
     */
    private void searchBacklogs(int opStatus, int backlogType, String searchStr) {
        if (!isViewAttached()) {
            return;
        }
        if (opStatus == Constant.LOAD_MORE) {
            if (Constant.LOAD_DATA_SIZE * pageIndex >= totalCount) {
                getView().onLoadMoreEmpty();
                return;
            } else {
                pageIndex += 1;
            }
        } else {
            pageIndex = 1;
        }
        getView().showLoadingDialog();

        mSubscribe = OAMainModel.getTaskList(userBean.getUserName(), Constant.LOAD_DATA_SIZE, pageIndex, backlogType, null, null, null, null, searchStr).subscribe(new Subscriber<BacklogInfo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().hideDialog();
                    Log.e("taskError", e.toString());
                    getView().showError("请求失败");
                }
            }

            @Override
            public void onNext(BacklogInfo backlogInfo) {
                if (isViewAttached()) {
                    getView().hideDialog();
                    totalCount = backlogInfo.getTotalCount();
                    if (backlogInfo.getBacklogs().isEmpty()) {
                        if (opStatus == Constant.LOAD_MORE) {
                            getView().onLoadMoreEmpty();
                        } else {
                            getView().showEmpty();
                        }
                    } else {
                        if (opStatus == Constant.REFRESH) {
                            getView().setServerTime(backlogInfo.getServerTime());
                            getView().refreshBacklogList(backlogInfo.getBacklogs());
                        } else {
                            getView().appendBacklogList(backlogInfo.getBacklogs());
                        }
                    }
                }
            }
        });
        mCompositeSubscription.add(mSubscribe);
    }


    /**
     * 搜索会议
     *
     * @param opStatus
     * @param searchStr
     */
    private void searchMeetings(int opStatus, String searchStr) {
        if (!isViewAttached()) {
            return;
        }
        if (opStatus == Constant.LOAD_MORE) {
            if (Constant.LOAD_DATA_SIZE * pageIndex >= totalCount) {
                getView().onLoadMoreEmpty();
                return;
            } else {
                pageIndex += 1;
            }
        } else {
            pageIndex = 1;
        }
        getView().showLoadingDialog();
        mSubscribe = OAMainModel.getMeetingList(userBean.getUserId(), pageIndex, Constant.LOAD_DATA_SIZE, searchStr).subscribe(new Subscriber<MeetingInfo>() {
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
                    if (meetingInfo.getMeetings() == null || meetingInfo.getMeetings().isEmpty()) {
                        if (opStatus == Constant.LOAD_MORE) {
                            getView().onLoadMoreEmpty();
                        } else {
                            getView().showEmpty();
                        }
                    } else {
                        totalCount = meetingInfo.getTotalCount();
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

    /**
     * 收索公文
     *
     * @param opStatus
     * @param searchStr
     */
    private void searchOfficialNotice(int opStatus, String searchStr) {
        if (!isViewAttached()) {
            return;
        }
        getView().showLoadingDialog();
        if (opStatus == Constant.LOAD_MORE) {
            pageIndex = 0;
        }

        mSubscribe = OAMainModel.getOfficialData(userBean.getUserId(), pageIndex, Constant.LOAD_DATA_SIZE, searchStr).subscribe(new Subscriber<OfficeNoticeInfo>() {
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
            public void onNext(OfficeNoticeInfo officeNoticeInfo) {
                if (isViewAttached()) {
                    getView().hideDialog();
                    isFirst = false;
                    if (officeNoticeInfo.getOfficeNoticeReminds() == null || officeNoticeInfo.getOfficeNoticeReminds().isEmpty()) {
                        if (opStatus == Constant.LOAD_MORE) {
                            getView().onLoadMoreEmpty();
                        } else {
                            getView().showEmpty();
                        }
                    } else {
                        if (opStatus == Constant.LOAD_MORE) {
                            getView().appendOfficialNoticeList(officeNoticeInfo);
                        } else {
                            getView().refreshOfficialNoticeList(officeNoticeInfo);
                        }
                    }
                }
            }
        });
        mCompositeSubscription.add(mSubscribe);
    }

    /**
     * 搜索公告
     *
     * @param opStatus
     * @param searchStr
     */
    private void searchOfficialDocument(int opStatus, String searchStr) {
        if (!isViewAttached()) {
            return;
        }
        getView().showLoadingDialog();
        if (opStatus == Constant.LOAD_MORE) {
            pageIndex = 0;
        }

        mSubscribe = OAMainModel.getOfficialDocumentData(userBean.getUserId(), pageIndex, Constant.LOAD_DATA_SIZE, searchStr).subscribe(new Subscriber<OfficeNoticeInfo>() {
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
            public void onNext(OfficeNoticeInfo officeNoticeInfo) {
                if (isViewAttached()) {
                    getView().hideDialog();
                    isFirst = false;
                    if (officeNoticeInfo.getOfficeNoticeReminds() == null || officeNoticeInfo.getOfficeNoticeReminds().isEmpty()) {
                        if (opStatus == Constant.LOAD_MORE) {
                            getView().onLoadMoreEmpty();
                        } else {
                            getView().showEmpty();
                        }
                    } else {
                        if (opStatus == Constant.LOAD_MORE) {
                            getView().appendOfficialDocumentList(officeNoticeInfo);
                        } else {
                            getView().refreshOfficialDocumentList(officeNoticeInfo);
                        }
                    }
                }
            }
        });
        mCompositeSubscription.add(mSubscribe);
    }


    public String getUserName() {
        UserBean mUserBean = null;
        try {
            mUserBean = AppConfiguration.getInstance().getUserBean();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mUserBean != null) {
            return mUserBean.getAccount();
        } else {
            return "";
        }
    }
}

