package com.gta.zssx.fun.classroomFeedback.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.gta.zssx.R;
import com.gta.zssx.fun.classroomFeedback.presenter.CourseSearchPresenter;
import com.gta.zssx.fun.classroomFeedback.view.CourseSearchView;
import com.gta.zssx.fun.classroomFeedback.view.adapter.ClassroomCourseAddAdapter;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.DetailItemShowBean;
import com.gta.zssx.pub.base.BaseMvpActivity;
import com.gta.zssx.pub.widget.ClearEditText;

import java.util.ArrayList;
import java.util.List;

/**
 * [Description]
 * <p> 课堂教学反馈课程选择界面
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by liang.lu on 2017/06/20.
 * @since 2.0.0
 */

public class CourseSearchActivity extends BaseMvpActivity<CourseSearchView, CourseSearchPresenter> implements
        CourseSearchView, ClassroomCourseAddAdapter.Listener {

    public static final int MY_COURSE = 2;
    public static final int SEARCH_COURSE = 1;
    public static final int RESULT_CODE_COURSE_SEARCH = 1;
    public static final String COURSE_INFO_BEAN = "course_info_bean";
    public static final String TEACHER_ID = "teacher_id";
    public static final String REGISTER_DATE = "date";
    private ClearEditText mSearchEt;
    private RecyclerView mRecyclerView;
    private TextView mMyCourseTv;
    private String teacherId;
    private String date;
    private String mKeyWord;
    private ClassroomCourseAddAdapter mAdapter;
    List<DetailItemShowBean.CourseInfoBean> myCourseInfoBeanList;

    @Override
    public int getLayoutId () {
        return R.layout.activity_classroom_add_course;
    }

    @Override
    protected void initView () {
        mSearchEt = (ClearEditText) findViewById (R.id.search_edit_text_course);
        mRecyclerView = (RecyclerView) findViewById (R.id.rv_my_course_result);
        mMyCourseTv = (TextView) findViewById (R.id.tv_my_course);
    }

    @NonNull
    @Override
    public CourseSearchPresenter createPresenter () {
        return new CourseSearchPresenter (this);
    }

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        intiData ();
        intiToolBar ();
        setInteractListener ();
        loadData ();
        //        hideSoftKeyboard ();
    }

    public static void start (Context context, String teacherId, String date) {
        final Intent lIntent = new Intent (context, CourseSearchActivity.class);
        Bundle mBundle = new Bundle ();
        mBundle.putString (TEACHER_ID, teacherId);
        mBundle.putString (REGISTER_DATE, date);
        lIntent.putExtras (mBundle);
        if (!(context instanceof Activity)) {
            lIntent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity (lIntent);
    }

    /**
     * 页面间数据接收
     */
    private void intiData () {
        teacherId = (String) getIntent ().getExtras ().get (TEACHER_ID);
        date = (String) getIntent ().getExtras ().get (REGISTER_DATE);
        if (teacherId == null || teacherId.equals ("")) {
            teacherId = "-1";
        }
    }

    private void intiToolBar () {
        mToolBarManager.setTitle (getResources ().getString (R.string.add_course));
    }

    private void setInteractListener () {
        mRecyclerView.setLayoutManager (new LinearLayoutManager (this));
        mRecyclerView.setHasFixedSize (true);
        mSearchEt.addTextChangedListener (new TextWatcher () {
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {
                if (mAdapter != null) {
                    mAdapter.removeData ();
                }
                mKeyWord = s.toString ().trim ();
                if (!mKeyWord.isEmpty ()) {
                    mMyCourseTv.setVisibility (View.GONE);
                    presenter.loadCourseSearchData (mKeyWord);
                }
            }

            @Override
            public void afterTextChanged (Editable s) {
                if (s.toString ().trim ().equals ("")) {
                    if (myCourseInfoBeanList != null) {
                        mMyCourseTv.setVisibility (View.VISIBLE);
                        mAdapter.setCourseInfoBeenList (myCourseInfoBeanList);
                        mAdapter.setState (MY_COURSE);
                        mAdapter.notifyDataSetChanged ();
                        CourseSearchActivity.this.myCourseInfoBeanList = getData (myCourseInfoBeanList);
                    }
                }
            }
        });
    }

    /**
     * 加载数据
     */
    private void loadData () {
        presenter.loadMyCourseData (teacherId, date);
    }

    /**
     * 服务器获取到我的课程结果回调
     *
     * @param courseInfoBeanList data
     */
    @Override
    public void showMyCourseResult (List<DetailItemShowBean.CourseInfoBean> courseInfoBeanList) {
        this.myCourseInfoBeanList = getData (courseInfoBeanList);
        mMyCourseTv.setVisibility (View.VISIBLE);
        mAdapter = new ClassroomCourseAddAdapter (this);
        mAdapter.setListener (this);
        mAdapter.setCourseInfoBeenList (courseInfoBeanList);
        mAdapter.setState (MY_COURSE);
        mRecyclerView.setAdapter (mAdapter);
    }

    private List<DetailItemShowBean.CourseInfoBean> getData (List<DetailItemShowBean.CourseInfoBean> courseInfoBeanList) {
        List<DetailItemShowBean.CourseInfoBean> courseInfoBeanList1 = new ArrayList<> ();
        for (int i = 0; i < courseInfoBeanList.size (); i++) {
            DetailItemShowBean.CourseInfoBean courseInfoBean = new DetailItemShowBean.CourseInfoBean ();
            courseInfoBean.setCourseId (courseInfoBeanList.get (i).getCourseId ());
            courseInfoBean.setCourseName (courseInfoBeanList.get (i).getCourseName ());
            courseInfoBean.setCourseCode (courseInfoBeanList.get (i).getCourseCode ());
            courseInfoBeanList1.add (courseInfoBean);
        }
        return courseInfoBeanList1;
    }

    /**
     * 服务器获取到搜索课程结果回调
     *
     * @param courseInfoBeanList data
     */
    @Override
    public void showSearchResult (List<DetailItemShowBean.CourseInfoBean> courseInfoBeanList) {
        if (mAdapter == null) {
            mAdapter = new ClassroomCourseAddAdapter (this);
            mAdapter.setCourseInfoBeenList (courseInfoBeanList);
            mAdapter.setListener (this);
            mAdapter.setSearchString (mKeyWord);
            mAdapter.setState (SEARCH_COURSE);
            mRecyclerView.setAdapter (mAdapter);
        } else {
            mAdapter.setCourseInfoBeenList (courseInfoBeanList);
            mAdapter.setState (SEARCH_COURSE);
            mAdapter.setSearchString (mKeyWord);
            mAdapter.notifyDataSetChanged ();
        }
    }

    /**
     * 出现错误提示信息 搜索无结果或者添加自定义课程失败的时候弹出提示
     *
     * @param string string
     */
    @Override
    public void showToast (String string) {
        ToastUtils.showShortToast (string);
    }

    //收起键盘
    //    protected void hideSoftKeyboard () {
    //        if (this.getCurrentFocus () != null) {
    //            InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService (Context.INPUT_METHOD_SERVICE);
    //            inputMethodManager.hideSoftInputFromWindow (this.getCurrentFocus ().getWindowToken (), 0);
    //        }
    //    }

    /**
     * adapter item 点击回调
     *
     * @param courseInfoBean data
     */
    @Override
    public void itemClickListener (DetailItemShowBean.CourseInfoBean courseInfoBean) {
        Intent mIntent = new Intent (this, RegisterDetailsActivity.class);
        Bundle mBundle = new Bundle ();
        mBundle.putSerializable (COURSE_INFO_BEAN, courseInfoBean);
        mIntent.putExtras (mBundle);
        setResult (RESULT_CODE_COURSE_SEARCH, mIntent);
        finish ();
    }
}
