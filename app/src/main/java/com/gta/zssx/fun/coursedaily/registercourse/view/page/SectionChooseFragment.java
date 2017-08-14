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

import com.gta.zssx.AppConfiguration;
import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.model.ClassDataManager;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SectionBean;
import com.gta.zssx.fun.coursedaily.registercourse.presenter.SectionChoosePresenter;
import com.gta.zssx.fun.coursedaily.registercourse.view.SectionChooseView;
import com.gta.zssx.fun.coursedaily.registercourse.view.adapter.SectionChooseAdapter;
import com.gta.zssx.fun.coursedaily.registercourse.view.adapter.SectionSingleChooseAdapter;
import com.gta.zssx.fun.coursedaily.registercourse.view.base.RegisterCourseActivity;
import com.gta.zssx.fun.coursedaily.registercourse.view.base.RegisterCourseBaseFragment;
import com.gta.zssx.fun.coursedaily.registercourse.view.base.RegisterCourseFragmentBuilder;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * [Description]
 * <p/>
 * [How to use]
 * <p/>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/18.
 * @since 1.0.0
 */
public class SectionChooseFragment extends RegisterCourseBaseFragment<SectionChooseView, SectionChoosePresenter> implements SectionChooseView {

    private static String sSectionId = "sectionId";
    private RecyclerView mRecyclerView;
    public static final String PAGE_TAG = SectionChooseFragment.class.getSimpleName();
    private Set<SectionBean> mSectionBeanSet;
    private boolean mIsModify;
    private int mSectionId;


    @NonNull
    @Override
    public SectionChoosePresenter createPresenter() {
        return new SectionChoosePresenter(getActivity());
    }

    @Override
    public void showResult(List<SectionBean> sectionBeanList) {
        showList(sectionBeanList);
    }

    private void showList(List<SectionBean> sectionBeanList) {
        if (mIsModify) {
            List<SectionBean> lSectionBeen = presenter.makeModifyUnSign(ClassDataManager.getDataCache().getSignatureDto().getSectionID());
            SectionSingleChooseAdapter.Listener lListener = new SectionSingleChooseAdapter.Listener() {
                @Override
                public void itemClick(SectionBean sectionBean) {

                }

                @Override
                public void checkBoxClick(boolean isCheck, SectionBean sectionBean) {
                    mSectionBeanSet = new HashSet<>();
                    if (isCheck) {
                        mSectionBeanSet.add(sectionBean);
                    } else {
                        mSectionBeanSet.remove(sectionBean);
                    }
                    mToolBarManager.getRightButton().setEnabled(mSectionBeanSet.size() > 0);
                }
            };
            SectionSingleChooseAdapter lSingleChooseAdapter = new SectionSingleChooseAdapter(lSectionBeen, mActivity, lListener, mSectionId);
            mRecyclerView.setAdapter(lSingleChooseAdapter);

        } else {
            SectionChooseAdapter.Listener lListener = new SectionChooseAdapter.Listener() {
                @Override
                public void itemClick(SectionBean sectionBean) {

                }

                @Override
                public void checkBoxClick(boolean isCheck, SectionBean sectionBean) {

                    if (isCheck) {
                        mSectionBeanSet.add(sectionBean);
                    } else {
                        mSectionBeanSet.remove(sectionBean);
                    }
                    mToolBarManager.getRightButton().setEnabled(mSectionBeanSet.size() > 0);
                }
            };
            SectionChooseAdapter lAdapter = new SectionChooseAdapter(sectionBeanList, mActivity, lListener, null);
            mRecyclerView.setAdapter(lAdapter);
        }
    }

    public static class Builder extends RegisterCourseFragmentBuilder<SectionChooseFragment> {


        boolean modify;
        int mSectionId;

        public Builder(Context context) {
            super(context);
        }

        public Builder(Context context, boolean isModify, int sectionId) {
            super(context);
            modify = isModify;
            mSectionId = sectionId;
        }

        public Builder(Context context, boolean isModify) {
            super(context);
            modify = isModify;
        }

        @Override
        public SectionChooseFragment create() {
            SectionChooseFragment lSectionChooseFragment = new SectionChooseFragment();
            Bundle lBundle = new Bundle();
            lBundle.putBoolean(RegisterCourseActivity.sIsModify, modify);
            lBundle.putInt(sSectionId, mSectionId);
            lSectionChooseFragment.setArguments(lBundle);
            return lSectionChooseFragment;
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
        mSectionBeanSet = new HashSet<>();
//        mSectionBeanSet.addAll(ClassDataManager.getDataCache().getSection());
        mIsModify = getArguments().getBoolean(RegisterCourseActivity.sIsModify);
        mSectionId = getArguments().getInt(sSectionId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_section_choose, container, false);
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

        UserBean lUserBean = null;
        try {
            lUserBean = AppConfiguration.getInstance().getUserBean();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int lClassId = ClassDataManager.getDataCache().getsClassDisplayBean().getClassId();
        presenter.getSectionList(lUserBean.getUserId(), lClassId, ClassDataManager.getDataCache().getSignDate());
    }

    //设置标题等
    private void initViews() {
//        mToolBarManager.getToolbar().setNavigationIcon(R.drawable.login_close_ic);
        mToolBarManager.getRightButton().setEnabled(false);
        mToolBarManager.setTitle(mActivity.getString(R.string.section_choose))
                .showRightButton(true)
                .setRightText(mActivity.getString(R.string.finish))
                .clickRightButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Set<SectionBean> lSectionBeanSet = presenter.sortSet(mSectionBeanSet);
                        ClassDataManager.getDataCache().setSection(lSectionBeanSet);
                        mActivity.onBackPressed();
                    }
                });


        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
    }

    //绑定控件
    private void findView() {
        mRecyclerView = (RecyclerView) mActivity.findViewById(R.id.setion_choose_rv);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.detachView(false);
    }

    @Override
    public boolean onBackPress() {
//        ClassDataManager.getDataCache().setSection(null);
        return super.onBackPress();
    }
}
