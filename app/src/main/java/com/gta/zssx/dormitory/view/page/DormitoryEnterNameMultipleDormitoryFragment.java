package com.gta.zssx.dormitory.view.page;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.gta.utils.resource.Toast;
import com.gta.zssx.R;
import com.gta.zssx.dormitory.model.bean.ClassInfoBean;
import com.gta.zssx.dormitory.model.bean.DetailItemBean;
import com.gta.zssx.dormitory.model.bean.DormitoryEnterNameMultipleItemInfoBean;
import com.gta.zssx.dormitory.model.bean.EnterNameMultipleItemBean;
import com.gta.zssx.dormitory.model.bean.RxDormitoryRankingListUpdateBean;
import com.gta.zssx.dormitory.model.bean.RxRefreshDormitoryListBean;
import com.gta.zssx.dormitory.model.bean.RxSaveNeedToOpt;
import com.gta.zssx.dormitory.presenter.DormitoryEnterNameMultipleDormitoryPresenter;
import com.gta.zssx.dormitory.view.DormitoryEnterNameMultipleDormitoryView;
import com.gta.zssx.dormitory.view.adapter.EnterNameClassListAdapter;
import com.gta.zssx.dormitory.view.adapter.EnterNameMultipleDormitoryDimensionAdapter;
import com.gta.zssx.pub.widget.FixedScrollRecyclerView;
import com.gta.zssx.fun.coursedaily.registerrecord.view.MyExpandListView;
import com.gta.zssx.pub.base.NextBaseMvpFragment;
import com.gta.zssx.pub.common.Constant;
import com.gta.zssx.pub.util.BeanCloneUtil;
import com.gta.zssx.pub.util.RxBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * [Description]
 * 录入姓名多项-宿舍维度Fragment
 * [How to use]
 * 用于显示录入姓名多项宿舍维度页面，选中Item和学生及其宿舍和班级得分联动
 * [Tips]
 *
 * Created by lan.zheng on 2017/7/24 13:10.
 */

public class DormitoryEnterNameMultipleDormitoryFragment extends NextBaseMvpFragment<DormitoryEnterNameMultipleDormitoryView, DormitoryEnterNameMultipleDormitoryPresenter> implements DormitoryEnterNameMultipleDormitoryView,EnterNameMultipleDormitoryDimensionAdapter.Listener,EnterNameClassListAdapter.Listener{

    /**
     * 缓存当前宿舍得分
     */
    private String cacheCurDormitoryScore;
    /**
     * 缓存页面数据实体
     */
    private HashMap<String, DormitoryEnterNameMultipleItemInfoBean> cacheOptionBeanMap = new HashMap<>();
    /**
     * 用户作修改的实体数据，修改之后的
     */
    private DormitoryEnterNameMultipleItemInfoBean mModifyBean;

    /**
     * 网络请求实体，非修改
     */
    private DormitoryEnterNameMultipleItemInfoBean dormitoryEnterNameMultipleItemInfoBean;

    /**
     * 是否显示减号
     */
    private TextView minusTextView;

    /**
     * 宿舍维度的MyExpandListView
     */
    private MyExpandListView dormitoryExpandListView;

    /**
     * 班级列表
     */
    private FixedScrollRecyclerView classRecyclerView;
    private View viewLine;

    /**
     * 宿舍维度总分数
     */
    private TextView dormitoryScoreTextView;
    /**
     * 请选择扣分学生(用于防止显示错乱)
     */
    private TextView pleaseChooseScoreStudentTextView;
    /**
     *  录入姓名多项宿舍维度
     */
    private EnterNameMultipleDormitoryDimensionAdapter enterNameMultipleDormitoryDimensionAdapter;  //学生条目适配器
    private EnterNameClassListAdapter enterNameClassListAdapter;  //班级列表适配器
    private List<Set<Integer>> checkListList;
    private List<Float> scoreList;
    private List<ClassInfoBean> classInfoBeanList;
    private int mAdditionOrSubtraction = 0;  //增分还是减分
    private int mActionType = 0;  //0.新增  1.修改  2.查看
    private boolean mIsCanEdit = true; //true：表示可以进行编辑和修改   false：表示只能查看
    private DormitoryOrClassIndexPointActivity mActivity;  //拿到Activity
    public String BuildingOrDeptId = "0";  //宿舍楼或专业部集合Id字符串
//    public boolean isChangedRemark = false;
    public int firstClassInitCount = 0; //第一次进入都是初始化班级列表，不算改变备注

    public static DormitoryEnterNameMultipleDormitoryFragment newInstance( int isAdditionOrSubtraction, int actionType) {
        Bundle args = new Bundle();
        args.putInt(KEY_ADDITION_OR_SUBTRACTION,isAdditionOrSubtraction);
        args.putInt(KEY_IS_CAN_MODIFY,actionType);
        DormitoryEnterNameMultipleDormitoryFragment fragment = new DormitoryEnterNameMultipleDormitoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_enter_name_multiple_item_dormitory_dimension;
    }

    @Override
    protected void initBundle(Bundle bundle) {
        super.initData();
        mActionType = bundle.getInt(KEY_IS_CAN_MODIFY); // 操作
        mAdditionOrSubtraction = bundle.getInt(KEY_ADDITION_OR_SUBTRACTION); // 增加或扣分
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(!mActivity.isAll && mActivity.dormitoryOrClassSingleInfoBeanList != null){
            //保存的时候去获得BuildingOrDeptId字符串
            BuildingOrDeptId = presenter.getBuildingOrDeptId(mActivity.dormitoryOrClassSingleInfoBeanList);
        }
    }

    @Override
    protected void initView(View view) {
        mActivity = (DormitoryOrClassIndexPointActivity) getActivity();
        dormitoryScoreTextView = (TextView)view.findViewById(R.id.tv_dormitory_score_show);
        classRecyclerView = (FixedScrollRecyclerView)view.findViewById(R.id.rv_multiple_item_class);
        dormitoryExpandListView = (MyExpandListView)view.findViewById(R.id.elv_multiple_item_student);
        dormitoryExpandListView.setGroupIndicator(null);
        classRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        classRecyclerView.setVisibility(View.VISIBLE);  //TODO 宿舍维度的要显示班级列表
        viewLine = view.findViewById(R.id.view_line);
        viewLine.setVisibility(View.VISIBLE);
        pleaseChooseScoreStudentTextView = (TextView)view.findViewById(R.id.tv_title_please_select_student_item);
        minusTextView = (TextView)view.findViewById(R.id.tv_minus_sign);
        //初始化值
        checkListList = new ArrayList<>();
        scoreList = new ArrayList<>();
        classInfoBeanList = new ArrayList<>();
        //是新增/编辑/查看进入,根据传入值判断
        if(mActionType == Constant.ACTION_TYPE_JUST_CHECK){
            //查看不能编辑
            mIsCanEdit = false;
        }else {
            //新增和修改可编辑
            mIsCanEdit = true;
        }
        cacheCurDormitoryScore = "";
        isChanged = false;
    }

    @Override
    public void requestData() {
        super.requestData();
        if (cacheOptionBeanMap.get(mActivity.currentPosition) == null) {
            //没有缓存数据从网络请求
            presenter.getEnterNameMultipleItemDormitoryInfo(mActivity.InputDate,mActivity.ItemId,String.valueOf(mActivity.dormitoryOrClassId),mActionType);
        }else {
            //缓存数据
            dormitoryEnterNameMultipleItemInfoBean = BeanCloneUtil.cloneTo(cacheOptionBeanMap.get(mActivity.currentPosition)); // 浅拷贝，否则是引用传递
            dormitoryEnterNameMultipleItemInfoBean.setDormitoryOrClassId(mActivity.dormitoryOrClassId);
            mModifyBean = BeanCloneUtil.cloneTo(dormitoryEnterNameMultipleItemInfoBean);
            if(TextUtils.isEmpty(mModifyBean.getDormitoryScore())){
                cacheCurDormitoryScore = "";
            }else {
                String score = mModifyBean.getDormitoryScore();
                cacheCurDormitoryScore = presenter.formatScore(score);
            }
            setDormitoryScore();
            firstClassInitCount = mModifyBean.getClassList().size();
            //刷新数据
            formatCheckListListAndTotalScoreList(mModifyBean);
            refreshData(mModifyBean);
        }

    }

    private void initClassList(List<ClassInfoBean> mClassInfoBeanList){
        classInfoBeanList.clear();
        classInfoBeanList.addAll(mClassInfoBeanList);
        if(enterNameClassListAdapter == null) {
            //班级列表适配器
            enterNameClassListAdapter = new EnterNameClassListAdapter(getActivity(),classInfoBeanList,mIsCanEdit,mAdditionOrSubtraction,this);
            classRecyclerView.setAdapter(enterNameClassListAdapter);
        } else {
            enterNameClassListAdapter.refreshData(classInfoBeanList);
        }
        classRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * 班级备注修改监听
     * @param position 位置
     * @param remark 备注
     */
    @Override
    public void enterRemark(int position, String remark) {
        //TODO 更新对应位置班级列表信息
        if(firstClassInitCount > 0){
            firstClassInitCount -- ;
        }else {
            isChanged = true;
            mActivity.setSaveButtonClickable(isChanged);
        }
        classInfoBeanList.get(position).setClassSingleRemark(remark);
        mModifyBean.setClassList(classInfoBeanList); //每次都重新设置infobean
    }


    private void formatCheckListListAndTotalScoreList(DormitoryEnterNameMultipleItemInfoBean dormitoryEnterNameMultipleItemInfoBean){
        scoreList.clear();
        checkListList.clear();
        for (int i = 0; i < dormitoryEnterNameMultipleItemInfoBean.getStudentList().size();i++){
            List<DetailItemBean> detailItemBeanList = dormitoryEnterNameMultipleItemInfoBean.getStudentList().get(i).getIndexItemBeanList();
            //TODO 分数统计
            float itemScore = 0;
            if(!TextUtils.isEmpty(dormitoryEnterNameMultipleItemInfoBean.getStudentList().get(i).getStudentScore())){
                itemScore = Float.parseFloat(dormitoryEnterNameMultipleItemInfoBean.getStudentList().get(i).getStudentScore());
            }

            scoreList.add(new Float(itemScore));
            //TODO 如果是勾选的，把位置的值放入checkList
            List<Integer> checkList = new ArrayList<>();
            for(int j = 0 ;j < detailItemBeanList.size();j++){
                if(detailItemBeanList.get(j).getDetailItemCheck()){
                    checkList.add(new Integer(j ));
                }
            }
            //TODO 每个大条目的勾选情况存入总表
            checkListList.add(new HashSet<Integer>(checkList));
        }
    }


    /**
     * 刷新选项里面数据(勾选状态)，并且刷新宿舍得分
     *
     * @param optionBean 当前录分页数据实体
     */
    private void refreshData(DormitoryEnterNameMultipleItemInfoBean optionBean) {
        initExpandListView(optionBean.getStudentList());  //学生床位列表显示
        initClassList(optionBean.getClassList());  //班级列表显示
    }

    private void initExpandListView(List<EnterNameMultipleItemBean> studentList) {
        classRecyclerView.setVisibility(View.VISIBLE);
        dormitoryExpandListView.setVisibility(View.VISIBLE);
        if(enterNameMultipleDormitoryDimensionAdapter == null) {
            enterNameMultipleDormitoryDimensionAdapter = new EnterNameMultipleDormitoryDimensionAdapter(getActivity(),studentList, checkListList,mIsCanEdit,mAdditionOrSubtraction,this);
            dormitoryExpandListView.setAdapter(enterNameMultipleDormitoryDimensionAdapter);
        } else {
            enterNameMultipleDormitoryDimensionAdapter.setmDormitoryTitleStudentBeanList(studentList, checkListList);
        }
        //进行高度计算,为了备注的显示
        ViewGroup.LayoutParams lLayoutParams = dormitoryExpandListView.getLayoutParams();
        lLayoutParams.height = getHeight();
        dormitoryExpandListView.setLayoutParams(lLayoutParams);
        dormitoryExpandListView.requestLayout();

        dormitoryExpandListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                enterNameMultipleDormitoryDimensionAdapter.updateChecked(groupPosition); //保证图表改变
                return false;
            }
        });
        //TODO 防止展开位置的错位
        pleaseChooseScoreStudentTextView.setFocusable(true);
        pleaseChooseScoreStudentTextView.setFocusableInTouchMode(true);
        pleaseChooseScoreStudentTextView.requestFocus();
    }

    /**
     *  更新整个页面的学生数据
     * @param enterNameMultipleItemBeanList 总表
     * @param groupPosition 改动的条目位置
     * @param itemScore 一个学生的总分数
     * @param checkList 针对一个条目勾选状态返回
     */
    @Override
    public void sendNewInfoStudentBack(List<EnterNameMultipleItemBean> enterNameMultipleItemBeanList, int groupPosition, float itemScore, Set<Integer> checkList) {
        //更新勾选表
        checkListList.get(groupPosition).clear();
        checkListList.get(groupPosition).addAll(checkList);
        //更新一个学生的总分
        scoreList.set(groupPosition,new Float(itemScore));
        //更新宿舍总分
        float totalScore = 0;
        for(int i = 0;i < scoreList.size();i++){
            totalScore += scoreList.get(i);
        }
        boolean isNeedToShowClassTotalScore = presenter.isHaveStudentScore(enterNameMultipleItemBeanList);
        //更新整个班级显示分数,有学生有分数才显示，否则为空
        if(isNeedToShowClassTotalScore){
            float f = (float)(Math.round(totalScore*1000))/1000;
            if(f >= 0 ){
                String score = String.valueOf(f);
                cacheCurDormitoryScore =  presenter.formatScore(score);
            }
        }else {
            cacheCurDormitoryScore = "";
        }
        setDormitoryScore();
        //更新总表
        presenter.setNewClassScore(classInfoBeanList,mModifyBean.getStudentList(),scoreList);
        mModifyBean.setStudentList(enterNameMultipleItemBeanList);
        mModifyBean.setDormitoryScore(cacheCurDormitoryScore);
        mModifyBean.setClassList(classInfoBeanList);
        //更新每个班级的分数,并刷新适配器
        enterNameClassListAdapter.refreshData(classInfoBeanList);
        //分数改变后按钮亮起来
        isChanged = true;
        mActivity.setSaveButtonClickable(isChanged);


    }

    /**
     * 进行高度计算,为了适配
     */
    private int getHeight(){
        int totalHeight = 0;
        int deliverCount= 0;
        for (int i = 0; i < enterNameMultipleDormitoryDimensionAdapter.getGroupCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listgroupItem = enterNameMultipleDormitoryDimensionAdapter .getGroupView(i, true, null, dormitoryExpandListView );
            listgroupItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listgroupItem .getMeasuredHeight(); // 统计所有子项的总高度
            for (int j = 0; j < enterNameMultipleDormitoryDimensionAdapter.getChildrenCount( i); j++) {
                View listchildItem = enterNameMultipleDormitoryDimensionAdapter .getChildView(i, j, false , null, dormitoryExpandListView);
                listchildItem.measure(0, 0); // 计算子项View 的宽高
                totalHeight += listchildItem.getMeasuredHeight(); // 统计所有子项的总高度
                deliverCount++;
            }
        }
//        totalHeight = totalHeight+(dormitoryExpandListView.getDividerHeight() * (enterNameMultipleDormitoryDimensionAdapter.getGroupCount() + deliverCount- 1));
        return totalHeight;
    }

    @NonNull
    @Override
    public DormitoryEnterNameMultipleDormitoryPresenter createPresenter() {
        return new DormitoryEnterNameMultipleDormitoryPresenter();
    }

    @Override
    public boolean isDataChanged(){
        return isChanged;
    }

    @Override
    public void saveCurrentPageData() {
        // 只需要保存改变了的部分
        DormitoryEnterNameMultipleItemInfoBean dormitoryEnterNameMultipleItemInfoBean = BeanCloneUtil.cloneTo(mModifyBean);
        presenter.saveEnterNameMultipleItemDormitoryInfo(mActivity.InputDate, mActivity.UserId, mActivity.ItemId, mActivity.RecordId, BuildingOrDeptId,dormitoryEnterNameMultipleItemInfoBean);
    }

    @Override
    public void showToast(String msg) {
        Toast.Short(getActivity(),msg);
    }

    @Override
    public void getDataInfoFailed() {
        //TODO 获取数据失败
        mActivity.setSaveButtonClickable(false);
    }

    @Override
    public void showDataInfo(DormitoryEnterNameMultipleItemInfoBean dormitoryEnterNameMultipleItemInfoBean) {
        dormitoryEnterNameMultipleItemInfoBean.setDormitoryOrClassId(mActivity.dormitoryOrClassId);
        this.dormitoryEnterNameMultipleItemInfoBean = dormitoryEnterNameMultipleItemInfoBean;
        mModifyBean = BeanCloneUtil.cloneTo(dormitoryEnterNameMultipleItemInfoBean);  //TODO 拷贝一份到修改bean中
        cacheOptionBeanMap.put(mActivity.currentPosition, this.dormitoryEnterNameMultipleItemInfoBean);
        //请求网络的宿舍分数赋值
        if(TextUtils.isEmpty(dormitoryEnterNameMultipleItemInfoBean.getDormitoryScore())){
            cacheCurDormitoryScore = "";
        }else {
            String score = dormitoryEnterNameMultipleItemInfoBean.getDormitoryScore();
            cacheCurDormitoryScore = presenter.formatScore(score);
        }
        setDormitoryScore();
        firstClassInitCount = mModifyBean.getClassList().size();
        //数据刷新
        formatCheckListListAndTotalScoreList(mModifyBean);
        refreshData(mModifyBean);
    }

    /**
     * 设置总分和备注
     */
    private void setDormitoryScore(){
        dormitoryScoreTextView.setText(cacheCurDormitoryScore);
        if(mAdditionOrSubtraction == Constant.Subtraction){
            if(!TextUtils.isEmpty(cacheCurDormitoryScore) && Float.valueOf(cacheCurDormitoryScore) > 0) {
                minusTextView.setVisibility(View.VISIBLE);
            }else {
                minusTextView.setVisibility(View.GONE);
            }
        }else {
            minusTextView.setVisibility(View.GONE);
        }
    }


    @Override
    public void saveAndGetRecordId(int trueRecordId) {
        //TODO 若原来的是新增录入，记得返回真正的RecordId
        if(mActivity.RecordId <=  0){
            mActivity.RecordId = trueRecordId;
            //TODO 新增录入的，保存后需要刷新未审核页面
            //rx_dormitory_ranking_list_refresh
            RxDormitoryRankingListUpdateBean dormitoryRankingListUpdateBean = new RxDormitoryRankingListUpdateBean();
            dormitoryRankingListUpdateBean.setNeedToUpdatePage(RxDormitoryRankingListUpdateBean.REFRESH_NOT_SUBMIT_LIST);
            RxBus.getDefault().post(dormitoryRankingListUpdateBean);
        }
        //TODO 保存成功后更新数据集合
        dormitoryEnterNameMultipleItemInfoBean = BeanCloneUtil.cloneTo(mModifyBean); // 保存，把修改后的数据实体重新赋值到缓存集合
        if (!dormitoryScoreTextView.getText().toString().trim().isEmpty())  //班级分数
            dormitoryEnterNameMultipleItemInfoBean.setDormitoryScore(dormitoryScoreTextView.getText().toString().trim());
        cacheOptionBeanMap.put(mActivity.currentPosition, dormitoryEnterNameMultipleItemInfoBean);
        //保存完后，记得把保存按钮和备注是否改变还原
        isChanged = false;
        mActivity.setSaveButtonClickable(isChanged);
        //update dormitory list
        RxBus.getDefault().post(new RxRefreshDormitoryListBean());
        //rx_dormitory_finish_new_ranking_page
        RxBus.getDefault().post(new RxSaveNeedToOpt());
        showToast("保存成功");
        //TODO 确定保存成功了才能改变位置并显示上一宿舍或下一宿舍的内容
        mActivity.saveSuccessAndResetButton();
    }

    @Override
    public void saveDataFailHandle(String msg) {
        Toast.Short(getActivity(),msg);
        isChanged = false;
        mActivity.setSaveButtonClickable(false);
    }

    @Override
    public void showOnFailReloading(String error) {
        super.showOnFailReloading(error);
        hideRecyclerView();
    }

    private void hideRecyclerView() {
        classRecyclerView.setVisibility(View.GONE);
        dormitoryExpandListView.setVisibility(View.GONE);
    }
}



