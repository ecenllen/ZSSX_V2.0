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
import android.widget.Toast;

import com.gta.zssx.AppConfiguration;
import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.model.ClassDataManager;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.ClassDisplayBean;
import com.gta.zssx.fun.coursedaily.registercourse.presenter.ClassDisplayPresenter;
import com.gta.zssx.fun.coursedaily.registercourse.view.ClassDisplayView;
import com.gta.zssx.fun.coursedaily.registercourse.view.adapter.ClassDisplayAdapter;
import com.gta.zssx.fun.coursedaily.registercourse.view.base.RegisterCourseActivity;
import com.gta.zssx.fun.coursedaily.registercourse.view.base.RegisterCourseBaseFragment;
import com.gta.zssx.fun.coursedaily.registercourse.view.base.RegisterCourseFragmentBuilder;
import com.gta.zssx.fun.coursedaily.registerrecord.view.page.AlreadyRegisteredRecordActivity;
import com.gta.zssx.fun.coursedaily.registerrecord.view.page.AlreadyRegisteredRecordPeriodOfTimeActivity;
import com.gta.zssx.fun.coursedaily.registerrecord.view.page.MyClassRecordFragment;
import com.gta.zssx.fun.coursedaily.registerrecord.view.page.MyClassRecordSelectTabFragment;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.fun.personalcenter.view.page.PersonCenterActivity;
import com.gta.zssx.pub.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * [Description]
 * <p/>
 * [How to use]
 * <p/>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/15.
 * @since 1.0.0
 */
public class ClassDisplayFragment extends RegisterCourseBaseFragment<ClassDisplayView, ClassDisplayPresenter> implements ClassDisplayView {

    public static final String PAGE_TAG = ClassDisplayFragment.class.getSimpleName();
    public RecyclerView mRecyclerView;

    @NonNull
    @Override
    public ClassDisplayPresenter createPresenter() {
        return new ClassDisplayPresenter();
    }

    @Override
    public void showResult(List<ClassDisplayBean> classDisplayBeen) {
        showList(classDisplayBeen);
    }

    public static class Builder extends RegisterCourseFragmentBuilder<ClassDisplayFragment> {


        public Builder(Context context) {
            super(context);
        }

        @Override
        public ClassDisplayFragment create() {
            return new ClassDisplayFragment();
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
        initMyClassId();
    }

    private List<UserBean.ClassList> mClassLists;
    private void initMyClassId(){
        mClassLists = new ArrayList<>();
        UserBean lUserBean = null;
        try {
            lUserBean = AppConfiguration.getInstance().getUserBean();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(lUserBean != null){
            mClassLists = lUserBean.getClassName();
            LogUtil.Log("lenita","mClassLists.size() = "+ mClassLists.size());
        }else{
            LogUtil.Log("lenita","mClassLists.size() = "+ mClassLists.size());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_class_display, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        uiAction();
    }

    private void uiAction() {
        findView();
        initViews();
        setOnInteractListener();
    }

    //事件处理
    private void setOnInteractListener() {
        presenter.getClassList();
    }

    private void showList(List<ClassDisplayBean> classDisplayBeen) {
        ClassDisplayAdapter.Listener lListener = new ClassDisplayAdapter.Listener() {
            @Override
            public void itemClick(ClassDisplayBean item) {
                ClassDataManager.getDataCache().setsClassDisplayBean(null);
                ClassDataManager.getDataCache().setsClassDisplayBean(item);
                new RegisterDetailFragment.Builder(mActivity, item.getClassName(), false).display();
            }

            @Override
            public void addClick() {
                ClassDataManager.getDataCache().setClassDisplay(null);
                RegisterCourseActivity.start(mActivity, ClassChooseFragment.PAGE_TAG);
                mActivity.finish();
            }

            @Override
            public void myClassClick() {
                if(mClassLists.size() != 0){
                    if(mClassLists.size() > 1){
                        AlreadyRegisteredRecordActivity.start(mActivity, MyClassRecordSelectTabFragment.PAGE_TAG, mClassLists,true);
                    }else {
                        AlreadyRegisteredRecordActivity.start(mActivity, MyClassRecordFragment.PAGE_TAG, mClassLists,true);
                    }
                }else {
                    Toast.makeText(mActivity, getResources().getString(R.string.text_class_log), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void itemLongClick(ClassDisplayBean classDisplayBean, int position) {

            }

        };

        ClassDisplayAdapter lAdapter = new ClassDisplayAdapter(mActivity, lListener, classDisplayBeen);
        mRecyclerView.setAdapter(lAdapter);
    }

    //设置标题等
    private void initViews() {

        mToolBarManager.getRightButton().setEnabled(true);
        mToolBarManager.showBack(false)
                .setTitle("课堂日志")
                .showRightButton(true)
                .showLeftImage(true)
                .setRightText("已登记")
                .clickRightButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //进入已登记页面
//                        AlreadyRegisteredRecordActivity.start4Result(mActivity, AlreadyRegisteredRecordFragment.TAG);
                        AlreadyRegisteredRecordPeriodOfTimeActivity.start(mActivity);
                    }
                })
                .clickLeftImage(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //进入个人中心
                        PersonCenterActivity.start(mActivity);
                    }
                });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
    }

    //绑定控件
    private void findView() {
        mRecyclerView = (RecyclerView) mActivity.findViewById(R.id.class_display_rv);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.detachView(false);
    }
}
