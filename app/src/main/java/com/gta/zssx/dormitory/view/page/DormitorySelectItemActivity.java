package com.gta.zssx.dormitory.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.gta.utils.resource.Toast;
import com.gta.zssx.R;
import com.gta.zssx.dormitory.model.bean.ItemInfoBean;
import com.gta.zssx.dormitory.model.bean.ItemLevelBean;
import com.gta.zssx.dormitory.model.bean.LevelList;
import com.gta.zssx.dormitory.model.bean.RxItemSelect;
import com.gta.zssx.dormitory.presenter.DormitorySelectItemPresenter;
import com.gta.zssx.dormitory.view.DormitorySelectItemView;
import com.gta.zssx.dormitory.view.adapter.SelectItemAdapter;
import com.gta.zssx.pub.base.BaseMvpActivity;
import com.gta.zssx.pub.util.RxBus;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * [Description]
 * 指标项选择Activity
 * [How to use]
 * 用于指标项选择
 * [Tips]
 *
 * Created by lan.zheng on 2017/7/24 13:15.
 */

public class DormitorySelectItemActivity extends BaseMvpActivity<DormitorySelectItemView,DormitorySelectItemPresenter> implements DormitorySelectItemView,ExpandableListView.OnChildClickListener{
    public static final String PAGE_TAG = DormitorySelectItemActivity.class.getSimpleName();

    private final static String LEVEL_ID = "LEVEL_ID";
    private final static String PAGE_FLAG ="PAGE_FLAG";
    private final static String MAIN_PAGE_FLAG = "MAIN_PAGE_FLAG";
    private final static String ITEM_INFO_BEAN = "ITEM_INFO_BEAN";
    private final static String SELECTED_ITEM_ID = "SELECT_ITEM_ID";  //选择过的指标项
//    private final static String ITEM_NAME_FORMAT_LEVEL = "ITEM_NAME_FORMAT_LEVEL";

    private ExpandableListView expandableListView;
    private TextView emptyTextView;  //空白页
    private ItemInfoBean itemInfoBean;  //指标项信息
    private int mLevelId = -1;
    private int mPageFlag = 0;
    private boolean isMainPage;  //是否是主页（第一个页面）
    private boolean isNeedToRequestNextLevel = false;  //首次进入的设为false
    private SelectItemAdapter mAdapter;//适配器
    private List<ItemInfoBean> itemInfoBeanList;//指标项层级列表
    private int mSelectItemId;
    private String itemNameFullString;  //指标项全称

    /**
     *
     * @param context context
     * @param levelId 指标项层级Id
     * @param flagMainPage 主页标识
     * @param pageFlag 第几个页面 首页传0
     * @param itemInfoBean 指标项信息
     * @param selectItemId 已选择的指标项，用于打钩
     */
    public static void start(Context context, int levelId, boolean flagMainPage,int pageFlag,ItemInfoBean itemInfoBean,int selectItemId) {
        Intent lIntent = new Intent(context, DormitorySelectItemActivity.class);
        Bundle lBundle = new Bundle();
        lBundle.putInt(LEVEL_ID, levelId);
        lBundle.putBoolean(MAIN_PAGE_FLAG,flagMainPage);
        lBundle.putInt(PAGE_FLAG,pageFlag);
        lBundle.putSerializable(ITEM_INFO_BEAN,itemInfoBean);
        lBundle.putInt(SELECTED_ITEM_ID,selectItemId);
//        lBundle.putString(ITEM_NAME_FORMAT_LEVEL,itemNameFormatLevelString);
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        lIntent.putExtras(lBundle);
        context.startActivity(lIntent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_dormitory_select_item;
    }

    @Override
    protected void initData() {
        super.initData();
        mLevelId = getIntent().getExtras().getInt(LEVEL_ID,-1);
        mPageFlag = getIntent().getExtras().getInt(PAGE_FLAG,0);
        isMainPage = getIntent().getExtras().getBoolean(MAIN_PAGE_FLAG,true);
        mSelectItemId  = getIntent().getExtras().getInt(SELECTED_ITEM_ID,-1);
        if(isMainPage){
            itemInfoBean = new ItemInfoBean();
            isNeedToRequestNextLevel = false;
        }else{
            itemInfoBean = (ItemInfoBean) getIntent().getExtras().getSerializable(ITEM_INFO_BEAN);
            isNeedToRequestNextLevel = true;

        }
        mAdapter = new SelectItemAdapter(this);
        itemInfoBeanList = new ArrayList<>();
    }

    @Override
    protected void initView() {
        emptyTextView = (TextView)findViewById(R.id.tv_item_empty);
        expandableListView = (ExpandableListView)findViewById(R.id.expendlist);
        expandableListView.setDivider(null);
        expandableListView.setOnChildClickListener(this);

        mToolBarManager.setTitle(getString(R.string.please_choose)).showRightImage(true).setRightImage(getResources().getDrawable(R.drawable.express_new_select_user_search_bg))
                .clickRightImage(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO 指标项搜索
                        DormitoryItemSearchActivity.start(DormitorySelectItemActivity.this, mSelectItemId);
                    }
                });
        addListener();
    }


    private void addListener() {
        /**
         * 添加指标项选择回调监听 - 本页面收到回调finish（）
         */
        presenter.mCompositeSubscription.add(RxBus.getDefault().toObserverable(RxItemSelect.class)
                .subscribe(new Subscriber<RxItemSelect>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(RxItemSelect rxItemSelect) {
                        //TODO 这个界面监听到了要finish()
                        finish();
                    }
                }));

    }

    @Override
    protected void requestData() {
        super.requestData();
        if(isMainPage){  //TODO 主页进入才去获取服务器，第二次进入直接可以赋值
            presenter.getItemData(mLevelId,isNeedToRequestNextLevel);
        }else {
            showLevelList(itemInfoBean);
        }
    }

    @NonNull
    @Override
    public DormitorySelectItemPresenter createPresenter() {
        return new DormitorySelectItemPresenter();
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        ItemLevelBean itemLevelBean = itemInfoBean.getLevelList().get(childPosition);
        if (itemLevelBean.getType() == 0) {
            //点击的是指标项,回调显示,获得指标项全称,全称重新赋值到ItemName中
            itemNameFullString = itemLevelBean.getItemFullName();
            itemLevelBean.setItemName(itemNameFullString);
            RxItemSelect rxItemSelect = new RxItemSelect();
            rxItemSelect.setItemLevelBean(itemLevelBean);
            /**
             * rx_dormitory_item_select
             */
            RxBus.getDefault().post(rxItemSelect);
        } else {
            //点击的是指标项层级，再次请求数据，判断是否要跳转至另一个页面显示
            mLevelId = itemLevelBean.getItemId();
            isNeedToRequestNextLevel = true;
            presenter.getItemData(mLevelId,isNeedToRequestNextLevel);
        }
        return false;
    }

    @Override
    public void showEmptyView() {
        emptyTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showToast() {
        Toast.Short(this,"该层级下无数据");
    }

    @Override
    public void showResult(ItemInfoBean itemInfoBean,boolean isNeedToRequestNextLevel) {
        //获取到了结构都显示组织结构
        emptyTextView.setVisibility(View.GONE);
        expandableListView.setVisibility(View.VISIBLE);
        if(isNeedToRequestNextLevel){  //请求下一层级
            DormitorySelectItemActivity.start(DormitorySelectItemActivity.this,mLevelId,false,mPageFlag+1,itemInfoBean,mSelectItemId);
        }else {   //TODO isNeedToRequestNextLevel = false,证明首页进入，才进行赋值，其他页面都通过传递
             this.isNeedToRequestNextLevel = true;
             showLevelList(itemInfoBean);
        }
    }


    private void showLevelList(ItemInfoBean itemInfoBean){
        this.itemInfoBean = itemInfoBean;
        itemInfoBeanList.clear();
        itemInfoBeanList.add(itemInfoBean);
        mAdapter.setEntities(itemInfoBeanList,mSelectItemId);
        expandableListView.setAdapter(mAdapter);
        //默认都展开
        for (int i = 0; i < mAdapter.getGroupCount(); i++) {
                expandableListView.expandGroup(i);
        }
    }
}
