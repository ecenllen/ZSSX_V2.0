package com.gta.zssx.patrolclass.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.gta.zssx.R;
import com.gta.zssx.patrolclass.model.entity.ClassChooseEntity;
import com.gta.zssx.patrolclass.presenter.AddClassChoosePresenter;
import com.gta.zssx.patrolclass.view.AddClassChooseView;
import com.gta.zssx.patrolclass.view.adapter.AddClassChooseAdapter;
import com.gta.zssx.pub.base.BaseMvpActivity;
import com.gta.zssx.pub.widget.easysidebar.EasyFloatingImageView;
import com.gta.zssx.pub.widget.easysidebar.EasyImageSection;
import com.gta.zssx.pub.widget.easysidebar.EasyRecyclerViewSidebar;
import com.gta.zssx.pub.widget.easysidebar.EasySection;

import java.util.List;

/**
 * [Description]
 * <p>   按计划巡课添加班级页面和随机巡课添加班级页面
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/7/4.
 * @since 1.0.0
 */
public class AddClassChooseActivity extends BaseMvpActivity<AddClassChooseView, AddClassChoosePresenter>
        implements AddClassChooseView, EasyRecyclerViewSidebar.OnTouchSectionListener,
        AddClassChooseAdapter.AddClassItemClicklistener {


    private EasyRecyclerViewSidebar mSidebar;
    private TextView mFloatingTv, noDataTv;
    private EasyFloatingImageView imageFloatingIv;
    private RecyclerView mRecyclerView;
    public RelativeLayout mFloatingRl;
    public TextView mSearchTv;
    private AddClassChooseAdapter mAdapter;
    /**
     * 记录当前页面是属于那个巡课的
     * random:随机巡课
     * plan:按计划巡课
     */
    private String classType;
    private String type;//区分是新增进入，还是修改进入 1  新增  2  修改
    private String date;

    List<ClassChooseEntity.ClassListBean> addPlanClassDatas;

    @NonNull
    @Override
    public AddClassChoosePresenter createPresenter () {
        return new AddClassChoosePresenter ();
    }

    //msg只能是plan和random
    public static void start (Context context, String msg, String type, String date) {
        final Intent lIntent = new Intent (context, AddClassChooseActivity.class);
        lIntent.putExtra ("classType", msg);
        lIntent.putExtra ("type", type);
        lIntent.putExtra ("date", date);
        if (!(context instanceof Activity)) {
            lIntent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity (lIntent);
    }

    @Override
    public int getLayoutId () {
        return R.layout.fragment_class_choose;
    }

    //初始化view
    protected void initView () {
        findViews ();
        initToolbar ();
        setOnInteractListener ();

        mRecyclerView.setHasFixedSize (true);
        mRecyclerView.setLayoutManager (new LinearLayoutManager (this));

        mAdapter = new AddClassChooseAdapter (classType);
        mRecyclerView.setAdapter (mAdapter);
        if (classType.equals ("random")) {  //如果是随机巡课，设置item点击监听器
            mAdapter.setItemListener (this);
        }
        this.mSidebar.setFloatView (mFloatingRl);
        this.mSidebar.setOnTouchSectionListener (this);
    }

    private void initToolbar () {
        mToolBarManager.getRightButton ().setEnabled (true);
        mToolBarManager
                .showBack (true)
                .setTitle (getString ())
                .showRightButton (true);
    }

    @Override
    protected void requestData () {
        super.requestData ();
        presenter.loadClass ();
    }

    //搜索事件处理
    private void setOnInteractListener () {
        mSearchTv.setOnClickListener (v -> PlanClassSearchActivity.start (AddClassChooseActivity.this, classType, date));
    }

    //绑定控件
    private void findViews () {
        mRecyclerView = (RecyclerView) findViewById (R.id.section_rv_01);
        noDataTv = (TextView) findViewById (R.id.tv_non);
        mSidebar = (EasyRecyclerViewSidebar) findViewById (R.id.section_sidebar);
        mFloatingTv = (TextView) findViewById (R.id.section_floating_tv);
        imageFloatingIv = (EasyFloatingImageView) findViewById (R.id.section_floating_iv);
        mFloatingRl = (RelativeLayout) findViewById (R.id.section_floating_rl);
        mSearchTv = (TextView) findViewById (R.id.search_tv);
    }

    //用于页面间数据接收
    protected void initData () {
        classType = getIntent ().getStringExtra ("classType");
        type = getIntent ().getStringExtra ("type");
        date = getIntent ().getStringExtra ("date");
        if (date == null) {
            date = "";
        }
    }

    @Override
    public void onTouchImageSection (int sectionIndex, EasyImageSection imageSection) {

    }

    @Override
    public void onTouchLetterSection (int sectionIndex, EasySection letterSection) {
        int lI = presenter.getSparseIntArray ().get (sectionIndex);
        this.mFloatingTv.setVisibility (View.VISIBLE);
        this.imageFloatingIv.setVisibility (View.INVISIBLE);
        this.mFloatingTv.setText (letterSection.letter);
        scrollToPosition (lI);
    }

    /**
     * 指定recycleView滚动的位置
     *
     * @param position position
     */
    private void scrollToPosition (int position) {
        ((LinearLayoutManager) (this.mRecyclerView.getLayoutManager ())).scrollToPositionWithOffset (position, 0);
        //        mRecyclerView.smoothScrollToPosition (position);
    }

    @Override
    public void showSuccessDatas (List<ClassChooseEntity.ClassListBean> addPlanClassDatas) {
        this.addPlanClassDatas = addPlanClassDatas;

        mSidebar.setSections (presenter.getEasySections ());

        mAdapter.setDatas (addPlanClassDatas);
        mAdapter.notifyDataSetChanged ();
        if (!classType.equals ("random")) {
            mToolBarManager.setRightText ("下一步")
                    .clickRightButton (v -> {
                        //点击下一步，跳转到选择班级结果页面
                        if (presenter.isCanFinish ()) {
                            presenter.uploadChooseClass ();
                        } else {
                            ToastUtils.showShortToast("请选择班级");
                        }
                    });
        }

    }

    @Override
    public void intoClassesPage () {
        //        PatrolClassActivity.start4Result (this);
        finish ();
        PlanProtalResultActivity.start (this, "1", date);
    }

    @Override
    public void showEmpty () {
        mRecyclerView.setVisibility (View.GONE);
        noDataTv.setVisibility (View.VISIBLE);
        mSearchTv.setVisibility (View.GONE);
    }

    //随机巡课item点击事件
    @Override
    public void onClick (int type, int position) {
        String deptId = addPlanClassDatas.get (position).getDeptId () + "";
        int classId = addPlanClassDatas.get (position).getId ();
        String className = addPlanClassDatas.get (position).getClassName ();
        if (this.type.equals ("2")) {
            Intent lData = new Intent ();
            Bundle lBundle = new Bundle ();
            lBundle.putString ("deptId", deptId);
            lBundle.putInt ("classId", classId);
            lBundle.putString ("className", className);
            lBundle.putString ("isType", "0");//标明是随机还是按计划  0  随机  1  按计划
            lData.putExtras (lBundle);
            setResult (10, lData);
            finish ();
        } else {
            PatrolRegisterActivity.start (this, deptId, classId, date, 0, 0, "1", 0, "0", className);
        }
    }

    public String getString () {
        if (classType.equals ("random")) {
            return "选择随机巡课班级";
        }
        return "添加计划巡课班级";
    }
}
