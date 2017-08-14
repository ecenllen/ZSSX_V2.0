package com.gta.zssx.mobileOA.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.mobileOA.model.bean.Meeting;
import com.gta.zssx.mobileOA.view.page.BacklogDetailActivity;
import com.gta.zssx.pub.common.Constant;
import com.gta.zssx.pub.util.DateUtils;

import java.util.List;

/**
 * Created by xiaoxia.rang on 2016/10/24.
 * 会议列表adapter
 */
public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.ViewHolder> {

    private Context context;
    private List<Meeting> meetings;
    private LayoutInflater inflater;
    private String serverTime;

    public MeetingAdapter(Context context, List<Meeting> meetings) {
        this.context = context;
        this.meetings = meetings;
        this.inflater = LayoutInflater.from(context);
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(inflater.inflate(R.layout.item_oa_meeting, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Meeting meeting = meetings.get(position);
        holder.position = position;
        holder.tvTopic.setText(meeting.getTopic() == null ? "" : meeting.getTopic());
        holder.tvCompleteTime.setText(meeting.getCompleteTime() == null ? "" : DateUtils.formatDateTime(serverTime, meeting.getCompleteTime()));
        if (meeting.getStartTime() != null && meeting.getEndTime() != null &&
                !TextUtils.isEmpty(meeting.getStartTime()) && !TextUtils.isEmpty(meeting.getEndTime())) {
            holder.tvMeetingTime.setText(getMeetingTime(meeting.getStartTime(), meeting.getEndTime()));
        }
        holder.tvCreator.setText(meeting.getCreator() == null ? "" : meeting.getCreator());
        holder.tvMeetingRoom.setText(meeting.getMeetRoom() == null ? "" : meeting.getMeetRoom());
        if (meeting.getStatus().equals("0")) {
            holder.ivStatus.setVisibility(View.VISIBLE);
        } else {
            holder.ivStatus.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return meetings == null ? 0 : meetings.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView rlTask;
        ImageView ivStatus;
        TextView tvTopic;
        TextView tvCompleteTime;
        TextView tvMeetingTime;
        TextView tvMeetingRoom;
        TextView tvCreator;
        TextView tvDetail;
        int position;

        public ViewHolder(View itemView) {
            super(itemView);
            rlTask = (CardView) itemView.findViewById(R.id.rl_meeting);
            ivStatus = (ImageView) itemView.findViewById(R.id.iv_status);
            tvTopic = (TextView) itemView.findViewById(R.id.tv_topic);
            tvCompleteTime = (TextView) itemView.findViewById(R.id.tv_completeTime);
            tvMeetingTime = (TextView) itemView.findViewById(R.id.tv_meetingTime);
            tvMeetingRoom = (TextView) itemView.findViewById(R.id.tv_meetingRoom);
            tvCreator = (TextView) itemView.findViewById(R.id.tv_creator);
            tvDetail = (TextView) itemView.findViewById(R.id.tv_detail);
            tvDetail.setOnClickListener(this);
            rlTask.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, BacklogDetailActivity.class);
            intent.putExtra("id", meetings.get(position).getMeetingId());
            intent.putExtra("runId", meetings.get(position).getRunId());
            intent.putExtra("type", Constant.DETAIL_TYPE_MEETING);
            intent.putExtra("status", Integer.parseInt(meetings.get(position).getStatus()));
            intent.putExtra("position",position);
            context.startActivity(intent);
        }
    }

    public void refreshData(List<Meeting> meetingList) {
        meetings.clear();
        meetings.addAll(meetingList);
        notifyDataSetChanged();
    }

    public void appendData(List<Meeting> appendList) {
        meetings.addAll(appendList);
        notifyItemRangeChanged(meetings.size(), appendList.size());
    }

    public String getMeetingTime(String startTime, String endTime) {
        String[] startDate = startTime.split(" ");
        String[] endDate = endTime.split(" ");
        return startDate[0] + " " + startDate[1].substring(0, 5) + "-" + endDate[1].substring(0, 5);
    }
}
