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
 * [Description]
 * <p/>
 * [How to use]
 * <p/>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/24.
 * @since 1.0.0
 */
public class StudentListInnerAdapter extends RecyclerView.Adapter {

    private String mKeyWord;
    SectionBean mSectionBean;
    private Context mContext;
    private Listener mListener;
    private List<StudentListNewBean> mStudentList;
    List<Integer> mIntegerList;
    RxStudentList mRxStudentList;
//    List<StudentListBean> mStudentListBeen;

    /**
     * 没有关键字的时候显示所有的学生
     * @param sectionBean
     * @param studentListBeen
     * @param context
     * @param listener
     */
    public StudentListInnerAdapter(SectionBean sectionBean, List<StudentListNewBean> studentListBeen, Context context, Listener listener) {
        mSectionBean = sectionBean;
        mStudentList = studentListBeen;
//        mStudentList = ClassDataManager.getDataCache().getStudentList();
        mContext = context;
        mListener = listener;
        mIntegerList = new ArrayList<>();
        mRxStudentList = new RxStudentList();
        mKeyWord = "";
    }

    /**
     * 所有学生暂时不使用该方法，显示部分才需要
     * @param keyWord
     */
    public void setKeyword(String keyWord){
        mKeyWord = keyWord;
    }

    public interface Listener {
        void itemClick();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return InnerHolder.newHolder(parent, mContext);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final InnerHolder lInnerHolder = (InnerHolder) holder;
        final StudentListNewBean lStudentListBean = mStudentList.get(position);
        lInnerHolder.mTextView.setText(lStudentListBean.getStundentName());
//        lInnerHolder.normalButton.setTag(new Integer(position));
//        lInnerHolder.delayButton.setTag(new Integer(position));
//        lInnerHolder.leavaeButton.setTag(new Integer(position));
//        lInnerHolder.absentButton.setTag(new Integer(position));
//        lInnerHolder.vocationButton.setTag(new Integer(position));
        final int lSectionID = mSectionBean.getSectionId();
       /* switch (lSectionID) {
            case SectionBean.SECTION_1:
                int lLESSON1 = lStudentListBean.getLESSON1();
                if (lLESSON1 == StateBean.UNSIGN) {
                    lStudentListBean.setLESSON1(StateBean.NORMAL);
                }
                setRadioButtonDefaultState(lInnerHolder, lLESSON1, position);
                break;
            case SectionBean.SECTION_2:
                int lLESSON2 = lStudentListBean.getLESSON2();
                if (lLESSON2 == StateBean.UNSIGN) {
                    lStudentListBean.setLESSON2(StateBean.NORMAL);
                }
                setRadioButtonDefaultState(lInnerHolder, lStudentListBean.getLESSON2(), position);
                break;
            case SectionBean.SECTION_3:
                int lLESSON3 = lStudentListBean.getLESSON3();
                if (lLESSON3 == StateBean.UNSIGN) {
                    lStudentListBean.setLESSON3(StateBean.NORMAL);
                }
                setRadioButtonDefaultState(lInnerHolder, lStudentListBean.getLESSON3(), position);
                break;
            case SectionBean.SECTION_4:
                int lLESSON4 = lStudentListBean.getLESSON4();
                if (lLESSON4 == StateBean.UNSIGN) {
                    lStudentListBean.setLESSON4(StateBean.NORMAL);
                }
                setRadioButtonDefaultState(lInnerHolder, lStudentListBean.getLESSON4(), position);
                break;
            case SectionBean.SECTION_5:
                int lLESSON5 = lStudentListBean.getLESSON5();
                if (lLESSON5 == StateBean.UNSIGN) {
                    lStudentListBean.setLESSON5(StateBean.NORMAL);
                }
                setRadioButtonDefaultState(lInnerHolder, lStudentListBean.getLESSON5(), position);
                break;
            case SectionBean.SECTION_6:
                int lLESSON6 = lStudentListBean.getLESSON6();
                if (lLESSON6 == StateBean.UNSIGN) {
                    lStudentListBean.setLESSON6(StateBean.NORMAL);
                }
                setRadioButtonDefaultState(lInnerHolder, lStudentListBean.getLESSON6(), position);
                break;
            case SectionBean.SECTION_7:
                int lLESSON7 = lStudentListBean.getLESSON7();
                if (lLESSON7 == StateBean.UNSIGN) {
                    lStudentListBean.setLESSON7(StateBean.NORMAL);
                }
                setRadioButtonDefaultState(lInnerHolder, lStudentListBean.getLESSON7(), position);
                break;
            default:
                break;

        }*/

//        lInnerHolder.mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                switch (checkedId) {
//                    case R.id.normal_radio_button:
//                        changeState(lSectionID, lStudentListBean, StateBean.NORMAL);
//                        break;
//                    case R.id.delay_radio_button:
//                        changeState(lSectionID, lStudentListBean, StateBean.DELAY);
//                        break;
//                    case R.id.leave_radio_button:
//                        changeState(lSectionID, lStudentListBean, StateBean.LEAVE);
//                        break;
//                    case R.id.absent_radio_button:
//                        changeState(lSectionID, lStudentListBean, StateBean.ABSENT);
//                        break;
//                    case R.id.vocation_radio_button:
//                        changeState(lSectionID, lStudentListBean, StateBean.VOCATION);
//                        break;
//                    default:
//                        break;
//                }
//            }
//        });

        lInnerHolder.normalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeState(lSectionID, lStudentListBean, StateBean.NORMAL);
                sendRxBus();
            }
        });

        lInnerHolder.leavaeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeState(lSectionID, lStudentListBean, StateBean.LEAVE);
                sendRxBus();
            }
        });

        lInnerHolder.delayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeState(lSectionID, lStudentListBean, StateBean.DELAY);
                sendRxBus();
            }
        });

        lInnerHolder.absentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeState(lSectionID, lStudentListBean, StateBean.ABSENT);
                sendRxBus();
            }
        });

        lInnerHolder.vocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeState(lSectionID, lStudentListBean, StateBean.VOCATION);
                sendRxBus();
            }
        });

        /**
         * 搜索判断
         */
       /* if(!TextUtils.isEmpty(mKeyWord)){
            String studentNameLowerCase = exChange(lStudentListBean.getName());
            String pinyinLowerCase = exChange(lStudentListBean.getNameOfPinYin());
            if(studentNameLowerCase.contains(mKeyWord) || pinyinLowerCase.contains(mKeyWord)){
                lInnerHolder.itemLayout.setVisibility(View.VISIBLE);
            }else {
                //搜索的时候把不包含关键字的条目隐藏起来
                lInnerHolder.itemLayout.setVisibility(View.GONE);
            }
        }else {
            lInnerHolder.itemLayout.setVisibility(View.VISIBLE);
        }*/
    }

    private void sendRxBus() {
        mRxStudentList.setStudentListBeen(mStudentList);
        RxBus.getDefault().post(mRxStudentList);   //通知ActivityBean记录改变
    }

    private void changeState(int sectionID, StudentListNewBean studentListBean, int state) {
        /*switch (sectionID) {
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
        }*/


    }

    private void setRadioButtonDefaultState(InnerHolder innerHolder, int lessonId, int position) {
        switch (lessonId) {
            default:
            case StateBean.NORMAL:
                mIntegerList.add(new Integer(position));
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
        }
    }

    //大写转小写
    public static String exChange(String str){
        StringBuffer sb = new StringBuffer();
        if(str!=null){
            for(int i=0;i<str.length();i++){
                char c = str.charAt(i);
                if(Character.isUpperCase(c)){
                    sb.append(Character.toLowerCase(c));
                }else{
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    @Override
    public int getItemCount() {
        return mStudentList.size();
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

        private static InnerHolder newHolder(ViewGroup parent, Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_student_list_inner, parent, false);
            return new InnerHolder(context, view);
        }
    }
}
