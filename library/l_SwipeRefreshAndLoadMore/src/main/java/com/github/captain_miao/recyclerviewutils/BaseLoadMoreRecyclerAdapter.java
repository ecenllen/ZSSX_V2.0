package com.github.captain_miao.recyclerviewutils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

/**
 * Created on 15/8/23.
 */
public abstract class BaseLoadMoreRecyclerAdapter<T, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter {
    public static final int TYPE_FOOTER = Integer.MIN_VALUE;
    public static final int TYPE_ITEM = 0;

    private boolean hasFooter;//设置是否显示Footer
    private boolean hasMoreData;//设置是否可以继续加载数据

    private final List<T> mList = new LinkedList<T>();
    private CharSequence mFooterLoadingShowStringResource = "加载中...";//loading_more
    private CharSequence mFooterNoMoreDataShowStringResource = "没有更多数据了...";//loading_more

    public BaseLoadMoreRecyclerAdapter() {
        addFooterData();
    }

    public BaseLoadMoreRecyclerAdapter<T, VH> setFooterLoadingShowStringResource(CharSequence footerLoadingShowStringResource) {
        mFooterLoadingShowStringResource = footerLoadingShowStringResource;
        return this;
    }

    public BaseLoadMoreRecyclerAdapter<T, VH> setFooterNoMoreDataShowStringResource(CharSequence footerNoMoreDataShowStringResource) {
        mFooterNoMoreDataShowStringResource = footerNoMoreDataShowStringResource;
        return this;
    }

    //数据itemViewHolder 实现
    public abstract VH onCreateItemViewHolder(ViewGroup parent, int viewType);

    //数据itemViewHolder 实现
    public abstract void onBindItemViewHolder(final VH holder, int position);

    public int getFooterLayoutResource() {
        return R.layout.list_footer_view;//default
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {

        public final ProgressBar mProgressView;

        public final TextView mTextView;

        public FooterViewHolder(View view) {
            super(view);
            mProgressView = (ProgressBar) view.findViewById(R.id.progressbar);
            mTextView = (TextView) view.findViewById(R.id.tv_state);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTextView.getText();
        }

    }


    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_FOOTER://底部 加载view
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(getFooterLayoutResource(), parent, false);
                return getFooterViewHolder(view);
            default://数据itemViewHolder
                return onCreateItemViewHolder(parent, viewType);
        }
    }

    @NonNull
    protected RecyclerView.ViewHolder getFooterViewHolder(View view) {
        return new FooterViewHolder(view);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_FOOTER:
                //没有更多数据
                if (hasMoreData) {
                    bindMoreDataFooterViewHolder(holder);
                } else {
                    bindNoMoreFooterViewHolder(holder);
                }
                break;
            default:
                onBindItemViewHolder((VH) holder, position);
                break;
        }
    }

    protected void bindNoMoreFooterViewHolder(RecyclerView.ViewHolder holder) {
        final FooterViewHolder lHolder = (FooterViewHolder) holder;
        lHolder.mProgressView.setVisibility(View.GONE);
        lHolder.mTextView.setText(mFooterNoMoreDataShowStringResource);
    }

    protected void bindMoreDataFooterViewHolder(RecyclerView.ViewHolder holder) {
        final FooterViewHolder lHolder = (FooterViewHolder) holder;
        lHolder.mProgressView.setVisibility(View.VISIBLE);
        lHolder.mTextView.setText(mFooterLoadingShowStringResource);
    }

    @Override
    public final int getItemViewType(int position) {
        if ((position == getItemCount() - 1 && hasFooter)) {//有且是最后一个
            return TYPE_FOOTER;
        }
        return getItemViewType2(position);//0
    }

    protected int getItemViewType2(int position) {
        return 0;
    }

    public List<T> getList() {
        //去除最后代表footer的一个空数据
        final int lSize = getItemCount();
        if (lSize == 1) {
            return new LinkedList<>();
        }
        return mList.subList(0, lSize - 1);
    }

    public void appendToList(List<T> list) {
        if (list == null) {
            return;
        }
        //在footer数组前加上新数据
        if (hasFooter) {
            final int lSize = getItemCount();
            mList.addAll(lSize - 1, list);
        } else
            mList.addAll(list);
    }

    public void append(T t) {
        if (t == null) {
            return;
        }
        if (hasFooter) {
            mList.add(getItemCount() - 1, t);
        } else
            mList.add(t);
    }

    public void appendToTop(T item) {
        if (item == null) {
            return;
        }
        mList.add(0, item);
    }

    public void appendToTopList(List<T> list) {
        if (list == null) {
            return;
        }
        mList.addAll(0, list);
    }


    public void remove(int position) {
        if (hasFooter && getItemCount() - 1 == position) {
            //footer不能通过remove来删除
            return;
        }
        if (position < getItemCount() - 1 && position >= 0) {
            mList.remove(position);
        }
    }

    public void clear() {
        mList.clear();
        addFooterData();
    }

    public void clearData() {
        int lSize = mList.size();
        setHasMoreDataAndFooter(false, false);
        mList.clear();
        notifyItemRangeRemoved(0, lSize);

    }

    private void addFooterData() {
        if (hasFooter) {
            mList.add(null);
        }
    }

    public int getBzsObjsItemCount() {
        return hasFooter ? getItemCount() - 1 : getItemCount();
    }

    @Override
    public final int getItemCount() {
        return mList.size();
    }

    public T getItem(int position) {
        if (position > getItemCount() - 1) {
            return null;
        }
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public boolean hasFooter() {
        return hasFooter;
    }

    public BaseLoadMoreRecyclerAdapter<T, VH> setHasFooter(boolean hasFooter) {
        if (this.hasFooter != hasFooter) {
            if (hasFooter) {
                //原本没有，现在要加
                mList.add(null);
            } else {
                //原本有，现在要去掉
                mList.remove(getItemCount() - 1);
            }
            this.hasFooter = hasFooter;
//            notifyDataSetChanged();
        }
        return this;
    }


    public boolean hasMoreData() {
        return hasMoreData;
    }

    public BaseLoadMoreRecyclerAdapter<T, VH> setHasMoreData(boolean isMoreData) {
        if (this.hasMoreData != isMoreData) {
            this.hasMoreData = isMoreData;
            notifyDataSetChanged();
//            notifyItemChanged(getItemCount()-1);
        }
        return this;
    }

    public BaseLoadMoreRecyclerAdapter<T, VH> setHasMoreDataAndFooter(boolean hasMoreData, boolean hasFooter) {
        if (this.hasMoreData != hasMoreData || this.hasFooter != hasFooter) {
            this.hasMoreData = hasMoreData;
            setHasFooter(hasFooter);
//            notifyDataSetChanged();
            notifyItemChanged(getItemCount() - 1);
        }
        return this;
    }

    public BaseLoadMoreRecyclerAdapter<T, VH> setOnLoadMoreError() {
        setHasFooter(true);

        setFooterNoMoreDataShowStringResource("刷新出错...");

        return this;
    }

    public BaseLoadMoreRecyclerAdapter<T, VH> loadFirst(List<T> list, int pageItem) {

        boolean lHasFooter = list.size() >= pageItem;
        setHasMoreDataAndFooter(lHasFooter, lHasFooter);
        clear();
        appendToList(list);
        return this;
    }

    public BaseLoadMoreRecyclerAdapter<T, VH> loadMore(List<T> list) {
        setHasMoreDataAndFooter(true, true);
        // 在列表后加上下一页的数据
        appendToList(list);
        final int lItemCount = getItemCount();
        notifyItemRangeInserted(lItemCount, list.size());
        return this;
    }

    public BaseLoadMoreRecyclerAdapter<T, VH> loadEnd() {
        setHasMoreDataAndFooter(false, true);
        return this;
    }

}
