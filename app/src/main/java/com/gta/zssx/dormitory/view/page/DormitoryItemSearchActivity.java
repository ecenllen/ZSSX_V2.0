package com.gta.zssx.dormitory.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.gta.utils.resource.Toast;
import com.gta.zssx.R;
import com.gta.zssx.dormitory.model.bean.ItemLevelBean;
import com.gta.zssx.dormitory.model.bean.RxItemSelect;
import com.gta.zssx.dormitory.presenter.DormitoryItemSearchPresenter;
import com.gta.zssx.dormitory.view.DormitoryItemSearchView;
import com.gta.zssx.dormitory.view.adapter.DormitoryItemSearchAdapter;
import com.gta.zssx.pub.base.BaseMvpActivity;
import com.gta.zssx.pub.util.RxBus;
import com.gta.zssx.pub.widget.ClearEditText;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;


/**
 * [Description]
 * 指标项搜索Activity
 * [How to use]
 * 用于指标项搜索功能显示
 * [Tips]
 *
 * Created by lan.zheng on 2017/7/24 13:13.
 */
 public class DormitoryItemSearchActivity extends BaseMvpActivity<DormitoryItemSearchView,DormitoryItemSearchPresenter> implements DormitoryItemSearchView {

    public static final String PAGE_TAG = DormitoryItemSearchActivity.class.getSimpleName();
    private static final String SELECTED_ITEM_ID = "selected_item_id";

    private ClearEditText clearEditText;  //搜索框
    private RecyclerView recyclerView;  //列表
    private DormitoryItemSearchAdapter dormitoryItemSearchAdapter;//适配器
    private String mKeyWord; //关键字
    private TextView emptyTextView; //空页面
    private TextView finishTextView;  //返回按钮
    private boolean isFirstEnter = true; //是否是第一次进入
    private List<ItemLevelBean> mItemLevelBeanList;//指标项列表
    private int mSelectItemId; // 上次已选择指标项

    /**
     * 入口
     * @param context
     */
    public static void start(Context context,int selectItemId) {
        Intent lIntent = new Intent(context, DormitoryItemSearchActivity.class);
        Bundle lBundle = new Bundle();
        lBundle.putInt(SELECTED_ITEM_ID,selectItemId);
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        lIntent.putExtras(lBundle);
        context.startActivity(lIntent);
    }


    TextWatcher mSearchTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            //编辑完
            mKeyWord = s.toString ().trim ();
            if(TextUtils.isEmpty(mKeyWord)){
                emptyTextView.setVisibility(View.GONE);
                dormitoryItemSearchAdapter.setKeyWord(mKeyWord);
                dormitoryItemSearchAdapter.setDataAndRefresh(null);
            }else {
                emptyTextView.setVisibility(View.GONE);
                requestData();  //TODO 真实的去获取数据
            }
        }
    };

    @Override
    protected boolean isNeedToolbar() {
        return false;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_dormitory_item_search;
    }

    @Override
    protected void initData() {
        super.initData();
        mSelectItemId  = getIntent().getExtras().getInt(SELECTED_ITEM_ID,-1);
    }

    @Override
    protected void initView() {
        clearEditText = (ClearEditText)findViewById(R.id.search_edittext);
        recyclerView = (RecyclerView)findViewById(R.id.search_item_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        clearEditText.addTextChangedListener(mSearchTextWatcher);
        emptyTextView = (TextView)findViewById(R.id.no_data_tv);
        emptyTextView.setVisibility(View.GONE);
        finishTextView = (TextView)findViewById(R.id.finish_search_tv) ;

        mItemLevelBeanList = new ArrayList<>();
        dormitoryItemSearchAdapter = new DormitoryItemSearchAdapter(DormitoryItemSearchActivity.this,mItemLevelBeanList);
        dormitoryItemSearchAdapter.setKeyWord("");
        dormitoryItemSearchAdapter.setmSelectItemId(mSelectItemId);
        recyclerView.setAdapter(dormitoryItemSearchAdapter);
        addListener();
    }

    private void addListener() {
        finishTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /**
         * 添加指标项选择回调监听 - rx_dormitory_item_select
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
        if(isFirstEnter){
            isFirstEnter = false;
        }else {
            presenter.getSearchItemData(mKeyWord);
        }
    }

    @NonNull
    @Override
    public DormitoryItemSearchPresenter createPresenter() {
        return new DormitoryItemSearchPresenter();
    }

    @Override
    public void showResult(List<ItemLevelBean> itemLevelBeanList) {
        dormitoryItemSearchAdapter.setKeyWord(mKeyWord);
        dormitoryItemSearchAdapter.setDataAndRefresh(itemLevelBeanList);
        emptyTextView.setVisibility(View.GONE);
    }

    @Override
    public void showToast(String msg) {
//        Toast.Short(this,msg);
        dormitoryItemSearchAdapter.setDataAndRefresh(null);
        emptyTextView.setVisibility(View.VISIBLE);
    }
}
