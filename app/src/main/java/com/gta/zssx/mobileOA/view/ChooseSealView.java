package com.gta.zssx.mobileOA.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.mobileOA.model.bean.SealInfo;

import java.util.List;

/**
 * Created by xiaoxia.rang on 2017/5/15.
 * 选择签章presenter
 */

public interface ChooseSealView extends BaseView {

    /**
     * 显示签章列表
     *
     * @param sealInfos 签章
     */
    void showSealInfos(List<SealInfo> sealInfos);

    void showEmpty();
}
