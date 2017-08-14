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

import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.RxStudentList;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SectionBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.StateBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.StudentListNewBean;
import com.gta.zssx.pub.util.RxBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lan.zheng on 2017/3/1.
 *
 */
public class StudentSearchAdapter extends RecyclerView.Adapter {
//    private String mKeyWord;
//    private onCheckListener mListener;
    private SectionBean mSectionBean;
    private Context mContext;
    private List<StudentListNewBean> mStudentList;  //所有学生,当搜索的有改变，这个也要找到对应项改变
    List<StudentListNewBean> mStudentSearchList;    //搜索结果学生
    List<Integer> mIntegerList;
    RxStudentList mRxStudentList;
    private Listener mListener;
    private int mPosition;

    /**
     * 有关键字的时候
     * @param sectionBean 节次
     * @param studentListBeen  第一次显示所有学生的bean
     * @param context context
     */
    public StudentSearchAdapter(SectionBean sectionBean, List<StudentListNewBean> studentListBeen, Context context,int mPosition,Listener listener) {
        mSectionBean = sectionBean;
        mStudentList = studentListBeen;
        mContext = context;
        mIntegerList = new ArrayList<>();
        mRxStudentList = new RxStudentList();
        mStudentSearchList = studentListBeen;
        mListener = listener;
        this.mPosition = mPosition;
    }

    public interface Listener {
        void itemClick(StudentListNewBean studentListNewBean);
    }

    public void setSearchData(List<StudentListNewBean> studentListBeen){
        mStudentSearchList = new ArrayList<>();
        mStudentSearchList = studentListBeen;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return InnerHolder.newHolder(parent, mContext);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        InnerHolder lInnerHolder = (InnerHolder) holder;
        StudentListNewBean studentListNewBean = mStudentSearchList.get(position);
        String name = studentListNewBean.getStundentName();
        String changeStudentId = studentListNewBean.getStudentID();
        lInnerHolder.mTextView.setText(name);
        //点击姓名
        lInnerHolder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.itemClick(mStudentSearchList.get(position));
            }
        });
        lInnerHolder.normalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStudentSearchList.get(position).setMarkType(StateBean.NORMAL);
                lInnerHolder.normalButton.setChecked(true);
                //改变完单个学生记录后也更新所有学生列表
                for(int i = 0;i<mStudentList.size();i++){
                    if(mStudentList.get(i).getStudentID().equals(changeStudentId)){
                        mStudentList.get(i).setMarkType(StateBean.NORMAL);
                    }
                }
                sendRxBus();
            }
        });

        lInnerHolder.leavaeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStudentSearchList.get(position).setMarkType(StateBean.LEAVE);
                lInnerHolder.leavaeButton.setChecked(true);
                //改变完单个学生记录后也更新所有学生列表
                for(int i = 0;i<mStudentList.size();i++){
                    if(mStudentList.get(i).getStudentID().equals(changeStudentId)){
                        mStudentList.get(i).setMarkType(StateBean.LEAVE);
                    }
                }
                sendRxBus();

            }
        });

        lInnerHolder.delayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStudentSearchList.get(position).setMarkType(StateBean.DELAY);
                lInnerHolder.delayButton.setChecked(true);
                //改变完单个学生记录后也更新所有学生列表
                for(int i = 0;i<mStudentList.size();i++){
                    if(mStudentList.get(i).getStudentID().equals(changeStudentId)){
                        mStudentList.get(i).setMarkType(StateBean.DELAY);
                    }
                }
                sendRxBus();
            }
        });

        lInnerHolder.absentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStudentSearchList.get(position).setMarkType(StateBean.ABSENT);
                lInnerHolder.absentButton.setChecked(true);
                //改变完单个学生记录后也更新所有学生列表
                for(int i = 0;i<mStudentList.size();i++){
                    if(mStudentList.get(i).getStudentID().equals(changeStudentId)){
                        mStudentList.get(i).setMarkType(StateBean.ABSENT);
                    }
                }
                sendRxBus();
            }
        });

        lInnerHolder.vocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStudentSearchList.get(position).setMarkType(StateBean.VOCATION);
                lInnerHolder.vocationButton.setChecked(true);
                //改变完单个学生记录后也更新所有学生列表
                for(int i = 0;i<mStudentList.size();i++){
                    if(mStudentList.get(i).getStudentID().equals(changeStudentId)){
                        mStudentList.get(i).setMarkType(StateBean.VOCATION);
                    }
                }
                sendRxBus();
            }
        });
        setRadioButtonDefaultState(lInnerHolder,position);  //设置状态
    }

    private void sendRxBus() {
        /**
         * CourseSignWithAttendanceStatusActivity  --564行
         * StudentListinnerFragment   --300行
         * StudentSearchActivity  --216行
         */
        mRxStudentList.setmOrderPosition(mPosition);  //这里用位置标识0,1,2
        mRxStudentList.setStudentListBeen(mStudentList);
        RxBus.getDefault().post(mRxStudentList);   //StudentSearchActivity改变记录
    }

    private void setRadioButtonDefaultState(InnerHolder innerHolder,int position) {
        //注意这里改变的是搜索列表
        switch (mStudentSearchList.get(position).getMarkType()) {
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
        return mStudentSearchList.size();  //搜索列表
    }

    private static class InnerHolder extends RecyclerView.ViewHolder {
        LinearLayout itemLayout;
        TextView mTextView;
        RadioButton normalButton;
        RadioButton delayButton;
        RadioButton leavaeButton;
        RadioButton absentButton;
        RadioButton vocationButton;
        RadioGroup mRadioGroup;

        public InnerHolder(View itemView) {
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

        private static InnerHolder newHolder(ViewGroup parent, Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_student_list_inner, parent, false);
            return new InnerHolder(view);
        }
    }
}
