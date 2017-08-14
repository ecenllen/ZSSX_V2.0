package com.gta.zssx;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;

import com.blankj.utilcode.util.Utils;
import com.gta.utils.resource.L;
import com.gta.utils.resource.Toast;
import com.gta.utils.thirdParty.jPush.JpushManager;
import com.gta.zssx.fun.adjustCourse.view.base.AdjustCourse;
import com.gta.zssx.fun.assetmanagement.view.base.AssetManagement;
import com.gta.zssx.fun.coursedaily.registercourse.view.base.CourseDaily;
import com.gta.zssx.fun.personalcenter.model.bean.ServerBean;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.fun.personalcenter.view.base.PersonalCenter;
import com.gta.zssx.fun.personalcenter.view.page.LoginActivity;
import com.gta.zssx.main.HomePageActivity;
import com.gta.zssx.patrolclass.view.base.PatrolClass;
import com.gta.zssx.pub.InterfaceList;
import com.gta.zssx.pub.exception.CustomException;
import com.gta.zssx.pub.http.HttpMethod;
import com.gta.zssx.pub.http.HttpResult;
import com.gta.zssx.pub.manager.DownloadManager;
import com.gta.zssx.pub.service.GrayService;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.concurrent.TimeUnit;

import cn.sharesdk.framework.ShareSDK;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/16.
 * @since 1.0.0
 */
public class StartActivity extends AppCompatActivity {

    public CompositeSubscription mCompositeSubscription;
    private Context mAppContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        mAppContext = getApplicationContext();
        initApplication();



      /*  if (!isTaskRoot()) {
            FINISH();
            return;
        }*/

        mCompositeSubscription = new CompositeSubscription();


        try {
            AppConfiguration.SERVERURL = AppConfiguration.getInstance().getServerAddress().getCourseDailyUrl();
            final UserBean lUserBean = AppConfiguration.getInstance().getUserBean();
//            HttpMethod.getInstance().setmRetrofit(null);
            Subscription lSubscribe = Observable.mergeDelayError(userLogin(lUserBean.getAccount(), lUserBean.getMD5Password(), AppConfiguration.getInstance().getServerAddress().getCourseDailyUrl()),
                    Observable.timer(2, TimeUnit.SECONDS).map(new Func1<Long, UserBean>() {
                        @Override
                        public UserBean call(Long aLong) {
                            return null;
                        }
                    }))
                    .compose(HttpMethod.getInstance().<UserBean>applySchedulers())
                    //2秒后采样，产生userBean，null，userBean->onNext,null->onComplete,或者userBean出错,null,->onError
//                    .sample(2, TimeUnit.SECONDS)
                    .flatMap(new Func1<UserBean, Observable<UserBean>>() {
                        @Override
                        public Observable<UserBean> call(UserBean userBean) {
                            if (userBean == null) {
                                return Observable.empty();
                            } else {
                                return Observable.just(userBean);
                            }
                        }
                    }).subscribe(userBean -> {

                    }, throwable -> {
                        if (throwable instanceof CustomException) {
                            CustomException lE = (CustomException) throwable;
                            if (lE.getMessage().equals(StartActivity.this.getString(R.string.account_or_password_wrong))) {
                                Toast.Long(StartActivity.this, getString(R.string.password_error));
                            }
                        }
                        LoginActivity.start(StartActivity.this);
                        finish();
//                        AppConfiguration.getInstance().setUserLoginBean(null).setFirstIn(null).saveAppConfiguration();
                    }, () -> {
//                        if (AppConfiguration.getInstance().isFistIn()) {
//                            ClassChooseActivity.start4Result(StartActivity.this);
//                        } else {
//                            CourseDailyActivity.start4Result(StartActivity.this);
//                        }
                        HomePageActivity.start(this);
                        finish();
                    });

            mCompositeSubscription.add(lSubscribe);

        } catch (Exception e) {
            mCompositeSubscription.add(Observable.timer(2, TimeUnit.SECONDS)
                    .subscribe(new Subscriber<Long>() {
                        @Override
                        public void onCompleted() {
                            LoginActivity.start(StartActivity.this);
                            finish();
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(Long aLong) {

                        }
                    }));
        }

    }

    private void initApplication() {
        if (LeakCanary.isInAnalyzerProcess (mAppContext)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install ((Application) mAppContext);

//        init(mAppContext);

        ServerBean lServerAddress = AppConfiguration.getInstance ().getServerAddress ();
        if (lServerAddress != null) {
            PersonalCenter.getInstance ().init (mAppContext, lServerAddress.getCourseDailyUrl ());
            CourseDaily.getInstance ().init (mAppContext, lServerAddress.getCourseDailyUrl ());
            PatrolClass.getInstance ().init (mAppContext, lServerAddress.getPatrolClassUrl ());
            AssetManagement.getInstance ().init (mAppContext, lServerAddress.getAssetUrl ());
            AdjustCourse.getInstance ().init (mAppContext, lServerAddress.getAdjustCourseUrl ());
        }

        //初始化下载downloadManager
        DownloadManager.getInstance ().init (mAppContext);

    }

    public Observable<UserBean> userLogin(String loginName, String password, String serverAddress) {

        Observable<HttpResult<UserBean>> lHttpResultObservable = HttpMethod
                .getInstance()
                .retrofitClient(serverAddress, 3)
                .create(InterfaceList.class)
                .userLogin(loginName, password);

        return HttpMethod.getInstance().call(lHttpResultObservable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
        mAppContext = null;
    }
}
