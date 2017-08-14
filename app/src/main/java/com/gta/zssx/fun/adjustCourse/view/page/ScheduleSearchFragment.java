package com.gta.zssx.fun.adjustCourse.view.page;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.R;
import com.gta.zssx.fun.adjustCourse.model.bean.CurrentSemesterBean;
import com.gta.zssx.fun.adjustCourse.model.bean.SearchArguments;
import com.gta.zssx.fun.adjustCourse.model.bean.StuClassBean;
import com.gta.zssx.fun.adjustCourse.model.bean.TeacherBean;
import com.gta.zssx.fun.adjustCourse.presenter.ScheduleSearchPresenter;
import com.gta.zssx.fun.adjustCourse.view.ScheduleSearchView;
import com.gta.zssx.fun.adjustCourse.view.base.Constant;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.pub.base.BaseFragment;
import com.gta.zssx.pub.util.LogUtil;

import org.joda.time.DateTime;

import java.util.Date;

import static com.gta.zssx.fun.adjustCourse.view.page.ChooseTeacherActivity.TYPE;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/10/26.
 * @since 1.0.0
 */
public class ScheduleSearchFragment extends BaseFragment<ScheduleSearchView, ScheduleSearchPresenter> implements ScheduleSearchView {


    private static final String SEARCH_TYPE = "search_type";
    public static final int CHOOSE_CLASS_REQUEST_CODE = 108;
    public static final int CHOOSE_TEACHER_REQUEST_CODE = 107;
    public static final int SEARCH_SCHEDULE_DATE_REQUEST_CODE = 109;
    public static final String DEPT_ID = "deptId";

    private TextView mSemesterTv;
    private TextView mClassNameTv;
    private TextView mDateTv;
    private LinearLayout mClassLayout;
    private LinearLayout mDateLayout;
    public static String CLASS = "class";
    public static String TEACHER = "teacher";
    public String mType;
    private TextView mSearchTypeTv;
    public TeacherBean.TeacherListBean mTeacherListBean;
    public Date mDate;
    private Button mSearchBtn;
    public CurrentSemesterBean mSemesterBean;
    public StuClassBean.ClassListBean mClassListBean;


    @NonNull
    @Override
    public ScheduleSearchPresenter createPresenter() {
        return new ScheduleSearchPresenter(mActivity);
    }

    public ScheduleSearchFragment() {

    }

    public static ScheduleSearchFragment newInstance(String type) {
        ScheduleSearchFragment fragment = new ScheduleSearchFragment();
        Bundle lArgs = new Bundle();
        lArgs.putString(SEARCH_TYPE, type);
        fragment.setArguments(lArgs);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_schedule_search, container, false);
        initView(view);
        setInteractListener();
        return view;
    }

    private void setInteractListener() {
        mClassLayout.setOnClickListener(v -> {
            if (mType.equals(CLASS)) {
                Intent lIntent = new Intent(mActivity, ChooseClassActivity.class);

                this.startActivityForResult(lIntent, CHOOSE_CLASS_REQUEST_CODE);
            } else {
                Intent lIntent = new Intent(mActivity, ChooseTeacherActivity.class);
                lIntent.putExtra(TYPE, ChooseTeacherActivity.ALL_TEACHER);
                lIntent.putExtra(DEPT_ID, 1);
                this.startActivityForResult(lIntent, CHOOSE_TEACHER_REQUEST_CODE);
            }
        });

        mDateLayout.setOnClickListener(v -> {
            Intent lIntent = new Intent(mActivity, DateChooseActivity.class);
            Bundle lBundle = new Bundle();
            lBundle.putSerializable(CourseScheduleActivity.DATETIME, mDate);
            lIntent.putExtras(lBundle);
            this.startActivityForResult(lIntent, SEARCH_SCHEDULE_DATE_REQUEST_CODE);

        });

        mSearchBtn.setEnabled(false);


        mSearchBtn.setOnClickListener(v -> {
            SearchArguments lSearchArguments = new SearchArguments();
            lSearchArguments.setFlag(mType.equals(CLASS) ? CourseScheduleActivity.CLASS_SCHEDULE : CourseScheduleActivity.TEACHER_SCHEDULE);
            lSearchArguments.setFromSearch(true);
            if (mSemesterBean != null) {
                lSearchArguments.setSemesterId(mSemesterBean.getSemester());
            }
            if (mClassListBean != null) {
                lSearchArguments.setClassId(mClassListBean.getId() + "");
                lSearchArguments.setScheduleName(mClassListBean.getClassName());
            }
            if (mTeacherListBean != null) {
                lSearchArguments.setScheduleName(mTeacherListBean.getTeacherName());
                lSearchArguments.setTeacherUUId(mTeacherListBean.getTeacherBId());
            }
            lSearchArguments.setDate(new DateTime(mDate).toString(Constant.DATE_TYPE_01));
            int lWeekIndex = TimeUtils.getWeekIndex(mDate);
            lSearchArguments.setWeekIndex(lWeekIndex - 2);
            LogUtil.Log("weekIndex", lWeekIndex - 2 + "");
            CourseScheduleActivity.start(mActivity, CourseScheduleActivity.LOOK, "", lSearchArguments);
        });

        TextWatcher lTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean lEnabled = !mClassNameTv.getText().equals(getString(R.string.please_choose));
                mSearchBtn.setEnabled(lEnabled);
                if (lEnabled) {
                    mSearchBtn.setBackgroundResource(R.drawable.circlecouner_button_green_two_selector);
                } else {
                    mSearchBtn.setBackgroundResource(R.drawable.btn_background_blue_two_selector);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        mClassNameTv.addTextChangedListener(lTextWatcher);

        if (mType.equals(TEACHER)) {
            try {
                UserBean lUserBean = AppConfiguration.getInstance().getUserBean();
                mTeacherListBean = new TeacherBean.TeacherListBean();
                mTeacherListBean.setTeacherName(lUserBean.getEchoName());
                mTeacherListBean.setTeacherBId(lUserBean.getUserId());
                mClassNameTv.setText(lUserBean.getEchoName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initView(View view) {
        mSemesterTv = (TextView) view.findViewById(R.id.semester_tv);
        mClassNameTv = (TextView) view.findViewById(R.id.class_name_tv);
        mSearchTypeTv = (TextView) view.findViewById(R.id.search_type_tv);
        mDateTv = (TextView) view.findViewById(R.id.date_tv);
        mSearchBtn = (Button) view.findViewById(R.id.search_schedule_btn);
        mClassLayout = (LinearLayout) view.findViewById(R.id.class_layout);
        mDateLayout = (LinearLayout) view.findViewById(R.id.date_layout);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mType = getArguments().getString(SEARCH_TYPE);
        mDate = new Date();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(ScheduleSearchFragment.this);
        initData();
        loadData();

    }

    private void loadData() {
        presenter.getSemester();
    }

    private void initData() {
        mSearchTypeTv.setText(mType.equals(CLASS) ? getString(R.string.class_name) : getString(R.string.teacher_name));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //教师
        if (resultCode == ChooseTeacherActivity.TEACHER_SCHEDULE_RESULT_CODE && requestCode == CHOOSE_TEACHER_REQUEST_CODE) {
            mTeacherListBean = (TeacherBean.TeacherListBean) data.getExtras().getSerializable(ChooseTeacherActivity.TEACHER_BEAN);
            if (mTeacherListBean != null) {
                mClassNameTv.setText(mTeacherListBean.getTeacherName());
            }

            //日期
        } else if (resultCode == DateChooseActivity.SEARCH_SCHEDULE_DATE_RESULT_CODE && requestCode == SEARCH_SCHEDULE_DATE_REQUEST_CODE) {
            mDate = (Date) data.getExtras().getSerializable(DateChooseActivity.DATE);
            mDateTv.setText(new DateTime(mDate).toString(Constant.DATE_TYPE_01));
        } else if (requestCode == CHOOSE_CLASS_REQUEST_CODE && resultCode == ChooseClassActivity.CHOOSE_CLASS_RESULT_CODE) {
            mClassListBean = (StuClassBean.ClassListBean) data.getExtras().getSerializable(ChooseClassActivity.CLASSLISTBEAN);
            assert mClassListBean != null;
            mClassNameTv.setText(mClassListBean.getClassName());
        }
    }

    @Override
    public void showResult(CurrentSemesterBean semesterBean) {
        mSemesterBean = semesterBean;
        mSemesterTv.setText(semesterBean.getSemesterName());
    }
}
