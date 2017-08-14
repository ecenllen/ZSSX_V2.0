package com.gta.zssx.dormitory.model;
import com.gta.zssx.dormitory.view.base.Dormitory;
import com.gta.zssx.pub.DormitoryInterfaceList;
import com.gta.zssx.pub.http.HttpMethod;

import retrofit2.Retrofit;

/**
 * Created by lan.zheng on 2016/7/15.
 */
public class BaseDormitoryManager {

    /**
     * 生成InterfaceList接口对象
     * @param mListClass
     * @return
     */
    public static DormitoryInterfaceList getInterfaceList(Class mListClass){
        Retrofit lRetrofit = HttpMethod.getInstance().retrofitClient(Dormitory.getInstance().getmServerAddress());
        if(lRetrofit == null) throw new NullPointerException("DormitoryInterfaceList's lRetrofit cannot be null!");
        DormitoryInterfaceList lInterfaceList = (DormitoryInterfaceList) lRetrofit.create(mListClass);
        return lInterfaceList;
    }

}
