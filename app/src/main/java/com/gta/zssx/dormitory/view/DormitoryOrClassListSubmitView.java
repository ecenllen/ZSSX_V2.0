package com.gta.zssx.dormitory.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.dormitory.model.bean.DormitoryRankingBean;

import java.util.List;

/**
 * Created by lan.zheng on 2017/6/27.
 */

public interface DormitoryOrClassListSubmitView extends BaseView {
    void submitSuccessOrFail(boolean success);
    void changedDateSuccess(boolean success);
}
