package com.gta.zssx.mobileOA.view.page;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.gta.zssx.R;
import com.gta.zssx.mobileOA.model.bean.TaskArrange;
import com.gta.zssx.mobileOA.presenter.TaskArrangePresenter;
import com.gta.zssx.mobileOA.view.TaskArrangeView;
import com.gta.zssx.mobileOA.view.adapter.TaskArrangeAdapter;
import com.gta.zssx.patrolclass.view.page.ChooseTeacherActivity;
import com.gta.zssx.pub.base.BaseActivity;
import com.gta.zssx.pub.common.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务安排Activity
 */
public class TaskArrangeActivity extends BaseActivity<TaskArrangeView, TaskArrangePresenter> implements TaskArrangeView, View.OnClickListener ,TaskArrangeAdapter.OnChooseClickListener{

    private Toolbar toolbar;
    private TextView tvFinish;
    private RecyclerView rvTaskArrange;
    private RelativeLayout rlAdd;
    public AlertDialog.Builder mBuilder;

    private TaskArrangeAdapter adapter;
    private List<TaskArrange> taskArranges = new ArrayList<>();

    @NonNull
    @Override
    public TaskArrangePresenter createPresenter() {
        return new TaskArrangePresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oa_task_arrange);
        inits();
    }

    private void inits() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvFinish = (TextView) findViewById(R.id.tv_finish);
        rvTaskArrange = (RecyclerView) findViewById(R.id.rv_taskArranges);
        rlAdd = (RelativeLayout) findViewById(R.id.rl_add);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tvFinish.setOnClickListener(this);
        rlAdd.setOnClickListener(this);
        presenter.addTaskArrange();
    }

    @Override
    public void showTaskArranges(List<TaskArrange> arranges) {
        taskArranges = arranges;
        adapter = new TaskArrangeAdapter(TaskArrangeActivity.this, taskArranges,this);
        rvTaskArrange.setAdapter(adapter);
    }

    @Override
    public void showToast(String msg) {
        ToastUtils.showShortToast(msg);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_finish:
                presenter.submitTaskArranges();
                break;
            case R.id.rl_add:
                presenter.addTaskArrange();
                break;
        }

    }

    @Override
    public void showUnCompleteDialog() {
        if (mBuilder == null) {
            mBuilder = new AlertDialog.Builder(this);
            mBuilder.setMessage("请填写全部内容")
                    .setNegativeButton(getString(R.string.text_sure), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
        }
        mBuilder.show();
    }

    @Override
    public void onChoosePersonClick(int position) {
        Intent intent = new Intent(TaskArrangeActivity.this, ChooseTeacherActivity.class);
        intent.putExtra("type", Constant.TYPE_ARRANGE);
        startActivityForResult(intent,position);
    }

    @Override
    public void onChooseDepartmentClick(int position) {
        Intent intent = new Intent(TaskArrangeActivity.this, ChooseTeacherActivity.class);
        intent.putExtra("type", Constant.TYPE_ARRANGE);
        intent.putExtra("chooseType",1);
        startActivityForResult(intent,position);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            String deptId = data.getStringExtra("deptId");
            String deptName = data.getStringExtra("deptName");
            String personId = data.getStringExtra("personId");
            String personName = data.getStringExtra("personName");
//            Log.i("person","deptId : " + deptId  + "-------deptName:" + deptName  + "------personId:" + personId +"-------personName :" +personName );
            taskArranges.get(requestCode).setDeptId(deptId);
            taskArranges.get(requestCode).setDept(deptName);
            if(personId!=null){
                taskArranges.get(requestCode).setPerson(personId);
                taskArranges.get(requestCode).setPerson(personName);
            }
            adapter.notifyDataSetChanged();
        }
    }
}
