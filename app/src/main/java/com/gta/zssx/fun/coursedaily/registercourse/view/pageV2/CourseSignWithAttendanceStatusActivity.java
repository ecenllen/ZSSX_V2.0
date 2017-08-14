package com.gta.zssx.fun.coursedaily.registercourse.view.pageV2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.model.ClassDataManager;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.DataBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.DetailItemShowBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.RxSectionBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.RxStudentList;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SectionAtendentStatusListBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SectionBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SectionStudentListBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.StudentListBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.StudentListNewBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SubmitSignInfoBean;
import com.gta.zssx.fun.coursedaily.registercourse.presenter.SignPresenter;
import com.gta.zssx.fun.coursedaily.registercourse.view.SignView;
import com.gta.zssx.fun.coursedaily.registercourse.view.adapter.CommonFragmentStatePagerAdapter;
import com.gta.zssx.fun.coursedaily.registercourse.view.adapter.ui.IsNormalSubmitCanSignPartDataDialog;
import com.gta.zssx.fun.coursedaily.registercourse.view.adapter.ui.SureClickDialog;
import com.gta.zssx.fun.coursedaily.registercourse.view.base.Constant;
import com.gta.zssx.fun.coursedaily.registercourse.view.base.RegisterCourseActivity;
import com.gta.zssx.fun.coursedaily.registercourse.view.page.StudentListInnerFragmentV2;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.ClassInfoDto;
import com.gta.zssx.fun.coursedaily.registerrecord.view.page.AlreadyRegisteredRecordActivity;
import com.gta.zssx.fun.coursedaily.registerrecord.view.page.AlreadyRegisteredRecordFromSignatureFragment;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.pub.base.BaseActivity;
import com.gta.zssx.pub.manager.ToolBarManager;
import com.gta.zssx.pub.util.RxBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by lan.zheng on 2017/2/28.
 * new 学生考勤登记签名确认
 */
public class CourseSignWithAttendanceStatusActivity extends BaseActivity<SignView, SignPresenter> implements SignView{
    //学生列表
    public static String sDateBean = "mDateBean";
    public static String sIsModify = "isModify";
    public DataBean mDataBean;
    public SectionAtendentStatusListBean mSectionAtendentStatusListBean; //所有节次的考勤 - 新 - 登记数据相关 - 任何回调都需要更新这个总表
    List<SectionStudentListBean> mSectionStudentListBeanList = new ArrayList<>();  //所有节次的学生列表,接口8获取到的，传入Fragment显示界面
    //标题栏
    public Toolbar mToolbar;
    public ToolBarManager mToolBarManager;
    public TabLayout mTabLayout;
    private RelativeLayout noDataTv;
    public ViewPager mViewPager;
    private ArrayList<String> mTitleList;
    private ArrayList<Fragment> mViewList;
//    public RxSectionBean mRxSectionStudentBean;  //单个节次的考勤 - 废弃

    public String mTitleClassName;
    private boolean mIsModify;
//    private boolean isCanClickBackButton = true;  //用于弹框的时候，判断给不给点击返回让弹框消失
    public  TextView mSignDateTv; //登记日期
    public TextView mCourseNameTv;  //显示课程名称
    public TextView mCourseTeacherTv;  //显示授课老师
    public LinearLayout mSignInfoLayout;  //登记信息
    public String sectionTrueIdString;//要请求的真实节次IdString
    public int mClassId; //班级Id
    public String mSignDate;  //登记日期
    //节次 - 登记数据相关
    public List<SectionBean> mSectionBeanList;
    //教师列表 - 登记数据相关
    public List<DetailItemShowBean.TeacherInfoBean> mTeacherInfoBeanList;
    //课程列表 - 登记数据相关
    public List<DetailItemShowBean.CourseInfoBean> mCourseInfoBeanList;

    //提交登记所需数据封装
    public SubmitSignInfoBean mSubmitSignInfoBean;
    private UserBean mUserBean;
    private String courseText;
    private String teacherText;

    @NonNull
    @Override
    public SignPresenter createPresenter() {
        return new SignPresenter(this);
    }

    @Override
    public void showResult(String s) {
        finish(); //提交登记成功回调,finish()这个页面
        ClassInfoDto lClassInfoDto = new ClassInfoDto();
        lClassInfoDto.setClassName(mTitleClassName);
        lClassInfoDto.setClassID(""+ mClassId);
        lClassInfoDto.setSignDate(mSignDate);
        lClassInfoDto.setTeacherID(mUserBean.getUserId());
        lClassInfoDto.setIsFromClassLogMainpage(false);
        /**
         * 签名确认后把除首页外的Activity的finish掉
         * CourseDetailActivity -- 650行
         * AlreadyRegisteredRecordActivity -- 176行
         */
        RxBus.getDefault().post(new Constant.finishBean());
        finish();
//        CourseDailyActivity.start(this); //登记成功的，不需要再显示登记流程，按返回键直接是回到登记的主页

        AlreadyRegisteredRecordActivity.start(this, AlreadyRegisteredRecordFromSignatureFragment.PAGE_TAG, lClassInfoDto);
        ClassDataManager.destroyDataCache();
    }

    @Override
    public void showHaveBeenSignMassage(String s) {
        /*String[] split = s.split(",");
        String msg = "第";
        for(int i = 0;i<split.length;i++){
            for(int j = 0;j<mSectionBeanList.size();j++){
                int Oid = mSectionBeanList.get(j).getSectionOriginalId();
                int id = mSectionBeanList.get(j).getSectionId();
                if(split[i].equals(String.valueOf(Oid))){
                    if(i == 0 ){
                        msg += ""+id;
                    }else {
                        msg += "，"+id;
                    }
                }
            }
        }
        msg += "节已经被登记过，请重新选择要等级的节次";*/
        ToastUtils.showLongToast(s);
    }

    @Override
    public void emptyUI() {
        mTabLayout.setVisibility(View.GONE);
        noDataTv.setVisibility(View.VISIBLE);
        mToolBarManager.getRightButton().setEnabled(false);
        mSignInfoLayout.setVisibility(View.GONE);
    }

    @Override
    public void isHaveSectionHaveBeenSign(int unSignNum) {
        if(unSignNum == 0){
            //签名确认，当前选择的节次全都不可登记
//            RxBus.getDefault().post(new SectionBean());
//            finish();
            showAllHaveBeenSignDialog();
        }else if(unSignNum == mSectionBeanList.size()){
            //全部都是可以登记的
            presenter.postSignData(mSubmitSignInfoBean,null);
//            Log.e("lenita","CourseSignW..Activity  签名确认正常登记");
        }else {
//            Log.e("lenita","CourseSignW..Activity  签名确认部分已登记");
            showSubmitInfoDialog();
        }
    }

    private SureClickDialog mAllHaveBeenSignDialog;
    private void showAllHaveBeenSignDialog(){
        if(mAllHaveBeenSignDialog != null)
            mAllHaveBeenSignDialog = null;
//        backgroundAlpha(0.8f);
        String content = getResources().getString(R.string.text_contain_all_have_been_sign_section);
        mAllHaveBeenSignDialog = new SureClickDialog(this,content ,false, new SureClickDialog.Listener() {
            @Override
            public void onDialogDismissListener() {
//                backgroundAlpha(1f);
            }

            @Override
            public void onSureListerner() {
                /**
                 * 回调到CourseDetailActivity - 507行
                 */
                RxBus.getDefault().post(new SectionBean());
                finish();
            }
        });
        mAllHaveBeenSignDialog.setCanceledOnTouchOutside(false);
        mAllHaveBeenSignDialog.setCancelable(false);
        mAllHaveBeenSignDialog.show();
    }


    /**
     * 签名确认，当前选择的节次有部分不可登记，弹出提示询问是否直接提交可登记的部分节次
     */
    private IsNormalSubmitCanSignPartDataDialog mIsNormalSubmitCanSignPartDataDialog;
    private void showSubmitInfoDialog(){
        presenter.getStatusSignList(mSectionBeanList);  //先获取到节次登记状态
        if(mIsNormalSubmitCanSignPartDataDialog != null)
            mIsNormalSubmitCanSignPartDataDialog = null;
//        backgroundAlpha(0.8f);
        List<String> stringList = new ArrayList<>();
        for(int i = 0;i<mSectionBeanList.size();i++){
            if(mSectionBeanList.get(i).getHaveBeenSignFlag()){
                stringList.add(""+mSectionBeanList.get(i).getSectionId());
            }
        }
        mIsNormalSubmitCanSignPartDataDialog = new IsNormalSubmitCanSignPartDataDialog(this, stringList , new IsNormalSubmitCanSignPartDataDialog.Listener() {
            @Override
            public void onDialogDismissListener() {
//                backgroundAlpha(1f);
            }

            @Override
            public void onSureListerner() {
//                Log.e("lenita","CourseSignW..Activity  签名确认删除部分已登记节次后正常登记");
                presenter.postSignData(mSubmitSignInfoBean,mSectionBeanList);
            }
        });
        mIsNormalSubmitCanSignPartDataDialog.setCanceledOnTouchOutside(false);
        mIsNormalSubmitCanSignPartDataDialog.show();
    }

    @Override
    public void getStudentListInfo(List<SectionStudentListBean> sectionStudentListBeanList) {
        mTabLayout.setVisibility(View.VISIBLE);
        mSignInfoLayout.setVisibility(View.VISIBLE);
        //设置sectionStudentListBeanList的sectionId
        for(int i = 0;i<sectionStudentListBeanList.size();i++){
            sectionStudentListBeanList.get(i).setSectionId(mSectionBeanList.get(i).getSectionId());
        }
        mSectionStudentListBeanList = sectionStudentListBeanList;  //真实数据
//       mSectionStudentListBeanList = testData(mSectionBeanList); //测试结构改变后获取到的学生列表
        if(mIsModify){
            //如果是修改进入，拿到原来的分数和备注信息，并同样做好初始化
            String score =  mDataBean.getScoreString();
            String remark = mDataBean.getMemo();
            mSectionAtendentStatusListBean = presenter.countAttendanceNumModify(score,remark,sectionStudentListBeanList);
//            Log.e("lenita","CourseS...Activity 要修改的分数 score = "+mSectionAtendentStatusListBean.getSectionBeanList().get(0).getScoreString()+",remark = "+mSectionAtendentStatusListBean.getSectionBeanList().get(0).getRemark());
        }else {
            //新登记进入的，先把所有选择的节次的考勤状态、学生列表和OriginalId、SectionId和同步开关状态都初始化
            mSectionAtendentStatusListBean = presenter.setOriginalSectionStatus(sectionStudentListBeanList);
        }
        //最后判断右边是否需要显示可“签名确认”
        boolean isCanSign = presenter.isCanSign(mSectionAtendentStatusListBean);
        mToolBarManager.getRightButton().setEnabled(isCanSign);
        //进行fragment的初始化
        showList();
    }

    public static String sStudent_list = "student_list";  //旧,废弃
    public static String sSection_student_list = "section_student_list";  //新
    public static String sSection = "section";
    public static String sPosition = "position";
    private void showList() {
        mTitleList = new ArrayList<>();
        mViewList = new ArrayList<>();

        //旧的数据
//        List<StudentListNewBean> lStudentListBeen = presenter.setStudentDefaultState(studentListBeen, mSection);
//        rxStudentListStudentListBeen = lStudentListBeen;
        if (mSectionBeanList != null) {
            setFragmentList();  //设置fragment
        }
        assert mSectionBeanList != null;
        if (mSectionBeanList.size() > 4) {
            mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        } else {
            mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        }
        for (int i = 0; i < mTitleList.size(); i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(i)));
        }

        CommonFragmentStatePagerAdapter adapter = new CommonFragmentStatePagerAdapter(this.getSupportFragmentManager(), mViewList,mTitleList);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(mSectionBeanList.size());//此句必要，不然Fragment回收会导致数据复用有问题
        mTabLayout.setupWithViewPager(mViewPager);
    }

    public void setFragmentList(){
        for (int i = 0; i < mSectionBeanList.size(); i++) {
            if(mIsModify){
                // 修改进入的标题变为新选择的节次名字
                mTitleList.add(mDataBean.getSectionBeanNewSection().getLesson());
            }else {
                mTitleList.add(mSectionBeanList.get(i).getLesson());
            }

            //Fragment所需要的数据
            Bundle lBundle = new Bundle();
            lBundle.putSerializable(sSection_student_list,(Serializable)mSectionStudentListBeanList);
//            lBundle.putSerializable(sStudent_list, (Serializable) rxStudentListStudentListBeen);
//            lStudentListInnerFragment.setArguments(lBundle);
            lBundle.putSerializable(sSection, mSectionBeanList.get(i));
            lBundle.putInt(sPosition, i);
            lBundle.putSerializable(sDateBean,mDataBean);
            StudentListInnerFragmentV2 lStudentListInnerFragmentV2 = new StudentListInnerFragmentV2();
            lStudentListInnerFragmentV2.setArguments(lBundle);
            lStudentListInnerFragmentV2.setChangeStatusListener(mListener);
            mViewList.add(lStudentListInnerFragmentV2);
        }
    }

    StudentListInnerFragmentV2.Listener mListener = new StudentListInnerFragmentV2.Listener() {
        @Override
        public void listener(int position, boolean changeStatus,SectionBean sectionBean,List<StudentListNewBean> studentListNewBeanList) {
            hideSoftKeyboard();
            //改变状态列表
//            Log.e("lenita","同步的position = "+position+", changeStatus = "+changeStatus);
            mSectionAtendentStatusListBean.getSameWithPreviousStatusList().set(position,changeStatus);
            if(!changeStatus){
                //开关关闭，先还原该位置的数据
                sectionBean.setStudentListBeen(studentListNewBeanList);
                mSectionAtendentStatusListBean.getSectionBeanList().set(position,sectionBean);
//                startPosition = position+1;
            }
            //当开关关闭，且改变是最后一条数据，不需要回调了,因为已经在内部页面进行了刷新数据操作
            if(position+1 == mSectionAtendentStatusListBean.getSectionBeanList().size() && !changeStatus){
                return;
            }
            //其他情况均需要回调，从有开关的开始依次刷新界面
            changeStatusSameWithPreviousThenUpdateRightButton(1);
        }
    };

    /**
     * 处理"和上一节相同"开关数据,最开始的第一个节次是不会存在和上一节相同的按钮的，因此都是从第二个节次开始判断要不要同步
     */
    private void changeStatusSameWithPreviousThenUpdateRightButton(int startPosition){
       //有打开开关的需要同步数据
        for(int i = startPosition;i < mSectionAtendentStatusListBean.getSameWithPreviousStatusList().size();i++){
            boolean isSameWithPrevious = mSectionAtendentStatusListBean.getSameWithPreviousStatusList().get(i);
            if(isSameWithPrevious){
                //要改变的位置
                int sectionId = mSectionAtendentStatusListBean.getSectionBeanList().get(i).getSectionId();
                int sectionOriginalId= mSectionAtendentStatusListBean.getSectionBeanList().get(i).getSectionOriginalId();
                // 和前一节相同是开启的，那就和前一节数据相同
                SectionBean sectionBeanPre  = mSectionAtendentStatusListBean.getSectionBeanList().get(i-1);
                String score = sectionBeanPre.getScoreString();
                String remark = sectionBeanPre.getRemark();
                int leave = sectionBeanPre.getLeaveCount();
                int late = sectionBeanPre.getDelayCount();
                int absent = sectionBeanPre.getAbsentCount();
                int vocation = sectionBeanPre.getVocationCount();
                List<StudentListNewBean> studentListNewBeanList = sectionBeanPre.getStudentListBeen();
//                Log.e("lenita","changeStatusSameWithPreviousThenUpdateRightButton sectionOriginalId = "+sectionOriginalId+"score = "+score);
                //封装成新的bean
                SectionBean sectionBeanNew = new SectionBean();
                sectionBeanNew.setSectionId(sectionId);
                sectionBeanNew.setSectionOriginalId(sectionOriginalId);
                sectionBeanNew.setLeaveCount(leave);
                sectionBeanNew.setDelayCount(late);
                sectionBeanNew.setAbsentCount(absent);
                sectionBeanNew.setVocationCount(vocation);
                sectionBeanNew.setScoreString(score);
                sectionBeanNew.setRemark(remark);
                sectionBeanNew.setStudentListBeen(studentListNewBeanList);
                mSectionAtendentStatusListBean.getSectionBeanList().set(i,sectionBeanNew);
            }
        }
        //重置右边按钮状态
        boolean isCanSign = presenter.isCanSign(mSectionAtendentStatusListBean);
        //只要有没有填写分数的都不给提交登记
        mToolBarManager.getRightButton().setEnabled(isCanSign);
        /**
         * 数据更新完成，通知fragment改变
         */
        RxBus.getDefault().post(mSectionAtendentStatusListBean);
    }

    /**
     * @param context  上下文
     * @param dataBean 登记用的数据
     */
    public static void start(Context context, boolean isModify,DataBean dataBean) {
        final Intent lIntent = new Intent(context, CourseSignWithAttendanceStatusActivity.class);
        Bundle lBundle = new Bundle();
        lBundle.putBoolean(RegisterCourseActivity.sIsModify, isModify);
//        lBundle.putString(sTITLE, title);
        lBundle.putSerializable(sDateBean, dataBean);
        lIntent.putExtras(lBundle);
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_sign_with_attendance_status);
        initData();
        initView();
        loadData();
    }

    //用于页面间数据接收
    private void initData() {
//        presenter.attachView(this);
//        mRxSectionStudentBean = new RxSectionBean();
//        mSection = ClassDataManager.getDataCache().getSection();
        mIsModify = getIntent().getBooleanExtra(sIsModify,false);
        mDataBean = (DataBean) getIntent().getSerializableExtra(sDateBean);
        if(mDataBean != null){
            mTitleClassName = mDataBean.getTitle();
            mClassId = mDataBean.getClassId();
            mSignDate = mDataBean.getSignDate();
        }
        try {
            mUserBean = AppConfiguration.getInstance().getUserBean();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //本页面需要记录的数据
        mSectionBeanList = new ArrayList<>();
        if(mDataBean.getSectionBeen() != null){
            mSectionBeanList = mDataBean.getSectionBeen();
        }
        mTeacherInfoBeanList = new ArrayList<>();
        if(mDataBean.getTeacherInfoBeanList() != null){
            List<DetailItemShowBean.TeacherInfoBean> teacherInfoBeanList = new ArrayList<>(mDataBean.getTeacherInfoBeanList());
            //由于提交需要的是Guid，因此需要把TeacherId转存入GUide，然后TeacherId转为""
            mTeacherInfoBeanList = presenter.formatTeacherList(teacherInfoBeanList);
        }
        mCourseInfoBeanList = new ArrayList<>();
        if(mDataBean.getCourseInfoBeanList() != null){
            List<DetailItemShowBean.CourseInfoBean> courseInfoBeanList = new ArrayList<>(mDataBean.getCourseInfoBeanList());
            mCourseInfoBeanList = courseInfoBeanList;
        }
        teacherText = presenter.getTeacherString(mTeacherInfoBeanList);
        courseText = presenter.getCourseString(mCourseInfoBeanList);
        mSectionAtendentStatusListBean = new SectionAtendentStatusListBean();
        submitDataInitPart();  //先初始化一部分公共提交用的数据
    }

    private void submitDataInitPart(){
        mSubmitSignInfoBean = new SubmitSignInfoBean();
        mSubmitSignInfoBean.setClassID(mClassId);
        mSubmitSignInfoBean.setSignDate(mSignDate);
        mSubmitSignInfoBean.setTeacherID(mUserBean.getUserId());
        //是否是修改，修改的要给原始的信息
        if(mIsModify){
            mSubmitSignInfoBean.setType(SubmitSignInfoBean.MODIFY);
            mSubmitSignInfoBean.setOriginalClassID(mDataBean.getOriginalClassId());
            mSubmitSignInfoBean.setOriginalSectionID(mDataBean.getOriginalSectionId());
            mSubmitSignInfoBean.setOriginalSignDate(mDataBean.getOriginalSignDate());
        }else {
            mSubmitSignInfoBean.setType(SubmitSignInfoBean.ADD);
        }
        List<SubmitSignInfoBean.SectionBean> sectionBeanList = new ArrayList<>();
        for(int i = 0;i<mSectionBeanList.size();i++){
            SubmitSignInfoBean.SectionBean sectionBean = new SubmitSignInfoBean.SectionBean();
            //拿到对应位置的真正SectionOriginalId
            int sectionId = mSectionBeanList.get(i).getSectionOriginalId();
            sectionBean.setSectionID(sectionId);
            sectionBean.setTeacherId(mUserBean.getUserId());
            sectionBean.setCourseName(courseText);
            sectionBean.setMultipleCoursesList(mCourseInfoBeanList);
            sectionBean.setMultipleTeachersList(mTeacherInfoBeanList);
            sectionBeanList.add(sectionBean);
        }
        mSubmitSignInfoBean.setSectionBean(sectionBeanList);
    }

    private void loadData() {
        mSignDateTv.setText(mDataBean.getSignDate());
        mCourseNameTv.setText(courseText);
        mCourseTeacherTv.setText(teacherText);
        //拿到节次列表String，注意修改的时候拿到的应该是原来那个节次的数据
        sectionTrueIdString = presenter.getSectionString(mSectionBeanList);
//        Log.e("lenita","CourseS...Activity sectionTrueIdString = "+sectionTrueIdString);
        //TODO 获取的学生列表,注意:修改的有可能是改变了日期的，所以要获取的是改变日期之前那个节次的数据
        if(mIsModify){
            presenter.getStudentListNew(mDataBean.getOriginalSignDate(),mClassId,sectionTrueIdString);
        }else {
            presenter.getStudentListNew(mSignDate,mClassId,sectionTrueIdString);
        }

    }

    //初始化view
    private void initView() {
        findViews();
        initToolbar();
        setOnInteractListener();
    }

    private void initToolbar() {
        String shortClassName = mTitleClassName;
        if(mTitleClassName.length() > 11){
            shortClassName = mTitleClassName.substring(0,9)+"...";
        }
        mToolBarManager.init();
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mToolBarManager.getRightButton().setEnabled(false);
        mToolBarManager.setTitle(shortClassName)
                .showRightButton(true)
                .setRightText(this.getString(R.string.sign_confirm))
                .clickRightButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        CourseSignActivity.start(CourseSignWithAttendanceStatusActivity.this, mIsModify, mDataBean, setBean());  //旧版登记数据-旧版需要再跳入CourseSignActivity才能进行签名确认
                        formatSignData(); //新版直接在这就提交登记数据，不需要调下一页,把数据整合成提交所需
                        if(!mIsModify){
                            presenter.getIsHaveSectionHaveBeenSign(sectionTrueIdString,mSignDate,mClassId);
                        }else {
                            presenter.postSignData(mSubmitSignInfoBean,null);  //TODO 修改的不需要进行已登记判断
                        }

                    }
                });
    }

    /**
     * 旧版登记所需数据
     * @return
     */
        public List<StudentListNewBean> rxStudentListStudentListBeen;  //节次考勤 -old
    private List<StudentListBean> setBean(){
        List<StudentListBean> listBeen = new ArrayList<>();
        //转成旧的登记所数据
        for(int i = 0;i <rxStudentListStudentListBeen.size();i++){
            StudentListNewBean studentListNewBean = rxStudentListStudentListBeen.get(i);
            StudentListBean studentListBean = new StudentListBean();
            studentListBean.setStudentID(studentListNewBean.getStudentID());
            studentListBean.setStudentNO(studentListNewBean.getStudentNO());
            studentListBean.setStundentName(studentListNewBean.getStundentName());
//            studentListBean.setLESSON1(studentListNewBean.getLESSON1());
//            studentListBean.setLESSON2(studentListNewBean.getLESSON2());
//            studentListBean.setLESSON3(studentListNewBean.getLESSON3());
//            studentListBean.setLESSON4(studentListNewBean.getLESSON4());
//            studentListBean.setLESSON5(studentListNewBean.getLESSON5());
//            studentListBean.setLESSON6(studentListNewBean.getLESSON6());
//            studentListBean.setLESSON7(studentListNewBean.getLESSON7());
            listBeen.add(studentListBean);
        }
        return listBeen;
    }

    //事件处理
    private void setOnInteractListener() {
        //StudentListInnerAdapter 306 行
        //Adapter数据更新后会回调到此函数，用来提交登记
        /**
         * 节次考勤状态监听
         */
        presenter.mCompositeSubscription.add(RxBus.getDefault().toObserverable(RxStudentList.class)
                .subscribe(new Subscriber<RxStudentList>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(RxStudentList rxStudentList) {
                        int updatePosition = rxStudentList.getmOrderPosition();
//                        Log.e("lenita","C...Activity RxBus RxStudentList updatePosition = " + updatePosition);
                        //新的
                        List<StudentListNewBean> studentListNewBeanList = rxStudentList.getStudentListBeen();
                        //全节次 -- 接口8
                        SectionStudentListBean sectionStudentListBean = mSectionStudentListBeanList.get(updatePosition);
                        sectionStudentListBean.setStudentListNewBeenList(studentListNewBeanList);
                        mSectionStudentListBeanList.set(updatePosition,sectionStudentListBean);
                        //全节次 -- 所有节次
                        SectionBean sectionBean = mSectionAtendentStatusListBean.getSectionBeanList().get(updatePosition);
                        sectionBean.setStudentListBeen(studentListNewBeanList);
                        mSectionAtendentStatusListBean.getSectionBeanList().set(updatePosition,sectionBean);
                        //当登记多节课的时候，判断是否有“和上节相同”的需要修改
                        if(mSectionAtendentStatusListBean.getSectionBeanList().size() > 1){
//                            Log.e("lenita","CourseSignWithAttendanceStatusActivity RxStudentList - multi section sign");
                            changeStatusSameWithPreviousThenUpdateRightButton(1);
                        }else {
                            //只登记一节课的时候
                            String scoreString1 = mSectionAtendentStatusListBean.getSectionBeanList().get(0).getScoreString();
                            boolean isCanSign = !TextUtils.isEmpty(scoreString1);
//                            Log.e("lenita","CourseSignWithAttendanceStatusActivity RxStudentList - single section sign scoreString1 ="+scoreString1);
                            mToolBarManager.getRightButton().setEnabled(isCanSign);
                        }
//                        rxStudentListStudentListBeen = rxStudentList.getStudentListBeen(); //旧数据

                    }
                }));
        /**
         * 考勤学生列表总体情况监听-新
         */
        presenter.mCompositeSubscription.add(RxBus.getDefault().toObserverable(RxSectionBean.class)
                .subscribe(new Subscriber<RxSectionBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(RxSectionBean rxSectionBean) {
                        int sectionId = rxSectionBean.getSectionBean().getSectionId();
                        String scoreString = rxSectionBean.getSectionBean().getScoreString();
                        String remarkString = rxSectionBean.getSectionBean().getRemark();
                        int lateCount = rxSectionBean.getSectionBean().getDelayCount();
                        int leaveCount = rxSectionBean.getSectionBean().getLeaveCount();
                        int  absentCount = rxSectionBean.getSectionBean().getAbsentCount();
                        int  vocationCount = rxSectionBean.getSectionBean().getVocationCount();
                        //进行赋值
                        for(int i = 0;i < mSectionAtendentStatusListBean.getSectionBeanList().size();i++){
                            //更改对应节次的信息:考勤状况、分数和备注
                            if(mSectionAtendentStatusListBean.getSectionBeanList().get(i).getSectionId() == sectionId){
                                //全节次 -- 所有节次
                                SectionBean sectionBean = mSectionAtendentStatusListBean.getSectionBeanList().get(i);
//                                Log.e("lenita","C...Activity RxBus RxSectionBean sectionId = " + sectionId);
                                if(rxSectionBean.getWitchString() == RxSectionBean.SCORE){
                                    if(!TextUtils.isEmpty(scoreString)){
                                        sectionBean.setScore(Integer.valueOf(scoreString));
                                    }
                                    sectionBean.setScoreString(scoreString);
                                }else if(rxSectionBean.getWitchString() == RxSectionBean.REMARK){
                                    sectionBean.setRemark(remarkString);
                                }
                                sectionBean.setDelayCount(lateCount);
                                sectionBean.setLeaveCount(leaveCount);
                                sectionBean.setAbsentCount(absentCount);
                                sectionBean.setVocationCount(vocationCount);
                                mSectionAtendentStatusListBean.getSectionBeanList().set(i,sectionBean);
                            }
                        }
                        //当登记多节课的时候，判断是否有“和上节相同”的需要修改
                        if(mSectionAtendentStatusListBean.getSectionBeanList().size() > 1){
//                            Log.e("lenita","CourseSignWithAttendanceStatusActivity rxSectionBean - multi section sign");
                            changeStatusSameWithPreviousThenUpdateRightButton(1);
                        }else {
                            //只登记一节课的时候
                            String scoreString2 = mSectionAtendentStatusListBean.getSectionBeanList().get(0).getScoreString();
                            boolean isCanSign = !TextUtils.isEmpty(scoreString2);
//                            Log.e("lenita","CourseSignWithAttendanceStatusActivity rxSectionBean - single section sign scoreString2 =" + scoreString2);
                            mToolBarManager.getRightButton().setEnabled(isCanSign);
                        }
                    }
                }));
    }

    //绑定控件
    private void findViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolBarManager = new ToolBarManager(mToolbar, this);
        mTabLayout = (TabLayout) findViewById(R.id.student_list_tablayout_new);
        mViewPager = (ViewPager) findViewById(R.id.student_list_viewpager_new);
        noDataTv = (RelativeLayout) findViewById(R.id.no_data_tv_new);
        mSignDateTv = (TextView)findViewById(R.id.tv_show_sign_date);
        mCourseNameTv = (TextView)findViewById(R.id.tv_show_course_name);
        mCourseTeacherTv = (TextView)findViewById(R.id.tv_show_course_teacher);
        mSignInfoLayout = (LinearLayout)findViewById(R.id.layout_sign_info);
        /*mSignInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //测试查看所有的信息是否正确点击监听
                formatSignData();
                presenter.postSignData(mSubmitSignInfoBean);
            }
        });*/
        mSignInfoLayout.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        presenter.detachView(false);
        mToolBarManager.destroy();
    }

    /**
     * 提交数据前要对数据进行整合，拿到剩下应该赋值的数据
     */
    private void formatSignData(){
        for(int i = 0;i < mSectionAtendentStatusListBean.getSectionBeanList().size();i++){
            //拿到事前封装好的部分数据
            SubmitSignInfoBean.SectionBean sectionBean = mSubmitSignInfoBean.getSectionBean().get(i);
            if(mIsModify){ //TODO 修改的SectionId有可能已经改变，如修改第一节信息变为第二节,此时的id就是新的Id,提交的时候要注意这一点
                int sectionId = mDataBean.getSectionBeanNewSection().getSectionOriginalId();
                sectionBean.setSectionID(sectionId);
//                int sectionIdSubmit = mSubmitSignInfoBean.getSectionBean().get(i).getSectionID();
//                int OriginalSectionID = mSubmitSignInfoBean.getOriginalSectionID();
//                Log.e("lenita","mSubmitSignInfoBean OriginalSectionID = "+OriginalSectionID+",mSubmitSignInfoBean sectionIdSubmit = "+sectionIdSubmit);

            }
            //拿到分数、备注、学生列表状态，然后放入对应的节次位置
            String scoreString = mSectionAtendentStatusListBean.getSectionBeanList().get(i).getScoreString();
            String remark = mSectionAtendentStatusListBean.getSectionBeanList().get(i).getRemark();
            List<StudentListNewBean> studentListNewBeanList = mSectionAtendentStatusListBean.getSectionBeanList().get(i).getStudentListBeen();
            sectionBean.setScore(Integer.valueOf(scoreString));
            sectionBean.setRemark(remark);
            sectionBean.setStudentMarkDetails(studentListNewBeanList);
            mSubmitSignInfoBean.getSectionBean().set(i,sectionBean);
//            int sectionIdSection = mSectionAtendentStatusListBean.getSectionBeanList().get(i).getSectionOriginalId();
//            Log.e("lenita","mSectionAtendentStatusListBean sectionId = "+sectionIdSection+", score = "+mSubmitSignInfoBean.getSectionBean().get(i).getScore());
            /*if(studentListNewBeanList.size() > 1){
                Log.e("lenita","mSubmitSignInfoBean remark = "+mSubmitSignInfoBean.getSectionBean().get(i).getRemark()+",第一个学生状态 = "+studentListNewBeanList.get(0).getMarkType()+"，第二个学生状态 = "+studentListNewBeanList.get(1).getMarkType());
            }else {
                Log.e("lenita","mSubmitSignInfoBean remark = "+mSubmitSignInfoBean.getSectionBean().get(i).getRemark()+",第一个学生状态 = "+studentListNewBeanList.get(0).getMarkType());
            }*/
        }
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
    public void onBackPressed() {

        super.onBackPressed();
        CourseDetailActivity.isSectionHaveBeenSign = false;  //还原成没有查询有节次被登记的状态
    }
}
