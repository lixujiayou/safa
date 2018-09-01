package com.inspur.hebeiline.utils.base;

import org.reactivestreams.Subscription;

/**
 * Created by lixu on 2018/6/21.
 */

public interface RxActionManager<T> {

    void add(T tag, Subscription subscription);
    void remove(T tag);

    void cancel(T tag);

    void cancelAll();
}