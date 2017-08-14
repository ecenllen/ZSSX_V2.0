package com.gta.zssx.fun.personalcenter.model;

import com.gta.zssx.fun.coursedaily.registercourse.view.base.CourseDaily;
import com.gta.zssx.fun.personalcenter.model.bean.UpdateBean;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.pub.http.HttpMethod;
import com.gta.zssx.pub.InterfaceList;

import java.util.Map;

import okhttp3.RequestBody;
import rx.Observable;

/**
 * Created by lan.zheng on 2016/6/18.
 */
public class UpdateDataManager {


    /**
     * 上传头像
     * @param map 请求参数
     * @return 返回请求结果
     */
    public static Observable<UserBean> uploadAvtor(Map<String, RequestBody> map) {
        InterfaceList lInterfaceList = HttpMethod
                .getInstance()
                //创建Retrofit实例，传入服务器地址
                .retrofitClient(CourseDaily.getInstance().getmServerAddress())
                //创建接口类的实例，为了调用具体的接口
                .create(InterfaceList.class);
        return HttpMethod.getInstance().call(lInterfaceList.uploadAvtor(map));
    }

    /**
     * 检查更新
     *
     * @return
     */
    public static Observable<UpdateBean> checkUpdate() {
        InterfaceList lInterfaceList = HttpMethod
                .getInstance()
                .retrofitClient(CourseDaily.getInstance().getmServerAddress(),10)
                .create(InterfaceList.class);
        return HttpMethod.getInstance().call(lInterfaceList.checkUpdate());
    }

    /**
     * 获取用户信息
     */
    public static Observable<UserBean> getUserInfoUpdate(String userId) {
        InterfaceList lInterfaceList = HttpMethod
                .getInstance()
                .retrofitClient(CourseDaily.getInstance().getmServerAddress())
                .create(InterfaceList.class);
        return HttpMethod.getInstance().call(lInterfaceList.getUserInfoUpdate(userId));
    }

}
