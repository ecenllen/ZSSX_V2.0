package com.gta.zssx.fun.adjustCourse.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gta.zssx.R;

import java.util.List;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/11/1.
 * @since 1.0.0
 */
public class CenterItemDialog {
    private List<String> mList;
    private Context mContext;
    private onItemClick mOnItemClick;

    public CenterItemDialog(List<String> list, Context context) {
        mList = list;
        mContext = context;
    }

    public CenterItemDialog create() {
        AlertDialog.Builder lBuilder = new AlertDialog.Builder(mContext);
        View lInflate = LayoutInflater.from(mContext).inflate(R.layout.dialog_center_item, null);
        RecyclerView lRecyclerView = (RecyclerView) lInflate.findViewById(R.id.recyclerView);
        lRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        lBuilder.setView(lInflate);
        AlertDialog lShow = lBuilder.show();
        lShow.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        lRecyclerView.setAdapter(new ItemAdapter(mList, mContext, new ItemAdapter.Listener() {
            @Override
            public void itemClick(int position) {
                mOnItemClick.itemClick(position);
                lShow.dismiss();
            }
        }));

        return this;
    }

    public interface onItemClick {
        void itemClick(int position);
    }

    public CenterItemDialog setOnItemClickListener(onItemClick o) {
        mOnItemClick = o;
        return this;
    }

    private static class ItemAdapter extends RecyclerView.Adapter {

        private List<String> mList;
        private Context mContext;
        private Listener mListener;

        public ItemAdapter(List<String> list, Context context, Listener listener) {
            mList = list;
            mContext = context;
            mListener = listener;
        }

        public interface Listener {
            void itemClick(int position);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return ListHolder.newHolder(parent, mContext);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ListHolder lListHolder = (ListHolder) holder;
            String lBasePath = mList.get(position);
            lListHolder.mTextView.setText(lBasePath);
            lListHolder.itemView.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.itemClick(position);
                }
            });
            lListHolder.mView.setVisibility(position == (mList.size() - 1) ? View.GONE : View.VISIBLE);
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        private static class ListHolder extends RecyclerView.ViewHolder {
            TextView mTextView;
            View mView;

            public ListHolder(Context context, View itemView) {
                super(itemView);
                mTextView = (TextView) itemView.findViewById(R.id.center_tv);
                mView = itemView.findViewById(R.id.line_view);
            }

            private static ListHolder newHolder(ViewGroup parent, Context context) {
                View view = LayoutInflater.from(context).inflate(R.layout.item_center_dialog, parent, false);
                return new ListHolder(context, view);
            }
        }
    }
}
