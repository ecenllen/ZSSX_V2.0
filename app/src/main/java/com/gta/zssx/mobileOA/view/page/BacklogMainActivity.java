package com.gta.zssx.mobileOA.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.mobileOA.model.bean.Backlog;
import com.gta.zssx.mobileOA.presenter.BacklogMainPresenter;
import com.gta.zssx.mobileOA.view.BacklogMainView;
import com.gta.zssx.mobileOA.view.adapter.BacklogAdapter;
import com.gta.zssx.mobileOA.view.adapter.ui.titlePopupWindow;
import com.gta.zssx.pub.base.BaseActivity;
import com.gta.zssx.pub.common.Constant;
import com.gta.zssx.pub.hfrecyclerview.HFRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 待办/已办
 */
public class BacklogMainActivity extends BaseActivity<BacklogMainView, BacklogMainPresenter> implements BacklogMainView, HFRecyclerView.HFRecyclerViewListener {

    public static final int BACKLOG_UPDATE_RESULT_CODE = 12;
    private Toolbar toolbar;
    private ImageView ivSearch;
    private TextView titleTextView;
    private titlePopupWindow mTitlePopuWindow;
    private HFRecyclerView hfRecyclerView;
    private TextView tvEmpty;
    private List<Backlog> backlogs = new ArrayList<>();
    private BacklogAdapter adapter;
    private String[] titles = {"待办事项", "已办事项"};
    private int backlogType = Constant.BACKLOG_TYPE_UNDO;

    public static void start(Context context) {
        final Intent lIntent = new Intent(context, BacklogMainActivity.class);
//        Bundle lBundle = new Bundle();
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }

    @NonNull
    @Override
    public BacklogMainPresenter createPresenter() {
        return new BacklogMainPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oa_backlog_main);
        inits();
    }

    private void inits() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        titleTextView = (TextView) findViewById(R.id.tv_select_title);
        ivSearch = (ImageView) findViewById(R.id.iv_search);
        hfRecyclerView = (HFRecyclerView) findViewById(R.id.rv_backlogs);
        setOnInteractListener();
        tvEmpty = (TextView) findViewById(R.id.tv_emptyHint);
        titleTextView.setText(titles[0]);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BacklogMainActivity.this, SearchActivity.class);
                intent.putExtra(Constant.KEY_SEARCH_TYPE, Constant.SEARCH_TYPE_BACKLOG);
                intent.putExtra(Constant.KEY_BACKLOG_TYPE, backlogType);
                startActivity(intent);
            }
        });
        titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTitleItems();
            }
        });
        adapter = new BacklogAdapter(BacklogMainActivity.this, backlogs);
        hfRecyclerView.setAdapter(adapter);

        presenter.getBacklogs(Constant.REFRESH,backlogType);
    }

    private void showTitleItems() {
        String title = titleTextView.getText().toString();
        mTitlePopuWindow = new titlePopupWindow(this, titles, title, new titlePopupWindow.Listener() {
            @Override
            public void onPopupWindowDismissListener() {
                //无操作

            }

            @Override
            public void onItemClickListener(int position) {
                if (title.equals(titles[position]))
                    return;
                titleTextView.setText(titles[position]);
                if (position == 0) {
                    backlogType = Constant.BACKLOG_TYPE_UNDO;
                } else {
                    backlogType = Constant.BACKLOG_TYPE_FINISH;
                }
                presenter.getBacklogs(Constant.REFRESH,backlogType);
            }
        });
        mTitlePopuWindow.showAsDropDown(titleTextView);
    }

    private void setOnInteractListener() {
        hfRecyclerView.setCanLoadMore(true);
        hfRecyclerView.setCanRefresh(true);
        hfRecyclerView.setFooterViewText("");
        hfRecyclerView.setRecyclerViewListener(this);
    }

    @Override
    public void setServerTime(String serverTime) {
        adapter.setServerTime(serverTime);
    }

    @Override
    public void refreshBacklogList(List<Backlog> backlogsList) {
        hfRecyclerView.stopRefresh(true);
        backlogs.clear();
        backlogs.addAll(backlogsList);
        adapter.notifyDataSetChanged();
        tvEmpty.setVisibility(View.GONE);
        hfRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void appendBacklogList(List<Backlog> backlogsList) {
        hfRecyclerView.stopLoadMore(true);
        backlogs.addAll(backlogsList);
        adapter.notifyItemRangeChanged(backlogs.size(),backlogsList.size());
    }

    @Override
    public void onRefresh() {
        presenter.getBacklogs(Constant.REFRESH,backlogType);
    }

    @Override
    public void onLoadMore() {
        presenter.getBacklogs(Constant.LOAD_MORE,backlogType);
    }


    @Override
    public void showEmpty() {
        hfRecyclerView.stopRefresh(false);
        tvEmpty.setVisibility(View.VISIBLE);
        hfRecyclerView.setVisibility(View.GONE);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        // 进行同意或驳回，刷新界面
        if(resultCode == BACKLOG_UPDATE_RESULT_CODE) {
            onRefresh();
        }
            
        
    }
}
