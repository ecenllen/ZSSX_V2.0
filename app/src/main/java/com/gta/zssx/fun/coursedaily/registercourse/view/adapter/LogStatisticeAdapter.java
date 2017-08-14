package com.gta.zssx.fun.coursedaily.registercourse.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.LogChartBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.LogListBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.LogStatisticsBean;
import com.gta.zssx.fun.coursedaily.registercourse.view.pageV2.ClassLogStatisticeActivity;
import com.gta.zssx.pub.common.Constant;
import com.gta.zssx.pub.widget.PickerTimeDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * 日志统计
 * Created by xiao.peng on 2016/11/10.
 */
public class LogStatisticeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements PickerTimeDialog.OnClickConfirmListener {

    private Context mContext;
    private LogStatisticsBean mData;
    private LayoutInflater inflater;

    private static final int TYPE_CHART = 1;//图表
    private static final int TYPE_TIME = 2; //时间选择
    private static final int TYPE_LIST = 3; //列表
    private PickerTimeDialog dialog;
    private int currentYear;  //当前年
    private int currentMonth; //当前月
    private int currentDay;  //当前天
    private String currentTime;

    private int year;  //当前选择年
    private int month; //当前选择月
    private int day; //当前选择天

    private OnTimeNotifyListener mListener;

    public LogStatisticeAdapter(Context context, LogStatisticsBean logStatisticsBean, OnTimeNotifyListener listener) {
        this.mContext = context;
        this.mData = logStatisticsBean;
        this.mListener = listener;
        inflater = LayoutInflater.from(context);
    }

    public void setNotifyDatas(LogStatisticsBean logStatisticsBean) {
        this.mData = logStatisticsBean;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            default:
            case TYPE_CHART:
                return new ChartHolder(inflater.inflate(R.layout.item_log_chart, parent, false));
            case TYPE_TIME:
                return new TimeHolder(inflater.inflate(R.layout.item_log_time, parent, false));
            case TYPE_LIST:
                return new ListHolder(inflater.inflate(R.layout.item_log_list, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int lItemViewType = getItemViewType(position);
        if (lItemViewType == TYPE_CHART) {
            logChart((ChartHolder) holder);
        } else if (lItemViewType == TYPE_TIME) {
            logTime((TimeHolder) holder);
        } else if (lItemViewType == TYPE_LIST) {
            logList((ListHolder) holder, position);
        }
    }

    /**
     * 图表的处理
     */
    private void logChart(ChartHolder holder) {
        LogChartBean chartBean = mData.getChartBean();
        //生成迟到图表
        createChart(holder.lateTitle, holder.lateNum, chartBean.getTodayLate(), "迟到",
                holder.lateLineChart, chartBean.getLate(), "#5EB1EE", holder.lateLine);
        //生成请假图表
        createChart(holder.vacateTitle, holder.vacateNum, chartBean.getTodayVacate(), "请假",
                holder.vacateLineChart, chartBean.getVacate(), "#31C4B5", holder.vacateLine);
        //生成旷课图表
        createChart(holder.truantTitle, holder.truantNum, chartBean.getTodayTruant(), "旷课",
                holder.truantLineChart, chartBean.getTruant(), "#FE706F", holder.truantLine);
        //生成公假图表
        createChart(holder.sabbaticalsTitle, holder.sabbaticalsNum, chartBean.getTodaySabbaticals(), "公假",
                holder.sabbaticalsLineChart, chartBean.getSabbaticals(), "#AA88E2", holder.sabbaticalsLine);


    }

    /**
     * 生成图表
     *
     * @param tvTitle 要设置的标题
     * @param numTv   要设置的数目
     * @param num     数目
     * @param name    标题名
     * @param view    图表控件布局
     * @param list    图表上要显示的数据
     * @param color   图表的颜色
     * @param line    全为0的时候显示的线条
     */
    private void createChart(TextView tvTitle, TextView numTv, String num, String name, LineChartView view, List<String> list, String color, View line) {
        tvTitle.setText(String.format(mContext.getString(R.string.log_statistics_chart_title), name));
        numTv.setText(String.format(mContext.getString(R.string.log_statistics_chart_num), num));
        boolean b = false;
        for (String s : list) {
            if (!s.equals("0")) {
                b = true;
                break;
            }
        }
        line.setBackgroundColor(Color.parseColor(color));
        initLineChart(getAxisPoints(list), view, color);
        if (b) {
            line.setVisibility(View.GONE);
        } else {
            line.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 图表的每个点的显示
     */
    private List<PointValue> getAxisPoints(List<String> list) {
        List<PointValue> pointValues = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            pointValues.add(new PointValue(i, Integer.valueOf(list.get(i))));
        }
        return pointValues;
    }

    /**
     * 时间的处理
     */
    private void logTime(TimeHolder holder) {
        if (dialog == null) {
            initTime();
            dialog = new PickerTimeDialog(mContext, holder.tvTime, this);
        }
        holder.tvTitle.setText("专业部");
        holder.tvTime.setText(jointString(year, month, day));
        holder.tvTime.setOnClickListener(view -> {
            dialog.show();
            dialog.setPickerTime(year, month, day);
        });
        if (isCurrentDay()) {
            holder.tvNext.setClickable(false);
            holder.tvNext.setTextColor(Color.parseColor("#999999"));
        } else {
            holder.tvNext.setClickable(true);
            holder.tvNext.setTextColor(Color.parseColor("#43bc89"));
            holder.tvNext.setOnClickListener(view -> clickTimeNext());
        }
        if (isPremierDay()) {
            holder.tvPre.setClickable(false);
            holder.tvPre.setTextColor(Color.parseColor("#999999"));
        } else {
            holder.tvPre.setClickable(true);
            holder.tvPre.setTextColor(Color.parseColor("#43bc89"));
            holder.tvPre.setOnClickListener(view -> clickTimePre());
        }
    }

    /**
     * 初始化时间
     */
    private void initTime() {
        // 格式化当前时间，并转换为年月日整型数据
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        currentTime = sdf.format(new Date(System.currentTimeMillis()));
        String[] time = currentTime.split("-");
        currentYear = Integer.valueOf(time[0]);
        currentMonth = Integer.valueOf(time[1]);
        currentDay = Integer.valueOf(time[2]);
        year = currentYear;
        month = currentMonth;
        day = currentDay;
    }


    /**
     * 拼接日期字符串
     */
    private String jointString(int year, int month, int day) {
        return "< " + year + "年" + month + "月" + day + "日 >";
    }

    /**
     * 点击后一天时间
     */
    private void clickTimeNext() {
        //判断是否是这个月份的最后一天
        if (day == dialog.getLastDay(year, month)) {
            day = 1;
            //判断是否是最后一月
            if (month == 12) {
                month = 1;
                year++;
            } else {
                month++;
            }
        } else {
            day++;
        }
        //重新访问网络,拿数据
        mListener.timeNotify(year + "-" + month + "-" + day);
    }

    /**
     * 点击前一天时间
     */
    private void clickTimePre() {
        //判断当前天是否是1号
        if (day - 1 == 0) {
            //判断上一个月是否是1月
            if (month - 1 == 0) {
                //如果是1月份的1号,退一天就是12月份,12月只有31天
                day = 31;
                month = 12;
                year--;
            } else {
                month--;
                //获得月份下最大的天数
                day = dialog.getLastDay(year, month);
            }
        } else {
            //如果不是1号的话,前一天不会影响到年月
            day--;
        }
        //重新访问网络,拿数据
        mListener.timeNotify(year + "-" + month + "-" + day);
    }

    /**
     * 判断是否为当前年月日
     */
    private boolean isCurrentDay() {
        return year == currentYear && month == currentMonth && day == currentDay;
    }

    /**
     * 判断是否为最早的日期(1900-1-1)
     */
    private boolean isPremierDay() {
        return year == 1900 && month == 1 && day == 1;
    }

    /**
     * 选择时间后点击的确定
     */
    @Override
    public void onClickConfirm(String currentTime, int year, int month, int day) {
        this.currentTime = currentTime;
        this.year = year;
        this.month = month;
        this.day = day;
        //重新访问网络,拿数据
        mListener.timeNotify(year + "-" + month + "-" + day);
    }

    /**
     * 列表的处理
     */
    private void logList(ListHolder holder, int position) {
        List<LogListBean> listBeen = mData.getListBean();
        LogListBean bean = listBeen.get(position - 2);
        if (position == listBeen.size() + 1)
            holder.emptyHint.setVisibility(View.VISIBLE);
        else
            holder.emptyHint.setVisibility(View.GONE);
        holder.tvNmae.setText(bean.getmName());
        setLogListText(holder.tvLate, bean.getLate(), bean.isMaxLate());
        setLogListText(holder.tvVacate, bean.getVacate(), bean.isMaxVacate());
        setLogListText(holder.tvTruant, bean.getTruant(), bean.isMaxTruant());
        setLogListText(holder.tvSabbaticals, bean.getSabbaticals(), bean.isMaxSabbaticals());
        holder.llContent.setOnClickListener(view -> {
            //跳转到班级详情页
            Intent intent = new Intent(mContext, ClassLogStatisticeActivity.class);
            intent.putExtra(Constant.DATE, year + "-" + month + "-" + day);
            intent.putExtra(Constant.MID, bean.getmId());
            intent.putExtra(Constant.MNAME, bean.getmName());
            mContext.startActivity(intent);
        });

    }

    private void setLogListText(TextView tv, String text, boolean isMax) {
        tv.setText(text);
        tv.setTextColor("0".equals(text) ? Color.parseColor("#999999") :
                (isMax ? Color.parseColor("#fc3e39") : Color.parseColor("#666666")));
    }

    @Override
    public int getItemCount() {
        return mData.getListBean().size() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_CHART;
        } else if (position == 1) {
            return TYPE_TIME;
        } else {
            return TYPE_LIST;
        }
    }

    /**
     * 图表
     */
    private class ChartHolder extends RecyclerView.ViewHolder {

        private TextView lateTitle;
        private TextView lateNum;
        private LineChartView lateLineChart;
        private View lateLine;

        private TextView vacateTitle;
        private TextView vacateNum;
        private LineChartView vacateLineChart;
        private View vacateLine;

        private TextView truantTitle;
        private TextView truantNum;
        private LineChartView truantLineChart;
        private View truantLine;

        private TextView sabbaticalsTitle;
        private TextView sabbaticalsNum;
        private LineChartView sabbaticalsLineChart;
        private View sabbaticalsLine;

        public ChartHolder(View itemView) {
            super(itemView);
            //迟到
            CardView llLate = (CardView) itemView.findViewById(R.id.log_late);
            //请假
            CardView llVacate = (CardView) itemView.findViewById(R.id.log_vacate);
            //旷课
            CardView llTruant = (CardView) itemView.findViewById(R.id.log_truant);
            //公假
            CardView llSabbaticals = (CardView) itemView.findViewById(R.id.log_sabbaticals);

            lateTitle = (TextView) llLate.findViewById(R.id.tv_title);
            lateNum = (TextView) llLate.findViewById(R.id.tv_num);
            lateLineChart = (LineChartView) llLate.findViewById(R.id.line_chart);
            lateLine = llLate.findViewById(R.id.view_line);

            vacateTitle = (TextView) llVacate.findViewById(R.id.tv_title);
            vacateNum = (TextView) llVacate.findViewById(R.id.tv_num);
            vacateLineChart = (LineChartView) llVacate.findViewById(R.id.line_chart);
            vacateLine = llVacate.findViewById(R.id.view_line);

            truantTitle = (TextView) llTruant.findViewById(R.id.tv_title);
            truantNum = (TextView) llTruant.findViewById(R.id.tv_num);
            truantLineChart = (LineChartView) llTruant.findViewById(R.id.line_chart);
            truantLine = llTruant.findViewById(R.id.view_line);

            sabbaticalsTitle = (TextView) llSabbaticals.findViewById(R.id.tv_title);
            sabbaticalsNum = (TextView) llSabbaticals.findViewById(R.id.tv_num);
            sabbaticalsLineChart = (LineChartView) llSabbaticals.findViewById(R.id.line_chart);
            sabbaticalsLine = llSabbaticals.findViewById(R.id.view_line);


        }
    }

    /**
     * 时间
     */
    private class TimeHolder extends RecyclerView.ViewHolder {

        private TextView tvPre;

        private TextView tvTime;

        private TextView tvNext;

        private TextView tvTitle;

        public TimeHolder(View itemView) {
            super(itemView);
            //前一天
            tvPre = (TextView) itemView.findViewById(R.id.tv_pre);
            //后一天
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            //时间
            tvNext = (TextView) itemView.findViewById(R.id.tv_next);
            //标题
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }

    /**
     * 列表
     */
    private class ListHolder extends RecyclerView.ViewHolder {

        private TextView tvNmae;

        private TextView tvLate;

        private TextView tvVacate;

        private TextView tvTruant;

        private TextView tvSabbaticals;

        private LinearLayout llContent;

        private View emptyHint;

        public ListHolder(View itemView) {
            super(itemView);
            llContent = (LinearLayout) itemView.findViewById(R.id.ll_content);
            //名称
            tvNmae = (TextView) itemView.findViewById(R.id.tv_name);
            //迟到
            tvLate = (TextView) itemView.findViewById(R.id.tv_late);
            //请假
            tvVacate = (TextView) itemView.findViewById(R.id.tv_vacate);
            //旷课
            tvTruant = (TextView) itemView.findViewById(R.id.tv_truant);
            //公假
            tvSabbaticals = (TextView) itemView.findViewById(R.id.tv_sabbaticals);
            //空距离
            emptyHint = itemView.findViewById(R.id.emptyHint);
        }
    }

    public interface OnTimeNotifyListener {
        void timeNotify(String date);
    }


    /**=======================================================================================**/
    /**
     * 初始化LineChart的一些设置
     */
    private void initLineChart(List<PointValue> pointValues, LineChartView lineChart, String color) {
        lineChart.setVisibility(View.GONE);
        Line line = new Line(pointValues).setColor(Color.parseColor(color));  //折线的颜色
        List<Line> lines = new ArrayList<>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.SQUARE）
        line.setCubic(false);//曲线是否平滑
        line.setStrokeWidth(2);//线条的粗细，默认是3
        line.setFilled(true);//是否填充曲线的面积
//        line.setHasLabels(true);//曲线的数据坐标是否加上备注
//		line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用直线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(false);//是否显示圆点 如果为false 则没有原点只有点显示
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //设置行为属性，支持缩放、滑动以及平移
        lineChart.setInteractive(false);  //与用户交互
        lineChart.setZoomType(ZoomType.HORIZONTAL);  //缩放类型，水平
        lineChart.setMaxZoom((float) 2);//缩放比例
        lineChart.setLineChartData(data);
        lineChart.setVisibility(View.VISIBLE);

        Viewport v = new Viewport(lineChart.getMaximumViewport());
        v.left = 0;
        v.right = 4;
        lineChart.setCurrentViewport(v);
    }
}
