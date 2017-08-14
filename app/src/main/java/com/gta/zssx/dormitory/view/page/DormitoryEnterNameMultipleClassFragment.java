package com.gta.zssx.dormitory.view.page;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.gta.utils.resource.Toast;
import com.gta.zssx.R;
import com.gta.zssx.dormitory.model.bean.DetailItemBean;
import com.gta.zssx.dormitory.model.bean.DormitoryEnterNameMultipleItemInfoBeanV2;
import com.gta.zssx.dormitory.model.bean.EnterNameMultipleItemBean;
import com.gta.zssx.dormitory.model.bean.RxDormitoryRankingListUpdateBean;
import com.gta.zssx.dormitory.model.bean.RxRefreshDormitoryListBean;
import com.gta.zssx.dormitory.model.bean.RxSaveNeedToOpt;
import com.gta.zssx.dormitory.presenter.DormitoryEnterNameMultipleClassPresenter;
import com.gta.zssx.dormitory.view.DormitoryEnterNameMultipleClassView;
import com.gta.zssx.dormitory.view.adapter.EnterNameMultipleClassDimensionAdapter;
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
 * 录入姓名多项-班级维度Fragment
 * [How to use]
 * 用于显示录入姓名多项班级维度页面，选中Item和学生及其班级得分联动
 * [Tips]
 *
 * Created by lan.zheng on 2017/7/24 13:10.
 */

public class DormitoryEnterNameMultipleClassFragment  extends NextBaseMvpFragment<DormitoryEnterNameMultipleClassView, DormitoryEnterNameMultipleClassPresenter> implements DormitoryEnterNameMultipleClassView,EnterNameMultipleClassDimensionAdapter.Listener{

    /**
     * 缓存当前班级得分和备注
     */
    private String cacheCurClassScore;
    private String cacheCurClassRemark;

    /**
     * 缓存页面数据实体
     */
    private HashMap<String, DormitoryEnterNameMultipleItemInfoBeanV2> cacheOptionBeanMap = new HashMap<>();
    /**
     * 用户作修改的实体数据，修改之后的
     */
    private DormitoryEnterNameMultipleItemInfoBeanV2 mModifyBean;

    /**
     * 网络请求实体
     */
    private DormitoryEnterNameMultipleItemInfoBeanV2 dormitoryEnterNameMultipleItemInfoBeanV2;


    /**
     * 是否显示减号
     */
    private TextView minusTextView;

    /**
     * 班级维度的MyExpandListView
     */
    private MyExpandListView classExpandListView;

    /**
     * 班级维度总分数
     */
    private TextView classScoreTextView;

    /**
     * 班级维度备注
     */
    private EditText classRemarkEditText;

    /**
     *  录入姓名多项班级维度
     */
    private EnterNameMultipleClassDimensionAdapter enterNameMultipleClassDimensionAdapter;
    private List<Set<Integer>> checkListList;
    private List<Float> scoreList;
    private int mActionType = 0;  //0.新增  1.修改  2.查看
    private int mAdditionOrSubtraction = 0;  //1.增分 0.减分
    private boolean mIsCanEdit = true; //true：表示可以进行编辑和修改   false：表示只能查看
    private DormitoryOrClassIndexPointActivity mActivity;  //拿到Activity
    public String BuildingOrDeptId;  //宿舍楼或专业部集合Id字符串
    public boolean isChangedRemark = false;  //默认刚进入都是false,因为一开始会进行赋值，可能会导致EditText变化，所以需要标志位
    public boolean isGetDataFailed = false;  //是否成功获取数据，未获取数据成功的时候，备注输入保存按钮也不能亮起来

    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(isGetDataFailed){
                Toast.Short(getActivity(),"请重新获取数据后再填写备注");
                return;
            }
            if(!isChangedRemark){  //第一次是赋值，不算改变，所以不改变状态直接return
//                Log.e("lenita","第一次赋值，按钮不变");
                isChangedRemark = true;
                return;
            }
            isChanged = true;
            mActivity.setSaveButtonClickable(isChanged);
        }
    };

    public static DormitoryEnterNameMultipleClassFragment newInstance( int isAdditionOrSubtraction, int actionType) {
        Bundle args = new Bundle();
        args.putInt(KEY_ADDITION_OR_SUBTRACTION,isAdditionOrSubtraction);
        args.putInt(KEY_IS_CAN_MODIFY,actionType);
        DormitoryEnterNameMultipleClassFragment fragment = new DormitoryEnterNameMultipleClassFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_enter_name_multiple_item_class_dimension;
    }

    @Override
    protected void initBundle(Bundle bundle) {
        super.initData();
        mActionType = bundle.getInt(KEY_IS_CAN_MODIFY); // 操作
        mAdditionOrSubtraction = bundle.getInt(KEY_ADDITION_OR_SUBTRACTION); // 增加或扣分
        //默认初值“0”
        BuildingOrDeptId = "0";
    }

    @Override
    protected void initView(View view) {
        mActivity = (DormitoryOrClassIndexPointActivity) getActivity();
        classScoreTextView = (TextView)view.findViewById(R.id.tv_dormitory_class_score_show);
        classRemarkEditText = (EditText)view.findViewById(R.id.et_class_remark);
        classRemarkEditText.addTextChangedListener(mTextWatcher);
        classExpandListView = (MyExpandListView)view.findViewById(R.id.elv_multiple_item_student);
        classExpandListView.setGroupIndicator(null);
        minusTextView = (TextView)view.findViewById(R.id.tv_minus);
        scoreList = new ArrayList<>();
        checkListList = new ArrayList<>();

        //是新增/编辑/查看进入,根据传入值判断
        if(mActionType == Constant.ACTION_TYPE_JUST_CHECK){
            //查看不能编辑
            mIsCanEdit = false;
        }else {
            //新增和修改可编辑
            mIsCanEdit = true;
        }
        classRemarkEditText.setFocusable(mIsCanEdit);
        classRemarkEditText.setFocusableInTouchMode(mIsCanEdit);
        classRemarkEditText.setEnabled(mIsCanEdit);
        cacheCurClassScore = "";
        isChanged = false;
    }

    @Override
    public void requestData() {
        super.requestData();
        isChangedRemark = false; //第一次进入都是备注赋值，所以这个值一定是false
        if (cacheOptionBeanMap.get(mActivity.currentPosition) == null) {
            //没有缓存数据从网络请求
            presenter.getEnterNameMultipleItemClassInfo(mActivity.InputDate,mActivity.ItemId,String.valueOf(mActivity.dormitoryOrClassId),mActionType);
        }else {
            //有缓存
            dormitoryEnterNameMultipleItemInfoBeanV2 = BeanCloneUtil.cloneTo(cacheOptionBeanMap.get(mActivity.currentPosition)); // 浅拷贝，否则是引用传递
            dormitoryEnterNameMultipleItemInfoBeanV2.setDormitoryOrClassId(mActivity.dormitoryOrClassId);
            //TODO 改变都存入mModifyBean，不能操作缓存中的数据
            mModifyBean = BeanCloneUtil.cloneTo(dormitoryEnterNameMultipleItemInfoBeanV2);
            if(TextUtils.isEmpty(mModifyBean.getClassScore())){
                cacheCurClassScore = "";
            }else {
                String score = mModifyBean.getClassScore();
                cacheCurClassScore = presenter.formatScore(score);
            }
            cacheCurClassRemark = mModifyBean.getRemark();
            isGetDataFailed = false;//有缓存这个值为false
            setClassScoreAndRemarkValue();
            //刷新数据
            formatCheckListListAndTotalScoreList(mModifyBean);
            refreshData(mModifyBean);
        }
    }

    private void formatCheckListListAndTotalScoreList(DormitoryEnterNameMultipleItemInfoBeanV2 dormitoryEnterNameMultipleItemInfoBeanV2){
        scoreList.clear();
        checkListList.clear();
        for (int i = 0; i < dormitoryEnterNameMultipleItemInfoBeanV2.getStudentList().size();i++){
            List<DetailItemBean> detailItemBeanList = dormitoryEnterNameMultipleItemInfoBeanV2.getStudentList().get(i).getIndexItemBeanList();
            //分数统计
            float itemScore = 0;
            if(!TextUtils.isEmpty(dormitoryEnterNameMultipleItemInfoBeanV2.getStudentList().get(i).getStudentScore())){
                itemScore = Float.parseFloat(dormitoryEnterNameMultipleItemInfoBeanV2.getStudentList().get(i).getStudentScore());
            }
            scoreList.add(new Float(itemScore));
            //如果是勾选的，把位置的值放入checkList
            List<Integer> checkList = new ArrayList<>();
            for(int j = 0 ;j < detailItemBeanList.size();j++){
                if(detailItemBeanList.get(j).getDetailItemCheck()){
                    checkList.add(new Integer(j ));
                }
            }
            //每个大条目的勾选情况存入总表
            checkListList.add(new HashSet<Integer>(checkList));
        }
    }


    /**
     * 刷新选项里面数据(勾选状态)，并且刷新宿舍得分
     *
     * @param optionBean 当前录分页数据实体
     */
    private void refreshData(DormitoryEnterNameMultipleItemInfoBeanV2 optionBean) {
        initExpandListView(optionBean.getStudentList());
    }

    private void initExpandListView(List<EnterNameMultipleItemBean> studentList) {
        classExpandListView.setVisibility(View.VISIBLE);
        if(enterNameMultipleClassDimensionAdapter == null) {
            enterNameMultipleClassDimensionAdapter = new EnterNameMultipleClassDimensionAdapter(getActivity(), studentList, checkListList,mIsCanEdit,mAdditionOrSubtraction,this);
            classExpandListView.setAdapter(enterNameMultipleClassDimensionAdapter);
        } else {
            enterNameMultipleClassDimensionAdapter.setmDormitoryTitleStudentBeanList(studentList, checkListList);
        }
        //进行高度计算,为了备注的显示
        ViewGroup.LayoutParams lLayoutParams = classExpandListView.getLayoutParams();
        lLayoutParams.height = getHeight();
        classExpandListView.setLayoutParams(lLayoutParams);
        classExpandListView.requestLayout();

        classExpandListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                enterNameMultipleClassDimensionAdapter.updateChecked(groupPosition);  //保证图标改变
                return false;
            }
        });
    }


    /**
     * 更新整个页面的学生数据 - 当可编辑状态的时候才会有回调数据
     * @param enterNameMultipleItemBeanList 总表
     * @param groupPosition 改动的条目位置
     * @param itemScore 一个学生的总分数
     * @param checkList 针对一个条目勾选状态返回
     */
    @Override
    public void sendNewInfoStudentBack(List<EnterNameMultipleItemBean> enterNameMultipleItemBeanList, int groupPosition, float itemScore,Set<Integer> checkList) {
        //更新勾选表
        checkListList.get(groupPosition).clear();
        checkListList.get(groupPosition).addAll(checkList);
        //更新一个学生的总分
        scoreList.set(groupPosition,new Float(itemScore));
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
                cacheCurClassScore =  presenter.formatScore(score);
            }
        }else {
            cacheCurClassScore = "";
        }
        setClassScore();
        //更新要保存的数据总表
        mModifyBean.setStudentList(enterNameMultipleItemBeanList);
        mModifyBean.setClassScore(cacheCurClassScore);
        //分数改变后按钮亮起来
        isChanged = true;
        mActivity.setSaveButtonClickable(isChanged);

    }

    private void setClassScore() {
        classScoreTextView.setText(cacheCurClassScore);
        if(mAdditionOrSubtraction == Constant.Subtraction){
            if(!TextUtils.isEmpty(cacheCurClassScore) && Float.valueOf(cacheCurClassScore) > 0) {
                minusTextView.setVisibility(View.VISIBLE);
            }else {
                minusTextView.setVisibility(View.GONE);
            }
        }else {
            minusTextView.setVisibility(View.GONE);
        }
    }


    /**
     * 进行高度计算,为了适配
     */
    private int getHeight(){
        int totalHeight = 0;
        int deliverCount= 0;
        for (int i = 0; i < enterNameMultipleClassDimensionAdapter.getGroupCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listgroupItem = enterNameMultipleClassDimensionAdapter .getGroupView(i, true, null, classExpandListView );
            listgroupItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listgroupItem .getMeasuredHeight(); // 统计所有子项的总高度
            System. out.println("height : group" +i +"次" +totalHeight );
            for (int j = 0; j < enterNameMultipleClassDimensionAdapter.getChildrenCount( i); j++) {
                View listchildItem = enterNameMultipleClassDimensionAdapter .getChildView(i, j, false , null, classExpandListView);
                listchildItem.measure(0, 0); // 计算子项View 的宽高
                totalHeight += listchildItem.getMeasuredHeight(); // 统计所有子项的总高度
                deliverCount++;
            }
        }
//        totalHeight = totalHeight+(classExpandListView.getDividerHeight() * (enterNameMultipleClassDimensionAdapter.getGroupCount() + deliverCount- 1));
        return totalHeight;
    }

    @NonNull
    @Override
    public DormitoryEnterNameMultipleClassPresenter createPresenter() {
        return new DormitoryEnterNameMultipleClassPresenter();
    }

    @Override
    public boolean isDataChanged(){
        return isChanged;
    }

    @Override
    public void saveCurrentPageData() {
        //提交前记得获得班级备注
        mModifyBean.setRemark(classRemarkEditText.getText().toString().trim());
        //TODO 只需要保存改变了的部分
        DormitoryEnterNameMultipleItemInfoBeanV2 dormitoryEnterNameMultipleItemInfoBeanV2 = BeanCloneUtil.cloneTo(mModifyBean);
        //剔除0分的学生 - 取消剔除
//        dormitoryEnterNameMultipleItemInfoBeanV2 = presenter.formatInfoData(dormitoryEnterNameMultipleItemInfoBeanV2);
        //网络请求保存成功之后，添加/更新缓存集合 cacheOptionBeanMap
        if(!mActivity.isAll && mActivity.dormitoryOrClassSingleInfoBeanList != null){
            //保存的时候去获得BuildingOrDeptId字符串
            BuildingOrDeptId = presenter.getBuildingOrDeptId(mActivity.dormitoryOrClassSingleInfoBeanList);
        }
//        Log.e("lenita1","BuildingOrDeptId = "+BuildingOrDeptId);
        presenter.saveEnterNameMultipleItemClassInfo(mActivity.InputDate, mActivity.UserId, mActivity.ItemId, mActivity.RecordId, BuildingOrDeptId,dormitoryEnterNameMultipleItemInfoBeanV2 );
    }



    @Override
    public void showToast(String msg) {
        Toast.Short(getActivity(),msg);
    }

    @Override
    public void getDataInfoFailed() {
        isGetDataFailed = true;
        mActivity.setSaveButtonClickable(false);
    }

    @Override
    public void showDataInfo(DormitoryEnterNameMultipleItemInfoBeanV2 dormitoryEnterNameMultipleItemInfoBeanV2) {
        //保存的时候需要到dormitoryOrClassId数据,所以要先赋值
        dormitoryEnterNameMultipleItemInfoBeanV2.setDormitoryOrClassId(mActivity.dormitoryOrClassId);
        this.dormitoryEnterNameMultipleItemInfoBeanV2 = dormitoryEnterNameMultipleItemInfoBeanV2;
        mModifyBean = BeanCloneUtil.cloneTo(dormitoryEnterNameMultipleItemInfoBeanV2);  //TODO 拷贝一份到修改bean中
        cacheOptionBeanMap.put(mActivity.currentPosition, this.dormitoryEnterNameMultipleItemInfoBeanV2);
        //设置显示的分数和备注
        if(TextUtils.isEmpty(dormitoryEnterNameMultipleItemInfoBeanV2.getClassScore())){
            cacheCurClassScore = "";
        }else {
            String score = dormitoryEnterNameMultipleItemInfoBeanV2.getClassScore();
            cacheCurClassScore = presenter.formatScore(score);
        }
        cacheCurClassRemark = dormitoryEnterNameMultipleItemInfoBeanV2.getRemark() !=null? dormitoryEnterNameMultipleItemInfoBeanV2.getRemark() :"";
        isGetDataFailed = false;//获取数据成功，这个值变为false
        setClassScoreAndRemarkValue();
        //TODO 数据刷新，必须要ModifyBean，因为适配器会直接改变它的值，但是需要存一份原来的缓存
        formatCheckListListAndTotalScoreList(mModifyBean);
        refreshData(mModifyBean);
    }

    /**
     * 设置总分和备注
     */
    private void setClassScoreAndRemarkValue(){
        classRemarkEditText.setText(cacheCurClassRemark);
        setClassScore();
    }


    @Override
    public void saveAndGetRecordId(int trueRecordId) {
        //TODO 若原来的是新增录入，记得返回真正的RecordId
        if(mActivity.RecordId <= 0){
            mActivity.RecordId = trueRecordId;
            //TODO 新增录入的，保存后需要刷新未审核页面
            //rx_dormitory_ranking_list_refresh
            RxDormitoryRankingListUpdateBean dormitoryRankingListUpdateBean = new RxDormitoryRankingListUpdateBean();
            dormitoryRankingListUpdateBean.setNeedToUpdatePage(RxDormitoryRankingListUpdateBean.REFRESH_NOT_SUBMIT_LIST);
            RxBus.getDefault().post(dormitoryRankingListUpdateBean);
        }
        //TODO 赋值操作，请求网络的班级分数和备注存缓存
        dormitoryEnterNameMultipleItemInfoBeanV2 = BeanCloneUtil.cloneTo(mModifyBean); // 保存，把修改后的数据实体重新赋值到缓存集合
        if (!classScoreTextView.getText().toString().trim().isEmpty())  //班级分数
            dormitoryEnterNameMultipleItemInfoBeanV2.setClassScore(classScoreTextView.getText().toString().trim());
        if(!classRemarkEditText.getText().toString().isEmpty())  //班级备注
            dormitoryEnterNameMultipleItemInfoBeanV2.setRemark(classRemarkEditText.getText().toString().trim());
        cacheOptionBeanMap.put(mActivity.currentPosition, dormitoryEnterNameMultipleItemInfoBeanV2); //更新缓存集合

        //保存完后，记得把保存按钮和备注是否改变还原
        isChangedRemark = false;
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
        classExpandListView.setVisibility(View.GONE);
    }
}
