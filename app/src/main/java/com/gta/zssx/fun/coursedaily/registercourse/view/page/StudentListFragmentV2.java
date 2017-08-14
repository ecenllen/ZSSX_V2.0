package com.gta.zssx.fun.coursedaily.registercourse.view.page;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.model.ClassDataManager;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SectionBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.StudentListBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.StudentListNewBean;
import com.gta.zssx.fun.coursedaily.registercourse.presenter.StudentListPresenterV2;
import com.gta.zssx.fun.coursedaily.registercourse.view.StudentListViewV2;
import com.gta.zssx.fun.coursedaily.registercourse.view.base.RegisterCourseActivity;
import com.gta.zssx.fun.coursedaily.registercourse.view.base.RegisterCourseBaseFragment;
import com.gta.zssx.fun.coursedaily.registercourse.view.base.RegisterCourseFragmentBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * [Description]
 * <p/>
 * [How to use]
 * <p/>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/24.
 * @since 1.0.0
 */
@Deprecated
public class StudentListFragmentV2 extends RegisterCourseBaseFragment<StudentListViewV2, StudentListPresenterV2> implements StudentListViewV2 {

    public TabLayout mTabLayout;
    public ViewPager mViewPager;
    private ArrayList<String> mTitleList;
    private ArrayList<Fragment> mViewList;
    public Set<SectionBean> mSection;
    public String mTitle;
    public static String sTITLE = "TITLE";
    private boolean mIsModify;

    @NonNull
    @Override
    public StudentListPresenterV2 createPresenter() {
        return new StudentListPresenterV2();
    }

    public static final String PAGE_TAG = StudentListFragmentV2.class.getSimpleName();

    @Override
    public void showResult(List<StudentListNewBean> studentListBeen) {
//        showList(studentListBeen);
    }

    @Override
    public void emptyUI() {

    }

    private void showList(List<StudentListBean> studentListBeen) {

        mTitleList = new ArrayList<>();
        mViewList = new ArrayList<>();

        if (mSection != null) {
            List<SectionBean> lSectionBeen = new ArrayList<>(mSection);
            for (int i = 0; i < lSectionBeen.size(); i++) {
                mTitleList.add(lSectionBeen.get(i).getLesson());
                StudentListInnerFragment lInnerFragment = new StudentListInnerFragment.Builder(mActivity, i).create();
                mViewList.add(lInnerFragment);
            }
        }

        assert mSection != null;
//        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        if (mSection.size() > 4) {
        } else {
            mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        }
        for (int i = 0; i < mTitleList.size(); i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(i)));
        }

        FavouritePageAdapter adapter = new FavouritePageAdapter(getChildFragmentManager(), mViewList);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    public static class Builder extends RegisterCourseFragmentBuilder<StudentListFragmentV2> {

        String mTitle;

        boolean mIsModify;

        public Builder(Context context, String title, boolean isModify) {
            super(context);
            mTitle = title;
            mIsModify = isModify;
        }

        @Override
        public StudentListFragmentV2 create() {
            Bundle lBundle = new Bundle();

            lBundle.putBoolean(RegisterCourseActivity.sIsModify, mIsModify);
            lBundle.putString(sTITLE, mTitle);
            StudentListFragmentV2 lStudentListFragment = new StudentListFragmentV2();
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
        mIsModify = getArguments().getBoolean(RegisterCourseActivity.sIsModify);
        mSection = ClassDataManager.getDataCache().getSection();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_student_list_01, container, false);
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

    //设置标题等
    private void initViews() {

        mToolBarManager.setTitle(mTitle)
                .showRightButton(true)
                .setRightText(mActivity.getString(R.string.next_step))
                .clickRightButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new SignFragment.Builder(mActivity, mIsModify).display();
                    }
                });
    }


    //绑定控件
    private void findView() {
        mTabLayout = (TabLayout) mActivity.findViewById(R.id.student_list_tablayout);
        mViewPager = (ViewPager) mActivity.findViewById(R.id.student_list_viewpager);
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


    private class FavouritePageAdapter extends FragmentPagerAdapter {
        public static final String TAB_POSITION = "tab_position";
        private ArrayList<Fragment> mFragmentArrayList;

        public FavouritePageAdapter(FragmentManager fm, ArrayList<Fragment> fragmentArrayList) {
            super(fm);
            mFragmentArrayList = fragmentArrayList;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment List = mFragmentArrayList.get(position);
            Bundle lArguments = List.getArguments();
//            Bundle args = new Bundle();
            lArguments.putInt(TAB_POSITION, position);
            List.setArguments(lArguments);
            return List;
        }

        @Override
        public int getCount() {
            return mFragmentArrayList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mToolBarManager.showRightButton(true);
    }


}
