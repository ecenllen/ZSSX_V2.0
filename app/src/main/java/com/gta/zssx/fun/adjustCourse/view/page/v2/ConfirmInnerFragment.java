package com.gta.zssx.fun.adjustCourse.view.page.v2;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gta.zssx.fun.adjustCourse.model.bean.ApplyConfirmBean;
import com.gta.zssx.fun.adjustCourse.presenter.ConfirmInnerPresenter;
import com.gta.zssx.fun.adjustCourse.view.ConfirmInnerView;
import com.gta.zssx.fun.adjustCourse.view.adapter.MyConfirmAdapter;
import com.gta.zssx.fun.adjustCourse.view.base.BaseRecyclerViewFragment;
import com.gta.zssx.fun.adjustCourse.view.base.Constant;
import com.gta.zssx.pub.util.LogUtil;
import com.gta.zssx.pub.util.RxBus;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2017/3/20.
 * @since 1.0.0
 */
public class ConfirmInnerFragment extends BaseRecyclerViewFragment
        <ConfirmInnerView, ConfirmInnerPresenter, ApplyConfirmBean.ListBean> implements ConfirmInnerView {

    public static String FRAGMENT_TYPE = "type";
    //0代表我的确认列表，1代表我的审核列表
    public int mType;
    public static final int CONFIRM = 0;
    public static final int AUDIT = 1;
    private String mStatus;


    @Override
    protected BaseQuickAdapter getRecyclerAdapter() {
        return new MyConfirmAdapter(mStatus);
    }

    @NonNull
    @Override
    public ConfirmInnerPresenter createPresenter() {
        return new ConfirmInnerPresenter();
    }

    @Override
    protected void initData() {
        super.initData();
        mStatus = getArguments().getString(MyApplyInnerFragment.STATUS);
        mType = getArguments().getInt(FRAGMENT_TYPE);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.mCompositeSubscription.add(RxBus.getDefault().toObserverable(Constant.ConfirmSuccess.class)
                .subscribe(confirmSuccess -> {
                    onRefresh();
                }, throwable -> {
                    LogUtil.Log(ConfirmInnerFragment.class.getSimpleName(), "确认返回刷新出错");
                }));

        presenter.mCompositeSubscription.add(RxBus.getDefault().toObserverable(Constant.AuditSuccess.class)
                .subscribe(auditSuccess -> {
                    onRefresh();
                }, throwable -> {
                    LogUtil.Log(ConfirmInnerFragment.class.getSimpleName(), "审核返回刷新出错");
                }));
    }

    @Override
    protected void requestData() {
        super.requestData();
        presenter.getApplyConfirm(mPage, mType, mStatus);
    }

    @Override
    protected void itemClick(ApplyConfirmBean.ListBean confirmBean, int position) {
        super.itemClick(confirmBean, position);
        if (mType == AUDIT) {
            if (mStatus.equals(Constant.AUDIT_STATUS_N)) {
                DetailActivity.start(mActivity, Constant.DETAIL_TYPE_AUDIT, confirmBean.getTransferApplyId(), Constant.PAGE_THREE);
            } else {
                DetailActivity.start(mActivity, Constant.DETAIL_TYPE_CHECK, confirmBean.getTransferApplyId(), Constant.PAGE_THREE);
            }
        } else {
            if (mStatus.equals(Constant.AUDIT_STATUS_N)) {
                DetailActivity.start(mActivity, Constant.DETAIL_TYPE_CONFIRM, confirmBean.getTransferApplyId(), Constant.PAGE_TOW);
            } else {
                DetailActivity.start(mActivity, Constant.DETAIL_TYPE_CHECK, confirmBean.getTransferApplyId(), Constant.PAGE_TOW);
            }
        }

    }


    @Override
    public void getResultData(ApplyConfirmBean confirmBean) {
        update(confirmBean.getList());
    }
}
