package com.gta.zssx.dormitory.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.gta.utils.resource.Toast;
import com.gta.zssx.R;
import com.gta.zssx.dormitory.model.bean.DormitoryListArgumentBean;
import com.gta.zssx.dormitory.model.bean.DormitoryOrClassSingleInfoBean;
import com.gta.zssx.dormitory.model.bean.ItemLevelBean;
import com.gta.zssx.dormitory.model.bean.RxItemSelect;
import com.gta.zssx.dormitory.model.bean.RxSaveNeedToOpt;
import com.gta.zssx.dormitory.presenter.DormitoryNewRankingPresenter;
import com.gta.zssx.dormitory.view.DormitoryNewRankingView;
import com.gta.zssx.fun.coursedaily.registercourse.view.adapter.ui.SureClickDialog;
import com.gta.zssx.pub.base.BaseMvpActivity;
import com.gta.zssx.pub.common.Constant;
import com.gta.zssx.pub.util.RxBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import rx.Subscriber;
import rx.Subscription;

/**
 * [Description]
 * 新增评分页Activity
 * [How to use]
 *
 * [Tips]
 *
 * Created by lan.zheng on 2017/7/24 13:14.
 */

public class DormitoryNewRankingActivity extends BaseMvpActivity<DormitoryNewRankingView,DormitoryNewRankingPresenter> implements DormitoryNewRankingView,View.OnClickListener {

    public static final String PAGE_TAG = DormitoryNewRankingActivity.class.getSimpleName();
    public static final String CHECK_LIST_FLAG = "check_list_flag";  //勾选位置记录，为了下次进入拿到勾选的item返回
    public static final String RECORD_LIST = "record_list";  //勾选的
    public static final String DORMITORY_OR_CLASS_LIST = "dormitory_or_class_list";  //所有的,用于内部搜索
    public static final String IS_CLICK_CLEAR = "is_click_clear";//如果点击清空，是选择全部
    public static final int DEFAULT_ITEM_ID = -1; //默认首次进入都是-1

    public static final int CHECK_CAN_NORMAL_INPUT = 0;  //正常录入
    public static final int CHECK_HAVE_BEEN_DELETE_DORMITORY_OT_CLASS = 1; //有宿舍楼或专业部被删除
    public static final int CHECK_HAVE_BENN_INPUT_DORMITORY_OR_CLASS = 2; //有宿舍楼或专业部被登记

    public SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日",Locale.CHINA);
    public static final int REQUEST_CODE = 101;
    public static final int RESULT_CODE = 102;

    private List<DormitoryOrClassSingleInfoBean> mDomitoryOrClassInfoBeanList;  //拿到的所有宿舍楼或专业部
    private List<DormitoryOrClassSingleInfoBean> mRecordList;  //选择的宿舍楼或专业部
    private Set<Integer> mCheckList;  //勾选位置列表
    private RelativeLayout dateSelectLayout;  //日期条目
    private RelativeLayout itemSelectLayout;  //指标项条目
    private RelativeLayout levelSelectLayout;  //宿舍楼或专业部条目
    private TextView dateShowTextView;  //日期显示
    private TextView itemShowTextView;  //指标项显示
    private TextView levelNameTextView;  //宿舍楼或专业部
    private TextView levelShowTextView;  //具体宿舍楼还是专业部显示
    private String mItemName;   //指标项名称
    private String mDateString; //年月日
    private String mInputDate;  //2017-07-07
    private String mDormitoryOrClassNameListString = "0";  //宿舍楼或专业部显示的String，全部的时候这里是“0”
    private Calendar mCalendar;
    private int mItemBelongDimension;  //维度
    private int mItemId;  //选中的指标项Id，用于打钩标识

    private boolean isAllDormitoryOrClass = true;  //是否是全部
    private boolean isNeedToUpdateLevelInfo = false;  //是否要重新获取专业部或宿舍楼列表
    private boolean isItemHaveSelect = false;

    public static void start(Context context) {
        final Intent lIntent = new Intent(context, DormitoryNewRankingActivity.class);

        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_dormitory_new_ranking;
    }

    @Override
    protected void initData() {
        super.initData();
        //初始化列表集合
        mItemId = DEFAULT_ITEM_ID;
        mDomitoryOrClassInfoBeanList = new ArrayList<>();
        mRecordList = new ArrayList<>();
        mCheckList = new HashSet<>();
        mItemBelongDimension = Constant.DimensionType_Dormitory;  //TODO 默认为宿舍维度，当选择指标项后跟着改变即可
    }

    @Override
    protected void initView() {
        sdf = new SimpleDateFormat("yyyy年MM月dd日",Locale.CHINA);

        dateSelectLayout = (RelativeLayout)findViewById(R.id.layout_select_date);
        dateShowTextView = (TextView)findViewById(R.id.tv_dormitory_date_show);
        itemSelectLayout = (RelativeLayout)findViewById(R.id.layout_select_item);
        itemShowTextView = (TextView)findViewById(R.id.tv_dormitory_item_show);
        levelSelectLayout = (RelativeLayout)findViewById(R.id.layout_select_level);
        levelNameTextView = (TextView)findViewById(R.id.tv_dormitory_level);
        levelShowTextView = (TextView)findViewById(R.id.tv_dormitory_level_show);

        mToolBarManager.setTitle(getString(R.string.string_dormitory_new_ranking)).setRightText(getString(R.string.next_step))
                .clickRightButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ListingId = "0";
                        //TODO 检查当前的选择是否能进行登记,全部返回0，勾选过返回列表 String
                        if(!isAllDormitoryOrClass) {
                            ListingId = presenter.formatDormitoryOrClassListId(mRecordList);
                        }
                        Date lDate = null;
                        try {
                            lDate = sdf.parse(mDateString);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
                        mInputDate = simpleDateFormat.format(lDate);
//                        Log.d("lenita","ListingId = "+ListingId+",mInputDate = "+mInputDate);
                        presenter.checkIfCanInput(mItemId,ListingId,mInputDate);
                    }
                });
        mToolBarManager.getRightButton().setEnabled(false);  //默认置灰
        dateSelectLayout.setOnClickListener(this);
        itemSelectLayout.setOnClickListener(this);
        levelSelectLayout.setOnClickListener(this);
/*
        try {
            UserBean mUserBean = AppConfiguration.getInstance().getUserBean();
            mUserId = mUserBean.getUserId();
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        addListener();  //指标项选择回调监听
        getInitDate();  //获取手机默认时间
        updateDateView();  //更新时间View
        getServerTime();   //获取服务器时间
    }

    private void addListener() {
        /**
         * 添加指标项选择回调监听:rx_dormitory_item_select
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
                        ItemLevelBean itemLevelBean = rxItemSelect.getItemLevelBean();
                        mItemId = itemLevelBean.getItemId();
                        mItemName = itemLevelBean.getItemName();
                        mItemBelongDimension = itemLevelBean.getItemDimension();
                        itemShowTextView.setText(mItemName);
                        isItemHaveSelect = true;  //选择过指标项的，标识都为true
                        //TODO 拿到指标项后去请求宿舍/专业部列表
                        if(mItemBelongDimension == Constant.DimensionType_Dormitory){
                            levelNameTextView.setText(getResources().getString(R.string.string_dormitory_dormitory));
                        }else {
                            levelNameTextView.setText(getResources().getString(R.string.string_dormitory_class));
                        }
                        levelShowTextView.setText("全部");
                        presenter.getDormitoryOrClassList(mItemBelongDimension);
                    }
                }));

        /**
         * rx_dormitory_finish_new_ranking_page
         */
        presenter.mCompositeSubscription.add(RxBus.getDefault().toObserverable(RxSaveNeedToOpt.class)
                .subscribe(new Subscriber<RxSaveNeedToOpt>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(RxSaveNeedToOpt rxSaveNeedToOpt) {
                        //TODO 当详情页保存过后，将无法返回到新增页面
                        finish();

                    }
                }));
    }

    private void getServerTime(){
        presenter.getServerTime();
    }


    /**
     * 获取手机的本地时间
     */
    private void getInitDate() {
        mCalendar = Calendar.getInstance();
        mDateString = mCalendar.get(Calendar.YEAR) + "年"
                + ((mCalendar.get(Calendar.MONTH) + 1) > 9 ? (mCalendar.get(Calendar.MONTH) + 1) : "0" + (mCalendar.get(Calendar.MONTH) + 1)) +"月"
                + (mCalendar.get(Calendar.DAY_OF_MONTH) > 9 ? mCalendar.get(Calendar.DAY_OF_MONTH) : "0" + mCalendar.get(Calendar.DAY_OF_MONTH))+"日";
    }

    @NonNull
    @Override
    public DormitoryNewRankingPresenter createPresenter() {
        return new DormitoryNewRankingPresenter();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_select_date:
                selectDate();
                break;
            case R.id.layout_select_item:
                DormitorySelectItemActivity.start(this,DEFAULT_ITEM_ID,true,0,null,mItemId);
                break;
            case R.id.layout_select_level:
                if(!isItemHaveSelect){  //没有选择过指标项的，不给进入宿舍楼或者专业部的选择
                    Toast.Short(DormitoryNewRankingActivity.this,getString(R.string.string_please_select_item_first));
                    return;
                }
//                Log.e("lenita1","mCheckList.size = "+mCheckList.size());
                DormitorySelectLevelActivity.startActivityforResult(DormitoryNewRankingActivity.this,mItemBelongDimension,REQUEST_CODE,mDomitoryOrClassInfoBeanList,mCheckList,isNeedToUpdateLevelInfo);
                break;
            default:
                break;
        }
    }

    @Override
    public void getServerTimeSuccess(String dateTime) {
        //TODO 显示真正的服务器时间
        String split[] = dateTime.split(" ");
        if(split.length > 0){
            mDateString = split[0];
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
            try {
                Date lDate = simpleDateFormat.parse(mDateString);
                mDateString = sdf.format(lDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        updateDateView();
        getCheckList();
    }

    /**
     * 用于判断宿舍楼或专业部的勾选情况,勾选的位置
     */
    private void getCheckList(){
        mCheckList.clear();
        for(int i = 0; i<mDomitoryOrClassInfoBeanList.size();i++){
            mCheckList.add(new Integer(i));
        }
    }

    @Override
    public void getDormitoryOrClassSuccess(List<DormitoryOrClassSingleInfoBean> domitoryOrClassInfoBeanList) {
        mToolBarManager.getRightButton().setEnabled(true);  //把按钮变亮
        mDomitoryOrClassInfoBeanList.clear();
        mDomitoryOrClassInfoBeanList.addAll(domitoryOrClassInfoBeanList);
        mRecordList.clear();
        mRecordList.addAll(domitoryOrClassInfoBeanList);
        getCheckList();
        isNeedToUpdateLevelInfo = false;
    }

    @Override
    public void getDormitoryOrClassFailed() {
        //获取失败了，下一步不能变亮
        mToolBarManager.getRightButton().setEnabled(false);
        //TODO 清空之前的所有选择，让size变为0
        mRecordList.clear();
        mDomitoryOrClassInfoBeanList.clear();
        mCheckList.clear();
        if(mItemBelongDimension == Constant.DimensionType_Dormitory){
            Toast.Short(this,"无法获取宿舍楼信息，请手动进行重新选择");
        }else {
            Toast.Short(this,"无法获取专业部信息，请手动进行重新选择");
        }
        isNeedToUpdateLevelInfo = true;
    }

    @Override
    public void resultIsCanInput(int message,List<Integer> idList) {
        //TODO 需要重新做处理
        isNeedToUpdateLevelInfo = false;
        if(message == CHECK_CAN_NORMAL_INPUT){
            //TODO 新增录入进入列表
            ArrayList<DormitoryOrClassSingleInfoBean> dormitoryOrClassInfoBeanArrayList = new ArrayList<>(mRecordList) ;
            DormitoryListArgumentBean dormitoryListArgumentBean = new DormitoryListArgumentBean();
            dormitoryListArgumentBean.setRecordId(-1);  //新增默认给-1
            dormitoryListArgumentBean.setItemId(mItemId);
            dormitoryListArgumentBean.setItemName(mItemName);
            dormitoryListArgumentBean.setDate(mInputDate);
            dormitoryListArgumentBean.setActionType(Constant.ACTION_TYPE_NEW);  //新增操作
            dormitoryListArgumentBean.setAll(isAllDormitoryOrClass);
            dormitoryListArgumentBean.setDimensionType(mItemBelongDimension);
            dormitoryListArgumentBean.setDormitoryIdList(dormitoryOrClassInfoBeanArrayList);
            DormitoryListActivity.start(this,dormitoryListArgumentBean);
        }else {
            String showMsg = "";
            if(message == CHECK_HAVE_BEEN_DELETE_DORMITORY_OT_CLASS){
                if(idList != null && idList.size() > 0){
                    if(idList.size() == 1 && idList.get(0) == 0){
                        //TODO 全部都被登记
                        showMsg += "无"+(mItemBelongDimension == Constant.DimensionType_Dormitory?"宿舍楼":"专业部")+"存在，无法进行下一步";
                    }else {
                        //TODO 别的情况被登记的
                        showMsg = presenter.getWitchCanNotInputString(idList,mRecordList)+"不存在，请修改";
                    }
                    showHaveBeenSignDialog(showMsg);
                }
                //TODO 因为被删除了，所以置空，并且再次进入选择时需要获取最新的宿舍楼或专业部
                levelShowTextView.setText("");
                mToolBarManager.getRightButton().setEnabled(false);
                isNeedToUpdateLevelInfo = true;
            }else if(message == CHECK_HAVE_BENN_INPUT_DORMITORY_OR_CLASS){
                //TODO 专业部和宿舍楼有已经被录入的
                if(idList != null && idList.size() > 0){
                    /*if(idList.size() == 1 && idList.get(0) == 0){
                        //全部都被登记
                        showMsg += "指标项‘"+mItemName+"'在该日期下没有可以登记的"
                                +(mItemBelongDimension == Constant.DimensionType_Dormitory?"宿舍楼":"专业部");
                    }else {
                        //别的情况被登记的
                        showMsg = "指标项‘"+mItemName+"'在该日期下"+"的‘"+presenter.getWitchCanNotInputString(idList,mRecordList)+"'已被登记过，请修改";
                    }*/
                    showMsg = "该评分日期已录入数据，请返回修改";
                    showHaveBeenSignDialog(showMsg);
                }
                mToolBarManager.getRightButton().setEnabled(false);
            }

        }
    }

    public SureClickDialog mHaveBeenSignDialog;

    /**
     * 有已经录入过的要弹框告知
     * @param content 内容
     */
    private void showHaveBeenSignDialog(String content){
        if (mHaveBeenSignDialog != null)
            mHaveBeenSignDialog = null;
        mHaveBeenSignDialog = new SureClickDialog(this, content,false, new SureClickDialog.Listener() {
            @Override
            public void onDialogDismissListener() {
            }

            @Override
            public void onSureListerner() {
            }
        });
//        mHaveBeenSignDialog.setCancelable(false);
        mHaveBeenSignDialog.show();
    }

    public TimePickerView mTimePickerView;
    private void selectDate() {
        mTimePickerView = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        Date lDate = null;
        try {
            lDate = sdf.parse(mDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mTimePickerView.setTime(lDate);
        mTimePickerView.setCyclic(true);
        mTimePickerView.setCancelable(true);
        mTimePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                mDateString = sdf.format(date);
                if(isItemHaveSelect && !isNeedToUpdateLevelInfo){
                    //如果选择了指标项且能获取到宿舍楼和和专业部信息(isNeedToUpdateLevelInfo = false证明已经是拿到了专业部或宿舍楼信息)
                    mToolBarManager.getRightButton().setEnabled(true);
                }else{
                    mToolBarManager.getRightButton().setEnabled(false);
                }
                updateDateView();
            }
        });
        mTimePickerView.show();
    }

    private void updateDateView() {
        dateShowTextView.setText(mDateString);
    }

    @Override
    public void onBackPressed() {
        if(mTimePickerView != null && mTimePickerView.isShowing()) {
            mTimePickerView.dismiss();
        } else
            super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_CODE){
            //TODO 拿到勾选列表和勾选值列表
            mCheckList = (Set<Integer>) data.getExtras().getSerializable(CHECK_LIST_FLAG);
            mRecordList = (List<DormitoryOrClassSingleInfoBean>) data.getExtras().getSerializable(RECORD_LIST);
//            Toast.Short(DormitoryNewRankingActivity.this,"recordList.size = " + mRecordList.size()+",mCheckList.size() = "+mCheckList.size());
            mDomitoryOrClassInfoBeanList = (List<DormitoryOrClassSingleInfoBean>) data.getExtras().getSerializable(DORMITORY_OR_CLASS_LIST);
            isAllDormitoryOrClass = data.getExtras().getBoolean(IS_CLICK_CLEAR,false);
            //TODO 当点击确定的时候，下一步变亮，因为有可能原来没有获取到数据，后面进入宿舍楼专业部选择获取到了
            if(mDomitoryOrClassInfoBeanList.size() > 0){
                mToolBarManager.getRightButton().setEnabled(true);
            }
            if(isAllDormitoryOrClass){  // 全部
                mDormitoryOrClassNameListString = "0";
                levelShowTextView.setText("全部");
            }else {   //显示具体
                mDormitoryOrClassNameListString = presenter.formatDormitoryOrClassListName(mRecordList);
                levelShowTextView.setText(mDormitoryOrClassNameListString);
            }
            isNeedToUpdateLevelInfo = false;
        }
    }
}
