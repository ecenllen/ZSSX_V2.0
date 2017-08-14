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
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.MyClassSubTabInfoDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.RegisteredRecordFromSignatureDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.RxApproveStatus;
import com.gta.zssx.fun.coursedaily.registerrecord.model.RegisteredRecordManager;
import com.gta.zssx.fun.coursedaily.registerrecord.presenter.MyClassRecordSubTabPresenter;
import com.gta.zssx.fun.coursedaily.registerrecord.view.MyClassRecordSubTabView;
import com.gta.zssx.fun.coursedaily.registerrecord.view.SelectOprationPopupWindow;
import com.gta.zssx.fun.coursedaily.registerrecord.view.adapter.MyClassRecordListAdapter;
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
 * Created by lan.zheng on 2016/7/2.
 */
public class MyClassRecordSubTabFragment extends RegisterRecordBaseFragment<MyClassRecordSubTabView, MyClassRecordSubTabPresenter> implements MyClassRecordSubTabView {

    public static final String PAGE_TAG = RegisterRecordBaseFragment.class.getSimpleName();
    //长按操作标识
    private final static int ACTION_NOT_OPR = 0;
    private final static int ACTION_MODIFY = 1;  //修改
    private final static int ACTION_DELETE = 2;  //删除
    private final static int ACTION_CANCEL = 3;  //取消
    private RegisteredRecordFromSignatureDto mRegisteredRecordFromSignatureDto;  //删除或修改的对象内容
    private int mDeleteItemPosition;  //删除的位置
    private int SectionID;  //删除的节次id
    private final static int JUST_ONE_CLASS = 0 ;
    private static String CLASS_ID = "CLASS_ID";
    private static String NOT_NETWORK = "NOT_NETWORK";
    private static String INFO_DTO = "INFO_DTO";  //传入时间日期

    public RecyclerView mRecyclerView;
    private TextView mNotResultTextView;
    public UserBean mUserBean;
    public TextView mDateTv;
    public TextView mApproveStatusTextView;   //审核状态
    public TimePickerView mTimePickerView;
    public String mSearchDate;
    public String mDayOfWeek;

    //班级名称和班级ID,以及记录集
    private int ClassIDDeleteOpr;  //删除或修改时的ID
    private String TeacherID;
    private int mClassID;
    private boolean mNotNetwork;
    private int mCount;
//    private MyClassRecordListAdapter lAdapter;
    private MyClassRecordListNewAdapter lAdapter;

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
    public MyClassRecordSubTabPresenter createPresenter() {
        return new MyClassRecordSubTabPresenter();
    }

    private void showList(MyClassRecordDto myClassRecordDto) {
        mApproveStatusTextView.setVisibility(View.VISIBLE);
        mNotResultTextView.setVisibility(View.GONE);  //有结果
        mDateTv.setVisibility(View.VISIBLE);
        String ClassName = myClassRecordDto.getClassName();
        List<RegisteredRecordFromSignatureDto> recordFromSignatureDtos = myClassRecordDto.getCourse();
        setCourse(ClassName ,recordFromSignatureDtos);
    }

    private void setCourse(String ClassName,List<RegisteredRecordFromSignatureDto> recordFromSignatureDtos){
        for(int i= 0; i < recordFromSignatureDtos.size();i++){
            recordFromSignatureDtos.get(i).setClassName(ClassName);
            recordFromSignatureDtos.get(i).setClassID(mClassID);
        }
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
                /*for(int i = 0;i < mCount ;i++){
                    AlreadyRegisteredRecordActivity.mMyClassesStatus.get(i).setStatus(AlreadyRegisteredRecordActivity.MY_CLASSES_SHOW_CACHE);
                }
                new DetailRegisteredRecordFragment.Builder(mActivity, lClassInfoDto).display();*/

            }

            @Override
            public void itemLongClick(RegisteredRecordFromSignatureDto registeredRecordFromSignatureDto, int position) {
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

        String showApproveText = haveBeenApprove?getResources().getString(R.string.text_have_been_approve):getResources().getString(R.string.text_no_approve);
        mApproveStatusTextView.setText(showApproveText);
        lAdapter = null;
        lAdapter = new MyClassRecordListNewAdapter(mActivity, lListener, recordFromSignatureDtos);
        mRecyclerView.setAdapter(lAdapter);
        getApproveData();  //有结果要查看当前的审核状态
    }

    @Override
    public void showResult(boolean isNeedToast,MyClassRecordDto myClassRecordDto) {
        if(myClassRecordDto.getCourse() == null || myClassRecordDto.getCourse().size() == 0){
            mApproveStatusTextView.setVisibility(View.INVISIBLE);
            notResult(isNeedToast,myClassRecordDto.getClassName());  //没有结果的判断是否要显示Toast
        }else {
            showList(myClassRecordDto);  //有结果的进行显示
        }

    }

    @Override
    public void notResult(boolean isNeedToast,String className) {
        if(lAdapter != null){
            lAdapter.clearUpData();
            lAdapter = null;
        }
        mApproveStatusTextView.setVisibility(View.INVISIBLE);
        mNotResultTextView.setVisibility(View.VISIBLE);  //显示没有记录的view
        if(className.equals(mActivity.getResources().getString(R.string.text_you_are_not_headmaster))){
            mNotResultTextView.setText(className);  //在页面改变文字信息并隐藏时间选择器
            mDateTv.setVisibility(View.GONE);
        }else {
            mNotResultTextView.setText(mActivity.getResources().getString(R.string.not_record_now));
            mDateTv.setVisibility(View.VISIBLE);
        }
        if(isNeedToast){
            String toastInfo = "班级 '" + className + "' " + mActivity.getResources().getString(R.string.text_not_result_select_date);
            Toast.Long(mActivity,toastInfo);
        }

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

    /**
     * 去服务器获取领导审核日数据
     */
    private void getApproveData() {
        presenter.getApproveDate(mSearchDate);  //去服务器获取领导审核日数据,和服务器时间对比，得到审核状态
    }


    public static class Builder extends RegisterRecordFragmentBuilder<MyClassRecordSubTabFragment> {

        int mClassId;
        boolean mNotNetwork;
        MyClassSubTabInfoDto mMyClassSubTabInfoDto;

        public Builder(Context context, int id,boolean notNetwork,MyClassSubTabInfoDto lMyClassSubTabInfoDto) {
            super(context);
            mClassId = id;
            mNotNetwork = notNetwork;
            mMyClassSubTabInfoDto = lMyClassSubTabInfoDto;
        }

        @Override
        public MyClassRecordSubTabFragment create() {
            Bundle lBundle = new Bundle();
            lBundle.putInt(CLASS_ID, mClassId);
            lBundle.putBoolean(NOT_NETWORK,mNotNetwork);
            lBundle.putSerializable(INFO_DTO,mMyClassSubTabInfoDto);
            MyClassRecordSubTabFragment lMyClassRecordSubTabFragment = new MyClassRecordSubTabFragment();
            lMyClassRecordSubTabFragment.setArguments(lBundle);
            return lMyClassRecordSubTabFragment;
        }

        @Override
        public String getPageTag() {
            return PAGE_TAG;
        }
    }

    public void updateClassInfo(){
        LogUtil.Log("lenita","updateClassInfo()");
        presenter.getMyClass(TeacherID, mSearchDate, mClassID, mDayOfWeek,false,true,mActivity);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        dataAction();
    }

    //fragment间参数传递
    private void dataAction() {
        getLocalDate();
        try {
            mUserBean = AppConfiguration.getInstance().getUserBean();

        } catch (Exception e) {
            e.printStackTrace();
        }
        mCount = JUST_ONE_CLASS;
        mCount = mUserBean.getTotalCount();
        TeacherID = mUserBean.getUserId();
        mClassID = JUST_ONE_CLASS;
        mClassID = getArguments().getInt(CLASS_ID);
        mNotNetwork = getArguments().getBoolean(NOT_NETWORK);  //用于判断是否要显示未连接网络的toast,true不用显示，因为前一个页面已经获取了
        MyClassSubTabInfoDto lMyClassSubTabInfoDto1 = (MyClassSubTabInfoDto) getArguments().getSerializable(INFO_DTO);
        LogUtil.Log("lenita","mClassID = "+ mClassID);
        MyClassSubTabInfoDto lMyClassSubTabInfoDto2 = RegisteredRecordManager.getMyClassRecordDataCache().getMyClassSubTabInfoDto();
        if(lMyClassSubTabInfoDto2 != null && lMyClassSubTabInfoDto2.getIsSubTab()){
            mSearchDate = lMyClassSubTabInfoDto2.getDate();
            mDayOfWeek = lMyClassSubTabInfoDto2.getWeek();
        }else {
            //缓存失效，读取getAguments里的时间
            mSearchDate = lMyClassSubTabInfoDto1.getDate();
            mDayOfWeek = lMyClassSubTabInfoDto1.getWeek();
        }
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
        View lInflate =inflater.inflate(R.layout.fragment_my_class_record, container, false);
        uiAction(lInflate);
        return lInflate;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        setOnInteractListener();
    }

    private void uiAction(View view) {
        findView(view);
        initViews();
    }


    //事件处理
    private void setOnInteractListener() {
        //设置title，要在生成mToolBarManager之后才能set
        mToolBarManager.setTitle("我的班级");
        //进来默认获取从父Fragment得到的时间来显示
        mDateTv.setText(mSearchDate + " " + mDayOfWeek);
        mDateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date lDate = null;
                try {
                    lDate = sdf.parse(mSearchDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                mTimePickerView.setTime(lDate);
                mTimePickerView.show();
            }
        });

        //时间控件监听
        setTimePickerListener();
        //用于第一次进入且没有网络的情况，直接不需要去获取服务器
        if(mNotNetwork){
            mNotNetwork = false;
            mNotResultTextView.setVisibility(View.VISIBLE);

            return;
        }
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
        presenter.getMyClass(TeacherID, mSearchDate, mClassID, mDayOfWeek,false,false,mActivity);

        //改变对应ID的状态，为了不相互影响数据(暂时不需要了)
       /* for(int i = 0; i < mCount ; i++){
            if(mClassID == AlreadyRegisteredRecordActivity.mMyClassesStatus.get(i).getClassID()){
                if(AlreadyRegisteredRecordActivity.mMyClassesStatus.get(i).getStatus() == AlreadyRegisteredRecordActivity.MY_CLASSES_SHOW_CACHE){
                    showCache();
                    //显示完缓存后这个值要改变回成这个状态
                    AlreadyRegisteredRecordActivity.mMyClassesStatus.get(i).setStatus(AlreadyRegisteredRecordActivity.MY_CLASSES_FIRST_GET_DATA_DONE);
                }else {
                    presenter.getMyClass(TeacherID, mSearchDate, mClassID, mDayOfWeek, mActivity);
                    LogUtil.Log("lenita","presenter.getMyClass(TeacherID, mSearchDate, mClassID, mDayOfWeek, mActivity)");
                }
            }
        }*/

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
                mDateTv.setText(mSearchDate + "  " + mDayOfWeek);
                presenter.getMyClass(mUserBean.getUserId(), mSearchDate, mClassID, mDayOfWeek,true,true,mActivity);
            }
        });
    }

    private void showCache(){
        MyClassRecordDto lMyClassRecordDto = RegisteredRecordManager.getMyClassRecordDataCache().getMyClassRecordSingleDto(mClassID);
        if(lMyClassRecordDto != null){
            mSearchDate = lMyClassRecordDto.getDate();
            mDayOfWeek = lMyClassRecordDto.getWeek();
            mDateTv.setText(mSearchDate + " " + mDayOfWeek);
            showResult(false,lMyClassRecordDto);
        }
    }




    //设置View
    private void initViews() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mNotResultTextView.setVisibility(View.GONE);

    }

    //绑定控件
    private void findView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list_item_class_data_show_my_class);
        mDateTv = (TextView) view.findViewById(R.id.tv_sign_date_my_class);
        mApproveStatusTextView = (TextView)view.findViewById(R.id.tv_class_section_data_my_class_status);
        mNotResultTextView = (TextView)view.findViewById(R.id.not_result_text_show_my_class);
        if (mTimePickerView == null) {
            mTimePickerView = new TimePickerView(mActivity, TimePickerView.Type.YEAR_MONTH_DAY);
            mTimePickerView.setCancelable(true);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.detachView(false);
    }

    @Override
    public boolean onBackPress() {
        RegisteredRecordManager.getMyClassRecordDataCache().setMyClassSubTabInfoDto(null);
        Log.e("lenita","onBackPress -- MyClassRecordSubTabFragment");
        return super.onBackPress();
    }
}