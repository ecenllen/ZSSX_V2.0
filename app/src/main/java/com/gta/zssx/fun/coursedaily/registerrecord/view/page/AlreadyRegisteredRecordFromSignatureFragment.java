package com.gta.zssx.fun.coursedaily.registerrecord.view.page;

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
import com.gta.utils.resource.Toast;
import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.view.pageV2.CourseDetailActivity;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.ClassInfoDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.DeleteRecordDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.RegisteredRecordFromSignatureDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.RxApproveStatus;
import com.gta.zssx.fun.coursedaily.registerrecord.model.RegisteredRecordManager;
import com.gta.zssx.fun.coursedaily.registerrecord.presenter.AlreadyRegisterRecordFromSignaturePresenter;
import com.gta.zssx.fun.coursedaily.registerrecord.presenter.AlreadyRegisterRecordPresenter;
import com.gta.zssx.fun.coursedaily.registerrecord.view.AlreadyRegisteredRecordFromSignatureView;
import com.gta.zssx.fun.coursedaily.registerrecord.view.SelectOprationPopupWindow;
import com.gta.zssx.fun.coursedaily.registerrecord.view.adapter.RegisterRecordFromSignatureListAdapter;
import com.gta.zssx.fun.coursedaily.registerrecord.view.base.RegisterRecordBaseFragment;
import com.gta.zssx.fun.coursedaily.registerrecord.view.base.RegisterRecordFragmentBuilder;
import com.gta.zssx.pub.util.RxBus;
import com.gta.zssx.pub.util.LogUtil;
import com.gta.zssx.pub.util.StringUtils;
import com.gta.zssx.pub.widget.ConfirmOrCancelDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import rx.Subscriber;

/**
 * Created by lan.zheng on 2016/6/21.
 *
 */
public class AlreadyRegisteredRecordFromSignatureFragment extends RegisterRecordBaseFragment<AlreadyRegisteredRecordFromSignatureView, AlreadyRegisterRecordFromSignaturePresenter>
        implements AlreadyRegisteredRecordFromSignatureView, View.OnClickListener {
    private final static int ACTION_NOT_OPR = 0;
    private final static int ACTION_MODIFY = 1;
    private final static int ACTION_DELETE = 2;
    private final static int ACTION_CANCEL = 3;
    private final static String FROM_SIGN_PAGE_CLASS_ID = "0";  //从签名确认过来直接获取全天数据，从"已登记"进入获取当天对应的classId的数据

    public static final String PAGE_TAG = AlreadyRegisteredRecordFromSignatureFragment.class.getSimpleName();
    public static String CLASS_INFO_DTO = "CLASS_INFO_DTO";
    private ClassInfoDto mClassInfoDto;

    private TextView mSignDateTextView;
    private TextView mSectionTextView;
    private TextView mApproveStatusTextView;
    private TextView mNotResultTextView;
    private RecyclerView mRecyclerView;
    private RegisterRecordFromSignatureListAdapter lAdapter;

    private int SectionID;  //登记的真实节次Id，非1,2,3,4
    private String ClassName;
    private String TeacherID;
    private String ClassID;  //注意，classId为0的时候表示获取的数据和班级无关，只和登记老师和日期有关
    private String SignDate;
    private int ClassIDDeleteOpr;  //删除或修改时的ID
    private boolean IsLogStatistice;  //是否日志统计进入
    private String signWeekDay;//选择时间日期的星期几
    //从何处进入
    private boolean IsFromClassLogMainOrStatistics;  //首页进入和统计页进入要用真实的ClassId，签名确认不需要
    //是否是第一次获取领导审核日期，第一次失败显示空页面，第二次失败可以不做任何修改
    private boolean isFirstTimeGetApproveDate = true;
    //是否是获取领导审核日失败而长按的
//    private boolean isGetApproveDateFailed = false;
    //登记日期和领导审核日对比结果
    private boolean haveBeenApprove = false;
    //是否是长按，长按的话要重新去获取领导审核日，并改变状态
    private boolean isLongClick = false;
    //是否是弹框的操作
    private boolean showSelectOprationWindows = false;
    //是否是确认删除操作
    private boolean isSureDeleteClick = false;

    @Override
    public void showResultList(List<RegisteredRecordFromSignatureDto> registeredRecordFromSignatureDtos) {
//        String section = "共 " + registeredRecordFromSignatureDtos.size() + " 课时";
        int sectionNum = presenter.getSectionNum(registeredRecordFromSignatureDtos);
        String section = "" + sectionNum;
        mNotResultTextView.setVisibility(View.GONE);
        mDtoList = registeredRecordFromSignatureDtos;
        LogUtil.Log("lenita", "section = " + section+", ClassName = "+registeredRecordFromSignatureDtos.get(0).getClassName());
        updateSectionInfo(section, registeredRecordFromSignatureDtos);
    }

    @Override
    public void notResult() {
        mNotResultTextView.setVisibility(View.VISIBLE);
        mSectionTextView.setText("0");
        if (lAdapter != null) {
            lAdapter.clearUpData();
            lAdapter = null;
        }
    }

    //是否有记录被删除
    private boolean isHaveItemDelete = false;
    AlreadyRegisterRecordPresenter mAlreadyRegisterRecordPresenter = new AlreadyRegisterRecordPresenter(getActivity());

    @Override
    public void deleteResult() {
        //更新本界面的信息，删除成功
//        Toast.makeText(mActivity, mActivity.getResources().getString(R.string.text_delete_success), Toast.LENGTH_SHORT).show();
        Toast.Short(mActivity, mActivity.getResources().getString(R.string.text_delete_success));
        isLongClick = false;
        lAdapter.removeData(mDeleteItemPosition);  //移除某条数据动画，移除后还是去获取一次领导审核日
        isHaveItemDelete = true;  //有删除的动作，返回时需要刷新界面
        //TODO 删除了一条信息，记得更新课时数
        mDtoList.remove(mDeleteItemPosition);
        String section = "" + presenter.getSectionNum(mDtoList);
        mSectionTextView.setText(section);
//        getApproveData();  //删除前已经进行了判断，这里不需要
    }

    @Override
    public void deleteFailed() {
        isLongClick = false;
        getApproveData();
    }


    @Override
    public void showApproveResult(boolean isApprove) {
//        ApproveDate = approveBean.getDate();
//        setApproveStatus();   //置领导审核日的对比
        isFirstTimeGetApproveDate = false;  //获取一次成功后，永远设置false
        haveBeenApprove = isApprove;  //置领导审核日的结果
        String showApproveText = "，" + (haveBeenApprove?getResources().getString(R.string.text_have_been_approve):getResources().getString(R.string.text_no_approve));
        mApproveStatusTextView.setText(showApproveText);
        LogUtil.Log("lenita", "haveBeenApprove = " + isApprove);
        //TODO 新添加操作，在点击删除“确认”后
        if(isSureDeleteClick){
            isSureDeleteClick = false; //删不删都重置
            isLongClick = false;  //删不删都重置
            if(haveBeenApprove){
                //TODO 弹出提示无法删除，并刷新界面把状态变成已审核
                ToastUtils.showLongToast(mActivity.getResources().getString(R.string.text_can_not_delete));
                lAdapter.changeStatus(haveBeenApprove);
            }else {
                deleteItem();
            }
            //当是确认删除，无论什么情况都不往下执行
            return;
        }
        if (isLongClick) {
            if (haveBeenApprove) {
                //当长按并且当前数据已经是审核后
                lAdapter.changeStatus(haveBeenApprove);
            }
            if (showSelectOprationWindows) {
                //长按弹框
                popupSelectOpration();
            }
            showSelectOprationWindows = false;  //弹框后重置为默认状态
            oprClickAction();
        } else {
            //显示选中天的条目信息
            refreshListData();
        }
    }

    private void deleteItem(){
        if(lDtoList != null){
            presenter.deleteSignInfo(lDtoList);
            lDtoList = null;
        }
    }

    private void oprClickAction() {
        if (clickAction == ACTION_NOT_OPR) {
            LogUtil.Log("lenita", "oprClickAction() ACTION_NOT_OPR");
            return;
        }
        switch (clickAction) {
            case ACTION_MODIFY:
                if (haveBeenApprove) {
//                    Toast.makeText(mActivity, mActivity.getResources().getString(R.string.text_can_not_modify), Toast.LENGTH_SHORT).show();
                    Toast.Long(mActivity, mActivity.getResources().getString(R.string.text_can_not_modify));
                } else {
                    //跳转到修改页面
                    mRegisteredRecordFromSignatureDto.setSignDate(SignDate);
                    Log.e("lenita", "Already...Fragment SignDate = "+SignDate+",classId = "+mRegisteredRecordFromSignatureDto.getClassID());
                    Log.e("lenita", "Already...Fragment teacher size = "+mRegisteredRecordFromSignatureDto.getMultipleTeachersList().size()+",course size = "+mRegisteredRecordFromSignatureDto.getMultipleCoursesList().size());
//                    RegisterCourseActivity.start4Result(mActivity, RegisterDetailFragment.TAG, registeredRecordFromSignatureDto, true);
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


    //    private boolean isShowApproveDateStatus = true;//审核状态是否显示，默认显示，暂时没用到此标识
    @Override
    public void getApproveFailed(boolean showToast) {
        if (isFirstTimeGetApproveDate) {  //第一次失败才显示空页面
            if (lAdapter != null) {
                lAdapter.clearUpData();
                lAdapter = null;
            }
            mNotResultTextView.setVisibility(View.VISIBLE);
            isFirstTimeGetApproveDate = false;
        }

//        isGetApproveDateFailed = true;
        //所有值还原然后弹出提示，haveBeenApprove维持原来的值，除非是在审核日成功获取后作出赋值
        isLongClick = false;
        mSelectOprationPopupWindow = null;
        clickAction = ACTION_NOT_OPR;
        if (showToast)
            Toast.Long(mActivity, mActivity.getResources().getString(R.string.text_get_approve_failed));
//        isShowApproveDateStatus = false;  //审核状态是否显示，暂时不用
//        refreshListData();//获取领导日失败也要显示列表，暂时不用，如有需要显示，后面优化
    }

    private RegisteredRecordFromSignatureDto mRegisteredRecordFromSignatureDto;
    private List<RegisteredRecordFromSignatureDto> mDtoList;
    private int mDeleteItemPosition;

    private void updateSectionInfo(String section, List<RegisteredRecordFromSignatureDto> lDtos) {
        mSectionTextView.setText(section);
        RegisterRecordFromSignatureListAdapter.Listener lListener = new RegisterRecordFromSignatureListAdapter.Listener() {
            @Override
            public void itemClick(RegisteredRecordFromSignatureDto registeredRecordFromSignatureDto) {
                //跳转到最详细的页面
                LogUtil.Log("lenita", "传数据并跳转-->详细信息显示页面,班级名称为：" + registeredRecordFromSignatureDto.getClassName());
                //记下本页面的时间数据，跳Activity不用
//                SaveCacheDto lSaveCacheDto = new SaveCacheDto();
//                lSaveCacheDto.setSignDate(SignDate);
//                lSaveCacheDto.setSaveRecordFromSignature(true);
//                RegisteredRecordManager.setSaveCacheDtoFromSignature(lSaveCacheDto);
                //获取真实的数据传递入下一个fragment-->详细信息显示页面DetailRegisteredRecordFragment
                String ClassID = "" + registeredRecordFromSignatureDto.getClassID();
                String ClassName = registeredRecordFromSignatureDto.getClassName();
                int SectionID = registeredRecordFromSignatureDto.getSection().getSectionOriginalId();
                ClassInfoDto lClassInfoDto = new ClassInfoDto();
                lClassInfoDto.setClassName(ClassName);
                lClassInfoDto.setClassID(ClassID);
                lClassInfoDto.setTeacherID("0");  //传0代表是老师，这里默认都显示详情页面出来
                lClassInfoDto.setSignDate(SignDate);
                lClassInfoDto.setSectionID(SectionID);
                DetailRegisteredRecordActivity.start(mActivity, lClassInfoDto);
                if (RegisteredRecordManager.getSaveCacheDtoFromSignature() != null)
                    RegisteredRecordManager.setSaveCacheDtoFromSignature(null);  //清除缓存
            }

            @Override
            public void itemLongClick(RegisteredRecordFromSignatureDto registeredRecordFromSignatureDto, int position) {
                if (IsLogStatistice)
                    return;
                SectionID = registeredRecordFromSignatureDto.getSection().getSectionOriginalId();

                if (haveBeenApprove) {
                    return;
                }
                //如果是没有审核的，长按显示删除，修改，取消的dialog(都是可以操作的，因为都是当天该老师登记的信息)
                isLongClick = true;
                showSelectOprationWindows = true;
                mRegisteredRecordFromSignatureDto = registeredRecordFromSignatureDto;
                mDeleteItemPosition = position;
                LogUtil.Log("lenita", "itemLongClickDialog SectionID = " + SectionID + ",pos = " + mDeleteItemPosition);
                getApproveData();  //弹框前次获取领导审核日的对比
            }
        };
        String showApproveText = "，" + (haveBeenApprove?getResources().getString(R.string.text_have_been_approve):getResources().getString(R.string.text_no_approve));
        mApproveStatusTextView.setText(showApproveText);
        lAdapter = null;   //置空后再重新new,资源回收
        //重新去改变view的显示,签名确认进入需要显示上课班级，其他地方进入的不需要
        lAdapter = new RegisterRecordFromSignatureListAdapter(mActivity, lListener, lDtos, haveBeenApprove, !IsFromClassLogMainOrStatistics);
        mRecyclerView.setAdapter(lAdapter);
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
                        /*if (haveBeenApprove) {
                            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.text_can_not_modify), Toast.LENGTH_SHORT).show();
                        } else {
                            LogUtil.Log("lenita", "new StudentListFragment.Builder(mActivity,ClassName).display()");
                            //跳转到修改页面
                            registeredRecordFromSignatureDto.setSignDate(SignDate);
//                            RegisterCourseActivity.start4Result(mActivity, RegisterDetailFragment.TAG, registeredRecordFromSignatureDto, true);
                            CourseDetailActivity.start4Result(mActivity, registeredRecordFromSignatureDto.getClassName(), true, registeredRecordFromSignatureDto, -1);
                        }
                        isLongClick = false;
                        mSelectOprationPopupWindow = null;  //资源回收*/
                        break;
                    case R.id.tv_delete:
                        clickAction = ACTION_DELETE;
                        getApproveData();
                        /*if (haveBeenApprove) {
                            isLongClick = false;
                            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.text_can_not_delete), Toast.LENGTH_SHORT).show();
                        } else {
                            LogUtil.Log("lenita", "弹出对话框，dormitoryItemNameDialog()");
                            //弹出对话框，询问是否真的要删除
                            dormitoryItemNameDialog(mActivity.getResources().getString(R.string.text_sure_delete_ask));
                        }
                        mSelectOprationPopupWindow = null;  //资源回收*/
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
                lDeleteRecordDto.setSignDate(SignDate);
                lDtoList = new ArrayList<>();
                lDtoList.add(lDeleteRecordDto);
                //最终确认删除前再次去判断领导审核日
                isSureDeleteClick = true;
                getApproveData();
                // 删除本条记录
//                    presenter.deleteSignInfo(lDtoList);
//                    lDtoList = null;
            }

            @Override
            public void onDismissListener() {
//                isLongClick = false;  //返回要置回值false
            }
        });
        modifyDialog.setCanceledOnTouchOutside(false);  //外围点击不消失
        modifyDialog.show();
      /*  if (modifyDialog == null) {
            View contentView = LayoutInflater.from(mActivity).inflate(R.layout.dialog_delete_comfirn, null);
            modifyContent = (TextView) contentView.findViewById(R.id.dialog_content_text);
            modifyContent.setText(content);
            View btnOK = contentView.findViewById(R.id.dialog_btn_confirm);
            View btnBack = contentView.findViewById(R.id.dialog_btn_cancel);
            btnOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    modifyDialog.dismiss();
                    SectionID = mRegisteredRecordFromSignatureDto.getSection().getSectionOriginalId();
                    ClassIDDeleteOpr = mRegisteredRecordFromSignatureDto.getClassID();
                    LogUtil.Log("lenita", "删除本条记录 SectionID = " + SectionID + " , ClassIDDeleteOpr = " + ClassIDDeleteOpr);
                    DeleteRecordDto lDeleteRecordDto = new DeleteRecordDto();
                    lDeleteRecordDto.setClassID(ClassIDDeleteOpr);
                    lDeleteRecordDto.setSectionID(SectionID);
                    lDeleteRecordDto.setSignDate(SignDate);
                    lDtoList = new ArrayList<>();
                    lDtoList.add(lDeleteRecordDto);
                    //最终确认删除前再次去判断领导审核日
                    isSureDeleteClick = true;
                    getApproveData();
                    // 删除本条记录
//                    presenter.deleteSignInfo(lDtoList);
//                    lDtoList = null;
                }
            });
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    modifyDialog.dismiss();
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
                    modifyDialog = null;
                }
            });
            modifyDialog.setContentView(contentView);
        }
        modifyDialog.show();*/
    }


    /**
     * 根据ApproveDate和SignDate的比较结果得到haveBeenApprove
     */
    /*private void setApproveStatus() {
        SimpleDateFormat lSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date signTime = lSimpleDateFormat.parse(SignDate);
            Date approveTime = lSimpleDateFormat.parse(ApproveDate);
            if (signTime.getTime() > approveTime.getTime()) {
                //在审核日期之后
                haveBeenApprove = false;
            } else {
                haveBeenApprove = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        LogUtil.Log("lenita", "haveBeenApprove = " + haveBeenApprove);
    }
*/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sign_date:
                LogUtil.Log("lenita", "can change date(AlreadyRegisteredRecordFromSignatureFragment)");
                isLongClick = false;
                selectSignTime();
                break;
            default:
                break;
        }
    }

    private void selectSignTime() {
        TimePickerView mSignTimePickerView = new TimePickerView(mActivity, TimePickerView.Type.YEAR_MONTH_DAY);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
        Date lDate = null;
        try {
            lDate = sdf.parse(SignDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mSignTimePickerView.setTime(lDate);
        mSignTimePickerView.setCyclic(true);
        mSignTimePickerView.setCancelable(true);
        mSignTimePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
                SignDate = sdf.format(date);
                long signTimeInMillis = 0;
                //获取星期几
                try {
                    signTimeInMillis = sdf.parse(SignDate).getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar lCalendar = Calendar.getInstance();
                lCalendar.setTimeInMillis(signTimeInMillis);
                int weekDay = lCalendar.get(Calendar.DAY_OF_WEEK);
                signWeekDay = StringUtils.changeWeekDaytoString(weekDay);
                LogUtil.Log("lenita", "选择的时间是：" + SignDate + signWeekDay);
                updateDateView();
                getApproveData();
//                setApproveStatus();
//                refreshListData();
            }
        });
        mSignTimePickerView.show();
    }

    private void updateDateView() {
        String date = SignDate + " " + signWeekDay;
        mSignDateTextView.setText(date);
    }

    private void refreshListData() {
        if (!IsFromClassLogMainOrStatistics)  //当从“签名确认”进入的时候，不需要classId
            ClassID = FROM_SIGN_PAGE_CLASS_ID;
        Log.e("lenita","refreshListData ClassID = "+ClassID);
        presenter.getRegisteredRecordFromSignatureData(TeacherID, SignDate, ClassID, IsLogStatistice);  // 接口的连通性
    }

    /*public static class Singleton{

        private static Context sContext;
        private static ClassInfoDto sClassInfoDto;
        public Singleton(Context context, ClassInfoDto classInfoDto){
            sContext = context;
            sClassInfoDto = classInfoDto;
        }

        public Singleton(){
        }*/

    public static class Builder extends RegisterRecordFragmentBuilder<AlreadyRegisteredRecordFromSignatureFragment> {

//            private static Singleton INSTANCE = new Singleton();

//            private static Builder INSTANCE = new Builder(sContext,sClassInfoDto);

        ClassInfoDto mClassInfoDto;

        public Builder(Context context, ClassInfoDto classInfoDto) {
            super(context);
            mClassInfoDto = classInfoDto;
        }

        @Override
        public AlreadyRegisteredRecordFromSignatureFragment create() {
            Bundle lBundle = new Bundle();
            lBundle.putSerializable(CLASS_INFO_DTO, mClassInfoDto);
            AlreadyRegisteredRecordFromSignatureFragment lAlreadyRegisteredRecordFromSignatureFragment = new AlreadyRegisteredRecordFromSignatureFragment();
            lAlreadyRegisteredRecordFromSignatureFragment.setArguments(lBundle);
            return lAlreadyRegisteredRecordFromSignatureFragment;
        }

        @Override
        public String getPageTag() {
            return PAGE_TAG;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        IsFromClassLogMainOrStatistics = true; // 默认需要真实的ClassId
        mClassInfoDto = (ClassInfoDto) getArguments().getSerializable(CLASS_INFO_DTO);
        if (mClassInfoDto != null) {
            ClassName = mClassInfoDto.getClassName();
            TeacherID = mClassInfoDto.getTeacherID();
            ClassID = mClassInfoDto.getClassID();
            SignDate = mClassInfoDto.getSignDate();
            IsLogStatistice = mClassInfoDto.isLogStatisticeInto();
            IsFromClassLogMainOrStatistics = mClassInfoDto.getIsFromClassLogMainpage();
            LogUtil.Log("lenita", "Signature name = " + ClassName+",ClassID = "+ClassID);
        } else {
            //避免空指针异常
            Calendar lCalendar = Calendar.getInstance();
            String month = String.valueOf((lCalendar.get(Calendar.MONTH) + 1) <= 9 ? ("0" + (lCalendar.get(Calendar.MONTH) + 1)) : (lCalendar.get(Calendar.MONTH) + 1));
            String day = String.valueOf((lCalendar.get(Calendar.DAY_OF_MONTH) + 1) <= 9 ? ("0" + (lCalendar.get(Calendar.DAY_OF_MONTH) + 1)) : (lCalendar.get(Calendar.DAY_OF_MONTH) + 1));
            SignDate = lCalendar.get(Calendar.YEAR) + "-" + month + "-" + day;
        }
        LogUtil.Log("lenita", "Signature SignDate 是否打印看上句 = " + SignDate);
        ClassIDDeleteOpr = 0;  //默认为0
        getTimeMillisandWeek(SignDate);  //获取当前日期是周几
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtil.Log("lenita", "onCreateView");
        return inflater.inflate(R.layout.fragment_already_registered_record_from_signature_confirmation, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        uiAction();
    }

    private void uiAction() {
        //先判断是否有时间的缓存，如果有，证明是从detail或是别的页面回来的--这段暂时不用判断了，因为下一个页面做成了Activity
        /*if (RegisteredRecordManager.getSaveCacheDtoFromSignature() != null && RegisteredRecordManager.getSaveCacheDtoFromSignature().getSaveRecordFromSignature()) {
            SignDate = RegisteredRecordManager.getSaveCacheDtoFromSignature().getSignDate();
            RegisteredRecordManager.setSaveCacheDtoFromSignature(null);
        }*/

        initView();
        setOnInteractListener();
    }

    private void setOnInteractListener() {
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
                    //TODO 刷新界面
                    getApproveData();
                }
            }
        }));

    }

    /**
     * 去服务器获取领导审核日数据
     */
    private void getApproveData() {
        presenter.getApproveDate(SignDate);  //去服务器获取领导审核日数据
    }


    private void getTimeMillisandWeek(String date) {
        SimpleDateFormat lSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        long signTimeInMillis = 0;
        //获取星期几
        try {
            signTimeInMillis = lSimpleDateFormat.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            LogUtil.Log("lenita", "e = " + e.toString());
        }
        Calendar lCalendar = Calendar.getInstance();
        lCalendar.setTimeInMillis(signTimeInMillis);
        int weekDay = lCalendar.get(Calendar.DAY_OF_WEEK);
        signWeekDay = StringUtils.changeWeekDaytoString(weekDay);
    }


    private void initView() {
        mSignDateTextView = (TextView) mActivity.findViewById(R.id.tv_sign_date);
        mSectionTextView = (TextView) mActivity.findViewById(R.id.tv_class_section_data);
        mApproveStatusTextView = (TextView)mActivity.findViewById(R.id.tv_approve_status);
        mNotResultTextView = (TextView) mActivity.findViewById(R.id.not_result_text_show);
        mRecyclerView = (RecyclerView) mActivity.findViewById(R.id.list_item_class_data_show);

        if (IsFromClassLogMainOrStatistics) {
            mSignDateTextView.setOnClickListener(this);
        } else {
            //从签名确认过来的，隐藏时间选择的小箭头,并且不用监听,显示的title是“课堂日志”
            mSignDateTextView.setCompoundDrawables(null, null, null, null);
            ClassName = getResources().getString(R.string.text_class_log);
        }

        String shortClassName = ClassName;
        if(ClassName.length() > 11){
            shortClassName = ClassName.substring(0,9)+"...";
        }
        mToolBarManager.setTitle(shortClassName);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mNotResultTextView.setVisibility(View.GONE);

        //更新数据内容
        updateDateView();
        //获取领导审核日
        getApproveData();
    }

    @NonNull
    @Override
    public AlreadyRegisterRecordFromSignaturePresenter createPresenter() {
        return new AlreadyRegisterRecordFromSignaturePresenter();
    }

    @Override
    public void setRetainInstance(boolean retainingInstance) {

    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.detachView(false);
    }

    @Override
    public boolean onBackPress() {
        //把缓存清除
        if (RegisteredRecordManager.getSaveCacheDtoFromSignature() != null)
            RegisteredRecordManager.setSaveCacheDtoFromSignature(null);
        //判断是否要刷新界面
        if (isHaveItemDelete) {
            RxBus.getDefault().post(mAlreadyRegisterRecordPresenter);
        }
        return super.onBackPress();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mClassInfoDto != null) {
            outState.putSerializable(CLASS_INFO_DTO, mClassInfoDto);
        }
        super.onSaveInstanceState(outState);   //保存实例
    }
}
