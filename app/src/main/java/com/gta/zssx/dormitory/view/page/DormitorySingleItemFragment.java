package com.gta.zssx.dormitory.view.page;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.gta.zssx.R;
import com.gta.zssx.dormitory.model.bean.DormitoryEnterNameSingleHttpInfoBean;
import com.gta.zssx.dormitory.model.bean.DormitoryEnterNameSingleItemInfoBean;
import com.gta.zssx.dormitory.model.bean.RxDormitoryRankingListUpdateBean;
import com.gta.zssx.dormitory.model.bean.RxSaveNeedToOpt;
import com.gta.zssx.dormitory.model.bean.RxRefreshDormitoryListBean;
import com.gta.zssx.dormitory.presenter.DormitoryEnterNameSinglePresenter;
import com.gta.zssx.dormitory.view.DormitoryEnterNameSingleView;
import com.gta.zssx.dormitory.view.adapter.SingleItemClassListAdapterV10;
import com.gta.zssx.dormitory.view.adapter.SingleItemStudentListAdapter;
import com.gta.zssx.pub.common.Constant;
import com.gta.zssx.pub.util.StringUtils;
import com.gta.zssx.pub.widget.FixedScrollRecyclerView;
import com.gta.zssx.pub.base.NextBaseMvpFragment;
import com.gta.zssx.pub.util.BeanCloneUtil;
import com.gta.zssx.pub.util.RxBus;

import java.util.HashMap;

import butterknife.Bind;

/**
 * [Description]
 * <p>宿舍管理---单项设置界面，包含宿舍维度和班级维度
 * <p> 主要操作中间数据交互与显示，选中Item 与宿舍得分联动
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Weiye.Chen on 2017/7/22.
 * @since 2.0.0
 */
public class DormitorySingleItemFragment extends NextBaseMvpFragment<DormitoryEnterNameSingleView, DormitoryEnterNameSinglePresenter> implements DormitoryEnterNameSingleView {

    @Bind(R.id.recycler_option)
    FixedScrollRecyclerView optionRecyclerView;
    @Bind(R.id.recycler_class)
    FixedScrollRecyclerView classRecyclerView;
    /**
     * 减分的(-号)
     */
    @Bind(R.id.tv_minus)
    TextView tvMinus;
    @Bind(R.id.tv_dormitory_class_score_show)
    TextView tvDormitoryScore;
    @Bind(R.id.tv_dormitory_class_score)
    TextView tvDormitoryScoreName;
    /**
     * 班级维度-备注，宿舍维度不显示
     */
    @Bind(R.id.rl_remark)
    RelativeLayout rlClassRemark;
    @Bind(R.id.et_class_remark)
    EditText etClassRemark;
    @Bind(R.id.tv_empty)
    TextView tvEmpty;
//    /**
//     * 缓存当前宿舍得分
//     */
//    private String cacheCurDormitoryScore;
    /**宿舍备注是否第一次初始化*/
    private boolean isRemarkFirstInit = true;
    /**宿舍得分是否第一次初始化*/
    private boolean isScoreFirstInit = true;

    DormitoryScoreListener dormitoryScoreListener;

    private String buildingOrDeptIDs;


    /**
     * 缓存页面数据实体
     */
    private HashMap<String, DormitoryEnterNameSingleItemInfoBean> cacheOptionBeanMap = new HashMap<>();

    /**
     * 用户作修改的实体数据，修改之后的
     */
    private DormitoryEnterNameSingleItemInfoBean mOptionBean;
    /**
     * 本地缓存或网络请求的实体数据，修改之前的
     */
    private DormitoryEnterNameSingleItemInfoBean tempOptionBean;

    /**
     * 选项适配器
     */
    private SingleItemStudentListAdapter enterNameSingleAdapter;
    /**
     * 班级适配器
     */
    private SingleItemClassListAdapterV10 classAdapter;
    private DormitoryOrClassIndexPointActivity mActivity;

    /**
     * @param dimensionType           true 为宿舍维度 false 为班级维度
     * @param isAdditionOrSubtraction 1 为增分 2 为扣分
     * @param actionType              新增、修改、查看 状态
     * @return fragment;
     */
    public static DormitorySingleItemFragment newInstance(int dimensionType, int isAdditionOrSubtraction, int actionType) {
        Bundle args = new Bundle();
        args.putInt(KEY_DORMITORY_OR_CLASS, dimensionType);
        args.putInt(KEY_ADDITION_OR_SUBTRACTION, isAdditionOrSubtraction);
        args.putInt(KEY_IS_CAN_MODIFY, actionType);
        DormitorySingleItemFragment fragment = new DormitorySingleItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_dormitory_enter_name_single;
    }

    @Override
    protected void initBundle(Bundle bundle) {
        isAdditionOrSubtraction = bundle.getInt(KEY_ADDITION_OR_SUBTRACTION); // 增加或扣分
        dimensionType = bundle.getInt(KEY_DORMITORY_OR_CLASS); // 宿舍维度或班级维度
        actionType = bundle.getInt(KEY_IS_CAN_MODIFY); // 是否能修改
    }

    @Override
    protected void initView(View view) {
        mActivity = (DormitoryOrClassIndexPointActivity) getActivity();
        etClassRemark.setEnabled(actionType != Constant.ACTION_TYPE_JUST_CHECK); // 是否查看状态，不能修改
        initSingleAdapter();
        if (dimensionType == Constant.DimensionType_Dormitory) { // 宿舍维度，初始化班级适配器（班级维度没有）,宿舍维度则隐藏备注, 班级维度则显示
            tvDormitoryScoreName.setText(R.string.string_dormitory_dormitory_score);
            rlClassRemark.setVisibility(View.GONE);
            initClassAdapter();
        } else {
            tvDormitoryScoreName.setText(R.string.string_dormitory_class_score);
        }
        addListener();
    }

    private void addListener() {
        dormitoryScoreListener = new DormitoryScoreListener();
        tvDormitoryScore.addTextChangedListener(dormitoryScoreListener);
        etClassRemark.addTextChangedListener(dormitoryScoreListener);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buildingOrDeptIDs = presenter.formatBuildingOrDeptIDs(mActivity.isAll, mActivity.dormitoryOrClassSingleInfoBeanList);
    }

    /**
     * 宿舍得分输入框文本监听
     */
    private class DormitoryScoreListener implements TextWatcher {
//        private boolean isScoreOrRemark;
//
//        private DormitoryScoreListener(boolean isScoreOrRemark) {
//            this.isScoreOrRemark = isScoreOrRemark;
//        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(isScoreFirstInit) {
                isScoreFirstInit = false;
                return;
            }
            if(isRemarkFirstInit) {
                isRemarkFirstInit = false;
                return;
            }

            isChanged = true;
            mActivity.setSaveButtonClickable(true);
//            isClickOptionItemAction = false;
        }
    }

    private void initSingleAdapter() {
        if (enterNameSingleAdapter == null) {
            enterNameSingleAdapter = new SingleItemStudentListAdapter(mActivity, (position, dormitoryScore, selectedStuScoreSumMap, isChecked) -> {
                if (classAdapter != null)
                    classAdapter.setClassScoreMap(selectedStuScoreSumMap);
                autoSetDormitoryScore(dormitoryScore);
            });
            enterNameSingleAdapter.setCanEditScore(actionType != Constant.ACTION_TYPE_JUST_CHECK);  // 是否查看状态，不能修改
            enterNameSingleAdapter.setDormitoryOrClass(dimensionType); // 设置为宿舍维度,false 为班级维度
            optionRecyclerView.setAdapter(enterNameSingleAdapter);
            optionRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
            optionRecyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL));
            optionRecyclerView.setItemAnimator(new DefaultItemAnimator());
        }
    }

    private void initClassAdapter() {
        if (classAdapter == null) {
            classAdapter = new SingleItemClassListAdapterV10(mActivity, (position, afterText, isScoreOrRemark) -> {
                if (!isChanged) {
//                    DormitoryEnterNameSingleItemInfoBean optionBean = cacheOptionBeanMap.get(mActivity.currentPosition);
                    if (null != mOptionBean) {
                        if (isScoreOrRemark) {
                            if (!afterText.equalsIgnoreCase(mOptionBean.getClassList().get(position).getClassSingleScore() + "")) {
                                isChanged = true;
                                mActivity.setSaveButtonClickable(true);
                            }
                        } else {
                            if (!afterText.equalsIgnoreCase(mOptionBean.getClassList().get(position).getClassSingleRemark() + "")) {
                                isChanged = true;
                                mActivity.setSaveButtonClickable(true);
                            }
                        }
                    }
                }
            });
            classAdapter.setCanEdit(actionType != Constant.ACTION_TYPE_JUST_CHECK);
            classAdapter.setIsAdditionOrSub(isAdditionOrSubtraction);
            classRecyclerView.setAdapter(classAdapter);
            classRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
//            classRecyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL));
            classRecyclerView.setItemAnimator(new DefaultItemAnimator());
        }
    }

    @Override
    public void requestData() {
        hideEmpty();
        if (cacheOptionBeanMap.get(mActivity.currentPosition) == null) // 没有缓存数据从网络请求
            presenter.getDormitoryOptionDetail(mActivity.InputDate, mActivity.ItemId,
                    mActivity.dormitoryOrClassId + "", mActivity.actionType, mActivity.dormitoryListArgumentBean.getDimensionType());
//            presenter.getDormitoryOptionDetail(); // 假数据，上面方法网络请求真数据
        else {
            mOptionBean = BeanCloneUtil.cloneTo(cacheOptionBeanMap.get(mActivity.currentPosition)); // 浅拷贝，否则是引用传递
            refreshData(mOptionBean); // 有缓存，刷新数据
        }
    }

    /**
     * 刷新选项里面数据(勾选状态)，并且刷新宿舍得分
     *
     * @param optionBean 当前录分页数据实体
     */
    private void refreshData(DormitoryEnterNameSingleItemInfoBean optionBean) {
        isRemarkFirstInit = true;
        isScoreFirstInit = true;
        if (optionBean != null) {
//            cacheCurDormitoryScore = optionBean.getDormitoryScore();
            autoSetDormitoryScore(optionBean.getDormitoryScore());
            etClassRemark.setText(optionBean.getRemark());

            if (dimensionType == Constant.DimensionType_Dormitory) {
                if (optionBean.getStudentList() != null && optionBean.getStudentList().isEmpty()
                        && optionBean.getClassList() != null && optionBean.getClassList().isEmpty()) {
                    showEmpty();
                }
            } else {
                if (optionBean.getStudentList() != null && optionBean.getStudentList().isEmpty()) {
                    showEmpty();
                }
            }


            if (enterNameSingleAdapter != null) {
                optionRecyclerView.setVisibility(View.VISIBLE);
                enterNameSingleAdapter.setmEnterNameSingleItemBeanList(optionBean.getStudentList());
            }
            if (classAdapter != null){
                classRecyclerView.setVisibility(View.VISIBLE);
                classAdapter.setList(optionBean.getClassList());
            }
        }
    }

    /**
     * 自动计算并设置上面宿舍得分
     *
     * @param autoScore autoScore
     */
    private void autoSetDormitoryScore(String autoScore) {
        if (!autoScore.isEmpty() && Float.valueOf(autoScore) > 0) {
//        if (!autoScore.isEmpty()) {
            if (isAdditionOrSubtraction == Constant.Addition)
                tvMinus.setVisibility(View.GONE); // - 号，增分显示，扣分隐藏
            else
                tvMinus.setVisibility(View.VISIBLE);
//            tvDormitoryScore.setText(StringUtils.formatScore(autoScore));
        } else {
//            tvDormitoryScore.setText("");
            tvMinus.setVisibility(View.INVISIBLE);
        }
        tvDormitoryScore.setText(StringUtils.formatScore(autoScore));
    }

    /**
     * 中间保存按钮调用该方法，进行保存操作
     */
    @Override
    public void saveCurrentPageData() {
        if (mOptionBean != null) {
            mOptionBean.setDormitoryOrClassId(mActivity.dormitoryOrClassId);
            mOptionBean.setDormitoryOrClassScore(tvDormitoryScore.getText().toString().trim());
            mOptionBean.setRemark(etClassRemark.getText().toString().trim());
        }

        DormitoryEnterNameSingleHttpInfoBean dormitoryEnterNameSingleHttpInfoBean = new DormitoryEnterNameSingleHttpInfoBean();
        dormitoryEnterNameSingleHttpInfoBean.setItemId(mActivity.ItemId);
        dormitoryEnterNameSingleHttpInfoBean.setInputDate(mActivity.InputDate);
        dormitoryEnterNameSingleHttpInfoBean.setBuildingOrDeptIds(buildingOrDeptIDs);
        dormitoryEnterNameSingleHttpInfoBean.setUserId(mActivity.UserId);
        dormitoryEnterNameSingleHttpInfoBean.setDetailContent(mOptionBean);
        dormitoryEnterNameSingleHttpInfoBean.setRecordId(mActivity.RecordId);

        presenter.SaveDormitoryOrClassSingleData(mActivity.dormitoryListArgumentBean.getDimensionType(), dormitoryEnterNameSingleHttpInfoBean);

    }


    /**
     * 上下宿舍切换点击,是否有改动数据，有则弹框提示
     * return true 为有改动，false 为无改动
     */
    @Override
    public boolean isDataChanged() {
        return isChanged;
    }


    @NonNull
    @Override
    public DormitoryEnterNameSinglePresenter createPresenter() {
        return new DormitoryEnterNameSinglePresenter();
    }

    private void showEmpty() {
        tvEmpty.setVisibility(View.VISIBLE);
    }

    private void hideEmpty() {
        tvEmpty.setVisibility(View.GONE);
    }

    @Override
    public void showResult(DormitoryEnterNameSingleItemInfoBean dormitoryEnterNameSingleItemInfoBean) {
        if(dormitoryEnterNameSingleItemInfoBean != null) {
            mOptionBean = dormitoryEnterNameSingleItemInfoBean;
            tempOptionBean = BeanCloneUtil.cloneTo(dormitoryEnterNameSingleItemInfoBean); // 浅拷贝，否则是引用传递
            refreshData(mOptionBean);
            cacheOptionBeanMap.put(mActivity.currentPosition, tempOptionBean);
        }
    }

    @Override
    public void saveSuccess(boolean success, int recordId) {
        if (success) {
            ToastUtils.showShortToast(getString(R.string.gallery_save_file_success));
            /*更新宿舍列表 update dormitory list*/
            RxBus.getDefault().post(new RxRefreshDormitoryListBean());
            //rx_dormitory_ranking_list_refresh  ,保存成功之后，关闭新增页面
            RxBus.getDefault().post(new RxSaveNeedToOpt());
            if(mActivity.RecordId == -1) {
                //TODO 新增录入的，保存后需要刷新未审核页面
                //rx_dormitory_ranking_list_refresh
                RxDormitoryRankingListUpdateBean dormitoryRankingListUpdateBean = new RxDormitoryRankingListUpdateBean();
                dormitoryRankingListUpdateBean.setNeedToUpdatePage(RxDormitoryRankingListUpdateBean.REFRESH_NOT_SUBMIT_LIST);
                RxBus.getDefault().post(dormitoryRankingListUpdateBean);
            }
            putCacheDataAndReset();
            mActivity.RecordId = recordId;
            mActivity.saveSuccessAndResetButton();
        } else {
            isChanged = false;
            mActivity.setSaveButtonClickable(false);
            ToastUtils.showShortToast(getString(R.string.string_save_fail));
        }
    }

    /**
     * 保存缓存数据，并且重置状态
     */
    private void putCacheDataAndReset() {
        tempOptionBean = BeanCloneUtil.cloneTo(mOptionBean); // 保存，把修改后的数据实体重新赋值到缓存集合
        cacheOptionBeanMap.put(mActivity.currentPosition, tempOptionBean);
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
        optionRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cacheOptionBeanMap.clear();
        cacheOptionBeanMap = null;
        tvDormitoryScore.removeTextChangedListener(dormitoryScoreListener);
        etClassRemark.removeTextChangedListener(dormitoryScoreListener);
        tvDormitoryScore.clearFocus();
        etClassRemark.clearFocus();
        tvDormitoryScore = null;
        etClassRemark = null;
        dormitoryScoreListener = null;
    }
}
