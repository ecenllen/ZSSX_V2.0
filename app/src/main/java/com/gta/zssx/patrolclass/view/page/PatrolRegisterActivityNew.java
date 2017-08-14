package com.gta.zssx.patrolclass.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.patrolclass.fragment.PatrolPointsDetailsFragment;
import com.gta.zssx.patrolclass.model.entity.PatrolRegisterBeanNew;
import com.gta.zssx.patrolclass.model.entity.RegisterInterfaceEntity;
import com.gta.zssx.patrolclass.presenter.PatrolRegisterPresenterNew;
import com.gta.zssx.patrolclass.view.PatrolRegisterViewNew;
import com.gta.zssx.patrolclass.view.adapter.PatrolPointsDetailsAdapter;
import com.gta.zssx.patrolclass.view.dialog.DatePickerView;
import com.gta.zssx.patrolclass.view.dialog.DeleteDialog;
import com.gta.zssx.pub.base.BaseMvpActivity;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.gta.zssx.patrolclass.view.page.PatrolChooseSectionActivity.RESULT_CODE_SECTION;
import static com.gta.zssx.patrolclass.view.page.PatrolChooseSectionActivity.SECTION_DATA;
import static com.gta.zssx.patrolclass.view.page.PatrolChooseSectionActivity.SECTION_ID;
import static com.gta.zssx.patrolclass.view.page.PatrolChooseSectionActivity.SECTION_NAME;

/**
 * Created by liang.lu on 2017/3/1 16:49.
 */

public class PatrolRegisterActivityNew extends BaseMvpActivity<PatrolRegisterViewNew, PatrolRegisterPresenterNew>
        implements PatrolRegisterViewNew, View.OnClickListener, PatrolPointsDetailsFragment.DeleteItemListener
        , PatrolPointsDetailsFragment.ListChangedListener {

    public static final String PATROL_CLASS_SECTION = "patrol_class_section";
    public static final int REQUEST_CODE_SECTION = 200;
    private PatrolPointsDetailsAdapter mPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private List<Fragment> mFragmentList;
    private TextView mAddTeacher;
    private List<String> mTabTitleList;
    private PatrolRegisterBeanNew mPatrolRegisterBean;
    private List<PatrolRegisterBeanNew.TeacherListBean> mTeacherListBeen;
    private TextView mPatrolClassDate;
    private TextView mClassName;
    private TextView mCurrentSection;
    private TextView mSubmitTv;
    private RelativeLayout mLayoutDate;
    private RelativeLayout mLayoutClass;
    private RelativeLayout mLayoutSection;
    private RegisterInterfaceEntity mEntity;
    private List<PatrolRegisterBeanNew.SectionsListBean> mSectionData;

    @Override
    public int getLayoutId () {
        return R.layout.activity_patrol_register_new;
    }

    @Override
    protected void initView () {
        findViews ();
        initToolbar ();
        setOnClick ();
        init ();
    }

    private void setOnClick () {
        mLayoutDate.setOnClickListener (this);
        mLayoutClass.setOnClickListener (this);
        mLayoutSection.setOnClickListener (this);
        mAddTeacher.setOnClickListener (this);
        mSubmitTv.setOnClickListener (this);
    }

    private void initToolbar () {
        mToolBarManager
                .showBack (true)
                .setTitle (R.string.patrol_register);
        mToolBarManager.getToolbar ().setNavigationOnClickListener (v -> {
            DeleteDialog deleteDialog = new DeleteDialog (PatrolRegisterActivityNew.this);
            deleteDialog.setDismissListener (this :: onBackPressed);
            deleteDialog.showDialog (true, "继续编辑", "确定要退出吗？", null, false);
            deleteDialog.setExitText ("退出");
        });
    }

    private void findViews () {
        mAddTeacher = (TextView) findViewById (R.id.text_view_add_teacher);
        mSubmitTv = (TextView) findViewById (R.id.submit_patrol);
        mViewPager = (ViewPager) findViewById (R.id.patrol_viewpage);
        mTabLayout = (TabLayout) findViewById (R.id.patrol_tablayout);
        mPatrolClassDate = (TextView) findViewById (R.id.tv_patrolRegister_date);
        mClassName = (TextView) findViewById (R.id.tv_patrolRegister_className);
        mCurrentSection = (TextView) findViewById (R.id.tv_patrolRegister_sections);
        mLayoutDate = (RelativeLayout) findViewById (R.id.rl_date);
        mLayoutClass = (RelativeLayout) findViewById (R.id.rl_class);
        mLayoutSection = (RelativeLayout) findViewById (R.id.rl_section);
    }

    private void init () {
        // TODO: 2017/4/19 根据页面传来的参数信息请求接口获取登记数据
        mEntity = new RegisterInterfaceEntity ();
        presenter.getPatrolRegisterData (mEntity);
        if (mPatrolRegisterBean != null) {
            mPatrolClassDate.setText (mPatrolRegisterBean.getCurrentdate ());
            mClassName.setText (mPatrolRegisterBean.getClassName ());
            mCurrentSection.setText (mPatrolRegisterBean.getSectionName ());
        }
    }

    public static void start (Context context) {
        final Intent lIntent = new Intent (context, PatrolRegisterActivityNew.class);
        if (!(context instanceof Activity)) {
            lIntent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity (lIntent);
    }

    @NonNull
    @Override
    public PatrolRegisterPresenterNew createPresenter () {
        return new PatrolRegisterPresenterNew ();
    }

    @Override
    public void onClick (View v) {
        switch (v.getId ()) {
            case R.id.text_view_add_teacher:
                //添加老师
                presenter.addTeacher ();
                break;
            case R.id.rl_date:
                //更换日期
                changePatrolDate ();
                break;
            case R.id.rl_class:
                //更换班级
                break;
            case R.id.rl_section:
                //更换上课节次
                changeSection ();
                break;
            case R.id.submit_patrol:
                //巡课登记提交
                mPatrolRegisterBean.setTeacherList (mTeacherListBeen);
                presenter.submitRegisterData (mPatrolRegisterBean);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult (requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SECTION) {
            switch (resultCode) {
                case RESULT_CODE_SECTION:
                    mSectionData = (List<PatrolRegisterBeanNew.SectionsListBean>) data.getExtras ().getSerializable (SECTION_DATA);
                    String sectionName = data.getStringExtra (SECTION_NAME);
                    int sectionId = data.getIntExtra (SECTION_ID, -1);
                    mCurrentSection.setText (sectionName);
                    mEntity.setSectionId (sectionId);
                    // TODO: 2017/4/19 根据选择的节次重新加载页面数据
                    //  presenter.getPatrolRegisterData (mEntity);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 点击更换节次，如果mSectionData不是null的话说明是从节次页面传回来的，就直接用
     */
    private void changeSection () {
        if (mSectionData == null && mPatrolRegisterBean != null) {
            mSectionData = mPatrolRegisterBean.getSectionsList ();
            for (int i = 0; i < mSectionData.size (); i++) {
                if (mSectionData.get (i).getType () == 0) {
                    if (mSectionData.get (i).getSectionId () == mPatrolRegisterBean.getSectionId ()) {
                        mSectionData.get (i).setCheck (true);
                    }
                }
            }
        }
        Intent mIntent = new Intent (this, PatrolChooseSectionActivity.class);
        Bundle mBundle = new Bundle ();
        mBundle.putSerializable (PATROL_CLASS_SECTION, (Serializable) mSectionData);
        mIntent.putExtras (mBundle);
        startActivityForResult (mIntent, REQUEST_CODE_SECTION);
    }

    /**
     * 更换巡视日期，弹出日期选择控件
     */
    private void changePatrolDate () {
        DatePickerView.showView (this, mPatrolClassDate.getText ().toString (), date -> {
            SimpleDateFormat mLineFormat = new SimpleDateFormat ("yyyy-MM-dd", Locale.getDefault ());
            SimpleDateFormat mWordsFormat = new SimpleDateFormat ("yyyy年MM月dd日", Locale.getDefault ());
            String mWordsDate = mWordsFormat.format (date);
            String mLineDate = mLineFormat.format (date);
            Date mDate = null;
            if (mPatrolRegisterBean != null) {
                try {
                    //服务器时间
                    mDate = mLineFormat.parse (mPatrolRegisterBean.getServicedate ());
                } catch (ParseException e) {
                    e.printStackTrace ();
                }
                if (date.after (mDate)) {
                    //选择的时间在当前服务器时间之后（不能选择当前日期之后的日期），给出提示
                    showDateSelectErrorDialog (getResources ().getString (R.string.patrol_class_date_error), true);
                } else {
                    //还要先验证日期的有效性，是不是在可选学期之内，访问接口
                    mEntity.setClassId (mPatrolRegisterBean.getClassId ());
                    mEntity.setWordsDate (mWordsDate);
                    mEntity.setLineDate (mLineDate);
                    mEntity.setSectionId (mPatrolRegisterBean.getSectionId ());
                    presenter.VerificationDate (mEntity);
                }
            }
        });
    }

    /**
     * fragment中点击删除的回调，在此页面进行数据的更新处理
     *
     * @param teacherListBeen 待处理数据集合
     */
    @Override
    public void onDelete (List<PatrolRegisterBeanNew.TeacherListBean> teacherListBeen) {
        int size = mFragmentList.size ();
        int currentItem = mViewPager.getCurrentItem ();
        if (size > 0) {
            mFragmentList.remove (currentItem);
            teacherListBeen.remove (currentItem);
            mTabTitleList.remove (currentItem);
            for (int i = 0; i < mFragmentList.size (); i++) {
                PatrolPointsDetailsFragment fragment = (PatrolPointsDetailsFragment) mFragmentList.get (i);
                fragment.setPosition (i);
                fragment.setClose (true);
            }
            if (mPagerAdapter != null) {
                mPagerAdapter = null;
            }
            mPagerAdapter = new PatrolPointsDetailsAdapter (getSupportFragmentManager (), mFragmentList,
                    mTabTitleList);
            mViewPager.setAdapter (mPagerAdapter);
            if (currentItem == size - 1 || currentItem == 0) {
                mViewPager.setCurrentItem (currentItem - 1);
            } else {
                mViewPager.setCurrentItem (currentItem);
            }
            mTabLayout.setupWithViewPager (mViewPager);
        }
        this.mTeacherListBeen = teacherListBeen;
    }

    /**
     * 访问接口得到登记页面的数据，通过回调拿到数据进行fragment的创建及页面数据的展示
     *
     * @param patrolRegisterBeanNew 登记页面的数据
     */
    @Override
    public void setFragmentLists (PatrolRegisterBeanNew patrolRegisterBeanNew) {
        this.mPatrolRegisterBean = patrolRegisterBeanNew;
        mTeacherListBeen = patrolRegisterBeanNew.getTeacherList ();
        if (mTeacherListBeen == null) {
            mTeacherListBeen = new ArrayList<> ();
            return;
        }
        mFragmentList = new ArrayList<> ();
        mTabTitleList = new ArrayList<> ();
        for (int i = 0; i < mTeacherListBeen.size (); i++) {
            PatrolPointsDetailsFragment fragment = PatrolPointsDetailsFragment.newInstance (
                    (ArrayList<PatrolRegisterBeanNew.TeacherListBean>) mTeacherListBeen, i);
            fragment.setDeleteListener (this);
            fragment.setChangedListener (this);
            mTabTitleList.add (mTeacherListBeen.get (i).getTeacherName ());
            mFragmentList.add (fragment);
        }
        mPagerAdapter = new PatrolPointsDetailsAdapter (getSupportFragmentManager (), mFragmentList,
                mTabTitleList);
        mViewPager.setAdapter (mPagerAdapter);
        mTabLayout.setupWithViewPager (mViewPager);
    }

    /**
     * 点击增加，访问添加老师接口，回调得到数据，更新adapter
     *
     * @param mTeacherBean mTeacherBean
     */
    @Override
    public void addTeacherData (PatrolRegisterBeanNew.TeacherListBean mTeacherBean) {
        mTeacherListBeen.add (mTeacherBean);
        PatrolPointsDetailsFragment fragment = PatrolPointsDetailsFragment.newInstance (
                (ArrayList<PatrolRegisterBeanNew.TeacherListBean>) mTeacherListBeen, mTeacherListBeen.size () - 1);
        fragment.setDeleteListener (this);
        fragment.setChangedListener (this);
        mFragmentList.add (fragment);
        mTabTitleList.add (mTeacherListBeen.get (mTeacherListBeen.size () - 1).getTeacherName ());
        mPagerAdapter.setTabTitleList (mTabTitleList);
        mPagerAdapter.setList (mFragmentList);
        mPagerAdapter.notifyDataSetChanged ();
        mViewPager.setCurrentItem (mTeacherListBeen.size () - 1);
    }

    /**
     * 每个fragment中的数据发生改变后，要更新在该页面并且更新到每个fragment当中
     * 更换教师后，tab上的教师名字也要更新
     *
     * @param teacherListBeans fragment中更新过来的数据集合
     */
    @Override
    public void setData (ArrayList<PatrolRegisterBeanNew.TeacherListBean> teacherListBeans) {
        this.mTeacherListBeen = teacherListBeans;
        for (int i = 0; i < mFragmentList.size (); i++) {
            PatrolPointsDetailsFragment fragment = (PatrolPointsDetailsFragment) mFragmentList.get (i);
            fragment.setTeacherListBeans (teacherListBeans);
        }
        if (!teacherListBeans.get (mViewPager.getCurrentItem ()).getTeacherName ()
                .equals (mTabTitleList.get (mViewPager.getCurrentItem ()))) {
            mTabTitleList.set (mViewPager.getCurrentItem (), teacherListBeans
                    .get (mViewPager.getCurrentItem ()).getTeacherName ());
            mPagerAdapter.setTabTitleList (mTabTitleList);
            mPagerAdapter.notifyDataSetChanged ();
        }
    }

    /**
     * 选择日期不合理错误提示框
     *
     * @param msg         dialog要显示的内容
     * @param hasListener 是否需要确定回调
     */
    public void showDateSelectErrorDialog (String msg, boolean hasListener) {
        DeleteDialog.OnErrorDialogClickListener listener = null;
        if (hasListener) {
            //提示框点击确定回调
            listener = this :: changePatrolDate;
        }
        new DeleteDialog (this).showDialog (false, "确定", msg, listener, false);
    }

    /**
     * 日期重新选择且通过了日期验证后，根据重新选择的日期进行网络请求更新界面数据
     *
     * @param entity entity
     */
    @Override
    public void refreshData (RegisterInterfaceEntity entity) {
        mPatrolClassDate.setText (entity.getWordsDate ());
        //        presenter.getPatrolRegisterData (entity);
    }

}
