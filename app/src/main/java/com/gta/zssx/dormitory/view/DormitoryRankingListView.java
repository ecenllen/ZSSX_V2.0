package com.gta.zssx.dormitory.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.dormitory.model.bean.DormitoryRankingBean;

import java.util.List;

/**
 * Created by lan.zheng on 2017/6/27.
 */

public interface DormitoryRankingListView extends BaseView {
    void showToast(String msg,int actionType);  //显示提示
    void showEmpty(int action, boolean success);  //显示空白页，针对下拉刷新
    void getListSuccess(List<DormitoryRankingBean> dormitoryRankingBeanList,int refreshOrLoadMore);  //获取列表成功
    void deleteOrSubmitSuccess(int actionType);
}
