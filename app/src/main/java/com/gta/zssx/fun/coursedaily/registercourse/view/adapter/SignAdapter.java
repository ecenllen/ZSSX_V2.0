package com.gta.zssx.fun.coursedaily.registercourse.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gta.utils.resource.Toast;
import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.model.ClassDataManager;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.DataBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.PostSignBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SectionBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.StudentListBean;
import com.gta.zssx.pub.util.RxBus;
import com.gta.zssx.pub.widget.DisableEmojiEditText;

import java.util.List;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/22.
 * @since 1.0.0
 */
public class SignAdapter extends RecyclerView.Adapter {

    private static Context mContext;
    private Listener mListener;
    private List<SectionBean> mSectionBeanList;
    private ClassDataManager.DataCache mDataCache;
    private List<StudentListBean> mStudentList;
    public List<PostSignBean.SectionBean> mPostSectionBeen;
    public DataBean mDataBean;


    public SignAdapter(Context context, Listener listener,
                       List<SectionBean> sectionBeanList,
                       List<StudentListBean> studentList, DataBean dataBean) {
        mContext = context;
        mListener = listener;
        mSectionBeanList = sectionBeanList;
        mDataBean = dataBean;
        mDataCache = ClassDataManager.getDataCache();
//        if (mDataCache == null)
//            return;
//        mStudentList = mDataCache.getStudentList();
        mStudentList = studentList;
//        mPostSectionBeen = dataBean.getPostSectionBean();
    }

    public interface Listener {
        void itemClick();

        void postSectionBean(List<PostSignBean.SectionBean> sectionBeanList);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return SignHolder.newHolder(parent, mContext);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final SignHolder lHolder = (SignHolder) holder;
        SectionBean lSectionBean = mSectionBeanList.get(position);
//        lHolder.courseNameTv.setText(mDataBean.getCourseName());
        lHolder.classNameTv.setText(mDataBean.getTitle());
        lHolder.sectionNameTv.setText(lSectionBean.getLesson());
        int delayCount = lSectionBean.getDelayCount();
        int leaveCount = lSectionBean.getLeaveCount();
        int absentCount = lSectionBean.getAbsentCount();
        int vocationCount = lSectionBean.getVocationCount();

//        int lSectionID = lSectionBean.getSectionID();
        if (mDataBean.isMotify()) {
            lHolder.flagEt.setText(mDataBean.getMemo());
        }

//        /**
//         * 计算每节课迟到，请假，旷课，公假的人数
//         */
//        switch (lSectionID) {
//            case 1:
//                for (int i = 0; i < mStudentList.size(); i++) {
//                    StudentListBean lStudentListBean = mStudentList.get(i);
//                    int lLESSON1 = lStudentListBean.getLESSON1();
//                    if (lLESSON1 == StateBean.DELAY) {
//                        delayCount = delayCount + 1;
//                    } else if (lLESSON1 == StateBean.LEAVE) {
//                        leaveCount = leaveCount + 1;
//                    } else if (lLESSON1 == StateBean.ABSENT) {
//                        absentCount = absentCount + 1;
//                    } else if (lLESSON1 == StateBean.VOCATION) {
//                        vocationCount = vocationCount + 1;
//                    }
//                }
//                break;
//            case 2:
//                for (int i = 0; i < mStudentList.size(); i++) {
//                    StudentListBean lStudentListBean = mStudentList.get(i);
//                    int lLESSON1 = lStudentListBean.getLESSON2();
//                    if (lLESSON1 == StateBean.DELAY) {
//                        delayCount = delayCount + 1;
//                    } else if (lLESSON1 == StateBean.LEAVE) {
//                        leaveCount = leaveCount + 1;
//                    } else if (lLESSON1 == StateBean.ABSENT) {
//                        absentCount = absentCount + 1;
//                    } else if (lLESSON1 == StateBean.VOCATION) {
//                        vocationCount = vocationCount + 1;
//                    }
//                }
//                break;
//            case 3:
//                for (int i = 0; i < mStudentList.size(); i++) {
//                    StudentListBean lStudentListBean = mStudentList.get(i);
//                    int lLESSON1 = lStudentListBean.getLESSON3();
//                    if (lLESSON1 == StateBean.DELAY) {
//                        delayCount = delayCount + 1;
//                    } else if (lLESSON1 == StateBean.LEAVE) {
//                        leaveCount = leaveCount + 1;
//                    } else if (lLESSON1 == StateBean.ABSENT) {
//                        absentCount = absentCount + 1;
//                    } else if (lLESSON1 == StateBean.VOCATION) {
//                        vocationCount = vocationCount + 1;
//                    }
//                }
//                break;
//            case 4:
//                for (int i = 0; i < mStudentList.size(); i++) {
//                    StudentListBean lStudentListBean = mStudentList.get(i);
//                    int lLESSON1 = lStudentListBean.getLESSON4();
//                    if (lLESSON1 == StateBean.DELAY) {
//                        delayCount = delayCount + 1;
//                    } else if (lLESSON1 == StateBean.LEAVE) {
//                        leaveCount = leaveCount + 1;
//                    } else if (lLESSON1 == StateBean.ABSENT) {
//                        absentCount = absentCount + 1;
//                    } else if (lLESSON1 == StateBean.VOCATION) {
//                        vocationCount = vocationCount + 1;
//                    }
//                }
//                break;
//            case 5:
//                for (int i = 0; i < mStudentList.size(); i++) {
//                    StudentListBean lStudentListBean = mStudentList.get(i);
//                    int lLESSON1 = lStudentListBean.getLESSON5();
//                    if (lLESSON1 == StateBean.DELAY) {
//                        delayCount = delayCount + 1;
//                    } else if (lLESSON1 == StateBean.LEAVE) {
//                        leaveCount = leaveCount + 1;
//                    } else if (lLESSON1 == StateBean.ABSENT) {
//                        absentCount = absentCount + 1;
//                    } else if (lLESSON1 == StateBean.VOCATION) {
//                        vocationCount = vocationCount + 1;
//                    }
//                }
//                break;
//            case 6:
//                for (int i = 0; i < mStudentList.size(); i++) {
//                    StudentListBean lStudentListBean = mStudentList.get(i);
//                    int lLESSON1 = lStudentListBean.getLESSON6();
//                    if (lLESSON1 == StateBean.DELAY) {
//                        delayCount = delayCount + 1;
//                    } else if (lLESSON1 == StateBean.LEAVE) {
//                        leaveCount = leaveCount + 1;
//                    } else if (lLESSON1 == StateBean.ABSENT) {
//                        absentCount = absentCount + 1;
//                    } else if (lLESSON1 == StateBean.VOCATION) {
//                        vocationCount = vocationCount + 1;
//                    }
//                }
//                break;
//            case 7:
//                for (int i = 0; i < mStudentList.size(); i++) {
//                    StudentListBean lStudentListBean = mStudentList.get(i);
//                    int lLESSON1 = lStudentListBean.getLESSON7();
//                    if (lLESSON1 == StateBean.DELAY) {
//                        delayCount = delayCount + 1;
//                    } else if (lLESSON1 == StateBean.LEAVE) {
//                        leaveCount = leaveCount + 1;
//                    } else if (lLESSON1 == StateBean.ABSENT) {
//                        absentCount = absentCount + 1;
//                    } else if (lLESSON1 == StateBean.VOCATION) {
//                        vocationCount = vocationCount + 1;
//                    }
//                }
//                break;
//            default:
//                break;
//        }

        int lScore = lSectionBean.getScore();

        //迟到算出勤
        int lStudentCount = mStudentList.size();
        final int unusualCount = delayCount + leaveCount + absentCount + vocationCount;
        int normalCount = lStudentCount - unusualCount + delayCount;
//        int lScore = 100 - unusualCount;
//        if (lScore < 0) {
//            lScore = 0;
//        }
        lHolder.scoreEt.setText(String.valueOf(lScore));
        lHolder.classStateTv.setText(normalCount + "/" + lStudentCount);
        lHolder.delayNumTv.setText(String.valueOf(delayCount));
        lHolder.leaveNumTv.setText(String.valueOf(leaveCount));
        lHolder.AbsentNumTv.setText(String.valueOf(absentCount));
        lHolder.vocationNumTv.setText(String.valueOf(vocationCount));

        if (delayCount > 0) {
            lHolder.delayNumTv.setTextColor(mContext.getResources().getColor(R.color.main_color));
        }
        if (leaveCount > 0) {
            lHolder.leaveNumTv.setTextColor(mContext.getResources().getColor(R.color.main_color));
        }
        if (absentCount > 0) {
            lHolder.AbsentNumTv.setTextColor(mContext.getResources().getColor(R.color.main_color));
        }
        if (vocationCount > 0) {
            lHolder.vocationNumTv.setTextColor(mContext.getResources().getColor(R.color.main_color));
        }

//        PostSignBean.SectionBean lSectionBean1 = new PostSignBean.SectionBean();
//
//        lSectionBean1.setScore(lScore);
//        lSectionBean1.setSectionID(lSectionID);
//        lSectionBean1.setCourseName(mDataBean.getCourseName());
//        lSectionBean1.setRemark(mDataBean.getMemo());
//
//        mPostSectionBeen.add(lSectionBean1);
//
//        /**
//         * 最后把这个登记的课堂对象保存起来
//         */
//        if (position == mSectionBeanList.size() - 1) {
//            if (mListener != null) {
//                mListener.postSectionBean(mPostSectionBeen);
//            }
//        }


        /**
         * 把修改后的分数保存起来
         */
        final int finalLScore = lScore;
        lHolder.scoreEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
//                    mDataCache.getPostSectionBeans().get(position).setScore(Integer.parseInt(s.toString()));
                    mPostSectionBeen.get(position).setScore(Integer.parseInt(s.toString()));
                    mListener.postSectionBean(mPostSectionBeen);
                }
                RxBus.getDefault().post(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && !s.toString().equals("")) {
                    int num = Integer.parseInt(s.toString());
                    if (num > 100) {
                        Toast.Short(mContext, "分数不能超过100");
                        lHolder.scoreEt.setText(String.valueOf(String.valueOf((finalLScore))));
                    }
                }
            }
        });

        /**
         * 把remarks保存起来
         */
        lHolder.flagEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPostSectionBeen.get(position).setRemark(s.toString());
                mListener.postSectionBean(mPostSectionBeen);
//                mDataCache.getPostSectionBeans().get(position).setRemark(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /**
         * 分数点击
         */
        lHolder.scoreLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lHolder.scoreEt.setFocusable(true);
                lHolder.scoreEt.requestFocus();
                if (((Activity)mContext).getCurrentFocus() != null)
                {
                    InputMethodManager inputMethodManager = (InputMethodManager)((Activity) mContext).getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput( ((Activity) mContext).getCurrentFocus(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });
        /**
         * 备注点击
         */
        lHolder.remarkLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lHolder.flagEt.setFocusable(true);
                lHolder.flagEt.requestFocus();
                if (((Activity)mContext).getCurrentFocus() != null)
                {
                    InputMethodManager inputMethodManager = (InputMethodManager)((Activity) mContext).getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput( ((Activity) mContext).getCurrentFocus(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSectionBeanList.size();
    }

    private static class SignHolder extends RecyclerView.ViewHolder {
        TextView classNameTv;
        TextView classStateTv;
        TextView courseNameTv;
        TextView sectionNameTv;
        TextView delayNumTv;
        TextView leaveNumTv;
        TextView AbsentNumTv;
        TextView vocationNumTv;
        EditText scoreEt;
        DisableEmojiEditText flagEt;
        //TODO 分数和备注点击范围加大
        RelativeLayout scoreLayout;
        RelativeLayout remarkLayout;

        public SignHolder(Context context, View itemView) {
            super(itemView);
            classNameTv = (TextView) itemView.findViewById(R.id.class_name_tv);
            classStateTv = (TextView) itemView.findViewById(R.id.course_state_tv);
            courseNameTv = (TextView) itemView.findViewById(R.id.course_name_tv);
            sectionNameTv = (TextView) itemView.findViewById(R.id.section_name_tv);
            delayNumTv = (TextView) itemView.findViewById(R.id.delay_num_tv);
            leaveNumTv = (TextView) itemView.findViewById(R.id.leave_num_tv);
            AbsentNumTv = (TextView) itemView.findViewById(R.id.absent_num_tv);
            vocationNumTv = (TextView) itemView.findViewById(R.id.vocation_num_tv);
            scoreEt = (EditText) itemView.findViewById(R.id.score_et);

            scoreLayout = (RelativeLayout)itemView.findViewById(R.id.layout_score_input);
            remarkLayout = (RelativeLayout)itemView.findViewById(R.id.layout_remark_input);
            flagEt = (DisableEmojiEditText) itemView.findViewById(R.id.flag_et);
            flagEt.setMaxLength(30);
            flagEt.setMaxLine(5);
        }

        private static SignHolder newHolder(ViewGroup parent, Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_sign, parent, false);
            return new SignHolder(context, view);
        }
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.layout_score_input:

                    break;
                case R.id.layout_remark_input:
                    if (((Activity)mContext).getCurrentFocus() != null)
                    {
                        InputMethodManager inputMethodManager = (InputMethodManager)((Activity) mContext).getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.showSoftInput( ((Activity) mContext).getCurrentFocus(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    break;

            }
        }
    };



}
