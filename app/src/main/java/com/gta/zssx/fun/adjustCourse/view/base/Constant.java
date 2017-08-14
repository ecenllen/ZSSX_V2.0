package com.gta.zssx.fun.adjustCourse.view.base;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2017/3/13.
 * @since 1.0.0
 */
public class Constant {

    //    调课
    public static final String APPLY_TYPE_T = "T";
    //    代课
    public static final String APPLY_TYPE_D = "D";
    //    调时间
    public static final String APPLY_TYPE_H = "H";
    //    审核状态已结束
    public static final String AUDIT_STATUS_Y = "Y";
    //    审核状态未结束
    public static final String AUDIT_STATUS_N = "N";

    //    查看类型的详情
    public static final int DETAIL_TYPE_CHECK = 2;
    //    确认类型的详情
    public static final int DETAIL_TYPE_CONFIRM = 0;
    //    审核类型的详情
    public static final int DETAIL_TYPE_AUDIT = 1;
    //    代表我的申请
    public static final int PAGE_ONE = 1;
    //    代表确认申请
    public static final int PAGE_TOW = 2;
    //    代表审核申请
    public static final int PAGE_THREE = 3;
    //    需要审核
    public static final int HAS_AUDIT = 1;
    //    不需要审核
    public static final int NO_AUDIT = 0;

    //    删除RxBus
    public static class DeleteSuccess {}

    //    确认RxBus
    public static class ConfirmSuccess {}

    //    审核RxBus
    public static class AuditSuccess {}

    //调课类型
    public static final String COURSE_T = "course_t";
    //代课类型
    public static final String COURSE_D = "course_d";
    //用于课程表界面判断是否从调代课详情进入
    public static final String COURSE_N = "course_n";
    //调时间
    public static final String COURSE_S = "course_s";

    public static final String DATE_TYPE_01 = "yyyy-MM-dd";

    public static final String DATE_TYPE_02 = "yyyy年MM月";

    public static final String DATE_TYPE_03 = "yyyy-MM-dd HH:mm";

}
