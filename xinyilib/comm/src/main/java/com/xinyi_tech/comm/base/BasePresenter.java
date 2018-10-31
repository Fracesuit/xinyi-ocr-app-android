package com.xinyi_tech.comm.base;


import com.xinyi_tech.comm.CommCallBackListener;
import com.xinyi_tech.comm.rxjava1.DefaultSubscriber;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class BasePresenter {
    protected BaseView mView;

    public void attach(BaseView view) {
        mView = view;
    }

    public void destroy() {
        release();
        mView = null;
    }

    private Map<Integer, Subscription> subscriptionMap;

    protected <T> DefaultSubscriber<T> getDefaultSubscriber(final int requestCode) {
        return getDefaultSubscriber(requestCode, null);
    }

    protected <T> DefaultSubscriber<T> getDefaultSubscriber(final int requestCode, final CommCallBackListener<T> callBackListener) {
        return new DefaultSubscriber<T>(mView, requestCode) {
            @Override
            public void onNext(T t) {
                if (callBackListener != null) {
                    callBackListener.callBack(t);
                } else {
                    mView.doParseData(requestCode, t);
                }

            }
        };
    }

    protected <T> void execute(Observable<T> observable, final int requestCode, CommCallBackListener<T> callBackListener) {
        final DefaultSubscriber<T> observer = getDefaultSubscriber(requestCode, callBackListener);
        Subscription subscribe = observable
                .subscribeOn(Schedulers.io())//控制前面的
                .observeOn(AndroidSchedulers.mainThread())//控制后面的
                .compose(observer.bindLifecycle())//绑定生命周期
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        subscriptionMap.remove(requestCode);
                    }
                })
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        subscriptionMap.remove(requestCode);
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        observer.doOnStart();//主线程中进行，也可以在子线程中进行，由后面的subscribeOn来确定
                    }
                })
                .subscribe(observer);
        addSubscription(requestCode, subscribe);
    }

    protected <T> void execute(Observable<T> observable, final int requestCode) {
        execute(observable, requestCode, null);
    }

    /**
     * 任务取消 释放资源
     */
    private void release() {
        if (subscriptionMap != null) {
            cancelAll();
            subscriptionMap.clear();
            subscriptionMap = null;
        }
    }

    public Boolean hasHttp() {
        return subscriptionMap.size() > 0;
    }

    public Boolean hasHttp(Integer requestCode) {
        final Subscription subscription = subscriptionMap.get(requestCode);
        if (subscription != null && !subscription.isUnsubscribed()) {
            return true;
        }
        return false;
    }

    public Boolean cancel(Integer requestCode) {
        if (subscriptionMap != null) {
            final Subscription subscription = subscriptionMap.get(requestCode);
            if (subscription != null && !subscription.isUnsubscribed()) {
                subscription.unsubscribe();
                return true;
            }
        }
        return false;
    }

    public void cancelAll() {
        if (subscriptionMap != null) {
            final Set<Integer> integers = subscriptionMap.keySet();
            for (Integer key : integers) {
                cancel(key);
            }
        }
    }


    /**
     * 添加管理
     *
     * @param subscription
     */
    protected void addSubscription(int requestCode, Subscription subscription) {
        if (subscriptionMap == null) {
            subscriptionMap = new HashMap();
        }
        subscriptionMap.put(requestCode, subscription);
    }
}
