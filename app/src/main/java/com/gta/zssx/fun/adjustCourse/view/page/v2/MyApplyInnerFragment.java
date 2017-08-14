package com.gta.zssx.fun.adjustCourse.view.page.v2;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gta.zssx.fun.adjustCourse.model.bean.ApplyBean;
import com.gta.zssx.fun.adjustCourse.presenter.MyApplyInnerPresenter;
import com.gta.zssx.fun.adjustCourse.view.MyApplyInnerView;
import com.gta.zssx.fun.adjustCourse.view.adapter.MyApplyAdapter;
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
 * @author Created by Zhimin.Huang on 2017/3/6.
 * @since 1.0.0
 */
public class MyApplyInnerFragment extends BaseRecyclerViewFragment<MyApplyInnerView, MyApplyInnerPresenter, ApplyBean.ListBean>
        implements MyApplyInnerView {

    private String TAG = MyApplyInnerFragment.class.getSimpleName();


    public static String STATUS = "Status";
    private String mStatus;

    @NonNull
    @Override
    public MyApplyInnerPresenter createPresenter() {
        return new MyApplyInnerPresenter();
    }


    @Override
    protected void initView(View view) {
        super.initView(view);

    }

    @Override
    protected void initData() {
        mStatus = getArguments().getString(STATUS);
    }

    @Override
    protected void requestData() {
        presenter.getApply(mPage, mStatus);
    }


    @Override
    public void getResultData(ApplyBean applyBean) {
        update(applyBean.getList());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.mCompositeSubscription.add(RxBus.getDefault().toObserverable(Constant.DeleteSuccess.class)
                .subscribe(s -> {
                    onRefresh();
                }, throwable -> {
                    LogUtil.Log(TAG, "删除返回刷新出错");
                }));
    }

    @Override
    protected BaseQuickAdapter getRecyclerAdapter() {
        return new MyApplyAdapter(mStatus);
    }

    @Override
    protected void itemClick(ApplyBean.ListBean listBean, int position) {
        super.itemClick(listBean, position);
        DetailActivity.start(mActivity, Constant.DETAIL_TYPE_CHECK, listBean.getTransferApplyId(), Constant.PAGE_ONE);
    }
}
