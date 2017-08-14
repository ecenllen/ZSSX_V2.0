package com.gta.zssx.mobileOA.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.gta.zssx.R;
import com.gta.zssx.mobileOA.model.bean.TaskDetail;
import com.gta.zssx.mobileOA.presenter.TaskDetailPresenter;
import com.gta.zssx.mobileOA.view.TaskDetailView;
import com.gta.zssx.mobileOA.view.adapter.SubTaskAdapter;
import com.gta.zssx.pub.base.BaseActivity;
import com.gta.zssx.pub.manager.ToolBarManager;

/**
 * 我的任务->任务详情
 */
public class TaskDetailActivity extends BaseActivity<TaskDetailView, TaskDetailPresenter> implements TaskDetailView, View.OnClickListener {
    Toolbar mToolbar;
    TextView tvTopic;
    TextView tvDept;
    TextView tvStartTime;
    TextView tvEndTime;
    TextView tvGoal;
    TextView tvContent;
    ImageView ivAttachments;
    RecyclerView rvSubtasks;
    TextView tvStart;
    TextView tvfinish;
    TextView tvArrange;
    ToolBarManager mToolBarManager;

    SubTaskAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oa_task_detail);
        inits();
    }

    @NonNull
    @Override
    public TaskDetailPresenter createPresenter() {
        return new TaskDetailPresenter();
    }

    public static void start(Context context) {
        final Intent lIntent = new Intent(context, TaskDetailActivity.class);
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }

    private void inits() {
        //初始化标题栏
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTopic = (TextView) findViewById(R.id.tv_topic);
        tvDept = (TextView) findViewById(R.id.tv_dept);
        tvStartTime = (TextView) findViewById(R.id.tv_startTime);
        tvEndTime = (TextView) findViewById(R.id.tv_endTime);
        tvGoal = (TextView) findViewById(R.id.tv_goal);
        tvContent = (TextView) findViewById(R.id.tv_content);
        ivAttachments = (ImageView) findViewById(R.id.iv_attachments);
        rvSubtasks = (RecyclerView) findViewById(R.id.rv_subtasks);

        tvStart = (TextView) findViewById(R.id.tv_start);
        tvfinish = (TextView) findViewById(R.id.tv_finish);
        tvArrange = (TextView) findViewById(R.id.tv_arrange);


        mToolBarManager = new ToolBarManager(mToolbar, this);
        mToolBarManager.init();
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mToolBarManager.showBack(true)
                .setTitle(getResources().getString(R.string.oa_task));

        tvStart.setOnClickListener(this);
        tvfinish.setOnClickListener(this);
        tvArrange.setOnClickListener(this);
        presenter.getTaskDetail(TaskDetailActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_start:
                presenter.changeTaskStatus(1);
                break;
            case R.id.tv_finish:
                presenter.changeTaskStatus(2);
                break;
            case R.id.tv_arrange:
                Intent intent = new Intent(TaskDetailActivity.this, TaskArrangeActivity.class);
                intent.putExtra("taskId", presenter.getTaskId());
                startActivity(intent);
                break;
        }
    }

    @Override
    public void showTaskDetail(TaskDetail detail) {
        tvTopic.setText(detail.getTopic());
        tvDept.setText(detail.getDept());
        tvStartTime.setText(detail.getStartTime());
        tvEndTime.setText(detail.getEndTime());
        tvGoal.setText(detail.getGoal());
        tvContent.setText(detail.getContent());
        if (null != detail.getAttachments()) {
            ivAttachments.setVisibility(View.VISIBLE);
        } else {
            ivAttachments.setVisibility(View.GONE);
        }

        if (null != detail.getSubTasks()) {
            adapter = new SubTaskAdapter(TaskDetailActivity.this, detail.getSubTasks());
            rvSubtasks.setAdapter(adapter);
        }

        if (detail.getType() == 0) {
            tvStart.setVisibility(View.VISIBLE);
            tvfinish.setVisibility(View.VISIBLE);
            tvArrange.setVisibility(View.GONE);
        } else {
            tvStart.setVisibility(View.GONE);
            tvfinish.setVisibility(View.GONE);
            tvArrange.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showChangeSuccess() {
        ToastUtils.showShortToast("修改任务状态成功！");
    }

    @Override
    public void showChangeFail(String msg) {
        ToastUtils.showShortToast(msg);
    }
}
