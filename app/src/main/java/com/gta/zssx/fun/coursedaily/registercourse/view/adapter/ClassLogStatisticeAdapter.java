package com.gta.zssx.fun.coursedaily.registercourse.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gta.zssx.AppConfiguration;
import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.LogListBean;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.ClassInfoDto;
import com.gta.zssx.fun.coursedaily.registerrecord.view.page.AlreadyRegisteredRecordActivity;
import com.gta.zssx.fun.coursedaily.registerrecord.view.page.AlreadyRegisteredRecordFromSignatureFragment;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;

import java.util.List;

/**
 * Created by xiao.peng on 2016/11/16.
 *
 */
public class ClassLogStatisticeAdapter extends RecyclerView.Adapter<ClassLogStatisticeAdapter.ClassListHolder> {

    private Context mContext;
    private List<LogListBean> mData;
    private LayoutInflater inflater;
    private String mDate;

    public ClassLogStatisticeAdapter(Context context, List<LogListBean> listBean, String date) {
        this.mContext = context;
        this.mData = listBean;
        this.mDate = date;
        inflater = LayoutInflater.from(context);
    }

    public void setNotifyDatas(List<LogListBean> listBean, String date) {
        this.mData = listBean;
        this.mDate = date;
        notifyDataSetChanged();
    }

    @Override
    public ClassListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ClassListHolder(inflater.inflate(R.layout.item_log_list, parent, false));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onBindViewHolder(ClassListHolder holder, int position) {
        LogListBean bean = mData.get(position);
        if (position == mData.size() - 1)
            holder.emptyHint.setVisibility(View.VISIBLE);
        else
            holder.emptyHint.setVisibility(View.GONE);
        holder.tvNmae.setText(bean.getcName());
        setLogListText(holder.tvLate, bean.getLate(), bean.isMaxLate());
        setLogListText(holder.tvVacate, bean.getVacate(), bean.isMaxVacate());
        setLogListText(holder.tvTruant, bean.getTruant(), bean.isMaxTruant());
        setLogListText(holder.tvSabbaticals, bean.getSabbaticals(), bean.isMaxSabbaticals());
        holder.llContent.setOnClickListener(view -> {
            //跳转到详细点的页面
            ClassInfoDto lClassInfoDto = new ClassInfoDto();
            lClassInfoDto.setSignDate(mDate);
            lClassInfoDto.setClassName(mData.get(position).getcName());
            lClassInfoDto.setClassID(mData.get(position).getcId());
            lClassInfoDto.setLogStatisticeInto(true);
            lClassInfoDto.setIsFromClassLogMainpage(true);
            UserBean userBean = null;
            try {
                userBean = AppConfiguration.getInstance().getUserBean();
                lClassInfoDto.setTeacherID(userBean.getUserId());
            } catch (Exception e) {
                e.printStackTrace();
            }
            //获取真实的数据传递入下一个Activity
            lClassInfoDto.setIsFromClassLogMainpage(true);
            //改成跳转到Activity
            AlreadyRegisteredRecordActivity.start(mContext, AlreadyRegisteredRecordFromSignatureFragment.PAGE_TAG, lClassInfoDto);
        });
    }

    private void setLogListText(TextView tv, String text, boolean isMax) {
        tv.setText(text);
        tv.setTextColor("0".equals(text) ? Color.parseColor("#999999") :
                (isMax ? Color.parseColor("#fc3e39") : Color.parseColor("#666666")));
    }


    /**
     * 列表
     */
    protected class ClassListHolder extends RecyclerView.ViewHolder {

        private TextView tvNmae;

        private TextView tvLate;

        private TextView tvVacate;

        private TextView tvTruant;

        private TextView tvSabbaticals;

        private LinearLayout llContent;

        private View emptyHint;

        public ClassListHolder(View itemView) {
            super(itemView);
            llContent = (LinearLayout) itemView.findViewById(R.id.ll_content);
            //名称
            tvNmae = (TextView) itemView.findViewById(R.id.tv_name);
            //迟到
            tvLate = (TextView) itemView.findViewById(R.id.tv_late);
            //请假
            tvVacate = (TextView) itemView.findViewById(R.id.tv_vacate);
            //旷课
            tvTruant = (TextView) itemView.findViewById(R.id.tv_truant);
            //公假
            tvSabbaticals = (TextView) itemView.findViewById(R.id.tv_sabbaticals);
            //空距离
            emptyHint = itemView.findViewById(R.id.emptyHint);
        }
    }
}
