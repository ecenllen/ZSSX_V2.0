package com.gta.zssx.main;

import com.gta.zssx.fun.coursedaily.registercourse.view.base.CourseDaily;
import com.gta.zssx.fun.personalcenter.model.bean.UpdateBean;
import com.gta.zssx.pub.http.HttpMethod;
import com.gta.zssx.pub.InterfaceList;

import rx.Observable;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/8/25.
 * @since 1.0.0
 */
public class HomeDataManager {

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
}
