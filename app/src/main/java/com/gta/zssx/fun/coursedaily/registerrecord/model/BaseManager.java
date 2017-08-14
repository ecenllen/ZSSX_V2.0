package com.gta.zssx.fun.coursedaily.registerrecord.model;

import com.gta.zssx.fun.coursedaily.registercourse.view.base.CourseDaily;
import com.gta.zssx.pub.http.HttpMethod;
import com.gta.zssx.pub.InterfaceList;

import retrofit2.Retrofit;

/**
 * Created by lan.zheng on 2016/7/15.
 */
public class BaseManager {

    /**
     * 生成InterfaceList接口对象
     * @param mListClass
     * @return
     */
    public static InterfaceList getInterfaceList(Class mListClass){
        Retrofit lRetrofit = HttpMethod.getInstance().retrofitClient(CourseDaily.getInstance().getmServerAddress());
        InterfaceList lInterfaceList = (InterfaceList) lRetrofit.create(mListClass);
        return lInterfaceList;
    }

}
