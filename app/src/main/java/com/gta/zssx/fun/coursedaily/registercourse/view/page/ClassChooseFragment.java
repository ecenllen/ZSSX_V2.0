package com.gta.zssx.fun.coursedaily.registercourse.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.gta.zssx.AppConfiguration;
import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.ClassChooseBean01;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SortClassBean;
import com.gta.zssx.fun.coursedaily.registercourse.presenter.ClassChoosePresenter;
import com.gta.zssx.fun.coursedaily.registercourse.view.ClassChooseView;
import com.gta.zssx.fun.coursedaily.registercourse.view.adapter.ClassChooseAdapter;
import com.gta.zssx.fun.coursedaily.registercourse.view.base.RegisterCourseActivity;
import com.gta.zssx.fun.coursedaily.registercourse.view.base.RegisterCourseBaseFragment;
import com.gta.zssx.fun.coursedaily.registercourse.view.base.RegisterCourseFragmentBuilder;
import com.gta.zssx.fun.coursedaily.registercourse.view.pageV2.ClassSearchActivity;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.pub.widget.easysidebar.EasyFloatingImageView;
import com.gta.zssx.pub.widget.easysidebar.EasyImageSection;
import com.gta.zssx.pub.widget.easysidebar.EasyRecyclerViewSidebar;
import com.gta.zssx.pub.widget.easysidebar.EasySection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
public class ClassChooseFragment extends RegisterCourseBaseFragment<ClassChooseView, ClassChoosePresenter> implements ClassChooseView
        , EasyRecyclerViewSidebar.OnTouchSectionListener {

    public static final String PAGE_TAG = ClassChooseFragment.class.getSimpleName();

    private EasyRecyclerViewSidebar mSidebar;
    private TextView mFloatingTv;
    private EasyFloatingImageView imageFloatingIv;
    private RecyclerView mRecyclerView;
    public RelativeLayout mFloatingRl;
    public Set<SortClassBean.ClassByYear> mClassByYearRecords;
    public Set<ClassChooseBean01.ClassListBean> mClassListBeanSet;
    public TextView mSearchTv;

    @NonNull
    @Override
    public ClassChoosePresenter createPresenter() {
        return new ClassChoosePresenter();
    }


    @Override
    public void addSuccess() {
        UserBean lUserBean = null;
        try {
            lUserBean = AppConfiguration.getInstance().getUserBean();
        } catch (Exception e) {
            e.printStackTrace();
        }
        AppConfiguration.getInstance().setFirstIn(lUserBean);
//        AppConfiguration.getInstance().finishActivity();
        RegisterCourseActivity.start(mActivity, ClassDisplayFragment.PAGE_TAG);
        mActivity.finish();
    }

    @Override
    public void showClass01(List<ClassChooseBean01.ClassListBean> classListBeen) {
        displayList01(classListBeen);
    }

    @Override
    public void showEmpty() {

    }

    /**
     * 显示列表
     */

    private void displayList01(List<ClassChooseBean01.ClassListBean> classListBeen) {
        ClassChooseAdapter.Listener lListener = new ClassChooseAdapter.Listener() {
//            @Override
//            public void checkBoxClick(boolean isCheck, ClassChooseBean01.ClassListBean classListBean, int position) {
//                if (isCheck) {
//                    mClassListBeanSet.add(classListBean);
//                } else {
//                    mClassListBeanSet.remove(classListBean);
//                }
//                mToolBarManager.getRightButton().setEnabled(mClassListBeanSet.size() > 0);
//            }

            @Override
            public void checkBoxAllClick(boolean isCheck, Set<ClassChooseBean01.ClassListBean> classListBeanSet, int position, List<ClassChooseBean01.ClassListBean> r) {
//                if (isCheck) {
//                    mClassListBeanSet.addAll(classListBeanSet);
//                } else {
//                    mClassListBeanSet.removeAll(classListBeanSet);
//                }
                mClassListBeanSet = classListBeanSet;
                mToolBarManager.getRightButton().setEnabled(mClassListBeanSet.size() > 0);
            }

        };
        ClassChooseAdapter lChooseAdapter = new ClassChooseAdapter(mActivity, classListBeen, lListener, presenter.getSparseIntArray());

        mSidebar.setSections(presenter.getEasySections());
        mRecyclerView.setAdapter(lChooseAdapter);
    }


    @Override
    public void onTouchImageSection(int sectionIndex, EasyImageSection imageSection) {

    }

    @Override
    public void onTouchLetterSection(int sectionIndex, EasySection letterSection) {
        int lI = presenter.getSparseIntArray().get(sectionIndex);
        this.mFloatingTv.setVisibility(View.VISIBLE);
        this.imageFloatingIv.setVisibility(View.INVISIBLE);
        this.mFloatingTv.setText(letterSection.letter);
        scrollToPosition(lI);
    }

    public static class Builder extends RegisterCourseFragmentBuilder<ClassChooseFragment> {


        public Builder(Context context) {
            super(context);
        }

        @Override
        public ClassChooseFragment create() {
            return new ClassChooseFragment();
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
        mClassByYearRecords = new HashSet<>();
        mClassListBeanSet = new HashSet<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_class_choose, container, false);
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
        Map<Integer,String> classMap = new HashMap<>();
        //网络请求
        presenter.loadClass01(classMap);

        mSearchTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent lIntent = new Intent(mActivity, ClassSearchActivity.class);
                startActivity(lIntent);

            }
        });
    }


    //设置标题等
    private void initViews() {

        mToolBarManager.getRightButton().setEnabled(false);

        mToolBarManager
                .showBack(false)
                .setTitle("添加班级")
                .showRightButton(true)
                .setRightText("完成")
                .clickRightButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<ClassChooseBean01.ClassListBean> lClassByYears = presenter.sortSet(mClassListBeanSet);
                        presenter.addClass(presenter.getAddClassBean(lClassByYears));
                    }
                });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));

        this.mSidebar.setFloatView(mFloatingRl);
        this.mSidebar.setOnTouchSectionListener(this);

    }

    //绑定控件
    private void findView() {
        mRecyclerView = (RecyclerView) mActivity.findViewById(R.id.section_rv_01);
        mSidebar = (EasyRecyclerViewSidebar) mActivity.findViewById(R.id.section_sidebar);
        mFloatingTv = (TextView) mActivity.findViewById(R.id.section_floating_tv);
        imageFloatingIv = (EasyFloatingImageView) mActivity.findViewById(R.id.section_floating_iv);
        mFloatingRl = (RelativeLayout) mActivity.findViewById(R.id.section_floating_rl);
        mSearchTv = (TextView) mActivity.findViewById(R.id.search_tv);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.detachView(false);
    }

    /**
     * 指定recycleview滚动的位置
     *
     * @param position
     */
    private void scrollToPosition(int position) {
        ((LinearLayoutManager) (this.mRecyclerView.getLayoutManager())).scrollToPositionWithOffset(position, 0);
    }
}
