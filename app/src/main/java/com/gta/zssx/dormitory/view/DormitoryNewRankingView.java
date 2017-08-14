package com.gta.zssx.dormitory.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.dormitory.model.bean.DormitoryOrClassSingleInfoBean;

import java.util.List;

/**
 * Created by lan.zheng on 2017/6/27.
 */

public interface DormitoryNewRankingView extends BaseView {
    void getServerTimeSuccess(String dateTime);

    void getDormitoryOrClassSuccess(List<DormitoryOrClassSingleInfoBean> domitoryOrClassInfoBeanList);

    void getDormitoryOrClassFailed();

    void resultIsCanInput(int message, List<Integer> idList);  //message为0，表示选中的宿舍楼或专业部中有被删除的，为1表示有被登记过的
}
