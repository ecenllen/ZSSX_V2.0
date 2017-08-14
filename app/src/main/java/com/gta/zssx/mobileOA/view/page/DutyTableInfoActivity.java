package com.gta.zssx.mobileOA.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gta.utils.resource.Toast;
import com.gta.zssx.R;
import com.gta.zssx.mobileOA.model.bean.DutyTableInfo;
import com.gta.zssx.mobileOA.presenter.DutyTableInfoPresenter;
import com.gta.zssx.mobileOA.view.DutyTableInfoView;
import com.gta.zssx.mobileOA.view.adapter.DutyTableAdapter;
import com.gta.zssx.pub.base.BaseActivity;
import com.gta.zssx.pub.hfrecyclerview.HFRecyclerView;
import com.gta.zssx.pub.manager.ToolBarManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lan.zheng on 2016/11/19.
 */
public class DutyTableInfoActivity extends BaseActivity<DutyTableInfoView,DutyTableInfoPresenter>
        implements DutyTableInfoView,HFRecyclerView.HFRecyclerViewListener {
    private HFRecyclerView mRecyclerView;
    private TextView tv_emptyHintTextView;
    public Toolbar mToolbar;
    protected ToolBarManager mToolBarManager;
    private DutyTableAdapter mDutyTableAdapter;
    private String mRemark;

    @NonNull
    @Override
    public DutyTableInfoPresenter createPresenter() {
        return new DutyTableInfoPresenter();
    }

    public static void start(Context context) {
        final Intent lIntent = new Intent(context, DutyTableInfoActivity.class);
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duty_table_info);
        initView();
        initData();
    }

    private void initView(){
        tv_emptyHintTextView = (TextView)findViewById(R.id.tv_emptyHint);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(view -> onBackPressed());
        mToolBarManager = new ToolBarManager(mToolbar, this);
        mToolBarManager.init();
        mToolBarManager.setTitle(getString(R.string.text_duty_table))
                .setRightText(R.string.text_remark2).clickRightButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 测试数据
                mRemark = getResources().getString(R.string.text_duty_table_remark_content)
                        +getResources().getString(R.string.text_duty_table_remark_content)
                        +getResources().getString(R.string.text_duty_table_remark_content);
                if(TextUtils.isEmpty(mRemark)){
                    Toast.Short(DutyTableInfoActivity.this,"无备注信息");
                    return;
                }
                presenter.popupConfirmDialog(mRemark,DutyTableInfoActivity.this);
            }
        });
        mRecyclerView = (HFRecyclerView) findViewById(R.id.recycler_duty_table_info);
        setOnInteractListener();
    }

    private void setOnInteractListener () {
        mRecyclerView.setCanLoadMore (false);
        mRecyclerView.setCanRefresh (false);
        mRecyclerView.setFooterViewText ("");
        mRecyclerView.setRecyclerViewListener (this);
    }


    private void initData(){
        tv_emptyHintTextView.setVisibility(View.VISIBLE);
        mRemark = "";
//        presenter.getDutyTable(1);
        //==============测试数据=============
        tv_emptyHintTextView.setVisibility(View.GONE);
        List<DutyTableInfo.DutyArrange.DutyDtail> list = new ArrayList<>();
        DutyTableInfo.DutyArrange.DutyDtail dutyDtail1 = new DutyTableInfo.DutyArrange.DutyDtail();
        dutyDtail1.setAddress("哈哈");
        dutyDtail1.setPeople("哈哈");
        list.add(dutyDtail1);
        list.add(dutyDtail1);
        list.add(dutyDtail1);
        List<DutyTableInfo.DutyArrange> dutyArranges = new ArrayList<>();
        for(int i = 1;i<7;i++){
            DutyTableInfo.DutyArrange dutyArrange= new DutyTableInfo.DutyArrange();
            dutyArrange.setWeek("星期"+i);
            dutyArrange.setWeekDuration("1-10周");
            dutyArrange.setDetails(list);
            dutyArranges.add(dutyArrange);
        }
        mDutyTableAdapter = new DutyTableAdapter(this,dutyArranges);
        mRecyclerView.setAdapter(mDutyTableAdapter);
        //===============================
    }

    @Override
    public void onRefresh() {
        //暂时不需要
    }

    @Override
    public void onLoadMore() {
        //暂时不需要
    }
}
