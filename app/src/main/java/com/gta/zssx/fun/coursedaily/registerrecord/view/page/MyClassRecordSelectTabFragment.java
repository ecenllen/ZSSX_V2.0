package com.gta.zssx.fun.coursedaily.registerrecord.view.page;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gta.zssx.AppConfiguration;
import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.MyClassSubTabInfoDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.RegisteredRecordManager;
import com.gta.zssx.fun.coursedaily.registerrecord.presenter.MyClassRecordSelectTabPresenter;
import com.gta.zssx.fun.coursedaily.registerrecord.view.MyClassRecordSelectTabView;
import com.gta.zssx.fun.coursedaily.registerrecord.view.base.RegisterRecordBaseFragment;
import com.gta.zssx.fun.coursedaily.registerrecord.view.base.RegisterRecordFragmentBuilder;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.pub.util.LogUtil;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by lan.zheng on 2016/6/30.
 * 用于有多个班级的情况
 */
public class MyClassRecordSelectTabFragment extends RegisterRecordBaseFragment<MyClassRecordSelectTabView, MyClassRecordSelectTabPresenter> implements MyClassRecordSelectTabView {

    public static final String PAGE_TAG = MyClassRecordSelectTabFragment.class.getSimpleName();
    public TabLayout mTabLayout;
    public ViewPager mViewPager;
    private ArrayList<String> mTitleList;
    private ArrayList<Fragment> mViewList;

    public List<UserBean.ClassList> mClassLists;
    public static String CLASS_LIST = "CLASS_LIST";
    public static String TO_GET_SERVER_TIME = "TO_GET_SERVER_TIME";
    public static String NEW_PAGE = "NEW_PAGE";
    public String TeacherID;
    private static String mDate;  //日期
    public static String mWeek;  //星期几
    private boolean getServerTimeFailedCauseNotNetwork = false;
    private boolean toGetServerTime = false;
    private boolean newPage = true;

    @NonNull
    @Override
    public MyClassRecordSelectTabPresenter createPresenter() {
        return new MyClassRecordSelectTabPresenter();
    }

    @Override
    public void changeCacheStatus() {
        for(int i = 0;i < mClassLists.size();i++){
            AlreadyRegisteredRecordActivity.mMyClassesStatus.get(i).setStatus(AlreadyRegisteredRecordActivity.MY_CLASSES_FIRST_GET_DATA_DONE);
        }
    }

    @Override
    public void setServerTime(String date,String week) {
        Date lDate = null;
        SimpleDateFormat lSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            lDate = lSimpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mDate = lSimpleDateFormat.format(lDate);
        mWeek = week;
        initPageAndGetData();  //初始化page
    }

    @Override
    public void setServerTimeFailed(boolean isNotNetwork) {
        getServerTimeFailedCauseNotNetwork = isNotNetwork;
        initPageAndGetData(); //初始化page
    }

    public static class Builder extends RegisterRecordFragmentBuilder<MyClassRecordSelectTabFragment> {

        List<UserBean.ClassList> mClassLists;
        boolean newPage;  //是否生成新的Fragment

        public Builder(Context context, List<UserBean.ClassList> classLists) {
            super(context);
            mClassLists = classLists;
            newPage = true;
        }

        public Builder(Context context){
            super(context);
            newPage = false;
        }


        @Override
        public MyClassRecordSelectTabFragment create() {
            Bundle lBundle = new Bundle();
            lBundle.putSerializable(CLASS_LIST, (Serializable) mClassLists);
            lBundle.putBoolean(TO_GET_SERVER_TIME,true);
            lBundle.putBoolean(NEW_PAGE,newPage);
            MyClassRecordSelectTabFragment lMyClassRecordSelectTabFragment = new MyClassRecordSelectTabFragment();
            lMyClassRecordSelectTabFragment.setArguments(lBundle);
            return lMyClassRecordSelectTabFragment;
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
    private void dataAction(){
        getLocalDate();
        mClassLists = (List<UserBean.ClassList>) getArguments().getSerializable(CLASS_LIST);
        try {
            UserBean lUserBean = AppConfiguration.getInstance().getUserBean();
            TeacherID = lUserBean.getUserId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        toGetServerTime = getArguments().getBoolean(TO_GET_SERVER_TIME,true);
        newPage = getArguments().getBoolean(NEW_PAGE,true);  //TODO 是否是去生成Fragment
    }

    private void getLocalDate(){
        Calendar lCalendar = Calendar.getInstance();
        mDate =  lCalendar.get(Calendar.YEAR)+"-"
                +((lCalendar.get(Calendar.MONTH)+1)>9?(lCalendar.get(Calendar.MONTH)+1):"0"+(lCalendar.get(Calendar.MONTH)+1))+"-"
                +(lCalendar.get(Calendar.DAY_OF_MONTH)>9 ? lCalendar.get(Calendar.DAY_OF_MONTH):"0"+lCalendar.get(Calendar.DAY_OF_MONTH));
        mWeek = RegisteredRecordManager.getWeek(mDate);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_class_select_tab, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        uiAction();
    }

    private void uiAction(){
        findView();
        initViews();
        if(toGetServerTime){
            //当没有缓存时(状态为false)，证明第一进入此页，去获取服务器时间
            getServerTime();
        }

    }

    private void initPageAndGetData(){
        getMyClass();
        initSubTabData();
        initPageView();
    }



    //绑定控件
    private void findView() {
        mTabLayout = (TabLayout) mActivity.findViewById(R.id.class_list_tablayout);
        mViewPager = (ViewPager) mActivity.findViewById(R.id.class_list_viewpager);
    }


    //设置标题等
    private void initViews() {

        mToolBarManager.setTitle("我的班级")
                .showRightButton(false)
                .setRightText("")
                .showBack(true);
    }


    private void getServerTime(){
        presenter.getServerTime(TeacherID);
    }

    private void getMyClass(){
        presenter.getMyClass(TeacherID,mDate,0,mWeek);
    }

    MyClassSubTabInfoDto lMyClassSubTabInfoDto;
    private void initSubTabData(){
        //无论服务器时间是否能返回，都要列出我的班级且存入当前时间数据
        LogUtil.Log("lenita","MyClassRecordSelectTabFragment date = "+ mDate);
        lMyClassSubTabInfoDto = new MyClassSubTabInfoDto();
        lMyClassSubTabInfoDto.setIsSubTab(true);
        lMyClassSubTabInfoDto.setDate(mDate);
        lMyClassSubTabInfoDto.setWeek(mWeek);
        RegisteredRecordManager.getMyClassRecordDataCache().setMyClassSubTabInfoDto(lMyClassSubTabInfoDto);
    }

    private void initPageView(){
        mTitleList = new ArrayList<>();
        mViewList = new ArrayList<>();
        int titleNum = 0;

        for(int i = 0; i < mClassLists.size();i++){
            mTitleList.add(mClassLists.get(i).getClassName());
            titleNum += mClassLists.get(i).getClassName().length();
        }

        LogUtil.Log("lenita","length"+titleNum);
        if(mTitleList.size() == 2){
            if(mClassLists.get(0).getClassName().length() < 8 && mClassLists.get(1).getClassName().length() < 8 ){
                mTabLayout.setTabMode(TabLayout.MODE_FIXED);
            }else {
                mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            }
        }else if(mTitleList.size() == 3){
            if(mClassLists.get(0).getClassName().length() < 6 && mClassLists.get(1).getClassName().length() < 6 && mClassLists.get(2).getClassName().length() < 6  ) {
                mTabLayout.setTabMode(TabLayout.MODE_FIXED);
            }else {
                mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            }
        }else if(mTitleList.size() == 4){
            if(mClassLists.get(0).getClassName().length() < 4 && mClassLists.get(1).getClassName().length() < 4 && mClassLists.get(2).getClassName().length() < 4  && mClassLists.get(3).getClassName().length() < 4  ) {
                mTabLayout.setTabMode(TabLayout.MODE_FIXED);
            }else {
                mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            }
        }else {
            mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }



        for (int i = 0; i < mTitleList.size(); i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(i)));
            MyClassRecordSubTabFragment lMyClassRecordSubTabFragment  = new MyClassRecordSubTabFragment.Builder(mActivity,mClassLists.get(i).getId(),getServerTimeFailedCauseNotNetwork,lMyClassSubTabInfoDto).create();
            mViewList.add(lMyClassRecordSubTabFragment);
        }

        FavouritePageAdapter adapter = new FavouritePageAdapter(getChildFragmentManager(), mViewList);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //page选择定后进行数据刷新
                MyClassRecordSubTabFragment lSubTabFragment =(MyClassRecordSubTabFragment) adapter.getFragment(position);
                lSubTabFragment.updateClassInfo();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//                MyClassRecordSubTabFragment lSubTabFragment =(MyClassRecordSubTabFragment) mViewList.get(position);
//                lSubTabFragment.updateClassInfo();
        /*mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                LogUtil.Log("lenita","onTabReselected()");
            }
        });*/

    }


    @Override
    public void onDetach() {
        super.onDetach();
        presenter.detachView(false);
    }

    @Override
    public boolean onBackPress() {
        //缓存清空
        RegisteredRecordManager.destroyMyClassRecordDataCache();
        AlreadyRegisteredRecordActivity.mMyClassesLoadCatch = false;
        Log.e("lenita","onBackPress -- MyClassRecordSelectTabFragment");
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
            LogUtil.Log("lenita","lArguments = "+lArguments.toString());
            lArguments.putInt(TAB_POSITION, position);
            List.setArguments(lArguments);
            return List;
        }

        public Fragment getFragment(int position){
            Fragment List = mFragmentArrayList.get(position);
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
