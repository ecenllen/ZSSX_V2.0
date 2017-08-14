package com.gta.zssx.fun.coursedaily.registercourse.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.StateBean;

import java.util.List;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/20.
 * @since 1.0.0
 */
public class StudentStateInnerAdapter extends RecyclerView.Adapter {

    private List<StateBean> mStateBeen;
    private Context mContext;
    private ImageView mSelectImage;
    private Listener mListener;
    private int mLessonState;

    public StudentStateInnerAdapter(List<StateBean> stateBeen, Context context,
                                    Listener listener, int lessonState) {
        mStateBeen = stateBeen;
        mContext = context;
        mListener = listener;
        mLessonState = lessonState;
    }

    public interface Listener {
        void itemClick(StateBean stateBean);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return InnerHolder.newHolder(parent, mContext);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final StateBean lStateBean = mStateBeen.get(position);
        final InnerHolder lInnerHolder = (InnerHolder) holder;
        lInnerHolder.mTextView.setText(lStateBean.getState());

        if (lStateBean.getStateCode() == mLessonState) {
            lInnerHolder.mImageView.setVisibility(View.VISIBLE);
            mSelectImage = lInnerHolder.mImageView;
        }


        lInnerHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectImage.setVisibility(View.GONE);
                lInnerHolder.mImageView.setVisibility(View.VISIBLE);
                mSelectImage = lInnerHolder.mImageView;
                if (mListener != null) {
                    mListener.itemClick(lStateBean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mStateBeen.size();
    }

    private static class InnerHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        ImageView mImageView;

        public InnerHolder(Context context, View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.state_tv);
            mImageView = (ImageView) itemView.findViewById(R.id.state_image);
        }

        private static InnerHolder newHolder(ViewGroup parent, Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_student_state_inner, parent, false);
            return new InnerHolder(context, view);
        }
    }
}
