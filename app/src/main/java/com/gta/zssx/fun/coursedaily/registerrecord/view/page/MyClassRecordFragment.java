package com.gta.zssx.fun.coursedaily.registerrecord.view.page;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.blankj.utilcode.util.ToastUtils;
import com.gta.utils.helper.Helper_String;
import com.gta.utils.resource.Toast;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.view.pageV2.CourseDetailActivity;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.ClassInfoDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.DeleteRecordDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.MyClassRecordDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.RegisteredRecordFromSignatureDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.RxApproveStatus;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.SaveCacheDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.RegisteredRecordManager;
import com.gta.zssx.fun.coursedaily.registerrecord.presenter.MyClassRecordPresenter;
import com.gta.zssx.fun.coursedaily.registerrecord.view.MyClassRecordView;
import com.gta.zssx.fun.coursedaily.registerrecord.view.SelectOprationPopupWindow;
import com.gta.zssx.fun.coursedaily.registerrecord.view.adapter.MyClassRecordListNewAdapter;
import com.gta.zssx.fun.coursedaily.registerrecord.view.base.RegisterRecordBaseFragment;
import com.gta.zssx.fun.coursedaily.registerrecord.view.base.RegisterRecordFragmentBuilder;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.pub.util.LogUtil;
import com.gta.zssx.pub.util.RxBus;
import com.gta.zssx.pub.widget.ConfirmOrCancelDialog;

import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rx.Subscriber;

/**
 * [Description]
 * <p/>
 * [How to use]
 * <p/>
 * [Tips]
 *
 * @author Created by Lan.Zheng on 2016/6/25.
 * @since 1.0.0  用于单个页面
 */
public class MyClassRecordFragment extends RegisterRecordBaseFragment<MyClassRecordView, MyClassRecordPresenter> implements MyClassRecordView {

    public static final String PAGE_TAG = RegisterRecordBaseFragment.class.getSimpleName();
    //长按操作标识
    private final static int ACTION_NOT_OPR = 0;
    private final static int ACTION_MODIFY = 1;  //修改
    private final static int ACTION_DELETE = 2;  //删除
    private final static int ACTION_CANCEL = 3;  //取消
    private RegisteredRecordFromSignatureDto mRegisteredRecordFromSignatureDto;  //删除或修改的对象内容
    private int mDeleteItemPosition;  //删除的位置
    private int SectionID;  //删除的节次id
    //    public static boolean mIsSubTab = false;
    //    private static String CLASS_ID = "CLASS_ID";
    private static String CLASS_INFO ="CLASS_INFO ";
    private static String TO_GET_SERVER_TIME = "TO_GET_SERVER_TIME";
    private final static int JUST_ONE_CLASS = 0 ;

    public RecyclerView mRecyclerView;
    public UserBean mUserBean;
    public UserBean.ClassList mClassEntry;
    public TextView mDateTv;
    public TextView mApproveStatusTextView;   //审核状态
    public TextView mNotResultTextView;
    public TimePickerView mTimePickerView;
    public String mSearchDate;  //查询时间
    public String mDayOfWeek;

    //班级名称和班级ID,以及记录集
    private int ClassIDDeleteOpr;  //删除或修改时的ID
    private String TeacherID;
    private String mClassName;
    private int mClassID;
    private boolean mIsFirstGetDateandWeek = true;  //第一次进入一定是真
//    private MyClassRecordListAdapter lAdapter;
    private MyClassRecordListNewAdapter lAdapter;
    private SimpleDateFormat sdf;

    //登记日期和领导审核日对比结果
    private boolean haveBeenApprove = true;  //未审核false，默认都是已审核，以防误操作
    //是否是长按，长按的话要重新去获取领导审核日，并改变状态
    private boolean isLongClick = false;
    //是否是弹框的操作
    private boolean showSelectOprationWindows = false;
    //是否是确认删除操作
    private boolean isSureDeleteClick = false;

    @NonNull
    @Override
    public MyClassRecordPresenter createPresenter() {
        return new MyClassRecordPresenter();
    }

    private void showList(List<MyClassRecordDto> myClassRecordDtos) {
        if(myClassRecordDtos.get(0) == null && myClassRecordDtos.get(0).getCourse() == null){
            if(lAdapter != null){
                lAdapter.clearUpData();
                lAdapter = null;
            }
            mApproveStatusTextView.setVisibility(View.INVISIBLE);
            mNotResultTextView.setVisibility(View.VISIBLE);
            Toast.Short(mActivity, mActivity.getResources().getString(R.string.text_not_result));
            return;
        }
        String ClassName = myClassRecordDtos.get(0).getClassName();
        //把数据组装封装入List<RegisteredRecordFromSignatureDto>
        List<RegisteredRecordFromSignatureDto> recordFromSignatureDtos = myClassRecordDtos.get(0).getCourse();
        setCourse(ClassName ,recordFromSignatureDtos);
    }

    private void setCourse(String ClassName,List<RegisteredRecordFromSignatureDto> recordFromSignatureDtos) {
        MyClassRecordListNewAdapter.Listener lListener = new MyClassRecordListNewAdapter.Listener() {
            @Override
            public void itemClick(RegisteredRecordFromSignatureDto registeredRecordFromSignatureDto) {
                ClassInfoDto lClassInfoDto = new ClassInfoDto();
                lClassInfoDto.setClassID(registeredRecordFromSignatureDto.getClassID() + "");
                lClassInfoDto.setClassName(registeredRecordFromSignatureDto.getClassName());
                lClassInfoDto.setSignDate(mSearchDate);
                if(mUserBean.isTeacherName()){
                    lClassInfoDto.setTeacherID("0");
                }else {
                    lClassInfoDto.setTeacherID(mUserBean.getUserId());
                }
                lClassInfoDto.setSectionID(registeredRecordFromSignatureDto.getSection().getSectionOriginalId());
                DetailRegisteredRecordActivity.start(mActivity,lClassInfoDto);
//                MyClassRecordPresenter.mIsShowCache = true;  //跳转到详情页面，回来要加载缓存回来
//                new DetailRegisteredRecordFragment.Builder(mActivity, lClassInfoDto).display();
            }

            @Override
            public void itemLongClick(RegisteredRecordFromSignatureDto registeredRecordFromSignatureDto,int position) {
                //长按操作
                LogUtil.Log("lenita", "itemLongClickDialog SectionID = " + registeredRecordFromSignatureDto.getSection().getSectionOriginalId() + ",pos = " + position);
                SectionID = registeredRecordFromSignatureDto.getSection().getSectionOriginalId();
                if (haveBeenApprove) {
                    return;
                }
                //如果是没有审核的，长按显示删除，修改，取消的dialog(都是可以操作的，因为都是当天该老师登记的信息)
                isLongClick = true;
                showSelectOprationWindows = true;
                mRegisteredRecordFromSignatureDto = registeredRecordFromSignatureDto;
                mDeleteItemPosition = position;
                getApproveData();  //弹框前次获取领导审核日的对比
            }
        };


        if(recordFromSignatureDtos.size() > 0){
            for(int i= 0; i < recordFromSignatureDtos.size();i++){
                recordFromSignatureDtos.get(i).setClassName(ClassName);
                recordFromSignatureDtos.get(i).setClassID(mClassID);
            }
            mApproveStatusTextView.setVisibility(View.VISIBLE);
            String showApproveText = haveBeenApprove?getResources().getString(R.string.text_have_been_approve):getResources().getString(R.string.text_no_approve);
            mApproveStatusTextView.setText(showApproveText);
            lAdapter = null;
            lAdapter = new MyClassRecordListNewAdapter(mActivity, lListener, recordFromSignatureDtos);
            mRecyclerView.setAdapter(lAdapter);
            mNotResultTextView.setVisibility(View.GONE);
            getApproveData();  //有数据的时候直接去获取领导审核日的对比，然后改变审核状态
        }else {
            if(lAdapter != null){
                lAdapter.clearUpData();
                lAdapter = null;
            }
            mApproveStatusTextView.setVisibility(View.INVISIBLE);
            mNotResultTextView.setVisibility(View.VISIBLE);
            Toast.Short(mActivity, mActivity.getResources().getString(R.string.text_not_result));
        }
    }

    @Override
    public void showResult(List<MyClassRecordDto> recordFromSignatureDtos) {
        if(recordFromSignatureDtos == null || recordFromSignatureDtos.size() == 0){
            if(lAdapter != null){
                lAdapter.clearUpData();
                lAdapter = null;
            }
            mApproveStatusTextView.setVisibility(View.INVISIBLE);
            mNotResultTextView.setVisibility(View.VISIBLE);
            Toast.Short(mActivity, mActivity.getResources().getString(R.string.text_not_result));
            return;
        }
        showList(recordFromSignatureDtos);
    }

    @Override
    public void notResult() {
        if(lAdapter != null){
            lAdapter.clearUpData();
            lAdapter = null;
        }
        mApproveStatusTextView.setVisibility(View.INVISIBLE);
        mNotResultTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setServerTime(String date, String week) {
        Date lDate = null;
        SimpleDateFormat lSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            lDate = lSimpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mSearchDate = sdf.format(lDate);
        saveDate();  //缓存日期，服务器获取到的
        mDayOfWeek = week;
        String dateString = mSearchDate + " " + mDayOfWeek;
        mDateTv.setText(dateString);
        getMyClassData(); //获取当天数据

    }

    @Override
    public void setServerTimeFailed(boolean isNotNetwork) {
        LogUtil.Log(PAGE_TAG,"获取服务器时间失败");
        if(isNotNetwork){
            notResult();  //第一次进入没有网络
            return;
        }
        getMyClassData(); //有网络的情况下，才需要获取当天数据，否则不需要获取
    }

    @Override
    public void showApproveResult(boolean isApprove) {
        haveBeenApprove = isApprove;
        String showApproveText = haveBeenApprove?getResources().getString(R.string.text_have_been_approve):getResources().getString(R.string.text_no_approve);
        mApproveStatusTextView.setText(showApproveText);
        //TODO 新添加操作，在点击删除“确认”后
        if(isSureDeleteClick){
            isSureDeleteClick = false; //删不删都重置
            isLongClick = false;  //删不删都重置
            if(haveBeenApprove){
                //TODO 弹出提示无法删除，并刷新界面把状态变成已审核
                ToastUtils.showLongToast(mActivity.getResources().getString(R.string.text_can_not_delete));
                lAdapter.setApproveStatus(haveBeenApprove);
            }else {
                deleteItem();
            }
            //当是确认删除，无论什么情况都不往下执行
            return;
        }
        if (isLongClick) {
            if (haveBeenApprove) {
                //当长按并且当前数据已经是审核后
                if(lAdapter != null)
                    lAdapter.setApproveStatus(haveBeenApprove);
            }
            if (showSelectOprationWindows) {
                //长按弹框
                popupSelectOpration();
            }
            showSelectOprationWindows = false;  //弹框后重置为默认状态
            //修改、删除、取消
            oprClickAction();
        }else {
            if(lAdapter != null)
                lAdapter.setApproveStatus(haveBeenApprove);
        }

    }

    private void deleteItem(){
        if(lDtoList != null){
            presenter.deleteSignInfo(lDtoList);
            lDtoList = null;
        }
    }

    public int clickAction = ACTION_NOT_OPR;
    public boolean clickBlankLetWindowGone = true;
    public SelectOprationPopupWindow mSelectOprationPopupWindow;
    private void popupSelectOpration() {
        clickAction = ACTION_NOT_OPR;      //没有操作
        clickBlankLetWindowGone = true;  //点击空白处返回的
        backgroundAlpha(0.8f);
        mSelectOprationPopupWindow = new SelectOprationPopupWindow(mActivity, haveBeenApprove, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickBlankLetWindowGone = false;    //非点击空白处
                showSelectOprationWindows = false;  //不需要显示弹框，而是选择了操作
                mSelectOprationPopupWindow.dismiss();
                switch (v.getId()) {
                    case R.id.tv_modify:
                        clickAction = ACTION_MODIFY;
                        getApproveData();
                        break;
                    case R.id.tv_delete:
                        clickAction = ACTION_DELETE;
                        getApproveData();
                        break;
                    case R.id.tv_cancel_show:
                        clickAction = ACTION_CANCEL;
                        getApproveData();
                        break;
                    default:
                        break;
                }
            }
        });
        //显示窗口消失后让背景恢复
        mSelectOprationPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                LogUtil.Log("lenita", "onDismiss()clickBlankLetWindowGone = " + clickBlankLetWindowGone);
                if (clickBlankLetWindowGone) {
                    isLongClick = false;
                    clickAction = ACTION_NOT_OPR;
                }
                clickBlankLetWindowGone = true;
                backgroundAlpha(1f);
            }
        });
        mSelectOprationPopupWindow.showAtLocation(mActivity.findViewById(R.id.container), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
    }

    private void backgroundAlpha(float b) {
        WindowManager.LayoutParams layoutParams = mActivity.getWindow().getAttributes();
        layoutParams.alpha = b;
        mActivity.getWindow().setAttributes(layoutParams);
    }

    private void oprClickAction() {
        if (clickAction == ACTION_NOT_OPR) {
            LogUtil.Log("lenita", "oprClickAction() ACTION_NOT_OPR");
            return;
        }
        switch (clickAction) {
            case ACTION_MODIFY:
                if (haveBeenApprove) {
                    Toast.Long(mActivity, mActivity.getResources().getString(R.string.text_can_not_modify));
                } else {
                    //跳转到修改页面
                    mRegisteredRecordFromSignatureDto.setSignDate(mSearchDate);
                    //TODO 修改记录-待定
                    CourseDetailActivity.start(mActivity, mRegisteredRecordFromSignatureDto.getClassName(), true, mRegisteredRecordFromSignatureDto, -1);
                }
                isLongClick = false;
                mSelectOprationPopupWindow = null;  //资源回收
                break;
            case ACTION_DELETE:
                if (haveBeenApprove) {
                    isLongClick = false;
//                    Toast.makeText(mActivity, mActivity.getResources().getString(R.string.text_can_not_delete), Toast.LENGTH_SHORT).show();
                    Toast.Long(mActivity, mActivity.getResources().getString(R.string.text_can_not_delete));
                } else {
                    LogUtil.Log("lenita", "弹出对话框，dormitoryItemNameDialog()");
                    //弹出对话框，询问是否真的要删除
                    popupConfirmDialog(mActivity.getResources().getString(R.string.text_sure_delete_ask));
                }
                mSelectOprationPopupWindow = null;
                break;
            case ACTION_CANCEL:
                isLongClick = false;
                mSelectOprationPopupWindow = null;  //资源回收
                break;
            default:
                break;
        }
        clickAction = ACTION_NOT_OPR;  //点击任何操作后，都要设置回初始值
    }

    private ConfirmOrCancelDialog modifyDialog;
//    public TextView modifyContent;
    private List<DeleteRecordDto> lDtoList;

    private void popupConfirmDialog(String content) {
        modifyDialog =  new ConfirmOrCancelDialog(mActivity, content, new ConfirmOrCancelDialog.Listener() {
            @Override
            public void onCancelListener() {
                getApproveData();
                isLongClick = false;  //返回要置回值false
            }

            @Override
            public void onConfirmListener() {
                SectionID = mRegisteredRecordFromSignatureDto.getSection().getSectionOriginalId();
                ClassIDDeleteOpr = mRegisteredRecordFromSignatureDto.getClassID();
                LogUtil.Log("lenita", "删除本条记录 SectionID = " + SectionID + " , ClassIDDeleteOpr = " + ClassIDDeleteOpr);
                DeleteRecordDto lDeleteRecordDto = new DeleteRecordDto();
                lDeleteRecordDto.setClassID(ClassIDDeleteOpr);
                lDeleteRecordDto.setSectionID(SectionID);
                lDeleteRecordDto.setSignDate(mSearchDate);
                lDtoList = new ArrayList<DeleteRecordDto>();
                lDtoList.add(lDeleteRecordDto);
                //最终确认删除前再次去判断领导审核日
                isSureDeleteClick = true;
                getApproveData();
                //删除本条记录
//                    presenter.deleteSignInfo(lDtoList);
//                    lDtoList = null;
            }

            @Override
            public void onDismissListener() {
            }
        });
        modifyDialog.show();
        /*backgroundAlpha(0.8f);
        if (modifyDialog == null) {
            View contentView = LayoutInflater.from(mActivity).inflate(R.layout.dialog_delete_comfirn, null);
            modifyContent = (TextView) contentView.findViewById(R.id.dialog_content_text);
            modifyContent.setText(content);
            View btnOK = contentView.findViewById(R.id.dialog_btn_confirm);
            View btnBack = contentView.findViewById(R.id.dialog_btn_cancel);
            btnOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    modifyDialog.dismiss();
//                    backgroundAlpha(1f);
                    SectionID = mRegisteredRecordFromSignatureDto.getSection().getSectionOriginalId();
                    ClassIDDeleteOpr = mRegisteredRecordFromSignatureDto.getClassID();
                    LogUtil.Log("lenita", "删除本条记录 SectionID = " + SectionID + " , ClassIDDeleteOpr = " + ClassIDDeleteOpr);
                    DeleteRecordDto lDeleteRecordDto = new DeleteRecordDto();
                    lDeleteRecordDto.setClassID(ClassIDDeleteOpr);
                    lDeleteRecordDto.setSectionID(SectionID);
                    lDeleteRecordDto.setSignDate(mSearchDate);
                    lDtoList = new ArrayList<DeleteRecordDto>();
                    lDtoList.add(lDeleteRecordDto);
                    //最终确认删除前再次去判断领导审核日
                    isSureDeleteClick = true;
                    getApproveData();
                    //删除本条记录
//                    presenter.deleteSignInfo(lDtoList);
//                    lDtoList = null;
                }
            });
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    modifyDialog.dismiss();
//                    backgroundAlpha(1f);
                    getApproveData();
                    isLongClick = false;  //返回要置回值false
                }
            });
            modifyDialog = new Dialog(mActivity, R.style.myDialogTheme);  //使用自定义的样式
            modifyDialog.setCanceledOnTouchOutside(false);  //外围点击不消失
            modifyDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    backgroundAlpha(1f);
                }
            });
            modifyDialog.setContentView(contentView);
        }
        modifyDialog.show();*/
    }

    @Override
    public void getApproveFailed(boolean showToast) {

         haveBeenApprove = true;  //失败的都算是已审核，不给长按
         if(lAdapter != null)
             lAdapter.setApproveStatus(haveBeenApprove);
        isLongClick = false;
        mSelectOprationPopupWindow = null;
        clickAction = ACTION_NOT_OPR;
        if (showToast)
            Toast.Long(mActivity, mActivity.getResources().getString(R.string.text_get_approve_failed));
    }

    @Override
    public void deleteResult() {
        //更新本界面的信息，删除成功
//        Toast.makeText(mActivity, mActivity.getResources().getString(R.string.text_delete_success), Toast.LENGTH_SHORT).show();
        Toast.Short(mActivity, mActivity.getResources().getString(R.string.text_delete_success));
        isLongClick = false;
        lAdapter.removeData(mDeleteItemPosition);  //移除某条数据动画，移除后还是去获取一次领导审核日
        getApproveData();
    }

    @Override
    public void deleteFailed() {
        isLongClick = false;
        getApproveData();
    }

    SaveCacheDto lSaveCacheDto ;
    private void saveDate(){
        lSaveCacheDto = new SaveCacheDto();    //缓存
        lSaveCacheDto.setShowDate(mSearchDate);
        RegisteredRecordManager.setSaveCacheDtoDate(lSaveCacheDto);
    }

    /**
     * 去服务器获取领导审核日数据
     */
    private void getApproveData() {
        presenter.getApproveDate(mSearchDate);  //去服务器获取领导审核日数据,和服务器时间对比，得到审核状态
    }


    public static class Builder extends RegisterRecordFragmentBuilder<MyClassRecordFragment> {

        //        int mClassId;
        UserBean.ClassList mClassEntry;

        public Builder(Context context,UserBean.ClassList classList) {
            super(context);
//            mClassId = classList.getId();
            mClassEntry = classList;
        }

        @Override
        public MyClassRecordFragment create() {
            Bundle lBundle = new Bundle();
//            lBundle.putInt(CLASS_ID, mClassId);
            lBundle.putSerializable(CLASS_INFO,mClassEntry);
            lBundle.putBoolean(TO_GET_SERVER_TIME,true);   //TODO create都要重新去获取服务器时间,这里很有可能会重新生成一个新的Fragment，导致有两层
            MyClassRecordFragment lMyClassRecordFragment = new MyClassRecordFragment();
            lMyClassRecordFragment.setArguments(lBundle);
            return lMyClassRecordFragment;
        }

        @Override
        public String getPageTag() {
            return PAGE_TAG;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        dataAction();
    }

    //fragment间参数传递
    private void dataAction() {
        getLocalDate();  //首次获取手机时间
        try {
            mUserBean = AppConfiguration.getInstance().getUserBean();
        } catch (Exception e) {
            e.printStackTrace();
        }
        TeacherID = mUserBean.getUserId();
        mClassID = JUST_ONE_CLASS;  //单个页面ClassID=0，去获取数据
        mClassName = getResources().getString(R.string.my_class);
        mClassEntry = (UserBean.ClassList)getArguments().getSerializable(CLASS_INFO);
        mClassID = mClassEntry.getId();
        mClassName = mClassEntry.getClassName();
        mIsFirstGetDateandWeek = getArguments().getBoolean(TO_GET_SERVER_TIME,true); //是否要去获取系统时间
        if(RegisteredRecordManager.getSaveCacheDtoDate() != null)
            mIsFirstGetDateandWeek = false;  //有缓存，不用去获取系统时间
        LogUtil.Log("lenita","获取缓存中的日期?mIsFirstGetDateandWeek = "+mIsFirstGetDateandWeek);
    }

    private void getLocalDate(){
        mSearchDate = "";
        mDayOfWeek = "";
        Calendar lCalendar = Calendar.getInstance();
        mSearchDate =  lCalendar.get(Calendar.YEAR)+"-"
                +((lCalendar.get(Calendar.MONTH)+1)>9?(lCalendar.get(Calendar.MONTH)+1):"0"+(lCalendar.get(Calendar.MONTH)+1))+"-"
                +(lCalendar.get(Calendar.DAY_OF_MONTH)>9 ? lCalendar.get(Calendar.DAY_OF_MONTH):"0"+lCalendar.get(Calendar.DAY_OF_MONTH));
        mDayOfWeek = RegisteredRecordManager.getWeek(mSearchDate);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_class_record, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        uiAction();
    }

    private void uiAction() {
        sdf =  new SimpleDateFormat("yyyy-MM-dd");
        findView();
        initViews();
        setOnInteractListener();

        if(mIsFirstGetDateandWeek){
            presenter.getServerTime(TeacherID);
            mIsFirstGetDateandWeek = false;
        }else {
            //要还原Fragment的时间，暂时记录不下时间信息，除非用cache
            if(RegisteredRecordManager.getSaveCacheDtoDate() != null){
                mSearchDate = RegisteredRecordManager.getSaveCacheDtoDate().getShowDate();
                mDayOfWeek = RegisteredRecordManager.getWeek(mSearchDate);
                String dateString = mSearchDate + " " + mDayOfWeek;
                mDateTv.setText(dateString);
            }

        }
    }

    //设置标题等
    private void initViews() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mNotResultTextView.setVisibility(View.GONE);
        mToolBarManager.setTitle(mClassName);
    }

    //绑定控件
    private void findView() {

        mRecyclerView = (RecyclerView) mActivity.findViewById(R.id.list_item_class_data_show_my_class);
        mDateTv = (TextView) mActivity.findViewById(R.id.tv_sign_date_my_class);
        mApproveStatusTextView = (TextView)mActivity.findViewById(R.id.tv_class_section_data_my_class_status);
        mNotResultTextView = (TextView)mActivity.findViewById(R.id.not_result_text_show_my_class);
        if (mTimePickerView == null) {
            mTimePickerView = new TimePickerView(mActivity, TimePickerView.Type.YEAR_MONTH_DAY);
            mTimePickerView.setCancelable(true);
        }
    }

    //事件处理
    private void setOnInteractListener() {
        String dateString = mSearchDate + " " + mDayOfWeek;
        mDateTv.setText(dateString);
        mDateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date lDate = null;
                SimpleDateFormat lSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    lDate = lSimpleDateFormat.parse(mSearchDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                mTimePickerView.setTime(lDate);
                mTimePickerView.show();
            }
        });

        //时间控件监听
        setTimePickerListener();

        /**
         * 修改提交监听 - SignPresenter - 100行
         */
        presenter.mCompositeSubscription.add(RxBus.getDefault().toObserverable(RxApproveStatus.class).subscribe(new Subscriber<RxApproveStatus>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(RxApproveStatus rxApproveStatus) {
                if (rxApproveStatus.getApprove()){
                    //刷新界面
                    getApproveData();
                }
            }
        }));
    }

    private void getMyClassData(){
        //TODO 去获取服务器数据
        presenter.getMyClass(TeacherID, mSearchDate, mClassID, mDayOfWeek);
    }

    private void setTimePickerListener(){
        SimpleDateFormat lSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date lDate = lSimpleDateFormat.parse(mSearchDate);
            mTimePickerView.setTime(lDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        mTimePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                mDayOfWeek = Helper_String.getDayOfWeek(date);
                DateTime lDateTime = new DateTime(date);
                mSearchDate = lDateTime.toString("yyyy-MM-dd");
                saveDate();  //缓存日期，时间选择的时候
                String dateString = mSearchDate + "  " + mDayOfWeek;
                mDateTv.setText(dateString);
                presenter.getMyClass(mUserBean.getUserId(), mSearchDate, mClassID, mDayOfWeek);
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.detachView(false);
    }

    @Override
    public boolean onBackPress() {
        RegisteredRecordManager.destroyDataCache();
        Log.e("lenita","onBackPress -- MyClassRecordFragment");
        return super.onBackPress();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(mClassEntry != null){
            outState.putSerializable(CLASS_INFO,mClassEntry);
            outState.putBoolean(TO_GET_SERVER_TIME,false);
        }
        super.onSaveInstanceState(outState);
    }
}
