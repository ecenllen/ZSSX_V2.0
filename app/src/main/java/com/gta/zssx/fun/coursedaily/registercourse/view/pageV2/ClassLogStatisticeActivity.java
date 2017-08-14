package com.gta.zssx.fun.coursedaily.registercourse.view.pageV2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.model.ClassDataManager;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.LogListBean;
import com.gta.zssx.fun.coursedaily.registercourse.view.adapter.ClassLogStatisticeAdapter;
import com.gta.zssx.pub.base.BaseAppCompatActivity;
import com.gta.zssx.pub.common.Constant;
import com.gta.zssx.pub.manager.ToolBarManager;
import com.gta.zssx.pub.widget.PickerTimeDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.Subscription;

/**
 * 日志统计-班级信息
 * Created by xiao.peng on 2016/11/15.
 */
public class ClassLogStatisticeActivity extends BaseAppCompatActivity implements PickerTimeDialog.OnClickConfirmListener {


    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.tv_pre)
    TextView tvPre;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.tv_next)
    TextView tvNext;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_time_title)
    TextView tvTimeTitle;
    @Bind(R.id.class_display_rv)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private String mId;
    private String mName;
    private PickerTimeDialog dialog;
    public Subscription mSubscriptionList;


    private int currentYear;  //当前年
    private int currentMonth; //当前月
    private int currentDay;  //当前天

    private int day;
    private int month;
    private int year;

    private List<LogListBean> mListBean = new ArrayList<>();
    private ClassLogStatisticeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_log_statistice);
        ButterKnife.bind(this);

        initData();
        initView();
        initEvent();
        loadData();
    }

    private void initData() {
        Intent intent = getIntent();
        mId = intent.getStringExtra(Constant.MID);
        mName = intent.getStringExtra(Constant.MNAME);
        String currentDate = intent.getStringExtra(Constant.DATE);
        initTime(currentDate);

    }

    private void initView() {
        tvTimeTitle.setVisibility(View.GONE);  //显示部门的不需要Title
        ToolBarManager mToolBarManager = new ToolBarManager(mToolbar, this);
        mToolBarManager.init();
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());

        mToolBarManager.showBack(true)
                .setTitle(mName);
        tvTime.setText(jointString(year, month, day));
        tvTitle.setText("班级");

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mSwipeRefreshLayout.setColorSchemeResources(R.color.main_color, R.color.blue, R.color.purple);
        mSwipeRefreshLayout.setOnRefreshListener(() -> getLogList(year + "-" + month + "-" + day));

    }

    private void initEvent() {
        if (dialog == null) {
            dialog = new PickerTimeDialog(this, tvTime, this);
        }
        tvTime.setOnClickListener(view -> {
            dialog.show();
            dialog.setPickerTime(year, month, day);
        });
    }

    private void tvChange() {
        if (isCurrentDay()) {
            tvNext.setClickable(false);
            tvNext.setTextColor(Color.parseColor("#999999"));
        } else {
            tvNext.setClickable(true);
            tvNext.setTextColor(Color.parseColor("#43bc89"));
            tvNext.setOnClickListener(view -> clickTimeNext());
        }
        if (isPremierDay()) {
            tvPre.setClickable(false);
            tvPre.setTextColor(Color.parseColor("#999999"));
        } else {
            tvPre.setClickable(true);
            tvPre.setTextColor(Color.parseColor("#43bc89"));
            tvPre.setOnClickListener(view -> clickTimePre());
        }
    }

    private void loadData() {
        getLogList(year + "-" + month + "-" + day);
    }

    /**
     * 初始化时间
     */
    private void initTime(String time) {
        // 格式化当前时间，并转换为年月日整型数据
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentTime = sdf.format(new Date(System.currentTimeMillis()));
        String[] currentTimes = currentTime.split("-");
        currentYear = Integer.valueOf(currentTimes[0]);
        currentMonth = Integer.valueOf(currentTimes[1]);
        currentDay = Integer.valueOf(currentTimes[2]);

        String[] times = time.split("-");
        year = Integer.valueOf(times[0]);
        month = Integer.valueOf(times[1]);
        day = Integer.valueOf(times[2]);
    }

    /**
     * 拼接日期字符串
     */
    private String jointString(int year, int month, int day) {
        return "< " + year + "年" + month + "月" + day + "日 >";
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
        tvTime.setText(jointString(year, month, day));
        //重新访问网络,拿数据
        getLogList(year + "-" + month + "-" + day);
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
        tvTime.setText(jointString(year, month, day));
        //重新访问网络,拿数据
        getLogList(year + "-" + month + "-" + day);
    }

    @Override
    public void onClickConfirm(String currentTime, int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        getLogList(year + "-" + month + "-" + day);
    }


    /**
     * 日志统计-部门列表
     */
    public void getLogList(String date) {
        tvChange();  //改变日期的都先改变View，然后再去请求数据
        mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(true));
        mSubscriptionList = ClassDataManager.getLogClassList(date, mId)
                .subscribe(new Subscriber<List<LogListBean>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        mListBean.clear();
                        showResult();
                    }

                    @Override
                    public void onNext(List<LogListBean> listBean) {
                        mListBean = sortList(listBean);
                        showResult();
                    }

                });
    }

    private List<LogListBean> sortList(List<LogListBean> list) {
        //记录当前最大值
        int late = 0;
        int vacate = 0;
        int truant = 0;
        int sabbaticals = 0;
        for (int i = 0; i < list.size(); i++) {
            int late1 = Integer.valueOf(list.get(i).getLate());
            late = late > late1 ? late : late1;
            int truant1 = Integer.valueOf(list.get(i).getTruant());
            truant = truant > truant1 ? truant : truant1;
            int vacate1 = Integer.valueOf(list.get(i).getVacate());
            vacate = vacate > vacate1 ? vacate : vacate1;
            int sabbaticals1 = Integer.valueOf(list.get(i).getSabbaticals());
            sabbaticals = sabbaticals > sabbaticals1 ? sabbaticals : sabbaticals1;
        }
        for (int i = 0; i < list.size(); i++) {
            if (late == Integer.valueOf(list.get(i).getLate())) {
                list.get(i).setMaxLate(true);
            }
            if (vacate == Integer.valueOf(list.get(i).getVacate())) {
                list.get(i).setMaxVacate(true);
            }
            if (truant == Integer.valueOf(list.get(i).getTruant())) {
                list.get(i).setMaxTruant(true);
            }
            if (sabbaticals == Integer.valueOf(list.get(i).getSabbaticals())) {
                list.get(i).setMaxSabbaticals(true);
            }
        }
        return list;
    }

    private void showResult() {
        mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(false));
        String date = year + "-" + month + "-" + day;
        if (month < 10) {
            if (day < 10) {
                date = year + "-0" + month + "-0" + day;
            } else {
                date = year + "-0" + month + "-" + day;
            }
        } else if (day < 10) {
            date = year + "-" + month + "-0" + day;
        }
        if (mAdapter == null) {
            mAdapter = new ClassLogStatisticeAdapter(this, mListBean, date);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setNotifyDatas(mListBean, date);
        }
    }
}
