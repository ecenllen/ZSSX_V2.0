package com.gta.zssx.patrolclass.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ExpandableListView;

import com.blankj.utilcode.util.ToastUtils;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.R;
import com.gta.zssx.patrolclass.model.entity.ChooseTeacherEntity;
import com.gta.zssx.patrolclass.model.entity.ChooseTeacherSearchEntity;
import com.gta.zssx.patrolclass.model.entity.PatrolRegisterBeanNew;
import com.gta.zssx.patrolclass.presenter.ChooseTeacherPresenter;
import com.gta.zssx.patrolclass.view.ChooseTeacherView;
import com.gta.zssx.patrolclass.view.adapter.ChooseTeacherAdapter;
import com.gta.zssx.pub.base.BaseMvpActivity;
import com.gta.zssx.pub.util.RxBus;

import java.util.ArrayList;
import java.util.List;

import static com.gta.zssx.patrolclass.view.page.DockScoreNewActivity.OPTION_BEANS;

/**
 * Created by liang.lu on 2016/8/16 14:55.
 * 教师选择界面
 */
public class ChooseTeacherActivity extends BaseMvpActivity<ChooseTeacherView, ChooseTeacherPresenter>
        implements ChooseTeacherView, ExpandableListView.OnChildClickListener {
    public static final String STATUS = "status";
    public static final String STATUS_ONE = "1";
    public static final String STATUS_TWO = "2";
    public static final String WHICH_PAGE = "which_page";
    public static final String PATROL_CLASS = "patrol_class";
    private ExpandableListView mListView;
    private List<ChooseTeacherEntity> entities;
    private String title;
    //区分是登记页面进入还是第二层级进入   1  登记页面进入   2  第二层级进入
    private String status;
    private String whichPage;//区分是哪个页面进来的
//    private String mTeacherId;
    private ArrayList<PatrolRegisterBeanNew.TeacherListBean> mTeacherBeanList;

    @Override
    public int getLayoutId () {
        return R.layout.activity_choose_teacher_expandable;
    }

    @Override
    protected void initData () {
        super.initData ();
        title = getIntent ().getStringExtra ("title");
        status = getIntent ().getStringExtra (STATUS);
        whichPage = getIntent ().getStringExtra (WHICH_PAGE);
        mTeacherBeanList = (ArrayList<PatrolRegisterBeanNew.TeacherListBean>) getIntent ().getSerializableExtra (OPTION_BEANS);
        //        mTeacherId = getIntent ().getStringExtra (DockScoreNewActivity.TEACHER_ID);
        if (title == null) {
            title = getString (R.string.please_choose);
        }
        deptId = getIntent ().getStringExtra ("deptId");
        if (deptId == null) {
            deptId = "-1";
        }
    }

    @Override
    protected void initView () {
        mListView = (ExpandableListView) findViewById (R.id.expendlist);
        mListView.setDivider (null);
        mListView.setOnChildClickListener (this);
        initToolBar ();
    }

    @Override
    protected void requestData () {
        super.requestData ();
        presenter.getChooseTeacherDatas ();
    }

    private void initToolBar () {
        mToolBarManager.showBack (true)
                .setTitle (title)
                .showIvSearch (true)
                .clickIvSearch (v -> {
                    //点击搜索
                    ChooseTeacherSearchActivity.start (ChooseTeacherActivity.this, PATROL_CLASS);
                });
    }

    @NonNull
    @Override
    public ChooseTeacherPresenter createPresenter () {
        return new ChooseTeacherPresenter ();
    }

    @Override
    public void showResult (List<ChooseTeacherEntity> entities) {
        this.entities = entities;
        ChooseTeacherAdapter mAdapter = new ChooseTeacherAdapter (this);
        mAdapter.setEntities (entities);
        mListView.setAdapter (mAdapter);
        if (status.equals (STATUS_TWO)) {
            for (int i = 0; i < mAdapter.getGroupCount (); i++) {
                mListView.expandGroup (i);
            }
        }

    }

    @Override
    public String getDeptId () {
        return deptId;
    }

    public static void start (Context context) {
        final Intent lIntent = new Intent (context, ChooseTeacherActivity.class);
        if (!(context instanceof Activity)) {
            lIntent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity (lIntent);
    }

    String deptId = "-1";


    @Override
    public boolean onChildClick (ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        ChooseTeacherEntity.DeptListBean deptListBean = entities.get (groupPosition).getDeptList ().get (childPosition);
        if (deptListBean.getType () == 0) {//点击的是老师
            if (mTeacherBeanList != null) {
                for (PatrolRegisterBeanNew.TeacherListBean teacherListBean : mTeacherBeanList) {
                    String teacherId = teacherListBean.getTeacherId ();
                    if (teacherId.equals (deptListBean.getDeptId ())) {
                        ToastUtils.showShortToast("请选择不同上课教师");
                        return false;
                    }
                }
            }
            ChooseTeacherSearchEntity entity = new ChooseTeacherSearchEntity ();
            entity.setTeacherId (deptListBean.getDeptId ());
            entity.setName (deptListBean.getDeptName ());
            Intent mIntent = new Intent ();
            if (whichPage.equals (DockScoreNewActivity.DOCK_SCORE_NEW_PAGE)) {
                mIntent.setClass (this, DockScoreNewActivity.class);
                AppConfiguration.getInstance ().finishActivity (ChooseTeacherActivity.class);
            } else if (whichPage.equals (PatrolRegisterActivity.PATROL_REGISTER_PAGE)) {
                mIntent.setClass (this, PatrolRegisterActivity.class);
                startActivity (mIntent);
            }
            RxBus.getDefault ().post (entity);
        } else {//点击的是部门，请求数据，跳转至另一个页面显示
            deptId = deptListBean.getDeptId ();
            title = deptListBean.getDeptName ();
            Intent intent = new Intent ();
            intent.setClass (this, ChooseTeacherActivity.class);
            intent.putExtra ("deptId", deptId);
            intent.putExtra ("title", title);
            //            intent.putExtra (DockScoreNewActivity.TEACHER_ID, mTeacherId);
            intent.putExtra (STATUS, STATUS_TWO);
            intent.putExtra (WHICH_PAGE, whichPage);
            intent.putExtra (OPTION_BEANS, mTeacherBeanList);
            startActivity (intent);
        }
        return false;
    }

}
