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
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.StudentListBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.StudentListNewBean;
import com.gta.zssx.fun.coursedaily.registercourse.presenter.StudentListPresenter;
import com.gta.zssx.fun.coursedaily.registercourse.view.StudentListView;
import com.gta.zssx.fun.coursedaily.registercourse.view.adapter.StudentListAdapter;
import com.gta.zssx.fun.coursedaily.registercourse.view.base.RegisterCourseBaseFragment;
import com.gta.zssx.fun.coursedaily.registercourse.view.base.RegisterCourseFragmentBuilder;

import java.util.ArrayList;
import java.util.List;

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
@Deprecated
public class StudentListFragment extends RegisterCourseBaseFragment<StudentListView, StudentListPresenter> implements StudentListView {

    public static final String PAGE_TAG = StudentListFragment.class.getSimpleName();
    public static boolean backRefresh;
    public static String sTITLE = "TITLE";
    public RecyclerView mRecyclerView;
    public StudentListAdapter mAdapter;
    private int mPosition;
    public String mTitle;

    @NonNull
    @Override
    public StudentListPresenter createPresenter() {
        return new StudentListPresenter();
    }

    @Override
    public void showResult(List<StudentListNewBean> studentListBeen) {
//        showList(studentListBeen);
    }

    public static class Builder extends RegisterCourseFragmentBuilder<StudentListFragment> {


        String mTitle;

        public Builder(Context context, String title) {
            super(context);
            mTitle = title;
        }

        @Override
        public StudentListFragment create() {

            Bundle lBundle = new Bundle();

            lBundle.putString(sTITLE, mTitle);
            StudentListFragment lStudentListFragment = new StudentListFragment();
            lStudentListFragment.setArguments(lBundle);
            return lStudentListFragment;
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
        mTitle = getArguments().getString(sTITLE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_student_list, container, false);
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

        int lClassId = ClassDataManager.getDataCache().getsClassDisplayBean().getClassId();
        presenter.getStudentList(lClassId, ClassDataManager.getDataCache().getSignDate());
    }

    private void showList(List<StudentListBean> studentListBeen) {
        StudentListAdapter.Listener lListener = new StudentListAdapter.Listener() {
            @Override
            public void itemClick(StudentListBean studentListBean, int position) {
                mPosition = position;
                new StudentStateFragment.Builder(mActivity, studentListBean, position).display();
            }
        };

        mAdapter = new StudentListAdapter(mActivity, studentListBeen, lListener);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (backRefresh) {
            List<StudentListNewBean> studentListNewBeanList = ClassDataManager.getDataCache().getStudentList();
            List<StudentListBean> studentListBeen = new ArrayList<>();
            for(int i = 0;i < studentListNewBeanList.size();i++){
                //TODO Bean转换
            }
            mAdapter.refreshData(studentListBeen, mPosition);
            backRefresh = false;
        }
    }

    //设置标题等
    private void initViews() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));

        mToolBarManager.setTitle(mTitle)
                .showRightButton(true)
                .setRightText(mActivity.getString(R.string.next_step))
                .clickRightButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new SignFragment.Builder(mActivity,false).display();
                    }
                });
    }

    //绑定控件
    private void findView() {
        mRecyclerView = (RecyclerView) mActivity.findViewById(R.id.student_list_rv);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.detachView(false);
    }


    @Override
    public boolean onBackPress() {

        ClassDataManager.getDataCache().setStudentList(null);
        return super.onBackPress();

    }
}
