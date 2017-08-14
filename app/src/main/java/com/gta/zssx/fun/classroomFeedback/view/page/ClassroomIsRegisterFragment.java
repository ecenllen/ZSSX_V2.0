package com.gta.zssx.fun.classroomFeedback.view.page;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.gta.zssx.R;
import com.gta.zssx.fun.classroomFeedback.model.bean.ClassroomFeedbackBean;
import com.gta.zssx.fun.classroomFeedback.presenter.ClassroomIsRegisterPresenter;
import com.gta.zssx.fun.classroomFeedback.view.ClassroomIsRegisterView;
import com.gta.zssx.fun.classroomFeedback.view.adapter.ClassroomFeedbackAdapter;
import com.gta.zssx.pub.base.BaseMvpFragment;
import com.gta.zssx.pub.hfrecyclerview.HFRecyclerView;
import com.gta.zssx.pub.util.RxBus;

import java.util.List;

import rx.Subscriber;

/**
 * [Description]
 * <p> 课堂教学反馈首页，已登记Fragment
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by liang.lu on 2017/06/20.
 * @since 2.0.0
 */
public class ClassroomIsRegisterFragment extends BaseMvpFragment<ClassroomIsRegisterView, ClassroomIsRegisterPresenter> implements ClassroomIsRegisterView
        , ClassroomFeedbackAdapter.OptionItemListener, HFRecyclerView.HFRecyclerViewListener {

    public static final String REFRESH = "refresh";
    private TextView mEmptyTv;
    private HFRecyclerView mRecyclerView;
    private List<ClassroomFeedbackBean.RegisterDataListBean> list;
    private boolean isRefresh;
    private ClassroomFeedbackAdapter mAdapter;

    @Override
    public int getLayoutId () {
        return R.layout.fragment_classroom_register;
    }

    @Override
    protected void initView (View view) {
        mRecyclerView = (HFRecyclerView) view.findViewById (R.id.recycler);
        mEmptyTv = (TextView) view.findViewById (R.id.tv_non);
        setOnInteractListener ();
    }

    private void setOnInteractListener () {
        mRecyclerView.setCanLoadMore (false);
        mRecyclerView.setCanRefresh (true);
        mRecyclerView.setRecyclerViewListener (this);
    }

    @Override
    protected void requestData () {
        super.requestData ();
        presenter.loadData (2, "");
    }

    @NonNull
    @Override
    public ClassroomIsRegisterPresenter createPresenter () {
        return new ClassroomIsRegisterPresenter ();
    }

    @Override
    public void onActivityCreated (@Nullable Bundle savedInstanceState) {
        super.onActivityCreated (savedInstanceState);
        RxBus ();
    }

    private void RxBus () {
        addSubscription (RxBus.getDefault ().toObserverable (String.class)
                .subscribe (new Subscriber<String> () {
                    @Override
                    public void onCompleted () {

                    }

                    @Override
                    public void onError (Throwable e) {

                    }

                    @Override
                    public void onNext (String s) {
                        if (s.equals (RegisterActivity.CLASSROOM_SUBMIT_SUCCESS)) {
                            isRefresh = true;
                        }
                    }
                }));
    }

    @Override
    public void onResume () {
        super.onResume ();
        if (isRefresh) {
            presenter.loadData (2, "");
            isRefresh = false;
        }
    }

    @Override
    public void showResult (List<ClassroomFeedbackBean.RegisterDataListBean> list, String state) {
        this.list = list;
        mEmptyTv.setVisibility (View.GONE);
        if (state.equals (REFRESH)) {
            mRecyclerView.stopRefresh (true);
        }
        if (mAdapter == null) {
            mAdapter = new ClassroomFeedbackAdapter (mActivity);
            mAdapter.setDataListBeen (list);
            mAdapter.setOptionItemListener (this);
            mRecyclerView.setAdapter (mAdapter);
            return;
        }
        mAdapter.setDataListBeen (list);
        mAdapter.notifyDataSetChanged ();
    }

    @Override
    public void showEmpty (String state, boolean isSuccess) {
        if (state.equals (REFRESH)) {
            mRecyclerView.stopRefresh (isSuccess);
            return;
        }
        if (mAdapter == null) {
            mAdapter = new ClassroomFeedbackAdapter (mActivity);
            mAdapter.setDataListBeen (null);
            mRecyclerView.setAdapter (mAdapter);
        } else {
            mAdapter.setDataListBeen (null);
            mAdapter.notifyDataSetChanged ();
        }
        mEmptyTv.setVisibility (View.VISIBLE);
    }

    @Override
    public void showToast () {
        ToastUtils.showShortToast (getString (R.string.no_registration_info));
    }

    @Override
    public void onRefresh () {
        presenter.loadData (2, REFRESH);
    }

    @Override
    public void onLoadMore () {

    }

    @Override
    public void optionClickListener (int position) {
        RegisterActivity.start (mActivity, list.get (position));
    }
}
