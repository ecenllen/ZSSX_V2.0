package com.gta.zssx.pub.http;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.gta.utils.resource.SysRes;
import com.gta.zssx.BuildConfig;
import com.gta.zssx.pub.exception.AssetException;
import com.gta.zssx.pub.exception.CustomException;
import com.gta.zssx.pub.exception.PatrolClassException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Jim.Huang on 2016/6/14.
 * @since 1.0.0
 */
public class HttpMethod {


    private static Retrofit mRetrofit;
    private static final int sTimeout = 10;
    private static Context sContext;
    private static String sUrl;

    private HttpMethod() {
    }

    public static void init(Context context, String url) {
        sContext = context;
        sUrl = url;
        getInstance();
    }

    private static class SINGLEINSTANCE {
        static HttpMethod sHttpMethod = new HttpMethod();
    }

    public static HttpMethod getInstance() {
        return SINGLEINSTANCE.sHttpMethod;
    }


    public <T> Observable<T> call(Observable<HttpResult<T>> observable) {


        return Observable.just(observable)
                //判断网络状态
                .doOnNext((Observable<HttpResult<T>> httpResultObservable) -> {
                    if (!SysRes.isConnected(sContext)) {
                        CustomException lCustomException = new CustomException();
                        lCustomException.setCode(CustomException.NETWORK_UNAVAILABLE);
                        lCustomException.setMessage("网络不可用，请稍候重试");
                        throw lCustomException;
//                            Observable.error(lCustomException);
                    }
                })
                //切换到io线程进行网络请求
                .observeOn(Schedulers.io())
                .flatMap(new Func1<Observable<HttpResult<T>>, Observable<HttpResult<T>>>() {
                    @Override
                    public Observable<HttpResult<T>> call(Observable<HttpResult<T>> httpResultObservable) {
                        return httpResultObservable;
                    }
                })
                //切换到主线程分发结果
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
                .compose(this.<HttpResult<T>>applySchedulers())
                .flatMap(new Func1<HttpResult<T>, Observable<T>>() {
                    @Override
                    public Observable<T> call(final HttpResult<T> tHttpResult) {
                        CustomException lCustomException = new CustomException();
                        PatrolClassException lPatrolClassException = new PatrolClassException();
                        AssetException assetException = new AssetException();
                        if (tHttpResult.getCode() == CustomException.FAIL) {
                            lCustomException.setCode(CustomException.FAIL);
                            lCustomException.setMessage(tHttpResult.getMessage());
                            return Observable.error(lCustomException);
                        }

                        if (tHttpResult.getCode() == CustomException.HAS_SIGN) {
                            lCustomException.setCode(CustomException.HAS_SIGN);
                            lCustomException.setMessage("你提交的节次已被登记，请重新选择");
                            return Observable.error(lCustomException);
                        }

                        if (tHttpResult.getCode() == CustomException.NO_MORE_RECORD) {
                            lCustomException.setCode(CustomException.NO_MORE_RECORD);
                            lCustomException.setMessage("无更多结果");
                            return Observable.error(lCustomException);
                        }

                        if (tHttpResult.getCode() == CustomException.HAS_ADD) {
                            lCustomException.setCode(CustomException.HAS_ADD);
                            lCustomException.setMessage("已有班级被添加，请重新选择");
                            return Observable.error(lCustomException);
                        }

                        if (tHttpResult.getCode() == CustomException.HAS_ADD_ALL) {
                            lCustomException.setCode(CustomException.HAS_ADD);
                            lCustomException.setMessage("班级已被添加，请重新选择");
                            return Observable.error(lCustomException);
                        }

                        if (tHttpResult.getCode() == PatrolClassException.DATA_IS_EMPTY) {
                            lPatrolClassException.setCode(PatrolClassException.DATA_IS_EMPTY);
                            return Observable.error(lPatrolClassException);
                        }

                        if (tHttpResult.getCode() == PatrolClassException.DATA_IS_NULL) {
                            lPatrolClassException.setCode(PatrolClassException.DATA_IS_NULL);
                            return Observable.error(lPatrolClassException);
                        }

                        if (tHttpResult.getCode() == PatrolClassException.HAS_PATROL_CLASS) {
                            lPatrolClassException.setCode(PatrolClassException.HAS_PATROL_CLASS);
                            return Observable.error(lPatrolClassException);
                        }

                        if (tHttpResult.getCode() == PatrolClassException.HAS_CLASS_ADD) {
                            lPatrolClassException.setCode(PatrolClassException.HAS_CLASS_ADD);
                            return Observable.error(lPatrolClassException);
                        }

                        if (tHttpResult.getCode() == PatrolClassException.HAS_CLASS_ADD_ALL) {
                            lPatrolClassException.setCode(PatrolClassException.HAS_CLASS_ADD_ALL);
                            return Observable.error(lPatrolClassException);
                        }

                        if (tHttpResult.getCode() == PatrolClassException.IS_SUBMIT_NOW) {
                            lPatrolClassException.setCode(PatrolClassException.IS_SUBMIT_NOW);
                            return Observable.error(lPatrolClassException);
                        }

                        if (tHttpResult.getCode() == AssetException.ASSET_NOT_IN_FROM_NUM) {
                            assetException.setCode(AssetException.ASSET_NOT_IN_FROM_NUM);
                            return Observable.error(assetException);
                        }

                        if (tHttpResult.getCode()!=0){
                            lCustomException.setCode(tHttpResult.getCode());
                            lCustomException.setMessage(tHttpResult.getMessage());
                            return Observable.error(lCustomException);
                        }

                        return Observable.create(subscriber -> {
                            if (tHttpResult.getData() == null) {
                                subscriber.onNext(null);
                                subscriber.onCompleted();
                                return;
                            }
                            subscriber.onNext(tHttpResult.getData());
                            subscriber.onCompleted();
                        });
//                        return Observable.just(tHttpResult.getData());
                    }
                });

    }

    @NonNull
    private <T> Action1<Observable<HttpResult<T>>> doOnNetWork() {
        return (Observable<HttpResult<T>> httpResultObservable) -> {
            if (!SysRes.isConnected(sContext)) {
                CustomException lCustomException = new CustomException();
                lCustomException.setCode(CustomException.NETWORK_UNAVAILABLE);
                lCustomException.setMessage("网络不可用，请稍候重试...");
                throw lCustomException;
//                            Observable.error(lCustomException);
            }
        };
    }

    //线程切换方法
    public <H> Observable.Transformer<H, H> applySchedulers() {
        return (Observable<H> observable) -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Retrofit retrofitClient(String url, int timeout) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        //  Log信息拦截器
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            //设置 Debug Log 模式
            builder.addInterceptor(loggingInterceptor);
        }

        //参数拦截用于公共参数配置和参数加密
        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request lRequest = chain.request();
                //拦截url里面的参数，如果为空则拦截body里面的
                if (BuildConfig.DEBUG) {
                    String lQuery = lRequest.url().query();
                    if (lQuery != null) {
                        Log.i("Parameter", lQuery);
                    } else {
                        if (lRequest.method().equals("POST")) {
                            Buffer buffer = new Buffer();
                            RequestBody lBody = lRequest.body();
                            lBody.writeTo(buffer);
                            String lS = buffer.readUtf8();

                            if (lS != null) {
                                Log.i("Parameter...POST", lS);
                            }
//                        RequestBody lRequestBody = RequestBody.create(MediaType.parse("application/json"), lS);
//                        Request newRequest = lRequest.newBuilder()
//                                .method(lRequest.method(), lRequestBody)
//                                .build();
                        }
                    }
                }

                return chain.proceed(lRequest);
            }
        };
        //网络不好时会导致莫名崩溃
//        builder.addInterceptor(headerInterceptor);
        //设置超时时间
        builder.connectTimeout(timeout, TimeUnit.SECONDS);
        builder.readTimeout(timeout, TimeUnit.SECONDS);

        //错误重连
        builder.retryOnConnectionFailure(true);

        if (null != url && !url.isEmpty()) {
            OkHttpClient okHttpClient = builder.build();
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    //设置 Json 转换器
                    .addConverterFactory(GsonConverterFactory.create())
                    //RxJava 适配器
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(okHttpClient)
                    .build();
        }

        return mRetrofit;
    }

    public static void setmRetrofit(Retrofit mRetrofit) {

        HttpMethod.mRetrofit = mRetrofit;

    }

    public Retrofit retrofitClient(String url) {

        return retrofitClient(url, sTimeout);

    }

}
