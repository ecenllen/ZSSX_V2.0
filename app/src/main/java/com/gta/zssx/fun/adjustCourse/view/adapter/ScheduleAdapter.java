package com.gta.zssx.fun.adjustCourse.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.fun.adjustCourse.model.bean.ScheduleBean;
import com.gta.zssx.fun.adjustCourse.model.bean.SearchArguments;
import com.gta.zssx.fun.adjustCourse.model.utils.SingleChooseDialog;
import com.gta.zssx.fun.adjustCourse.view.base.Constant;
import com.gta.zssx.fun.adjustCourse.view.page.CourseScheduleActivity;
import com.gta.zssx.pub.util.DensityUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/10/24.
 * @since 1.0.0
 */
public class ScheduleAdapter extends RecyclerView.Adapter {

    /**
     * 星期,表示列数
     */
    private static final int WEEK = 7;
    /**
     * 行数
     */
    private static int COL_NUMBER = 8;

    private String[] mWeek = new String[]{"周一", "周二", "周三", "周四", "周五", "周六", "周日"};


    private Context mContext;
    private int mPosition;
    private ScheduleBean mScheduleBean;
    /**
     * 用于记录选中的position
     */
    private List<Integer> mIntegers;
    //是否可以选择
    private String mScheduleType;
    private Listener mListener;
    //保存选中的节次数据
    private Map<String, List<ScheduleBean.SectionBean>> mSelectedSchedule;
    //用于记录长按的节次
    private String mKey;
    //用于区分班级课表还是教师课表
    private int mFlag;
    private SearchArguments mSearchArguments;
    public String mCourseType;

    public interface Listener {
        void itemClick(Map<String, List<ScheduleBean.SectionBean>> listMap, int column);

        void itemLongClick(List<ScheduleBean.SectionBean> beanList, int column, String key);

        void notifySectionMap();
    }

    /**
     * @param context         上下文
     * @param scheduleBean    课程表数据
     * @param listener        回调
     * @param scheduleType    用于区分课表的课程是否可以被选中
     * @param searchArguments 各种课程表用到的参数
     * @param courseType      区分调时间调教师代课
     */
    public ScheduleAdapter(Context context, ScheduleBean scheduleBean,
                           Listener listener, String scheduleType, SearchArguments searchArguments
            , String courseType) {
        mScheduleType = scheduleType;
        mContext = context;
        mListener = listener;
        mScheduleBean = scheduleBean;
        mIntegers = new ArrayList<>();
        mSelectedSchedule = new HashMap<>();
        mFlag = searchArguments.getFlag();
        COL_NUMBER = scheduleBean.getMaxUnit();
        mKey = searchArguments.getKey();
        mSearchArguments = searchArguments;
        mCourseType = courseType;

        int lWeekIndex = searchArguments.getWeekIndex();
        //周六没课并且选的是周六查询，不处理会导致不展开课程表
        if (!scheduleBean.isIsSaturdayHasCourse() && lWeekIndex >= 5) {
            mPosition = 0;
        } else {
            mPosition = lWeekIndex < 0 ? 0 : lWeekIndex;
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CourseHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_schedule_selected, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CourseHolder lCourseHolder = (CourseHolder) holder;
        iniView(lCourseHolder, position);

        //判断是否为打开状态,将对应控件设置隐藏和显示
        if (mPosition != position) {
            lCourseHolder.weekOpen.setVisibility(View.GONE);
            lCourseHolder.llCourseOpen.setVisibility(View.GONE);
            lCourseHolder.weekColse.setVisibility(View.VISIBLE);
            lCourseHolder.llCourseClose.setVisibility(View.VISIBLE);
//            mSelectedSchedule.clear();
        } else {
            lCourseHolder.llCourseClose.setVisibility(View.GONE);
            lCourseHolder.weekColse.setVisibility(View.GONE);
            lCourseHolder.llCourseOpen.setVisibility(View.VISIBLE);
            lCourseHolder.weekOpen.setVisibility(View.VISIBLE);
            //点回来的时候恢复默认状态
            restoreOpenState(position, lCourseHolder);
        }
    }

    private void restoreOpenState(int position, CourseHolder courseHolder) {
        int lChildCount = courseHolder.llCourseOpen.getChildCount();
        for (int i = 0; i < lChildCount; i++) {
            View lChildAt = courseHolder.llCourseOpen.getChildAt(i);
            String lCurrentKey = getCurrentKey(i, position);
            List<ScheduleBean.SectionBean> lSectionBeen = mScheduleBean.getCourseListMap().get(lCurrentKey);
            if (lSectionBeen != null && lSectionBeen.size() > 0) {
                ScheduleBean.SectionBean lSectionBean = lSectionBeen.get(0);
                String lTransferType = lSectionBean.getTransferType();
                if ("1".equals(lTransferType)) {
                    lChildAt.setBackgroundResource(R.color.yellow_ff9501);
                } else if ("2".equals(lTransferType)) {
                    lChildAt.setBackgroundResource(R.color.gray_999999);
                } else {
                    if (Constant.COURSE_S.equals(mCourseType)) {
                        if (lSectionBeen.get(0).getIsMultiTeacherFlag() == 1) {
                            lChildAt.setBackgroundResource(R.color.gray_999999);
                        } else {
                            lChildAt.setBackgroundResource(R.color.schedule_backgrouond_def);
                        }
                    } else {
                        if (mKey == null && !lCurrentKey.equals(mKey)) {
                            lChildAt.setBackgroundResource(R.color.schedule_backgrouond_def);
                        }
                    }
                }
            }
        }
    }

    private void iniView(CourseHolder holder, final int position) {

        //设置标题
        String[] lDays = mSearchArguments.getDays();
        String lWeek = mWeek[position];
        if (lDays != null) {
            lWeek = lDays[position] + "\n" + lWeek;
        }
        holder.weekOpen.setText(lWeek);
        holder.weekColse.setText(lWeek);
        //列的点击事件
        holder.llCol.setOnClickListener(v -> {
            if (position == mPosition)
                return;
            //把当前列表合上
            notifyItemChanged(mPosition);
            //把key制空
            mKey = null;
            //记录当前列
            mPosition = position;
            //换行清数据
            mIntegers.clear();
            mSelectedSchedule.clear();
            //
            mListener.notifySectionMap();
            //刷新打开的列表
            notifyItemChanged(position);
            mIntegers = new ArrayList<>();
        });

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, DensityUtil.dip2px(mContext, 1));

        for (int i = 0; i < COL_NUMBER; i++) {
            //动态创建生成TextView,并添加到view中
            holder.llCourseClose.addView(createTextView(mContext, params, i, position));

            //动态创建生成LinearLayout,并添加到view中
            holder.llCourseOpen.addView(createLinearLayout(holder, i, position));
        }
    }

    private View createLinearLayout(final CourseHolder holder, final int row, int column) {
        //创建打开时LinearLayout
        LinearLayout ll = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_course_info, holder.llCourseOpen, false);
        sortChildView(ll, row, column);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mScheduleType.equals(CourseScheduleActivity.CLICKABLE))
                    return;
                String lKey = getCurrentKey(row, column);
                List<ScheduleBean.SectionBean> lSectionBeen = mScheduleBean.getCourseListMap().get(lKey);
                //被调代课或者调走的不能选中
                if (lSectionBeen != null && lSectionBeen.size() > 0) {
                    ScheduleBean.SectionBean lSectionBean = lSectionBeen.get(0);
                    if (lSectionBean.getTransferType() != null) {
                        return;
                    }
                }

//                调时间时多个老师的课程置灰不能选中
                if (Constant.COURSE_S.equals(mCourseType)) {
                    if (lSectionBeen != null && lSectionBeen.size() > 0) {
                        if (lSectionBeen.get(0).getIsMultiTeacherFlag() == 1) {
                            return;
                        }
                    }
                }
                sortInteger(mIntegers);
//                单击已经选中了的，取消选中
                if (mIntegers.contains(row)) {
                    if (row != mIntegers.get(0) && row != mIntegers.get(mIntegers.size() - 1)) {
                        int lChildCount = holder.llCourseOpen.getChildCount();
                        for (int i = 0; i < lChildCount; i++) {
                            if (mIntegers.contains(i)) {
                                View lChildAt = holder.llCourseOpen.getChildAt(i);
                                lChildAt.setBackgroundResource(R.color.schedule_backgrouond_def);
                            }
                        }
                        mIntegers.clear();
                        mSelectedSchedule.clear();
                        mListener.itemClick(mSelectedSchedule, column);
                    } else {
                        v.setBackgroundResource(R.color.schedule_backgrouond_def);
                        mIntegers.remove(Integer.valueOf(row));
                        mSelectedSchedule.remove(lKey);
                        mListener.itemClick(mSelectedSchedule, column);
                        if (lKey.equals(mKey)) {
                            mKey = null;
                        }
                    }
                } else {
                    if (mIntegers.size() > 0) {
                        //只能选中连着的节次
                        if ((mIntegers.get(0) - 1 == row) || mIntegers.get(mIntegers.size() - 1) + 1 == row) {
                            //只有有节次信息的课程才能被选中
                            if (lSectionBeen != null) {
                                List<ScheduleBean.SectionBean> lPreSectionBeen = mSelectedSchedule.get(getPreKey(row, column));
                                List<ScheduleBean.SectionBean> lNextSectionBeen = mSelectedSchedule.get(getNextKey(row, column));
                                boolean isPreCourseEqual = false;
                                boolean isNextCourseEqual = false;
                                ScheduleBean.SectionBean lEqulBean = null;

                                if (lPreSectionBeen != null) {
                                    for (int i = 0; i < lSectionBeen.size(); i++) {
                                        ScheduleBean.SectionBean lSectionBean = lSectionBeen.get(i);
                                        // TODO  这里判断不对，要判断教学班级ID
//                                        if (lSectionBean.getCourseName().equals(lPreSectionBeen.get(0).getCourseName())) {
                                        if (lSectionBean.getTeachClassId() == lPreSectionBeen.get(0).getTeachClassId()) {
                                            isPreCourseEqual = true;
                                            lEqulBean = lSectionBean;
                                            break;
                                        }
                                    }
                                } else {
                                    isPreCourseEqual = true;
                                }


                                if (lNextSectionBeen != null) {
                                    for (int i = 0; i < lSectionBeen.size(); i++) {
                                        ScheduleBean.SectionBean lSectionBean = lSectionBeen.get(i);
                                        // TODO  这里判断不对，要判断班级ID
//                                        if (lSectionBean.getCourseName().equals(lNextSectionBeen.get(0).getCourseName())) {
                                        if (lSectionBean.getTeachClassId() == lNextSectionBeen.get(0).getTeachClassId()) {
                                            lEqulBean = lSectionBean;
                                            isNextCourseEqual = true;
                                            break;
                                        }
                                    }
                                } else {
                                    isNextCourseEqual = true;
                                }

                                //说明选的不是同一种课程
                                if (!isPreCourseEqual
                                        || !isNextCourseEqual) {
                                    ScheduleBean.SectionBean lSectionBean = lSectionBeen.get(0);
                                    //多阶段的课程
                                    if (lSectionBeen.size() > 1) {
                                        // TODO: 2017/4/1 阶段
                                        new SingleChooseDialog(mContext, 0, lSectionBeen)
                                                .setOnCheckListener(new SingleChooseDialog.onCheckListener() {
                                                    @Override
                                                    public void onCheck(int position) {
                                                        int lChildCount = holder.llCourseOpen.getChildCount();
                                                        for (int i = 0; i < lChildCount; i++) {
                                                            if (mIntegers.contains(i)) {
                                                                View lChildAt = holder.llCourseOpen.getChildAt(i);
                                                                lChildAt.setBackgroundResource(R.color.schedule_backgrouond_def);
                                                            }
                                                        }
                                                        v.setBackgroundResource(R.color.background_color_gray);
                                                        mIntegers.clear();
                                                        mSelectedSchedule.clear();
                                                        mIntegers.add(row);
                                                        List<ScheduleBean.SectionBean> lList = new ArrayList<>();
                                                        lList.add(lSectionBeen.get(position));
                                                        mSelectedSchedule.put(lKey, lList);
                                                        mListener.itemClick(mSelectedSchedule, column);
                                                    }
                                                })
                                                .create();


                                    } else {
                                        //多阶段的课程
                                        if (lSectionBean.getOpenCourseType() != 15 && lSectionBean.getOpenCourseType() != 0) {
                                            new SingleChooseDialog(mContext, 0, lSectionBeen)
                                                    .setOnCheckListener(new SingleChooseDialog.onCheckListener() {
                                                        @Override
                                                        public void onCheck(int position) {
                                                            clearAndCheck(v, lKey, lSectionBeen, holder, row, column);
                                                        }
                                                    }).create();
//                                            不是多阶段
                                        } else {
                                            clearAndCheck(v, lKey, lSectionBeen, holder, row, column);
                                        }
                                    }

                                    //是同一种课程
                                } else {
                                    if (lSectionBeen.size() > 1) {
                                        List<ScheduleBean.SectionBean> lBeen = new ArrayList<>();
                                        v.setBackgroundResource(R.color.background_color_gray);
                                        mIntegers.add(row);
                                        lBeen.add(lEqulBean);
                                        mSelectedSchedule.put(lKey, lBeen);
                                        mListener.itemClick(mSelectedSchedule, column);
                                        sortInteger(mIntegers);
                                    } else {
                                        v.setBackgroundResource(R.color.background_color_gray);
                                        mIntegers.add(row);
                                        mSelectedSchedule.put(lKey, lSectionBeen);
                                        mListener.itemClick(mSelectedSchedule, column);
                                        sortInteger(mIntegers);
                                    }
                                }
                            }
                            //如果点击的是不连续则取消之前选中的，选中点击的
                        } else {
                            //只有有节次信息的课程才能被选中
                            if (lSectionBeen != null) {
                                if (lSectionBeen.size() > 1) {
                                    // TODO: 2017/4/1 阶段
                                    new SingleChooseDialog(mContext, 0, lSectionBeen)
                                            .setOnCheckListener(new SingleChooseDialog.onCheckListener() {
                                                @Override
                                                public void onCheck(int position) {
                                                    clearAndCheck(v, lKey, lSectionBeen, holder, row, column);
                                                }
                                            }).create();
                                } else {
//                                    阶段
                                    ScheduleBean.SectionBean lSectionBean = lSectionBeen.get(0);
                                    if (lSectionBean.getOpenCourseType() != 15 && lSectionBean.getOpenCourseType() != 0) {
                                        new SingleChooseDialog(mContext, 0, lSectionBeen)
                                                .setOnCheckListener(new SingleChooseDialog.onCheckListener() {
                                                    @Override
                                                    public void onCheck(int position) {
                                                        clearAndCheck(v, lKey, lSectionBeen, holder, row, column);
                                                    }
                                                }).create();
                                    } else {
//                                        不是阶段
                                        clearAndCheck(v, lKey, lSectionBeen, holder, row, column);
                                    }
                                }
                            }
                        }
                    } else {
                        //只有有节次信息的课程才能被选中
                        if (lSectionBeen != null) {
                            //不为空设置数据
                            if (lSectionBeen.size() > 1) {
                                // TODO: 2017/3/31 阶段
                                new SingleChooseDialog(mContext, 0, lSectionBeen)
                                        .setOnCheckListener(new SingleChooseDialog.onCheckListener() {
                                            @Override
                                            public void onCheck(int position) {
                                                v.setBackgroundResource(R.color.background_color_gray);
                                                mIntegers.add(row);
                                                List<ScheduleBean.SectionBean> lList = new ArrayList<>();
                                                lList.add(lSectionBeen.get(position));
                                                mSelectedSchedule.put(lKey, lList);
                                                mListener.itemClick(mSelectedSchedule, column);
                                            }
                                        })
                                        .create();
                            } else {
//                                ScheduleBean.SectionBean lSectionBean = lSectionBeen.get(0);
//                                if (lSectionBean.getOpenCourseType() != 15 && lSectionBean.getOpenCourseType() != 0) {
//                                    // TODO: 2017/3/31 阶段
//                                    new SingleChooseDialog(mContext, 0, lSectionBeen)
//                                            .setOnCheckListener(new SingleChooseDialog.onCheckListener() {
//                                                @Override
//                                                public void onCheck(int position) {
//                                                    v.setBackgroundResource(R.color.background_color_gray);
//                                                    mIntegers.add(row);
//                                                    List<ScheduleBean.SectionBean> lList = new ArrayList<>();
//                                                    lList.add(lSectionBeen.get(position));
//                                                    mSelectedSchedule.put(lKey, lList);
//                                                    mListener.itemClick(mSelectedSchedule, column);
//                                                }
//                                            })
//                                            .create();
//                                } else {
//                                    v.setBackgroundResource(R.color.background_color_gray);
//                                    mIntegers.add(row);
//                                    mSelectedSchedule.put(lKey, lSectionBeen);
//                                    mListener.itemClick(mSelectedSchedule, column);
//                                }

                                v.setBackgroundResource(R.color.background_color_gray);
                                mIntegers.add(row);
                                mSelectedSchedule.put(lKey, lSectionBeen);
                                mListener.itemClick(mSelectedSchedule, column);
                            }
                        }
                    }
                }
            }
        });

        ll.setOnLongClickListener(v -> {
            String lKey = getCurrentKey(row, column);
            List<ScheduleBean.SectionBean> lSectionBeen = mScheduleBean.getCourseListMap().get(lKey);
            if (lSectionBeen != null && lSectionBeen.size() > 0) {
                lSectionBeen.get(0).setKey(lKey);
                mListener.itemLongClick(lSectionBeen, column, lKey);
            }
            return true;
        });
        return ll;
    }

    private void clearAndCheck(View v, String key, List<ScheduleBean.SectionBean> sectionBeen, CourseHolder holder, int row, int column) {
        int lChildCount = holder.llCourseOpen.getChildCount();
        for (int i = 0; i < lChildCount; i++) {
            if (mIntegers.contains(i)) {
                View lChildAt = holder.llCourseOpen.getChildAt(i);
                lChildAt.setBackgroundResource(R.color.schedule_backgrouond_def);
            }
        }
        v.setBackgroundResource(R.color.background_color_gray);
        mIntegers.clear();
        mSelectedSchedule.clear();
        mIntegers.add(row);
        mSelectedSchedule.put(key, sectionBeen);
        mListener.itemClick(mSelectedSchedule, column);
    }

    private class SingleAdapter extends ArrayAdapter<ScheduleBean.SectionBean> {

        public SingleAdapter(Context context, int resource, List<ScheduleBean.SectionBean> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return super.getView(position, convertView, parent);
        }
    }

    private void sortInteger(List<Integer> integers) {
        Collections.sort(integers, (o1, o2) -> {
            if (o1 > o2) {
                return 1;
            } else {
                return -1;
            }
        });
    }

    private void sortChildView(LinearLayout ll, int row, int column) {
        TextView tvCourseName = (TextView) ll.findViewById(R.id.tv_course_name);
        TextView tvCourseAddress = (TextView) ll.findViewById(R.id.tv_course_address);
        TextView tvClassName = (TextView) ll.findViewById(R.id.tv_class_name);
        TextView tvClassRemark = (TextView) ll.findViewById(R.id.schedule_class_remark_tv);
        TextView tvTimeRemark = (TextView) ll.findViewById(R.id.schedule_time_remark_tv);
        TextView tvStateOne = (TextView) ll.findViewById(R.id.tv_state_one);
        TextView tvStateTow = (TextView) ll.findViewById(R.id.tv_state_tow);
        TextView tvStateThree = (TextView) ll.findViewById(R.id.tv_state_three);
        TextView tvStateFour = (TextView) ll.findViewById(R.id.tv_state_four);
        //判断是否有课程ID,没有课程ID说明数据是空的
//        if (mSchedulesDatas.get(position).getcId() == null || mSchedulesDatas.get(position).getcId().equals("")) {
//            tvCourseName.setText("");
//            tvCourseAddress.setText("");
//            return;
//        }
        String lKey = getCurrentKey(row, column);

        tvStateOne.setVisibility(View.GONE);
        tvStateTow.setVisibility(View.GONE);
        tvStateThree.setVisibility(View.GONE);
        tvStateFour.setVisibility(View.GONE);
        //不为空设置数据
        List<ScheduleBean.SectionBean> lSectionBeen = mScheduleBean.getCourseListMap().get(lKey);
        if (lSectionBeen != null) {
            if (lSectionBeen.size() > 0) {
                ScheduleBean.SectionBean lSectionBean = lSectionBeen.get(0);
                String lCourseName = lSectionBean.getCourseName();
                if (lCourseName.length() > 9) {
                    lCourseName = lCourseName.substring(0, 9);
                    lCourseName += "...";
                }
                tvCourseName.setText(lCourseName);
                tvCourseAddress.setVisibility(lSectionBean.getRoomParam() != 3 ? View.VISIBLE : View.GONE);
                if (lSectionBean.getRoomParam() != 3) {
                    String lText = lSectionBean.getBuildingShortName() + "-" + lSectionBean.getClassroomName();
                    if (lText.length() > 9) {
                        lText = lText.substring(0, 9);
                        lText += "...";
                    }
                    tvCourseAddress.setText(lText);
                }
                //班级课表显示教师名称，教师课表显示班级名称
                tvClassName.setText(mFlag == CourseScheduleActivity.CLASS_SCHEDULE ? lSectionBean.getTeacherName() : lSectionBean.getClassName());
                //阶段开课判断
                if (lSectionBeen.size() > 1) {
                    tvTimeRemark.setVisibility(View.VISIBLE);
                } else {
                    if (lSectionBean.getOpenCourseType() != 15 && lSectionBean.getOpenCourseType() != 0) {
                        tvTimeRemark.setVisibility(View.VISIBLE);
                    } else {
                        tvTimeRemark.setVisibility(View.GONE);
                    }
                }

                //合班开课判断
                tvClassRemark.setVisibility(lSectionBean.getIsMultiClassFlag() == 1 ? View.VISIBLE : View.GONE);
                String lTransferType = lSectionBean.getTransferType();
                if ("1".equals(lTransferType)) {
                    ll.setBackgroundResource(R.color.yellow_ff9501);
                    tvCourseName.setTextColor(ContextCompat.getColor(mContext, R.color.wirte_ffffff));
                    tvClassName.setTextColor(ContextCompat.getColor(mContext, R.color.wirte_ffffff));
                    tvCourseAddress.setTextColor(ContextCompat.getColor(mContext, R.color.wirte_ffffff));
//                    课程已调走不用上课页面标识灰色
                } else if ("2".equals(lTransferType)) {
                    ll.setBackgroundResource(R.color.gray_999999);
                    tvCourseName.setTextColor(ContextCompat.getColor(mContext, R.color.wirte_ffffff));
                    tvClassName.setTextColor(ContextCompat.getColor(mContext, R.color.wirte_ffffff));
                    tvCourseAddress.setTextColor(ContextCompat.getColor(mContext, R.color.wirte_ffffff));
                } else {
                    if (Constant.COURSE_S.equals(mCourseType)) {
//                    调时间时多个教师的课程不能被选中
                        if (lSectionBean.getIsMultiTeacherFlag() == 1) {
                            ll.setBackgroundResource(R.color.gray_999999);
                            tvCourseName.setTextColor(ContextCompat.getColor(mContext, R.color.wirte_ffffff));
                            tvClassName.setTextColor(ContextCompat.getColor(mContext, R.color.wirte_ffffff));
                            tvCourseAddress.setTextColor(ContextCompat.getColor(mContext, R.color.wirte_ffffff));
                        } else {
                            ll.setBackgroundResource(R.color.schedule_backgrouond_def);
                            tvCourseName.setTextColor(ContextCompat.getColor(mContext, R.color.textColor_def));
                            tvClassName.setTextColor(ContextCompat.getColor(mContext, R.color.textColor_def));
                            tvCourseAddress.setTextColor(ContextCompat.getColor(mContext, R.color.textColor_def));
                        }
                    } else {
                        ll.setBackgroundResource(R.color.schedule_backgrouond_def);
                        tvCourseName.setTextColor(ContextCompat.getColor(mContext, R.color.textColor_def));
                        tvClassName.setTextColor(ContextCompat.getColor(mContext, R.color.textColor_def));
                        tvCourseAddress.setTextColor(ContextCompat.getColor(mContext, R.color.textColor_def));
                    }
                }


            }
        } else {
            List<String> weekUnit = mScheduleBean.getWeekUnit();
            if (weekUnit != null && weekUnit.size() != 0) {
                for (int i = 0; i < weekUnit.size(); i++) {
                    if (lKey.equals(weekUnit.get(i))) {
                        tvCourseName.setText("周会");
                    }
                }

            }
        }

//        从课本查询进入时要选中长按的那个，如果是多阶段的先弹框
        if (mKey != null) {
            if (mKey.equals(lKey)) {
                ScheduleBean.SectionBean lSectionBean = lSectionBeen.get(0);
                if (lSectionBeen.size() > 1) {
                    new SingleChooseDialog(mContext, 0, lSectionBeen)
                            .setOnCheckListener(new SingleChooseDialog.onCheckListener() {
                                @Override
                                public void onCheck(int position) {
                                    ll.setBackgroundResource(R.color.background_color_gray);
                                    mIntegers.clear();
                                    mSelectedSchedule.clear();
                                    mIntegers.add(row);
                                    List<ScheduleBean.SectionBean> lList = new ArrayList<>();
                                    lList.add(lSectionBeen.get(position));
                                    mSelectedSchedule.put(lKey, lList);
                                    mListener.itemClick(mSelectedSchedule, column);
                                }
                            })
                            .create();
                } else {
                    ll.setBackgroundResource(R.color.background_color_gray);
                    mIntegers.clear();
                    mSelectedSchedule.clear();
                    mIntegers.add(row);
                    mSelectedSchedule.put(lKey, lSectionBeen);
                }
            }
        }
    }

    private View createTextView(Context context, LinearLayout.LayoutParams params, int row, int column) {
        //创建关闭时textView
        TextView txt = new TextView(context);
        txt.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimensionPixelSize(R.dimen.schedule_text_size_close));
        txt.setPadding(5, 5, 5, 5);
        txt.setEllipsize(TextUtils.TruncateAt.END);
        String key = getCurrentKey(row, column);
        List<ScheduleBean.SectionBean> lSectionBeen = mScheduleBean.getCourseListMap().get(key);

        txt.setGravity(Gravity.CENTER);
        if (lSectionBeen != null) {
            if (lSectionBeen.size() > 0) {
                String lCourseName = lSectionBeen.get(0).getCourseName();
                txt.setText(lCourseName);

                String lTransferType = lSectionBeen.get(0).getTransferType();
//                1为调代课标识仍要上课，页面标黄色
                if ("1".equals(lTransferType)) {
                    txt.setBackgroundResource(R.color.yellow_ff9501);
                    txt.setTextColor(ContextCompat.getColor(mContext, R.color.wirte_ffffff));
//                    2为课程已调走不用上课页面标识灰色
                } else if ("2".equals(lTransferType)) {
                    txt.setBackgroundResource(R.color.gray_999999);
                    txt.setTextColor(ContextCompat.getColor(mContext, R.color.wirte_ffffff));
                } else {
                    //                调时间时多个老师的节次置灰
                    if (Constant.COURSE_S.equals(mCourseType)) {
                        if (lSectionBeen.get(0).getIsMultiTeacherFlag() == 1) {
                            txt.setBackgroundResource(R.color.gray_999999);
                            txt.setTextColor(ContextCompat.getColor(mContext, R.color.wirte_ffffff));
                        } else {
                            txt.setBackgroundResource(R.color.schedule_backgrouond_def);
                            txt.setTextColor(ContextCompat.getColor(mContext, R.color.textColor_def));
                        }
                    } else {
                        txt.setBackgroundResource(R.color.schedule_backgrouond_def);
                        txt.setTextColor(ContextCompat.getColor(mContext, R.color.textColor_def));
                    }
                }


            }
        } else {
            List<String> weekUnit = mScheduleBean.getWeekUnit();
            if (weekUnit != null && weekUnit.size() != 0) {
                for (int i = 0; i < weekUnit.size(); i++) {
                    if (key.equals(weekUnit.get(i))) {
                        txt.setText("周会");
                    }
                }
            }
            txt.setBackgroundResource(R.color.schedule_backgrouond_def);
            txt.setTextColor(ContextCompat.getColor(mContext, R.color.textColor_def));
        }
        txt.setHeight((int) context.getResources().getDimension(R.dimen.schedule_course_height));
        txt.setLayoutParams(params);

        return txt;
    }

    private String getCurrentKey(int row, int column) {
        return (row + 1) + "" + (column + 1);
    }

    private String getNextKey(int row, int column) {
        return (row + 2) + "" + (column + 1);
    }

    private String getPreKey(int row, int column) {
        return (row) + "" + (column + 1);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (mScheduleBean != null) {
            if (mScheduleBean.isIsSaturdayHasCourse()) {
                return 6;
            } else {
                return 5;
            }
        } else {
            return 0;
        }
//        return WEEK;
    }

    private class CourseHolder extends RecyclerView.ViewHolder {
        LinearLayout llCol; //总列
        LinearLayout llCourseClose; //关闭列
        LinearLayout llCourseOpen;  //打开列
        TextView weekColse;  //关闭状态标题
        TextView weekOpen;   //打开状态标题

        public CourseHolder(View itemView) {
            super(itemView);
            llCol = (LinearLayout) itemView.findViewById(R.id.ll_col);
            llCourseClose = (LinearLayout) itemView.findViewById(R.id.ll_course_close);
            llCourseOpen = (LinearLayout) itemView.findViewById(R.id.ll_course_open);
            weekColse = (TextView) itemView.findViewById(R.id.tv_week_close);
            weekOpen = (TextView) itemView.findViewById(R.id.tv_week_open);
        }
    }

}
