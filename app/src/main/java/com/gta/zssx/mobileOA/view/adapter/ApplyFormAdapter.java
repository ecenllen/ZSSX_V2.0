package com.gta.zssx.mobileOA.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.mobileOA.model.bean.ApplyForm;
import com.gta.zssx.mobileOA.view.page.BacklogDetailActivity;
import com.gta.zssx.pub.common.Constant;

import java.util.List;

/**
 * Created by xiaoxia.rang on 2016/10/24.
 * 申请表adapter
 */

public class ApplyFormAdapter extends RecyclerView.Adapter<ApplyFormAdapter.ViewHolder> {

    private Context context;
    private List<ApplyForm> forms;
    private LayoutInflater inflater;

    public ApplyFormAdapter(Context context, List<ApplyForm> forms) {
        this.context = context;
        this.forms = forms;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(inflater.inflate(R.layout.item_oa_apply_form, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ApplyForm form = forms.get(position);
        holder.position = position;
        holder.tvName.setText(form.getName());
    }

    @Override
    public int getItemCount() {
        return forms == null ? 0 : forms.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout llForm;
        TextView tvName;
        int position;

        public ViewHolder(View itemView) {
            super(itemView);
            llForm = (LinearLayout) itemView.findViewById(R.id.ll_form);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            llForm.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, BacklogDetailActivity.class);
            intent.putExtra("applyFormId",forms.get(position).getId());
            intent.putExtra("type", Constant.DETAIL_TYPE_APPLY);
            context.startActivity(intent);
        }
    }
}
