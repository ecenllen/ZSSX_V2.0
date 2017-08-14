package com.gta.zssx.dormitory.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.dormitory.model.bean.DormitoryRankingBean;
import com.gta.zssx.pub.common.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * [Description]
 * 首页列表Item适配器
 * [How to use]
 *
 * [Tips]
 *
 * Created by lan.zheng on 2017/7/25 11:27.
 */

public class DormitoryItemListAdapter extends RecyclerView.Adapter {
    private List<DormitoryRankingBean> mDormitoryRankingBeanList;
    private Context mContext;
    private Listener mListener;
    private int mIsShowAction;
    public static final int NEED_TO_SHOW_ACTION = 0;

    public DormitoryItemListAdapter(Context context,int isShowAction, Listener listener) {
        mContext = context;
        mIsShowAction = isShowAction;
        mListener = listener;
    }

    public void refreshData(List<DormitoryRankingBean> dormitoryRankingBeanList){
        this.mDormitoryRankingBeanList = dormitoryRankingBeanList;
        notifyDataSetChanged();
    }


    public void setMoreData(List<DormitoryRankingBean> dormitoryRankingBeanList){
        mDormitoryRankingBeanList.addAll(dormitoryRankingBeanList);
        notifyDataSetChanged();
    }

    public void removeItemData(int position){
        if (mDormitoryRankingBeanList != null && mDormitoryRankingBeanList.size() - 1 >= position ) {
            mDormitoryRankingBeanList.remove(position);
            notifyItemRemoved(position) ;
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mItemView = LayoutInflater.from(mContext).inflate(R.layout.list_item_dormitory_ranking_item, parent, false);
        return new DisplayHolder(mItemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DisplayHolder displayHolder = (DisplayHolder) holder;
        DormitoryRankingBean dormitoryRankingBean = mDormitoryRankingBeanList.get(position);
        //TODO 是否显示送审和删除按钮
        if(mIsShowAction == NEED_TO_SHOW_ACTION){
            displayHolder.submitBtn.setVisibility(View.VISIBLE);
            displayHolder.deleteBtn.setVisibility(View.VISIBLE);
        }else {
            displayHolder.submitBtn.setVisibility(View.GONE);
            displayHolder.deleteBtn.setVisibility(View.GONE);
        }
        //TODO 设置值
        String itemName = dormitoryRankingBean.getItemName() != null?dormitoryRankingBean.getItemName():"null";
        displayHolder.itemNameTv.setText(itemName);
        int status = dormitoryRankingBean.getStates();
        String statusShow = "";
        if(status == Constant.TYPE_NOT_PASS){
            //不通过的要变成红色
            statusShow = "未通过";
            displayHolder.itemStatusTv.setTextColor(mContext.getResources().getColor(R.color.red_color));
        }else {
            if(status == Constant.TYPE_NOT_SUBMIT){
                statusShow = "未送审";
            }else if(status == Constant.TYPE_ON_SUBMIT){
                statusShow = "送审中";
            }else if (status == Constant.TYPE_HAVE_BEEN_PASS){
                statusShow = "已通过";
            }else {
                statusShow = "已发布";
            }
            displayHolder.itemStatusTv.setTextColor(mContext.getResources().getColor(R.color.main_color));
        }
        displayHolder.itemStatusTv.setText(statusShow);
        if(dormitoryRankingBean.getDimensionType() == Constant.DimensionType_Dormitory){
            displayHolder.dormitoryOrClassTv.setText("宿舍楼：");
        }else {
            displayHolder.dormitoryOrClassTv.setText("专业部：");
        }
        String nameShow = dormitoryRankingBean.getbuildingNames() != null?dormitoryRankingBean.getbuildingNames():"null";
        displayHolder.dormitoryNameTv.setText(nameShow);
        displayHolder.dormitoryDateTv.setText(getDate(dormitoryRankingBean.getScoreDate()));
    }

    @Override
    public int getItemCount() {
        return mDormitoryRankingBeanList == null ? 0 : mDormitoryRankingBeanList.size();
    }

/*    private String getNameShow(List<DormitoryOrClassSingleInfoBean> domitoryOrClassInfoBeanList){
        String str = "";
        if(domitoryOrClassInfoBeanList != null && domitoryOrClassInfoBeanList.size() > 0 ){
            for(int i = 0;i<domitoryOrClassInfoBeanList.size();i++){
                if(i != 0 ){
                    str += ","+ domitoryOrClassInfoBeanList.get(i).getDormitoryOrClassName();
                }else {
                    str = domitoryOrClassInfoBeanList.get(i).getDormitoryOrClassName();
                }
            }
        }
        return str;
    }*/

    private String getDate(String date){
        String str = "";
        String[] split = date.split("T");
        if(split.length >= 1){
            str = split[0];
        }
        return str;
    }

    private class DisplayHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView itemNameTv;  //指标项名称
        TextView itemStatusTv;  //状态
        TextView dormitoryOrClassTv; //宿舍楼还是专业部
        TextView dormitoryNameTv; //名称条目
        TextView dormitoryDateTv; //日期
        TextView submitBtn;
        TextView deleteBtn;
        LinearLayout linearLayout;

        public DisplayHolder(View itemView) {
            super(itemView);
            itemNameTv = (TextView)itemView.findViewById(R.id.tv_dormitory_item_name);
            itemStatusTv = (TextView)itemView.findViewById(R.id.tv_dormitory_item_status);
            dormitoryOrClassTv = (TextView)itemView.findViewById(R.id.tv_dormitory_name);
            dormitoryNameTv = (TextView)itemView.findViewById(R.id.tv_dormitory_name_show);
            dormitoryDateTv = (TextView)itemView.findViewById(R.id.tv_dormitory_date_show);
            submitBtn = (TextView) itemView.findViewById(R.id.btn_dormitory_item_submit) ;
            deleteBtn = (TextView) itemView.findViewById(R.id.btn_dormitory_item_delete);
            linearLayout = (LinearLayout) itemView.findViewById((R.id.layout_item_content));
            if(!submitBtn.hasOnClickListeners()) submitBtn.setOnClickListener(this);
            if(!deleteBtn.hasOnClickListeners()) deleteBtn.setOnClickListener(this);
            if(!linearLayout.hasOnClickListeners()) linearLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int actionType = 0;
            switch (v.getId()) {
                case R.id.btn_dormitory_item_submit:    // 送审
                    actionType = Constant.DORMITORY_ACTION_SUBMIT;
                    break;
                case R.id.btn_dormitory_item_delete:    // 删除
                    actionType = Constant.DORMITORY_ACTION_DELETE;
                    break;
                case R.id.layout_item_content:  // 看详情
                    actionType = Constant.DORMITORY_ACTION_DETAIL;
                    break;
                default:
            }
            if(mListener != null)
                mListener.itemActionClick(actionType,getLayoutPosition() - 1,mDormitoryRankingBeanList.get(getLayoutPosition() - 1));
        }
    }


    public interface Listener {
        void itemActionClick(int action,int position,DormitoryRankingBean dormitoryRankingBean);
    }

}


