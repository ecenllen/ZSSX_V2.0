package com.gta.zssx.fun.coursedaily.registerrecord.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.DetailItemShowBean;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.ClassInfoDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.DetailRegisteredRecordDto;
import com.gta.zssx.fun.coursedaily.registerrecord.presenter.DetailRegisteredRecordPresenter;
import com.gta.zssx.fun.coursedaily.registerrecord.view.DetailRegisteredRecordView;
import com.gta.zssx.fun.coursedaily.registerrecord.view.MyExpandListView;
import com.gta.zssx.fun.coursedaily.registerrecord.view.adapter.CheckExpandableListAdapter;
import com.gta.zssx.pub.base.BaseActivity;
import com.gta.zssx.pub.manager.ToolBarManager;
import com.gta.zssx.pub.util.LogUtil;

import java.util.List;

/**
 * Created by lan.zheng on 2016/7/4.
 */
public class DetailRegisteredRecordActivity extends BaseActivity<DetailRegisteredRecordView, DetailRegisteredRecordPresenter> implements DetailRegisteredRecordView{

    public static final String PAGE_TAG = DetailRegisteredRecordActivity.class.getSimpleName();
    public static String CLASS_INFO_DTO = "CLASS_INFO_DTO";
    private ClassInfoDto mClassInfoDto;
    private String ClassName;

    public Toolbar mToolbar;
    public ToolBarManager mToolBarManager;
    private FrameLayout mRelativeLayout;
    private TextView mTextViewNotResult;
    private TextView mCourseNameTextView;
    private TextView mDeptNameTextView;
    private TextView mTeacherNameTextView;
    private TextView mStudentCountTextView;
    private TextView mAttendCountTextView;
    private TextView mCourseTeacherTextView;
    private TextView mRemarkTextView;

    private MyExpandListView expandableListView;
    //    private ExpandableListView expandableListView;
//    private ExpandableListAdapter mExpandableListAdapter;
    private CheckExpandableListAdapter mExpandableListAdapter;
    String[] lateString;    //迟到
    String[] leaveString;   //请假
    String[] truantString;  //旷课
    String[] holidayString; //公假
    private boolean isHaveDataToShow = false;
    private ScrollView mScrollView;

    @NonNull
    @Override
    public DetailRegisteredRecordPresenter createPresenter() {
        return new DetailRegisteredRecordPresenter();
    }

    @Override
    public void showResultList(DetailRegisteredRecordDto detailRegisteredRecordDto) {
        formatCount(detailRegisteredRecordDto);  //迟到等数量变成所需格式
        updateView(detailRegisteredRecordDto);   //更新View
        isHaveDataToShow = true;  //有数据就显现，否则显示“无法显示详细信息”
        updateExpandableList();
    }

    @Override
    public void updateExpandableList() {
        if(isHaveDataToShow){
            updateList();
        }else {
            //没有数据的时候就显示空白页
            this.hideDialog();
            mRelativeLayout.setVisibility(View.VISIBLE);
            mScrollView.setVisibility(View.GONE);
            mTextViewNotResult.setText("无法显示登记的详细信息");
            mTextViewNotResult.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 返回的数据进行分出迟到人数，请假人数，旷课人数和公假人数列表处理
     * @param detailRegisteredRecordDto
     */
    private void formatCount(DetailRegisteredRecordDto detailRegisteredRecordDto){
        if(detailRegisteredRecordDto.getLateList() != null){
            int count = detailRegisteredRecordDto.getLateList().size();
            lateString = new String[count];
            for(int i=0;i<count;i++){
                lateString[i] = detailRegisteredRecordDto.getLateList().get(i).getStudentName()+" ("
                        +detailRegisteredRecordDto.getLateList().get(i).getStudentNo()+")";
            }
        }else
            LogUtil.Log("lenita","lateString length = "+lateString.length);
        //=================================
        if(detailRegisteredRecordDto.getLeaveList() != null){
            int count = detailRegisteredRecordDto.getLeaveList().size();
            leaveString = new String[count];
            for(int i=0;i<count;i++){
                leaveString[i] = detailRegisteredRecordDto.getLeaveList().get(i).getStudentName()+" ("
                        +detailRegisteredRecordDto.getLeaveList().get(i).getStudentNo()+")";
            }
        }else
            LogUtil.Log("lenita","lateString length = "+leaveString.length);
        //=================================
        if(detailRegisteredRecordDto.getTruantList() != null){
            int count = detailRegisteredRecordDto.getTruantList().size();
            truantString = new String[count];
            for(int i=0;i<count;i++){
                truantString[i] = detailRegisteredRecordDto.getTruantList().get(i).getStudentName()+" ("
                        +detailRegisteredRecordDto.getTruantList().get(i).getStudentNo()+")";
            }
        }else
            LogUtil.Log("lenita","lateString length = "+truantString.length);
        //===================================
        if(detailRegisteredRecordDto.getHolidayList() != null){
            int count = detailRegisteredRecordDto.getHolidayList().size();
            holidayString = new String[count];
            for(int i=0;i<count;i++){
                holidayString[i] = detailRegisteredRecordDto.getHolidayList().get(i).getStudentName()+"   ("
                        +detailRegisteredRecordDto.getHolidayList().get(i).getStudentNo()+")";
            }
        }else
            LogUtil.Log("lenita","lateString length = "+holidayString.length);

        LogUtil.Log("lenita","length = "+lateString.length + " "+ leaveString.length+" "+truantString.length+" "+holidayString.length);
    }

    private void updateView(DetailRegisteredRecordDto detailRegisteredRecordDto){
        //根据获取到的信息来更新View
        if(detailRegisteredRecordDto.getCourseName()!=null && detailRegisteredRecordDto.getCourseName().size() > 0){
            List<DetailItemShowBean.CourseInfoBean> courseInfoBeanList = detailRegisteredRecordDto.getCourseName();
            String courseName = presenter.getCourseString(courseInfoBeanList);
            //TODO 列表转换
            mCourseNameTextView.setText(courseName);
        }
        if(detailRegisteredRecordDto.getDeptName()!=null && !detailRegisteredRecordDto.getDeptName().equals("")){
            mDeptNameTextView.setText(detailRegisteredRecordDto.getDeptName());
        }
        if(detailRegisteredRecordDto.getTeacherName()!=null && !detailRegisteredRecordDto.getTeacherName().equals("")){
            mTeacherNameTextView.setText(detailRegisteredRecordDto.getTeacherName());
        }
        if(detailRegisteredRecordDto.getCourseTeacher() != null && detailRegisteredRecordDto.getCourseTeacher().size() > 0){
            List<DetailItemShowBean.TeacherInfoBean> teacherInfoBeanList = detailRegisteredRecordDto.getCourseTeacher();
            String teacherName = presenter.getTeacherString(teacherInfoBeanList);
            //TODO 列表转换
            mCourseTeacherTextView.setText(teacherName);
        }
        if(!TextUtils.isEmpty(String.valueOf(detailRegisteredRecordDto.getStudentCount()))){
            mStudentCountTextView.setText(""+detailRegisteredRecordDto.getStudentCount());
        }
        if(!TextUtils.isEmpty(String.valueOf(detailRegisteredRecordDto.getAttendCount()))){
            mAttendCountTextView.setText(""+detailRegisteredRecordDto.getAttendCount());
        }
        if(detailRegisteredRecordDto.getMemo()!=null && !detailRegisteredRecordDto.getMemo().equals("")){
            mRemarkTextView.setText(detailRegisteredRecordDto.getMemo());
        }
    }

    private void updateList(){
        LogUtil.Log("lenita","updateList()");
        mExpandableListAdapter = null;
        mExpandableListAdapter = new CheckExpandableListAdapter(this,lateString,leaveString,truantString,holidayString);
        expandableListView.setAdapter(mExpandableListAdapter);
        //默认为全部展开
        int groupCount = expandableListView.getCount();
        for (int i=0; i<groupCount; i++) {
            expandableListView.expandGroup(i);
        }
        //进行高度计算,为了备注的显示
        ViewGroup.LayoutParams lLayoutParams = expandableListView.getLayoutParams();
        lLayoutParams.height = getHeight();
        expandableListView.setLayoutParams(lLayoutParams);
        expandableListView.requestLayout();

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
//                LogUtil.Log("lenita"," onGroupClick");
                mExpandableListAdapter.updateChecked(groupPosition);
//                CheckExpandableListAdapter.group_checked[groupPosition] = CheckExpandableListAdapter.group_checked[groupPosition]+1;
                // 刷新界面
                mExpandableListAdapter.notifyDataSetChanged();
                return false;
            }
        });
        //加载完毕再显示所有数据
        this.hideDialog();
        mRelativeLayout.setVisibility(View.VISIBLE);
        mTextViewNotResult.setVisibility(View.GONE);
        //设置焦点为顶部，因为列表为默认展开，所以需要适配回顶端的标题显示，而不是中间的列表处
        mCourseNameTextView.setFocusable(true);
        mCourseNameTextView.setFocusableInTouchMode(true);
        mCourseNameTextView.requestFocus();
    }


    /**
     * 进行高度计算,为了备注的显示
     */
    private int getHeight(){
        int totalHeight = 0;
        int deliverCount= 0;
        for (int i = 0; i < mExpandableListAdapter.getGroupCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listgroupItem = mExpandableListAdapter .getGroupView(i, true, null, expandableListView );
            listgroupItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listgroupItem .getMeasuredHeight(); // 统计所有子项的总高度
            System. out.println("height : group" +i +"次" +totalHeight );
            for (int j = 0; j < mExpandableListAdapter.getChildrenCount( i); j++) {
                View listchildItem = mExpandableListAdapter .getChildView(i, j, false , null, expandableListView);
                listchildItem.measure(0, 0); // 计算子项View 的宽高
                totalHeight += listchildItem.getMeasuredHeight(); // 统计所有子项的总高度
                deliverCount++;
            }
        }
        totalHeight = totalHeight+(expandableListView.getDividerHeight() * (mExpandableListAdapter.getGroupCount() + deliverCount- 1));
        return totalHeight;
    }

    public static void start(Context context, ClassInfoDto classInfoDto) {
        final Intent lIntent = new Intent(context, DetailRegisteredRecordActivity.class);
        Bundle lBundle = new Bundle();
        lBundle.putSerializable(CLASS_INFO_DTO,classInfoDto);
        lIntent.putExtras(lBundle);
        if (!(context instanceof Activity)) {
                lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(lIntent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_detail_registered_record);
        /*WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight  = display.getHeight();
        LogUtil.Log("http",screenWidth+" "+screenHeight);
        Toast.Long(this,screenWidth+" "+screenHeight);*/
        initData();
        initView();
        loadData();
    }

    //用于页面间数据接收
    private void initData() {
        presenter.attachView(this);
        mClassInfoDto = (ClassInfoDto) this.getIntent().getExtras().getSerializable(CLASS_INFO_DTO);
        if(mClassInfoDto !=  null){
            ClassName = mClassInfoDto.getClassName();
        }
        //初始化数组
        lateString = new String[0];
        leaveString = new String[0];
        truantString = new String[0];
        holidayString = new String[0];
    }

    //初始化view
    private void initView() {
        findViews();
        initToolbar();
        setOnInteractListener();
    }

    private void initToolbar() {
        mToolBarManager.init();
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mToolBarManager
                .showBack(true)
                .showLeftImage(false)
                .setTitle(ClassName)
                .showRightButton(false)
                .setRightText("");
    }

    //事件处理
    private void setOnInteractListener() {

    }

    //绑定控件
    private void findViews() {
        mRelativeLayout = (FrameLayout) this.findViewById(R.id.layout_all) ;
        mTextViewNotResult = (TextView)this.findViewById(R.id.layout_tv) ;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolBarManager = new ToolBarManager(mToolbar, this);

        mCourseNameTextView = (TextView)this.findViewById(R.id.tv_course_name_show);
        mDeptNameTextView = (TextView)this.findViewById(R.id.tv_dpat_show);
        mTeacherNameTextView = (TextView)this.findViewById(R.id.tv_teacher_name_show);
        mStudentCountTextView= (TextView)this.findViewById(R.id.tv_class_person_num_show);
        mAttendCountTextView = (TextView)this.findViewById(R.id.tv_class_actual_come_show);
        mCourseTeacherTextView = (TextView)this.findViewById(R.id.tv_text_class_teacher);
        mRemarkTextView = (TextView)this.findViewById(R.id.tv_class_remark_show);
        expandableListView = (MyExpandListView) this.findViewById(R.id.expanded_menu);
        mScrollView = (ScrollView)this.findViewById(R.id.sv_main_data_show);
    }

    private void getRecordDetail(){
        presenter.getRegisteredRecordDetailData(mClassInfoDto);
    }

    private void loadData() {
        // 设置默认图标为不显示状态
        expandableListView.setGroupIndicator(null);
        mRelativeLayout.setVisibility(View.INVISIBLE);
        getRecordDetail();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView(false);
        mToolBarManager.destroy();
    }

}
