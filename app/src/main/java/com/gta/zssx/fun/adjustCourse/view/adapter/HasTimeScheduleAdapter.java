package com.gta.zssx.fun.adjustCourse.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.fun.adjustCourse.model.bean.HasTimeScheduleBean;
import com.gta.zssx.fun.adjustCourse.model.bean.SearchArguments;
import com.gta.zssx.pub.util.DensityUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2017/3/27.
 * @since 1.0.0
 */
public class HasTimeScheduleAdapter extends RecyclerView.Adapter {

    /**
     * 星期,表示列数
     */
    private static final int WEEK = 7;
    /**
     * 行数
     */
    private static int COL_NUMBER = 8;

    private String[] mWeek = new String[]{"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
    private int mPosition;
    private String mKey;

    /**
     * 用于记录选中的position
     */
    private List<Integer> mIntegers;
    private List<String> mSelectKeys;

    public interface Listener {
        void itemClick(List<String> mKeys);
        void notifySectionMap();
    }

    private Context mContext;
    private Listener mListener;
    private HasTimeScheduleBean mHasTimeScheduleBean;
    private SearchArguments mArguments;

    public HasTimeScheduleAdapter(Context context, Listener listener, HasTimeScheduleBean hasTimeScheduleBean
            , SearchArguments searchArguments) {
        mContext = context;
        mListener = listener;
        mHasTimeScheduleBean = hasTimeScheduleBean;
        mArguments = searchArguments;
        mSelectKeys = new ArrayList<>();
        mIntegers = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HasTimeScheduleAdapter.CourseHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_schedule_selected, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HasTimeScheduleAdapter.CourseHolder lCourseHolder = (HasTimeScheduleAdapter.CourseHolder) holder;
        iniView(lCourseHolder, position);

        //判断是否为打开状态,将对应控件设置隐藏和显示
        if (mPosition != position) {
            lCourseHolder.weekOpen.setVisibility(View.GONE);
            lCourseHolder.llCourseOpen.setVisibility(View.GONE);
            lCourseHolder.weekColse.setVisibility(View.VISIBLE);
            lCourseHolder.llCourseClose.setVisibility(View.VISIBLE);
        } else {
            lCourseHolder.llCourseClose.setVisibility(View.GONE);
            lCourseHolder.weekColse.setVisibility(View.GONE);
            lCourseHolder.llCourseOpen.setVisibility(View.VISIBLE);
            lCourseHolder.weekOpen.setVisibility(View.VISIBLE);
            restoreOpenState(position, lCourseHolder);
        }
    }

    private void restoreOpenState(int position, CourseHolder lCourseHolder) {
        String hasCourseTime = mHasTimeScheduleBean.getHasCourseTime();
        String[] split = hasCourseTime.split(Pattern.quote(","));
        List<String> strings= Arrays.asList(split);
        for (int i = 0; i < lCourseHolder.llCourseOpen.getChildCount(); i++) {
            if (!strings.contains(getCurrentKey(i, position))) {
                View lChildAt = lCourseHolder.llCourseOpen.getChildAt(i);
                lChildAt.setBackgroundResource(R.color.wirte_ffffff);
            }
        }
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private void iniView(CourseHolder holder, int position) {
        //设置标题
        String[] lDays = mArguments.getDays();
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
            //
            mListener.notifySectionMap();
            //刷新打开的列表
            notifyItemChanged(position);

            mIntegers = new ArrayList<>();
            mSelectKeys = new ArrayList<>();
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

    private View createLinearLayout(CourseHolder holder, final int row, int column) {
        RelativeLayout ll = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_has_time_schedule, holder.llCourseOpen, false);
        String lKey = getCurrentKey(row, column);
        TextView tvClassName = (TextView) ll.findViewById(R.id.tv_class_name);
        if (!mHasTimeScheduleBean.getHasCourseTime().contains(lKey)) {
            tvClassName.setText("可选择");
            ll.setBackgroundResource(R.color.wirte_ffffff);
        } else {
            ll.setBackgroundResource(R.color.gray_dbdbdb);
        }
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mHasTimeScheduleBean.getHasCourseTime().contains(lKey))
                    return;
                if (mSelectKeys.contains(lKey)) {
                    mSelectKeys.remove(lKey);
                    ll.setBackgroundResource(R.color.wirte_ffffff);
                    mListener.itemClick(mSelectKeys);
                } else {
//                    达到匹配数量时
                    if (mSelectKeys.size() == mArguments.getCourseCount()) {
                        String lastKey = mSelectKeys.get(mSelectKeys.size() - 1);
                        String nextKey = getNextKey(lastKey);
                        String preKey = getPreKey(lastKey);
//                        点击的是连续的
                        if (lKey.equals(nextKey) || lKey.equals(preKey)) {
                            int lChildCount = holder.llCourseOpen.getChildCount();
                            for (int i = 0; i < lChildCount; i++) {
                                if (getCurrentKey(i, column).equals(mSelectKeys.get(0))) {
                                    View lChildAt = holder.llCourseOpen.getChildAt(i);
                                    lChildAt.setBackgroundResource(R.color.wirte_ffffff);
                                }
                            }
                            mSelectKeys.remove(0);
                            mSelectKeys.add(lKey);
                            ll.setBackgroundResource(R.color.background_color_gray);
                            mListener.itemClick(mSelectKeys);
                        } else {
                            int lChildCount = holder.llCourseOpen.getChildCount();
                            for (int i = 0; i < lChildCount; i++) {
                                if (mSelectKeys.contains(getCurrentKey(i, column))) {
                                    View lChildAt = holder.llCourseOpen.getChildAt(i);
                                    lChildAt.setBackgroundResource(R.color.wirte_ffffff);
                                }
                            }
                            mSelectKeys.clear();
                            mSelectKeys.add(lKey);
                            ll.setBackgroundResource(R.color.background_color_gray);
                            mListener.itemClick(mSelectKeys);
                        }

                    } else if (mSelectKeys.size() == 0) {
                        mSelectKeys.add(lKey);
                        ll.setBackgroundResource(R.color.background_color_gray);
                        mListener.itemClick(mSelectKeys);
                    } else {
                        String lastKey = mSelectKeys.get(mSelectKeys.size() - 1);
                        String nextKey = getNextKey(lastKey);
                        String preKey = getPreKey(lastKey);
                        if (lKey.equals(nextKey) || lKey.equals(preKey)) {
                            mSelectKeys.add(lKey);
                            ll.setBackgroundResource(R.color.background_color_gray);
                            mListener.itemClick(mSelectKeys);
                        } else {
                            int lChildCount = holder.llCourseOpen.getChildCount();
                            for (int i = 0; i < lChildCount; i++) {
                                if (mSelectKeys.contains(getCurrentKey(i, column))) {
                                    View lChildAt = holder.llCourseOpen.getChildAt(i);
                                    lChildAt.setBackgroundResource(R.color.wirte_ffffff);
                                }
                            }
                            mSelectKeys.clear();
                            mSelectKeys.add(lKey);
                            ll.setBackgroundResource(R.color.background_color_gray);
                            mListener.itemClick(mSelectKeys);
                        }
                    }
                }
            }
        });
        return ll;
    }

    private String getNextKey(String lastKey) {
        String[] split = lastKey.split(Pattern.quote(":"));
        int row = Integer.parseInt(split[1]) - 1;
        int nextRow = row + 1;
        return getCurrentKey(nextRow, Integer.parseInt(split[0]) - 1);
    }

    private String getPreKey(String lastKey) {
        String[] split = lastKey.split(Pattern.quote(":"));
        int row = Integer.parseInt(split[1]) - 1;
        int nextRow = row - 1;
        return getCurrentKey(nextRow, Integer.parseInt(split[0]) - 1);
    }

    private View createTextView(Context context, LinearLayout.LayoutParams params, int row, int column) {
        //创建关闭时textView
        TextView txt = new TextView(context);
        txt.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimensionPixelSize(R.dimen.schedule_text_size_close));
        txt.setPadding(5, 5, 5, 5);
        txt.setEllipsize(TextUtils.TruncateAt.END);
        txt.setGravity(Gravity.CENTER);
        txt.setHeight((int) context.getResources().getDimension(R.dimen.schedule_course_height));
        txt.setLayoutParams(params);
        String lKey = getCurrentKey(row, column);
        if (!mHasTimeScheduleBean.getHasCourseTime().contains(lKey)) {
            txt.setText("可选择");
            txt.setBackgroundResource(R.color.wirte_ffffff);
        } else {
            txt.setBackgroundResource(R.color.gray_dbdbdb);
        }
        return txt;
    }

    @Override
    public int getItemCount() {
        if (mHasTimeScheduleBean != null) {
            if (mHasTimeScheduleBean.isIsSaturdayHasCourse()) {
                return 6;
            } else {
                return 5;
            }
        } else {
            return 0;
        }
    }

    private String getCurrentKey(int row, int column) {
        return (column + 1) + ":" + (row + 1);
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
