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
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.DetailItemShowBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.RxCountTeacherNumBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.RxTeacherInfoBean;
import com.gta.zssx.fun.coursedaily.registercourse.presenter.MultiselectTeacherPresenter;
import com.gta.zssx.fun.coursedaily.registercourse.view.MultiselectTeacherView;
import com.gta.zssx.fun.coursedaily.registercourse.view.adapter.CountNumAdapter;
import com.gta.zssx.fun.coursedaily.registercourse.view.adapter.ui.CountNumPopupWindow;
import com.gta.zssx.fun.coursedaily.registercourse.view.adapter.ui.IsDeleteSelectItemDialog;
import com.gta.zssx.patrolclass.model.entity.ChooseTeacherEntity;
import com.gta.zssx.patrolclass.model.entity.ChooseTeacherSearchEntity;
import com.gta.zssx.patrolclass.view.adapter.ChooseTeacherAdapter;
import com.gta.zssx.patrolclass.view.adapter.ChooseTeacherSearchAdapter;
import com.gta.zssx.pub.base.BaseActivity;
import com.gta.zssx.pub.manager.ToolBarManager;
import com.gta.zssx.pub.util.RxBus;
import com.gta.zssx.pub.widget.ClearEditText;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import rx.Subscriber;

/**
 * Created by lan.zheng on 2017/3/3.
 */
public class MultiselectTeacherActivity extends BaseActivity<MultiselectTeacherView,MultiselectTeacherPresenter>
        implements MultiselectTeacherView,ExpandableListView.OnChildClickListener, View.OnClickListener {

    private final static  String TAG = MultiselectTeacherActivity.class.getSimpleName();
    private final static String DEFAULT_DEP_FLAG = "-1";
    public final static String DEP_ID = "DEP_ID";
    public final static String MAIN_PAGE_FLAG = "MAIN_PAGE_FLAG";
    public final static String TEACHER_INFO_BEAN = "TEACHER_INFO_BEAN";
    public final static String CHOOSE_TEACHER_LIST = "CHOOSE_TEACHER_LIST";
    public final static String TEACHER_COUNT_NUM_INFO = "TEACHER_COUNT_NUM_INFO";
    public final static String PAGE_FLAG = "PAGE_FLAG";

    private int pageFlag = 0;
    private String mDeptId = "-1";
    private List<ChooseTeacherEntity> entities;
    private Set<DetailItemShowBean.TeacherInfoBean> teacherInfoBeanSet;  //老师列表不能重复
    private Set<DetailItemShowBean.TeacherInfoBean> teacherNewAddSet;    //新添加的老师数量
//    private CountNumAdapter mCountNumAdapter;  //计数条目
    private ChooseTeacherSearchAdapter mChooseTeacherSearchAdapter; //搜索
    private boolean isShowSearchView = false; //是否显示搜索条,显示搜索条同时代表是第一个MultiselectTeacherActivity页面
    private boolean isNeedToGetTeacherList = true; //false：获取失败搜索回退都不在显示组织结构,搜索的时候也是这样
    private String mKeyWord;

    public ClearEditText mSearchClearEditText; //搜索条
    public LinearLayout mSearchTeacherLayout;  //搜索教师布局
    public RecyclerView mTeacherSearchRecyclerView;  //搜索教师列表
    public ExpandableListView mTeacherExpandableListView; //老师组织架构
    //计数条相关
    public LinearLayout mCountNumLayout;  //老师有可能是复选页面，此时这个需要隐藏
    public RelativeLayout mPopupWindowPositionLayout;
    public TextView mCountNumTextView;      //计数处
    public TextView mNewSelectNumTextView;  //新勾选的
    public TextView mCountSureTextView;     //点击确定按钮
    public ImageView mCountNumImageView;    //图标显示
    //标题栏
    public ToolBarManager mToolBarManager;
    public Toolbar mToolbar;

    TextWatcher mSearchTeacherTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //编辑中
            if (mChooseTeacherSearchAdapter != null) {
                mChooseTeacherSearchAdapter.removeData ();
            }
            mKeyWord = s.toString ().trim ();
            if (!mKeyWord.isEmpty ()) {
                if(isNeedToGetTeacherList){
                    mTeacherExpandableListView.setVisibility(View.GONE);
                }
                mTeacherSearchRecyclerView.setVisibility(View.VISIBLE);
                getSearchData();
            }else {
                //没有字的时候,把我的课程显示出来,搜索隐藏
                if(isNeedToGetTeacherList){
                    mTeacherExpandableListView.setVisibility(View.VISIBLE);
                }
                mTeacherSearchRecyclerView.setVisibility(View.GONE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void getSearchData(){
        presenter.getTeacherSearchDatas(mKeyWord);
    }

    @NonNull
    @Override
    public MultiselectTeacherPresenter createPresenter() {
        return new MultiselectTeacherPresenter(this);
    }

    /**
     *
     * @param context context
     * @param depId 部门Id
     * @param flagMainPage 主页标识
     * @param teacherInfoBeenSet  首页进入，有就传,没有给null
     *
     */
    public static void start(Context context, String depId, boolean flagMainPage, Set<DetailItemShowBean.TeacherInfoBean> teacherInfoBeenSet, List<ChooseTeacherEntity> chooseTeacherEntityList, RxCountTeacherNumBean rxCountTeacherNumBean,int pageFlag) {
        Intent lIntent = new Intent(context, MultiselectTeacherActivity.class);
        Bundle lBundle = new Bundle();
        lBundle.putString(DEP_ID, depId);
        lBundle.putBoolean(MAIN_PAGE_FLAG,flagMainPage);
        lBundle.putSerializable(TEACHER_INFO_BEAN, (Serializable) teacherInfoBeenSet);
        lBundle.putSerializable(CHOOSE_TEACHER_LIST,(Serializable) chooseTeacherEntityList);
        lBundle.putSerializable(TEACHER_COUNT_NUM_INFO,rxCountTeacherNumBean);
        lBundle.putInt(PAGE_FLAG,pageFlag);
        lIntent.putExtras(lBundle);
        context.startActivity(lIntent);
    }

    //收起键盘
    protected void hideSoftKeyboard()
    {
        if (this.getCurrentFocus() != null)
        {
            InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiselect_teacher);
        loadData();
        initView();
        initToolBar();
        TeacherData(false);  //首次获取
        hideSoftKeyboard();  //进入确保不弹软键盘
    }

    private void loadData(){
        mDeptId = getIntent().getExtras().getString(DEP_ID,DEFAULT_DEP_FLAG);
        isShowSearchView = getIntent().getBooleanExtra(MAIN_PAGE_FLAG,false);
        pageFlag = getIntent().getIntExtra(PAGE_FLAG,0);
        if(getIntent().getBooleanExtra(MAIN_PAGE_FLAG,false)){
            teacherInfoBeanSet = new HashSet<>();  //所有的列
            teacherNewAddSet = new HashSet<>();    //新添加的
            if(null != getIntent().getSerializableExtra(TEACHER_INFO_BEAN)){
                teacherInfoBeanSet = (Set<DetailItemShowBean.TeacherInfoBean>) getIntent().getSerializableExtra(TEACHER_INFO_BEAN);
            }
            Iterator iterator = teacherInfoBeanSet.iterator();
            while (iterator.hasNext()){
                Log.e("lenita","tname = "+((DetailItemShowBean.TeacherInfoBean)iterator.next()).getTeacherName());
            }
        }
        entities = (List<ChooseTeacherEntity>) getIntent().getSerializableExtra(CHOOSE_TEACHER_LIST);
        addListener();  //点击回调监听
    }

    private boolean needToGetCallBack = true;  //是否需要回调，因为都是同一个页面，回调会多次出问题
    private void addListener(){
        /**
         * 添加教师监听
         */
        presenter.mCompositeSubscription.add(RxBus.getDefault().toObserverable(RxTeacherInfoBean.class)
                .subscribe(new Subscriber<RxTeacherInfoBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(RxTeacherInfoBean rxTeacherInfoBean) {
//                        boolean isMainPage = isShowSearchView;
                        DetailItemShowBean.TeacherInfoBean teacherInfoBean = rxTeacherInfoBean.getTeacherInfoBean();
                        RxCountTeacherNumBean rxCountTeacherNumBean = new RxCountTeacherNumBean();
                        int rxPageFlag = rxTeacherInfoBean.getPageFlag();
                        Log.e("lenita","rxPageFlag = "+rxPageFlag+ ", pageFlag = "+pageFlag);
                        if(rxPageFlag == pageFlag){  //TODO 注意只有当前页面去接收添加老师的item,别的页面只需要进行数量更改即可
                            //总添加教师人数
                            int countAllNum = teacherInfoBeanSet.size();
                            if(countAllNum > 9){
                                ToastUtils.showShortToast(getResources().getString(R.string.text_select_teacher_can_not_above_ten));
                                return;
                            }
                            teacherInfoBeanSet.add(teacherInfoBean);
                            if(countAllNum == teacherInfoBeanSet.size()){
                                ToastUtils.showShortToast(getResources().getString(R.string.text_add_same_teacher_item));
                                return;
                            }
                            //TODO rxCountTeacherNumBean拿到总教师
                            rxCountTeacherNumBean.setTeacherInfoBeanSet(teacherInfoBeanSet);
                            String countAll = "已选择"+teacherInfoBeanSet.size()+"人";
                            mCountNumTextView.setText(countAll);
                            //新添加教师人数
                            int countNewNum = teacherNewAddSet.size();  //用于判断是否要谈提示
                            teacherNewAddSet.add(teacherInfoBean);
                            //TODO rxCountTeacherNumBean封装teacherNewAddSet参数
                            rxCountTeacherNumBean.setTeacherNewAddSet(teacherNewAddSet);
                            rxCountTeacherNumBean.setClickSureButton(false);
                            RxBus.getDefault().post(rxCountTeacherNumBean);
                            //TODO 通知所有MultiselectTeacherActivity
                            if(countNewNum == teacherNewAddSet.size()){
                                //当添加以后的Set集合一样
                                ToastUtils.showShortToast(getResources().getString(R.string.text_add_same_teacher_item));
                                return;
                            }
                            mNewSelectNumTextView.setText(""+teacherNewAddSet.size());
                            if(teacherNewAddSet.size() > 0){
                                mNewSelectNumTextView.setVisibility(View.VISIBLE);
                            }else {
                                mNewSelectNumTextView.setVisibility(View.GONE);
                            }
                        }
                        /*else {
                            //不是首页的都关闭
                            finish();
                        }*/
                    }
                }));

        /**
         * 教师数量改变监听
         */
        presenter.mCompositeSubscription.add(RxBus.getDefault().toObserverable(RxCountTeacherNumBean.class)
                .subscribe(new Subscriber<RxCountTeacherNumBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(RxCountTeacherNumBean rxCountTeacherNumBean) {
                        if(rxCountTeacherNumBean.getClickSureButton()){
                            //TODO 如果是点击确认，则finish()所有的MultiselectTeacherActivity
                            finish();
                        }else {
                            //TODO 如果不是点击确认按钮，就只是通知每个页面进行更新
                            updateBottonCountNun(rxCountTeacherNumBean);
                        }

                    }
                }));
    }

    private void initView(){
        mSearchClearEditText = (ClearEditText)findViewById(R.id.search_edittext_teacher);
        mSearchClearEditText.addTextChangedListener(mSearchTeacherTextWatcher);
        mSearchTeacherLayout = (LinearLayout)findViewById(R.id.layout_search_teacher);
        //教师组织架构
        mTeacherExpandableListView = (ExpandableListView)findViewById(R.id.expendlist);
        mTeacherExpandableListView.setDivider(null);
        mTeacherExpandableListView.setOnChildClickListener(this);
        //搜索展示
        mTeacherSearchRecyclerView = (RecyclerView)findViewById(R.id.rv_search_result);
        mTeacherSearchRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //计数相关初始化
        mCountNumLayout = (LinearLayout)findViewById(R.id.layout_count_num);
        mPopupWindowPositionLayout = (RelativeLayout)findViewById(R.id.layout_popup_window_position);
        mCountNumTextView = (TextView)findViewById(R.id.tv_show_select_total_num);
        mNewSelectNumTextView = (TextView) findViewById(R.id.tv_remark_new_select_num);
        mCountNumImageView = (ImageView)findViewById(R.id.iv_show_select_pic);
        mCountNumImageView.setOnClickListener(this);
        mCountNumImageView.setImageDrawable(getResources().getDrawable(R.drawable.count_teacher_num_selector));
        mCountSureTextView = (TextView)findViewById(R.id.tv_select_sure);
        mCountSureTextView.setOnClickListener(this);
        isNeedToGetTeacherList = isShowSearchView;  //首页获取服务器，非第一个Activity进入不需要
        //搜索首页才给搜索，跳入选择老师不显示搜索框
        if(isShowSearchView){
            mSearchTeacherLayout.setVisibility(View.VISIBLE);
            mCountNumLayout.setVisibility(View.VISIBLE);
            String count = "已选择"+teacherInfoBeanSet.size()+"人";
            mCountNumTextView.setText(count);
            mNewSelectNumTextView.setVisibility(View.GONE);  //新增的小圆圈第一个Activity进入默认都为0
            //其他暂时隐藏，当条件触发开始显示
            mTeacherExpandableListView.setVisibility(View.GONE);
            mTeacherSearchRecyclerView.setVisibility(View.GONE);
        }else {
            //进入二级部门
            mSearchTeacherLayout.setVisibility(View.GONE);
            //TODO 二级部门也要显示计数条
            mCountNumLayout.setVisibility(View.VISIBLE);
            RxCountTeacherNumBean rxCountTeacherNumBean = (RxCountTeacherNumBean) getIntent().getSerializableExtra(TEACHER_COUNT_NUM_INFO);
            teacherInfoBeanSet = rxCountTeacherNumBean.getTeacherInfoBeanSet();
            teacherNewAddSet = rxCountTeacherNumBean.getTeacherNewAddSet();
            String count = "已选择"+teacherInfoBeanSet.size()+"人";
            mCountNumTextView.setText(count);
            if(teacherNewAddSet.size() > 0 ){
                mNewSelectNumTextView.setVisibility(View.VISIBLE);
                mNewSelectNumTextView.setText(""+teacherNewAddSet.size() );
            }else {
                mNewSelectNumTextView.setVisibility(View.GONE);
            }
            mTeacherExpandableListView.setVisibility(View.VISIBLE);
            mTeacherSearchRecyclerView.setVisibility(View.GONE);
            //非首页进入且列表不为空直接显示列表
            if(!isNeedToGetTeacherList && entities!= null ){
                showTeacherList();
            }
        }

    }

    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolBarManager = new ToolBarManager(mToolbar, this);
        mToolBarManager.init();
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
        mToolBarManager.showBack(true).setTitle(getResources().getString(R.string.add_teacher));
    }

    private void TeacherData(boolean isRequestHaveNext){
        if(isNeedToGetTeacherList){
            //访问服务器去获取老师，首页进来都要获取
            presenter.getTeacherData(mDeptId,isRequestHaveNext);
        }
    }

    @Override
    public void showResult(List<ChooseTeacherEntity> entities,boolean isRequestHaveNext) {
        //获取到了结构都显示组织结构
        isNeedToGetTeacherList = true;
        mTeacherExpandableListView.setVisibility(View.VISIBLE);
        if(entities.size()>0 && isRequestHaveNext){
            //请求下一层级
            RxCountTeacherNumBean rxCountTeacherNumBean = new RxCountTeacherNumBean();
            rxCountTeacherNumBean.setTeacherInfoBeanSet(teacherInfoBeanSet);
            rxCountTeacherNumBean.setTeacherNewAddSet(teacherNewAddSet);
            MultiselectTeacherActivity.start(MultiselectTeacherActivity.this,mDeptId,false,null,entities,rxCountTeacherNumBean,pageFlag+1);
        }else {
            this.entities = entities;  //首页才进行赋值，其他页面都通过传递
            showTeacherList();
        }
    }

    private void showTeacherList(){
        ChooseTeacherAdapter mAdapter = new ChooseTeacherAdapter(this);
        mAdapter.setEntities(entities);
        mTeacherExpandableListView.setAdapter(mAdapter);
        if (!mDeptId.equals(DEFAULT_DEP_FLAG)) {
            //部门id不为-1表示进入二层或更下层
            for (int i = 0; i < mAdapter.getGroupCount(); i++) {
                mTeacherExpandableListView.expandGroup(i);
            }
        }
    }

    @Override
    public void getTeacherListFailed(boolean isRequestHaveNext) {
        isNeedToGetTeacherList = false; //获取失败，只能进行搜索
        if(mDeptId.equals(DEFAULT_DEP_FLAG)){
            ToastUtils.showShortToast("教师架构"+getResources().getString(R.string.text_get_teacher_list_failed));
        }else {
            ToastUtils.showShortToast("部门"+getResources().getString(R.string.text_get_teacher_list_failed));
        }
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        ChooseTeacherEntity.DeptListBean deptListBean = entities.get(groupPosition).getDeptList().get(childPosition);
        hideSoftKeyboard();
        if (deptListBean.getType() == 0) {
            //点击的是老师
//            DetailItemShowBean detailItemShowBean = new DetailItemShowBean();
//            Set<DetailItemShowBean.TeacherInfoBean> listBeen = new HashSet<>();
            DetailItemShowBean.TeacherInfoBean teacherInfoBean = new DetailItemShowBean.TeacherInfoBean();
            teacherInfoBean.setTeacherName(deptListBean.getDeptName());
            teacherInfoBean.setTeacherId(deptListBean.getDeptId());
            teacherInfoBean.setTeacherNo(deptListBean.getTeacherCode());
//            listBeen.add(teacherInfoBean);
//            detailItemShowBean.setTeacherInfoBean(listBeen);
            /**
             * 添加教师监听
             */
            RxTeacherInfoBean rxTeacherInfoBean = new RxTeacherInfoBean();
            rxTeacherInfoBean.setPageFlag(pageFlag);
            rxTeacherInfoBean.setTeacherInfoBean(teacherInfoBean);
            RxBus.getDefault().post(rxTeacherInfoBean);  //TODO 发送添加信息
        } else {//点击的是部门，再次请求数据，判断是否要跳转至另一个页面显示
            mDeptId = deptListBean.getDeptId();
//            startSelf();  //到下一个页面获取数据
            isNeedToGetTeacherList = true;
            TeacherData(true);  //跳转前再次去获取数据
        }
        return false;
    }

    @Override
    public void showToast(String s) {
        ToastUtils.showLongToast(s);
    }

    @Override
    public void showResultSearch(List<ChooseTeacherSearchEntity> chooseTeacherSearchEntities) {
        if(chooseTeacherSearchEntities.size() != 0){
            mChooseTeacherSearchAdapter = new ChooseTeacherSearchAdapter(this, chooseTeacherSearchEntities, mKeyWord, new ChooseTeacherSearchAdapter.Listener() {
                @Override
                public void itemClickListener(DetailItemShowBean.TeacherInfoBean teacherInfoBean) {
                    //点击回调增加新的条目
                    /**
                     * 添加教师监听
                     */
                    RxTeacherInfoBean rxTeacherInfoBean = new RxTeacherInfoBean();
                    rxTeacherInfoBean.setPageFlag(pageFlag);
                    rxTeacherInfoBean.setTeacherInfoBean(teacherInfoBean);
                    RxBus.getDefault().post(rxTeacherInfoBean);//TODO 发送添加信息
                }
            });
            mTeacherSearchRecyclerView.setAdapter(mChooseTeacherSearchAdapter);
        }
    }

    CountNumPopupWindow mCountNumPopupWindow;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_show_select_pic:
                if(teacherInfoBeanSet.size() == 0){
                    ToastUtils.showShortToast(getResources().getString(R.string.text_now_not_have_select_teacher));
                    return;
                }
                Set<Object> objectSet = new HashSet<>();
                List<DetailItemShowBean.TeacherInfoBean> teacherInfoBeanList = new ArrayList<>(teacherInfoBeanSet);
                for(int i = teacherInfoBeanList.size();i > 0;i--){
                    objectSet.add(teacherInfoBeanList.get(i-1));
                }
                String title = getResources().getString(R.string.text_have_been_select_teacher);
                mCountNumPopupWindow = new CountNumPopupWindow(this,title,objectSet,CountNumAdapter.COUNT_SHOW_TEACHER,9,mListener);
                backgroundAlpha(0.8f);
                mCountNumPopupWindow.showAtLocation(mCountNumLayout, Gravity.BOTTOM ,0,mCountNumLayout.getHeight());
                break;
            case R.id.tv_select_sure:
                if(isShowSearchView){
                    //TODO 如果是第一个进入的页面，使用StartActivityForResult,点击确定返回所需显示参数
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(TEACHER_INFO_BEAN,(Serializable)teacherInfoBeanSet);
                    intent.putExtras(bundle);
                    setResult(CourseDetailActivity.RESULT_CODE_TEACHER, intent);
                    finish();
                }else{
                    //TODO 其他页面点击确定使用回调
                    RxCountTeacherNumBean rxCountTeacherNumBean = new RxCountTeacherNumBean();
                    rxCountTeacherNumBean.setClickSureButton(true);
                    rxCountTeacherNumBean.setTeacherInfoBeanSet(teacherInfoBeanSet);
                    RxBus.getDefault().post(rxCountTeacherNumBean);
                }

                break;
        }
    }


    CountNumPopupWindow.Listener mListener = new CountNumPopupWindow.Listener() {
        @Override
        public void onPopupWindowDismissListener() {
            //TODO 取消监听暂时不用
            backgroundAlpha(1f);
        }

        @Override
        public void onDeleteAllClickListener() {
//            backgroundAlpha(0.8f);
            popupConfirmClearAllDialog(getResources().getString(R.string.text_sure_delete_all)+getResources().getString(R.string.text_teacher_show2));
            /*int preAllCount = teacherInfoBeanSet.size();
            int preNewCount = teacherNewAddSet.size();
            teacherInfoBeanSet.clear();
            teacherNewAddSet.clear();
            if(preAllCount !=  teacherInfoBeanSet.size()){
                String count = "已选择"+teacherInfoBeanSet.size()+"人";
                Log.e("lenita","onDeleteItemListener = "+count);
                mCountNumTextView.setText(count);
                if(teacherInfoBeanSet.size() == 0 && mCountNumPopupWindow !=null){
                    mCountNumPopupWindow.dismiss();
                }
            }
            if(preNewCount != teacherNewAddSet.size()){
                preNewCount = teacherNewAddSet.size();
                mNewSelectNumTextView.setText(String.valueOf(preNewCount));
                if(teacherNewAddSet.size() == 0){
                    mNewSelectNumTextView.setVisibility(View.GONE);
                }else {
                    mNewSelectNumTextView.setVisibility(View.VISIBLE);
                }
            }*/
        }

        @Override
        public void onItemDeleteListener(Object object) {
            //删除一条数据
            int preAllCount = teacherInfoBeanSet.size();
            int preNewCount = teacherNewAddSet.size();
            teacherInfoBeanSet.remove(object);
            teacherNewAddSet.remove(object);
            if(preAllCount !=  teacherInfoBeanSet.size()){
                String count = "已选择"+teacherInfoBeanSet.size()+"人";
                Log.e("lenita","onDeleteItemListener = "+count);
                mCountNumTextView.setText(count);
                if(teacherInfoBeanSet.size() == 0 && mCountNumPopupWindow != null){
                    mCountNumPopupWindow.dismiss();
                }
            }
            if(preNewCount != teacherNewAddSet.size()){
                preNewCount = teacherNewAddSet.size();
                mNewSelectNumTextView.setText(String.valueOf(preNewCount));
                if(teacherNewAddSet.size() == 0){
                    mNewSelectNumTextView.setVisibility(View.GONE);
                }else {
                    mNewSelectNumTextView.setVisibility(View.VISIBLE);
                }
            }
            RxCountTeacherNumBean rxCountTeacherNumBean = new RxCountTeacherNumBean();
            rxCountTeacherNumBean.setTeacherInfoBeanSet(teacherInfoBeanSet);
            rxCountTeacherNumBean.setTeacherNewAddSet(teacherNewAddSet);
            rxCountTeacherNumBean.setClickSureButton(false);
            RxBus.getDefault().post(rxCountTeacherNumBean);

        }
    };

    private IsDeleteSelectItemDialog mDialog;
    //显示弹框确认是否删除全部
    private void popupConfirmClearAllDialog(String content) {
        if(mDialog != null)
            mDialog = null; //置空然后重新赋值
        mDialog = new IsDeleteSelectItemDialog(mActivity, content, new IsDeleteSelectItemDialog.Listener() {
            @Override
            public void onDialogDismissListener() {
//                backgroundAlpha(1f);
            }

            @Override
            public void onSureListerner() {
                int preAllCount = teacherInfoBeanSet.size();
                int preNewCount = teacherNewAddSet.size();
                teacherInfoBeanSet.clear();
                teacherNewAddSet.clear();
                if(preAllCount !=  teacherInfoBeanSet.size()){
                    String count = "已选择"+teacherInfoBeanSet.size()+"人";
                    Log.e("lenita","onDeleteItemListener = "+count);
                    mCountNumTextView.setText(count);
                    if(teacherInfoBeanSet.size() == 0 && mCountNumPopupWindow != null){
                        mCountNumPopupWindow.dismiss();
                    }
                }
                if(preNewCount != teacherNewAddSet.size()){
                    preNewCount = teacherNewAddSet.size();
                    mNewSelectNumTextView.setText(String.valueOf(preNewCount));
                    if(teacherNewAddSet.size() == 0){
                        mNewSelectNumTextView.setVisibility(View.GONE);
                    }else {
                        mNewSelectNumTextView.setVisibility(View.VISIBLE);
                    }
                }
                RxCountTeacherNumBean rxCountTeacherNumBean = new RxCountTeacherNumBean();
                rxCountTeacherNumBean.setTeacherInfoBeanSet(teacherInfoBeanSet);
                rxCountTeacherNumBean.setTeacherNewAddSet(teacherNewAddSet);
                rxCountTeacherNumBean.setClickSureButton(false);
                RxBus.getDefault().post(rxCountTeacherNumBean);

            }
        });  //使用自定义的样式
        mDialog.show();
    }

    private void backgroundAlpha(float b) {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.alpha = b;
        getWindow().setAttributes(layoutParams);
    }

    private void updateBottonCountNun(RxCountTeacherNumBean rxCountTeacherNumBean){
        teacherInfoBeanSet = rxCountTeacherNumBean.getTeacherInfoBeanSet();
        teacherNewAddSet = rxCountTeacherNumBean.getTeacherNewAddSet();
        String count = "已选择"+teacherInfoBeanSet.size()+"人";
        mCountNumTextView.setText(count);
        if(teacherNewAddSet.size() > 0 ){
            mNewSelectNumTextView.setVisibility(View.VISIBLE);
            mNewSelectNumTextView.setText(""+teacherNewAddSet.size() );
        }else {
            mNewSelectNumTextView.setVisibility(View.GONE);
        }
    }


   /* CountNumAdapter.Listener mDeleteListener = new CountNumAdapter.Listener() {
        @Override
        public void onDeleteItemListener(Object object) {
            //删除一条数据
            int preAllCount = teacherInfoBeanSet.size();
            int preNewCount = teacherNewAddSet.size();
            teacherInfoBeanSet.remove(object);
            teacherNewAddSet.remove(object);
            if(preAllCount !=  teacherInfoBeanSet.size()){
                String count = "已选择"+teacherInfoBeanSet.size()+"人";
                Log.e("lenita","onDeleteItemListener = "+count);
                mCountNumTextView.setText(count);
                if(teacherInfoBeanSet.size() == 0 && mCountNumPopupWindow !=null){
                    mCountNumPopupWindow.dismiss();
                }
            }
            if(preNewCount != teacherNewAddSet.size()){
                preNewCount = teacherNewAddSet.size();
                mNewSelectNumTextView.setText(String.valueOf(preNewCount));
                if(teacherNewAddSet.size() == 0){
                    mNewSelectNumTextView.setVisibility(View.GONE);
                }else {
                    mNewSelectNumTextView.setVisibility(View.VISIBLE);
                }
            }

        }
    };*/
}
