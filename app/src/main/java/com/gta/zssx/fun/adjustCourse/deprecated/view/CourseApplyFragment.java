package com.gta.zssx.fun.adjustCourse.deprecated.view;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.R;
import com.gta.zssx.fun.adjustCourse.model.bean.AdjustCourseBean;
import com.gta.zssx.fun.adjustCourse.model.bean.ApplySuccessBean;
import com.gta.zssx.fun.adjustCourse.model.bean.CurrentSemesterBean;
import com.gta.zssx.fun.adjustCourse.model.bean.GetTeacherBean;
import com.gta.zssx.fun.adjustCourse.model.bean.ReplaceCourseBean;
import com.gta.zssx.fun.adjustCourse.model.bean.ScheduleBean;
import com.gta.zssx.fun.adjustCourse.model.bean.SearchArguments;
import com.gta.zssx.fun.adjustCourse.model.bean.TeacherRecommendBean;
import com.gta.zssx.fun.adjustCourse.deprecated.presenter.CourseApplyPresenter;
import com.gta.zssx.fun.adjustCourse.view.CourseApplyView;
import com.gta.zssx.fun.adjustCourse.view.page.ApplySuccessActivity;
import com.gta.zssx.fun.adjustCourse.view.page.CourseScheduleActivity;
import com.gta.zssx.fun.adjustCourse.view.page.TeacherRecommendActivity;
import com.gta.zssx.fun.adjustCourse.view.page.v2.ClassScheduleActivity;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.pub.base.BaseFragment;
import com.gta.zssx.pub.widget.DisableEmojiEditText;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/10/25.
 * @since 1.0.0
 */
@Deprecated
public class CourseApplyFragment extends BaseFragment<CourseApplyView, CourseApplyPresenter>
        implements CourseApplyView {


    //调课类型
    public static final String COURSE_T = "course_t";
    //代课类型
    public static final String COURSE_D = "course_d";
    //用于课程表界面判断是否从调代课详情进入
    public static final String COURSE_N = "course_n";
    //调时间
    public static final String COURSE_S = "course_s";

    public static final String GET_TEACHER_BEAN = "getTeacherBean";
    public static final int APPLY_DATE_REQUEST_CODE = 102;
    public static final int ADJUST_DATE_REQUEST_CODE = 106;
    public static final int ADJUST_TEACHER_REQUEST_CODE = 104;
    public static String sType = "type";
    private Button mApplyBt;
    private TextView mApplyNameTv;
    private TextView mApplyDateTv;
    private TextView mApplyCourseTv;
    private TextView mAdjustNameTv;
    private TextView mAdjustDateTv;
    private TextView mAdjustCourseTv;
    private DisableEmojiEditText mRemark;
    private LinearLayout mAdjustScheduleLayout;
    private String mType;
    private ImageView mImageView;
    private LinearLayout mApplyCourseLayout;
    private UserBean mUserBean;
    private Map<String, List<ScheduleBean.SectionBean>> mAppListMap;
    private TeacherRecommendBean.SameClassTeacherBean mSameClassTeacherBean;
    private DateTime mAppleDateTime;
    private boolean isAdjustNameClickable;
    private boolean isAdjustScheduleClickable;
    private Map<String, List<ScheduleBean.SectionBean>> mAdjustListMap;
    private DateTime mAdjustDateTime;
    public CurrentSemesterBean mSemesterBean;
    public String mApplyDateStr;
    private TextView mCountTv;
    public String mApplySection;
    public String mAdjustSection;
    public int mApplyCourseId;
    public int mApplyClassId;
    public int mAdjustClassId;
    public int mAdjustCourseId;
    public int mWeekNum;
    private LinearLayout mChooseTeacherLayout;
    public String mAdjustDateStr;
    private LinearLayout mWeekLayout;
    private TextView mWeekTv;
    private TextView mCourseType;
    private ScrollView mScrollView;
    private TextView mDescribeTv;
    private TextView mSwitchTv;
    private LinearLayout mSwitchLayout;
    private boolean isCourseExchange;

    @NonNull
    @Override
    public CourseApplyPresenter createPresenter() {
        return new CourseApplyPresenter(mActivity);
    }

    public CourseApplyFragment() {

    }

    public static CourseApplyFragment newInstance(String type, Map<String,
            List<ScheduleBean.SectionBean>> beanList, DateTime dateTime, int weekNum) {
        CourseApplyFragment fragment = new CourseApplyFragment();
        Bundle lArgs = new Bundle();
        lArgs.putSerializable(CourseScheduleActivity.SCHEDULE, (Serializable) beanList);
        lArgs.putSerializable(CourseScheduleActivity.DATETIME, dateTime);
        lArgs.putString(sType, type);
        lArgs.putInt(CourseScheduleActivity.WEEK_INDEX, weekNum);
        fragment.setArguments(lArgs);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_course_apply, container, false);
        initView(view);
        setInteractListener();
        return view;
    }

    private void setInteractListener() {

        mCourseType.setText(mType.equals(COURSE_D) ? "代课教师" : "调课教师");

        mSwitchLayout.setVisibility(mType.equals(COURSE_T) ? View.VISIBLE : View.GONE);

        mScrollView.post(() -> mScrollView.fullScroll(ScrollView.FOCUS_UP));

        mImageView.setImageResource(mType.equals(COURSE_T) ? R.drawable.exchange : R.drawable.character_exchange);

        mRemark.setMaxLength(100);

        mWeekLayout.setVisibility(mType.equals(COURSE_D) ? View.VISIBLE : View.GONE);
        //提交申请
        mApplyBt.setOnClickListener(v -> {

            if (!NetworkUtils.isConnected()) {
                showInfo("提交失败，无法连接网络");
                return;
            }
            /**
             * 代课申请
             */
            if (mType.equals(COURSE_D)) {
                if (!isAdjustNameClickable || mAdjustNameTv.getText().toString().isEmpty()) {
                    showInfo("请填写完整内容");
                    return;
                }
                ReplaceCourseBean lReplaceCourseBean = getReplaceCourseBean();
                presenter.applyCourseReplace(lReplaceCourseBean);
            } else {
                /**
                 * 调课申请
                 */
                if (!isAdjustScheduleClickable || mAdjustCourseTv.getText().toString().isEmpty()) {
                    showInfo("请填写完整内容");
                    return;
                }
                AdjustCourseBean lAdjustCourseBean = getAdjustCourseBean();
                presenter.applyCourseAdjust(lAdjustCourseBean);
            }

        });
        //去选择调代课教师
        mChooseTeacherLayout.setOnClickListener(v -> {
            if (mType.equals(COURSE_T)) {
                if (!isCourseExchange) {
                    if (!isAdjustNameClickable) {
                        showInfo("请先选择申请人课程");
                        return;
                    }
                    ChooseAdjustTeacher();
                }
            } else {
                if (!isAdjustNameClickable) {
                    showInfo("请先选择申请人课程");
                    return;
                }
                ChooseAdjustTeacher();
            }

        });

        //去调代课课表选择课程
        mAdjustScheduleLayout.setOnClickListener(v -> {
            if (mSemesterBean == null) {
                showInfo(getString(R.string.server_error_message));
                return;
            }
            if (isCourseExchange) {
                if (mAppListMap == null) {
                    showInfo("请先填写申请课程");
                    return;
                }
                Intent lIntent = new Intent(mActivity, ClassScheduleActivity.class);
                startActivity(lIntent);
            } else {
                goToOtherSchedule();
            }

        });

        //去申请人课程表
        mApplyCourseLayout.setOnClickListener(v -> {
            if (mSemesterBean == null) {
                showInfo(getString(R.string.server_error_message));
                return;
            }
            Intent lIntent = new Intent(mActivity, CourseScheduleActivity.class);
            Bundle lBundle = new Bundle();
            SearchArguments lSearchArguments = new SearchArguments();
            lSearchArguments.setTeacherUUId(mUserBean.getUserId());
            lSearchArguments.setFlag(CourseScheduleActivity.TEACHER_SCHEDULE);
            lSearchArguments.setSemesterId(mSemesterBean.getSemester());
            lSearchArguments.setDate(mAppleDateTime == null ? DateTime.now().toString("yyyy-MM-dd") : mAppleDateTime.toString("yyyy-MM-dd"));
            if (mApplyDateStr == null) {
                lSearchArguments.setWeekIndex(TimeUtils.getWeekIndex(new Date()) - 2);
            } else {
                lSearchArguments.setWeekIndex(TimeUtils.getWeekIndex(new DateTime(mApplyDateStr).toDate()) - 2);
            }
            lSearchArguments.setScheduleName(mUserBean.getEchoName());
            lSearchArguments.setFromSearch(false);
            lSearchArguments.setCourseCount(0);
            lBundle.putSerializable(CourseScheduleActivity.SEARCH_ARGUMENT, lSearchArguments);
            lIntent.putExtra(CourseScheduleActivity.SCHEDULE_TYPE, CourseScheduleActivity.CLICKABLE);
            lIntent.putExtra(CourseScheduleActivity.COURSE_TYPE, mType);
            lIntent.putExtras(lBundle);
            this.startActivityForResult(lIntent, APPLY_DATE_REQUEST_CODE);
        });

        mAdjustScheduleLayout.setVisibility(mType.equals(COURSE_D) ? View.GONE : View.VISIBLE);

        mApplyNameTv.setText(mUserBean.getEchoName());

        mCountTv.setVisibility(View.VISIBLE);

        TextWatcher lTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int lLength = s.toString().length();
                mCountTv.setText("剩余" + (100 - lLength) + "字");
            }
        };

        mRemark.addTextChangedListener(lTextWatcher);

        mSwitchTv.setOnClickListener(v -> {
            isCourseExchange = !isCourseExchange;
            mCourseType.setText(isCourseExchange ? "被调课班级" : "被调课教师");
        });
    }

    private void goToOtherSchedule() {
        if (!isAdjustScheduleClickable) {
            showInfo("请先选择教师");
            return;
        }

        if (mSameClassTeacherBean == null) {
            showInfo("请先选择教师");
            return;
        }
        Intent lIntent = new Intent(mActivity, CourseScheduleActivity.class);
        Bundle lBundle = new Bundle();
        SearchArguments lSearchArguments = new SearchArguments();
        lSearchArguments.setTeacherUUId(mSameClassTeacherBean.getTeacherBId());
        lSearchArguments.setSemesterId(mSemesterBean.getSemester());
        lSearchArguments.setFlag(CourseScheduleActivity.TEACHER_SCHEDULE);
        lSearchArguments.setDate(mApplyDateStr);
        if (mAdjustDateStr == null) {
            lSearchArguments.setWeekIndex(TimeUtils.getWeekIndex(new DateTime(mApplyDateStr).toDate()) - 2);
        } else {
            lSearchArguments.setWeekIndex(TimeUtils.getWeekIndex(new DateTime(mAdjustDateStr).toDate()) - 2);
        }
        lSearchArguments.setScheduleName(mSameClassTeacherBean.getName());
        lSearchArguments.setFromSearch(false);
        lSearchArguments.setCourseCount(mAppListMap.size());
        lBundle.putSerializable(CourseScheduleActivity.SEARCH_ARGUMENT, lSearchArguments);
        lIntent.putExtra(CourseScheduleActivity.SCHEDULE_TYPE, CourseScheduleActivity.CLICKABLE);
        lIntent.putExtra(CourseScheduleActivity.COURSE_TYPE, mType);
        lIntent.putExtras(lBundle);
        this.startActivityForResult(lIntent, ADJUST_DATE_REQUEST_CODE);
    }

    @NonNull
    private ReplaceCourseBean getReplaceCourseBean() {
        ReplaceCourseBean lReplaceCourseBean = new ReplaceCourseBean();
        lReplaceCourseBean.setAdjustTechId(mSameClassTeacherBean.getId());
        lReplaceCourseBean.setApplyTechId(mUserBean.getUserId());
        lReplaceCourseBean.setApplyCourseId(mApplyCourseId + "");
        lReplaceCourseBean.setApplyClassId(mApplyClassId + "");
        lReplaceCourseBean.setSerialWeekNum(mWeekNum);
        lReplaceCourseBean.setApplyType("D");
        lReplaceCourseBean.setSemesterCurrent(mSemesterBean.getSemester());
        lReplaceCourseBean.setUserId(mUserBean.getUserId());
        lReplaceCourseBean.setApplyDate(mApplyDateStr);
        lReplaceCourseBean.setAdjustDate(mApplyDateStr);
        lReplaceCourseBean.setApplyPolysyllabicWordStr(mApplySection);
        lReplaceCourseBean.setApplyCause(mRemark.getText().toString());
        return lReplaceCourseBean;
    }

    @NonNull
    private AdjustCourseBean getAdjustCourseBean() {
        AdjustCourseBean lAdjustCourseBean = new AdjustCourseBean();
        lAdjustCourseBean.setAdjustTechId(mSameClassTeacherBean.getId());
        lAdjustCourseBean.setApplyTechId(mUserBean.getUserId());
        lAdjustCourseBean.setApplyCourseId(mApplyCourseId + "");
        lAdjustCourseBean.setApplyClassId(mApplyClassId + "");
        lAdjustCourseBean.setApplyDate(mApplyDateStr);
        lAdjustCourseBean.setAdjustDate(mAdjustDateStr);
        lAdjustCourseBean.setApplyType("T");
        lAdjustCourseBean.setApplyCause(mRemark.getText().toString());
        lAdjustCourseBean.setAdjustPolysyllabicWordStr(mAdjustSection);
        lAdjustCourseBean.setApplyPolysyllabicWordStr(mApplySection);
        lAdjustCourseBean.setSemesterCurrent(mSemesterBean.getSemester());
        lAdjustCourseBean.setUserId(mUserBean.getUserId());
        lAdjustCourseBean.setAdjustClassId(mAdjustClassId + "");
        lAdjustCourseBean.setAdjustCourseId(mAdjustCourseId + "");
        return lAdjustCourseBean;
    }

    private void ChooseAdjustTeacher() {
        String section = "";
        List<ScheduleBean.SectionBean> lValue = null;
        Iterator<Map.Entry<String, List<ScheduleBean.SectionBean>>> it = mAppListMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, List<ScheduleBean.SectionBean>> entry = it.next();
            String lKey = entry.getKey();
            char lC = lKey.charAt(0);
            section += String.valueOf(lC);
            if (it.hasNext()) {
                section += ",";
            }
            if (!it.hasNext()) {
                lValue = entry.getValue();
            }
        }
        int lClassId = 0;
        String lTeacherID = null;
        if (lValue != null && lValue.size() > 0) {
            ScheduleBean.SectionBean lSectionBean = lValue.get(0);
            lClassId = lSectionBean.getClassId();
            lTeacherID = lSectionBean.getTeacherID();
        }

        String type = mType.equals(COURSE_D) ? "D" : "T";
        String lBasePath = mAppleDateTime.toString("yyyy-MM-dd");
        GetTeacherBean lTeacherBean = new GetTeacherBean();
        lTeacherBean.setApplyType(type);
        lTeacherBean.setApplyClassId(lClassId);
        lTeacherBean.setApplyTechBId(mUserBean.getUserId());
        lTeacherBean.setApplyTechId(Integer.valueOf(lTeacherID));
        lTeacherBean.setDate(lBasePath);
        lTeacherBean.setSelectUnitApply(section);
        lTeacherBean.setNumberTeacher(1);
        lTeacherBean.setDeptId(1);
        Intent lIntent = new Intent(mActivity, TeacherRecommendActivity.class);
        Bundle lBundle = new Bundle();
        lBundle.putSerializable(GET_TEACHER_BEAN, lTeacherBean);
        lIntent.putExtras(lBundle);
        this.startActivityForResult(lIntent, ADJUST_TEACHER_REQUEST_CODE);
    }

    private void initView(View view) {

        mApplyNameTv = (TextView) view.findViewById(R.id.apply_teacher_name_tv);
        mApplyDateTv = (TextView) view.findViewById(R.id.apply_date_tv);
        mApplyCourseTv = (TextView) view.findViewById(R.id.apply_course_section_tv);
        mAdjustNameTv = (TextView) view.findViewById(R.id.adjust_teacher_name_tv);
        mAdjustDateTv = (TextView) view.findViewById(R.id.adjust_date_tv);
        mDescribeTv = (TextView) view.findViewById(R.id.describe_tv);
        mSwitchTv = (TextView) view.findViewById(R.id.switch_tv);
        mCountTv = (TextView) view.findViewById(R.id.leave_count_tv);
        mAdjustCourseTv = (TextView) view.findViewById(R.id.adjust_course_section_tv);
        mImageView = (ImageView) view.findViewById(R.id.adjust_image);
        mAdjustScheduleLayout = (LinearLayout) view.findViewById(R.id.adjust_course_schedule_layout);
        mApplyCourseLayout = (LinearLayout) view.findViewById(R.id.apply_course_layout);
        mSwitchLayout = (LinearLayout) view.findViewById(R.id.switch_layout);
        mChooseTeacherLayout = (LinearLayout) view.findViewById(R.id.adjust_course_choose_teacher_layout);
        mRemark = (DisableEmojiEditText) view.findViewById(R.id.remark_et);
        mApplyBt = (Button) view.findViewById(R.id.apply_btn);
        mWeekLayout = (LinearLayout) view.findViewById(R.id.week_layout);
        mWeekTv = (TextView) view.findViewById(R.id.week_tv);
        mCourseType = (TextView) view.findViewById(R.id.course_type_tv);
        mScrollView = (ScrollView) view.findViewById(R.id.scrollview);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        //申请人课程
        if (resultCode == CourseScheduleActivity.APPLY_AND_ADJUST_RESULT_CODE && requestCode == APPLY_DATE_REQUEST_CODE) {
            //当再次选择课程时，重置调代课教师和日期课程等
            mAdjustNameTv.setText("");
            mAdjustDateTv.setText("");
            mAdjustCourseTv.setText("");
            isAdjustScheduleClickable = false;
            mAppListMap = (Map<String, List<ScheduleBean.SectionBean>>) data.getExtras().getSerializable(CourseScheduleActivity.SCHEDULE);
            mAppListMap = presenter.sortMap(mAppListMap);
            mAppleDateTime = (DateTime) data.getExtras().getSerializable(CourseScheduleActivity.DATETIME);
            mWeekNum = data.getExtras().getInt(CourseScheduleActivity.WEEK_INDEX);
            setApplyCourse();

            //被调代课老师名称
        } else if (resultCode == TeacherRecommendActivity.ADJUST_TEACHER_RESULT_CODE && requestCode == ADJUST_TEACHER_REQUEST_CODE) {
            //当再次选择调课教师时，重置调课课程
            mAdjustDateTv.setText("");
            mAdjustCourseTv.setText("");
            isAdjustScheduleClickable = true;
            mSameClassTeacherBean = (TeacherRecommendBean.SameClassTeacherBean) data.getExtras().getSerializable(TeacherRecommendActivity.TEACHER_BEAN);
            mAdjustNameTv.setText(mSameClassTeacherBean.getName());
            //被调代课老师的课程
        } else if (requestCode == ADJUST_DATE_REQUEST_CODE && resultCode == CourseScheduleActivity.APPLY_AND_ADJUST_RESULT_CODE) {
            mAdjustListMap = (Map<String, List<ScheduleBean.SectionBean>>) data.getExtras().getSerializable(CourseScheduleActivity.SCHEDULE);
            mAdjustDateTime = (DateTime) data.getExtras().getSerializable(CourseScheduleActivity.DATETIME);
            mAdjustListMap = presenter.sortMap(mAdjustListMap);
            setAdjustCourse();

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void setAdjustCourse() {
        if (mAdjustDateTime != null) {
            mAdjustDateStr = mAdjustDateTime.toString("yyyy-MM-dd");
            Date lDate = mAdjustDateTime.toDate();
            String lWeek = TimeUtils.getWeek(lDate);
            mAdjustDateTv.setText(mAdjustDateStr + " " + lWeek);
        }

        String lAdjustSection = "";
        mAdjustSection = "";
        Iterator<Map.Entry<String, List<ScheduleBean.SectionBean>>> it = mAdjustListMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, List<ScheduleBean.SectionBean>> entry = it.next();
            String lKey = entry.getKey();
            List<ScheduleBean.SectionBean> lValue = entry.getValue();
            char lC = lKey.charAt(0);
            if (lValue != null && lValue.size() > 0) {
                ScheduleBean.SectionBean lSectionBean = lValue.get(0);
                mAdjustClassId = lSectionBean.getClassId();
                mAdjustCourseId = lSectionBean.getCourseId();
                String lCourseName = lSectionBean.getCourseName();
                String lClassName = lSectionBean.getClassName();
                lAdjustSection += "第" + String.valueOf(lC) + "节 " + lCourseName + " (" + lClassName + ")";
                if (it.hasNext()) {
                    lAdjustSection += "\n";
                }
            }

            mAdjustSection += String.valueOf(lC);
            if (it.hasNext()) {
                mAdjustSection += ",";
            }
        }
        mAdjustCourseTv.setText(lAdjustSection);
    }

    private void setApplyCourse() {
        if (mAppleDateTime != null) {
            mApplyDateStr = mAppleDateTime.toString("yyyy-MM-dd");
            Date lDate = mAppleDateTime.toDate();
            String lWeek = TimeUtils.getWeek(lDate);
            mApplyDateTv.setText(mApplyDateStr + " " + lWeek);
        }

        if (mType.equals(COURSE_D)) {
            mWeekTv.setText("代课" + mWeekNum + "周");
        }

        String lApplySection = "";
        mApplySection = "";


        Iterator<Map.Entry<String, List<ScheduleBean.SectionBean>>> it = mAppListMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, List<ScheduleBean.SectionBean>> entry = it.next();
            String lKey = entry.getKey();
            List<ScheduleBean.SectionBean> lValue = entry.getValue();
            char lC = lKey.charAt(0);
            if (lValue != null && lValue.size() > 0) {
                ScheduleBean.SectionBean lSectionBean = lValue.get(0);
                mApplyCourseId = lSectionBean.getCourseId();
                mApplyClassId = lSectionBean.getClassId();
                String lCourseName = lSectionBean.getCourseName();
                String lClassName = lSectionBean.getClassName();
                lApplySection += "第" + String.valueOf(lC) + "节 " + lCourseName + " (" + lClassName + ")";
                if (it.hasNext()) {
                    lApplySection += "\n";
                }
            }

            mApplySection += String.valueOf(lC);
            if (it.hasNext()) {
                mApplySection += ",";
            }
        }
        mApplyCourseTv.setText(lApplySection);
        isAdjustNameClickable = true;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mType = getArguments().getString(sType);
        mAppListMap = (Map<String, List<ScheduleBean.SectionBean>>) getArguments().getSerializable(CourseScheduleActivity.SCHEDULE);
        mAppleDateTime = (DateTime) getArguments().getSerializable(CourseScheduleActivity.DATETIME);
        mWeekNum = getArguments().getInt(CourseScheduleActivity.WEEK_INDEX, 1);
        try {
            mUserBean = AppConfiguration.getInstance().getUserBean();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(CourseApplyFragment.this);
        initData();
        if (mAppListMap != null) {
            setData();
        }
    }

    private void setData() {
        setApplyCourse();
    }

    private void initData() {
        presenter.getSemester();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView(false);
    }

    @Override
    public void showSemester(CurrentSemesterBean semesterBean) {
        mSemesterBean = semesterBean;

    }

    @Override
    public void replaceCourseSuccess(ApplySuccessBean s) {
        showInfo("申请成功");
        ApplySuccessActivity.start(mActivity, s);
    }

    @Override
    public void AdjustCourseSuccess(ApplySuccessBean s) {
        showInfo("申请成功");
        ApplySuccessActivity.start(mActivity, s);
    }
}
