package com.gta.zssx.dormitory.view.page;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.dormitory.model.bean.DormitoryListArgumentBean;
import com.gta.zssx.dormitory.model.bean.DormitoryOrClassListBean;
import com.gta.zssx.dormitory.model.bean.DormitoryOrClassSingleInfoBean;
import com.gta.zssx.dormitory.model.bean.RxRefreshDormitoryListBean;
import com.gta.zssx.dormitory.presenter.DormitoryListInnerPresenter;
import com.gta.zssx.dormitory.view.DormitoryListInnerView;
import com.gta.zssx.dormitory.view.adapter.DormitoryListOrClassListAdapter;
import com.gta.zssx.dormitory.view.adapter.DormitoryListOrClassListChildAdapter;
import com.gta.zssx.pub.base.BaseMvpFragment;
import com.gta.zssx.pub.common.Constant;
import com.gta.zssx.pub.util.PreferencesUtils;
import com.gta.zssx.pub.util.RxBus;

import java.util.ArrayList;

import butterknife.Bind;
import ca.barrenechea.widget.recyclerview.decoration.StickyHeaderDecoration;
import rx.Subscriber;

/**
 * [Description]
 * <p> 宿舍列表/班级列表界面
 * [How to use]
 * <p>
 * [Tips]
 * @author Create by Weiye.Chen on 2017/7/21.
 * @since 2.0.0
 */
public class DormitoryListInnerFragment extends BaseMvpFragment<DormitoryListInnerView, DormitoryListInnerPresenter> implements DormitoryListInnerView {

    @Bind(R.id.recycler)
    RecyclerView recyclerView;
    @Bind(R.id.tv_empty)
    TextView tvEmpty;
    private DormitoryListOrClassListAdapter adapter;
    private StickyHeaderDecoration decor;


    /**
     * 宿舍楼ID数组/专业部ID数组,前一界面传过来
     */
    private ArrayList<DormitoryOrClassSingleInfoBean> list;
    /** 指标项ID*/
    private int mItemId;
    /** 1新增，2修改，3查看*/
    private int actionType;


    private DormitoryListArgumentBean mDormitoryListArgumentBean;
    /**
     * 当前宿舍楼/专业部的位置，用来从宿舍楼列表要当前宿舍楼ID
     */
    private int mDormitoryBuildingPosition;
    /** 用来保存最后浏览层楼下标的KEY, recordId(唯一) + key + 宿舍楼位置(1)*/
    private String levelIndexKey;
    private String key = "k";
    private int mLevelPositionId = -1;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_dormitory_list_inner;
    }

    @Override
    protected void initBundle(Bundle bundle) { // 从DormitoryListActivity 传入Bundle
        super.initBundle(bundle);

        mDormitoryListArgumentBean = bundle.getParcelable(DormitoryListActivity.KEY_DORMITORY_LIST_ARGUMENT);
        mDormitoryBuildingPosition = bundle.getInt(DormitoryListFragment.DormitoryBuildingPosition);

        if(mDormitoryListArgumentBean != null) {
            list = mDormitoryListArgumentBean.getDormitoryIdList();
            mItemId = mDormitoryListArgumentBean.getItemId();
            actionType = mDormitoryListArgumentBean.getActionType();
        }

        /*根据保存最后浏览层楼下标的KEY，获取上一次最后浏览层楼的位置*/
        if(((DormitoryListActivity)getActivity()).recordId != -1 && actionType != Constant.ACTION_TYPE_JUST_CHECK) {  // 非新增进来，有保存过;并且不是查看状态
            levelIndexKey = ((DormitoryListActivity)getActivity()).recordId + key + mDormitoryBuildingPosition;
            mLevelPositionId = PreferencesUtils.getInt(mActivity, levelIndexKey, -1);
        }
    }

    @Override
    protected void initView(View view) {
        tvEmpty.setVisibility(View.GONE);
        initAdapter();
    }

    private void initAdapter() {
        adapter = new DormitoryListOrClassListAdapter(mActivity, new ChildOnItemClickListener(), mDormitoryListArgumentBean.getDimensionType());
        decor = new StickyHeaderDecoration(adapter);
        recyclerView.setAdapter(adapter);
        if(actionType != Constant.ACTION_TYPE_JUST_CHECK) // 非查看状态才显示
            adapter.setBrowseIndex(mLevelPositionId);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.addItemDecoration(decor);
    }

    private class ChildOnItemClickListener implements DormitoryListOrClassListChildAdapter.OnItemClickListener {

        @Override
        public void onItemClick(int parentPosition, int childPosition, int id) {
            mDormitoryListArgumentBean.setChildPosition(childPosition);
            mDormitoryListArgumentBean.setParentPosition(parentPosition);
            mDormitoryListArgumentBean.setDormitoryOrClassId(id); // 当前宿舍/班级ID
            mDormitoryListArgumentBean.setRecordId(((DormitoryListActivity)mActivity).recordId);  //每次点击都拿最新的RecordID,因为新增的时候会拿到真正的recordId
            mDormitoryListArgumentBean.setDate(((DormitoryListActivity)mActivity).inputDate); //每次点击都拿最新的InputDate,因为日期可以改变
            DormitoryOrClassIndexPointActivity.start((Activity) mActivity, mDormitoryListArgumentBean);
            // 记录保存当前进入的评分录入 层楼/年级 下标
            if (mLevelPositionId != parentPosition) {
                mLevelPositionId = parentPosition;
            }
        }
    }

    @Override
    public void requestData() {
        hideEmpty();
        if (list != null && !list.isEmpty() && list.get(mDormitoryBuildingPosition) != null) {
            ArrayList<String> ListString = new ArrayList<>();
            ListString.add("" + list.get(mDormitoryBuildingPosition).getDormitoryOrClassId());
            presenter.getDormitoryOrClassList(((DormitoryListActivity)getActivity()).inputDate, mItemId, ListString);
        }
    }

    /**
     * 刷新分隔线（楼层字体加租）
     */
    public void refreshHeaderDecoration(int levelPosition) {
        mLevelPositionId = levelPosition >= 0 ? levelPosition : mLevelPositionId;
        if(decor != null && actionType != Constant.ACTION_TYPE_JUST_CHECK) {
            adapter.setBrowseIndex(mLevelPositionId);
            decor.clearHeaderCache(); // 刷新楼层字体，mLevelPositionId 位置加粗显示
        }
    }

    @NonNull
    @Override
    public DormitoryListInnerPresenter createPresenter() {
        return new DormitoryListInnerPresenter();
    }

    @Override
    public void showResult(DormitoryOrClassListBean dormitoryListBean) {
        if (dormitoryListBean != null) {
            if(mDormitoryListArgumentBean != null)
                mDormitoryListArgumentBean.setDormitoryOrClassListBean(dormitoryListBean);
//            adapter.setBrowseIndex(mLevelPositionId);
//            decor.clearHeaderCache(); // 刷新楼层字体，mLevelPositionId 位置加粗显示
            if(dormitoryListBean.getDormitoryOrClassInfo() != null && !dormitoryListBean.getDormitoryOrClassInfo().isEmpty()) {
                if(dormitoryListBean.getDormitoryOrClassInfo().get(0) != null) {
                    if(!dormitoryListBean.getDormitoryOrClassInfo().get(0).getDormitoryOrClassList().isEmpty()){
                        hideEmpty();
                        adapter.setIsAdditionOrSubtraction(dormitoryListBean.getItemInfo().getItemMode());
                        adapter.setList(dormitoryListBean.getDormitoryOrClassInfo().get(0).getDormitoryOrClassList());
                    } else {
                        showEmpty();
                    }
                }
            } else {
                showEmpty();
            }
        }
    }

    private void showEmpty() {
        hideRecyclerView();
        tvEmpty.setVisibility(View.VISIBLE);
    }

    private void hideEmpty() {
        recyclerView.setVisibility(View.VISIBLE);
        tvEmpty.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 新增进来，保存过评分, 并且点进评分录入页；并且非查看状态
        if(((DormitoryListActivity)getActivity()).recordId != -1 && mLevelPositionId != -1 && actionType != Constant.ACTION_TYPE_JUST_CHECK) {
            levelIndexKey = ((DormitoryListActivity)getActivity()).recordId + key + mDormitoryBuildingPosition; // recordId 会改变，所以要重新组合KEY
            PreferencesUtils.putInt(mActivity, levelIndexKey, mLevelPositionId);
        }
    }

    @Override
    public void showOnFailReloading(String error) {
        super.showOnFailReloading(error);
        hideRecyclerView();
    }

    private void hideRecyclerView() {
        recyclerView.setVisibility(View.GONE);
    }
}
