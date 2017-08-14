package com.gta.zssx.dormitory.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.blankj.utilcode.util.ToastUtils;
import com.gta.zssx.R;
import com.gta.zssx.ZSSXApplication;
import com.gta.zssx.dormitory.model.bean.DormitoryListArgumentBean;
import com.gta.zssx.dormitory.model.bean.OptionBean;
import com.gta.zssx.dormitory.model.bean.OptionHttpBean;
import com.gta.zssx.dormitory.model.bean.RxDormitoryRankingListUpdateBean;
import com.gta.zssx.dormitory.model.bean.RxRefreshDormitoryListBean;
import com.gta.zssx.dormitory.model.bean.RxSaveNeedToOpt;
import com.gta.zssx.dormitory.presenter.DormitoryOrClassListSubmitPresenter;
import com.gta.zssx.dormitory.view.DormitoryOrClassListSubmitView;
import com.gta.zssx.pub.base.BaseMvpActivity;
import com.gta.zssx.pub.common.Constant;
import com.gta.zssx.pub.util.DormitoryItemNameDialog;
import com.gta.zssx.pub.util.RxBus;
import com.gta.zssx.pub.widget.CustomToast;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import rx.Subscriber;


/**
 * [Description]
 * <p> 宿舍列表/班级列表界面
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Weiye.Chen on 2017/7/20.
 * @since 2.0.0
 */
public class DormitoryListActivity extends BaseMvpActivity<DormitoryOrClassListSubmitView, DormitoryOrClassListSubmitPresenter> implements DormitoryOrClassListSubmitView {
    public static final int REQUEST_CODE = 321;
    /**
     * 指标项ID,宿舍楼ID，指标项名称，日期等参数
     */
    public static final String KEY_DORMITORY_LIST_ARGUMENT = "key_dormitory_list_argument";

    private DormitoryListFragment dormitoryListFragment;
    private Bundle mBundle;
    @Bind(R.id.rl_sub_title)
    RelativeLayout rlDateContainer;
    @Bind(R.id.tv_dormitory_date)
    TextView tvDate;
    @Bind(R.id.iv_dormitory_date_arrow)
    ImageView ivDateArrow;
    /**
     * 1新增，2修改，3查看
     */
    private int actionType;
    /** 记录ID，每条记录唯一ID*/
    public int recordId;
    /**宿舍维度/班级维度*/
    private int dimensionType;
    /** 指标项名称*/
    private String itemName;
    /**指标项ID*/
    private int itemId;
    /**评分日期*/
    public String inputDate;
    /**宿舍楼ID/专业部ID，用于送审网络操作*/
    private String buildingOrDeptIds;
    private Date mTempDate;
    public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    private boolean isNeedToRefresh;

    /**
     *                recordId 记录ID
     *                itemId   指标项ID
     *                itemName 指标项名称
     *                date     日期
     *                dimensionType 1为宿舍维度, 2 为班级维度
     *                actionType  1新增、2编辑、3查看 状态
     *                isAll     是否全部宿舍楼/专业部
     *                dormitoryIdList   宿舍楼/专业部 list ID
     * @param context context
     */
    public static void start(Context context, DormitoryListArgumentBean dormitoryListArgumentBean) {
        Intent lIntent = new Intent(context, DormitoryListActivity.class);
        Bundle lBundle = new Bundle();
        lBundle.putParcelable(KEY_DORMITORY_LIST_ARGUMENT, dormitoryListArgumentBean);
        lIntent.putExtras(lBundle);
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }

    private MyHandler myHandler;

    private static class MyHandler extends Handler {
        WeakReference<Activity> mActivityReference;

        MyHandler(Activity activity) {
            mActivityReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final Activity activity = mActivityReference.get();
            if (activity != null) {
                activity.finish();
            }
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_dormitory_list;
    }

    @Override
    protected void initData() {
        mBundle = getIntent().getExtras();
        if (null == mBundle)
            mBundle = new Bundle();
        DormitoryListArgumentBean dormitoryListArgumentBean = mBundle.getParcelable(KEY_DORMITORY_LIST_ARGUMENT);
        if (dormitoryListArgumentBean != null) {
            recordId = dormitoryListArgumentBean.getRecordId();
            itemName = dormitoryListArgumentBean.getItemName();
            actionType = dormitoryListArgumentBean.getActionType();
            inputDate = dormitoryListArgumentBean.getDate();
            itemId = dormitoryListArgumentBean.getItemId();
            dimensionType = dormitoryListArgumentBean.getDimensionType();
            boolean isAllDormitoryOrClass = dormitoryListArgumentBean.isAll();
            buildingOrDeptIds = presenter.formatBuildingOrDeptIDs(isAllDormitoryOrClass, dormitoryListArgumentBean.getDormitoryIdList());
        }
    }

    @Override
    protected void initView() {
        myHandler = new MyHandler(this);
        initTitle();
        updateTitleDate();
        initFragment();
        addListener();
    }

    private void initTitle() {
        RightButtonAndSubTitleClickListener listener = new RightButtonAndSubTitleClickListener();
        if (actionType == Constant.ACTION_TYPE_JUST_CHECK) { // 查看状态，不显示送审按钮
            mToolBarManager.setTitle(itemName).setTitleClickListener(listener).setRightText("");
            ivDateArrow.setVisibility(View.GONE);
        } else {
            rlDateContainer.setOnClickListener(listener); // 日期栏点击监听，非查看状态才可以点击
            mToolBarManager.setTitle(itemName).setTitleClickListener(listener).setRightText(getString(R.string.string_submit)).clickRightButton(listener);
        }

    }

    private void initFragment() {
        dormitoryListFragment = new DormitoryListFragment();
        dormitoryListFragment.setArguments(mBundle);
        getSupportFragmentManager().beginTransaction().add(R.id.container, dormitoryListFragment, dormitoryListFragment.getTag()).commitAllowingStateLoss();

    }

    private void addListener() {
          /*更新宿舍列表 update dormitory list*/
        presenter.mCompositeSubscription.add(RxBus.getDefault().toObserverable(RxRefreshDormitoryListBean.class).subscribe(new Subscriber<RxRefreshDormitoryListBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(RxRefreshDormitoryListBean aBoolean) {
                if(aBoolean != null)
                    isNeedToRefresh = true;
            }
        }));
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isNeedToRefresh) {
            if (dormitoryListFragment != null && dormitoryListFragment.getAdapter() != null) {
                ((DormitoryListInnerFragment) dormitoryListFragment.getAdapter().getCurFragment()).requestData();
            }
            isNeedToRefresh = false;
        }
    }


    /**
     * 更新标题栏日期显示
     */
    private void updateTitleDate() {
        tvDate.setText(inputDate);
    }

    @NonNull
    @Override
    public DormitoryOrClassListSubmitPresenter createPresenter() {
        return new DormitoryOrClassListSubmitPresenter();
    }

    /**
     * 送审点击，送审操作
     * 日期栏点击，修改日期操作
     * 标题点击，弹框显示标题操作
     */
    private class RightButtonAndSubTitleClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_sub_title:  // 点击标题栏切换日期操作
                    showTimePicker();
                    break;
                case R.id.right_button: // 点击送审操作
                    OptionHttpBean optionHttpBean = new OptionHttpBean();
                    optionHttpBean.setRecordId(recordId);
                    optionHttpBean.setBuildingOrDeptIds(buildingOrDeptIds);
                    optionHttpBean.setInputDate(inputDate);
                    optionHttpBean.setItemId(itemId);
                    optionHttpBean.setUserId(ZSSXApplication.instance.getUser().getUserId());
                    optionHttpBean.setDetailContent(new OptionBean());
                    presenter.submitDormitoryOrClassList(optionHttpBean, Constant.DORMITORY_ACTION_SUBMIT, dimensionType);
                    break;
                case R.id.title:  // 标题点击
                    DormitoryItemNameDialog.dormitoryItemNameDialog(DormitoryListActivity.this, itemName);
                    break;
                default:
            }
        }
    }

    private TimePickerView mTimePickerView;
    /**
     * 底部向上弹出时间选择器：年月日
     */
    private void showTimePicker() {
        mTimePickerView = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        Date lDate = null;
        try {
            lDate = sdf.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mTimePickerView.setTime(lDate);
        mTimePickerView.setCyclic(true);
        mTimePickerView.setCancelable(true);
        mTimePickerView.setOnTimeSelectListener(date -> {
            mTempDate = date;  // 临时缓存，网络判断成功之后再更新此日期
            presenter.checkIfCanInput(itemId, buildingOrDeptIds, sdf.format(date), recordId);
        });
        mTimePickerView.show();
    }

    @Override
    public void onBackPressed() {
        if(mTimePickerView != null && mTimePickerView.isShowing()) {
            mTimePickerView.dismiss();
        } else
            super.onBackPressed();
    }

    /**
     * 送审成功、失败
     * @param success 成功、失败
     */
    @Override
    public void submitSuccessOrFail(boolean success) {
        if (success) {
            CustomToast.ToastWithImageShort(this, R.drawable.ic_submit_check_large, getString(R.string.string_submit_success));
            //新增进入的，送审后要finish新增录入页面
            //rx_dormitory_finish_new_ranking_page
            RxBus.getDefault().post(new RxSaveNeedToOpt());
            /*
              添加刷新页面监听:rx_dormitory_list_refresh
             */
            RxDormitoryRankingListUpdateBean dormitoryRankingListUpdateBean = new RxDormitoryRankingListUpdateBean();
            dormitoryRankingListUpdateBean.setNeedToUpdatePage(RxDormitoryRankingListUpdateBean.REFRESH_ALL_LIST);
            dormitoryRankingListUpdateBean.setSpecialRefresh(true); //定位到“已送审”页面
            RxBus.getDefault().post(dormitoryRankingListUpdateBean);
            myHandler.sendEmptyMessageAtTime(1, 1000); // 延期0.5秒后关闭当前Activity
        } else
            ToastUtils.showShortToast(getString(R.string.string_submit_fail));
    }

    /**
     * 修改日期，判断该日期是否能进行评分录入
     * @param success 是否修改成功，true 成功 false 失败
     */
    @Override
    public void changedDateSuccess(boolean success) {
        if(success) {  // 能正常切换日期,更新标题栏日期显示
            if(mTempDate != null) {
                inputDate = sdf.format(mTempDate);
            }
            updateTitleDate();
            if(recordId != -1) {
                RxDormitoryRankingListUpdateBean dormitoryRankingListUpdateBean = new RxDormitoryRankingListUpdateBean();
                dormitoryRankingListUpdateBean.setNeedToUpdatePage(RxDormitoryRankingListUpdateBean.REFRESH_NOT_SUBMIT_LIST);
                RxBus.getDefault().post(dormitoryRankingListUpdateBean);
            }
            ToastUtils.showShortToast(R.string.string_change_dormitory_date_success);
        } else
            ToastUtils.showShortToast(R.string.string_dormitory_date_exist);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (dormitoryListFragment != null && dormitoryListFragment.getAdapter() != null) {
                if (data != null) {
                    DormitoryListInnerFragment fragment = ((DormitoryListInnerFragment) dormitoryListFragment.getAdapter().getCurFragment());
                    fragment.refreshHeaderDecoration(data.getIntExtra(DormitoryOrClassIndexPointActivity.KEY_LEVEL_POSITION, 0)); // 把浏览到哪个楼层位置的下标传回来
                    recordId = data.getIntExtra(DormitoryOrClassIndexPointActivity.KEY_RECORD_ID, 0);// 当新增的时候为-1，保存过之后返回对应的值
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(myHandler != null) {
            myHandler.removeCallbacksAndMessages(this);
            myHandler = null;
        }
    }
}
