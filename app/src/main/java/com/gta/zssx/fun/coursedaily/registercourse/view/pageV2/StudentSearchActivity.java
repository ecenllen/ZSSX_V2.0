package com.gta.zssx.fun.coursedaily.registercourse.view.pageV2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.fun.adjustCourse.model.bean.GetTeacherBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.RxStudentList;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SectionBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.StudentListNewBean;
import com.gta.zssx.fun.coursedaily.registercourse.view.adapter.StudentSearchAdapter;
import com.gta.zssx.fun.coursedaily.registercourse.view.adapter.ui.StudentInfoDialog;
import com.gta.zssx.fun.coursedaily.registercourse.view.page.StudentListInnerFragment;
import com.gta.zssx.fun.coursedaily.registercourse.view.page.StudentListInnerFragmentV2;
import com.gta.zssx.pub.base.BaseAppCompatActivity;
import com.gta.zssx.pub.util.RxBus;
import com.gta.zssx.pub.widget.ClearEditText;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by lan.zheng on 2017/2/28.
 */
public class StudentSearchActivity extends BaseAppCompatActivity {

    public final static String sStudent_list = "student_list";
    public final static String sPosition = "position";
    public final static String sSectionBean = "section_bean";
    public final static String sIsClickAnyStatus = "is_click_any_status";
    private int mPosition;
    private SectionBean mSectionBean;
//    private List<StudentListBean> mStudentListBean;  //原来的Bean
    private List<StudentListNewBean> mStudentListNewBeanList;  //修改后的Bean
    private List<StudentListNewBean> mSearchResultList;  //搜索结果
    public TextView textView;
    public RecyclerView recyclerView;
    public TextView notDataView;
    public ClearEditText clearEditText;
    public StudentSearchAdapter studentSearchAdapter;
    public String mKeyWord;
    public boolean isClickAnyStatus = false; //是否有点击过任何的状态按钮，如果没有任何改变，返回的时候分数不应该变化

    TextWatcher mSearchStudentTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //编辑中
            mKeyWord = s.toString ().trim ();
            if(TextUtils.isEmpty(mKeyWord)){  //关键字为空，显示所有的学生数据
                notDataView.setVisibility(View.GONE);
                studentSearchAdapter.setSearchData(mStudentListNewBeanList);
            }else {
                //进行搜索
                mSearchResultList = new ArrayList<>();
                for(int i = 0;i<mStudentListNewBeanList.size() ;i++){
                    String studentName = mStudentListNewBeanList.get(i).getStundentName();
                    String pingyin = mStudentListNewBeanList.get(i).getNameOfPinYin();
                    String pingyinLowerCase = exChange(pingyin);
                    String keyWordLowerCase = exChange(mKeyWord);
                    if(studentName.contains(mKeyWord) || pingyinLowerCase.contains(keyWordLowerCase)){
                        mSearchResultList.add(mStudentListNewBeanList.get(i));
                    }
                }
                //对搜索结果处理
                if(mSearchResultList.size() == 0){
                    notDataView.setVisibility(View.VISIBLE);
                }else {
                    notDataView.setVisibility(View.GONE);
                }
                studentSearchAdapter.setSearchData(mSearchResultList);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    /**
     * @param context  上下文
     * @param sectionBean    节次Id
     * @param position 位置
     * @param studentListNewBeanList  数据集合
     */
    @Deprecated
    public static void start(StudentListInnerFragment contextFragment,Context context, SectionBean sectionBean, int position , List<StudentListNewBean> studentListNewBeanList) {
        final Intent lIntent = new Intent(context, StudentSearchActivity.class);
        Bundle lBundle = new Bundle();
        lBundle.putSerializable(sSectionBean, sectionBean);
        lBundle.putInt(sPosition, position);
        lBundle.putSerializable(sStudent_list, (Serializable) studentListNewBeanList);
        lIntent.putExtras(lBundle);
        contextFragment.startActivityForResult(lIntent, Activity.RESULT_FIRST_USER);
    }

    /**
     * @param context  上下文
     * @param sectionBean    节次Id
     * @param position 位置
     * @param studentListNewBeanList  数据集合
     */
    public static void start(StudentListInnerFragmentV2 contextFragment, Context context, SectionBean sectionBean, int position , List<StudentListNewBean> studentListNewBeanList) {
        final Intent lIntent = new Intent(context, StudentSearchActivity.class);
        Bundle lBundle = new Bundle();
        lBundle.putSerializable(sSectionBean, sectionBean);
        lBundle.putInt(sPosition, position);
        lBundle.putSerializable(sStudent_list, (Serializable) studentListNewBeanList);
        lIntent.putExtras(lBundle);
        contextFragment.startActivityForResult(lIntent, Activity.RESULT_FIRST_USER);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_search);
        initData();
        initView();
        initListener();
        loadData();
    }

    private void initData(){
        mKeyWord = "";
        mStudentListNewBeanList = new ArrayList<>();
        if(getIntent().getExtras().getSerializable(sStudent_list) != null){
            mStudentListNewBeanList = (List<StudentListNewBean>) getIntent().getExtras().getSerializable(sStudent_list);
        }
        mPosition = getIntent().getExtras().getInt(sPosition);
        mSectionBean = (SectionBean) getIntent().getExtras().getSerializable(sSectionBean);
    }

    private void initView(){
        textView = (TextView)findViewById(R.id.finish_search_tv);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //告知Fragment需要改变View
                Log.e("lenita","toldFragmentNeedToChangeView mPosition = "+mPosition);
                callBackToFragment();
                hideSoftKeyboard();
            }
        });
        notDataView = (TextView)findViewById(R.id.no_data_tv);
        recyclerView = (RecyclerView)findViewById(R.id.search_student_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        clearEditText = (ClearEditText)findViewById(R.id.search_edittext);
        clearEditText.addTextChangedListener(mSearchStudentTextWatcher);
        clearEditText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        clearEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {  //点击搜索也会搜索
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.e("lenita","clearEditText.setOnEditorActionListener");
                mKeyWord = clearEditText.getText().toString ().trim ();
                if(TextUtils.isEmpty(mKeyWord)){  //关键字为空，显示所有的学生数据
                    notDataView.setVisibility(View.GONE);
                    studentSearchAdapter.setSearchData(mStudentListNewBeanList);
                }else {
                    //进行搜索
                    mSearchResultList = new ArrayList<>();
                    for(int i = 0;i<mStudentListNewBeanList.size() ;i++){
                        String studentName = mStudentListNewBeanList.get(i).getStundentName();
                        String pingyin = mStudentListNewBeanList.get(i).getNameOfPinYin();
                        String pingyinLowerCase = exChange(pingyin);
                        String keyWordLowerCase = exChange(mKeyWord);
                        if(studentName.contains(mKeyWord) || pingyinLowerCase.contains(keyWordLowerCase)){
                            mSearchResultList.add(mStudentListNewBeanList.get(i));
                        }
                    }
                    //对搜索结果处理
                    if(mSearchResultList.size() == 0){
                        notDataView.setVisibility(View.VISIBLE);
                    }else {
                        notDataView.setVisibility(View.GONE);
                    }
                    studentSearchAdapter.setSearchData(mSearchResultList);
                }
                hideSoftKeyboard();
                return false;
            }
        });
    }


    public CompositeSubscription mCompositeSubscription;  //监听
    private void initListener(){
        mCompositeSubscription = new CompositeSubscription();
        /**
         * 学生考勤状态监听
         */
        mCompositeSubscription.add(RxBus.getDefault().toObserverable(RxStudentList.class)
                .subscribe(new Subscriber<RxStudentList>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(RxStudentList rxStudentList) {
                        isClickAnyStatus = true;  //只要有任何的点击，都代表数据有更新
                        Log.e("lenita","S...Activity RxBus isClickAnyStatus = "+isClickAnyStatus);
                        mStudentListNewBeanList = rxStudentList.getStudentListBeen();
                    }
                }));

    }

    private void loadData(){
        studentSearchAdapter = new StudentSearchAdapter(mSectionBean, mStudentListNewBeanList, this,mPosition, new StudentSearchAdapter.Listener() {
            @Override
            public void itemClick(StudentListNewBean studentListNewBean) {
//                backgroundAlpha(0.8f);
                showStudentInfoDialog(studentListNewBean);
            }
        });
        recyclerView.setAdapter(studentSearchAdapter);
    }

    public StudentInfoDialog mStudentInfoDialog;
    private void showStudentInfoDialog(StudentListNewBean studentListNewBean){
        if(mStudentInfoDialog != null){
            mStudentInfoDialog = null;
        }
        mStudentInfoDialog = new StudentInfoDialog(this,studentListNewBean, new StudentInfoDialog.Listener() {
            @Override
            public void onDialogDismissListener() {
//                backgroundAlpha(1f);
                //TODO 暂时不用处理
            }
        });
        mStudentInfoDialog.show();
    }

    @Override
    public void onBackPressed() {
        //告知Fragment需要改变View
        callBackToFragment();
        hideSoftKeyboard();
        super.onBackPressed();
    }

    private void callBackToFragment(){
        Intent intent = new Intent();
        Bundle bundle =  new Bundle();
        bundle.putSerializable(sStudent_list,(Serializable)mStudentListNewBeanList);
//        bundle.putSerializable(sStudent_list,(Serializable)mStudentListNewBeanList); //废弃
        bundle.putBoolean(sIsClickAnyStatus,isClickAnyStatus);
        intent.putExtras(bundle);
        setResult(mPosition,intent);
        finish();
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

    //大写转小写
    public static String exChange(String str){
        StringBuffer sb = new StringBuffer();
        if(str!=null){
            for(int i=0;i<str.length();i++){
                char c = str.charAt(i);
                if(Character.isUpperCase(c)){
                    sb.append(Character.toLowerCase(c));
                }else{
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    private void backgroundAlpha(float b) {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.alpha = b;
        getWindow().setAttributes(layoutParams);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeSubscription.unsubscribe();
    }

}
