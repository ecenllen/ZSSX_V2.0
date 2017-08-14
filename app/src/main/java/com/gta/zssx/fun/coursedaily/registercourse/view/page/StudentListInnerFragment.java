package com.gta.zssx.fun.coursedaily.registercourse.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.model.ClassDataManager;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.RxSectionBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.RxStudentList;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SectionBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SectionStudentListBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.StateBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.StudentListNewBean;
import com.gta.zssx.fun.coursedaily.registercourse.presenter.StudentListInnerPresenter;
import com.gta.zssx.fun.coursedaily.registercourse.view.StudentListInnerView;
import com.gta.zssx.fun.coursedaily.registercourse.view.adapter.StudentListInnerAdapter;
import com.gta.zssx.fun.coursedaily.registercourse.view.base.RegisterCourseBaseFragment;
import com.gta.zssx.fun.coursedaily.registercourse.view.base.RegisterCourseFragmentBuilder;
import com.gta.zssx.fun.coursedaily.registercourse.view.pageV2.StudentSearchActivity;
import com.gta.zssx.pub.util.RxBus;
import com.gta.zssx.pub.widget.DisableEmojiEditText;
import com.kyleduo.switchbutton.SwitchButton;

import java.io.Serializable;
import java.util.List;

import rx.Subscriber;

/**
 * [Description]
 * <p/>
 * [How to use]
 * <p/>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/24.
 * @since 1.0.0
 */
public class StudentListInnerFragment extends RegisterCourseBaseFragment<StudentListInnerView, StudentListInnerPresenter> implements StudentListInnerView {

    public static final String PAGE_TAG = StudentListInnerFragment.class.getSimpleName();
    public static String sStudent_list = "student_list";
    public static String sSection = "section";
    public static String sPosition = "position";
    public static String sSection_student_list = "section_student_list";
    public List<SectionStudentListBean> mSectionStudentListBean;
    public List<StudentListNewBean> mStudentListBeen;
    public SectionBean mSectionBean;
    public RxSectionBean mRxSectionBean;
    public ClassDataManager.DataCache mDataCache;
    public int mPosition;
    public int mSectionId;
    public int mStudentTotalCount;  //学生总数
    public int mStudentAttendenceCount;  //实际出勤数
    public TextView mSearchStudentTextView;  //搜索
    public RecyclerView mRecyclerView;  //学生列表

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
    boolean isSameWithPreviour;

    TextWatcher mScoreTextWatch = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            SectionBean sectionBean = new SectionBean();
            sectionBean.setSectionId(mSectionId);
            sectionBean.setDelayCount(Integer.valueOf(lateTextView.getText().toString()));
            sectionBean.setLeaveCount(Integer.valueOf(leaveTextView.getText().toString()));
            sectionBean.setAbsentCount(Integer.valueOf(truantTextView.getText().toString()));
            sectionBean.setVocationCount(Integer.valueOf(vocationTextView.getText().toString()));
            sectionBean.setScoreString(s.toString());  //分数
            sectionBean.setRemark(remarkEditText.getText().toString().trim());  //备注
            mRxSectionBean = new RxSectionBean();
            mRxSectionBean.setSectionBean(sectionBean);
            //TODO 更新在Activity中的数据
            /**
             * 考勤信息更改监听
             */
            RxBus.getDefault().post(mRxSectionBean);
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s != null && !s.toString().equals("")) {
                int num = Integer.parseInt(s.toString());
                if (num > 100) {
                    ToastUtils.showShortToast("分数不能超过100");
                    scoreEditText.setText(""+score);
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
            SectionBean sectionBean = new SectionBean();
            sectionBean.setSectionId(mSectionId);
            sectionBean.setScoreString(""+score);
            sectionBean.setRemark(s.toString());
            //TODO 更新在Activity中的数据
            RxBus.getDefault().post(sectionBean);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    @NonNull
    @Override
    public StudentListInnerPresenter createPresenter() {
        return new StudentListInnerPresenter();
    }

    public static class Builder extends RegisterCourseFragmentBuilder<StudentListInnerFragment> {
        private List<StudentListNewBean> mStudentListBeen;
        private SectionBean mSectionBean;
        private int mPosition;

        public Builder(Context context, int position, List<StudentListNewBean> studentListBeen, SectionBean sectionBean) {
            super(context);
            mPosition = position;
            mStudentListBeen = studentListBeen;
            mSectionBean = sectionBean;
        }

        //暂不使用
        public Builder(Context context, SectionBean sectionBean) {
            super(context);
            mSectionBean = sectionBean;

        }

        //暂不使用
        public Builder(Context context, int position) {
            super(context);
            mPosition = position;

        }

        @Override
        public StudentListInnerFragment create() {

            Bundle lBundle = new Bundle();
            lBundle.putSerializable(sStudent_list, (Serializable) mStudentListBeen);
            lBundle.putSerializable(sSection, mSectionBean);
            lBundle.putInt(sPosition, mPosition);
            StudentListInnerFragment lStudentListInnerFragment = new StudentListInnerFragment();
            lStudentListInnerFragment.setArguments(lBundle);
            return lStudentListInnerFragment;
        }

        @Override
        public String getPageTag() {
            return PAGE_TAG;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(PAGE_TAG , "onAttach");
        dataAction();  //使用builder方法才进入这里
    }

    //fragment参数传递
    private void dataAction() {
        mPosition = getArguments().getInt(sPosition);
        mSectionBean = (SectionBean) getArguments().getSerializable(sSection);
        mSectionStudentListBean = (List<SectionStudentListBean>) getArguments().getSerializable(sSection_student_list);
        Log.e("lenita","mSectionStudentListBean size = "+mSectionStudentListBean.size());
        //TODO 改结构
//        mStudentListBeen = mSectionStudentListBean.get(mPosition+1).getStudentListNewBeen();
        //TODO 原来的数据
        mStudentListBeen = (List<StudentListNewBean>) getArguments().getSerializable(sStudent_list);
        if(mSectionBean != null)
            mSectionId = mSectionBean.getSectionId();
        if(mStudentListBeen != null){
            mStudentTotalCount = mStudentListBeen.size();
            mStudentAttendenceCount = mStudentListBeen.size();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View lInflate = inflater.inflate(R.layout.fragment_student_list, container, false);
        Log.d(PAGE_TAG, "onCreateView");
        dataAction(); //数据获取
        uiAction(lInflate);
        return lInflate;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(PAGE_TAG, "onViewCreated");
        presenter.attachView(this);
    }

    private void uiAction(View view) {
        mDataCache = ClassDataManager.getDataCache();
        findView(view);
        setOnInteractListener();
        //显示初始的考勤数据
        StudentListInnerAdapter lAdapter = new StudentListInnerAdapter(mSectionBean, mStudentListBeen, getActivity(), lListener);
        mRecyclerView.setAdapter(lAdapter);
        showAttendanceStatus(); //TODO 显示考勤块信息
    }
    private void showAttendanceStatus(){
        //计算各项出勤
        countAttendanceNum(mStudentListBeen);
        if(mSectionBean!= null){
            mStudentAttendenceCount = mStudentTotalCount - leaveCount-absentCount-vocationCount;
            //总状态
            attendanceStatusTextView.setText(mStudentAttendenceCount+"/"+mStudentTotalCount);
            scoreEditText.setText(""+score);
            //迟到
            lateTextView.setText(""+delayCount);
            if(delayCount > 0){
                lateTextView.setTextColor(getActivity().getResources().getColor(R.color.main_color));
            }else {
                lateTextView.setTextColor(getActivity().getResources().getColor(R.color.word_color_999999));
            }
            //请假
            leaveTextView.setText(""+leaveCount);
            if(leaveCount > 0){
                leaveTextView.setTextColor(getActivity().getResources().getColor(R.color.main_color));
            }else {
                leaveTextView.setTextColor(getActivity().getResources().getColor(R.color.word_color_999999));
            }
            //旷课
            truantTextView.setText(""+absentCount);
            if(absentCount > 0){
                truantTextView.setTextColor(getActivity().getResources().getColor(R.color.main_color));
            }else {
                truantTextView.setTextColor(getActivity().getResources().getColor(R.color.word_color_999999));
            }
            //公假
            vocationTextView.setText(""+vocationCount);
            if(vocationCount > 0 ){
                vocationTextView.setTextColor(getActivity().getResources().getColor(R.color.main_color));
            }else {
                vocationTextView.setTextColor(getActivity().getResources().getColor(R.color.word_color_999999));

            }
        }
    }

    //搜索时有改变的都回调到这里进行View的改变
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==  Activity.RESULT_FIRST_USER && resultCode == mPosition){
            Log.e(PAGE_TAG+mPosition,"Fragment-resultCode = "+resultCode);
            mStudentListBeen = (List<StudentListNewBean>) data.getExtras().getSerializable(sStudent_list);
            //显示初始的考勤数据
            StudentListInnerAdapter lAdapter = new StudentListInnerAdapter(mSectionBean, mStudentListBeen, getActivity(), lListener);
            mRecyclerView.setAdapter(lAdapter);
            showAttendanceStatus();  //显示考勤块信息
        }
    }

    //事件处理
    private void setOnInteractListener() {
        /**
         * 学生考勤状态监听
         */
        RxBus.getDefault().toObserverable(RxStudentList.class)
                .subscribe(new Subscriber<RxStudentList>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(RxStudentList rxStudentList) {
                        Log.e("lenita","S...Fragment RxBus");
                        mStudentListBeen = rxStudentList.getStudentListBeen();
                        showAttendanceStatus();  //Adapter可以不重新生成，但是考勤块信息要更新
                    }
                });

        /**
         * 搜索监听
         */
        mSearchStudentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StudentSearchActivity.start(StudentListInnerFragment.this,getActivity(),mSectionBean,mPosition,mStudentListBeen);
//                bundle.putSerializable(sStudent_list, (Serializable) mStudentListBeen);
//                bundle.putInt(sPosition,mPosition);  //用于判断startActivityForResult应该要告知的Fragment
//                bundle.putInt(sSectionId,mSectionBean.getSectionID());  //用于定位要搜索那个节次下的学生
            }
        });
        sameSignSwitchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isSameWithPreviour = isChecked;
                ToastUtils.showShortToast("isSameWithPreviour = "+isSameWithPreviour);
                if(isChecked){
                    //TODO 数据变成和前一节次一样
                    mSearchStudentTextView.setVisibility(View.GONE);
                }else {
                    //TODO 数据还原
                    mSearchStudentTextView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    //绑定控件
    private void findView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.student_list_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSearchStudentTextView = (TextView)view.findViewById(R.id.tv_search_student);
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
        scoreEditText.setText(""+score);
        scoreEditText.addTextChangedListener(mScoreTextWatch);
        remarkEditText = (DisableEmojiEditText)view.findViewById(R.id.et_remark_sign);
        remarkEditText.addTextChangedListener(mRemarkTextWatch);
    }

    StudentListInnerAdapter.Listener lListener = new StudentListInnerAdapter.Listener() {
        @Override
        public void itemClick() {

        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(PAGE_TAG + mPosition, "onDestroyView");
        presenter.detachView(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(PAGE_TAG + mPosition, "onDestroy");
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(PAGE_TAG + mPosition, "onActivityCreated");
    }

    @Override
    public boolean onBackPress() {
        ClassDataManager.getDataCache().setStudentList(null);
        return super.onBackPress();
    }

    public void countAttendanceNum(List<StudentListNewBean> studentListNewBeanList){
        int lSectionID = mSectionBean.getSectionId();
        delayCount = 0;
        leaveCount = 0;
        absentCount = 0;
        vocationCount = 0;
        /**
         * 计算每节课迟到，请假，旷课，公假的人数
         */
        switch (lSectionID) {
            case 1:
                for (int i = 0; i < studentListNewBeanList.size(); i++) {
                    StudentListNewBean lStudentListBean = studentListNewBeanList.get(i);
                    int lLESSON1 = 0;//lStudentListBean.getLESSON1();
                    if (lLESSON1 == StateBean.DELAY) {
                        delayCount = delayCount + 1;
                    } else if (lLESSON1 == StateBean.LEAVE) {
                        leaveCount = leaveCount + 1;
                    } else if (lLESSON1 == StateBean.ABSENT) {
                        absentCount = absentCount + 1;
                    } else if (lLESSON1 == StateBean.VOCATION) {
                        vocationCount = vocationCount + 1;
                    }
                }
                break;
            case 2:
                for (int i = 0; i < studentListNewBeanList.size(); i++) {
                    StudentListNewBean lStudentListBean = studentListNewBeanList.get(i);
                    int lLESSON1 = 0;//lStudentListBean.getLESSON2();
                    if (lLESSON1 == StateBean.DELAY) {
                        delayCount = delayCount + 1;
                    } else if (lLESSON1 == StateBean.LEAVE) {
                        leaveCount = leaveCount + 1;
                    } else if (lLESSON1 == StateBean.ABSENT) {
                        absentCount = absentCount + 1;
                    } else if (lLESSON1 == StateBean.VOCATION) {
                        vocationCount = vocationCount + 1;
                    }
                }
                break;
            case 3:
                for (int i = 0; i < studentListNewBeanList.size(); i++) {
                    StudentListNewBean lStudentListBean = studentListNewBeanList.get(i);
                    int lLESSON1 = 0;//lStudentListBean.getLESSON3();
                    if (lLESSON1 == StateBean.DELAY) {
                        delayCount = delayCount + 1;
                    } else if (lLESSON1 == StateBean.LEAVE) {
                        leaveCount = leaveCount + 1;
                    } else if (lLESSON1 == StateBean.ABSENT) {
                        absentCount = absentCount + 1;
                    } else if (lLESSON1 == StateBean.VOCATION) {
                        vocationCount = vocationCount + 1;
                    }
                }
                break;
            case 4:
                for (int i = 0; i < studentListNewBeanList.size(); i++) {
                    StudentListNewBean lStudentListBean = studentListNewBeanList.get(i);
                    int lLESSON1 = 0;//lStudentListBean.getLESSON4();
                    if (lLESSON1 == StateBean.DELAY) {
                        delayCount = delayCount + 1;
                    } else if (lLESSON1 == StateBean.LEAVE) {
                        leaveCount = leaveCount + 1;
                    } else if (lLESSON1 == StateBean.ABSENT) {
                        absentCount = absentCount + 1;
                    } else if (lLESSON1 == StateBean.VOCATION) {
                        vocationCount = vocationCount + 1;
                    }
                }
                break;
            case 5:
                for (int i = 0; i < studentListNewBeanList.size(); i++) {
                    StudentListNewBean lStudentListBean = studentListNewBeanList.get(i);
                    int lLESSON1 = 0;//lStudentListBean.getLESSON5();
                    if (lLESSON1 == StateBean.DELAY) {
                        delayCount = delayCount + 1;
                    } else if (lLESSON1 == StateBean.LEAVE) {
                        leaveCount = leaveCount + 1;
                    } else if (lLESSON1 == StateBean.ABSENT) {
                        absentCount = absentCount + 1;
                    } else if (lLESSON1 == StateBean.VOCATION) {
                        vocationCount = vocationCount + 1;
                    }
                }
                break;
            case 6:
                for (int i = 0; i < studentListNewBeanList.size(); i++) {
                    StudentListNewBean lStudentListBean = studentListNewBeanList.get(i);
                    int lLESSON1 = 0;//lStudentListBean.getLESSON6();
                    if (lLESSON1 == StateBean.DELAY) {
                        delayCount = delayCount + 1;
                    } else if (lLESSON1 == StateBean.LEAVE) {
                        leaveCount = leaveCount + 1;
                    } else if (lLESSON1 == StateBean.ABSENT) {
                        absentCount = absentCount + 1;
                    } else if (lLESSON1 == StateBean.VOCATION) {
                        vocationCount = vocationCount + 1;
                    }
                }
                break;
            case 7:
                for (int i = 0; i < studentListNewBeanList.size(); i++) {
                    StudentListNewBean lStudentListBean = studentListNewBeanList.get(i);
                    int lLESSON1 = 0;//lStudentListBean.getLESSON7();
                    if (lLESSON1 == StateBean.DELAY) {
                        delayCount = delayCount + 1;
                    } else if (lLESSON1 == StateBean.LEAVE) {
                        leaveCount = leaveCount + 1;
                    } else if (lLESSON1 == StateBean.ABSENT) {
                        absentCount = absentCount + 1;
                    } else if (lLESSON1 == StateBean.VOCATION) {
                        vocationCount = vocationCount + 1;
                    }
                }
                break;
            default:
                break;
        }

        //TODO 分数（旷课扣3，迟到扣1，其他不扣）
        int points = delayCount+absentCount*3;
        score = 100 - points;
        if (score < 0) {
            score = 0;
        }
    }
}
