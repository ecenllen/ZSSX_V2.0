package com.gta.zssx.pub.util;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;
import com.gta.zssx.pub.base.BaseActivity;

/**
 * [Description]
 * <p/>
 * [How to use] 使用之后要把订阅关系添加到 {@link BaseActivity}的mCompositeSubscription集合中
 * <p/>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/7/7.
 * @since 1.0.0
 */
public class RxBus {

    private final Subject<Object, Object> BUS;

    private RxBus() {
        BUS = new SerializedSubject<>(PublishSubject.create());
    }

    public static RxBus getDefault() {
        return SINGLETON.sRxBus;
    }

    private static class SINGLETON {
        static final RxBus sRxBus = new RxBus();
    }

    //发射一个对象
    public void post(Object event) {
        BUS.onNext(event);
    }

    //监听一个对象
    public <T> Observable<T> toObserverable(Class<T> eventType) {
        // ofType = filter + cast
        return BUS.ofType(eventType);
    }

}