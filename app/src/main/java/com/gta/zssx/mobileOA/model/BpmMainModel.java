package com.gta.zssx.mobileOA.model;

import com.gta.zssx.ZSSXApplication;
import com.gta.zssx.mobileOA.model.bean.SealInfo;
import com.gta.zssx.pub.BpmInMobileOAInterfaceList;
import com.gta.zssx.pub.http.HttpMethod;

import java.io.File;
import java.util.List;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by xiaoxia.rang on 2017/5/15.
 */

public class BpmMainModel {
    public static String address = ZSSXApplication.instance.getUser().getBpmHostUrl() + File.separator;
    private static final Class mInterfaceListClass = BpmInMobileOAInterfaceList.class;

    /**
     * 生成InterfaceList接口对象
     *
     * @param mListClass
     * @return
     */
    public static BpmInMobileOAInterfaceList getInterfaceList(Class mListClass) {
        Retrofit lRetrofit = HttpMethod.getInstance().retrofitClient(getBpmUrl());
        BpmInMobileOAInterfaceList lInterfaceList = (BpmInMobileOAInterfaceList) lRetrofit.create(mListClass);
        return lInterfaceList;
    }

    /**
     * 获取OAurl
     *
     * @return
     */
    public static String getBpmUrl() {
        return address == null ? ZSSXApplication.instance.getUser().getBpmHostUrl() + File.separator : address;
    }

    /**
     * 获取bpm中签章数据
     *
     * @param userId 用户id
     * @return 签章列表
     */
    public static Observable<List<SealInfo>> getSealInfoList(String userId) {
        return HttpMethod.getInstance().call(getInterfaceList(mInterfaceListClass).getSealInfoList(userId));
    }
}
