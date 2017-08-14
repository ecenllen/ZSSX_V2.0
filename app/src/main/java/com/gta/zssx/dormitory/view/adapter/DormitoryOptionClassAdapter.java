package com.gta.zssx.dormitory.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.dormitory.model.bean.ClassInfoBean;
import com.gta.zssx.pub.common.Constant;
import com.gta.zssx.pub.util.DormitoryScoreFilter;
import com.gta.zssx.pub.util.StringUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * [Description]
 * <p> 宿舍管理-班级维度/宿舍维度-不录入姓名-有/无选项设置-班级适配器
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Weiye.Chen on 2017/07/24.
 * @since 2.0.0
 */
public class DormitoryOptionClassAdapter extends RecyclerView.Adapter<DormitoryOptionClassAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private Context mContext;
    private ArrayList<ClassInfoBean> list;
    private OnClassItemTextChangeListener listener;
    /**
     * 默认班级得分
     */
    private String defaultClassScore;
    private boolean isCanEdit;
    private int isAdditionOrSub;
    /**
     * 分数取值于列表，或者自动跟随宿舍得分
     */
    private int scoreFromListOrAuto;
    private int fromList = 1;
    private int fromAuto = 2;

    public DormitoryOptionClassAdapter(Context context, OnClassItemTextChangeListener listener) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    public void setCanEdit(boolean canEdit) {
        isCanEdit = canEdit;
    }

    public void setIsAdditionOrSub(int isAdditionOrSub) {
        this.isAdditionOrSub = isAdditionOrSub;
    }

    public void setList(ArrayList<ClassInfoBean> list) {
        scoreFromListOrAuto = fromList;
        this.list = list;
        notifyDataSetChanged();
    }

    public ArrayList<ClassInfoBean> getList() {
        return list;
    }

    public void setDefaultClassScore(String defaultClassScore) {
        scoreFromListOrAuto = fromAuto;
        this.defaultClassScore = defaultClassScore;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final View view = mInflater.inflate(R.layout.adapter_item_option_class, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DormitoryOptionClassAdapter.ViewHolder holder, int position) {
        ClassInfoBean classInfoBean = list.get(position);
        if (!isCanEdit) { // 查看状态，不能编辑
            holder.etRemark.setEnabled(false);
            holder.etClassScore.setEnabled(false);
        }
        if (scoreFromListOrAuto == fromAuto) {  // 自动得分，跟随宿舍得分
            classInfoBean.setClassSingleScore(defaultClassScore);
        }

//        if (!classInfoBean.getClassSingleScore().isEmpty() && Float.valueOf(classInfoBean.getClassSingleScore()) == 0) { // 扣分项，分数为0，隐藏-号和隐藏分数显示
//            classInfoBean.setClassSingleScore("");
//        }

        if (isAdditionOrSub == Constant.Addition)
            holder.tvMinus.setVisibility(View.INVISIBLE);
        else
            holder.tvMinus.setVisibility(View.VISIBLE);
        holder.tvDormitoryName.setText(classInfoBean.getClassName());
        holder.etClassScore.setText(StringUtils.formatScore(classInfoBean.getClassSingleScore()));
        holder.etRemark.setText(classInfoBean.getClassSingleRemark());
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.et_class_score)
        EditText etClassScore;
        @Bind(R.id.et_class_remark)
        EditText etRemark;
        @Bind(R.id.tv_dormitory_class_name)
        TextView tvDormitoryName;
        @Bind(R.id.tv_minus)
        TextView tvMinus;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            etClassScore.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String text = s.toString().trim();

                        list.get(getLayoutPosition()).setClassSingleScore(s.toString().trim());
                        if (listener != null)
                            listener.onTextChange(getLayoutPosition(), text, true);
                    if (etClassScore.hasFocus()) {  // 自动得分，跟随宿舍得分
                        // TODO
                        /*班级评分输入框，过虑输入不合法，不标准的分数，保留3位小数*/
                        DormitoryScoreFilter.filterScore(s, etClassScore);
                    }
                }
            });
            etRemark.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String text = s.toString().trim();
                    list.get(getLayoutPosition()).setClassSingleRemark(text);
                    if (listener != null)
                        listener.onTextChange(getLayoutPosition(), text, false);
                }
            });
        }

    }

    public interface OnClassItemTextChangeListener {
        /**
         * @param position  下标
         * @param afterText 输入内容
         * @param isScore   班级得分或备注
         */
        void onTextChange(int position, String afterText, boolean isScore);
    }

}
