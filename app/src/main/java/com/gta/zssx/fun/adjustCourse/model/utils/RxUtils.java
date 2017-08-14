package com.gta.zssx.fun.adjustCourse.model.utils;

import rx.Observable;
import rx.Subscriber;


/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2017/4/6.
 * @since 1.0.0
 */
public class RxUtils {
    public static <K> Observable<K> createObservable(MyCallable<K> callable) {
        return Observable.unsafeCreate(new Observable.OnSubscribe<K>() {
            @Override
            public void call(Subscriber<? super K> subscriber) {
                try {
                    K lCall = callable.call();
                    subscriber.onNext(lCall);
                } catch (Exception e) {
                    subscriber.onError(e);
                }
                subscriber.onCompleted();
            }
        });
    }
}
