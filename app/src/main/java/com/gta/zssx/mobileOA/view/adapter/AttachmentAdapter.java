package com.gta.zssx.mobileOA.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.mobileOA.model.bean.Attachment;
import com.gta.zssx.mobileOA.view.page.AttachmentDetailActivity;

import java.util.List;

/**
 * Created by xiaoxia.rang on 2016/10/24.
 * 附件adapter
 */

public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.ViewHolder> {

    private Context context;
    private List<Attachment> attachments;
    private LayoutInflater inflater;

    public AttachmentAdapter(Context context, List<Attachment> attachments) {
        this.context = context;
        this.attachments = attachments;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(inflater.inflate(R.layout.item_oa_attachment, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Attachment attachment = attachments.get(position);
        holder.position = position;
        holder.tvName.setText(attachment.getName());
        holder.tvSize.setText(attachment.getSize());
    }

    @Override
    public int getItemCount() {
        return attachments == null ? 0 : attachments.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RelativeLayout rlAttachment;
        ImageView ivType;
        TextView tvName;
        TextView tvSize;
        ImageView ivMore;
        int position;

        public ViewHolder(View itemView) {
            super(itemView);
            rlAttachment = (RelativeLayout)itemView.findViewById(R.id.rl_attachment);
            ivType = (ImageView)itemView.findViewById(R.id.iv_type);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvSize = (TextView) itemView.findViewById(R.id.tv_size);
            ivMore = (ImageView)itemView.findViewById(R.id.iv_more);
            rlAttachment.setOnClickListener(this);
            ivMore.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, AttachmentDetailActivity.class);
            intent.putExtra("attachment",attachments.get(position));
            context.startActivity(intent);
        }
    }
}
