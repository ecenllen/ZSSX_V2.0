package com.gta.zssx.pub;

import com.gta.zssx.mobileOA.model.bean.SealInfo;
import com.gta.zssx.pub.http.HttpResult;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by xiaoxia.rang on 2017/5/15.
 */

public interface BpmInMobileOAInterfaceList {

    /**
     * 获取资产数据
     *
     * @param account
     * @return
     */
    @GET(BpmInMobileOAInterfaceName.GET_SEALINFO_LIST)
    Observable<HttpResult<List<SealInfo>>> getSealInfoList(@Query("account") String account);
}
