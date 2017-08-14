package com.gta.zssx.fun.coursedaily.registercourse.view.pageV2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.DetailItemShowBean;
import com.gta.zssx.fun.coursedaily.registercourse.presenter.MultiselectCoursePresenter;
import com.gta.zssx.fun.coursedaily.registercourse.view.MultiselectCourseView;
import com.gta.zssx.fun.coursedaily.registercourse.view.adapter.CountNumAdapter;
import com.gta.zssx.fun.coursedaily.registercourse.view.adapter.CourseSearchAdapter;
import com.gta.zssx.fun.coursedaily.registercourse.view.adapter.MyCourseAdapter;
import com.gta.zssx.fun.coursedaily.registercourse.view.adapter.ui.CountNumPopupWindow;
import com.gta.zssx.fun.coursedaily.registercourse.view.adapter.ui.IsDeleteSelectItemDialog;
import com.gta.zssx.pub.base.BaseActivity;
import com.gta.zssx.pub.manager.ToolBarManager;
import com.gta.zssx.pub.util.RxBus;
import com.gta.zssx.pub.widget.ClearEditText;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rx.Subscriber;

import static com.gta.zssx.fun.classroomFeedback.view.page.RegisterDetailsActivity.PAGE_CLASSROOM;
import static com.gta.zssx.fun.classroomFeedback.view.page.RegisterDetailsActivity.RESULT_CODE_CLASSROOM_COURSE;
import static com.gta.zssx.fun.classroomFeedback.view.page.RegisterDetailsActivity.WHICH_PAGE;
import static com.gta.zssx.fun.coursedaily.registercourse.view.pageV2.CourseDetailActivity.PAGE_COURSE_DAILY;

/**
 * Created by lan.zheng on 2017/3/9.
 */
public class MultiselectCourseActivity extends BaseActivity<MultiselectCourseView, MultiselectCoursePresenter>
        implements MultiselectCourseView, View.OnClickListener {
    private final static String TAG = MultiselectCourseActivity.class.getSimpleName ();
    //标题栏
    public ToolBarManager mToolBarManager;
    public Toolbar mToolbar;
    public final static String TEACHER_ID = "TEACHER_ID";
    public final static String SELECT_DATE = "SELECT_DATE";
    public final static String COURSE_INFO_BEAN = "COURSE_INFO_BEAN";
    //搜索
    public ClearEditText mSearchClearEditText;
    public LinearLayout mSearchCourseLayout;  //搜索课程
    //显示我的课程
    public LinearLayout mMyCourseLayout;
    public RecyclerView mMyCourseRecyclerView; //我的课程
    //显示搜索
    public LinearLayout mSearchResultLayout;   //搜索结果布局
    //    public TextView mAddKeywordCourseTextView;  //以关键字作为课程添加 -- 1.7.2屏蔽此功能
    public RecyclerView mCourseSearchRecyclerView;  //搜索课程列表
    private boolean isNeedToShowMyCourseList = true;
    private String mDate;
    private String mTeacherId;
    private String mKeyWord;
    private Set<DetailItemShowBean.CourseInfoBean> courseInfoBeanSet;  //课程列表不能重复
    private Set<DetailItemShowBean.CourseInfoBean> courseNewAddSet;    //新添加的课程数量

    private CourseSearchAdapter mCourseSearchAdapter; //搜索
    private MyCourseAdapter mMyCourseAdapter;  //我的课程
    //    private CountNumAdapter mCountNumAdapter;  //计数条目

    //计数条相关
    public LinearLayout mCountNumLayout;
    public RelativeLayout mPopupWindowPositionLayout;
    public TextView mCountNumTextView;      //计数处
    public TextView mNewSelectNumTextView;  //新勾选的
    public TextView mCountSureTextView;     //点击确定按钮
    public ImageView mCountNumImageView;    //图标显示
    private String mPage;//表示从哪个页面进来的   PAGE_COURSE_DAILY 课堂日志  PAGE_CLASSROOM  课堂教学反馈

    TextWatcher mSearchCourseTextWatcher = new TextWatcher () {
        @Override
        public void beforeTextChanged (CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged (CharSequence s, int start, int before, int count) {
            //编辑中
            if (mCourseSearchAdapter != null) {
                mCourseSearchAdapter.removeData ();
            }
            mKeyWord = s.toString ().trim ();
            if (!mKeyWord.isEmpty ()) {
                //显示自定义课程
                //                String addKeyWordCourse = "添加课程“"+mKeyWord+"”";
                //                mAddKeywordCourseTextView.setText(addKeyWordCourse);
                //                mAddKeywordCourseTextView.setVisibility(View.VISIBLE);
                if (isNeedToShowMyCourseList) {
                    mMyCourseLayout.setVisibility (View.GONE);
                }
                mSearchResultLayout.setVisibility (View.VISIBLE);
                getSearchData ();
            } else {
                //没有字的时候,把我的课程显示出来,搜索隐藏
                if (isNeedToShowMyCourseList) {
                    mMyCourseLayout.setVisibility (View.VISIBLE);
                }
                mSearchResultLayout.setVisibility (View.GONE);
            }
        }

        @Override
        public void afterTextChanged (Editable s) {

        }
    };

    //大写转小写
    public static String exChange (String str) {
        StringBuffer sb = new StringBuffer ();
        if (str != null) {
            for (int i = 0; i < str.length (); i++) {
                char c = str.charAt (i);
                if (Character.isUpperCase (c)) {
                    sb.append (Character.toLowerCase (c));
                } else {
                    sb.append (c);
                }
            }
        }
        return sb.toString ();
    }

    @NonNull
    @Override
    public MultiselectCoursePresenter createPresenter () {
        return new MultiselectCoursePresenter (this);
    }

    //收起键盘
    protected void hideSoftKeyboard () {
        if (this.getCurrentFocus () != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService (Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow (this.getCurrentFocus ().getWindowToken (), 0);
        }
    }

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_multiselect_course);
        loadData ();
        initView ();
        initToolBar ();
        getMyCourse ();
        hideSoftKeyboard ();
    }

    private void loadData () {
        courseInfoBeanSet = new HashSet<> ();
        courseNewAddSet = new HashSet<> ();
        mPage = getIntent ().getExtras ().getString (WHICH_PAGE);
        mTeacherId = getIntent ().getExtras ().getString (TEACHER_ID, "");
        mDate = getIntent ().getExtras ().getString (SELECT_DATE, "");
        if (getIntent ().getSerializableExtra (COURSE_INFO_BEAN) != null)
            courseInfoBeanSet = (Set<DetailItemShowBean.CourseInfoBean>) getIntent ().getSerializableExtra (COURSE_INFO_BEAN);
        addListener ();
    }

    private void addListener () {
        /**
         * 添加课程监听
         */
        presenter.mCompositeSubscription.add (RxBus.getDefault ().toObserverable (DetailItemShowBean.CourseInfoBean.class)
                .subscribe (new Subscriber<DetailItemShowBean.CourseInfoBean> () {
                    @Override
                    public void onCompleted () {

                    }

                    @Override
                    public void onError (Throwable e) {

                    }

                    @Override
                    public void onNext (DetailItemShowBean.CourseInfoBean courseInfoBean) {
                        //总添加课程门数
                        int countAllNum = courseInfoBeanSet.size ();
                        if (countAllNum > 9) {
                            ToastUtils.showShortToast (getResources ().getString (R.string.text_select_course_can_not_above_ten));
                            return;
                        }
                        courseInfoBeanSet.add (courseInfoBean);
                        if (countAllNum == courseInfoBeanSet.size ()) {
                            ToastUtils.showShortToast (getResources ().getString (R.string.text_add_same_course_item));
                            return;
                        }
                        String countAll = "已选择" + courseInfoBeanSet.size () + "门";
                        mCountNumTextView.setText (countAll);
                        //新添加课程门数
                        int countNewNum = courseNewAddSet.size ();  //用于判断是否要谈提示
                        courseNewAddSet.add (courseInfoBean);
                        if (countNewNum == courseNewAddSet.size ()) {
                            //当添加以后的Set集合一样，不需要更新界面
                            ToastUtils.showShortToast (getResources ().getString (R.string.text_add_same_course_item));
                            return;
                        }
                        mNewSelectNumTextView.setText ("" + courseNewAddSet.size ());
                        if (courseNewAddSet.size () > 0) {
                            mNewSelectNumTextView.setVisibility (View.VISIBLE);
                        } else {
                            mNewSelectNumTextView.setVisibility (View.GONE);
                        }
                    }
                }));
    }

    private void initView () {
        mSearchClearEditText = (ClearEditText) findViewById (R.id.search_edittext_course);
        mSearchClearEditText.addTextChangedListener (mSearchCourseTextWatcher);
        mSearchCourseLayout = (LinearLayout) findViewById (R.id.layout_search_course);
        //我的课程
        mMyCourseLayout = (LinearLayout) findViewById (R.id.layout_my_course_result);
        mMyCourseRecyclerView = (RecyclerView) findViewById (R.id.rv_my_course_result);
        mMyCourseRecyclerView.setLayoutManager (new LinearLayoutManager (this));
        //搜索展示
        mSearchResultLayout = (LinearLayout) findViewById (R.id.layout_search_result);
        //        mAddKeywordCourseTextView = (TextView)findViewById(R.id.tv_add_key_word_course);
        //        mAddKeywordCourseTextView.setOnClickListener(this);
        //        mAddKeywordCourseTextView.setVisibility(View.GONE);
        mCourseSearchRecyclerView = (RecyclerView) findViewById (R.id.rv_search_result);
        mCourseSearchRecyclerView.setLayoutManager (new LinearLayoutManager (this));
        //计数相关
        mCountNumLayout = (LinearLayout) findViewById (R.id.layout_count_num);
        mPopupWindowPositionLayout = (RelativeLayout) findViewById (R.id.layout_popup_window_position);
        mCountNumTextView = (TextView) findViewById (R.id.tv_show_select_total_num);
        mNewSelectNumTextView = (TextView) findViewById (R.id.tv_remark_new_select_num);
        mCountNumImageView = (ImageView) findViewById (R.id.iv_show_select_pic);
        mCountNumImageView.setOnClickListener (this);
        mCountNumImageView.setImageDrawable (getResources ().getDrawable (R.drawable.count_course_num_selectot));
        mCountSureTextView = (TextView) findViewById (R.id.tv_select_sure);
        mCountSureTextView.setOnClickListener (this);
        //计数初始化
        String count = "已选择" + courseInfoBeanSet.size () + "门";
        mCountNumTextView.setText (count);
        mNewSelectNumTextView.setVisibility (View.GONE);  //新增的小圆圈第一个Activity进入默认都为0
    }

    private void initToolBar () {
        mToolbar = (Toolbar) findViewById (R.id.toolbar);
        mToolBarManager = new ToolBarManager (mToolbar, this);
        mToolBarManager.init ();
        mToolbar.setNavigationOnClickListener (v -> onBackPressed ());
        mToolBarManager.showBack (true).setTitle (getResources ().getString (R.string.add_course));
    }

    private void getMyCourse () {
        Log.e (TAG, "mTeacherId = " + mTeacherId + " , mDate =" + mDate);
        presenter.getMyCourseData (mTeacherId, mDate);
    }

    @Override
    public void showMyCourse (List<DetailItemShowBean.CourseInfoBean> courseInfoBeanList) {
        if (courseInfoBeanList.size () != 0) {
            mMyCourseLayout.setVisibility (View.VISIBLE);
            mMyCourseAdapter = new MyCourseAdapter (this, courseInfoBeanList, new MyCourseAdapter.Listener () {
                @Override
                public void itemClickListener (DetailItemShowBean.CourseInfoBean courseInfoBean) {
                    //点击回调增加新的条目
                    /**
                     * 添加课程监听
                     */
                    RxBus.getDefault ().post (courseInfoBean);
                }
            });
            mMyCourseRecyclerView.setAdapter (mMyCourseAdapter);
        }
    }

    @Override
    public void getMyCourseFailed () {
        isNeedToShowMyCourseList = false;
        mMyCourseAdapter = null;
        mMyCourseLayout.setVisibility (View.GONE);
    }

    @Override
    public void showResultSearch (List<DetailItemShowBean.CourseInfoBean> courseInfoBeanList) {
        if (courseInfoBeanList.size () != 0) {
            //当搜索结果中有完全匹配的结果时，添加关键字课程条目不显示
            /*for(int i = 0;i<courseInfoBeanList.size();i++){
                String keyWordLowerCase = exChange(mKeyWord);
                String courseNameLowerCase = exChange(courseInfoBeanList.get(i).getCourseName());
                if(courseNameLowerCase.equals(keyWordLowerCase)){
                    mAddKeywordCourseTextView.setVisibility(View.GONE);
                }
            }*/
            mCourseSearchAdapter = new CourseSearchAdapter (this, courseInfoBeanList, mKeyWord, courseInfoBean -> {
                //点击回调增加新的条目
                /**
                 * 添加课程监听
                 */
                RxBus.getDefault ().post (courseInfoBean);
            });
            mCourseSearchRecyclerView.setAdapter (mCourseSearchAdapter);
        }
    }

    @Override
    public void showToast (String s) {
        //搜索无结果或者添加自定义课程失败的时候弹出提示
        ToastUtils.showShortToast (s);
        /*//测试
        if(s.equals(getResources().getString(R.string.text_add_custom_course_failed))){
            mSearchClearEditText.clearComposingText();
        }*/
    }

    @Override
    public void addCustomCourse (DetailItemShowBean.CourseInfoBean courseInfoBean) {
        ToastUtils.showShortToast ("添加自定义课程‘" + courseInfoBean.getCourseName () + "'成功");
        /**
         * 添加课程监听
         */
        RxBus.getDefault ().post (courseInfoBean);
        mSearchClearEditText.setText ("");
    }

    private void getSearchData () {
        presenter.getSearchCourseData (mKeyWord);
    }

    CountNumPopupWindow mCountNumPopupWindow;

    @Override
    public void onClick (View v) {
        switch (v.getId ()) {
            case R.id.iv_show_select_pic:
                //点击“已选”
                if (courseInfoBeanSet.size () == 0) {
                    ToastUtils.showShortToast (getResources ().getString (R.string.text_now_not_have_select_course));
                    return;
                }
                Set<Object> objectSet = new HashSet<> ();
                List<DetailItemShowBean.CourseInfoBean> courseInfoBeanList = new ArrayList<> (courseInfoBeanSet);
                for (int i = courseInfoBeanList.size (); i > 0; i--) {
                    objectSet.add (courseInfoBeanList.get (i - 1));
                }
                String title = getResources ().getString (R.string.text_have_been_select_course);
                mCountNumPopupWindow = new CountNumPopupWindow (this, title, objectSet, CountNumAdapter.COUNT_SHOW_COUNSE, 9, mListener);
                backgroundAlpha (0.8f);
                mCountNumPopupWindow.showAtLocation (mCountNumLayout, Gravity.BOTTOM, 0, mCountNumLayout.getHeight ());
                break;
            case R.id.tv_select_sure:
                //点击“确定”,返回所需显示参数
                Intent intent = new Intent ();
                Bundle bundle = new Bundle ();
                bundle.putSerializable (COURSE_INFO_BEAN, (Serializable) courseInfoBeanSet);
                intent.putExtras (bundle);
                if (mPage.equals (PAGE_COURSE_DAILY)) {
                    setResult (CourseDetailActivity.RESULT_CODE_COURSE, intent);
                } else if (mPage.equals (PAGE_CLASSROOM)) {
                    setResult (RESULT_CODE_CLASSROOM_COURSE, intent);
                }
                finish ();
                break;
            /*case R.id.tv_add_key_word_course:
                //点击“添加关键字为新课程”
                Log.e("lenita","mKeyWord = "+mKeyWord);
                if(!TextUtils.isEmpty(mKeyWord)){
                    presenter.submitUserDefinedCourse(mKeyWord,mTeacherId);
                }
                break;*/
            default:
                break;
        }
    }

    CountNumPopupWindow.Listener mListener = new CountNumPopupWindow.Listener () {
        @Override
        public void onPopupWindowDismissListener () {
            //TODO 取消监听暂时不用
            backgroundAlpha (1f);
        }

        @Override
        public void onDeleteAllClickListener () {
//            backgroundAlpha (0.8f);
            popupConfirmClearAllDialog (getResources ().getString (R.string.text_sure_delete_all) + getResources ().getString (R.string.text_course_show2));
            /*int preAllCount = courseInfoBeanSet.size();
            int preNewCount = courseNewAddSet.size();
            courseInfoBeanSet.clear();
            courseNewAddSet.clear();
            if(preAllCount !=  courseInfoBeanSet.size()){
                String count = "已选择"+courseInfoBeanSet.size()+"门";
                Log.e("lenita","onDeleteItemListener = "+count);
                mCountNumTextView.setText(count);
                if(courseInfoBeanSet.size() == 0 && mCountNumPopupWindow !=null){
                    mCountNumPopupWindow.dismiss();
                }
            }
            if(preNewCount != courseNewAddSet.size()){
                preNewCount = courseNewAddSet.size();
                mNewSelectNumTextView.setText(String.valueOf(preNewCount));
                if(courseNewAddSet.size() == 0){
                    mNewSelectNumTextView.setVisibility(View.GONE);
                }else {
                    mNewSelectNumTextView.setVisibility(View.VISIBLE);
                }
            }*/
        }

        @Override
        public void onItemDeleteListener (Object object) {
            //删除一条数据
            int preAllCount = courseInfoBeanSet.size ();
            int preNewCount = courseInfoBeanSet.size ();
            courseInfoBeanSet.remove (object);
            courseNewAddSet.remove (object);
            if (preAllCount != courseInfoBeanSet.size ()) {
                String count = "已选择" + courseInfoBeanSet.size () + "门";
                Log.e ("lenita", "onDeleteItemListener = " + count);
                mCountNumTextView.setText (count);
                if (courseInfoBeanSet.size () == 0 && mCountNumPopupWindow != null) {
                    mCountNumPopupWindow.dismiss ();
                }
            }
            if (preNewCount != courseNewAddSet.size ()) {
                preNewCount = courseNewAddSet.size ();
                mNewSelectNumTextView.setText (String.valueOf (preNewCount));
                if (courseNewAddSet.size () == 0) {
                    mNewSelectNumTextView.setVisibility (View.GONE);
                } else {
                    mNewSelectNumTextView.setVisibility (View.VISIBLE);
                }
            }
        }
    };

    private IsDeleteSelectItemDialog mDialog;

    //显示弹框确认是否删除全部
    private void popupConfirmClearAllDialog (String content) {
        if (mDialog != null)
            mDialog = null; //置空然后重新赋值
        mDialog = new IsDeleteSelectItemDialog (mActivity, content, new IsDeleteSelectItemDialog.Listener () {
            @Override
            public void onDialogDismissListener () {
//                backgroundAlpha (1f);
            }

            @Override
            public void onSureListerner () {

                int preAllCount = courseInfoBeanSet.size ();
                int preNewCount = courseNewAddSet.size ();
                courseInfoBeanSet.clear ();
                courseNewAddSet.clear ();
                if (preAllCount != courseInfoBeanSet.size ()) {
                    String count = "已选择" + courseInfoBeanSet.size () + "门";
                    Log.e ("lenita", "onDeleteItemListener = " + count);
                    mCountNumTextView.setText (count);
                    if (courseInfoBeanSet.size () == 0 && mCountNumPopupWindow != null) {
                        mCountNumPopupWindow.dismiss ();
                    }
                }
                if (preNewCount != courseNewAddSet.size ()) {
                    preNewCount = courseNewAddSet.size ();
                    mNewSelectNumTextView.setText (String.valueOf (preNewCount));
                    if (courseNewAddSet.size () == 0) {
                        mNewSelectNumTextView.setVisibility (View.GONE);
                    } else {
                        mNewSelectNumTextView.setVisibility (View.VISIBLE);
                    }
                }
            }
        });  //使用自定义的样式
        mDialog.show ();
    }

    private void backgroundAlpha (float b) {
        WindowManager.LayoutParams layoutParams = getWindow ().getAttributes ();
        layoutParams.alpha = b;
        getWindow ().setAttributes (layoutParams);
    }

  /*  CountNumAdapter.Listener mDeleteListener = new CountNumAdapter.Listener() {
        @Override
        public void onDeleteItemListener(Object object) {
            //删除一条数据
            int preAllCount = courseInfoBeanSet.size();
            int preNewCount = courseInfoBeanSet.size();
            courseInfoBeanSet.remove(object);
            courseNewAddSet.remove(object);
            if(preAllCount !=  courseInfoBeanSet.size()){
                String count = "已选择"+courseInfoBeanSet.size()+"门";
                Log.e("lenita","onDeleteItemListener = "+count);
                mCountNumTextView.setText(count);
                if(courseInfoBeanSet.size() == 0 && mCountNumPopupWindow !=null){
                    mCountNumPopupWindow.dismiss();
                }
            }
            if(preNewCount != courseNewAddSet.size()){
                preNewCount = courseNewAddSet.size();
                mNewSelectNumTextView.setText(String.valueOf(preNewCount));
                if(courseNewAddSet.size() == 0){
                    mNewSelectNumTextView.setVisibility(View.GONE);
                }else {
                    mNewSelectNumTextView.setVisibility(View.VISIBLE);
                }
            }

        }
    };*/
}
