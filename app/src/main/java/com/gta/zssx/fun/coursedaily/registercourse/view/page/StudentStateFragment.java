package com.gta.zssx.fun.coursedaily.registercourse.view.page;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.model.ClassDataManager;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SectionBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.StudentListBean;
import com.gta.zssx.fun.coursedaily.registercourse.presenter.StudentStatePresenter;
import com.gta.zssx.fun.coursedaily.registercourse.view.StudentStateView;
import com.gta.zssx.fun.coursedaily.registercourse.view.adapter.StudentStateAdapter;
import com.gta.zssx.fun.coursedaily.registercourse.view.base.RegisterCourseBaseFragment;
import com.gta.zssx.fun.coursedaily.registercourse.view.base.RegisterCourseFragmentBuilder;

import java.util.ArrayList;
import java.util.Set;

/**
 * [Description]
 * <p/>
 * [How to use]
 * <p/>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/20.
 * @since 1.0.0
 */
public class StudentStateFragment extends RegisterCourseBaseFragment<StudentStateView, StudentStatePresenter> implements StudentStateView {

    public static String sStudent_item = "student_item";
    public static String sItem_position = "item_position";
    public RecyclerView mRecyclerView;
    private StudentListBean mStudentListBean;
    private int mPosition;
    public StudentStateAdapter mAdapter;

    @NonNull
    @Override
    public StudentStatePresenter createPresenter() {
        return new StudentStatePresenter();
    }

    public static final String PAGE_TAG = StudentStateFragment.class.getSimpleName();

    public static class Builder extends RegisterCourseFragmentBuilder<StudentStateFragment> {

        StudentListBean mStudentListBean;
        int mInt;

        public Builder(Context context, StudentListBean studentListBean, int position) {
            super(context);
            mStudentListBean = studentListBean;
            mInt = position;
        }

        @Override
        public StudentStateFragment create() {
            Bundle lBundle = new Bundle();

            lBundle.putSerializable(sStudent_item, mStudentListBean);

            lBundle.putInt(sItem_position, mInt);
            StudentStateFragment lStudentStateFragment = new StudentStateFragment();
            lStudentStateFragment.setArguments(lBundle);
            return lStudentStateFragment;
        }

        @Override
        public String getPageTag() {
            return PAGE_TAG;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        dataAction();
    }

    //fragment间参数传递
    private void dataAction() {
        mStudentListBean = (StudentListBean) getArguments().getSerializable(sStudent_item);
        mPosition = getArguments().getInt(sItem_position);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_student_state, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        uiAction();
    }

    private void uiAction() {
        findView();
        initViews();
        setOnInteractListener();
    }

    //事件处理
    private void setOnInteractListener() {
        StudentStateAdapter.Listener lListener = new StudentStateAdapter.Listener() {
            @Override
            public void itemClick() {

            }
        };
        Set<SectionBean> lSection = ClassDataManager.getDataCache().getSection();
        ArrayList<SectionBean> lSectionBeen = new ArrayList<>(lSection);
        mAdapter = new StudentStateAdapter(mActivity, lSectionBeen, lListener, mStudentListBean);
        mRecyclerView.setAdapter(mAdapter);
    }

    //设置标题等
    private void initViews() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));

        mToolBarManager.getToolbar().setNavigationIcon(R.drawable.login_close_ic);

        mToolBarManager.setTitle("14会计1")
                .showRightButton(true)
                .setRightText("完成")
                .clickRightButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAdapter.updateStudentList(mPosition);
                        StudentListFragment.backRefresh = true;
                        mActivity.onBackPressed();
                    }
                });
    }

    //绑定控件
    private void findView() {

        mRecyclerView = (RecyclerView) mActivity.findViewById(R.id.student_state_rv);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.detachView(false);
    }
}
