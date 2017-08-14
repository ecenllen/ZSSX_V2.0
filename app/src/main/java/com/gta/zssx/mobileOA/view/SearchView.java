package com.gta.zssx.mobileOA.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.mobileOA.model.bean.Backlog;
import com.gta.zssx.mobileOA.model.bean.Meeting;
import com.gta.zssx.mobileOA.model.bean.OfficeNoticeInfo;

import java.util.List;

/**
 * Created by xiaoxia.rang on 2016/10/26.
 * 搜索View
 */

public interface SearchView extends BaseView {

    void showSearchHint(String str);

    /**
     * 显示数据为空
     */
    void showEmpty();

    /**
     * 设置服务器时间
     * @param serverTime
     */
    void setServerTime(String serverTime);

    /**
     * 显示待办事项
     */
    void refreshBacklogList(List<Backlog> backlogs);

    /**
     * 追加待办事项
     * @param backlogs
     */
    void appendBacklogList(List<Backlog> backlogs);

    /**
     * 显示会议列表
     */
    void refreshMeetingList(List<Meeting> meetingList);

    /**
     * 追加会议列表
     * @param meetingList
     */
    void appendMeetingList(List<Meeting> meetingList);

    /**
     *显示公告
     */
    void refreshOfficialDocumentList(OfficeNoticeInfo officeNoticeInfo);

    /**
     * 追加公告列表
     */
    void appendOfficialDocumentList(OfficeNoticeInfo officeNoticeInfo);

    /**
     *显示公文
     */
    void refreshOfficialNoticeList(OfficeNoticeInfo officeNoticeInfo);

    /**
     * 追加公文列表
     */
    void appendOfficialNoticeList(OfficeNoticeInfo officeNoticeInfo);

    /**
     * 加载更多失败
     */
    void onLoadMoreError ();

    /**
     * 刷新失败
     */
    void onRefreshError ();

    /**
     *
     */
    void onLoadMoreEmpty ();

}
