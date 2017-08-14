package com.gta.zssx.fun.adjustCourse.view.page.v2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
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
import com.gta.zssx.fun.adjustCourse.presenter.CourseApplyDetailPresenter;
import com.gta.zssx.fun.adjustCourse.view.CourseApplyDetailView;
import com.gta.zssx.fun.adjustCourse.view.base.Constant;
import com.gta.zssx.fun.adjustCourse.view.page.ApplySuccessActivity;
import com.gta.zssx.fun.adjustCourse.view.page.CourseScheduleActivity;
import com.gta.zssx.fun.adjustCourse.view.page.TeacherRecommendActivity;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.pub.base.BaseMvpFragment;
import com.gta.zssx.pub.widget.DisableEmojiEditText;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static com.gta.zssx.fun.adjustCourse.deprecated.view.CourseApplyFragment.ADJUST_DATE_REQUEST_CODE;
import static com.gta.zssx.fun.adjustCourse.deprecated.view.CourseApplyFragment.ADJUST_TEACHER_REQUEST_CODE;
import static com.gta.zssx.fun.adjustCourse.deprecated.view.CourseApplyFragment.APPLY_DATE_REQUEST_CODE;
import static com.gta.zssx.fun.adjustCourse.deprecated.view.CourseApplyFragment.GET_TEACHER_BEAN;
import static com.gta.zssx.fun.adjustCourse.deprecated.view.CourseApplyFragment.sType;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2017/3/21.
 * @since 1.0.0
 */
public class CourseApplyDetailFragment extends BaseMvpFragment<CourseApplyDetailView, CourseApplyDetailPresenter>
        implements CourseApplyDetailView, View.OnClickListener {

    @Bind(R.id.apply_teacher_name_tv)
    TextView mApplyNameTv;
    @Bind(R.id.apply_date_tv)
    TextView mApplyDateTv;
    @Bind(R.id.apply_course_section_tv)
    TextView mApplyCourseTv;
    @Bind(R.id.adjust_teacher_name_tv)
    TextView mAdjustNameTv;
    @Bind(R.id.adjust_date_tv)
    TextView mAdjustDateTv;
    @Bind(R.id.leave_count_tv)
    TextView mCountTv;
    @Bind(R.id.adjust_course_section_tv)
    TextView mAdjustCourseTv;
    @Bind(R.id.adjust_image)
    ImageView mImageView;
    @Bind(R.id.adjust_course_schedule_layout)
    LinearLayout mAdjustScheduleLayout;
    @Bind(R.id.remark_et)
    DisableEmojiEditText mRemark;
    @Bind(R.id.week_layout)
    LinearLayout mWeekLayout;
    @Bind(R.id.week_tv)
    TextView mWeekTv;
    @Bind(R.id.course_type_tv)
    TextView mCourseType;
    @Bind(R.id.scrollview)
    ScrollView mScrollView;

    private String mType;
    private boolean isAdjustNameClickable;
    private boolean isAdjustScheduleClickable;
    private Map<String, List<ScheduleBean.SectionBean>> mAppListMap;
    private DateTime mAppleDateTime;
    private int mWeekNum;
    private Map<String, List<ScheduleBean.SectionBean>> mAdjustListMap;
    private TeacherRecommendBean.SameClassTeacherBean mSameClassTeacherBean;
    private DateTime mAdjustDateTime;
    private String mApplyDateStr;
    private String mApplySection;
    private int mApplyCourseId;
    private int mApplyClassId;
    private String mAdjustDateStr;
    private String mAdjustSection;
    private int mAdjustClassId;
    private int mAdjustCourseId;
    private UserBean mUserBean;
    private CurrentSemesterBean mSemesterBean;
    public int mRoomParam;
    public int mOpenCourseType;
    public String mClassIdsStr;
    public int mClassroomId;
    public List<String> mHasTimeCourse;
    public int mNumberTeacher;
    public int mNumberClass;
    public int mNumberCourse;
    public int mAdjustOpenCourseType;
    public int mAdjustNumberClass;
    public int mAdjustNumberCourse;
    public int mAdjustNumberTeacher;
    public StringBuilder mStringBuilder;

    public static CourseApplyDetailFragment newInstance(String type, Map<String,
            List<ScheduleBean.SectionBean>> beanList, DateTime dateTime, int weekNum) {
        CourseApplyDetailFragment fragment = new CourseApplyDetailFragment();
        Bundle lArgs = new Bundle();
        lArgs.putSerializable(CourseScheduleActivity.SCHEDULE, (Serializable) beanList);
        lArgs.putSerializable(CourseScheduleActivity.DATETIME, dateTime);
        lArgs.putString(sType, type);
        lArgs.putInt(CourseScheduleActivity.WEEK_INDEX, weekNum);
        fragment.setArguments(lArgs);
        return fragment;
    }

    public static CourseApplyPageFragment newInstance(Bundle bundle) {
        CourseApplyPageFragment fragment = new CourseApplyPageFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_course_apply;
    }

    @Override
    protected void initData() {
        super.initData();
        try {
            mUserBean = AppConfiguration.getInstance().getUserBean();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mType = getArguments().getString(sType);
        mAppListMap = (Map<String, List<ScheduleBean.SectionBean>>) getArguments().getSerializable(CourseScheduleActivity.SCHEDULE);
        mAppleDateTime = (DateTime) getArguments().getSerializable(CourseScheduleActivity.DATETIME);
        mWeekNum = getArguments().getInt(CourseScheduleActivity.WEEK_INDEX, 1);
    }


    @Override
    protected void initView(View view) {

        //调课或者代课教师
        mCourseType.setText(mType.equals(Constant.COURSE_D) ? getString(R.string.substitute_teachers) : mType.equals(Constant.COURSE_T) ? getString(R.string.adjust_teachers) : getString(R.string.adjust_class));

        mImageView.setImageResource(mType.equals(Constant.COURSE_D) ? R.drawable.character_exchange : R.drawable.exchange);
        mScrollView.post(() -> mScrollView.fullScroll(ScrollView.FOCUS_UP));
        mRemark.setMaxLength(100);

        //代课周次布局
        mWeekLayout.setVisibility(mType.equals(Constant.COURSE_D) ? View.VISIBLE : View.GONE);

        //调课课表布局
        mAdjustScheduleLayout.setVisibility(mType.equals(Constant.COURSE_D) ? View.GONE : View.VISIBLE);

        mApplyNameTv.setText(mUserBean.getEchoName());

        mCountTv.setVisibility(View.VISIBLE);

    }

    @OnTextChanged(R.id.remark_et)
    public void onTextChange(CharSequence text) {
        int lLength = text.toString().length();
        if (mStringBuilder == null) {
            mStringBuilder = new StringBuilder();
        }
        mStringBuilder.delete(0, mStringBuilder.length());
        mStringBuilder.append("剩余").append(100 - lLength).append("字");
        mCountTv.setText(mStringBuilder);
    }


    @Override
    protected void requestData() {
        super.requestData();
        presenter.getSemester();
    }


    @NonNull
    @Override
    public CourseApplyDetailPresenter createPresenter() {
        return new CourseApplyDetailPresenter();
    }

    @OnClick({R.id.apply_btn, R.id.adjust_course_choose_teacher_layout, R.id.adjust_course_schedule_layout,
            R.id.apply_course_layout})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //提交申请
            case R.id.apply_btn:
                if (!NetworkUtils.isConnected()) {
                    showInfo("提交失败，无法连接网络");
                    return;
                }
                //代课申请提交
                if (mType.equals(Constant.COURSE_D)) {
                    if (!isAdjustNameClickable || mAdjustNameTv.getText().toString().isEmpty()) {
                        showInfo(getString(R.string.write_complete));
                        return;
                    }
                    ReplaceCourseBean lReplaceCourseBean = getReplaceCourseBean();
                    presenter.applyCourseReplace(lReplaceCourseBean);
                } else {
                    //调时间、调老师申请提交
                    if (!isAdjustScheduleClickable || mAdjustCourseTv.getText().toString().isEmpty()) {
                        showInfo(getString(R.string.write_complete));
                        return;
                    }
                    AdjustCourseBean lAdjustCourseBean = getAdjustCourseBean();
                    presenter.applyCourseAdjust(lAdjustCourseBean);
                }
                break;
            //调代课时选择教师
            case R.id.adjust_course_choose_teacher_layout:
                //调课教师
                if (mType.equals(Constant.COURSE_T)) {
                    if (!isAdjustNameClickable) {
                        showInfo(getString(R.string.write_apply_course_first));
                        return;
                    }
                    ChooseAdjustTeacher();
                    //代课教师
                } else if (mType.equals(Constant.COURSE_D)) {
                    if (!isAdjustNameClickable) {
                        showInfo(getString(R.string.write_apply_course_first));
                        return;
                    }
                    ChooseAdjustTeacher();
                }
                break;
            //调课课程表
            case R.id.adjust_course_schedule_layout:
                if (mSemesterBean == null) {
                    showInfo(getString(R.string.server_error_message));
                    return;
                }
                if (mType.equals(Constant.COURSE_S)) {
                    if (mAppListMap == null) {
                        showInfo(getString(R.string.write_apply_course_first));
                        return;
                    }
                    Intent lIntent = new Intent(mActivity, ClassScheduleActivity.class);
                    Bundle lBundle = new Bundle();
                    SearchArguments lSearchArguments = new SearchArguments();
                    lSearchArguments.setTeacherUUId(mUserBean.getUserId());
                    lSearchArguments.setSemesterId(mSemesterBean.getSemester());
                    lSearchArguments.setDate(mApplyDateStr);
                    lSearchArguments.setClassId(mClassIdsStr.isEmpty() ? mApplyClassId + "" : mClassIdsStr);
                    lSearchArguments.setRoomId(mClassroomId);
                    lSearchArguments.setRoomParams(mRoomParam);
                    lSearchArguments.setScheduleName(mUserBean.getEchoName());
                    lSearchArguments.setCourseType(mOpenCourseType);
                    lSearchArguments.setCourseCount(mAppListMap.size());
                    lBundle.putSerializable(CourseScheduleActivity.SEARCH_ARGUMENT, lSearchArguments);
                    lIntent.putExtras(lBundle);
                    startActivityForResult(lIntent, ClassScheduleActivity.REQUEST_CODE);
                } else {
                    goToOtherSchedule();
                }
                break;
            //申请人课表
            case R.id.apply_course_layout:
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
                break;
        }

    }

    /**
     * 去被调课教师的课程表
     */
    private void goToOtherSchedule() {
        if (!isAdjustScheduleClickable) {
            showInfo(getString(R.string.write_teacher_first));
            return;
        }

        if (mSameClassTeacherBean == null) {
            showInfo(getString(R.string.write_teacher_first));
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

    /**
     * 选择调课教师
     */
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

        String type = mType.equals(Constant.COURSE_D) ? "D" : "T";
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

    /**
     * @return 生成代课实体类，进行网络提交
     */
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
        lReplaceCourseBean.setApplyOpenCourseType(mOpenCourseType);
        lReplaceCourseBean.setApplyNumberTeacher(mNumberTeacher);
        lReplaceCourseBean.setApplyNumberClass(mNumberClass);
        lReplaceCourseBean.setApplyNumberCourse(mNumberCourse);
        return lReplaceCourseBean;
    }

    /**
     * @return 生成调课实体类，进行网络提交
     */
    @NonNull
    private AdjustCourseBean getAdjustCourseBean() {
        AdjustCourseBean lAdjustCourseBean = new AdjustCourseBean();
        lAdjustCourseBean.setUserId(mUserBean.getUserId());
        lAdjustCourseBean.setSemesterCurrent(mSemesterBean.getSemester());
        lAdjustCourseBean.setApplyTechId(mUserBean.getUserId());
        lAdjustCourseBean.setApplyCourseId(mApplyCourseId + "");
        lAdjustCourseBean.setApplyClassId(mApplyClassId + "");
        lAdjustCourseBean.setApplyDate(mApplyDateStr);
        lAdjustCourseBean.setApplyOpenCourseType(mOpenCourseType);
        lAdjustCourseBean.setApplyNumberTeacher(mNumberTeacher);
        lAdjustCourseBean.setApplyNumberClass(mNumberClass);
        lAdjustCourseBean.setApplyNumberCourse(mNumberCourse);
        lAdjustCourseBean.setApplyPolysyllabicWordStr(mApplySection);
        lAdjustCourseBean.setApplyCause(mRemark.getText().toString());

        if (mType.equals(Constant.COURSE_T)) {
            lAdjustCourseBean.setApplyType("T");
            lAdjustCourseBean.setAdjustNumberClass(mAdjustNumberClass);
            lAdjustCourseBean.setAdjustNumberCourse(mAdjustNumberCourse);
            lAdjustCourseBean.setAdjustNumberTeacher(mAdjustNumberTeacher);
            lAdjustCourseBean.setAdjustOpenCourseType(mAdjustOpenCourseType);

            lAdjustCourseBean.setAdjustTechId(mSameClassTeacherBean.getId());
            lAdjustCourseBean.setAdjustDate(mAdjustDateStr);
            lAdjustCourseBean.setAdjustPolysyllabicWordStr(mAdjustSection);
            lAdjustCourseBean.setAdjustClassId(mAdjustClassId + "");
            lAdjustCourseBean.setAdjustCourseId(mAdjustCourseId + "");
        } else {
            lAdjustCourseBean.setApplyType("H");
            lAdjustCourseBean.setAdjustDate(mAdjustDateStr);
            lAdjustCourseBean.setAdjustPolysyllabicWordStr(mAdjustSection);
        }
        return lAdjustCourseBean;
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
            //被调课老师的课程
        } else if (requestCode == ADJUST_DATE_REQUEST_CODE && resultCode == CourseScheduleActivity.APPLY_AND_ADJUST_RESULT_CODE) {
            mAdjustListMap = (Map<String, List<ScheduleBean.SectionBean>>) data.getExtras().getSerializable(CourseScheduleActivity.SCHEDULE);
            mAdjustDateTime = (DateTime) data.getExtras().getSerializable(CourseScheduleActivity.DATETIME);
            mAdjustListMap = presenter.sortMap(mAdjustListMap);

            setAdjustCourse();

        } else if (requestCode == ClassScheduleActivity.REQUEST_CODE && resultCode == ClassScheduleActivity.RESULT_CODE) {
            mHasTimeCourse = (List<String>) data.getExtras().getSerializable(ClassScheduleActivity.SELECT_COURSE);
            mAdjustDateTime = (DateTime) data.getExtras().getSerializable(ClassScheduleActivity.SELECT_DAY);
            isAdjustScheduleClickable = true;
            mAdjustDateStr = mAdjustDateTime.toString("yyyy-MM-dd");
            Date lDate = mAdjustDateTime.toDate();
            String lWeek = TimeUtils.getWeek(lDate);
            mAdjustDateTv.setText(mAdjustDateStr + " " + lWeek);
            String lAdjustSection = "";
            mAdjustSection = "";
            for (int i = 0; i < mHasTimeCourse.size(); i++) {
                String lS = mHasTimeCourse.get(i);
                String[] lSplit = lS.split(Pattern.quote(":"));
                lAdjustSection += lSplit[1];
                mAdjustSection += lSplit[1];
                if (i != mHasTimeCourse.size() - 1) {
                    lAdjustSection += "、";
                    mAdjustSection += ",";
                }
            }

            mAdjustCourseTv.setText("第" + lAdjustSection + "节  " + "无课");

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void sortApplyCourse() {
        TreeMap<String, List<ScheduleBean.SectionBean>> listTreeMap = new TreeMap<>((o1, o2) -> {
            int left = Integer.parseInt(String.valueOf(o1.charAt(0)));
            int right = Integer.parseInt(String.valueOf(o2.charAt(0)));
            if (left > right)
                return 1;
            return -1;
        });
        listTreeMap.putAll(mAppListMap);
        mAppListMap = listTreeMap;
    }

    private void sortAdjustCourse() {
        TreeMap<String, List<ScheduleBean.SectionBean>> listTreeMap = new TreeMap<>((o1, o2) -> {
            int left = Integer.parseInt(String.valueOf(o1.charAt(0)));
            int right = Integer.parseInt(String.valueOf(o2.charAt(0)));
            if (left > right)
                return 1;
            return -1;
        });
        listTreeMap.putAll(mAdjustListMap);
        mAdjustListMap = listTreeMap;
    }

    /**
     * 设置被调课教师的课程信息
     */
    private void setAdjustCourse() {
        sortAdjustCourse();
        if (mAdjustDateTime != null) {
            mAdjustDateStr = mAdjustDateTime.toString("yyyy-MM-dd");
            Date lDate = mAdjustDateTime.toDate();
            String lWeek = TimeUtils.getWeek(lDate);
            mAdjustDateTv.setText(mAdjustDateStr + " " + lWeek);
        }

        StringBuilder lBuilder = new StringBuilder();
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
                mAdjustOpenCourseType = lSectionBean.getOpenCourseType();
                mAdjustNumberClass = lSectionBean.getNumberClass();
                mAdjustNumberCourse = lSectionBean.getNumberCourse();
                mAdjustNumberTeacher = lSectionBean.getNumberTeacher();
                String lCourseName = lSectionBean.getCourseName();
                String lClassName = lSectionBean.getClassName();
                lBuilder.append("第").append(String.valueOf(lC)).append("节 ").append(lCourseName).append(" (").append(lClassName).append(")");
                if (it.hasNext()) {
                    lBuilder.append("\n");
                }
            }

            mAdjustSection += String.valueOf(lC);
            if (it.hasNext()) {
                mAdjustSection += ",";
            }
        }
        mAdjustCourseTv.setText(lBuilder.toString());
    }

    /**
     * 设置申请人的课程信息
     */
    private void setApplyCourse() {
        sortApplyCourse();
        if (mAppleDateTime != null) {
            mApplyDateStr = mAppleDateTime.toString("yyyy-MM-dd");
            Date lDate = mAppleDateTime.toDate();
            String lWeek = TimeUtils.getWeek(lDate);
            mApplyDateTv.setText(mApplyDateStr + " " + lWeek);
        }

        if (mType.equals(Constant.COURSE_D)) {
            mWeekTv.setText("代课" + mWeekNum + "周");
        }

        mApplySection = "";
        String lClassName = "";

        StringBuilder lBuilder = new StringBuilder();

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
                lClassName = lSectionBean.getClassName();
                lBuilder.append("第").append(String.valueOf(lC)).append("节 ").append(lCourseName).append(" (").append(lClassName).append(")");
                if (it.hasNext()) {
                    lBuilder.append("\n");
                }
                mRoomParam = lSectionBean.getRoomParam();
                mOpenCourseType = lSectionBean.getOpenCourseType();
                mClassIdsStr = lSectionBean.getClassIdsStr();
                mClassroomId = lSectionBean.getClassroomId();
                mNumberTeacher = lSectionBean.getNumberTeacher();
                mNumberClass = lSectionBean.getNumberClass();
                mNumberCourse = lSectionBean.getNumberCourse();
            }

            mApplySection += String.valueOf(lC);
            if (it.hasNext()) {
                mApplySection += ",";
            }
        }
        mApplyCourseTv.setText(lBuilder.toString());
        if (mType.equals(Constant.COURSE_S)) {
            mAdjustNameTv.setText(lClassName);
        }
        isAdjustNameClickable = true;
    }

    @Override
    public void showSemester(CurrentSemesterBean semesterBean) {
        mSemesterBean = semesterBean;
    }

    @Override
    public void replaceCourseSuccess(ApplySuccessBean applySuccessBean) {
        showInfo(getString(R.string.apply_success));
        applySuccessBean.setType(mType);
        ApplySuccessActivity.start(mActivity, applySuccessBean);
        getActivity().finish();
    }

    @Override
    public void AdjustCourseSuccess(ApplySuccessBean applySuccessBean) {
        showInfo(getString(R.string.apply_success));
        if (applySuccessBean == null) {
            applySuccessBean = new ApplySuccessBean();
        }
        applySuccessBean.setType(mType);
        ApplySuccessActivity.start(mActivity, applySuccessBean);
        getActivity().finish();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mAppListMap != null) {
            setApplyCourse();
        }
    }
}
