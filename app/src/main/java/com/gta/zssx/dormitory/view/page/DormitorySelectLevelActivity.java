package com.gta.zssx.dormitory.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.gta.utils.resource.Toast;
import com.gta.zssx.R;
import com.gta.zssx.dormitory.model.bean.DormitoryOrClassSingleInfoBean;
import com.gta.zssx.dormitory.presenter.DormitorySelectLevelPresenter;
import com.gta.zssx.dormitory.view.DormitorySelectLevelView;
import com.gta.zssx.dormitory.view.adapter.SelectLevelListApdater;
import com.gta.zssx.pub.base.BaseMvpActivity;
import com.gta.zssx.pub.common.Constant;
import com.gta.zssx.pub.widget.ClearEditText;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * [Description]
 * 宿舍楼或专业部选择页Activity
 * [How to use]
 * 用于宿舍楼或专业部选择
 * [Tips]
 *
 * Created by lan.zheng on 2017/7/24 13:16.
 */

public class DormitorySelectLevelActivity extends BaseMvpActivity<DormitorySelectLevelView,DormitorySelectLevelPresenter> implements DormitorySelectLevelView,View.OnClickListener {

    public static final String PAGE_TAG = DormitorySelectLevelActivity.class.getSimpleName();
    public static final String ITEM_DIMENSION = "item_dimension";
    private final static String NEED_TO_GET_DATA_AGAIN = "need_to_get_data_again";

    public ClearEditText searchEditText; //搜索框
    public RecyclerView recyclerView; //列表
    public TextView sureTextView;  //确定按钮
    public int mItemBelongDimension; //维度
    public List<DormitoryOrClassSingleInfoBean> mDormitoryOrClassInfoBeanList; //全部的宿舍楼或专业部
    public List<DormitoryOrClassSingleInfoBean> mRecordList;  //勾选的宿舍楼或专业部
    public Set<Integer> mCheckList;  //勾选的坐标集合
    public SelectLevelListApdater selectLevelListApdater; //适配器
    public boolean isNeedToGetDataAgain = false;  //默认不需要再次去获取，但是在外面有可能会获取失败，所以要尽心判断
    public boolean isCanClickSureBtn = false;  //是否能点击确定按钮

    /**
     * 入口
     * @param context context
     * @param DormitoryOrClass 维度
     * @param REQUEST_CODE 请求码
     * @param dormitoryOrClassInfoBeanList 列表
     * @param checkList 勾选列表
     */
    public static void startActivityforResult(Context context, int DormitoryOrClass, int REQUEST_CODE, List<DormitoryOrClassSingleInfoBean> dormitoryOrClassInfoBeanList, Set<Integer> checkList,boolean isNeedToGetDataAgain) {
        final Intent lIntent = new Intent(context, DormitorySelectLevelActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(ITEM_DIMENSION,DormitoryOrClass);
        bundle.putSerializable(DormitoryNewRankingActivity.DORMITORY_OR_CLASS_LIST, (Serializable) dormitoryOrClassInfoBeanList);
        bundle.putSerializable(DormitoryNewRankingActivity.CHECK_LIST_FLAG, (Serializable) checkList);
        bundle.putBoolean(NEED_TO_GET_DATA_AGAIN,isNeedToGetDataAgain);
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        lIntent.putExtras(bundle);
        ((DormitoryNewRankingActivity)context).startActivityForResult(lIntent,REQUEST_CODE);
    }

    TextWatcher mSearchTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //编辑中
            String mKeyWord = s.toString ().trim ();
            selectLevelListApdater.enterKeyWord(mKeyWord);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected void initData() {
        super.initData();
        mItemBelongDimension = Constant.DimensionType_Dormitory;  //默认先给宿舍维度
        mDormitoryOrClassInfoBeanList = new ArrayList<>();
        mRecordList = new ArrayList<>();
        mCheckList = new HashSet<>();

        //真实传入的值赋值
        mItemBelongDimension = getIntent().getExtras().getInt(ITEM_DIMENSION,0);
        mDormitoryOrClassInfoBeanList = (List<DormitoryOrClassSingleInfoBean>) getIntent().getExtras().getSerializable(DormitoryNewRankingActivity.DORMITORY_OR_CLASS_LIST);
        mCheckList = (Set<Integer>) getIntent().getExtras().getSerializable(DormitoryNewRankingActivity.CHECK_LIST_FLAG);
        boolean isNeed = getIntent().getExtras().getBoolean(NEED_TO_GET_DATA_AGAIN,true);
        if(mDormitoryOrClassInfoBeanList == null || mDormitoryOrClassInfoBeanList.size() == 0 || isNeed){
            //当为空或者因为后台宿舍楼或专业部被删除的情况，需要重新获取最新的宿舍楼或专业部列表
            mRecordList.clear();
            mCheckList.clear();
            isNeedToGetDataAgain = true;
            isCanClickSureBtn = false;
        }else {
            //根据勾选的组装一份初始的记录集合
            mRecordList = presenter.formatRecordList(mDormitoryOrClassInfoBeanList,mCheckList);
            isCanClickSureBtn = true;
        }
    }



    @Override
    protected void initView() {

        searchEditText = (ClearEditText)findViewById(R.id.search_edittext_dormitory_or_class);
        searchEditText.addTextChangedListener(mSearchTextWatcher);
        recyclerView = (RecyclerView)findViewById(R.id.rv_level_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sureTextView = (TextView) findViewById(R.id.tv_sure_select);
        sureTextView.setOnClickListener(this);
        selectLevelListApdater = new SelectLevelListApdater(DormitorySelectLevelActivity.this,mDormitoryOrClassInfoBeanList,mCheckList,listener);
        recyclerView.setAdapter(selectLevelListApdater);

        String title;
        if(mItemBelongDimension == Constant.DimensionType_Dormitory){
            searchEditText.setHint(R.string.string_search_dormitory);
            title = getString(R.string.string_select_dormitory);
        }else {
            searchEditText.setHint(R.string.string_search_class);
            title = getString(R.string.string_select_class);
        }

        mToolBarManager.setTitle(title).setRightText("重置").clickRightButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isCanClickSureBtn){
                    return;
                }
//                Toast.Short(DormitorySelectLevelActivity.this,"默认为选择‘全部’");
                mCheckList.clear();  //清空选择
                setAllHaveSelectCheckList(); //全选
                searchEditText.setText("");
                mRecordList.clear();
                mRecordList.addAll(mDormitoryOrClassInfoBeanList);
                //TODO 直接返回全部
                selectLevelListApdater.setNewCheckListAndRefresh(mCheckList);//重新设置适配器
//                sureTextView.setBackgroundColor(getResources().getColor(R.color.alpha_gray));
//                isCanClickSureBtn = false;

                /*Intent intent  = new Intent();
                Bundle bundle = new Bundle();
                bundle.putBoolean(DormitoryNewRankingActivity.IS_CLICK_CLEAR,true);
                bundle.putSerializable(DormitoryNewRankingActivity.CHECK_LIST_FLAG, (Serializable) mCheckList);
                bundle.putSerializable(DormitoryNewRankingActivity.RECORD_LIST, (Serializable) mRecordList);
                bundle.putSerializable(DormitoryNewRankingActivity.DORMITORY_OR_CLASS_LIST,(Serializable)mDormitoryOrClassInfoBeanList);
                intent.putExtras(bundle);
                setResult(DormitoryNewRankingActivity.RESULT_CODE,intent);
                finish();*/
            }
        });
                /*.showSave(true).setmSaveText(getString(R.string.string_select_all_select)).clickSave(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCheckList.clear();
                setAllHaveSelectCheckList();  //全选
                searchEditText.setText("");
                mRecordList.clear();
                mRecordList.addAll(mDormitoryOrClassInfoBeanList);
                sureTextView.setBackgroundColor(getResources().getColor(R.color.main_color));
                isCanClickSureBtn = true;
                Toast.Short(DormitorySelectLevelActivity.this,"recordList.size = " + mRecordList.size());
                //重新设置适配器
                selectLevelListApdater.setNewCheckListAndRefresh(mCheckList);
            }
        })

        if(isCanClickSureBtn){
            sureTextView.setBackgroundColor(getResources().getColor(R.color.main_color));
        }else {
            sureTextView.setBackgroundColor(getResources().getColor(R.color.alpha_gray));
        }*/
    }

    @Override
    protected void requestData() {
        super.requestData();
        if(isNeedToGetDataAgain){
            //TODO 如果外面没有拿到值，需要再次去请求服务器
            presenter.getDormitoryOrClassList(mItemBelongDimension);
        }else {
            //TODO 外面已经拿到了真实的值，可以直接去显示
            selectLevelListApdater.setDomitoryOrClassInfoBeanList(mDormitoryOrClassInfoBeanList);
            selectLevelListApdater.setNewCheckListAndRefresh(mCheckList);
        }
    }

    @Override
    public DormitorySelectLevelPresenter createPresenter() {
        return new DormitorySelectLevelPresenter();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_dormitory_select_level;
    }



    SelectLevelListApdater.Listener listener  = new SelectLevelListApdater.Listener() {
        @Override
        public void getRecordList(List<DormitoryOrClassSingleInfoBean> recordList, Set<Integer> checkList) {
            mRecordList.clear();
            mRecordList.addAll(recordList);
            mCheckList = checkList;
//            Log.d("lenita1","recordList.size = " + recordList.size()+",mCheckList.size() = "+mCheckList.size());
            /*if(mCheckList.size() > 0){
                sureTextView.setBackgroundColor(getResources().getColor(R.color.main_color));
                isCanClickSureBtn = true;
            }else {
                sureTextView.setBackgroundColor(getResources().getColor(R.color.alpha_gray));
                isCanClickSureBtn = false;
            }*/
        }
    };

    private void setAllHaveSelectCheckList(){
        for(int i = 0;i <mDormitoryOrClassInfoBeanList.size();i++ ){
            mCheckList.add(new Integer(i));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_sure_select:
                if(!isCanClickSureBtn){
                    return;  //不能点击确定
                }
                //TODO 返回勾选的数据测试
                Intent intent  = new Intent();
                Bundle bundle = new Bundle();
                if(mRecordList.size() == 0){  //和清空一样，都是全部
                    Toast.Short(DormitorySelectLevelActivity.this,"无选择数据，默认为选择‘全部’");
                    mCheckList.clear();  //清空选择
                    setAllHaveSelectCheckList(); //全选
                    searchEditText.setText("");
                    mRecordList.clear();
                    mRecordList.addAll(mDormitoryOrClassInfoBeanList);
                    bundle.putBoolean(DormitoryNewRankingActivity.IS_CLICK_CLEAR,true);
                }else {
                    bundle.putBoolean(DormitoryNewRankingActivity.IS_CLICK_CLEAR,false);
                }
                bundle.putSerializable(DormitoryNewRankingActivity.CHECK_LIST_FLAG, (Serializable) mCheckList);
                bundle.putSerializable(DormitoryNewRankingActivity.RECORD_LIST, (Serializable) mRecordList);
                bundle.putSerializable(DormitoryNewRankingActivity.DORMITORY_OR_CLASS_LIST,(Serializable)mDormitoryOrClassInfoBeanList);
                intent.putExtras(bundle);
                setResult(DormitoryNewRankingActivity.RESULT_CODE,intent);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void getDormitoryOrClassSuccess(List<DormitoryOrClassSingleInfoBean> domitoryOrClassInfoBeanList) {
        isCanClickSureBtn = true;
        mDormitoryOrClassInfoBeanList.clear();
        mDormitoryOrClassInfoBeanList.addAll(domitoryOrClassInfoBeanList);
        mRecordList.clear();
        mRecordList.addAll(domitoryOrClassInfoBeanList);
        getCheckList();  //获得勾选列表
        selectLevelListApdater.setDomitoryOrClassInfoBeanList(domitoryOrClassInfoBeanList);
        selectLevelListApdater.setNewCheckListAndRefresh(mCheckList);
        sureTextView.setBackgroundColor(getResources().getColor(R.color.main_color));
        mToolBarManager.getRightButton().setEnabled(isCanClickSureBtn);
    }

    @Override
    public void getDormitoryOrClassFailed() {
        isCanClickSureBtn = false;
        sureTextView.setBackgroundColor(getResources().getColor(R.color.alpha_gray));
        //TODO 失败表示获取都不成功，页面所有按钮都无法点击
        mToolBarManager.getRightButton().setEnabled(isCanClickSureBtn);
    }

    /**
     * 用于判断宿舍楼或专业部的勾选情况
     */
    private void getCheckList(){
        for(int i = 0; i<mDormitoryOrClassInfoBeanList.size();i++){
            mCheckList.add(new Integer(i));
        }
    }
}
