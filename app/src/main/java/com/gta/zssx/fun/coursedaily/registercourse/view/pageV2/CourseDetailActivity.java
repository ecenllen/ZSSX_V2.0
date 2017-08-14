package com.gta.zssx.fun.coursedaily.registercourse.view.pageV2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.blankj.utilcode.util.ToastUtils;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.model.ClassDataManager;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.DataBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.DefaultRegistInfoBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.DetailItemShowBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.RxCountTeacherNumBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.RxSectionBeanSignList;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SectionBean;
import com.gta.zssx.fun.coursedaily.registercourse.presenter.RegisterDetailPresenter;
import com.gta.zssx.fun.coursedaily.registercourse.view.DetailView;
import com.gta.zssx.fun.coursedaily.registercourse.view.adapter.DetailItemSelectAdapter;
import com.gta.zssx.fun.coursedaily.registercourse.view.adapter.ui.SureClickDialog;
import com.gta.zssx.fun.coursedaily.registercourse.view.adapter.ui.UpdateTeacherAndCourseDialog;
import com.gta.zssx.fun.coursedaily.registercourse.view.base.Constant;
import com.gta.zssx.fun.coursedaily.registercourse.view.base.RegisterCourseActivity;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.RegisteredRecordFromSignatureDto;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.pub.base.BaseActivity;
import com.gta.zssx.pub.manager.ToolBarManager;
import com.gta.zssx.pub.util.RxBus;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import rx.Subscriber;
import rx.functions.Action1;

import static com.gta.zssx.fun.classroomFeedback.view.page.RegisterDetailsActivity.WHICH_PAGE;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/7/4.
 * @since 1.0.0
 */
public class CourseDetailActivity extends BaseActivity<DetailView, RegisterDetailPresenter> implements DetailView {

    private final static  String TAG = CourseDetailActivity.class.getSimpleName();
    public final static String TEACHER_INFO_BEAN = "TEACHER_INFO_BEAN";  //回调老师列表
    public final static String COURSE_INFO_BEAN = "COURSE_INFO_BEAN";    //回调课程列表
    public final static String PAGE_COURSE_DAILY = "page_course_daily";

//    private TextView mSectionTv;  //废弃
//    public String mCourseName;  //废弃
//    private DisableEmojiEditText mCourseNameEt;  //废弃
    //改后的节次、课程、教师选择
    private TextView mClassTv;  //上课班级
    private TextView mCourseDateTv;
    private RecyclerView sectionRecyclerView;
    private RecyclerView courseRecyclerView;
    private RecyclerView teacherRecyclerView;
    private OptionsPickerView mOptionsPickerView;

    private ArrayList<String> mDateRange;   //时间段选择
    public List<SectionBean> mSectionBeenList = new ArrayList<>();  //节次-排序的，用于删除节次和节次选择返回的时候
    public Set<SectionBean> mSectionBeanSet = new HashSet<>();      //节次-Set集合,从节次选择回来的数据
    public Set<DetailItemShowBean.CourseInfoBean> mCourseInfoBeanSet = new HashSet<>();  //课程
    public Set<DetailItemShowBean.TeacherInfoBean> mTeacherInfoBeanSet = new HashSet<>(); //教师
    public View footerTeacher;
    public View footerCourse;
    public View footerSection;

    private UserBean mUserBean;
    public String mUserId;
    public String mUserName;
    public String mDateString;  //年-月-日,用于获取默认课程和教师，获取我的课程和传递到下一页面
    public int mFirstSection = -1; //一个节次，用于判断是否要拉取新的默认课程教师
    public String mFirstSectionName = "";
    public int mClassId; //登记班级-传入的时候
    public String mSignDate; //登记日期 年-月-日

    public DataBean mDataBean;
    public int mRequestCode = 110;
    public String mSignFormatDay;
    private boolean unavailableSignDate;  //有无可登记日期
    private boolean notCanSignSection;  //有无可登记节次
    private boolean isFirstTimeEnter = true;  //是否是第一次进入
    private boolean isClickNextStep = false;  //是否是点击了下一步
    public static boolean isSectionHaveBeenSign = false;  //回调是否有节次已经被登记

    //扩大点击范围
    private RelativeLayout timeSelectLayout;
//    private RelativeLayout sectionSelectLayout;   //废弃
//    private RelativeLayout classNameInputLayout;  //废弃

    //修改第一次进入选择的日期和节次
    private int mModifyFirstTimeEnterSection = 0;  //修改专用，为0表示无可选择的节次
    private String mModifyFirstTimeEnterDate = ""; //修改专用，为""表示无可选择的日期

    @NonNull
    @Override
    public RegisterDetailPresenter createPresenter() {
        return new RegisterDetailPresenter();
    }


    private void initTimePicker(RegisterDetailPresenter.Combined combined) {
        String StartDate = combined.getApproveBean().getDate();
        String[] lSplit = StartDate.split(" ");
        String lStartDate = lSplit[0];   //审核日当天也能被登记
//        DateTime lDateTime = new DateTime(lStartDate);
//        lStartDate = lDateTime.plusDays(1).toString("yyyy-MM-dd");
        String lEndDate = combined.getServerTimeDto().getDate();
        mDateRange = presenter.getDateRange(lStartDate, lEndDate);
        mOptionsPickerView.setPicker(mDateRange);
        if (!mIsModify) {
            mCourseDateTv.setText(mDateRange.get(mDateRange.size() - 1));
            mOptionsPickerView.setSelectOptions(mDateRange.size() - 1);
        } else {
            mOptionsPickerView.setSelectOptions(mDateRange.indexOf(mSignFormatDay));
        }
        mOptionsPickerView.setCyclic(false);
        mOptionsPickerView.setCancelable(true);
    }

    @Override
    public void showResult(RegisterDetailPresenter.Combined combined) {
        String StartDate = combined.getApproveBean().getDate();
        String lDate = combined.getServerTimeDto().getDate();
        String[] lSplit = lDate.split(" ");

        //判断是否有日期可以选择
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        boolean isNotCanSign = false;
        try {
            Date startTime = simpleDateFormat.parse(StartDate);
            Date endTime = simpleDateFormat.parse(lDate);
            if(startTime.getTime() > endTime.getTime()){
                isNotCanSign = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (isNotCanSign) {
            mCourseDateTv.setText(getResources().getString(R.string.not_register_date));
            unavailableSignDate = true;
            return;
        }

        /*if (lStartDate.equals(lSplit[0])) {
            mCourseDateTv.setText("暂无可登记的日期");
            unavailable = true;
            return;
        }*/

        if (!mIsModify) {
            mSignDate = lSplit[0];
            mDateString = lSplit[0];
            presenter.getTimeAndSection(mUserBean.getUserId(), mClassId, mSignDate,true);
        }
        initTimePicker(combined);
    }

    @Override
    public void showLastestSection(SectionBean sectionBean) {
        mSectionBeenList = new ArrayList<>();
        if (sectionBean != null) {
            notCanSignSection = false;
            sectionBean.setHaveBeenSignFlag(false);
            Set<SectionBean> lSectionBeen = new HashSet<>();
            lSectionBeen.add(sectionBean);
            mSectionBeanSet = lSectionBeen;
            mSectionBeenList.add(sectionBean);
            //有可能是回调进入，这里优先判断，如果是回调进入且最先的节次没有变化，这里可以直接return不做处理
            if(mFirstSection != -1 && mFirstSection == sectionBean.getSectionId() && isSectionHaveBeenSign){
                showSectionView();
//                Log.e("lenita","有可能是回调进入，这里优先判断，如果是回调进入且最先的节次没有变化，这里显示View后直接return不做处理");
                return;
            }
            //其他情况都要去请求服务器
            mFirstSection = sectionBean.getSectionId();
            mFirstSectionName = sectionBean.getLesson();
            setDefaultSectionData(); //第一次进入的时候去获取
//            ClassDataManager.getDataCache().setSection(lSectionBeen);
        } else {
//            mSectionTv.setText(this.getString(R.string.no_unsign_section));
            notCanSignSection = true;
            mToolBarManager.getRightButton().setEnabled(false);
            mSectionBeanSet = new HashSet<>();
            //TODO 没有可登记的节次，强制让用户重新选择日期
            showAllSectionHaveBeenSignDialog();
        }
        showSectionView();  //显示节次信息

    }

    @Override
    public void showSectionDefaultTeacherAndCourse(DefaultRegistInfoBean defaultRegistInfoBean,DetailItemShowBean.TeacherInfoBean teacherInfoBean) {
        List<DetailItemShowBean.TeacherInfoBean> teacherInfoBeanList = defaultRegistInfoBean.getMultipleTeachersList();
        List<DetailItemShowBean.CourseInfoBean> courseInfoBeanList = defaultRegistInfoBean.getMultipleCoursesList();
        //不管登记的是不是老师，都封装好登记老师的实体
        teacherInfoBean.setTeacherName(mUserName);
        teacherInfoBean.setTeacherId(mUserId);
        //弹框判断,第一次进入且不是修改进入的情况可以直接赋值
        if(isFirstTimeEnter && !mIsModify){
            //第一次获取的直接赋值
            isFirstTimeEnter = false;
            mCourseInfoBeanSet = new HashSet<>(courseInfoBeanList);
            if(teacherInfoBeanList.size() == 0){
                //没有课表老师的时候去判断登记的老师是不是老师，如果是可以用登记老师作为上课老师
                if(defaultRegistInfoBean.getIsTeacher()){
                    mTeacherInfoBeanSet.clear();
                    mTeacherInfoBeanSet.add(teacherInfoBean);
                }
            }else {
                //有课表老师
                mTeacherInfoBeanSet = new HashSet<>(teacherInfoBeanList);
            }
            showCourseView();
            showTeacherView();
            setTitleRightButtonEnable();
        }else {
            //改变获取的时候就要判断是否要弹框,如果原来就是空的直接进行赋值
            if(mCourseInfoBeanSet.size()== 0 && mTeacherInfoBeanSet.size() == 0 ) {
                //原来为空，课表都可以直接赋值，不做询问
                mCourseInfoBeanSet = new HashSet<>(courseInfoBeanList);
                mTeacherInfoBeanSet = new HashSet<>(teacherInfoBeanList);
                showCourseView();
                showTeacherView();
                setTitleRightButtonEnable();
            } else {
                //如果原来不为空，且获取到的默认课表也不为空，就要弹框，否则不做任何处理
                if(teacherInfoBeanList.size() > 0 || courseInfoBeanList.size() > 0){
                    //todo 当数量一样的时候要去判断内容是否完全一样
                    if(teacherInfoBeanList.size() == mTeacherInfoBeanSet.size() && courseInfoBeanList.size() == mCourseInfoBeanSet.size()){
                        boolean isNeedToShowPopupWindow = presenter.isNeedToShowPopupWindow(teacherInfoBeanList,courseInfoBeanList,new ArrayList<>(mTeacherInfoBeanSet),new ArrayList<>(mCourseInfoBeanSet));
                        if(isNeedToShowPopupWindow){
                            //todo 数量一样但是内容不一样直接弹框
//                            backgroundAlpha(0.8f);
                            showPopupWindow(teacherInfoBeanList,courseInfoBeanList);
                        }else {
                            Log.e("lenita","课程和老师与后台返回的数据一模一样，不需要弹框");
                        }
                    }else {
                        //todo 不一样的时候直接弹框
//                        backgroundAlpha(0.8f);
                        showPopupWindow(teacherInfoBeanList,courseInfoBeanList);
                    }
                }
            }

        }
    }

    @Override
    public void showSelectSectionNotSignCount(int notSignNum,boolean isCheckAllSection) {

        if(isCheckAllSection){
            if(notSignNum == 0){
                mFirstSection = -1;
                mFirstSectionName = "";
                //TODO 选择的日期所有节次都被登记了
                showAllSectionHaveBeenSignDialog();
            }
            return;
        }

        if(notSignNum != mSectionBeenList.size()){
            if(mIsModify){
                //修改的且换了日期发现该选中节次已经被登记了，提示用户清空节次
                mSectionBeenList.clear();
                mSectionBeanSet.clear();
                ToastUtils.showLongToast(getResources().getString(R.string.text_modify_section_have_been_sign));
                showSectionView();
            }else {
                //TODO 证明有被登记了的节次，弹框提示有被登记的节次
                showStatusSectionDialog();
            }

        }else {
            if(isClickNextStep){
                //TODO 当一样的时候，证明是正常的，如果是点击了下一步要跳转
                CourseSignWithAttendanceStatusActivity.start(CourseDetailActivity.this, mIsModify,mDataBean);
            }else {
                //TODO 一样的时候也要更新界面，因为之前有可能标了红色,且需要重新去拉取课表
                for(int i = 0;i < mSectionBeenList.size();i++){
                    mSectionBeenList.get(i).setHaveBeenSignFlag(false);
                }
                mSectionBeanSet = new HashSet<>(mSectionBeanSet);
                showSectionView();
                //TODO 重新去获取默认课表
                mFirstSection = mSectionBeenList.get(0).getSectionId();
                mFirstSectionName = mSectionBeenList.get(0).getLesson();
                setDefaultSectionData();
            }
        }
    }

    public SureClickDialog mAllSectionHaveBeenSignDialog;
    private void showAllSectionHaveBeenSignDialog(){
        if (mAllSectionHaveBeenSignDialog != null)
            mAllSectionHaveBeenSignDialog = null;
//        backgroundAlpha(0.8f);
        String content = "当前日期"+mSignDate+"下没有可登记的节次，请重新选择日期。";
        mAllSectionHaveBeenSignDialog = new SureClickDialog(this, content, false,new SureClickDialog.Listener() {
            @Override
            public void onDialogDismissListener() {
//                backgroundAlpha(1f);
            }

            @Override
            public void onSureListerner() {
//                backgroundAlpha(0.8f);
                //TODO 重新选择日期
                mOptionsPickerView.show();
            }
        });
        mAllSectionHaveBeenSignDialog.setCanceledOnTouchOutside(false);
        mAllSectionHaveBeenSignDialog.setCancelable(false);
        mAllSectionHaveBeenSignDialog.show();
    }

    public SureClickDialog mChangeStatusSectionDialog;
    //显示弹框提示是否要删除登记的节次
    private void showStatusSectionDialog(){
        //TODO 得到带有是否登记标识的登记节次列表,true 和 false标记,并刷新界面
        presenter.getStatusSignList(mSectionBeenList);
        mSectionBeanSet = new HashSet<>(mSectionBeenList);
        showSectionView();
//        backgroundAlpha(0.8f);
        if (mChangeStatusSectionDialog != null)
            mChangeStatusSectionDialog = null;

        /*
        List<String> stringList = new ArrayList<>();
        for(int i = 0;i<mSectionBeenList.size();i++){
            if(mSectionBeenList.get(i).getHaveBeenSignFlag()){
                stringList.add(""+mSectionBeenList.get(i).getSectionId());
            }
        }*/
        String content = "当前要登记的节次中，";
        for(int i = 0;i <mSectionBeenList.size();i++){
            //TODO 如果是标识了true，表示已经被登记过了
            if(mSectionBeenList.get(i).getHaveBeenSignFlag()){
                if(i > 0 && i < mSectionBeenList.size()){
                    content += "、";
                }
                content += mSectionBeenList.get(i).getLesson();
            }
        }
        content += "已经被登记过,请修改。";
        mChangeStatusSectionDialog = new SureClickDialog(this,content,false, new SureClickDialog.Listener() {
            @Override
            public void onDialogDismissListener() {
//                backgroundAlpha(1f);
            }

            @Override
            public void onSureListerner() {
                //点击确认不做任何操作
                /*showSectionView();
                if (mSectionBeanSet != null && mSectionBeanSet.size() > 0) { //转换成列表，如2,3节就拿第2节的课表
                    Log.e("lenita","CourseD...Activity mSectionBeenList.get(0).getSectionID() = "+ mSectionBeenList.get(0).getSectionId());
                    if(mFirstSection != mSectionBeenList.get(0).getSectionId()){
                        mFirstSection = mSectionBeenList.get(0).getSectionId();
                        //选了新的节次的情况，我们去重新获取数据库，询问是否要同步课表的节次信息
                        setDefaultSectionData();
                    }
                }*/
            }
        });
        mChangeStatusSectionDialog.show();
    }

    public UpdateTeacherAndCourseDialog mUpdateTeacherAndCourseDialog;
    //显示弹框是否要更新课表
    private void showPopupWindow(List<DetailItemShowBean.TeacherInfoBean> teacherInfoBeenNewList,List<DetailItemShowBean.CourseInfoBean> courseInfoBeenNewList){
        String newCourseString = presenter.getUpdateCourseString(courseInfoBeenNewList);
        String newTeacherString = presenter.getUpdateTeacherString(teacherInfoBeenNewList);
        List<DetailItemShowBean.CourseInfoBean> courseInfoBeanList = new ArrayList<>(mCourseInfoBeanSet);
        List<DetailItemShowBean.TeacherInfoBean> teacherInfoBeanList = new ArrayList<>(mTeacherInfoBeanSet);
        String oldCourseString = presenter.getUpdateCourseString(courseInfoBeanList);
        String oldTeacherString = presenter.getUpdateTeacherString(teacherInfoBeanList);
        String newDateAndSection = mDateString+mFirstSectionName+"的课表：";
        if (mUpdateTeacherAndCourseDialog != null)
            mUpdateTeacherAndCourseDialog = null;
        mUpdateTeacherAndCourseDialog = new UpdateTeacherAndCourseDialog(this,oldCourseString, newCourseString, oldTeacherString, newTeacherString,newDateAndSection,
                new UpdateTeacherAndCourseDialog.Listener() {
            @Override
            public void onDialogDismissListener() {
                //TODO 暂时不监听
//                backgroundAlpha(1f);
            }

            @Override
            public void onUpdateDataClickListener() {
                mCourseInfoBeanSet = new HashSet<>(courseInfoBeenNewList);
                mTeacherInfoBeanSet = new HashSet<>(teacherInfoBeenNewList);
                showCourseView();
                showTeacherView();
                setTitleRightButtonEnable();
            }
        });
        mUpdateTeacherAndCourseDialog.setCanceledOnTouchOutside(false);
        mUpdateTeacherAndCourseDialog.show();
    }

  /*  private void backgroundAlpha(float b) {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.alpha = b;
        getWindow().setAttributes(layoutParams);
    }*/

    //显示节次
    private void showSectionView(){
        DetailItemSelectAdapter mSectionDetailItemSelectAdapter = new DetailItemSelectAdapter(this,mSectionBeenList,listener);
//        View footerSection = LayoutInflater.from(this).inflate(R.layout.list_item_add_footer,sectionRecyclerView,false);
        mSectionDetailItemSelectAdapter.setFooter(footerSection,mIsModify);
        sectionRecyclerView.setAdapter(mSectionDetailItemSelectAdapter);
    }

    //显示课程
    private void showCourseView(){
        Iterator<DetailItemShowBean.CourseInfoBean> courseInfoBeanIterator =  mCourseInfoBeanSet.iterator();
//        Set<Object> listCourse  = new HashSet<>();
        List<Object> objectList = new ArrayList<>();
        while (courseInfoBeanIterator.hasNext()){
            objectList.add(courseInfoBeanIterator.next()) ;
        }
        //设置值
        DetailItemSelectAdapter mCourseDetailItemSelectAdapter = new DetailItemSelectAdapter(this,objectList,DetailItemSelectAdapter.COURSE_BEAN_FLAG,listener);
//        View footerCourse = LayoutInflater.from(this).inflate(R.layout.list_item_add_footer,courseRecyclerView,false);
        mCourseDetailItemSelectAdapter.setFooter(footerCourse,mIsModify);
        courseRecyclerView.setAdapter(mCourseDetailItemSelectAdapter);
    }

    //显示教师
    private void showTeacherView(){
        Iterator<DetailItemShowBean.TeacherInfoBean> teacherInfoBeanIterator =  mTeacherInfoBeanSet.iterator();
//        Set<Object> listTeacher  = new HashSet<>();
        List<Object> objectList = new ArrayList<>();
        while (teacherInfoBeanIterator.hasNext()){
            objectList.add(teacherInfoBeanIterator.next()) ;
        }
        //设置值
        DetailItemSelectAdapter mTeacherDetailItemSelectAdapter = new DetailItemSelectAdapter(this,objectList,DetailItemSelectAdapter.TEACHER_BEAN_FLAG,listener);
//        View footerTeacher = LayoutInflater.from(this).inflate(R.layout.list_item_add_footer,teacherRecyclerView,false);
        mTeacherDetailItemSelectAdapter.setFooter(footerTeacher,mIsModify);
        teacherRecyclerView.setAdapter(mTeacherDetailItemSelectAdapter);
    }

    public Toolbar mToolbar;
    public ToolBarManager mToolBarManager;
    public static String sModifyBean = "modifyBean";
    public static String sIsModify = "isModify";
    public static String sCLASSNAME = "CLASSNAME";
    public static String sClassId = "classId";
    private String mClassName;
    private RegisteredRecordFromSignatureDto mSignatureDto;
    private boolean mIsModify;
    private String mSelectedDate;


    /**
     * @param context                          上下文
     * @param className4Title                  班级名称
     * @param isModify                         是否是修改，如果是registeredRecordFromSignatureDto不能为null，否则可以为null
     * @param registeredRecordFromSignatureDto 修改的数据
     * @param classId                          登记进入传入真实的classID，修改进入请给负值如：-1
     */
    public static void start(Context context, String className4Title, boolean isModify,
                             RegisteredRecordFromSignatureDto registeredRecordFromSignatureDto, int classId) {
        final Intent lIntent = new Intent(context, CourseDetailActivity.class);
        Bundle lBundle = new Bundle();
        lBundle.putString(sCLASSNAME, className4Title);
        lBundle.putBoolean(sIsModify, isModify);
        lBundle.putInt(sClassId, classId);
        lBundle.putSerializable(sModifyBean, registeredRecordFromSignatureDto);
        lIntent.putExtras(lBundle);
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_register_detail);
        initData();
        initView();
        loadData();
    }

    private void loadData() {
        /**
         * 获取领导审核日
         */
        presenter.getApproveDate();
    }

    //初始化view
    private void initView() {
        initSectionCourseTeacherDataIfModify();  //修改的数据初始化
        findViews();
        initToolbar();
        setOnInteractListener();

        mOptionsPickerView.setOnoptionsSelectListener((options1, option2, options3) -> {
            mSelectedDate = mDateRange.get(options1);
            mCourseDateTv.setText(mSelectedDate);
            DateTime lDateTime = presenter.getDateTimesRange().get(options1);
            String lSignDate = lDateTime.toString("yyyy-MM-dd");
            ClassDataManager.getDataCache().setSignDate(lSignDate);
            mSignDate = lSignDate;
            //当选择还是同一个日期的时候，数据不要改变
            if(mDateString.equals(lSignDate)){
                //TODO 去询问当前选择日期是否全部被登记了- new
                presenter.getIsHaveSectionHaveBeenSign(null,mSignDate,mClassId);
                return;
            }
            mDateString = lSignDate;  //选择不是同一个日期的时候，去拉取该日期下的节次

//            ClassDataManager.getDataCache().setSection(null);  //选择日期后，更新节次信息
            /*if(mIsModify){
                //修改还要判断哪些节次被别的班级登记了
                presenter.getTimeAndSection(mUserBean.getUserId(), mClassId, mSignDate,true);
            }*/
            //节次为空的时候可以不处理，维持空,此时要还原没有节次可选的选项
            if( mSectionBeenList.size() ==0){
                notCanSignSection = false;
                //TODO 去询问当前选择日期是否全部被登记了- new
                presenter.getIsHaveSectionHaveBeenSign(null,mSignDate,mClassId);
                return;
            }
            //修改进入，且转换的和修改进入前一模一样的时候，直接可以保留
            if( mIsModify && mModifyFirstTimeEnterDate.equals(lSignDate) && mModifyFirstTimeEnterSection == mSectionBeenList.get(0).getSectionId()){
//                Log.e("lenita","mIsModify && mModifyFirstTimeEnterDate.equals(lSignDate) && mModifyFirstTimeEnterSection == mSectionBeenList.get(0).getSectionId()");
                return;
            }
            //其他情况，都去判断换日期后当前选择的节次是否有已登记的
            isClickNextStep = false;
            presenter.getIsHaveSectionHaveBeenSign(mSectionBeenList,mSignDate,mClassId);
        });
    }

    private void initSectionCourseTeacherDataIfModify() {
        /**
         * 从修改进入-先初始化相关的值
         */
        if (mIsModify) {
//            mSectionTv.setText(presenter.getSectionName(mSignatureDto.getSectionID()));
//            mCourseNameEt.setText(mCourseName);
            //拿到需要修改的初始值
            mDataBean.setOriginalClassId(mSignatureDto.getClassID());
            mDataBean.setOriginalSectionId(mSignatureDto.getSection().getSectionOriginalId());
            mDataBean.setOriginalSignDate(mSignatureDto.getSignDate());
            mDataBean.setMemo(mSignatureDto.getRemark());  //登记原始备注
            mDataBean.setScoreString(""+mSignatureDto.getScore()); //登记原始分数
            mSignDate = mSignatureDto.getSignDate();  //登记日期
            //修改进入的节次、老师、课程信息记得取修改前的
//            mClassId = mSignatureDto.getClassID();
//            mCourseName = mSignatureDto.getCourseName();
            //记录下当前的节次信息
            RegisterDetailPresenter lDetailPresenter = new RegisterDetailPresenter();
            mSectionBeanSet.addAll(lDetailPresenter.getModifySection(mSignatureDto));
            mSectionBeenList = new ArrayList<>(presenter.sortSetSection(mSectionBeanSet));
            mFirstSection = mSectionBeenList.get(0).getSectionId();
            mFirstSectionName = mSectionBeenList.get(0).getLesson();
            //记录下当前课程
            List<DetailItemShowBean.TeacherInfoBean> teacherInfoBeanList = mSignatureDto.getMultipleTeachersList();
            mTeacherInfoBeanSet = new HashSet<>(teacherInfoBeanList);
            //记录下当前教师
            List<DetailItemShowBean.CourseInfoBean> courseInfoBeanList = mSignatureDto.getMultipleCoursesList();
            mCourseInfoBeanSet = new HashSet<>(courseInfoBeanList);
        }
    }


    private void initToolbar() {
        mToolBarManager.init();
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
        String shortClassName = mClassName;
        if(mClassName.length() > 11){
            shortClassName = mClassName.substring(0,9)+"...";
        }
        if(mIsModify){
            mToolBarManager.getRightButton().setEnabled(true);
        }else {
            mToolBarManager.getRightButton().setEnabled(false);
        }
        mToolBarManager.setTitle(shortClassName)
                .showRightButton(true)
                .setRightText(getString(R.string.next_step))
                .clickRightButton(v -> {
//                    SysRes.showSoftInputOrNot(mCourseNameEt, false);
                    //把班级名字写入缓存中 -- 废弃
//                    ClassDataManager.getDataCache().setClassName(mCourseNameEt.getText().toString().trim());
//                    ClassDataManager.getDataCache().setClassName("课程名称");
                    //把课程写入
//                    mCourseName = mCourseNameEt.getText().toString().trim();
//                    mDataBean.setCourseName(mCourseName);

                    mDataBean.setTitle(mClassName);
                    mDataBean.setSignDate(mSignDate);
                    if(mIsModify && mSignatureDto.getSection().getSectionOriginalId() != mSectionBeenList.get(0).getSectionOriginalId()){
                        // 如果是修改进入的，且修改了节次的，需要记录下新的节次名字用于显示标题
                        mDataBean.setSectionBeanNewSection(mSectionBeenList.get(0));
                        SectionBean sectionBean = new SectionBean();
                        sectionBean.setSectionId(mSignatureDto.getSection().getSectionId());
                        sectionBean.setSectionOriginalId(mSignatureDto.getSection().getSectionOriginalId());
                        sectionBean.setLesson(mSignatureDto.getSection().getLesson());
                        List<SectionBean> sectionBeanList = new ArrayList<>();
                        sectionBeanList.add(sectionBean);
                        mDataBean.setSectionBeen(sectionBeanList);
                    }else {
                        //修改的节次不变，这里也就不变，修改是单节次修改
                        mDataBean.setSectionBeanNewSection(mSectionBeenList.get(0));
                        mDataBean.setSectionBeen(mSectionBeenList);  //节次信息
                    }

                    mDataBean.setTeacherInfoBeanList(mTeacherInfoBeanSet);  //老师信息
                    mDataBean.setCourseInfoBeanList(mCourseInfoBeanSet);  //课程
                    if(mIsModify){
                        CourseSignWithAttendanceStatusActivity.start(CourseDetailActivity.this, mIsModify,mDataBean);
                    }else {
                        // 新增登记在跳转前要先去判断选择的节次是不是有被别的地方抢先登记了
                        isClickNextStep = true;
                        presenter.getIsHaveSectionHaveBeenSign(mSectionBeenList,mSignDate,mClassId);
                    }

                });
    }

    //事件处理
    private void setOnInteractListener() {
//        mCourseNameEt.addTextChangedListener(lWatcher);
//        mSectionTv.addTextChangedListener(lWatcher);  //废弃

        /**
         * 签名确认后把除首页外的Activity的finish掉
         */
        presenter.mCompositeSubscription.add(RxBus.getDefault().toObserverable(Constant.finishBean.class)
        .subscribe(new Action1<Constant.finishBean>() {
            @Override
            public void call(Constant.finishBean finishBean) {
                finish();
            }
        }));

         //TODO 点击整行弹出选择项,收起软件盘
        timeSelectLayout.setOnClickListener(v -> {
            hideSoftKeyboard();
            if (!unavailableSignDate) {
                mOptionsPickerView.show();
            }
        });

        /**
         * 用于提交登记时提示节次被登记后的返回刷新 SignPresenter 72行
         */
        presenter.mCompositeSubscription.add(RxBus.getDefault().toObserverable(SectionBean.class)
                .subscribe(sectionBean -> {
                    //当提交登记时弹出所有都被登记过，从登记页面回调到这页面进行数据刷新
                    if(!isSectionHaveBeenSign){
                        isSectionHaveBeenSign = true;
                        loadData();
                    }
                }, throwable -> {

                }));

        /**
         * 用于点击返回重新判断是否要刷新界面 SectionChooseActivity-257行
         */
        presenter.mCompositeSubscription.add(RxBus.getDefault().toObserverable(RxSectionBeanSignList.class).subscribe(new Subscriber<RxSectionBeanSignList>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(RxSectionBeanSignList rxSectionBeanSignList) {
               // 转换成所需列表
                mSectionBeenList = new ArrayList<>(rxSectionBeanSignList.getSectionBeanList());
                for(int i = 0;i<mSectionBeenList.size();i++){
                    mSectionBeenList.get(i).setHaveBeenSignFlag(false);
                }
                mSectionBeanSet = new HashSet<>(mSectionBeenList);
                callbackToSettingSectionInfo();
            }
        }));

        /**
         * 点击确定 MultiselectTeacherActivity
         */
        presenter.mCompositeSubscription.add(RxBus.getDefault().toObserverable(RxCountTeacherNumBean.class).subscribe(new Subscriber<RxCountTeacherNumBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(RxCountTeacherNumBean rxCountTeacherNumBean) {
                //TODO 点击了确定返回的时候才进入这里
                if(rxCountTeacherNumBean.getClickSureButton()){
                    mTeacherInfoBeanSet = rxCountTeacherNumBean.getTeacherInfoBeanSet();
                    showTeacherView();
                }
            }
        }));
       /* sectionSelectLayout.setOnClickListener(v -> {
            hideSoftKeyboard();
            if(unavailableSignDate){
                return;  //如果没有可登记的日期无法选择上课节次，直接不跳页
            }
            if (mIsModify) {
//                    ArrayList<SectionBean> lSectionBeen = new ArrayList<>(mSectionBeanSet);
                SectionChooseActivity.startActivity4Result(CourseDetailActivity.this, true, mSignatureDto.getSectionID(), mRequestCode, mClassId, mSignDate, mSectionBeanSet);
            } else {
                SectionChooseActivity.startActivity4Result(CourseDetailActivity.this, false, -1, mRequestCode, mClassId, mSignDate, mSectionBeanSet);
            }
        });*/

    }

    /*TextWatcher lWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            boolean lEmpty = s.toString().isEmpty();
//                boolean lEquals = mCourseNameEt.getText().toString().trim().isEmpty();
//                boolean lEquals1 = mSectionTv.getText().toString().equals(CourseDetailActivity.this.getString(R.string.no_unsign_section));
            mToolBarManager.getRightButton().setEnabled(lEmpty);//判断是不是空
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };*/

    /**
     * 判断标题栏右边的按钮要不要亮起来
     */
    private void setTitleRightButtonEnable(){
        boolean sectionEmpty = (mSectionBeanSet.size() > 0);
        boolean courseEmpty = (mCourseInfoBeanSet.size() > 0);
        boolean teacherEmpty = (mTeacherInfoBeanSet.size() > 0);
        boolean status = sectionEmpty && courseEmpty && teacherEmpty;
        mToolBarManager.getRightButton().setEnabled(status);
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

    //绑定控件
    private void findViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolBarManager = new ToolBarManager(mToolbar, this);
        mCourseDateTv = (TextView) findViewById(R.id.course_date_tv);
        mClassTv = (TextView)findViewById(R.id.course_class_tv);
        mOptionsPickerView = new OptionsPickerView(this);
        timeSelectLayout = (RelativeLayout) findViewById(R.id.layout_time_select);
//        mSectionTv = (TextView) findViewById(R.id.section_tv);
//        mCourseNameEt = (DisableEmojiEditText) findViewById(R.id.course_name_et);
//        mCourseNameEt.setMaxLine(3);
//        mCourseNameEt.setMaxLength(20);

        //设置登记日期-修改的时候
        if(mIsModify){
            mSignFormatDay = presenter.getSignFormatDay(mSignDate);
            mCourseDateTv.setText(mSignFormatDay);
        }
        //设置班级名称
        mClassTv.setText(mClassName);
        //设置节次-初始化
        sectionRecyclerView = (RecyclerView)findViewById(R.id.rv_section) ;
//        LinearLayoutManager sectionLinearLayoutManager = new LinearLayoutManager(this);
        GridLayoutManager sectionGridLayoutManager  = new GridLayoutManager(this,2);
        sectionGridLayoutManager .setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                //符合单数节次数，且正好是最后一个节次的显示位置，让最后一个节次占满一行
                boolean isNeedToCrossLine = mSectionBeanSet.size() % 2==1 && position == mSectionBeanSet.size()-1;
                if(mSectionBeanSet.size() == position){  //footer 都是右对齐且占一行
                    isNeedToCrossLine = true;
                }
                return  isNeedToCrossLine ? 2: 1;  //需要跨行的占两个位置，不需要跨行的占一个位置
            }
        });
        sectionRecyclerView.setLayoutManager(sectionGridLayoutManager);
        footerSection = LayoutInflater.from(this).inflate(R.layout.list_item_add_footer,sectionRecyclerView,false);
        showSectionView();
        //设置课程-初始化
        courseRecyclerView = (RecyclerView)findViewById(R.id.rv_course);
//        LinearLayoutManager courseLinearLayoutManager = new LinearLayoutManager(this);
        GridLayoutManager courseGridLayoutManager  = new GridLayoutManager(this,2);
        courseGridLayoutManager .setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                //符合单数节次数，且正好是最后一个节次的显示位置，让最后一个节次占满一行
                boolean isNeedToCrossLine = mCourseInfoBeanSet.size() % 2==1 && position == mCourseInfoBeanSet.size()-1;
                if(mCourseInfoBeanSet.size() == position){  //footer 都是右对齐且占一行
                    isNeedToCrossLine = true;
                }
                return  isNeedToCrossLine ? 2: 1;  // 需要跨行的占两个位置，不需要跨行的占一个位置
            }
        });
        courseRecyclerView.setLayoutManager(courseGridLayoutManager);
        footerCourse = LayoutInflater.from(this).inflate(R.layout.list_item_add_footer,courseRecyclerView,false);
        showCourseView();
        //设置教师-初始化
        teacherRecyclerView = (RecyclerView)findViewById(R.id.rv_teacher);
//        LinearLayoutManager teacherLinearLayoutManager = new LinearLayoutManager(this);
        GridLayoutManager teacherGridLayoutManager  = new GridLayoutManager(this,2);
        teacherGridLayoutManager .setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                //符合单数节次数，且正好是最后一个节次的显示位置，让最后一个节次占满一行
                boolean isNeedToCrossLine = mTeacherInfoBeanSet.size() % 2==1 && position == mTeacherInfoBeanSet.size()-1;
                if(mTeacherInfoBeanSet.size() == position){  //footer 都是右对齐且占一行
                    isNeedToCrossLine = true;
                }
                return  isNeedToCrossLine ? 2: 1;  // 需要跨行的占两个位置，不需要跨行的占一个位置
            }
        });
        teacherRecyclerView.setLayoutManager(teacherGridLayoutManager);
        footerTeacher = LayoutInflater.from(this).inflate(R.layout.list_item_add_footer,teacherRecyclerView,false);
        showTeacherView();
    }

    private void setDefaultSectionData(){
        //获取真实的课表默认老师和课程
        if(mSectionBeenList.size() > 0){
            presenter.getClassSectionDefaultTeachersAndCoures(mDateString,mClassId,mFirstSection,mUserId);
        }
    }

    DetailItemSelectAdapter.Listener listener = new DetailItemSelectAdapter.Listener() {
        @Override
        public void addClickListener(int whichAdd) {
            switch (whichAdd){
                case DetailItemSelectAdapter.SESSION_BEAN_FLAG:
                    hideSoftKeyboard();
                    //无可登记日期-点击添加节次
                    if(unavailableSignDate ||  mCourseDateTv.getText().toString().trim().equals("")){
                        ToastUtils.showShortToast(getResources().getString(R.string.not_register_date));
                        return;  //如果没有可登记的日期无法选择上课节次，直接不跳页
                    }
                    if(notCanSignSection){
                        ToastUtils.showShortToast(getResources().getString(R.string.not_register_section));
                        return;  //如果没有可登记的日期无法选择上课节次，直接不跳页
                    }
                    if (mIsModify) {
                       //TODO 修改进入要注意当节次是空的时候的显示
                        boolean isSameWithFirstEnterModifyDate = (mModifyFirstTimeEnterDate.equals(mSignDate));
                        SectionChooseActivity.startActivity4Result(CourseDetailActivity.this, true, mModifyFirstTimeEnterSection, mRequestCode, mClassId, mSignDate, mSectionBeanSet,isSameWithFirstEnterModifyDate);
                    } else {
                        SectionChooseActivity.startActivity4Result(CourseDetailActivity.this, false, -1, mRequestCode, mClassId, mSignDate, mSectionBeanSet,false);
                    }
                    break;
                case DetailItemSelectAdapter.COURSE_BEAN_FLAG:
                    hideSoftKeyboard();
                    //无可登记日期、无可登记节次或没有获取到数据
                    if(unavailableSignDate || mCourseDateTv.getText().toString().trim().equals("")){
                        ToastUtils.showShortToast(getResources().getString(R.string.not_register_date));
                        return;
                    }
                    if(notCanSignSection){
                        ToastUtils.showShortToast(getResources().getString(R.string.not_register_section));
                        return;  //如果没有可登记的日期无法选择上课节次，直接不跳页
                    }
                    startActivityCourse();
                    break;
                case DetailItemSelectAdapter.TEACHER_BEAN_FLAG:
                    hideSoftKeyboard();
                    //无可登记日、无可登记节次或没有获取到数据
                    if(unavailableSignDate ||  mCourseDateTv.getText().toString().trim().equals("")){
                        ToastUtils.showShortToast(getResources().getString(R.string.not_register_date));
                        return;
                    }
                    if(notCanSignSection){
                        ToastUtils.showShortToast(getResources().getString(R.string.not_register_section));
                        return;  //如果没有可登记的日期无法选择上课节次，直接不跳页
                    }
                    startActivityTeacher();
                    break;
                default:
                    break;
            }
        }

        @Override
        public void deleteClickListener(int whichDelete, Object object) {
            if(whichDelete == DetailItemSelectAdapter.COURSE_BEAN_FLAG){
                DetailItemShowBean.CourseInfoBean courseInfoBean = (DetailItemShowBean.CourseInfoBean) object;
                mCourseInfoBeanSet.remove(courseInfoBean);
            }else if(whichDelete == DetailItemSelectAdapter.TEACHER_BEAN_FLAG){
                DetailItemShowBean.TeacherInfoBean teacherInfoBean = (DetailItemShowBean.TeacherInfoBean) object;
                mTeacherInfoBeanSet.remove(teacherInfoBean);
            }else if(whichDelete == DetailItemSelectAdapter.SESSION_BEAN_FLAG){
                //需要改界面,更新节次相关的集合
                SectionBean sectionBean = (SectionBean)object;
                mSectionBeanSet.remove(sectionBean);
                mSectionBeenList.remove(sectionBean);
                // 这里的判断要注意，删除的如果是已登记的节次，就不需要去获取课表的老师和课程
//                Log.e("lenita","section deleteClickListener");
                if(mSectionBeenList.size() > 0 && !mSectionBeenList.get(0).getHaveBeenSignFlag()){
                    if(mFirstSection != mSectionBeenList.get(0).getSectionId()){
                        mFirstSection = mSectionBeenList.get(0).getSectionId();
                        mFirstSectionName = mSectionBeenList.get(0).getLesson();
                        setDefaultSectionData();  //有改变的需要重新去获取课表和教师
                    }
                }
                //TODO 无论什么情况，只要删除一个节次都去查询当天是否已经没有可以登记的节次了
                presenter.getIsHaveSectionHaveBeenSign(null,mSignDate,mClassId);
            }
            //更新标题栏右边按钮状态
            setTitleRightButtonEnable();
        }
    };

    public final static int mResultCodeSection = 100;
    public final static int RESULT_CODE_COURSE = 101;
    public final static int RESULT_CODE_TEACHER = 102;
    private void startActivityTeacher(){
        Intent lIntent = new Intent(CourseDetailActivity.this, MultiselectTeacherActivity.class);
        Bundle lBundle = new Bundle();
        lBundle.putString(MultiselectTeacherActivity.DEP_ID, "-1");
        lBundle.putBoolean(MultiselectTeacherActivity.MAIN_PAGE_FLAG,true);
        lBundle.putSerializable(MultiselectTeacherActivity.TEACHER_INFO_BEAN, (Serializable) mTeacherInfoBeanSet);
        lBundle.putSerializable(MultiselectTeacherActivity.CHOOSE_TEACHER_LIST, null);
        lBundle.putSerializable(MultiselectTeacherActivity.TEACHER_COUNT_NUM_INFO,null);
        lBundle.putInt(MultiselectTeacherActivity.PAGE_FLAG,0);
        lIntent.putExtras(lBundle);
        startActivityForResult(lIntent,mRequestCode);
    }

    private void startActivityCourse(){
        Intent lIntent = new Intent(CourseDetailActivity.this, MultiselectCourseActivity.class);
        Bundle lBundle = new Bundle();
        lBundle.putString (WHICH_PAGE,PAGE_COURSE_DAILY);
        lBundle.putString(MultiselectCourseActivity.TEACHER_ID,mUserId);
        lBundle.putString(MultiselectCourseActivity.SELECT_DATE,mDateString);
        lBundle.putSerializable(MultiselectCourseActivity.COURSE_INFO_BEAN, (Serializable) mCourseInfoBeanSet);
        lIntent.putExtras(lBundle);
        startActivityForResult(lIntent,mRequestCode);
    }

    //用于页面间数据接收
    private void initData() {
//        presenter.attachView(this);
        mClassName = getIntent().getExtras().getString(sCLASSNAME);
        mSignatureDto = (RegisteredRecordFromSignatureDto) getIntent().getExtras().getSerializable(RegisterCourseActivity.sModifyBean);
        if(mSignatureDto != null){
            mDateString = mSignatureDto.getSignDate();
        }
        mIsModify = getIntent().getExtras().getBoolean(RegisterCourseActivity.sIsModify);
        mClassId = getIntent().getIntExtra(sClassId, -1);
        try {
            mUserBean = AppConfiguration.getInstance().getUserBean();
            mUserId = mUserBean.getUserId();
            mUserName = mUserBean.getEchoName();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //初始化要传递的数据
        mDataBean = new DataBean();
        mDataBean.setMotify(mIsModify);
        //修改进入且不为空的时候，要初始化一些值
        if(mIsModify && mSignatureDto != null){
            mClassId = mSignatureDto.getClassID();
            mModifyFirstTimeEnterDate = mSignatureDto.getSignDate();
            mModifyFirstTimeEnterSection = mSignatureDto.getSection().getSectionId();
//            Log.e("lenita","mModifyFirstTimeEnterDate = "+mModifyFirstTimeEnterDate+",mModifyFirstTimeEnterSection = "+mModifyFirstTimeEnterSection);
        }
        mDataBean.setClassId(mClassId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == mRequestCode){
            switch (resultCode){
                case mResultCodeSection:
                    Set<SectionBean> mSectionSet = (Set<SectionBean>) data.getExtras().getSerializable(SectionChooseActivity.sSectionSet);
                    mSectionBeanSet = mSectionSet;
                    mSectionBeenList = new ArrayList<SectionBean>(mSectionBeanSet);
                    callbackToSettingSectionInfo();
                    break;
                case RESULT_CODE_COURSE:
                    //改变课程列表
                    Set<DetailItemShowBean.CourseInfoBean> courseInfoBeanSet = (Set<DetailItemShowBean.CourseInfoBean>)data.getExtras().getSerializable(COURSE_INFO_BEAN) ;
                    if(courseInfoBeanSet != null)
                        mCourseInfoBeanSet = courseInfoBeanSet;
                    showCourseView();
                    break;
                case RESULT_CODE_TEACHER:
                    //改变教师列表
                    Set<DetailItemShowBean.TeacherInfoBean> teacherInfoBeenSet = (Set<DetailItemShowBean.TeacherInfoBean>)data.getExtras().getSerializable(TEACHER_INFO_BEAN) ;
                    if(teacherInfoBeenSet != null)
                        mTeacherInfoBeanSet = teacherInfoBeenSet;
                    showTeacherView();
                    break;
            }
            //更新标题栏右边按钮状态
            setTitleRightButtonEnable();
        }
    }

    private void callbackToSettingSectionInfo(){
        /*将list有序排列*/
        Collections.sort(mSectionBeenList, new Comparator<SectionBean>() {
            @Override
            public int compare(SectionBean o1, SectionBean o2) {
                return o1.getSectionId()-o2.getSectionId();
            }
        });
        showSectionView();
        if (mSectionBeanSet != null && mSectionBeanSet.size() > 0) { //转换成列表，如2,3节就拿第2节的课表
            if(mFirstSection != mSectionBeenList.get(0).getSectionId()){
                mFirstSection = mSectionBeenList.get(0).getSectionId();
                mFirstSectionName = mSectionBeenList.get(0).getLesson();
                //选了新的节次的情况，我们去重新获取数据库，询问是否要同步课表的节次信息
                setDefaultSectionData();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        presenter.detachView(false);
        mToolBarManager.destroy();
        ClassDataManager.destroyDataCache();
//        showSoftInputOrNot(mCourseNameEt, false);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize(); //如果被调用说明activity已经被回收，否则泄漏了
        Log.d(TAG, "====LeakActivity has been recycled!");
    }
}