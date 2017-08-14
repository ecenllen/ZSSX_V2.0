package com.gta.zssx.fun.coursedaily.registercourse.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.DetailItemShowBean;
import com.gta.zssx.patrolclass.model.entity.ChooseTeacherEntity;
import com.gta.zssx.patrolclass.model.entity.ChooseTeacherSearchEntity;

import java.util.List;

/**
 * Created by lan.zheng on 2017/3/3.
 */
public interface MultiselectTeacherView extends BaseView {
    void showResult(List<ChooseTeacherEntity> entities,boolean isRequestHaveNext);  //获取教师组织成功,如果是请求是否有下一级的isRequestHaveNext为true

    void getTeacherListFailed(boolean isRequestHaveNext);  //获取教师组织失败

    void showToast(String s);  //显示提示

    void showResultSearch(List<ChooseTeacherSearchEntity> chooseTeacherSearchEntities); //获取搜索老师成功
}
