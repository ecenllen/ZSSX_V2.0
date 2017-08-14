package com.gta.zssx.dormitory.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.dormitory.model.bean.DormitoryOrClassSingleInfoBean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * [Description]
 * 新增录入 - 专业部或宿舍楼选择
 * [How to use]
 *
 * [Tips]
 *
 * Created by lan.zheng on 2017/7/25 11:32.
 */

public class SelectLevelListApdater extends RecyclerView.Adapter {
    private Context mContext;
    private List<DormitoryOrClassSingleInfoBean> mDomitoryOrClassInfoBeanList = new ArrayList<>();  //所有的条目
    private List<DormitoryOrClassSingleInfoBean> mRecordList = new ArrayList<>();  //用于记录选中的条目并返回的数据
    private Set<Integer> checkList ;  //用于记录当前勾选中的条目,每次进入都根据这个判断
    private String mKeyWord = "";
    private boolean isSearch = false;
    private Listener mlistener;

    public SelectLevelListApdater(Context context, List<DormitoryOrClassSingleInfoBean> domitoryOrClassInfoBeanList, Set<Integer> checkSet, Listener listener){
        mContext = context;
        mDomitoryOrClassInfoBeanList.clear();
        mDomitoryOrClassInfoBeanList.addAll(domitoryOrClassInfoBeanList);
        checkList = new HashSet<>();
        checkList.clear();
        checkList.addAll(checkSet);
        setRecordList();  //先拿到选中的集合
        isSearch = false;
        mlistener = listener;
    }

    public void setDomitoryOrClassInfoBeanList(List<DormitoryOrClassSingleInfoBean> domitoryOrClassInfoBeanList){
        mDomitoryOrClassInfoBeanList.clear();
        mDomitoryOrClassInfoBeanList.addAll(domitoryOrClassInfoBeanList);
    }

    public void setNewCheckListAndRefresh(Set<Integer> checkSet){
        checkList = new HashSet<>();
        checkList = checkSet;
        setRecordList();  //先拿到选中的集合
    }


    private void setRecordList(){
        mRecordList.clear();
        for(int i = 0;i< mDomitoryOrClassInfoBeanList.size();i++){
            List<Integer> list = new ArrayList<>(checkList);
            for(int j = 0;j<list.size();j++){
                if(list.get(j) == i){
                    DormitoryOrClassSingleInfoBean domitoryOrClassInfoBean = mDomitoryOrClassInfoBeanList.get(i);
                    mRecordList.add(domitoryOrClassInfoBean);
                }
            }
        }
    }

    public void enterKeyWord(String keyWord){
        if(TextUtils.isEmpty(keyWord)){
            isSearch = false;
        }else {
            mKeyWord = keyWord;
            isSearch = true;
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mItemView = LayoutInflater.from(mContext).inflate(R.layout.list_item_select_level_item, parent, false);
        return new DisplayHolder(mItemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DisplayHolder displayHolder = (DisplayHolder)holder;
        DormitoryOrClassSingleInfoBean domitoryOrClassInfoBean = mDomitoryOrClassInfoBeanList.get(position);

        //防止recycleview复用带来的checkbox选中状态错乱
        displayHolder.mCheckBox.setTag(new Integer(position));

        //显示条目名称和checkBox状态
        displayHolder.nameTv.setText(domitoryOrClassInfoBean.getDormitoryOrClassName());
        if(checkList.contains(position)){  //包含说明要勾选，不包含说明不用勾选
            displayHolder.mCheckBox.setChecked(true);
        }else {
            displayHolder.mCheckBox.setChecked(false);
        }

        //TODO  点击单条条目,测试是否行得通
        displayHolder.selectItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean lChecked = displayHolder.mCheckBox.isChecked();
                displayHolder.mCheckBox.setChecked(!lChecked);
                if (displayHolder.mCheckBox.isChecked()) {
                    if (!checkList.contains(displayHolder.mCheckBox.getTag())) {
                        checkList.add(new Integer(position));
                    }
                    mRecordList.add(domitoryOrClassInfoBean);
                    mlistener.getRecordList(mRecordList,checkList);
                } else {
                    if (checkList.contains(displayHolder.mCheckBox.getTag())) {
                        checkList.remove(new Integer(position));
                    }
                    mRecordList.remove(domitoryOrClassInfoBean);
                    mlistener.getRecordList(mRecordList,checkList);
                }
            }
        });

        //是否显示条目
        if(isSearch){
            if(domitoryOrClassInfoBean.getDormitoryOrClassName().contains(mKeyWord)){
                displayHolder.selectItemLayout.setVisibility(View.VISIBLE);
            }else {
                displayHolder.selectItemLayout.setVisibility(View.GONE);
            }
        }else {
            displayHolder.selectItemLayout.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return mDomitoryOrClassInfoBeanList == null ? 0 : mDomitoryOrClassInfoBeanList.size();
    }

    private class DisplayHolder extends RecyclerView.ViewHolder {
        TextView nameTv;
        CheckBox mCheckBox;
        RelativeLayout selectItemLayout;

        public DisplayHolder(View itemView) {
            super(itemView);
            nameTv = (TextView)itemView.findViewById(R.id.tv_level_name_show);
            mCheckBox = (CheckBox)itemView.findViewById(R.id.checkbox);
            selectItemLayout = (RelativeLayout)itemView.findViewById(R.id.layout_select_item);
        }
    }


    public interface Listener{
        void getRecordList(List<DormitoryOrClassSingleInfoBean> recordList, Set<Integer> checkList);
    }


}
