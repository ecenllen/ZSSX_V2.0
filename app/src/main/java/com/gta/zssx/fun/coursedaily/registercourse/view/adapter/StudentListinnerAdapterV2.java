package com.gta.zssx.fun.coursedaily.registercourse.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.RxStudentList;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SectionBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SectionStudentListBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.StateBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.StudentListNewBean;
import com.gta.zssx.pub.util.RxBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lan.zheng on 2017/3/24.
 */
public class StudentListinnerAdapterV2 extends RecyclerView.Adapter{
    SectionBean mSectionBean;
    private Context mContext;
    private Listener mListener;
    List<Integer> mIntegerList;
    RxStudentList mRxStudentList;
    private int mPosition = 0;
    private List<SectionStudentListBean> mSectionStudentListBeanList = new ArrayList<>();
    private boolean isCanClick = true;  //默认可以更改
//    private List<StudentListNewBean> mStudentList = new ArrayList<>();  //废弃
//    List<StudentListBean> mStudentListBeen;

    /*public StudentListinnerAdapterV2(SectionBean sectionBean, List<StudentListNewBean> studentListBeen, Context context,int position, Listener listener) {
        mSectionBean = sectionBean;
        mStudentList = studentListBeen;
//        mStudentList = ClassDataManager.getDataCache().getStudentList();
        mContext = context;
        mListener = listener;
        mIntegerList = new ArrayList<>();
        mRxStudentList = new RxStudentList();
        mPosition = position;
    }*/

    /**
     * 是否能点击条目，记得Adapter更改的时候这个值一定要设置
     * @param isCanClick
     */
    public void setIsCanClick(boolean isCanClick){
        this.isCanClick = isCanClick;
    }

    /**
     * 数据结构改变，这里也做相应的调整
     */
    public StudentListinnerAdapterV2(Context context,SectionBean sectionBean, List<SectionStudentListBean> sectionStudentListBeanList, int position, Listener listener) {
        mSectionBean = sectionBean;
        mSectionStudentListBeanList = sectionStudentListBeanList;
        mContext = context;
        mListener = listener;
        mIntegerList = new ArrayList<>();
        mRxStudentList = new RxStudentList();
        mPosition = position;
    }

    public interface Listener {
        void itemClick(StudentListNewBean studentListNewBean);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_student_list_inner, parent, false);
        RecyclerView.ViewHolder holder = new InnerHolder(mContext, view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        InnerHolder lInnerHolder = (InnerHolder) holder;
        List<StudentListNewBean> mStudentList = mSectionStudentListBeanList.get(mPosition).getStudentListNewBeen();
        String name = mStudentList.get(position).getStundentName();
//        lInnerHolder.mRadioGroup.setTag(position);
        lInnerHolder.mTextView.setText(name);
        lInnerHolder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.itemClick(mStudentList.get(position));
            }
        });
        lInnerHolder.normalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                changeState(lSectionID, lStudentListBean, StateBean.NORMAL);
                if(isCanClick) {  //能点击
                    mSectionStudentListBeanList.get(mPosition).getStudentListNewBeen().get(position).setMarkType(StateBean.NORMAL);
                    lInnerHolder.normalButton.setChecked(true);
                    sendRxBus();
                }else {
                    ToastUtils.showLongToast(mContext.getString(R.string.text_same_with_pre_can_not_change));
                    if(mSectionStudentListBeanList.get(mPosition).getStudentListNewBeen().get(position).getMarkType() != StateBean.NORMAL){
                        setRadioButtonDefaultState(lInnerHolder,position,mStudentList);
                    }
                }
            }
        });

        lInnerHolder.leavaeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                changeState(lSectionID, lStudentListBean, StateBean.LEAVE);
                if(isCanClick){  //能点击
                    mSectionStudentListBeanList.get(mPosition).getStudentListNewBeen().get(position).setMarkType(StateBean.LEAVE);
                    lInnerHolder.leavaeButton.setChecked(true);
                    sendRxBus();
                }else {    //不能点击
                    ToastUtils.showLongToast(mContext.getString(R.string.text_same_with_pre_can_not_change));
                    if(mSectionStudentListBeanList.get(mPosition).getStudentListNewBeen().get(position).getMarkType() != StateBean.LEAVE){
                        setRadioButtonDefaultState(lInnerHolder,position,mStudentList);
                    }
                }

            }
        });

        lInnerHolder.delayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                changeState(lSectionID, lStudentListBean, StateBean.DELAY);
                if(isCanClick) {
                    mSectionStudentListBeanList.get(mPosition).getStudentListNewBeen().get(position).setMarkType(StateBean.DELAY);
                    lInnerHolder.delayButton.setChecked(true);
                    sendRxBus();
                }else {
                    ToastUtils.showLongToast(mContext.getString(R.string.text_same_with_pre_can_not_change));
                    if(mSectionStudentListBeanList.get(mPosition).getStudentListNewBeen().get(position).getMarkType() != StateBean.DELAY){
                        setRadioButtonDefaultState(lInnerHolder,position,mStudentList);
                    }
                }

            }
        });

        lInnerHolder.absentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                changeState(lSectionID, lStudentListBean, StateBean.ABSENT);
                if(isCanClick){
                    mSectionStudentListBeanList.get(mPosition).getStudentListNewBeen().get(position).setMarkType(StateBean.ABSENT);
                    lInnerHolder.absentButton.setChecked(true);
                    sendRxBus();
                }else {
                    ToastUtils.showLongToast(mContext.getString(R.string.text_same_with_pre_can_not_change));
                    if(mSectionStudentListBeanList.get(mPosition).getStudentListNewBeen().get(position).getMarkType() != StateBean.ABSENT){
                        setRadioButtonDefaultState(lInnerHolder,position,mStudentList);
                    }
                }

            }
        });

        lInnerHolder.vocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                changeState(lSectionID, lStudentListBean, StateBean.VOCATION);
                if(isCanClick){
                    mSectionStudentListBeanList.get(mPosition).getStudentListNewBeen().get(position).setMarkType(StateBean.VOCATION);
                    lInnerHolder.vocationButton.setChecked(true);
                    sendRxBus();
                }else {
                    ToastUtils.showLongToast(mContext.getString(R.string.text_same_with_pre_can_not_change));
                    if(mSectionStudentListBeanList.get(mPosition).getStudentListNewBeen().get(position).getMarkType() != StateBean.VOCATION){
                        setRadioButtonDefaultState(lInnerHolder,position,mStudentList);
                    }
                }

            }
        });
        setRadioButtonDefaultState(lInnerHolder,position,mStudentList);  //设置状态
    }

    private void sendRxBus() {
        mRxStudentList.setmOrderPosition(mPosition);  //这里用位置标识
        mRxStudentList.setStudentListBeen(mSectionStudentListBeanList.get(mPosition).getStudentListNewBeen());
        /**
         *CourseSignWithAttendanceStatusActivity  --564行
         *StudentListinnerFragment   --300行
         */
        RxBus.getDefault().post(mRxStudentList);   //通知ActivityBean记录改变,Fragment也要修改
    }

   /* public  Observable rxStudentListObservable;
    public void testFun(){
        String[] strings = {"test1","test2"};
        RxBus.getInstance().send(Events.OTHER, null);
    }*/


    private void setRadioButtonDefaultState(InnerHolder innerHolder,int position,List<StudentListNewBean> mStudentList) {
        switch (mStudentList.get(position).getMarkType()) {

            case StateBean.NORMAL:
                innerHolder.normalButton.setChecked(true);
                break;
            case StateBean.DELAY:
                innerHolder.delayButton.setChecked(true);
                break;
            case StateBean.LEAVE:
                innerHolder.leavaeButton.setChecked(true);
                break;
            case StateBean.ABSENT:
                innerHolder.absentButton.setChecked(true);
                break;
            case StateBean.VOCATION:
                innerHolder.vocationButton.setChecked(true);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mSectionStudentListBeanList.get(mPosition).getStudentListNewBeen().size();
    }

    private class InnerHolder extends RecyclerView.ViewHolder {
        LinearLayout itemLayout;
        TextView mTextView;
        RadioButton normalButton;
        RadioButton delayButton;
        RadioButton leavaeButton;
        RadioButton absentButton;
        RadioButton vocationButton;
        RadioGroup mRadioGroup;

        public InnerHolder(Context context, View itemView) {
            super(itemView);
            itemLayout = (LinearLayout)itemView.findViewById(R.id.layout_student_status_item);
            mTextView = (TextView) itemView.findViewById(R.id.student_name_tv);
            normalButton = (RadioButton) itemView.findViewById(R.id.normal_radio_button);
            delayButton = (RadioButton) itemView.findViewById(R.id.delay_radio_button);
            leavaeButton = (RadioButton) itemView.findViewById(R.id.leave_radio_button);
            absentButton = (RadioButton) itemView.findViewById(R.id.absent_radio_button);
            vocationButton = (RadioButton) itemView.findViewById(R.id.vocation_radio_button);
            mRadioGroup = (RadioGroup) itemView.findViewById(R.id.student_list_radio_group);
        }
    }

/*    private void changeState(int sectionID, StudentListNewBean studentListBean, int state) {
        switch (sectionID) {
            case SectionBean.SECTION_1:
                studentListBean.setLESSON1(state);
                break;
            case SectionBean.SECTION_2:
                studentListBean.setLESSON2(state);
                break;
            case SectionBean.SECTION_3:
                studentListBean.setLESSON3(state);
                break;
            case SectionBean.SECTION_4:
                studentListBean.setLESSON4(state);
                break;
            case SectionBean.SECTION_5:
                studentListBean.setLESSON5(state);
                break;
            case SectionBean.SECTION_6:
                studentListBean.setLESSON6(state);
                break;
            case SectionBean.SECTION_7:
                studentListBean.setLESSON7(state);
                break;
            default:
                break;
        }
    }*/

}
