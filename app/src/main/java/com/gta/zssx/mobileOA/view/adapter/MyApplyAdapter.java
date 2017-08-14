package com.gta.zssx.mobileOA.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gta.zssx.AppConfiguration;
import com.gta.zssx.R;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.mobileOA.model.bean.MyApplyBean;
import com.gta.zssx.mobileOA.view.page.BacklogDetailActivity;
import com.gta.zssx.pub.common.Constant;
import com.gta.zssx.pub.util.DateUtils;
import com.gta.utils.helper.GlideUtils;
import com.gta.utils.view.CircleImageView;

import java.util.List;

/**
 * Created by xiaoxia.rang on 2016/10/24.
 * 待办/已办/我的申请列表adapter
 */

public class MyApplyAdapter extends RecyclerView.Adapter<MyApplyAdapter.ViewHolder> {

    private Context context;
    private List<MyApplyBean> backlogs;
    private LayoutInflater inflater;
    private String serverTime = "";
    private UserBean mUserBean;

    public MyApplyAdapter(Context context, List<MyApplyBean> backlogs) {
        this.context = context;
        this.backlogs = backlogs;
        this.inflater = LayoutInflater.from(context);
        try {
            mUserBean = AppConfiguration.getInstance().getUserBean();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(inflater.inflate(R.layout.item_oa_backlog, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MyApplyBean backlog = backlogs.get(position);
        holder.position = position;
        GlideUtils.loadUserImage(context, mUserBean.getPhoto(), holder.ivAvatar);
        holder.tvPersonInfo.setText(mUserBean.getEchoName() == null ? "无" : mUserBean.getEchoName());
        holder.tvPosition.setText(backlog.getPosition() == null ? "无" : backlog.getPosition());
        holder.tvApplyTime.setText(backlog.getCreateTime() == null ? "无" : DateUtils.formatDateTime(serverTime, backlog.getCreateTime()));
        holder.tvTitle.setText(backlog.getSubject() == null ? "无" : backlog.getSubject());
        if (backlog.getRunId() != null) {
            holder.tvStatus.setVisibility(View.VISIBLE);
            if (backlog.getStatus().equals("1")) {
                holder.tvStatus.setText("审批中");
                holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.gray_999999));
            } else if (backlog.getStatus().equals("2")) {
                holder.tvStatus.setText("已同意");
                holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.textColor_status_agree));
            } else {
                holder.tvStatus.setText("已驳回");
                holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.textColor_status_reject));
            }
        } else {
            holder.tvStatus.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return backlogs == null ? 0 : backlogs.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RelativeLayout rlBacklog;
        CircleImageView ivAvatar;
        TextView tvPersonInfo;
        TextView tvPosition;
        TextView tvApplyTime;
        TextView tvTitle;
        TextView tvStatus;
        int position;

        public ViewHolder(View itemView) {
            super(itemView);
            rlBacklog = (RelativeLayout) itemView.findViewById(R.id.rl_backlog);
            ivAvatar = (CircleImageView) itemView.findViewById(R.id.iv_avatar);
            tvPersonInfo = (TextView) itemView.findViewById(R.id.tv_personInfo);
            tvPosition = (TextView) itemView.findViewById(R.id.tv_position);
            tvApplyTime = (TextView) itemView.findViewById(R.id.tv_applyTime);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvStatus = (TextView) itemView.findViewById(R.id.tv_status);
            rlBacklog.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, BacklogDetailActivity.class);
            intent.putExtra("runId", backlogs.get(position).getRunId());
            intent.putExtra("type", Constant.DETAIL_TYPE_MYAPPLY);
            context.startActivity(intent);
        }
    }


}
