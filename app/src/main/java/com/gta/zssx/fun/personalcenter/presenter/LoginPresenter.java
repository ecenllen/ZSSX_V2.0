package com.gta.zssx.fun.personalcenter.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.blankj.utilcode.util.SPUtils;
import com.gta.utils.helper.Helper_String;
import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.model.ClassDataManager;
import com.gta.zssx.fun.personalcenter.model.LoginDataManager;
import com.gta.zssx.fun.personalcenter.model.bean.ServerBean;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.fun.personalcenter.view.LoginView;
import com.gta.zssx.main.HomePageActivity;
import com.gta.zssx.pub.exception.CustomException;
import com.gta.zssx.pub.util.DES3Utils;
import com.gta.zssx.pub.util.LogUtil;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;


/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/14.
 * @since 1.0.0
 */
public class LoginPresenter extends BasePresenter<LoginView> {

    public static final String USERNAME_SP = "username_sp";
    public static final String KEY_SP = "username";

    public UserBean mUserBean;
    public Subscription mSubscription;

    public void Login(final Context context, final String loginName, final String Password, final String MD5Password, final String serverIP) {
        if (getView() == null) {
            return;
        }
        final String serverAddress = "http://" + serverIP + "/TQA/API/MoblieAPI/";
        final String PatrolClassServerAddress = "http://" + serverIP + "/TE/API/MoblieAPI/";
        final String AssetServerAddress = "http://" + serverIP + "/EAM/api/ApiMobileService/";
        final String OAServerAddress = "http://" + serverIP + "/OA/api/mobileapi/";
        final String DormitoryAddress = "http://" + serverIP + "/stu/api/DormMobileApi/";
        final String ClassroomFeedbackAddress = "http://" + serverIP + "/TE/API/MobileTEAPI/";//TODO 课堂教学反馈的服务器地址，待添加


        if (!Helper_String.isURL(serverAddress)) {
            getView().loginFail(1, context.getString(R.string.server_address_error));
            return;
        }
        getView().showDialog("正在登录...", false);
        mSubscription = LoginDataManager
                .userLogin(loginName, MD5Password, serverAddress)
                .flatMap(new Func1<UserBean, Observable<UserBean>>() {
                    @Override
                    public Observable<UserBean> call(UserBean userBean) {
                        return getTicket(userBean, loginName, MD5Password);
                    }
                })
                .subscribe(new Subscriber<UserBean>() {
                    @Override
                    public void onCompleted() {
                        if (getView() != null)
                            getView().hideDialog();
                    }

                    @Override
                    public void onError(Throwable e) {

                        if (getView() == null)
                            return;
                        getView().hideDialog();
                        if (e instanceof CustomException) {
                            CustomException lE = (CustomException) e;
                            if (lE.getMessage().equals(context.getString(R.string.account_or_password_wrong))) {
                                getView().loginFail(1, context.getString(R.string.password_error));
                            } else if (lE.getCode() == CustomException.NETWORK_UNAVAILABLE) {
                                getView().showError(e.getMessage());
                            } else {
                                getView().showError("登录失败！");
                            }
                        }
                        if (e instanceof ConnectException) {
                            getView().loginFail(1, context.getString(R.string.server_address_error));
                            return;
                        }

                        if (e instanceof SocketTimeoutException || e instanceof SocketException) {
                            showWarning(context.getString(R.string.requst_time_out));
                            return;
                        }

                        if (e instanceof UnknownHostException) {
                            getView().loginFail(1, context.getString(R.string.server_address_error));
                            return;
                        }

                        if (e instanceof HttpException) {
                            getView().loginFail(1, context.getString(R.string.server_address_error));
                            return;
                        }

                    }

                    @Override
                    public void onNext(UserBean userBean) {
                        if (getView() != null) {
                            userBean.setServerAddress(serverAddress);
                            AppConfiguration.SERVERURL = serverAddress;

                            userBean.setAccount(loginName);
                            userBean.setMD5Password(MD5Password);
                            userBean.setPassword(Password);
                            ServerBean lServerBean = new ServerBean();
                            lServerBean.setIp(serverIP);
                            lServerBean.setCourseDailyUrl(serverAddress);
                            lServerBean.setPatrolClassUrl(PatrolClassServerAddress);
                            lServerBean.setAssetUrl(AssetServerAddress);
                            lServerBean.setDormitoryUrl(DormitoryAddress);
                            lServerBean.setClassroomFeedbackUrl(ClassroomFeedbackAddress);
                            String oaUrl = userBean.getOaHostUrl().endsWith("/") ? userBean.getOaHostUrl() + "api/mobileapi/" : userBean.getOaHostUrl() + "/api/mobileapi/";
                            lServerBean.setOAUrL(oaUrl);

                            String AdjustCourseAddress = userBean.getCourseHostUrl().endsWith("/") ? userBean.getCourseHostUrl() + "api/" : userBean.getCourseHostUrl() + "/api/";
                            lServerBean.setAdjustCourseUrl(AdjustCourseAddress);
                            lServerBean.setTicket(userBean.getTicket());
                            AppConfiguration.getInstance().setServerAddress(lServerBean);

                            mUserBean = userBean;
                            LogUtil.e("userId = " + userBean.getUserId());
                            //                        getClassList(userBean);
//                        AppConfiguration.getInstance().setFirstIn(userBean);
                            AppConfiguration.getInstance().setUserLoginBean(userBean).saveAppConfiguration();
                            SPUtils lSPUtils = new SPUtils(USERNAME_SP);
                            String lValue = DES3Utils.encrypt3DESMode(loginName, context.getString(R.string.PASSWORD_CRYPT_KEY));
                            lSPUtils.put(KEY_SP, lValue);

                            getView().loginSuccess(userBean);
                        }
                    }
                });

        mCompositeSubscription.add(mSubscription);

    }

    @Deprecated
    private Observable<UserBean> getTicket(UserBean userBean, String userName, String MD5Password) {
        //M密码
//        String lMD5password = EncryptUtils.encryptMD5ToString(MD5Password + "{000000}").toLowerCase();
        //url
        String lPowerHostUrl = userBean.getPowerHostUrl();
        if (TextUtils.isEmpty(lPowerHostUrl)) {
            return Observable.just(userBean);
        } else {
            lPowerHostUrl += lPowerHostUrl.endsWith("/") ? "SignIn/" : "/SignIn/";
            userBean.setPowerHostUrl(lPowerHostUrl);
            return LoginDataManager.getTicket(userName, MD5Password, userBean.getPowerHostUrl(), userBean.getTicket())
                    .flatMap(new Func1<String, Observable<UserBean>>() {
                        @Override
                        public Observable<UserBean> call(String ticketBean) {
                            userBean.setTicket(ticketBean);
                            return Observable.just(userBean);
                        }
                    });
        }
    }


    public void getClassList(final UserBean userBean) {
        if (!isViewAttached())
            return;
        Subscription lSubscribe = ClassDataManager.getClassList(userBean.getUserId())
                .subscribe(classDisplayBeen -> {
                    if (getView() == null)
                        return;
                    getView().hideDialog();
                    if (classDisplayBeen != null) {
                        AppConfiguration.getInstance().setFirstIn(userBean);
                    } else {
                        getView().noRegisterClass(1, null, mUserBean);
                    }
                    AppConfiguration.getInstance().setUserLoginBean(userBean).saveAppConfiguration();
//                    getView().loginSuccess(userBean);
                    HomePageActivity.start(getView().getContext());
                }, throwable -> {

                    if (getView() == null)
                        return;
                    if (throwable instanceof CustomException) {
                        CustomException lErrorCodeException = (CustomException) throwable;
                        if (lErrorCodeException.getMessage().equals("无匹配的班级信息！")) {
                            getView().noRegisterClass(1, throwable.getMessage(), mUserBean);
                        }
                    }

                    getView().hideDialog();
                });

        mCompositeSubscription.add(lSubscribe);
    }


}
