package com.gta.zssx.mobileOA.view.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gta.utils.helper.GlideUtils;
import com.gta.zssx.R;
import com.gta.zssx.mobileOA.model.bean.SealInfo;
import com.gta.zssx.mobileOA.view.page.ChooseSealActivity;

import java.util.List;


/**
 * Created by xiaoxia.rang on 2017/4/24.
 * 印章adapter
 */

public class SealAdapter extends RecyclerView.Adapter<SealAdapter.ViewHolder> {

    private Context context;
    private List<SealInfo> sealInfos;
    private ChooseSealActivity.ISealSelectListener sealSelectListener;
    private AlertDialog sealDialog;

    public SealAdapter(Context context, List<SealInfo> sealInfos, ChooseSealActivity.ISealSelectListener listener) {
        this.context = context;
        this.sealInfos = sealInfos;
        this.sealSelectListener = listener;
    }

    @Override
    public SealAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_oa_seal, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(SealAdapter.ViewHolder holder, int position) {
        SealInfo sealInfo = sealInfos.get(position);
        GlideUtils.loadImage(context, sealInfo.getSealPath(), holder.imageView);
        holder.textView.setText(sealInfo.getSealName() == null ? "" : sealInfo.getSealName());
        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return sealInfos == null ? 0 : sealInfos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RelativeLayout relativeLayout;
        ImageView imageView;
        TextView textView;
        int position;

        public ViewHolder(View itemView) {
            super(itemView);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.rl_seal);
            imageView = (ImageView) itemView.findViewById(R.id.iv_seal);
            textView = (TextView) itemView.findViewById(R.id.tv_seal);
            relativeLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            showPwdDialog(position);
        }
    }

    public void showPwdDialog(int position) {
        SealInfo sealInfo = sealInfos.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.dialog_seal_validate, null);//获取自定义布局
        builder.setView(view);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        ImageView ivSeal = (ImageView) view.findViewById(R.id.iv_seal);
        TextView tvSeal = (TextView) view.findViewById(R.id.tv_seal);
        EditText etPwd = (EditText) view.findViewById(R.id.et_pwd);
        TextView tvConfirm = (TextView) view.findViewById(R.id.tv_confirm);
        TextView tvErrorHint = (TextView) view.findViewById(R.id.tv_pwd_error);
        btnCancel.setOnClickListener(v -> sealDialog.dismiss());
        GlideUtils.loadImage(context, sealInfo.getSealPath(), ivSeal);
        tvSeal.setText(sealInfo.getSealName() == null ? "" : sealInfo.getSealName());
        tvConfirm.setOnClickListener(v -> {
            String pwd = etPwd.getText().toString().trim();
            if (TextUtils.isEmpty(pwd)) {
                tvErrorHint.setText(R.string.hint_pwd_empty);
                tvErrorHint.setVisibility(View.VISIBLE);
                return;
            } else {
                if (pwd.equals(sealInfo.getSealPassword())) {
                    sealSelectListener.onSealSelected(sealInfo);
                    sealDialog.dismiss();
                } else {
                    tvErrorHint.setText(R.string.hint_pwd_error);
                    tvErrorHint.setVisibility(View.VISIBLE);
                    return;
                }
            }
        });
        sealDialog = builder.create();
        sealDialog.show();
    }
}
