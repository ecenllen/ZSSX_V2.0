package com.gta.zssx.pub.http;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import com.gta.utils.helper.Helper_Sp;
import com.gta.utils.resource.SysRes;
import com.gta.zssx.pub.exception.CustomException;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * [Description]
 * <p> 三级缓存网络请求
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/7/12.
 * @since 1.0.0
 */
public class ModelSource<T> {

    private T mData;
    private Observable<HttpResult<T>> mHttpResultObservable;
    private int mMode;
    private Context mContext;
    private Func1<Observable<HttpResult<T>>, T> mGet4MemoryFun;
    private Action1<T> mSave2MemoryAction;

    private Action1<Observable<HttpResult<T>>> mBeforeMemoryAction;
    private Action1<T> mAfterMemoryAction;

    private Action1<Observable<HttpResult<T>>> mBeforeDiskAction;
    private Action1<T> mAfterDiskAction;

    private Func1<Observable<HttpResult<T>>, T> mGetFromDiskFunc;

    private Func1<T, Boolean> mIsDataValid;

    public String mDiskKey;


    @IntDef({MODE_MDN, MODE_N, MODE_NMD, MODE_MND, MODE_DNM, MODE_MN})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SourceMode {

    }

    /**
     * 数据获取顺序：M(内存缓存)、N(网络获取)、D（本地存储）
     */
    public static final int MODE_MND = 1;
    /**
     * 数据获取顺序：N(网络获取)
     */
    public static final int MODE_N = 2;
    /**
     * 数据获取顺序：M(内存缓存)、D（本地存储）、N(网络获取)
     */
    public static final int MODE_MDN = 3;
    /**
     * 数据获取顺序：N(网络获取)、M(内存缓存)、D（本地存储）
     */
    public static final int MODE_NMD = 4;
    /**
     * 数据获取顺序：D（本地存储）、N(网络获取)、M(内存缓存)
     */
    public static final int MODE_DNM = 5;
    /**
     * 数据获取顺序：M(内存缓存)、N(网络获取)
     */
    public static final int MODE_MN = 6;


    /**
     * 构造方法
     *
     * @param context              上下文
     * @param httpResultObservable 用retrofit构造的Observable对象
     */
    public ModelSource(Context context, Observable<HttpResult<T>> httpResultObservable) {
        mHttpResultObservable = httpResultObservable;
        mContext = context;
    }


    /**
     * 创建获得目标对象结果观察者
     *
     * @return 目标对象结果观察者
     */
    public Observable<T> Observable() {
        Action1<Observable<HttpResult<T>>> lEmptyBeforeAction = getEmptyBeforeAction();
        Action1<T> lEmptyAfterAction = getEmptyAfterAction();

         /*///////////默认是空的钩子方法///////////////////////*/
        if (mBeforeDiskAction == null) {
            mBeforeDiskAction = lEmptyBeforeAction;
        }

        if (mBeforeMemoryAction == null) {
            mBeforeMemoryAction = lEmptyBeforeAction;
        }

        if (mAfterDiskAction == null) {
            mAfterDiskAction = lEmptyAfterAction;
        }

        if (mAfterMemoryAction == null) {
            mAfterMemoryAction = lEmptyAfterAction;
        }

        if (mSave2MemoryAction == null) {
            mSave2MemoryAction = t -> save2Memory(t);
        }

        if (mGet4MemoryFun == null) {
            mGet4MemoryFun = httpResultObservable -> getFromMemory(httpResultObservable);
        }

        final Observable<T> lNetwork = Observable.just(mHttpResultObservable)
                .doOnNext(httpResultObservable -> {
                    if (!SysRes.isConnected(mContext)) {
                        CustomException lCustomException = new CustomException();
                        lCustomException.setCode(CustomException.NETWORK_UNAVAILABLE);
                        lCustomException.setMessage("网络不可用，请稍候重试...");
                        throw lCustomException;
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(httpResultObservable -> httpResultObservable)
                .observeOn(Schedulers.io())
                .flatMap(getDispatchFunc())
                //sharepreference缓存
                .doOnNext(t -> {
                    if (t != null) {
                        switch (mMode) {
                            default:
                            case MODE_MN:
                            case MODE_N:
                                break;
                            case MODE_DNM:
                            case MODE_MDN:
                            case MODE_MND:
                            case MODE_NMD:
                                save2Disk(t);
                                break;
                        }
                    }
                })
                //内存缓存
                .doOnNext(mSave2MemoryAction);

        final Observable<T> lMemory = Observable.just(mHttpResultObservable)
                .observeOn(Schedulers.io())
                //设置前置钩子
                .doOnNext(mBeforeMemoryAction)
                .map(mGet4MemoryFun)
                //设置后置钩子
                .doOnNext(mAfterMemoryAction);


        if (mGetFromDiskFunc == null) {
            mGetFromDiskFunc = httpResultObservable -> {
                T t = (T) Helper_Sp.readObject(mDiskKey, mContext);
                return t;
            };
        }

        final Observable<T> lDisk = Observable.just(mHttpResultObservable)
                .observeOn(Schedulers.io())
                //设置前置钩子
                .doOnNext(mBeforeDiskAction)
                .observeOn(Schedulers.io())
                .map(mGetFromDiskFunc)
                .doOnNext(mSave2MemoryAction)
                //设置后置钩子
                .doOnNext(mAfterDiskAction);


          /*///////////根据模式复合功能///////////////////////*/
        Func1<Observable<HttpResult<T>>, Observable<T>> lAllFun = httpResultObservable -> {
            final Observable<T> lConcat;
            switch (mMode) {
                case MODE_MDN:
                    lConcat = Observable
                            .concat(lMemory, lDisk, lNetwork);
                    break;
                case MODE_NMD:
                    lConcat = Observable
                            .concat(lNetwork, lMemory, lDisk);
                    break;
                case MODE_N:
                    lConcat = lNetwork;
                    break;
                case MODE_DNM:
                    lConcat = Observable.concat(lDisk, lNetwork, lMemory);
                    break;
                case MODE_MND:
                default:
                    lConcat = Observable
                            .concat(lMemory, lNetwork, lDisk);
                    break;
            }
            return lConcat.first(mIsDataValid);
        };

        return Observable
                .just(mHttpResultObservable)
                .flatMap(lAllFun)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @NonNull
    private Func1<HttpResult<T>, Observable<T>> getDispatchFunc() {
        return tHttpResult -> {
            if (tHttpResult.getCode() == CustomException.FAIL) {
                CustomException lCustomException = new CustomException();
                lCustomException.setCode(CustomException.FAIL);
                lCustomException.setMessage(tHttpResult.getMessage());
                return Observable.error(lCustomException);
            }
            if (tHttpResult.getCode() == CustomException.NO_MORE_RECORD) {
                CustomException lCustomException = new CustomException();
                lCustomException.setCode(CustomException.NO_MORE_RECORD);
                lCustomException.setMessage(tHttpResult.getMessage());
                return Observable.error(lCustomException);
            }

            return Observable.create(new Observable.OnSubscribe<T>() {
                @Override
                public void call(Subscriber<? super T> subscriber) {
                    if (tHttpResult.getData() == null) {
                        subscriber.onNext(null);
                        subscriber.onCompleted();
                        return;
                    }
                    subscriber.onNext(tHttpResult.getData());
                    subscriber.onCompleted();
                }
            });
        };
    }

    private void save2Disk(T tHttpResult) {
        Helper_Sp.saveObject(tHttpResult, mDiskKey, mContext);
    }

    /**
     * 线程切换方法
     */
    public <H> Observable.Transformer<H, H> applySchedulers() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 设置数据请求来源的模式
     *
     * @param mode 有{@link #MODE_MDN}、{@link #MODE_MND}、{@link #MODE_N}、{@link #MODE_NMD}这几种可选，具体参见各自说明。
     *             一般请求数据使用{@link #MODE_MND}，上传数据使用{@link #MODE_N}
     * @return 链式方法
     */
    public ModelSource<T> setMode(@SourceMode int mode) {
        mMode = mode;
        return this;
    }

    /**
     * 设置硬盘存储的key,设置硬盘缓存必须调用
     *
     * @param name 用于保存数据的key
     * @return 链式方法
     */
    public ModelSource<T> setInterfaceName(String name) {
        mDiskKey = name;
        return this;
    }

    /**
     * 设置【内存缓存】功能实现
     *
     * @param get4Memory  【内存缓存】保存功能实现
     * @param save2Memory 【内存缓存】恢复功能实现
     */
    public ModelSource<T> setMemoryFun(Func1<Observable<HttpResult<T>>, T> get4Memory, Action1<T> save2Memory) {
        this.mGet4MemoryFun = get4Memory;
        this.mSave2MemoryAction = save2Memory;
        return this;
    }

    /**
     * 设置【本地存储】操作过程的前后钩子
     *
     * @param beforeAction 前置钩子
     * @param afterAction  后置钩子
     * @return 链式方法
     */
    public ModelSource<T> setHook4Memory(Action1<Observable<HttpResult<T>>> beforeAction, Action1<T> afterAction) {
        this.mBeforeMemoryAction = beforeAction;
        mAfterMemoryAction = afterAction;
        return this;
    }

    /**
     * 设置【本地存储】操作过程的前后钩子
     *
     * @param beforeAction 前置钩子
     * @param afterAction  后置钩子
     * @return 链式方法
     */
    public ModelSource<T> setHook4Disk(Action1<Observable<HttpResult<T>>> beforeAction, Action1<T> afterAction) {
        this.mBeforeDiskAction = beforeAction;
        mAfterDiskAction = afterAction;
        return this;
    }

    /**
     * 设置【判断数据有效性】的操作
     *
     * @param isDataValid 【判断数据有效性】的操作
     * @return 链式方法
     */
    public ModelSource<T> setIsDataValidFun(Func1<T, Boolean> isDataValid) {
        mIsDataValid = isDataValid;
        return this;
    }

    @NonNull
    private Action1<Observable<HttpResult<T>>> getEmptyBeforeAction() {
        return H -> {
            //不做任何处理
        };
    }


    /**
     * 不错任何处理
     *
     * @return
     */
    public Action1<T> getEmptyAfterAction() {
        return t -> {

        };
    }

    private void save2Memory(T t) {
        mData = t;
    }

    private T getFromMemory(Observable<HttpResult<T>> h) {
        return mData;
    }

}
