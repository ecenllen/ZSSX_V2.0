package com.gta.zssx.dormitory.view.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.dormitory.model.bean.DormitoryOrClassLevelList;
import com.gta.zssx.pub.common.Constant;
import com.gta.zssx.pub.util.DensityUtil;

import java.util.List;

import ca.barrenechea.widget.recyclerview.decoration.StickyHeaderAdapter;


/**
 * [Description]
 * <p> 宿舍列表/班级列表外部适配器
 * [How to use]
 * <p>
 * [Tips]
 * @author Create by Weiye.Chen on 2017/7/21.
 * @since 2.0.0
 */
public class DormitoryListOrClassListAdapter extends RecyclerView.Adapter<DormitoryListOrClassListAdapter.ViewHolder> implements
        StickyHeaderAdapter<DormitoryListOrClassListAdapter.HeaderHolder> {

    private LayoutInflater mInflater;
    private Context mContext;
    private List<DormitoryOrClassLevelList> list;
    private DormitoryListOrClassListChildAdapter.OnItemClickListener listener;
    private int DimensionType;
    /** 1 增分， 2 扣分*/
    public int isAdditionOrSubtraction;

    /**
     * 浏览下标
     */
    private int browseIndex = -1;

    public DormitoryListOrClassListAdapter(Context context, DormitoryListOrClassListChildAdapter.OnItemClickListener listener, int DimensionType) {
        this.mContext = context;
        this.listener = listener;
        this.DimensionType = DimensionType;
        mInflater = LayoutInflater.from(context);
    }

    public DormitoryListOrClassListAdapter(Context context, List<DormitoryOrClassLevelList> listBeen, DormitoryListOrClassListChildAdapter.OnItemClickListener listener, int DimensionType) {
        this.mContext = context;
        this.list = listBeen;
        this.listener = listener;
        mInflater = LayoutInflater.from(context);
    }

    public void setIsAdditionOrSubtraction(int isAdditionOrSubtraction) {
        this.isAdditionOrSubtraction = isAdditionOrSubtraction;
    }

    public void setList(List<DormitoryOrClassLevelList> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setBrowseIndex(int browseIndex) {
        this.browseIndex = browseIndex;
    }

    @Override
    public DormitoryListOrClassListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final View view = mInflater.inflate(R.layout.adapter_item_dormitory_list, viewGroup, false);
        return new DormitoryListOrClassListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DormitoryListOrClassListAdapter.ViewHolder holder, int position) {
        initValue(holder, position);
    }

    private void initValue(DormitoryListOrClassListAdapter.ViewHolder viewHolder, int i) {
        DormitoryListOrClassListChildAdapter childAdapter = new DormitoryListOrClassListChildAdapter(mContext, list.get(i).getLevelList(), listener, i);
        childAdapter.setDimensionType(DimensionType);
        childAdapter.setIsAdditionOrSubtraction(isAdditionOrSubtraction);
        viewHolder.item.setAdapter(childAdapter);
        int column;
        if (DimensionType == Constant.DimensionType_Dormitory) // 是否宿舍维度，宿舍5列，班级3列显示
            column = 5;
        else
            column = 3;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, column);
        viewHolder.item.setLayoutManager(gridLayoutManager);
        getHeaderId(i);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public long getHeaderId(int position) {
        return (long) position;
    }

    @Override
    public DormitoryListOrClassListAdapter.HeaderHolder onCreateHeaderViewHolder(ViewGroup parent) {
        final View view = mInflater.inflate(R.layout.sticky_header, parent, false);
        return new DormitoryListOrClassListAdapter.HeaderHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(DormitoryListOrClassListAdapter.HeaderHolder viewHolder, int position) {
        if (browseIndex == position) {
            viewHolder.headerText.getPaint().setFakeBoldText(true);
        }
        viewHolder.headerText.setText(list.get(position).getLevelName());
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        public RecyclerView item;

        public ViewHolder(View itemView) {
            super(itemView);
            item = (RecyclerView) itemView;
        }
    }

    class HeaderHolder extends RecyclerView.ViewHolder {
        private TextView headerText;

        private HeaderHolder(View itemView) {
            super(itemView);
            headerText = (TextView) itemView.findViewById(R.id.tv_dormitory_level_name);
        }
    }

}
