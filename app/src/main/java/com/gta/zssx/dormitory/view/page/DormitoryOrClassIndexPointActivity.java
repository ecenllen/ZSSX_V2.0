package com.gta.zssx.dormitory.view.page;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;

import com.gta.zssx.AppConfiguration;
import com.gta.zssx.R;
import com.gta.zssx.dormitory.model.bean.DormitoryListArgumentBean;
import com.gta.zssx.dormitory.model.bean.DormitoryOrClassLevelList;
import com.gta.zssx.dormitory.model.bean.DormitoryOrClassListBean;
import com.gta.zssx.dormitory.model.bean.DormitoryOrClassSingleInfoBean;
import com.gta.zssx.dormitory.model.bean.ItemInfo;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.pub.base.NextBaseCommonActivity;
import com.gta.zssx.pub.base.NextBaseMvpFragment;
import com.gta.zssx.pub.common.Constant;
import com.gta.zssx.pub.util.DormitoryItemNameDialog;
import com.gta.zssx.pub.widget.ConfirmOrCancelDialog;

import java.util.ArrayList;
import java.util.List;



/**
 * [Description]
 * <p> 处理坐标，中间界面在fragment 实现 : 上一宿舍/下一宿舍 切换改变坐标位置，并记录当前宿舍ID，提供给fragment 使用
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Weiye.Chen on 2017/7/20.
 * @since 2.0.0
 */

public class DormitoryOrClassIndexPointActivity extends NextBaseCommonActivity {
    public static final String KEY_DORMITORY_OR_CLASS_ARGUMENT_BEAN = "dormitoryOrClassArgumentBean";
    public static final String KEY_RECORD_ID = "record_id";
    public static final String KEY_LEVEL_POSITION = "level_position";
    /**
     * 某一宿舍/班级ID
     */
    public int dormitoryOrClassId;
    /**
     * 宿舍楼层/专业部 - 下标
     */
    public int parentPosition;
    /**
     * 某一宿舍下标/某一班级下标
     */
    public int childPosition;
    /**
     * 是否能修改（1新增、2修改、3查看，查看即不能修改）
     */
    public int actionType;
    private int nextOrLastClick = 0;
    public String itemName;
    private float alpha = 0.7f;
    /**
     * 上一界面传进来所需要全部参数，宿舍楼listID， 指标项ID、名称,宿舍维度等
     */
    public DormitoryListArgumentBean dormitoryListArgumentBean;

    /**
     * 缓存集合坐标 parentPosition + childPosition，提供给fragment 的hashMap作为Key使用
     */
    public String currentPosition;
    private NextBaseMvpFragment optionFragment;
    private DormitoryOrClassListBean mDormitoryOrClassListBean;
    private List<DormitoryOrClassLevelList> dormitoryOrClassLists = new ArrayList<>();

    /**
     * 保存所需要到的参数，公共参数 -- 录入姓名多项班级、宿舍维度使用，勿删
     */
    public UserBean mUserBean;
    public String InputDate;  //录入日期
    public String UserId;  //用户Id
    public int ItemId;  //指标项Id
    public int RecordId = -1;  //记录Id，如果是新增的为-1，否则为真实的记录Id
    public boolean isAll = true; //是否是全部，还是列表集合
    public List<DormitoryOrClassSingleInfoBean> dormitoryOrClassSingleInfoBeanList = new ArrayList<>();

    public static void start(Activity context, DormitoryListArgumentBean dormitoryListArgumentBean) {
        Intent lIntent = new Intent(context, DormitoryOrClassIndexPointActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_DORMITORY_OR_CLASS_ARGUMENT_BEAN, dormitoryListArgumentBean);
        lIntent.putExtras(bundle);
        context.startActivityForResult(lIntent, DormitoryListActivity.REQUEST_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
            initData(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelable(KEY_DORMITORY_OR_CLASS_ARGUMENT_BEAN, dormitoryListArgumentBean);
    }

    @Override
    public Fragment getFragment() {
        if (optionFragment == null) {
            optionFragment = switchFragment();
        }
        return optionFragment;
    }

    @Override
    protected void initData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            initData(extras);
        }
    }

    private void initData(Bundle extras) {
        dormitoryListArgumentBean = extras.getParcelable(KEY_DORMITORY_OR_CLASS_ARGUMENT_BEAN);
        if (dormitoryListArgumentBean == null) finish();
        if (dormitoryListArgumentBean != null) {
            dormitoryOrClassId = dormitoryListArgumentBean.getDormitoryOrClassId();
            parentPosition = dormitoryListArgumentBean.getParentPosition();
            childPosition = dormitoryListArgumentBean.getChildPosition();
            itemName = dormitoryListArgumentBean.getItemName();
            actionType = dormitoryListArgumentBean.getActionType();
            mDormitoryOrClassListBean = dormitoryListArgumentBean.getDormitoryOrClassListBean();
        }
        if (mDormitoryOrClassListBean != null) {
            if (mDormitoryOrClassListBean.getDormitoryOrClassInfo() != null && !mDormitoryOrClassListBean.getDormitoryOrClassInfo().isEmpty())
                dormitoryOrClassLists = mDormitoryOrClassListBean.getDormitoryOrClassInfo().get(0).getDormitoryOrClassList();
        }
        initTitle();
        initButtonClickable();
        initSaveParams();  //保存需要到的参数
    }

    /**
     * 保存所需要的参数初始化
     */
    private void initSaveParams() {
        if (dormitoryListArgumentBean != null) {
            InputDate = dormitoryListArgumentBean.getDate();
            ItemId = dormitoryListArgumentBean.getItemId();
            RecordId = dormitoryListArgumentBean.getRecordId();
            isAll = dormitoryListArgumentBean.isAll();
            dormitoryOrClassSingleInfoBeanList.clear();
            if(dormitoryListArgumentBean.getDormitoryIdList() != null){
                dormitoryOrClassSingleInfoBeanList.addAll(dormitoryListArgumentBean.getDormitoryIdList());
            }
        }
        try {
            mUserBean = AppConfiguration.getInstance().getUserBean();
        } catch (Exception e) {
            e.printStackTrace();
        }
        UserId = mUserBean.getUserId();
    }

    private void initTitle() {
        if (dormitoryOrClassLists != null && !dormitoryOrClassLists.isEmpty()) {
            mToolBarManager.setTitle(itemName).
                    setSubTitleTv(dormitoryOrClassLists.get(parentPosition).getLevelName() +
                            dormitoryOrClassLists.get(parentPosition).getLevelList().get(childPosition).getName()).setTitleClickListener(v -> DormitoryItemNameDialog.dormitoryItemNameDialog(DormitoryOrClassIndexPointActivity.this, itemName));
        }
    }

    /**
     * 根据宿舍/班级维度，单选/多项、有选项/无选项，返回对应的fragment
     *
     * @return NextBaseMvpFragment
     */
    private NextBaseMvpFragment switchFragment() {
        if (mDormitoryOrClassListBean != null) {
            ItemInfo itemInfo = mDormitoryOrClassListBean.getItemInfo();
            if (itemInfo != null) { // 测试数据为null，下面直接返回
                int itemMode = itemInfo.getItemMode(); // 1增分，或 2扣分
                switch (itemInfo.getScoringTemplateType()) {
                    case Constant.ScoringTemplateType_Single: // 1 单项设置
                        if (itemInfo.getDimensionType() == Constant.DimensionType_Dormitory) {
                            // 宿舍维度
                            return DormitorySingleItemFragment.newInstance(Constant.DimensionType_Dormitory, itemMode, actionType);
                        } else if (itemInfo.getDimensionType() == Constant.DimensionType_Class) {
                            // 班级维度
                            return DormitorySingleItemFragment.newInstance(Constant.DimensionType_Class, itemMode, actionType);
                        }
                        break;
                    case Constant.ScoringTemplateType_Multiple: // 2 多项设置
                        if (itemInfo.getDimensionType() == Constant.DimensionType_Dormitory) {
                            // 宿舍维度
                            return DormitoryEnterNameMultipleDormitoryFragment.newInstance(itemMode, actionType);
                        } else if (itemInfo.getDimensionType() == Constant.DimensionType_Class) {
                            // 班级维度
                            return DormitoryEnterNameMultipleClassFragment.newInstance(itemMode, actionType);
                        }
                        break;
                    case Constant.ScoringTemplateType_HasOption: // 3 有选项设置
                        if (itemInfo.getDimensionType() == Constant.DimensionType_Dormitory) {
                            // 宿舍维度
                            return DormitoryOptionFragment.newInstance(Constant.DimensionType_Dormitory, Constant.ScoringTemplateType_HasOption, itemMode, actionType);
                        } else if (itemInfo.getDimensionType() == Constant.DimensionType_Class) {
                            // 班级维度
                            return DormitoryOptionFragment.newInstance(Constant.DimensionType_Class, Constant.ScoringTemplateType_HasOption, itemMode, actionType);
                        }
                        break;
                    case Constant.ScoringTemplateType_NoOption: // 4 无选项设置
                        if (itemInfo.getDimensionType() == Constant.DimensionType_Dormitory) {
                            // 宿舍维度
                            return DormitoryOptionFragment.newInstance(Constant.DimensionType_Dormitory, Constant.ScoringTemplateType_NoOption, itemMode, actionType);
                        } else if (itemInfo.getDimensionType() == Constant.DimensionType_Class) {
                            // 班级维度
                            return DormitoryOptionFragment.newInstance(Constant.DimensionType_Class, Constant.ScoringTemplateType_NoOption, itemMode, actionType);
                        }
                        break;
                    default:
                }
            }
        }
        return null;
    }

    /**
     * 首次进来判断上下宿舍按钮是否可点击
     */
    private void initButtonClickable() {
        if (null == dormitoryOrClassLists || dormitoryOrClassLists.isEmpty() || null == dormitoryOrClassLists.get(parentPosition))
            return;
        if (parentPosition == dormitoryOrClassLists.size() - 1 && childPosition == dormitoryOrClassLists.get(parentPosition).getLevelList().size() - 1) {
            setNextClickable(false);
        } else {
            setNextClickable(true);
        }
        if (parentPosition == 0 && childPosition == 0) {
            setLastClickable(false);
        } else {
            setLastClickable(true);
        }
        currentPosition = String.valueOf(parentPosition) + String.valueOf(childPosition);
        if(dormitoryListArgumentBean.getDimensionType() == Constant.DimensionType_Dormitory) {
            btnNext.setText(getString(R.string.string_next_dormitory));
            btnLast.setText(getString(R.string.string_last_dormitory));
        } else {
            btnNext.setText(getString(R.string.string_next_class));
            btnLast.setText(getString(R.string.string_last_class));
        }
    }

    /**
     * 设置下一宿舍按钮
     */
    private void setNextClickable(boolean clickable) {
        btnNext.setEnabled(clickable);
    }

    /**
     * 设置上一宿舍按钮
     */
    private void setLastClickable(boolean clickable) {
        btnLast.setEnabled(clickable);
    }

    /**
     * 设置保存按钮
     */
    public void setSaveButtonClickable(boolean clickable) {
        btnSave.setEnabled(clickable);
    }

    /**
     * 数据有改动，弹框提示点击确定/取消监听
     */
    private class DialogListener implements ConfirmOrCancelDialog.Listener {

        @Override
        public void onCancelListener() {
            saveSuccessAndResetButton();
        }

        @Override
        public void onConfirmListener() {
            optionFragment.saveCurrentPageData();
        }

        @Override
        public void onDismissListener() {
//            WindowBackgroundAlpha.backgroundAlpha(DormitoryOrClassIndexPointActivity.this, 1);
        }
    }


    @Override
    public void onNext() {
        if (!optionFragment.isDataChanged()) { //先判断数据是否有改动，有则弹框提示
            onNextProcessPosition();
        } else {
            nextOrLastClick = Constant.DORMITORY_ACTION_NEXT_DORMITORY;
//            WindowBackgroundAlpha.backgroundAlpha(DormitoryOrClassIndexPointActivity.this, alpha);
            new ConfirmOrCancelDialog(this, "数据有修改，是否需要保存？", new DialogListener()).show();
        }
    }

    /**
     * 保存评分录入成功之后，重置保存按钮，并切换上下宿舍下标
     */
    public void saveSuccessAndResetButton() {
        optionFragment.isChanged = false;
        setSaveButtonClickable(false);
        if (nextOrLastClick == Constant.DORMITORY_ACTION_NEXT_DORMITORY){
            onNextProcessPosition();  //不能在这里把位置改变，需要保存成功了才能指向下一宿舍或上一宿舍
        } else if (nextOrLastClick == Constant.DORMITORY_ACTION_PREVIOUS_DORMITORY) {
            onLastProcessPosition();
        }
        nextOrLastClick = 0; // 还原状态
        if(isBackPressed) {
            setResultParams(); //确定保存成功才赋值
            DormitoryOrClassIndexPointActivity.super.onBackPressed();
        }
        isBackPressed = false;
    }

    /**
     * 下一宿舍操作，主要控制层楼下标parentPosition 和 宿舍下标childPosition
     * 正常宿舍下标childPosition +1，当该下标处于当前层楼最后一个宿舍，进行相应操作
     */
    public void onNextProcessPosition() {
        childPosition++;
        if (childPosition > dormitoryOrClassLists.get(parentPosition).getLevelList().size() - 1) { //跳到下一层楼
            parentPosition++;
            childPosition = 0;
        }
        if (childPosition == dormitoryOrClassLists.get(parentPosition).getLevelList().size() - 1) { // 当前层楼最后一个宿舍
            if (parentPosition == dormitoryOrClassLists.size() - 1) {
                // 已经超出最大值，不能继续进行下一宿舍操作,设置不能点击
                setNextClickable(false);
            }
        }
        setLastClickable(true);
        initTitle();
        currentPosition = String.valueOf(parentPosition) + String.valueOf(childPosition);
        dormitoryOrClassId = dormitoryOrClassLists.get(parentPosition).getLevelList().get(childPosition).getId();
        optionFragment.requestData();
    }


    @Override
    public void onLast() {
        if (!optionFragment.isDataChanged()) { //先判断数据是否有改动，有则弹框提示
            onLastProcessPosition();
        } else {
            nextOrLastClick = Constant.DORMITORY_ACTION_PREVIOUS_DORMITORY;
//            WindowBackgroundAlpha.backgroundAlpha(DormitoryOrClassIndexPointActivity.this, alpha);
            new ConfirmOrCancelDialog(this, getString(R.string.string_data_changed_dialog), new DialogListener()).show();
        }
    }

    /**
     * 上一宿舍操作，主要控制层楼下标parentPosition 和 宿舍下标childPosition
     * 正常宿舍下标childPosition -1，当该下标处于当前层楼最前一个宿舍，进行相应操作
     */
    public void onLastProcessPosition() {
        childPosition--;
        if (childPosition < 0) {// 跳到上一楼层
            parentPosition--;
            childPosition = dormitoryOrClassLists.get(parentPosition).getLevelList().size() - 1;
        }
        if (childPosition == 0 && parentPosition == 0) { // 当前楼层第一个宿舍
//            if (parentPosition == 0) { // 已经超出最小值，不能再进行上一宿舍操作,设置不能点击
                setLastClickable(false);
//            }
        }
        setNextClickable(true);
        initTitle();
        currentPosition = String.valueOf(parentPosition) + String.valueOf(childPosition);
        dormitoryOrClassId = dormitoryOrClassLists.get(parentPosition).getLevelList().get(childPosition).getId();
        optionFragment.requestData();
    }

    private boolean isBackPressed = false;

    @Override
    public void onBackPressed() {
        if (!optionFragment.isDataChanged()) {
            setResultParams();
            super.onBackPressed();
        } else {
//            WindowBackgroundAlpha.backgroundAlpha(DormitoryOrClassIndexPointActivity.this, alpha);
            new ConfirmOrCancelDialog(this, getString(R.string.string_data_changed_dialog), new ConfirmOrCancelDialog.Listener() {
                @Override
                public void onCancelListener() {
                    setResultParams();
                    DormitoryOrClassIndexPointActivity.super.onBackPressed();
                }

                @Override
                public void onConfirmListener() {
                    isBackPressed = true;
                    optionFragment.saveCurrentPageData();
//                    setResultParams();
//                    DormitoryOrClassIndexPointActivity.super.onBackPressed();
                }

                @Override
                public void onDismissListener() {
//                    WindowBackgroundAlpha.backgroundAlpha(DormitoryOrClassIndexPointActivity.this, 1);
                }
            }).show();
        }
    }

    /**
     * 回传最新RecordId给宿舍列表页，用作送审操作
     * 把浏览到哪个楼层位置的下标传回宿舍列表页，用于楼层加粗显示
     */
    private void setResultParams() {
        Intent intent = new Intent();
        intent.putExtra(KEY_RECORD_ID, RecordId);
        intent.putExtra(KEY_LEVEL_POSITION, parentPosition);
        setResult(0, intent);
    }

    @Override
    public void onSave() {
        setSaveButtonClickable(false);
        optionFragment.saveCurrentPageData();  //点击保存
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        try {
//            InputMethodManager.class.getDeclaredMethod("windowDismissed", IBinder.class).invoke(imm,
//                    getWindow().getDecorView().getWindowToken());
//        } catch (Exception e){
//            e.printStackTrace();
//        }
        dormitoryOrClassLists.clear();
        dormitoryListArgumentBean = null;
        mDormitoryOrClassListBean = null;
        optionFragment = null;
    }
}
