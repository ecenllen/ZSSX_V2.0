package com.gta.zssx.fun.adjustCourse.view.adapter;

import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gta.zssx.R;
import com.gta.zssx.fun.adjustCourse.model.bean.ApplyConfirmBean;
import com.gta.zssx.fun.adjustCourse.view.base.AdjustCourse;
import com.gta.zssx.fun.adjustCourse.view.base.Constant;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2017/3/27.
 * @since 1.0.0
 */
public class MyConfirmAdapter extends BaseQuickAdapter<ApplyConfirmBean.ListBean, BaseViewHolder> {

    private String mFinishStatus;

    public MyConfirmAdapter(String finishStatus) {
        super(R.layout.item_adjust_course);
        mFinishStatus = finishStatus;
    }


    @Override
    protected void convert(BaseViewHolder helper, ApplyConfirmBean.ListBean item) {
        TextView lCourseType = helper.getView(R.id.adjust_type_tv);
        TextView mAcceptTimeCourse = helper.getView(R.id.accept_time_course_tv);
        TextView lStatus = helper.getView(R.id.status_tv);
        if (item.getApplyType().equals("T") || item.getApplyType().equals("H")) {
            lCourseType.setText("调课申请");
            String lAdjustDate = TimeUtils.millis2String(item.getAdjustDate(), Constant.DATE_TYPE_01);
            mAcceptTimeCourse.setText(lAdjustDate + "  第" + item.getAdjustPolysyllabicWord() + "节");
            helper.setImageResource(R.id.type_image, R.drawable.exchange_of_people);
        } else {
            lCourseType.setText("代课申请");
            mAcceptTimeCourse.setText("");
            helper.setImageResource(R.id.type_image, R.drawable.people);
        }
        String lApplyDate = TimeUtils.millis2String(item.getApplyDate(), Constant.DATE_TYPE_01);
        helper.setText(R.id.apply_time_tv, lApplyDate);
        helper.setText(R.id.apply_name_tv, item.getApplyTeacherName());
        helper.setText(R.id.apply_time_course_tv, lApplyDate + "  第" + item.getApplyPolysyllabicWord() + "节");
//            有审核流程
        if (AdjustCourse.getInstance().hasAudit()) {
            String text;
            if (("0").equals(item.getConfimType())) {
                text = "已确认";
            } else {
                text = "未确认";
            }
            if (("1").equals(item.getAuditType())) {
                text = text + "，" + "已审核";
            } else {
                text = text + "，" + "未审核";
            }
            lStatus.setText(text);
        } else {
            String text;
            if (("0").equals(item.getConfimType())) {
                text = "已确认";
            } else {
                text = "未确认";
            }
            lStatus.setText(text);
        }
        lStatus.setTextColor(item.getAuditStatus().equals("N") ? ContextCompat.getColor(mContext, R.color.gray_666666) : ContextCompat.getColor(mContext, R.color.main_color));
        helper.setText(R.id.accept_name_tv, item.getAdjustTeacherName());
    }
}
