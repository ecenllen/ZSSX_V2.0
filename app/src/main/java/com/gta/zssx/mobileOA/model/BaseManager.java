package com.gta.zssx.mobileOA.model;

import com.gta.zssx.AppConfiguration;
import com.gta.zssx.ZSSXApplication;
import com.gta.zssx.pub.MobileOAInterfaceList;
import com.gta.zssx.pub.http.HttpMethod;

import retrofit2.Retrofit;

/**
 * Created by lan.zheng on 2016/7/15.
 */
public class BaseManager {
//    public static String address = ZSSXApplication.instance.getUser().getOaHostUrl() + "/api/mobileapi/";
    public static String address;

    /**
     * 生成InterfaceList接口对象
     *
     * @param mListClass
     * @return
     */
    public static MobileOAInterfaceList getInterfaceList(Class mListClass) {
        Retrofit lRetrofit = HttpMethod.getInstance().retrofitClient(getOaUrl());
        MobileOAInterfaceList lInterfaceList = (MobileOAInterfaceList) lRetrofit.create(mListClass);
        return lInterfaceList;
    }

    /**
     * 获取OAurl
     * @return
     */
    private static String getOaUrl() {
        return address == null ? (address = AppConfiguration.getInstance().getServerAddress().getOAUrL() + "/api/mobileapi/") : address;
    }

}
