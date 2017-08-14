package com.gta.zssx.fun.personalcenter.model;

import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.pub.InterfaceList;
import com.gta.zssx.pub.http.HttpMethod;
import com.gta.zssx.pub.http.HttpResult;

import rx.Observable;

/**
 * [Description]
 * <p/>
 * [How to use]
 * <p/>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/16.
 * @since 1.0.0
 */
public class LoginDataManager {

    public static Observable<UserBean> userLogin(String loginName, String password, String serverAddress) {
        Observable<HttpResult<UserBean>> lHttpResultObservable = HttpMethod
                .getInstance()
                .retrofitClient(serverAddress, 10)
                .create(InterfaceList.class)
                .userLogin(loginName, password);

        return HttpMethod.getInstance().call(lHttpResultObservable);
    }

    public static Observable<String> getTicket(String loginName, String password, String serverAddress,String ticket) {
        Observable<HttpResult<String>> lHttpResultObservable = HttpMethod
                .getInstance()
                .retrofitClient(serverAddress, 10)
                .create(InterfaceList.class)
                .getTicket(loginName, password,ticket);

        return HttpMethod.getInstance().call(lHttpResultObservable);
    }


}
