package com.gta.zssx.mobileOA.view.page;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.gta.utils.resource.Toast;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.R;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.mobileOA.model.bean.SemessterAndWeeklyInfo;
import com.gta.zssx.mobileOA.model.bean.TermInfo;
import com.gta.zssx.mobileOA.model.bean.TermWeekInfo;
import com.gta.zssx.mobileOA.presenter.SemesterSchedulePresenter;
import com.gta.zssx.mobileOA.view.SemesterScheduleView;
import com.gta.zssx.mobileOA.view.adapter.SemesterScheduleAdapter;
import com.gta.zssx.mobileOA.view.adapter.TitleSpinnerAdapter;
import com.gta.zssx.mobileOA.view.adapter.WeeklyScheduleAdapter;
import com.gta.zssx.mobileOA.view.base.BaseOAFragment;
import com.gta.zssx.pub.hfrecyclerview.HFRecyclerView;
import com.gta.zssx.pub.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lan.zheng on 2016/11/2.
 */
public class SemesterScheduleFragment extends BaseOAFragment<SemesterScheduleView,SemesterSchedulePresenter> implements  SemesterScheduleView, HFRecyclerView.HFRecyclerViewListener , View.OnClickListener{

    private SemesterScheduleAdapter mSemesterScheduleAdapter;
    private HFRecyclerView mRecyclerView;
    private LinearLayout mSemesterLayout;
    private LinearLayout mWeekLayout;
    private TextView mAllEmptyTextView;     //用于第一次获取学期失败的情况
    private TextView mListEmptyTextView;    //用于列表没数据和周获取失败的情况
    private TextView mSemesterTextView;
    private TextView mWeekTextView;
    private OptionsPickerView mSemesterOptionsPickerView;
    private OptionsPickerView mWeekOptionsPickerView;
    private List<TermInfo> mSemesterList;
    private List<TermWeekInfo.WeekEntity> mWeekList;
    private List<String> mSemesterStringList; //学期名字列表
    private List<String> mWeekStringList;    //取出周名字列表
    private int semesterPosition = 0;   //选中
    private int weekPosition = 0;       //选中
    private boolean isCanClickWeekSelect = true;  //是否能进行周选择

    @NonNull
    @Override
    public SemesterSchedulePresenter createPresenter() {
        return new SemesterSchedulePresenter();
    }

    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_semester_schedule, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        initView();
        initData();
    }
    private void initView(){
        mRecyclerView = (HFRecyclerView) view.findViewById(R.id.rv_schedule);
        mAllEmptyTextView = (TextView)view.findViewById(R.id.tv_all_empty);
        mListEmptyTextView = (TextView)view.findViewById(R.id.tv_emptyHint);
        mSemesterTextView = (TextView)view.findViewById(R.id.semester_tv_show);
        mWeekTextView = (TextView)view.findViewById(R.id.week_tv_show) ;
        mSemesterLayout = (LinearLayout)view.findViewById(R.id.semester_layout);
        mWeekLayout = (LinearLayout)view.findViewById(R.id.week_layout);
        mSemesterLayout.setOnClickListener(this);
        mWeekLayout.setOnClickListener(this);
        setOnInteractListener ();
    }

    private void setOnInteractListener () {
        mRecyclerView.setCanLoadMore (false);  //暂时不提供上拉
        mRecyclerView.setCanRefresh (true);
        mRecyclerView.setFooterViewText ("");
        mRecyclerView.setRecyclerViewListener (this);
    }

    private void initData(){
        mSemesterList = new ArrayList<>();
        mSemesterStringList = new ArrayList<>();
        mWeekList = new ArrayList<>();
        mWeekStringList = new ArrayList<>();

        //TODO 获取当前学期和所有学期，第一次才需要
        mAllEmptyTextView.setVisibility(View.VISIBLE);
        presenter.getSemesterData();  //测试SemesterId = 20161
    }

    @Override
    public void onRefresh() {
        if(isCanClickWeekSelect){
            getDetailWeeklyInfo(true);  //有周才能去刷新数据
        }else {
            Toast.Short(getActivity(),"周为“空”，无法获取数据");
            mRecyclerView.stopRefresh(true);
        }

    }

    @Override
    public void onLoadMore() {
        //不提供加载更多
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.semester_layout:
                mSemesterOptionsPickerView.setSelectOptions(semesterPosition);
                mSemesterOptionsPickerView.show();
                break;
            case R.id.week_layout:
                if(!isCanClickWeekSelect){
                    return;  //获取周失败不弹
                }
                mWeekOptionsPickerView.setSelectOptions(weekPosition);
                mWeekOptionsPickerView.show();
                break;
            default:
                break;
        }
    }


    private void initSemesterOption(){
        mSemesterOptionsPickerView = new OptionsPickerView(getActivity());
        mSemesterOptionsPickerView.setPicker((ArrayList) mSemesterStringList);
        mSemesterOptionsPickerView.setSelectOptions(semesterPosition);
        mSemesterOptionsPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                if(semesterPosition == options1 && isCanClickWeekSelect){
                    return;  //选的是同一个且周不为空，就不用重新获取
                }
                semesterPosition = options1;
                mSemesterTextView.setText(mSemesterStringList.get(semesterPosition));
                //获取周列表
                getWeekInfo();
            }
        });
        mSemesterOptionsPickerView.setCyclic(false);
    }

    private void initWeekOption(){
        mWeekOptionsPickerView = new OptionsPickerView(getActivity());
        isCanClickWeekSelect = false;  //是否能进行周选择
        List<SemessterAndWeeklyInfo> infoList = new ArrayList<>();
        mSemesterScheduleAdapter = new SemesterScheduleAdapter(mActivity,infoList);
        mRecyclerView.setAdapter(mSemesterScheduleAdapter);
    }

    private void setWeekOption(){
        mWeekOptionsPickerView.setPicker((ArrayList) mWeekStringList);
        mWeekOptionsPickerView.setSelectOptions(weekPosition);
        mWeekOptionsPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                if(!isCanClickWeekSelect){
                    return;  //不能进行选择的返回，否则都重新去获取数据
                }
                weekPosition = options1;
                mWeekTextView.setText(mWeekStringList.get(weekPosition));
                //获取选中周的内容
                getDetailWeeklyInfo(false);
            }
        });
        mWeekOptionsPickerView.setCyclic(false);
    }

    @Override
    public void getSemesterData(List<TermInfo> semesterArrayList) {
        mAllEmptyTextView.setVisibility(View.GONE);
        mSemesterList = semesterArrayList;
        mSemesterStringList = presenter.getSemesterStringList(mSemesterList);
        semesterPosition = presenter.getCurrentSemesterPosition(mSemesterList);
        mSemesterTextView.setText(mSemesterList.get(semesterPosition).getSemesterName());
        //初始化学期选项
        initSemesterOption();
        //初始化学期的周
        initWeekOption();
        //获取周列表
        getWeekInfo();
    }

    private void getWeekInfo(){
        int termId = mSemesterList.get(semesterPosition).getSemesterId();
        presenter.getWeekData(termId);
    }


    @Override
    public void getWeekData(TermWeekInfo termWeekInfo) {
//默认显示空界面
        mListEmptyTextView.setVisibility(View.VISIBLE);
        //获取周期成功的要开启可选week的操作
        isCanClickWeekSelect = true;
        //初始化周数据
        mWeekList = termWeekInfo.getSemester();
        mWeekStringList = presenter.getWeekStringList(mWeekList);
        int currentWeek = termWeekInfo.getCurrentWeek();
        weekPosition = 0;  //默认先给选中第一个
        if(currentWeek != 0){
            for(int i = 0; i< mWeekList.size(); i++){
                if(mWeekList.get(i).getWeek() == currentWeek){
                    weekPosition = i;
                }
            }
        }
        mWeekTextView.setText(mWeekStringList.get(weekPosition));
        //重新设置周的列表
        setWeekOption();
        //获取选中周的内容
        getDetailWeeklyInfo(false);
    }

    private void getDetailWeeklyInfo(boolean isRefresh){
        String beginDate = mWeekList.get(weekPosition).getWeekStartDate();
        String endDate = mWeekList.get(weekPosition).getWeekEndDate();
        presenter.getDetailInfoData(beginDate,endDate,isRefresh);
    }

    @Override
    public void showAllEmptyPageWhenGetSemesterFailed() {
        //获取学期失败显示
        mAllEmptyTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void getWeeklyScheduleData(List<SemessterAndWeeklyInfo> semessterAndWeeklyInfoList) {
        mRecyclerView.stopRefresh(true);
        if(semessterAndWeeklyInfoList.size()==0){
            //默认显示空界面
            mSemesterScheduleAdapter.clearData();
            mListEmptyTextView.setVisibility(View.VISIBLE);
            return;
        }
        mListEmptyTextView.setVisibility(View.GONE);
        mSemesterScheduleAdapter = new SemesterScheduleAdapter(mActivity,semessterAndWeeklyInfoList);
        mRecyclerView.setAdapter(mSemesterScheduleAdapter);
    }

    @Override
    public void showListEmptyPage(boolean isGetDetailFailed) {
        mRecyclerView.stopRefresh(true);
        mSemesterScheduleAdapter.clearData();
        mListEmptyTextView.setVisibility(View.VISIBLE);  //空页面
        if(isGetDetailFailed){
            isCanClickWeekSelect = true;
        }else {
            //获取周失败
            isCanClickWeekSelect = false;
            mWeekTextView.setText("空");
        }
    }

    @Override
    public void onRefreshError() {
        mRecyclerView.stopRefresh(false);
    }



}
