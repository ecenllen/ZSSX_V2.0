package com.gta.zssx.mobileOA.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.mobileOA.presenter.ChangeShitfDetailPresenter;
import com.gta.zssx.mobileOA.view.ChangeShitfDetailView;
import com.gta.zssx.pub.base.BaseActivity;

/**
 * 调班申请详情Activity
 */
public class ChangeShiftDetailActivity extends BaseActivity<ChangeShitfDetailView, ChangeShitfDetailPresenter> {
    private Toolbar mToolbar;
    private TextView tvTitle;
    private TextView tvRota;
    private TextView tvPerson;
    private TextView tvDate;
    private TextView tvType;
    private TextView tvStatus;


    @NonNull
    @Override
    public ChangeShitfDetailPresenter createPresenter() {
        return new ChangeShitfDetailPresenter();
    }

    public static void start(Context context) {
        final Intent lIntent = new Intent(context, ChangeShiftDetailActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putInt("dutyType",registerOrCheck);
//        lIntent.putExtras(bundle);
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_shift_detail);
        inits();
    }

    public void inits() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitle = (TextView)findViewById(R.id.tv_title);
        tvRota = (TextView)findViewById(R.id.tv_rota);
        tvPerson = (TextView)findViewById(R.id.tv_person);
        tvDate = (TextView)findViewById(R.id.tv_date);
        tvType = (TextView)findViewById(R.id.tv_arrangeType);
        tvStatus = (TextView)findViewById(R.id.tv_status);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvRota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DutyTableInfoActivity.start(ChangeShiftDetailActivity.this);
            }
        });

    }
}
