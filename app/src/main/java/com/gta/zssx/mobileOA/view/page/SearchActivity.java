package com.gta.zssx.mobileOA.view.page;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.mobileOA.model.bean.Backlog;
import com.gta.zssx.mobileOA.model.bean.Meeting;
import com.gta.zssx.mobileOA.model.bean.OfficeNoticeInfo;
import com.gta.zssx.mobileOA.presenter.SearchPresenter;
import com.gta.zssx.mobileOA.view.SearchView;
import com.gta.zssx.mobileOA.view.adapter.BacklogAdapter;
import com.gta.zssx.mobileOA.view.adapter.MeetingAdapter;
import com.gta.zssx.mobileOA.view.adapter.OfficeNoticeAdapter;
import com.gta.zssx.pub.base.BaseActivity;
import com.gta.zssx.pub.common.Constant;
import com.gta.zssx.pub.hfrecyclerview.HFRecyclerView;
import com.gta.zssx.pub.widget.ClearEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 搜索
 */
public class SearchActivity extends BaseActivity<SearchView, SearchPresenter> implements View.OnClickListener, SearchView, HFRecyclerView.HFRecyclerViewListener {

    private ClearEditText clearEditText;
    private TextView tvCancel;
    private HFRecyclerView hfRecyclerView;
    private TextView tvEmpty;
    private String searchStr;

    private List<Backlog> backlogs = new ArrayList<>();
    private List<Meeting> meetings = new ArrayList<>();
    private BacklogAdapter backlogAdapter;
    private MeetingAdapter meetingAdapter;
    private OfficeNoticeAdapter officeNoticeAdapter;
    private int officeNoticeType;

    @NonNull
    @Override
    public SearchPresenter createPresenter() {
        return new SearchPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oa_search);
        inits();
    }

    private void inits() {
        clearEditText = (ClearEditText) findViewById(R.id.et_search);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);
        hfRecyclerView = (HFRecyclerView) findViewById(R.id.rv_list);
        setOnInteractListener();
        tvEmpty = (TextView) findViewById(R.id.tv_emptyHint);
        tvCancel.setOnClickListener(this);
        presenter.getSearchType(SearchActivity.this);

        initAdapter();

        clearEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchStr = s.toString();
                if (TextUtils.isEmpty(searchStr)) {
                    clearSearchResult();
                    return;
                } else {
                    presenter.doSearch(Constant.REFRESH, searchStr);
                }
            }
        });
    }

    private void initAdapter() {
        switch (presenter.getSearchType()) {
            case Constant.SEARCH_TYPE_BACKLOG:
                backlogAdapter = new BacklogAdapter(SearchActivity.this, backlogs);
                hfRecyclerView.setAdapter(backlogAdapter);
                break;
            case Constant.SEARCH_TYPE_MEETING:
                meetingAdapter = new MeetingAdapter(SearchActivity.this, meetings);
                hfRecyclerView.setAdapter(meetingAdapter);
                break;
            case Constant.SEARCH_TYPE_TASK:

                break;
            case Constant.SEARCH_TYPE_OFFICIAL:
                //公文公告
                initOfficeNoticeAdapter();
                break;
        }
    }

    private void initOfficeNoticeAdapter() {
        officeNoticeType = getIntent().getIntExtra(Constant.KEY_OFFICIAL_NOTICE_TYPE, Constant.OFFICIAL_NOTICE_TYPE_NOTICE);
        OfficeNoticeInfo officeNoticeInfo1 = new OfficeNoticeInfo();
        Date d = new Date();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        officeNoticeInfo1.setServerTime(s.format(d));
        List<OfficeNoticeInfo.OfficeNoticeRemindEntity> list = new ArrayList<>();
        officeNoticeInfo1.setOfficeNoticeReminds(list);
        officeNoticeAdapter = new OfficeNoticeAdapter(this, listener, officeNoticeInfo1);
        hfRecyclerView.setAdapter(officeNoticeAdapter);
    }

    OfficeNoticeAdapter.Listener listener = new OfficeNoticeAdapter.Listener() {
        @Override
        public void onClickListener(OfficeNoticeInfo.OfficeNoticeRemindEntity remind, int position) {
//            OfficialNoticeDetailActivity.start(SearchActivity.this, officeNoticeType, remind.getId());
            Intent intent = new Intent(SearchActivity.this, BacklogDetailActivity.class);
            intent.putExtra("id", remind.getId());
            intent.putExtra("runId", remind.getRunId());
            intent.putExtra("position", position);
            intent.putExtra("status", remind.getStatus());
//            if(type == SHOW_TITLE_PAGE_NOTICE){
//                intent.putExtra("type", Constant.DETAIL_TYPE_NOTICE);
//            }else {
            intent.putExtra("type", Constant.DETAIL_TYPE_DOC);
//            }
            startActivity(intent);
        }
    };

    private void setOnInteractListener() {
        hfRecyclerView.setCanLoadMore(true);
        hfRecyclerView.setCanRefresh(true);
        hfRecyclerView.setFooterViewText("");
        hfRecyclerView.setRecyclerViewListener(this);
    }

    @Override
    public void onClick(View v) {
        onBackPressed();
    }

    @Override
    public void showSearchHint(String str) {
        clearEditText.setHint(str);
    }

    @Override
    public void showEmpty() {
        hfRecyclerView.stopRefresh(false);
        tvEmpty.setVisibility(View.VISIBLE);
        hfRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void setServerTime(String serverTime) {
        if (backlogAdapter != null) {
            backlogAdapter.setServerTime(serverTime);
        }
        if (meetingAdapter != null) {
            meetingAdapter.setServerTime(serverTime);
        }
    }

    @Override
    public void refreshBacklogList(List<Backlog> backlogsList) {
        tvEmpty.setVisibility(View.GONE);
        hfRecyclerView.setVisibility(View.VISIBLE);
        hfRecyclerView.stopRefresh(true);
        backlogs.clear();
        backlogs.addAll(backlogsList);
        backlogAdapter.notifyDataSetChanged();
    }

    @Override
    public void appendBacklogList(List<Backlog> backlogsList) {
        hfRecyclerView.stopLoadMore(true);
        backlogs.addAll(backlogsList);
        backlogAdapter.notifyItemRangeChanged(backlogs.size(), backlogsList.size());
    }

    @Override
    public void refreshMeetingList(List<Meeting> meetingList) {
        tvEmpty.setVisibility(View.GONE);
        hfRecyclerView.setVisibility(View.VISIBLE);
        hfRecyclerView.stopRefresh(true);
        meetingAdapter.refreshData(meetingList);
    }

    @Override
    public void appendMeetingList(List<Meeting> meetingList) {
        hfRecyclerView.stopLoadMore(true);
        meetingAdapter.appendData(meetingList);
    }

    @Override
    public void refreshOfficialDocumentList(OfficeNoticeInfo officeNoticeInfo) {
        tvEmpty.setVisibility(View.GONE);
        hfRecyclerView.setVisibility(View.VISIBLE);
        hfRecyclerView.stopRefresh(true);
        officeNoticeAdapter.refreshData(officeNoticeInfo);
        tvEmpty.setVisibility(View.GONE);

    }

    @Override
    public void appendOfficialDocumentList(OfficeNoticeInfo officeNoticeInfo) {
        hfRecyclerView.stopLoadMore(true);
        officeNoticeAdapter.setData(officeNoticeInfo);
    }

    @Override
    public void refreshOfficialNoticeList(OfficeNoticeInfo officeNoticeInfo) {
        tvEmpty.setVisibility(View.GONE);
        hfRecyclerView.setVisibility(View.VISIBLE);
        hfRecyclerView.stopRefresh(true);
        officeNoticeAdapter.refreshData(officeNoticeInfo);
        tvEmpty.setVisibility(View.GONE);
    }

    @Override
    public void appendOfficialNoticeList(OfficeNoticeInfo officeNoticeInfo) {
        hfRecyclerView.stopLoadMore(true);
        officeNoticeAdapter.setData(officeNoticeInfo);
    }

    public void clearSearchResult() {
        switch (presenter.getSearchType()) {
            case Constant.SEARCH_TYPE_BACKLOG:
                backlogs.clear();
                backlogAdapter.notifyDataSetChanged();
                break;
            case Constant.SEARCH_TYPE_MEETING:
                meetings.clear();
                meetingAdapter.notifyDataSetChanged();
                break;
            case Constant.SEARCH_TYPE_TASK:

                break;
            case Constant.SEARCH_TYPE_OFFICIAL:
                //公文公告
                officeNoticeAdapter.clearData();
                break;
        }
    }

    @Override
    public void onLoadMoreError() {
        hfRecyclerView.stopLoadMore(false);
    }

    @Override
    public void onRefreshError() {
        hfRecyclerView.stopRefresh(false);
    }

    @Override
    public void onLoadMoreEmpty() {
        hfRecyclerView.stopLoadMore(false);
        hfRecyclerView.setFooterViewText("无更多信息");
    }

    @Override
    public void onRefresh() {
        presenter.doSearch(Constant.REFRESH, searchStr);
    }

    @Override
    public void onLoadMore() {
        presenter.doSearch(Constant.LOAD_MORE, searchStr);
    }
}
