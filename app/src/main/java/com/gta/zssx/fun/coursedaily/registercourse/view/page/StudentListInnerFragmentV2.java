package com.gta.zssx.fun.coursedaily.registercourse.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.model.ClassDataManager;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.DataBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.RxSectionBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.RxStudentList;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SectionAtendentStatusListBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SectionBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SectionStudentListBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.StudentListNewBean;
import com.gta.zssx.fun.coursedaily.registercourse.view.adapter.StudentListinnerAdapterV2;
import com.gta.zssx.fun.coursedaily.registercourse.view.adapter.ui.StudentInfoDialog;
import com.gta.zssx.fun.coursedaily.registercourse.view.base.NoPresenterBaseFragment;
import com.gta.zssx.fun.coursedaily.registercourse.view.pageV2.StudentSearchActivity;
import com.gta.zssx.pub.util.RxBus;
import com.gta.zssx.pub.widget.DisableEmojiEditText;
import com.kyleduo.switchbutton.SwitchButton;

import java.util.List;

import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by lan.zheng on 2017/3/24.
 * 新的签名确认页面
 */
public class StudentListInnerFragmentV2 extends NoPresenterBaseFragment implements View.OnClickListener {
    public static final String PAGE_TAG = StudentListInnerFragment.class.getSimpleName();
//    public static String sStudent_list = "student_list";  //用于搜索学生的回调
    public final static String sIsClickAnyStatus = "is_click_any_status";  //是否有点击状态
    public static String sDateBean = "mDateBean";
    public static String sSection = "section";
    public static String sPosition = "position";
    public static String sSection_student_list = "section_student_list";
    public List<SectionStudentListBean> mSectionStudentListBeanList;  //要登记的多节次学生状态列表--同步开关变动都需要更新这个对象
    public RxSectionBean mRxSectionBean;   //传输回调
    public ClassDataManager.DataCache mDataCache;
    public int mPosition;   //位置0,1,2,3
//    public int mSectionId;  //节次1,2,3,4,5
    public int mStudentTotalCount;  //学生总数
    public int mStudentAttendenceCount;  //实际出勤数
    public TextView mSearchStudentTextView;  //搜索
    public RecyclerView mRecyclerView;  //学生列表
    public TextView mCanNotChangeStatusTextView;  //不能再改变学生状态的半透明色
    public StudentListinnerAdapterV2 mStudentListinnerAdapterV2;
    public DataBean mDataBean;
    public boolean mIsModify;

    //未开启"和前一节相同"之前的数据，当关闭该开关的时候要还原这个值
    public List<StudentListNewBean> mStudentListBeen;  //用于记录本页面改变和搜索改变，没有开启"和前一节相同"同步前的数据
    public SectionBean mSectionBean; //用于记录没有开启"和前一节相同"同步前的数据

    //考勤汇总条目
    public TextView attendanceStatusTextView;
    public TextView sameSignShowTextView;
    public SwitchButton sameSignSwitchButton;
    public TextView lateTextView;
    public TextView leaveTextView;
    public TextView truantTextView;
    public TextView vocationTextView;
    public EditText scoreEditText;
    public DisableEmojiEditText remarkEditText;
    int delayCount = 0;
    int leaveCount = 0;
    int absentCount = 0;
    int vocationCount = 0;
    int score = 100;
    public boolean isSameWithPreviour = false;  //默认都是false

    //监听
    public CompositeSubscription mCompositeSubscription;

    TextWatcher mScoreTextWatch = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
           //开关开启的时候已经通过别的地方进行回调了，因此这里不需要再回调，开关未开启才进行处理
            if(!isSameWithPreviour){
                /**
                 * 考勤信息更改监听--更新在Activity中的数据
                 */
                sendRxBusScore(s.toString().trim());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            //没有开启开关的时候才监听
            if(!isSameWithPreviour && s != null && !s.toString().equals("")){
                int num = Integer.parseInt(s.toString());
                if (num > 100) {
                    ToastUtils.showShortToast( "分数不能超过100");
                    scoreEditText.setText(String.valueOf(score));
                }
            }
        }
    };

    TextWatcher mRemarkTextWatch = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //开关开启的时候已经通过别的地方进行回调了，因此这里不需要再回调，开关未开启才进行处理
            if(!isSameWithPreviour){
                /**
                 * 考勤信息更改监听--更新在Activity中的数据
                 */
                sendRxBusRemark(s.toString().trim());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(PAGE_TAG , "onAttach");
        dataAction();  //使用builder方法才进入这里
    }

    //fragment参数传递
    private void dataAction() {
        mPosition = getArguments().getInt(sPosition);
        mSectionBean = (SectionBean) getArguments().getSerializable(sSection);
        mDataBean = (DataBean)getArguments().getSerializable(sDateBean);
        mSectionStudentListBeanList = (List<SectionStudentListBean>) getArguments().getSerializable(sSection_student_list);
        mIsModify = mDataBean.isMotify();
        /* mStudentListBeen = (List<StudentListNewBean>) getArguments().getSerializable(sStudent_list);
        if(mSectionBean != null)
            mSectionId = mSectionBean.getSectionId();
        if(mStudentListBeen != null){
            mStudentTotalCount = mStudentListBeen.size();
            mStudentAttendenceCount = mStudentListBeen.size();
        }*/

        //保存刚刚进入的学生数据
        mStudentListBeen = mSectionStudentListBeanList.get(mPosition).getStudentListNewBeen();
        if(mStudentListBeen != null){
            mStudentTotalCount = mStudentListBeen.size();  //总人数等于总数
            mStudentAttendenceCount = mStudentListBeen.size();  //出勤人数等于总数
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_student_list;
    }

    @Override
    protected void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.student_list_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSearchStudentTextView = (TextView)view.findViewById(R.id.tv_search_student);
        mCanNotChangeStatusTextView = (TextView)view.findViewById(R.id.tv_can_not_change_status);
        mCanNotChangeStatusTextView.setVisibility(View.GONE);
        //考勤汇总
        attendanceStatusTextView = (TextView)view.findViewById(R.id.tv_attendance_status_sign);
        attendanceStatusTextView.setText(mStudentAttendenceCount+"/"+mStudentTotalCount);
        sameSignSwitchButton = (SwitchButton)view.findViewById(R.id.sb_same_with_previous_status_sign);
        sameSignShowTextView = (TextView)view.findViewById(R.id.tv_same_with_previous_show);
        //第一个Fragment不需要显示
        if(mPosition == 0){
            sameSignShowTextView.setVisibility(View.GONE);
            sameSignSwitchButton.setVisibility(View.GONE);
        }
        lateTextView = (TextView)view.findViewById(R.id.tv_late_person_sign);
        leaveTextView = (TextView)view.findViewById(R.id.tv_leave_person_sign);
        truantTextView = (TextView)view.findViewById(R.id.tv_truant_person_sign);
        vocationTextView = (TextView)view.findViewById(R.id.tv_holiday_person_sign);
        scoreEditText = (EditText)view.findViewById(R.id.et_score_sign);
        scoreEditText.addTextChangedListener(mScoreTextWatch);
        scoreEditText.setOnClickListener(this);
        remarkEditText = (DisableEmojiEditText)view.findViewById(R.id.et_remark_sign);
        remarkEditText.addTextChangedListener(mRemarkTextWatch);
        remarkEditText.setOnClickListener(this);
        remarkEditText.setMaxLength(200);
        remarkEditText.setMaxLine(6);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    protected void initData() {
        mDataCache = ClassDataManager.getDataCache(); //废弃？？？
        setOnInteractListener();
        mRxSectionBean = new RxSectionBean();
        //TODO 新增和修改都要计算考勤块信息，并附初值
        showAttendanceStatusWithOutRemark(true);
//        Log.e("lenita","StudentListInnerFragmentV2 position = "+mPosition+",mIsModify = "+mIsModify);
        if(mIsModify){
//            Log.e("lenita","StudentListInnerFragmentV2 modify score = "+mDataBean.getScoreString()+",remark = "+mDataBean.getMemo());
            score = Integer.valueOf(mDataBean.getScoreString());
            scoreEditText.setText(mDataBean.getScoreString());
            remarkEditText.setText(mDataBean.getMemo());
        }
        //TODO 显示初始的学生状态数据
        mStudentListinnerAdapterV2 = new StudentListinnerAdapterV2(getActivity(),mSectionBean, mSectionStudentListBeanList, mPosition, lListener);
        mStudentListinnerAdapterV2.setIsCanClick(true);
        mRecyclerView.setAdapter(mStudentListinnerAdapterV2);
    }

    /**
     * 考勤信息更改监听--更新在Activity中的数据
     */
    private void sendRxBusScore(String score){  //这个必须是全赋值，同步开关关闭的时候需要这份数据来进行数据还原
        mSectionBean.setDelayCount(delayCount);
        mSectionBean.setLeaveCount(leaveCount);
        mSectionBean.setAbsentCount(absentCount);
        mSectionBean.setVocationCount(vocationCount);
        mSectionBean.setScoreString(score);
        mRxSectionBean.setSectionBean(mSectionBean);
        mRxSectionBean.setWitchString(RxSectionBean.SCORE);
        /**
         * 考勤信息更改监听 -- 更新在Activity中的数据
         */
        RxBus.getDefault().post(mRxSectionBean);
    }

    /**
     * 考勤信息更改监听--更新在Activity中的数据
     */
    private void sendRxBusRemark(String remark){  //这个必须是全赋值，同步开关关闭的时候需要这份数据来进行数据还原
        mSectionBean.setDelayCount(delayCount);
        mSectionBean.setLeaveCount(leaveCount);
        mSectionBean.setAbsentCount(absentCount);
        mSectionBean.setVocationCount(vocationCount);
        mSectionBean.setRemark(remark);
        mRxSectionBean.setSectionBean(mSectionBean);
        mRxSectionBean.setWitchString(RxSectionBean.REMARK);
//        Log.e("lenita","StudentListInnerFragmentV2 score = "+score+",remark = "+remark);
        /**
         * 考勤信息更改监听 -- 更新在Activity中的数据
         */
        RxBus.getDefault().post(mRxSectionBean);
    }


    StudentListinnerAdapterV2.Listener lListener = new StudentListinnerAdapterV2.Listener() {
        @Override
        public void itemClick(StudentListNewBean studentListNewBean) {
//            backgroundAlpha(0.8f);
            showStudentInfoDialog(studentListNewBean);
        }
    };

    public StudentInfoDialog mStudentInfoDialog;
    private void showStudentInfoDialog(StudentListNewBean studentListNewBean){
        if(mStudentInfoDialog != null){
            mStudentInfoDialog = null;
        }
        mStudentInfoDialog = new StudentInfoDialog(getActivity(), studentListNewBean, new StudentInfoDialog.Listener() {
            @Override
            public void onDialogDismissListener() {
                //暂时不用处理
//                backgroundAlpha(1f);
            }
        });
        mStudentInfoDialog.show();
    }

    //事件处理
    private void setOnInteractListener() {
        mCompositeSubscription = new CompositeSubscription();
        /**
         * 学生列表状态监听-开关关闭时候监听
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
                        //更改的是对应的Fragment里面的内容,当开关是关着的时候才需要监听，开关开着的时候界面会通过回调更新，不需要这里更新
                        if(rxStudentList.getmOrderPosition() == mPosition && !isSameWithPreviour){
//                            Log.e("lenita","S...Fragment RxBus mPosition"+mPosition+",rxStudentList isSameWithPreviour ="+isSameWithPreviour);
                            //搜索也是这份数据
                            mStudentListBeen = rxStudentList.getStudentListBeen();
                            //更新整个要登记的数据集
                            mSectionStudentListBeanList.get(mPosition).setStudentListNewBeenList(mStudentListBeen);
                            //Adapter可以不重新生成，但是考勤块信息要更新
                            showAttendanceStatusWithOutRemark(false);
                        }

                    }
                }));
        /**
         * 同步按钮信息回调监听 -- 只有开关开启的状态才能收到回调信息，否则都不做处理
         */
        mCompositeSubscription.add(RxBus.getDefault().toObserverable(SectionAtendentStatusListBean.class)
                .subscribe(new Subscriber<SectionAtendentStatusListBean>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(SectionAtendentStatusListBean sectionAtendentStatusListBean) {
                        if(isSameWithPreviour){
                            //TODO 开关打开的节次需要更新界面成和前一节一样，用mPosition来作为要修改的指针位置
                            SectionBean sectionBean = sectionAtendentStatusListBean.getSectionBeanList().get(mPosition);
                            //更新存储学生状态的列表
                            mSectionStudentListBeanList.get(mPosition).setStudentListNewBeenList(sectionBean.getStudentListBeen());
//                            Log.e("lenita",mPosition+" SLIFragment 开关打开 sectionId = "+sectionBean.getSectionId()+"，修改后 score = "+sectionBean.getScore());
                            //更新界面
                            setSameWithPrevioursDataOrRestoreData(sectionBean);
                        }

                    }
                }));

        /**
         * 搜索监听
         */
        mSearchStudentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StudentSearchActivity.start(StudentListInnerFragmentV2.this,getActivity(),mSectionBean,mPosition,mStudentListBeen);
            }
        });
        /**
         * 同步按钮监听
         */
        sameSignSwitchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isSameWithPreviour = isChecked;
//                ToastUtils.showShortToast(getActivity(),"isSameWithPreviour = "+isSameWithPreviour);
                setIsSameWithPreviourView();
            }
        });
    }

    /**
     * 打开或者关闭都通过这个函数进行数据重新赋值
     * @param sectionBean
     */
    private void setSameWithPrevioursDataOrRestoreData(SectionBean sectionBean){
        //设置分数、备注、请假等信息
        int mStudentAttendenceCount = mStudentTotalCount - sectionBean.getLeaveCount()-sectionBean.getAbsentCount()-sectionBean.getVocationCount();
        //总状态
        attendanceStatusTextView.setText(mStudentAttendenceCount+"/"+mStudentTotalCount);
        //迟到
        int delayCount = sectionBean.getDelayCount();
        lateTextView.setText(String.valueOf(delayCount));
        if(delayCount > 0){
            lateTextView.setTextColor(getActivity().getResources().getColor(R.color.main_color));
        }else {
            lateTextView.setTextColor(getActivity().getResources().getColor(R.color.word_color_999999));
        }
        //请假
        int leaveCount = sectionBean.getLeaveCount();
        leaveTextView.setText(String.valueOf(leaveCount));
        if(leaveCount > 0){
            leaveTextView.setTextColor(getActivity().getResources().getColor(R.color.main_color));
        }else {
            leaveTextView.setTextColor(getActivity().getResources().getColor(R.color.word_color_999999));
        }
        //旷课
        int absentCount = sectionBean.getAbsentCount();
        truantTextView.setText(String.valueOf(absentCount));
        if(absentCount > 0){
            truantTextView.setTextColor(getActivity().getResources().getColor(R.color.main_color));
        }else {
            truantTextView.setTextColor(getActivity().getResources().getColor(R.color.word_color_999999));
        }
        //公假
        int vocationCount = sectionBean.getVocationCount();
        vocationTextView.setText(String.valueOf(vocationCount));
        if(vocationCount > 0 ){
            vocationTextView.setTextColor(getActivity().getResources().getColor(R.color.main_color));
        }else {
            vocationTextView.setTextColor(getActivity().getResources().getColor(R.color.word_color_999999));
        }
        //分数
        scoreEditText.setText(sectionBean.getScoreString());
        //备注
        remarkEditText.setText(sectionBean.getRemark());
        //更新适配器的显示
        mStudentListinnerAdapterV2  = new StudentListinnerAdapterV2(getActivity(),sectionBean, mSectionStudentListBeanList, mPosition, lListener);
        mStudentListinnerAdapterV2.setIsCanClick(!isSameWithPreviour);
        mRecyclerView.setAdapter(mStudentListinnerAdapterV2);
    }

    private void setIsSameWithPreviourView(){
        if(isSameWithPreviour){
            //和上一节相同，因此不需要传这里的数据
            mListener.listener(mPosition,isSameWithPreviour,null,null);
            mSearchStudentTextView.setVisibility(View.GONE);
            mCanNotChangeStatusTextView.setVisibility(View.VISIBLE);
            mStudentListinnerAdapterV2.setIsCanClick(false);
            scoreEditText.setFocusableInTouchMode(false);
            scoreEditText.setFocusable(false);
            remarkEditText.setFocusableInTouchMode(false);
            remarkEditText.setFocusable(false);
        }else {
            //还原数据需要传这里的原先未开启时的数据
            mSearchStudentTextView.setVisibility(View.VISIBLE);
            mCanNotChangeStatusTextView.setVisibility(View.GONE);
            mStudentListinnerAdapterV2.setIsCanClick(true);
            scoreEditText.setFocusableInTouchMode(true);
            scoreEditText.setFocusable(true);
            remarkEditText.setFocusableInTouchMode(true);
            remarkEditText.setFocusable(true);
            //先还原数据
            mSectionStudentListBeanList.get(mPosition).setStudentListNewBeenList(mStudentListBeen);
//            String status = ""+mSectionStudentListBeanList.get(mPosition).getStudentListNewBeen().get(0).getMarkType();
//            Log.e("lenita","SLIFragment 开关关闭同步数据，第一个学生状态"+status);
            setSameWithPrevioursDataOrRestoreData(mSectionBean);
            //告知Activity需要更新存储的数据集
            mListener.listener(mPosition,isSameWithPreviour,mSectionBean,mStudentListBeen);
        }
    }

    private void showAttendanceStatusWithOutRemark(boolean isNeedToChangeScore){
        //计算各项出勤
        countAttendanceNum(mStudentListBeen);
        //给view赋值
        if(mSectionBean!= null){
            mStudentAttendenceCount = mStudentTotalCount - leaveCount-absentCount-vocationCount;
            //总状态
            attendanceStatusTextView.setText(mStudentAttendenceCount+"/"+mStudentTotalCount);
            //迟到
            lateTextView.setText(String.valueOf(delayCount));
            if(delayCount > 0){
                lateTextView.setTextColor(getActivity().getResources().getColor(R.color.main_color));
            }else {
                lateTextView.setTextColor(getActivity().getResources().getColor(R.color.word_color_999999));
            }
            //请假
            leaveTextView.setText(String.valueOf(leaveCount));
            if(leaveCount > 0){
                leaveTextView.setTextColor(getActivity().getResources().getColor(R.color.main_color));
            }else {
                leaveTextView.setTextColor(getActivity().getResources().getColor(R.color.word_color_999999));
            }
            //旷课
            truantTextView.setText(String.valueOf(absentCount));
            if(absentCount > 0){
                truantTextView.setTextColor(getActivity().getResources().getColor(R.color.main_color));
            }else {
                truantTextView.setTextColor(getActivity().getResources().getColor(R.color.word_color_999999));
            }
            //公假
            vocationTextView.setText(String.valueOf(vocationCount));
            if(vocationCount > 0 ){
                vocationTextView.setTextColor(getActivity().getResources().getColor(R.color.main_color));
            }else {
                vocationTextView.setTextColor(getActivity().getResources().getColor(R.color.word_color_999999));

            }

            String scoreString = scoreEditText.getText().toString();  //默认是显示回原来的分数
            if(isNeedToChangeScore){ //TODO 第一次进入的时候是一定要改变分数的，其他情况都不要去自动计分
                scoreString = ""+score;
            }
            scoreEditText.setText(scoreString);
        }
    }

    public void countAttendanceNum(List<StudentListNewBean> studentListNewBeanList) {
        delayCount = 0;
        leaveCount = 0;
        absentCount = 0;
        vocationCount = 0;
        for(int i = 0;i< studentListNewBeanList.size();i++){
            int studentStatus = studentListNewBeanList.get(i).getMarkType();
            switch (studentStatus){
                case 2:
                    delayCount++;
                    break;
                case 3:
                    leaveCount++;
                    break;
                case 4:
                    absentCount++;
                    break;
                case 5:
                    vocationCount++;
                    break;
                default:
                    break;
            }
        }
        //TODO 分数（旷课扣3，迟到扣1，其他不扣）
        int points = delayCount+absentCount*3;
        score = 100 - points;
        if (score < 0) {
            score = 0;
        }
    }

    //搜索时有改变的都回调到这里进行View的改变
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==  Activity.RESULT_FIRST_USER && resultCode == mPosition){
            Log.e(PAGE_TAG+mPosition,"Fragment-resultCode = "+resultCode);
            //TODO 在搜索界面回调的时候已经拿到了最新的该节次学生考勤,这里只需要刷新Adapter考勤数据
            boolean isClickAnyStatus = data.getBooleanExtra(sIsClickAnyStatus,false);
            if(isClickAnyStatus)  //只有确定有点击改变时分数才变化，否则不做处理
            {
                mStudentListinnerAdapterV2  = new StudentListinnerAdapterV2(getActivity(),mSectionBean, mSectionStudentListBeanList, mPosition, lListener);
                mStudentListinnerAdapterV2.setIsCanClick(true);  //能搜索的一定是没有开启这个开关的
                mRecyclerView.setAdapter(mStudentListinnerAdapterV2);
                showAttendanceStatusWithOutRemark(false);  //刷新显示考勤块信息
            }

        }
    }

    private Listener mListener;
    public void setChangeStatusListener(Listener listener){
        mListener = listener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.et_score_sign:
                if(!isSameWithPreviour){
                    if (getActivity().getCurrentFocus() != null)
                    {
                        InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.showSoftInput( getActivity().getCurrentFocus(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
                break;
            case R.id.et_remark_sign:
                if(!isSameWithPreviour){
                    if (getActivity().getCurrentFocus() != null)
                    {
                        InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.showSoftInput( getActivity().getCurrentFocus(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
                break;
            default:
                break;
        }
    }

    public interface Listener{
        /**
         * 监听开关的打开和关闭
         * @param position  打开位置
         * @param changeStatus 开关状态
         * @param sectionBean     节次
         * @param studentListNewBeanList  学生状态列表
         */
        void listener(int position,boolean changeStatus,SectionBean sectionBean,List<StudentListNewBean> studentListNewBeanList);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCompositeSubscription.unsubscribe();
    }

    private void backgroundAlpha(float b) {
        WindowManager.LayoutParams layoutParams = getActivity().getWindow().getAttributes();
        layoutParams.alpha = b;
        getActivity().getWindow().setAttributes(layoutParams);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
