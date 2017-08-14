package com.gta.zssx.mobileOA.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.ZSSXApplication;
import com.gta.zssx.mobileOA.model.bean.DutyNoticeInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lan.zheng on 2016/11/10.
 */
public class DutyNoticeAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<DutyNoticeInfo> mRemindList;
    private Listener mListener;
    private String UserId = ZSSXApplication.instance.getUser().getUserId();

    public DutyNoticeAdapter(Context context, Listener listener, List<DutyNoticeInfo> list) {
        mContext = context;
        mListener = listener;
        mRemindList = new ArrayList<>();
        mRemindList = list;
    }

    public void setData(List<DutyNoticeInfo> list){
        mRemindList.addAll(list);
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return RemindHolder.newHolder(parent, mContext);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final RemindHolder remindHolder = (RemindHolder) holder;
        DutyNoticeInfo Remind = mRemindList.get(position);
        remindHolder.DutyTimeTv.setText(Remind.getTime());
        if(Remind.getArrangeType() == 1){  //值班安排
            remindHolder.DutyNameTv.setText(mContext.getString(R.string.text_duty_type_1));
            if(Remind.getDutyOrCheckDuty() == 1){   //值班登记
                remindHolder.DutyOperation.setText(mContext.getString(R.string.text_duty_register));
            }else {
                remindHolder.DutyOperation.setText(mContext.getString(R.string.text_duty_check));
            }
        }else {   //手动调班
            remindHolder.DutyNameTv.setText(mContext.getString(R.string.text_duty_type_2));
            if(TextUtils.equals(UserId,Remind.getSwitchUserId()) && Remind.getShiftStatus() == 1 ){  //申请人且申请未被审批
                remindHolder.DutyOperation.setText(mContext.getString(R.string.text_duty_register));
            }else if(TextUtils.equals(UserId,Remind.getToSwitchUserId())){
                //被申请人，未审批进行审批，审批了进行登记
                if(Remind.getShiftStatus() == 1){
                    remindHolder.DutyOperation.setText(mContext.getString(R.string.text_shift_status_not_approve));
                }else {
                    remindHolder.DutyOperation.setText(mContext.getString(R.string.text_duty_register));
                }
            }else {
                remindHolder.DutyOperation.setText("");  //无操作
            }


        }
        //值班表名
        remindHolder.DutyDutyTableTv.setText(Remind.getDutyName());

        //值班领导
        String leaderString = getLeader(Remind.getLeaders());
        remindHolder.DutyPerson.setText(leaderString == ""?"无":leaderString);

        remindHolder.DutyLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickListener(Remind);
            }
        });
        remindHolder.DutyLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickListener(Remind);
            }
        });
    }

    private String getLeader(List<DutyNoticeInfo.LeadersEntity> list){
        String leaderStrings = "";
        String leader = "";
        for(int i = 0;i<list.size();i++){
            leader += list.get(i).getLeaderName()+"、";
        }
        leaderStrings = leader.substring(0,leader.length()-1);
        return leaderStrings;
    }


    @Override
    public int getItemCount() {
        return mRemindList.size();
    }

    private static class RemindHolder extends RecyclerView.ViewHolder {
        TextView DutyTimeTv;
        TextView DutyNameTv;
        TextView DutyDutyTableTv;
        TextView DutyOperation;
        TextView DutyPerson;
        RelativeLayout DutyLayout1;
        RelativeLayout DutyLayout2;

        public RemindHolder(Context context, View itemView) {
            super(itemView);
            DutyTimeTv = (TextView) itemView.findViewById(R.id.tv_duty_time);
            DutyNameTv = (TextView) itemView.findViewById(R.id.tv_duty_name);
            DutyDutyTableTv = (TextView) itemView.findViewById(R.id.tv_duty_table);
            DutyOperation = (TextView) itemView.findViewById(R.id.tv_duty_operation);
            DutyPerson = (TextView) itemView.findViewById(R.id.tv_duty_person_name);
            DutyLayout1 = (RelativeLayout) itemView.findViewById(R.id.layout_duty1);
            DutyLayout2 = (RelativeLayout) itemView.findViewById(R.id.layout_duty2);
        }

        private static RemindHolder newHolder(ViewGroup parent, Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.list_item_duty_notice, parent, false);
            return new RemindHolder(context, view);
        }
    }

    public interface Listener{
        void onClickListener(DutyNoticeInfo dutyNoticeInfo);
    }
}
