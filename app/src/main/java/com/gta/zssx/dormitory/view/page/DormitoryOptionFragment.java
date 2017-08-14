package com.gta.zssx.dormitory.view.page;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.gta.zssx.R;
import com.gta.zssx.dormitory.model.bean.OptionBean;
import com.gta.zssx.dormitory.model.bean.OptionHttpBean;
import com.gta.zssx.dormitory.model.bean.RxDormitoryRankingListUpdateBean;
import com.gta.zssx.dormitory.model.bean.RxSaveNeedToOpt;
import com.gta.zssx.dormitory.model.bean.RxRefreshDormitoryListBean;
import com.gta.zssx.dormitory.presenter.DormitoryOrClassOptionPresenter;
import com.gta.zssx.dormitory.view.OptionView;
import com.gta.zssx.dormitory.view.adapter.DormitoryOptionAdapter;
import com.gta.zssx.dormitory.view.adapter.DormitoryOptionClassAdapter;
import com.gta.zssx.pub.base.NextBaseMvpFragment;
import com.gta.zssx.pub.common.Constant;
import com.gta.zssx.pub.util.BeanCloneUtil;
import com.gta.zssx.pub.util.DormitoryScoreFilter;
import com.gta.zssx.pub.util.RxBus;
import com.gta.zssx.pub.util.StringUtils;

import java.util.HashMap;

import butterknife.Bind;


/**
 * [Description]
 * <p> 宿舍管理---有选项设置、无选项设置界面，包含宿舍维度和班级维度
 * <p> 主要操作中间数据交互与显示，选中Item 与宿舍得分联动
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Weiye.Chen on 2017/7/22.
 * @since 2.0.0
 */
public class DormitoryOptionFragment extends NextBaseMvpFragment<OptionView, DormitoryOrClassOptionPresenter> implements OptionView {

    @Bind(R.id.recycler_option)
    RecyclerView optionRecyclerView;
    @Bind(R.id.recycler_class)
    RecyclerView classRecyclerView;
    @Bind(R.id.ll_score_minus)
    LinearLayout llScoreAndMinus;
    @Bind(R.id.tv_minus_2)
    TextView tvMinus2;
    /**
     * 宿舍得分/班级得分  文字显示
     */
    @Bind(R.id.tv_dormitory_class_score)
    TextView tvDormitoryScoreName;
    /*减分的(-号)*/
    @Bind(R.id.tv_minus)
    TextView tvMinus;
    /**
     * 顶部宿舍得分/班级得分
     */
    @Bind(R.id.et_class_score)
    EditText etDormitoryScore;
    @Bind(R.id.tv_class_score)
    TextView tvDormitoryScore;
//    @Bind(R.id.tv_dormitory_class_score_show)
//    TextView tvDormitoryScore;
    /**
     * 顶部宿舍备注/班级备注
     */
    @Bind(R.id.et_class_remark)
    EditText etClassRemark;
    /**
     * 顶部备注容器，宿舍维度隐藏
     */
    @Bind(R.id.rl_remark)
    RelativeLayout rlRemark;
    /**
     * 指标项选项 文字显示
     */
    @Bind(R.id.tv_dormitory_item_title)
    TextView tvItemTag;
    @Bind(R.id.tv_empty)
    TextView tvEmpty;

//    /**
//     * 缓存当前宿舍得分
//     */
//    private String cacheCurDormitoryScore;
//    private String cacheCurDormitoryRemark;
    /**
     * 宿舍备注是否第一次初始化
     */
    private boolean isRemarkFirstInit = true;
    /**
     * 宿舍得分是否第一次初始化
     */
    private boolean isScoreFirstInit = true;
    /**
     * 数据是否初始化完成，用来判断是否修改过内容
     */
    private boolean refreshDataFinish;

    DormitoryScoreListener dormitoryScoreListener;
    DormitoryScoreListener dormitoryRemarkListener;

    /**
     * 缓存页面数据实体
     */
    private HashMap<String, OptionBean> cacheOptionBeanMap = new HashMap<>();
    /**
     * 用户作修改的实体数据，修改之后的
     */
    private OptionBean mOptionBean;
    /**
     * 本地缓存或网络请求的实体数据，修改之前的
     */
    private OptionBean tempOptionBean;

    /**
     * 选项适配器
     */
    private DormitoryOptionAdapter optionAdapter;
    /**
     * 班级适配器
     */
    private DormitoryOptionClassAdapter classAdapter;
    private DormitoryOrClassIndexPointActivity mCurActivity;

    private String buildingOrDeptIDs;
//    /** 点击选项操作，得分无限制*/
//    private boolean isClickOptionItemAction;

    /**
     * @param dimensionType           1 为宿舍维度，2 为班级维度
     * @param optionType              1 为有选项设置，2 为无选项设置
     * @param isAdditionOrSubtraction 1 为增分，2 为扣分
     * @return fragment
     */
    public static DormitoryOptionFragment newInstance(int dimensionType, int optionType, int isAdditionOrSubtraction, int actionType) {
        Bundle args = new Bundle();
        args.putInt(KEY_DORMITORY_OR_CLASS, dimensionType);
        args.putInt(KEY_ADDITION_OR_SUBTRACTION, isAdditionOrSubtraction);
        args.putInt(KEY_IS_HAS_OPTION, optionType);
        args.putInt(KEY_IS_CAN_MODIFY, actionType);
        DormitoryOptionFragment fragment = new DormitoryOptionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_dormitory_has_option;
    }

    @Override
    protected void initBundle(Bundle bundle) {
        isAdditionOrSubtraction = bundle.getInt(KEY_ADDITION_OR_SUBTRACTION); // 增加或扣分
        dimensionType = bundle.getInt(KEY_DORMITORY_OR_CLASS); // 宿舍维度或班级维度
        optionType = bundle.getInt(KEY_IS_HAS_OPTION); // 是否有选项设置
        actionType = bundle.getInt(KEY_IS_CAN_MODIFY); // 是否能修改
    }

    @Override
    protected void initView(View view) {
        mCurActivity = (DormitoryOrClassIndexPointActivity) mActivity;
        etDormitoryScore.setEnabled(actionType != Constant.ACTION_TYPE_JUST_CHECK);  // 是否查看状态，不能修改
        etClassRemark.setEnabled(actionType != Constant.ACTION_TYPE_JUST_CHECK);    // 是否查看状态，不能修改

        if (optionType == Constant.ScoringTemplateType_HasOption) {  // 有选项设置，宿舍得分设置EditText 不能手动输入
            etDormitoryScore.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
            etDormitoryScore.setTextColor(getResources().getColor(R.color.main_color));
            etDormitoryScore.setEnabled(false);
            llScoreAndMinus.setVisibility(View.VISIBLE);
            etDormitoryScore.setVisibility(View.GONE);
            initOptionAdapter();  // 有选项设置，初始化选项适配器
        } else {
            if (isAdditionOrSubtraction == Constant.Addition) // 无选项，是否显示-号，有选项需要在autoSetDormitoryScore另外处理
                tvMinus.setVisibility(View.GONE);
            else
                tvMinus.setVisibility(View.VISIBLE);
            tvItemTag.setVisibility(View.GONE);
        }
        if (dimensionType == Constant.DimensionType_Dormitory) { // 宿舍维度，把备注隐藏，并初始化班级适配器（班级维度没有）
            tvDormitoryScoreName.setText(R.string.string_dormitory_dormitory_score);
            rlRemark.setVisibility(View.GONE);
            initClassAdapter();
        } else { // 班级维度
            tvDormitoryScoreName.setText(R.string.string_dormitory_class_score);
        }

        addListener();

    }

    private void addListener() {
        dormitoryScoreListener = new DormitoryScoreListener(true);
        dormitoryRemarkListener = new DormitoryScoreListener(false);
        etDormitoryScore.addTextChangedListener(dormitoryScoreListener);
        tvDormitoryScore.addTextChangedListener(dormitoryScoreListener);
        etClassRemark.addTextChangedListener(dormitoryRemarkListener);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buildingOrDeptIDs = presenter.formatBuildingOrDeptIDs(mCurActivity.isAll, mCurActivity.dormitoryOrClassSingleInfoBeanList);
    }

    /**
     * 宿舍得分/宿舍备注，输入框文本监听
     */
    private class DormitoryScoreListener implements TextWatcher {
        private boolean isScoreOrRemark;

        private DormitoryScoreListener(boolean isScoreOrRemark) {
            this.isScoreOrRemark = isScoreOrRemark;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if(!refreshDataFinish) return;
            isChanged = true;
            mCurActivity.setSaveButtonClickable(true);

            if (isScoreOrRemark) {
                if (optionType == Constant.ScoringTemplateType_NoOption) { // 无选项手动输分操作，做最大分数限制
                    /*宿舍评分输入框，过虑输入不合法，不标准的分数，保留3位小数*/
                    if (DormitoryScoreFilter.filterScore(s, etDormitoryScore)) return;
                }

                if (isChanged) {
                    if (classAdapter != null){
                        classAdapter.setDefaultClassScore(s.toString().trim()); // 班级得分自动跟随宿舍得分
                    }
                }
            }
//            isClickOptionItemAction = false;
        }
    }

    /**
     * 初始化有选项适配器
     */
    private void initOptionAdapter() {
        if (optionAdapter == null) {
            optionAdapter = new DormitoryOptionAdapter(mCurActivity, (position, dormitoryScore, optionItemBean, isChecked) -> {
//                isClickOptionItemAction = true;
                autoSetDormitoryScore(dormitoryScore);
            });
            optionAdapter.setCanEdit(actionType != Constant.ACTION_TYPE_JUST_CHECK);
            optionAdapter.setIsAddOrSub(isAdditionOrSubtraction);
            optionRecyclerView.setAdapter(optionAdapter);
            optionRecyclerView.setLayoutManager(new LinearLayoutManager(mCurActivity));
            optionRecyclerView.addItemDecoration(new DividerItemDecoration(mCurActivity, DividerItemDecoration.VERTICAL));
            optionRecyclerView.setItemAnimator(new DefaultItemAnimator());
        }
    }

    /**
     * 初始化班级适配器
     */
    private void initClassAdapter() {
        if (classAdapter == null) {
            classAdapter = new DormitoryOptionClassAdapter(mCurActivity, (position, afterText, isScore) -> {
                if (!isChanged) {
                    OptionBean optionBean = cacheOptionBeanMap.get(mCurActivity.currentPosition);
                    if (null != optionBean) {
                        if (isScore) { // 班级得分
                            if (!afterText.equalsIgnoreCase(StringUtils.formatScore(optionBean.getClassList().get(position).getClassSingleScore()))) {
                                isChanged = true;
                                mCurActivity.setSaveButtonClickable(true);
                            }
                        } else {  // 班级备注
                            if (!afterText.equalsIgnoreCase(optionBean.getClassList().get(position).getClassSingleRemark() + "")) {
                                isChanged = true;
                                mCurActivity.setSaveButtonClickable(true);
                            }
                        }
                    }
                }
            });
            classAdapter.setCanEdit(actionType != Constant.ACTION_TYPE_JUST_CHECK);
            classAdapter.setIsAdditionOrSub(isAdditionOrSubtraction);
            classRecyclerView.setAdapter(classAdapter);
            classRecyclerView.setLayoutManager(new LinearLayoutManager(mCurActivity));
//            classRecyclerView.addItemDecoration(new DividerItemDecoration(mCurActivity, DividerItemDecoration.VERTICAL));
            classRecyclerView.setItemAnimator(new DefaultItemAnimator());

        }
    }

    @Override
    public void requestData() {
        hideEmpty(); // 隐藏“暂无数据”字样
        refreshDataFinish = false;
        if (cacheOptionBeanMap.get(mCurActivity.currentPosition) == null) { // 没有缓存数据从网络请求
            presenter.getDormitoryOrClassOptionDetail(mCurActivity.dormitoryListArgumentBean.getItemId(),
                    mCurActivity.dormitoryListArgumentBean.getDate(), mCurActivity.dormitoryOrClassId + "", mCurActivity.dormitoryListArgumentBean.getActionType(), dimensionType, optionType);
        } else {
            mOptionBean = BeanCloneUtil.cloneTo(cacheOptionBeanMap.get(mCurActivity.currentPosition)); // 浅拷贝，否则是引用传递
            refreshData(mOptionBean); // 有缓存，刷新数据
        }
    }

    /**
     * 刷新选项里面数据(勾选状态)，并且刷新宿舍得分
     *
     * @param optionBean 当前录分页数据实体
     */
    private void refreshData(OptionBean optionBean) {
        isRemarkFirstInit = true;
        isScoreFirstInit = true;
        if (optionBean != null) {
//            cacheCurDormitoryScore = optionBean.getDormitoryOrClassScore().equals("0") ? "" : optionBean.getDormitoryOrClassScore();
//            cacheCurDormitoryRemark = optionBean.getRemark() == null ? "" : optionBean.getRemark();
            autoSetDormitoryScore(optionBean.getDormitoryOrClassScore());
            etClassRemark.setText(optionBean.getRemark());

            if (optionType == Constant.ScoringTemplateType_HasOption && dimensionType == Constant.DimensionType_Dormitory) {
                if (optionBean.getItemList() != null && optionBean.getItemList().isEmpty() && optionBean.getClassList() != null && optionBean.getClassList().isEmpty()) {
                    showEmpty();
                }
            } else if (optionType == Constant.ScoringTemplateType_HasOption) {
                if (optionBean.getItemList() != null && optionBean.getItemList().isEmpty()) {
                    showEmpty();
                }
            } else if (dimensionType == Constant.DimensionType_Dormitory) {
                if (optionBean.getClassList() != null && optionBean.getClassList().isEmpty()) {
                    showEmpty();
                }
            }

            if (optionAdapter != null) {
                optionRecyclerView.setVisibility(View.VISIBLE);
                optionAdapter.setList(optionBean.getItemList());
            }
            if (classAdapter != null) {
                classRecyclerView.setVisibility(View.VISIBLE);
                classAdapter.setList(optionBean.getClassList());
            }
        } else {
            showEmpty();
        }
        refreshDataFinish = true;
    }

    /**
     * 自动计算并设置上面宿舍得分
     *
     * @param autoScore autoScore
     */
    private void autoSetDormitoryScore(String autoScore) {
        String score = StringUtils.formatScore(autoScore);
        if(optionType == Constant.ScoringTemplateType_HasOption) {  // 有选项，处理-号
            if (!score.isEmpty() && Float.valueOf(score) > 0) {
//        if (!autoScore.isEmpty()) {
                if (isAdditionOrSubtraction == Constant.Subtraction)    // 有选项->扣分，并且分数不为0情况下
                    tvMinus2.setVisibility(View.VISIBLE);
            } else
                tvMinus2.setVisibility(View.GONE);
            tvDormitoryScore.setText(score);
        } else
            etDormitoryScore.setText(score);
    }

    @Override
    public void saveCurrentPageData() {
        if (mOptionBean != null) {
            mOptionBean.setDormitoryOrClassId(mCurActivity.dormitoryOrClassId);
            if(optionType == Constant.ScoringTemplateType_HasOption) {  // 有选项，则取textView
                mOptionBean.setDormitoryOrClassScore(tvDormitoryScore.getText().toString().trim());
            } else
                mOptionBean.setDormitoryOrClassScore(etDormitoryScore.getText().toString().trim()); // 无选项，则取EditText
            mOptionBean.setRemark(etClassRemark.getText().toString().trim());
        }
        OptionHttpBean optionHttpBean = new OptionHttpBean();
        optionHttpBean.setItemId(mCurActivity.ItemId);
        optionHttpBean.setInputDate(mCurActivity.InputDate);
        optionHttpBean.setBuildingOrDeptIds(buildingOrDeptIDs);
        optionHttpBean.setUserId(mCurActivity.UserId);
        optionHttpBean.setDetailContent(mOptionBean);
        optionHttpBean.setRecordId(mCurActivity.RecordId);
        presenter.SaveDormitoryOrClassOptionData(mCurActivity.dormitoryListArgumentBean.getDimensionType(), optionType, optionHttpBean);
//        presenter.saveScoreData();

        //update dormitory list
//        RxBus.getDefault().post(new RxRefreshDormitoryListBean());
    }


    /**
     * 上下宿舍切换点击,是否有改动数据，有则弹框提示
     */
    @Override
    public boolean isDataChanged() {
        return isChanged;
    }

    @NonNull
    @Override
    public DormitoryOrClassOptionPresenter createPresenter() {
        return new DormitoryOrClassOptionPresenter();
    }

    private void showEmpty() {
        hideRecyclerView();
        tvEmpty.setVisibility(View.VISIBLE);
    }

    private void hideEmpty() {
        tvEmpty.setVisibility(View.GONE);
    }

    @Override
    public void showResult(OptionBean optionBean) {
        if (optionBean != null) {
            mOptionBean = optionBean;
            tempOptionBean = BeanCloneUtil.cloneTo(optionBean); // 浅拷贝，否则是引用传递
            refreshData(optionBean);
            cacheOptionBeanMap.put(mCurActivity.currentPosition, tempOptionBean);
        }
    }

    @Override
    public void saveSuccess(boolean success, int recordId) {
        if (success) {
            ToastUtils.showShortToast(getString(R.string.gallery_save_file_success));
             /*更新宿舍列表 update dormitory list*/
            RxBus.getDefault().post(new RxRefreshDormitoryListBean());
            //rx_dormitory_ranking_list_refresh ,保存成功之后，关闭新增页面
            RxBus.getDefault().post(new RxSaveNeedToOpt());

            if (mCurActivity.RecordId == -1) {
                //rx_dormitory_ranking_list_refresh
                RxDormitoryRankingListUpdateBean dormitoryRankingListUpdateBean = new RxDormitoryRankingListUpdateBean();
                dormitoryRankingListUpdateBean.setNeedToUpdatePage(RxDormitoryRankingListUpdateBean.REFRESH_NOT_SUBMIT_LIST);
                RxBus.getDefault().post(dormitoryRankingListUpdateBean);
            }
            putCacheDataAndReset();
            mCurActivity.dormitoryListArgumentBean.setRecordId(recordId);
            mCurActivity.RecordId = recordId;
            mCurActivity.saveSuccessAndResetButton();
        } else {
            isChanged = false;
            mCurActivity.setSaveButtonClickable(false);
            ToastUtils.showShortToast(getString(R.string.string_save_fail));
        }
    }

    /**
     * 保存缓存数据，并且重置状态
     */
    private void putCacheDataAndReset() {
        tempOptionBean = BeanCloneUtil.cloneTo(mOptionBean); // 保存，把修改后的数据实体重新赋值到缓存集合
//        tempOptionBean.setItemList(optionAdapter.getList());
//        tempOptionBean.setClassList(classAdapter.getList());
//        if (!etDormitoryScore.getText().toString().trim().isEmpty())
//            tempOptionBean.setDomitoryScore(Integer.valueOf(etDormitoryScore.getText().toString().trim()));
//        cacheCurDormitoryScore = etDormitoryScore.toString();
//        cacheCurDormitoryRemark = etClassRemark.toString();
        cacheOptionBeanMap.put(mCurActivity.currentPosition, tempOptionBean);
        isChanged = false;
        mCurActivity.setSaveButtonClickable(false);
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
        etDormitoryScore.removeTextChangedListener(dormitoryScoreListener);
        tvDormitoryScore.removeTextChangedListener(dormitoryScoreListener);
        etClassRemark.removeTextChangedListener(dormitoryRemarkListener);
        tvDormitoryScore.clearFocus();
        etClassRemark.clearFocus();
        etDormitoryScore.clearFocus();
        tvDormitoryScore = null;
        etDormitoryScore = null;
        etClassRemark = null;
        dormitoryScoreListener = null;
        dormitoryRemarkListener = null;
    }
}
